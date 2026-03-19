package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Ultra-simple test game that demonstrates all the features without dependencies
 */
public class TestGameScreen extends JPanel {
    private TestGMAEGUI mainGUI;
    
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
    private TestUser player1;
    private TestUser player2;
    private int currentPlayer = 1;
    private boolean competitive = false;
    
    // Simple game state
    private int player1X = 0, player1Y = 0;
    private int player2X = 9, player2Y = 9;
    private int relicsCollected = 0;
    private final int totalRelics = 5;
    private final int boardSize = 10;
    private final Random random = new Random();
    
    // Relic positions
    private boolean[][] relics = new boolean[boardSize][boardSize];
    
    public TestGameScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFocusable(true);
    }
    
    private void initializeComponents() {
        // Status labels
        statusLabel = new JLabel("Welcome to GuildQuest Test Adventure!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 215, 0));
        
        currentPlayerLabel = new JLabel("Current Player: None", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gameInfoLabel = new JLabel("Select an adventure from the menu to begin", JLabel.CENTER);
        gameInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Game log
        gameLog = new JTextArea(12, 40);
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
        
        // Center panel - game board and log
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(20, 20, 40));
        
        // Game board
        JPanel boardPanel = createGameBoard();
        centerPanel.add(boardPanel, BorderLayout.WEST);
        
        // Game log
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(20, 20, 40));
        bottomPanel.add(backButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(rulesButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Set preferred size
        setPreferredSize(new Dimension(900, 600));
    }
    
    private JPanel createGameBoard() {
        JPanel board = new JPanel(new GridLayout(boardSize, boardSize));
        board.setBackground(new Color(20, 20, 40));
        board.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
            "Game Board",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(255, 215, 0)
        ));
        
        updateBoard(board);
        
        return board;
    }
    
    private void updateBoard(JPanel board) {
        board.removeAll();
        
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                JLabel cell = new JLabel("", JLabel.CENTER);
                cell.setOpaque(true);
                cell.setPreferredSize(new Dimension(40, 40));
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                
                // Determine cell content
                if (x == player1X && y == player1Y) {
                    cell.setText("P1");
                    cell.setBackground(new Color(100, 100, 255)); // Blue
                    cell.setForeground(Color.WHITE);
                } else if (x == player2X && y == player2Y) {
                    cell.setText("P2");
                    cell.setBackground(new Color(255, 100, 100)); // Red
                    cell.setForeground(Color.WHITE);
                } else if (relics[x][y]) {
                    cell.setText("R");
                    cell.setBackground(new Color(255, 215, 0)); // Gold
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setBackground(new Color(50, 50, 50)); // Dark gray
                }
                
                board.add(cell);
            }
        }
        
        board.revalidate();
        board.repaint();
    }
    
    private void setupEventHandlers() {
        // Back button
        backButton.addActionListener(e -> {
            gameActive = false;
            mainGUI.showAdventureMenu();
        });
        
        // Reset button
        resetButton.addActionListener(e -> {
            resetGame();
            requestFocus();
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
        if (!gameActive) return;
        
        // Handle rules display toggle
        if (e.getKeyCode() == KeyEvent.VK_R) {
            showGameRules();
            return;
        }
        
        // Handle movement input
        String direction = "";
        int newX = 0, newY = 0;
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = "UP";
                newX = currentPlayer == 1 ? player1X : player2X;
                newY = currentPlayer == 1 ? player1Y - 1 : player2Y - 1;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                direction = "DOWN";
                newX = currentPlayer == 1 ? player1X : player2X;
                newY = currentPlayer == 1 ? player1Y + 1 : player2Y + 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = "LEFT";
                newX = currentPlayer == 1 ? player1X - 1 : player2X - 1;
                newY = currentPlayer == 1 ? player1Y : player2Y;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = "RIGHT";
                newX = currentPlayer == 1 ? player1X + 1 : player2X + 1;
                newY = currentPlayer == 1 ? player1Y : player2Y;
                break;
            default:
                return; // Ignore other keys
        }
        
        // Check bounds
        if (newX < 0 || newX >= boardSize || newY < 0 || newY >= boardSize) {
            appendToGameLog("Cannot move outside the board!");
            return;
        }
        
        // Check collision with other player
        if ((currentPlayer == 1 && newX == player2X && newY == player2Y) ||
            (currentPlayer == 2 && newX == player1X && newY == player1Y)) {
            appendToGameLog("Cannot move to the same space as the other player!");
            return;
        }
        
        // Process the move
        if (currentPlayer == 1) {
            player1X = newX;
            player1Y = newY;
        } else {
            player2X = newX;
            player2Y = newY;
        }
        
        TestUser currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        appendToGameLog(currentPlayerUser.getUsername() + " moved " + direction);
        
        // Check for relic collection
        if (relics[newX][newY]) {
            relics[newX][newY] = false;
            relicsCollected++;
            appendToGameLog(currentPlayerUser.getUsername() + " collected a relic!");
            
            if (currentPlayer == 1) {
                player1.addAchievement("Relic Collected!");
            } else {
                player2.addAchievement("Relic Collected!");
            }
        }
        
        // Update display
        updateDisplay();
        
        // Check for game completion
        if (relicsCollected >= totalRelics) {
            handleGameComplete();
        } else {
            // Switch turns
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            updateDisplay();
        }
        
        requestFocus();
    }
    
    private void showGameRules() {
        String rules = getGameRules();
        JOptionPane.showMessageDialog(this, 
            rules,
            "Game Rules - Test Adventure",
            JOptionPane.INFORMATION_MESSAGE);
        
        requestFocus();
    }
    
    private String getGameRules() {
        StringBuilder rules = new StringBuilder();
        rules.append("=== Test Adventure ===\n\n");
        rules.append("OBJECTIVE:\n");
        rules.append("Collect all relics (R) on the board!\n\n");
        
        rules.append("CONTROLS:\n");
        rules.append("• Use W/↑ to move UP\n");
        rules.append("• Use S/↓ to move DOWN\n");
        rules.append("• Use A/← to move LEFT\n");
        rules.append("• Use D/→ to move RIGHT\n");
        rules.append("• Press R to show these rules\n\n");
        
        rules.append("GAME MODE:\n");
        if (competitive) {
            rules.append("• Competitive: Player with most relics wins!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        } else {
            rules.append("• Co-op: Work together to collect all relics!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        }
        
        rules.append("\nWIN CONDITION:\n");
        rules.append("• Collect all 5 relics to complete the adventure\n");
        rules.append("• Blue (P1) and Red (P2) are the players\n");
        rules.append("• Gold (R) are the relics to collect\n");
        
        return rules.toString();
    }
    
    private void handleGameComplete() {
        gameActive = false;
        
        String message;
        String title;
        
        if (competitive) {
            int p1Achievements = player1.getAchievements().size();
            int p2Achievements = player2.getAchievements().size();
            
            if (p1Achievements > p2Achievements) {
                message = "Player 1 Wins!\n\nPlayer 1 collected " + p1Achievements + " relics\nPlayer 2 collected " + p2Achievements + " relics";
                title = "Victory!";
            } else if (p2Achievements > p1Achievements) {
                message = "Player 2 Wins!\n\nPlayer 1 collected " + p1Achievements + " relics\nPlayer 2 collected " + p2Achievements + " relics";
                title = "Victory!";
            } else {
                message = "It's a Tie!\n\nBoth players collected " + p1Achievements + " relics";
                title = "Game Complete!";
            }
        } else {
            message = "Co-op Adventure Complete!\n\nTeam collected all " + totalRelics + " relics!\nPlayer 1: " + player1.getAchievements().size() + " relics\nPlayer 2: " + player2.getAchievements().size() + " relics";
            title = "Mission Accomplished!";
        }
        
        // Show completion dialog
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        
        // Update status
        statusLabel.setText(title);
        gameInfoLabel.setText("Game Complete! Press Reset to play again or Back to menu.");
        appendToGameLog("=== " + title + " ===");
    }
    
    public void startAdventure(MiniAdventure adventure, TestUser player1, TestUser player2, boolean competitive) {
        this.player1 = player1;
        this.player2 = player2;
        this.competitive = competitive;
        
        resetGame();
        
        gameActive = true;
        
        // Clear and setup game log
        gameLog.setText("");
        appendToGameLog("=== Test Adventure Started! ===");
        appendToGameLog("Mode: " + (competitive ? "Competitive" : "Co-op"));
        appendToGameLog("Player 1: " + player1.getUsername() + " (Blue)");
        appendToGameLog("Player 2: " + player2.getUsername() + " (Red)");
        appendToGameLog("Use WASD or Arrow Keys to move");
        appendToGameLog("Press R for rules");
        appendToGameLog("Collect all " + totalRelics + " relics to win!");
        
        // Update display
        updateDisplay();
        
        // Show rules at start
        SwingUtilities.invokeLater(() -> {
            showGameRules();
            requestFocus();
        });
    }
    
    private void resetGame() {
        gameActive = false;
        currentPlayer = 1;
        player1X = 0;
        player1Y = 0;
        player2X = 9;
        player2Y = 9;
        relicsCollected = 0;
        
        // Clear achievements
        if (player1 != null) {
            player1.getAchievements().clear();
        }
        if (player2 != null) {
            player2.getAchievements().clear();
        }
        
        // Randomly place relics
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                relics[x][y] = false;
            }
        }
        
        int relicsPlaced = 0;
        while (relicsPlaced < totalRelics) {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            
            // Don't place relics on starting positions
            if ((x == 0 && y == 0) || (x == 9 && y == 9)) continue;
            if (!relics[x][y]) {
                relics[x][y] = true;
                relicsPlaced++;
            }
        }
        
        // Update board
        JPanel boardPanel = (JPanel) ((JPanel) getComponent(1)).getComponent(0);
        updateBoard(boardPanel);
        
        statusLabel.setText("Test Adventure - Ready!");
        gameInfoLabel.setText("Press any movement key to start");
    }
    
    private void updateDisplay() {
        if (player1 == null || player2 == null) return;
        
        TestUser currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        currentPlayerLabel.setText("Current Player: " + currentPlayerUser.getUsername() + " (Player " + currentPlayer + ")");
        
        if (gameActive) {
            gameInfoLabel.setText("Relics: " + relicsCollected + "/" + totalRelics + " | Mode: " + 
                (competitive ? "Competitive" : "Co-op"));
        }
        
        // Update board
        JPanel boardPanel = (JPanel) ((JPanel) getComponent(1)).getComponent(0);
        updateBoard(boardPanel);
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
