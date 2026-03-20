package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Backend.RelicHuntGameBackend;
import Backend.TimedRaidGameBackend;
import Backend.User;
import Backend.RealmSpace;
import Backend.SpriteManager;
import Backend.Tiles.*;

/**
 * Game screen for Relic Hunt mini-adventure that uses sprites and backend logic
 */
public class RelicHuntGameScreen extends JPanel {
    private GMAEGUI mainGUI;
    
    // UI Components
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JLabel gameInfoLabel;
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JTextArea gameLog;
    private JButton backButton;
    private JButton resetButton;
    private JButton startButton;
    
    // Game backend
    private RelicHuntGameBackend game;
    private User player1, player2;
    private boolean competitive;
    
    // Sprite rendering
    private SpriteBoardPanel boardPanel;
    private SpriteManager spriteManager;
    private Timer uiTickTimer;
    
    public RelicHuntGameScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.spriteManager = SpriteManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupUiTickTimer();
    }
    
    private void initializeComponents() {
        // Create game backend
        game = new RelicHuntGameBackend();
        
        // UI Components
        statusLabel = new JLabel("Welcome to Relic Hunt Adventure!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 215, 0));
        
        currentPlayerLabel = new JLabel("Press Start to begin", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentPlayerLabel.setForeground(Color.WHITE);
        
        gameInfoLabel = new JLabel("Select an adventure from the menu", JLabel.CENTER);
        gameInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gameInfoLabel.setForeground(Color.WHITE);

        player1HealthBar = new JProgressBar(0, RelicHuntGameBackend.MAX_HEALTH);
        player2HealthBar = new JProgressBar(0, RelicHuntGameBackend.MAX_HEALTH);
        styleHealthBar(player1HealthBar, "P1 Health");
        styleHealthBar(player2HealthBar, "P2 Health");
        
        gameLog = new JTextArea(8, 30);
        gameLog.setEditable(false);
        gameLog.setBackground(new Color(30, 30, 50));
        gameLog.setForeground(Color.WHITE);
        gameLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        boardPanel = new SpriteBoardPanel();
        
        // Buttons
        backButton = new JButton("Back to Menu");
        resetButton = new JButton("Reset Game");
        startButton = new JButton("Start Game");
        
        styleButton(backButton);
        styleButton(resetButton);
        styleButton(startButton);
        
        backButton.addActionListener(e -> {
            if (mainGUI != null) {
                mainGUI.showAdventureMenu(player1, player2);
            }
        });
        
        resetButton.addActionListener(e -> {
            if (game != null && player1 != null && player2 != null) {
                game.initialize(player1, player2, competitive);
                // Don't auto-start after reset
                updateDisplay();
                appendToGameLog("Game Reset! Click 'Start Game' to begin.");
                startButton.setEnabled(true);
                statusLabel.setText("Relic Hunt - Ready to Start!");
            }
        });
        
        startButton.addActionListener(e -> {
            if (game != null && !game.isActive()) {
                game.start();
                updateDisplay();
                appendToGameLog("Game Started!");
                startButton.setEnabled(false);
                requestFocus();
            }
        });
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void styleHealthBar(JProgressBar bar, String title) {
        bar.setStringPainted(true);
        bar.setForeground(new Color(60, 180, 75));
        bar.setBackground(new Color(70, 70, 90));
        bar.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
            title,
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 11),
            Color.WHITE
        ));
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

        JPanel healthPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        healthPanel.setBackground(new Color(20, 20, 40));
        healthPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        healthPanel.add(player1HealthBar);
        healthPanel.add(player2HealthBar);
        
        // Center panel - game board and log
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(20, 20, 40));
        centerPanel.add(boardPanel, BorderLayout.WEST);
        
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for controls
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(20, 20, 40));
        bottomPanel.add(startButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(backButton);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(20, 20, 40));
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(healthPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        setFocusable(true);
    }

    private void setupUiTickTimer() {
        uiTickTimer = new Timer(500, e -> {
            if (game == null || !game.isActive()) {
                return;
            }
            if (game instanceof TimedRaidGameBackend) {
                TimedRaidGameBackend timedRaid = (TimedRaidGameBackend) game;
                if (timedRaid.isTimedOut()) {
                    handleTimedRaidTimeout();
                    return;
                }
            }
            updateDisplay();
        });
        uiTickTimer.start();
    }
    
    private class SpriteBoardPanel extends JPanel {
        private final int tileSize = 40;
        
        public SpriteBoardPanel() {
            setBackground(new Color(20, 20, 40));
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                "Relic Hunt Board",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 215, 0)
            ));
            setPreferredSize(new Dimension(game.getBoardSize() * tileSize + 20, game.getBoardSize() * tileSize + 40));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Enable anti-aliasing for better sprite rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (game == null) {
                g2d.dispose();
                return;
            }
            
            // Calculate offset to center the board
            int offsetX = 10;
            int offsetY = 30;
            int boardSize = game.getBoardSize();
            
            // Draw grid and tiles
            for (int y = 0; y < boardSize; y++) {
                for (int x = 0; x < boardSize; x++) {
                    int px = offsetX + x * tileSize;
                    int py = offsetY + y * tileSize;
                    
                    // Draw ground tile as background
                    BufferedImage groundSprite = spriteManager.getScaledSprite(new Ground(new Point(x, y)), tileSize);
                    if (groundSprite != null) {
                        g2d.drawImage(groundSprite, px, py, tileSize, tileSize, null);
                    } else {
                        // Fallback to colored background
                        g2d.setColor(new Color(50, 50, 50));
                        g2d.fillRect(px, py, tileSize, tileSize);
                    }
                    
                    // Draw grid lines
                    g2d.setColor(new Color(80, 80, 80));
                    g2d.drawRect(px, py, tileSize, tileSize);
                    
                    // Draw game entities
                    RealmSpace realm = game.getRealmSpace();
                    java.util.List<Backend.Tiles.Tile> tiles = realm.getAllAt(new Point(x, y));
                    
                    for (Backend.Tiles.Tile tile : tiles) {
                        if (tile instanceof Player) {
                            drawPlayerSprite(g2d, px, py, tileSize, x == game.getPlayer1X() && y == game.getPlayer1Y());
                        } else if (tile instanceof RelicTile) {
                            drawRelicSprite(g2d, px, py, tileSize);
                        } else if (tile instanceof Enemy) {
                            drawEnemySprite(g2d, px, py, tileSize);
                        }
                    }
                }
            }
            
            g2d.dispose();
        }
        
        private void drawPlayerSprite(Graphics2D g2d, int px, int py, int tileSize, boolean isPlayer1) {
            // Use actual sprites from spritesheet at index 0 and 1
            try {
                BufferedImage playerSprite = spriteManager.getScaledSpriteFromIndex(isPlayer1 ? 0 : 1, tileSize);
                if (playerSprite != null) {
                    g2d.drawImage(playerSprite, px, py, tileSize, tileSize, null);
                } else {
                    // Fallback to colored circles
                    drawFallbackPlayer(g2d, px, py, tileSize, isPlayer1);
                }
            } catch (Exception e) {
                drawFallbackPlayer(g2d, px, py, tileSize, isPlayer1);
            }
        }
        
        private void drawFallbackPlayer(Graphics2D g2d, int px, int py, int tileSize, boolean isPlayer1) {
            if (isPlayer1) {
                g2d.setColor(new Color(100, 100, 255));
                g2d.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("1", px + tileSize/2 - 4, py + tileSize/2 + 6);
            } else {
                g2d.setColor(new Color(255, 100, 100));
                g2d.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("2", px + tileSize/2 - 4, py + tileSize/2 + 6);
            }
        }
        
        private void drawRelicSprite(Graphics2D g2d, int px, int py, int tileSize) {
            BufferedImage relicSprite = spriteManager.getScaledSprite(new RelicTile(new Point(px, py)), tileSize);
            if (relicSprite != null) {
                g2d.drawImage(relicSprite, px, py, tileSize, tileSize, null);
            } else {
                // Fallback to gold circle
                g2d.setColor(new Color(255, 215, 0));
                g2d.fillOval(px + 8, py + 8, tileSize - 16, tileSize - 16);
                g2d.setColor(new Color(200, 170, 0));
                g2d.fillOval(px + 12, py + 12, tileSize - 24, tileSize - 24);
            }
        }
        
        private void drawEnemySprite(Graphics2D g2d, int px, int py, int tileSize) {
            BufferedImage enemySprite = spriteManager.getScaledSprite(new Enemy(new Point(px, py), new Backend.NPCCharacter("Enemy", 3, "Goblin")), tileSize);
            if (enemySprite != null) {
                g2d.drawImage(enemySprite, px, py, tileSize, tileSize, null);
            } else {
                // Fallback to red square
                g2d.setColor(new Color(200, 50, 50));
                g2d.fillRect(px + 6, py + 6, tileSize - 12, tileSize - 12);
                g2d.setColor(new Color(150, 30, 30));
                g2d.fillRect(px + 10, py + 10, tileSize - 20, tileSize - 20);
            }
        }
    }
    
    public void startAdventure(RelicHuntGameBackend game, User player1, User player2, boolean competitive) {
        this.game = game;
        this.player1 = player1;
        this.player2 = player2;
        this.competitive = competitive;
        
        game.initialize(player1, player2, competitive);
        // Don't auto-start - let user click start button
        
        gameLog.setText("");
        appendToGameLog("=== " + game.getName() + " Adventure Ready! ===");
        appendToGameLog("Mode: " + (competitive ? "Competitive" : "Co-op"));
        appendToGameLog("Player 1: " + player1.getUsername());
        appendToGameLog("Player 2: " + player2.getUsername());
        appendToGameLog("Click 'Start Game' to begin!");
        appendToGameLog("Use WASD or Arrow Keys to move");
        appendToGameLog("Collect all " + game.getTotalRelics() + " relics while avoiding enemies!");
        if (game instanceof TimedRaidGameBackend) {
            appendToGameLog("Timed Raid rule: finish before the timer reaches 0.");
        }
        
        statusLabel.setText(game.getName() + " - Ready to Start!");
        startButton.setEnabled(true);
        updateDisplay();
        requestFocus();
    }
    
    private void handleKeyPress(KeyEvent e) {
        if (game == null || !game.isActive()) {
            return;
        }
        
        // Handle movement input
        String direction = "";
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = "up";
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                direction = "down";
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = "left";
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = "right";
                break;
            default:
                return; // Ignore other keys
        }
        
        int currentPlayer = game.getTurn();
        User currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        
        boolean success = game.acceptPlayerInput(currentPlayer, direction);
        
        if (success) {
            game.advanceTurn();
            appendToGameLog(currentPlayerUser.getUsername() + " moved " + direction);
            updateDisplay();
            
            // Check for game completion
            if (game.isComplete()) {
                handleGameComplete();
            } else if (game instanceof TimedRaidGameBackend && ((TimedRaidGameBackend) game).isTimedOut()) {
                handleTimedRaidTimeout();
            } else if (game.isLost() != -1) {
                handleGameOver(game.isLost());
            }
        } else {
            appendToGameLog(currentPlayerUser.getUsername() + " couldn't move " + direction + "!");
        }
        
        requestFocus();
    }
    
    private void handleGameComplete() {
        game.reset(); // Keep game inactive until Start Game is clicked again.
        String winner = game.getWinner() == 1 ? "Player 1" : (game.getWinner() == 2 ? "Player 2" : "Team");
        JOptionPane.showMessageDialog(this, "Congratulations " + winner + "! You collected all relics!", "Victory!", JOptionPane.INFORMATION_MESSAGE);
        appendToGameLog("=== Victory! ===");
        appendToGameLog("Click 'Start Game' to play again!");
        startButton.setEnabled(true);
        statusLabel.setText(game.getName() + " - Victory! Ready for another game?");
        updateDisplay();
    }
    
    private void handleGameOver(int loser) {
        game.reset(); // Keep game inactive until Start Game is clicked again.
        String loserName = loser == 1 ? "Player 1" : "Player 2";
        JOptionPane.showMessageDialog(this, loserName + " ran out of health! Game Over!", "Game Over", JOptionPane.WARNING_MESSAGE);
        appendToGameLog("=== Game Over! ===");
        appendToGameLog("Click 'Start Game' to try again!");
        startButton.setEnabled(true);
        statusLabel.setText(game.getName() + " - Game Over! Ready to try again?");
        updateDisplay();
    }

    private void handleTimedRaidTimeout() {
        game.reset(); // Keep timer stopped until Start Game is clicked.
        JOptionPane.showMessageDialog(
            this,
            "Time ran out before all relics were collected!",
            "Timed Raid Failed",
            JOptionPane.WARNING_MESSAGE
        );
        appendToGameLog("=== Timed Raid Failed: Time expired ===");
        appendToGameLog("Click 'Start Game' to try again!");
        startButton.setEnabled(true);
        statusLabel.setText("Timed Raid - Time expired. Ready to retry?");
        updateDisplay();
    }
    
    private void updateDisplay() {
        if (game == null) return;
        
        int currentPlayer = game.getTurn();
        User currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        currentPlayerLabel.setText("Current Player: " + currentPlayerUser.getUsername() + " (Player " + currentPlayer + ")");
        
        if (game.isActive()) {
            String info = "Relics: " + game.getRelicsCollected() + "/" + game.getTotalRelics() + 
                " | P1 Health: " + game.getPlayerHealth(1) + 
                " | P2 Health: " + game.getPlayerHealth(2) + 
                " | Mode: " + (game.isCompetitive() ? "Competitive" : "Co-op");
            if (game instanceof TimedRaidGameBackend) {
                TimedRaidGameBackend timedRaid = (TimedRaidGameBackend) game;
                info += " | Time: " + timedRaid.getTimeRemainingSeconds() + "s";
            }
            gameInfoLabel.setText(info);
        }

        int p1Health = game.getPlayerHealth(1);
        int p2Health = game.getPlayerHealth(2);
        player1HealthBar.setValue(p1Health);
        player2HealthBar.setValue(p2Health);
        player1HealthBar.setString(p1Health + "/" + RelicHuntGameBackend.MAX_HEALTH);
        player2HealthBar.setString(p2Health + "/" + RelicHuntGameBackend.MAX_HEALTH);
        
        boardPanel.repaint();
    }
    
    private void appendToGameLog(String message) {
        gameLog.append(message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }
}
