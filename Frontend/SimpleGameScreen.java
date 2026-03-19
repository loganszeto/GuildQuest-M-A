package Frontend;

import Backend.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * Simple test game screen that bypasses the map display issues
 */
public class SimpleGameScreen extends JPanel {
    private GMAEGUI mainGUI;
    private TwoPlayerGameManager gameManager;
    private MiniAdventure currentAdventure;
    
    // UI Components
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JLabel gameInfoLabel;
    private JTextArea gameLog;
    private JButton backButton;
    private JButton resetButton;
    private JButton rulesButton;
    
    // Game state
    private boolean gameActive = false;
    
    public SimpleGameScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.gameManager = new TwoPlayerGameManager();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFocusable(true);
    }
    
    private void initializeComponents() {
        // Status labels
        statusLabel = new JLabel("Welcome to GuildQuest Mini-Adventure!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 215, 0));
        
        currentPlayerLabel = new JLabel("Current Player: None", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gameInfoLabel = new JLabel("Select an adventure from the menu to begin", JLabel.CENTER);
        gameInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Game log
        gameLog = new JTextArea(10, 30);
        gameLog.setEditable(false);
        gameLog.setBackground(new Color(30, 30, 50));
        gameLog.setForeground(Color.WHITE);
        gameLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Buttons
        backButton = new JButton("Back to Menu");
        resetButton = new JButton("Reset Game");
        rulesButton = new JButton("Show Rules (R)");
        
        styleButton(backButton);
        styleButton(resetButton);
        styleButton(rulesButton);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 40));
        
        // Top panel for status
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setBackground(new Color(20, 20, 40));
        topPanel.add(statusLabel);
        topPanel.add(currentPlayerLabel);
        topPanel.add(gameInfoLabel);
        
        // Center panel for game log
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(20, 20, 40));
        bottomPanel.add(backButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(rulesButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Set preferred size
        setPreferredSize(new Dimension(600, 500));
    }
    
    private void setupEventHandlers() {
        // Back button
        backButton.addActionListener(e -> {
            gameActive = false;
            mainGUI.showAdventureMenu();
        });
        
        // Reset button
        resetButton.addActionListener(e -> {
            if (gameManager != null && currentAdventure != null) {
                gameManager.resetGame();
                updateDisplay();
                requestFocus(); // Reclaim focus for keyboard input
            }
        });
        
        // Rules button
        rulesButton.addActionListener(e -> showGameRules());
        
        // Keyboard input handler
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }
    
    private void handleKeyPress(KeyEvent e) {
        if (!gameActive || gameManager == null || currentAdventure == null) {
            return;
        }
        
        // Handle rules display toggle
        if (e.getKeyCode() == KeyEvent.VK_R) {
            showGameRules();
            return;
        }
        
        // Handle movement input
        String input = "";
        String direction = "";
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                input = "w";
                direction = "UP";
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                input = "s";
                direction = "DOWN";
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                input = "a";
                direction = "LEFT";
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                input = "d";
                direction = "RIGHT";
                break;
            default:
                return; // Ignore other keys
        }
        
        // Process the input
        boolean success = gameManager.processInput(input);
        if (success) {
            User currentPlayer = gameManager.getCurrentPlayer();
            appendToGameLog(currentPlayer.getUsername() + " moved " + direction);
            updateDisplay();
            
            // Check for game completion
            if (currentAdventure.isComplete()) {
                handleGameComplete();
            }
        } else {
            appendToGameLog("Invalid move!");
        }
    }
    
    private void showGameRules() {
        if (currentAdventure == null) {
            JOptionPane.showMessageDialog(this, 
                "No adventure selected. Please choose an adventure from the menu first.",
                "No Adventure", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String rules = getGameRules();
        JOptionPane.showMessageDialog(this, 
            rules,
            "Game Rules - " + currentAdventure.getName(),
            JOptionPane.INFORMATION_MESSAGE);
        
        requestFocus(); // Reclaim focus after dialog
    }
    
    private String getGameRules() {
        if (currentAdventure == null) {
            return "No adventure selected.";
        }
        
        StringBuilder rules = new StringBuilder();
        rules.append("=== ").append(currentAdventure.getName()).append(" ===\n\n");
        rules.append("OBJECTIVE:\n");
        rules.append(currentAdventure.getDescription()).append("\n\n");
        
        rules.append("CONTROLS:\n");
        rules.append("• Use W/↑ to move UP\n");
        rules.append("• Use S/↓ to move DOWN\n");
        rules.append("• Use A/← to move LEFT\n");
        rules.append("• Use D/→ to move RIGHT\n");
        rules.append("• Press R to show these rules\n\n");
        
        rules.append("GAME MODE:\n");
        if (gameManager != null && gameManager.isCompetitive()) {
            rules.append("• Competitive: Player with most relics wins!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        } else {
            rules.append("• Co-op: Work together to collect all relics!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        }
        
        rules.append("\nWIN CONDITION:\n");
        rules.append("• Collect all relics to complete the adventure\n");
        rules.append("• Avoid enemies and obstacles\n");
        
        return rules.toString();
    }
    
    private void handleGameComplete() {
        gameActive = false;
        
        int winner = currentAdventure.getWinner();
        String message;
        String title;
        
        if (gameManager.isCompetitive()) {
            if (winner == 1) {
                message = "Player 1 Wins!\n\n" + getPlayerStats(1);
                title = "Victory!";
            } else if (winner == 2) {
                message = "Player 2 Wins!\n\n" + getPlayerStats(2);
                title = "Victory!";
            } else {
                message = "It's a Tie!\n\n" + getPlayerStats(0);
                title = "Game Complete!";
            }
        } else {
            message = "Co-op Adventure Complete!\n\n" + getPlayerStats(0);
            title = "Mission Accomplished!";
        }
        
        // Show completion dialog
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        
        // Update status
        statusLabel.setText(title);
        gameInfoLabel.setText("Game Complete! Press Reset to play again or Back to menu.");
        appendToGameLog("=== " + title + " ===");
    }
    
    private String getPlayerStats(int playerNum) {
        if (gameManager == null) return "";
        
        Map<String, Object> state = gameManager.getCurrentState();
        Map<String, Object> adventureState = (Map<String, Object>) state.get("adventureState");
        
        if (adventureState == null) return "";
        
        StringBuilder stats = new StringBuilder();
        
        if (playerNum == 0) {
            // Co-op mode - show both players
            stats.append("Team Stats:\n");
            stats.append("Relics Collected: ").append(adventureState.get("relicsCollected")).append("/").append(adventureState.get("totalRelics"));
        } else {
            // Competitive mode - show winner
            User player = playerNum == 1 ? gameManager.getPlayer1() : gameManager.getPlayer2();
            stats.append("Player ").append(playerNum).append(" (").append(player.getUsername()).append(")\n");
            stats.append("Achievements: ").append(player.getAchievements().size());
        }
        
        return stats.toString();
    }
    
    public void startAdventure(MiniAdventure adventure, User player1, User player2, boolean competitive) {
        this.currentAdventure = adventure;
        
        // Setup game
        boolean success = gameManager.setupGame(player1, player2, adventure, competitive);
        if (success) {
            gameActive = true;
            
            // Clear and setup game log
            gameLog.setText("");
            appendToGameLog("=== " + adventure.getName() + " Started! ===");
            appendToGameLog("Mode: " + (competitive ? "Competitive" : "Co-op"));
            appendToGameLog("Player 1: " + player1.getUsername());
            appendToGameLog("Player 2: " + player2.getUsername());
            appendToGameLog("Use WASD or Arrow Keys to move");
            appendToGameLog("Press R for rules");
            
            // Update display
            updateDisplay();
            
            // Show rules at start
            SwingUtilities.invokeLater(() -> {
                showGameRules();
                requestFocus(); // Ensure keyboard focus
            });
        }
    }
    
    private void updateDisplay() {
        if (gameManager == null || currentAdventure == null) return;
        
        Map<String, Object> state = gameManager.getCurrentState();
        Map<String, Object> adventureState = (Map<String, Object>) state.get("adventureState");
        
        if (adventureState != null) {
            // Update current player
            int currentTurn = (Integer) state.get("currentPlayerTurn");
            User currentPlayer = gameManager.getCurrentPlayer();
            currentPlayerLabel.setText("Current Player: " + currentPlayer.getUsername() + " (Player " + currentTurn + ")");
            
            // Update game info
            if (gameActive) {
                int relicsCollected = (Integer) adventureState.get("relicsCollected");
                int totalRelics = (Integer) adventureState.get("totalRelics");
                gameInfoLabel.setText("Relics: " + relicsCollected + "/" + totalRelics + " | Mode: " + 
                    (gameManager.isCompetitive() ? "Competitive" : "Co-op"));
            }
        }
    }
    
    private void appendToGameLog(String message) {
        gameLog.append(message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }
    
    @Override
    public void requestFocus() {
        super.requestFocus();
        SwingUtilities.invokeLater(() -> {
            setFocusable(true);
            requestFocusInWindow();
        });
    }
}
