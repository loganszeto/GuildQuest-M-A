package Frontend;

import Backend.Tiles.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Realm game that demonstrates how to use existing tiles and sprites
 * Shows integration with Backend.Tiles system and SpriteManager
 */
public class RealmGameDemo extends JFrame {
    private TestUser player1;
    private TestUser player2;
    private int currentPlayer = 1;
    private boolean competitive = false;
    private boolean gameActive = false;
    
    // Simple realm system using Backend.Tiles
    private List<Tile> tiles = new ArrayList<>();
    private Player player1Tile;
    private Player player2Tile;
    private final Random random = new Random();
    private final int realmSize = 20;
    
    // UI Components
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JLabel gameInfoLabel;
    private JTextArea gameLog;
    private JPanel boardPanel;
    
    public RealmGameDemo() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        initializeRealm();
    }
    
    private void initializeComponents() {
        // Create test users
        player1 = new TestUser("Player1");
        player2 = new TestUser("Player2");
        
        // UI Components
        statusLabel = new JLabel("Welcome to GuildQuest Realm Adventure!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 215, 0));
        
        currentPlayerLabel = new JLabel("Current Player: Player 1", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gameInfoLabel = new JLabel("Press Start Game to begin", JLabel.CENTER);
        gameInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        gameLog = new JTextArea(10, 30);
        gameLog.setEditable(false);
        gameLog.setBackground(new Color(30, 30, 50));
        gameLog.setForeground(Color.WHITE);
        gameLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        boardPanel = createGameBoard();
    }
    
    private void initializeRealm() {
        tiles.clear();
        
        appendToGameLog("=== Initializing Realm with Backend.Tiles ===");
        
        // Create ground tiles covering the entire realm
        for (int x = -realmSize/2; x <= realmSize/2; x++) {
            for (int y = -realmSize/2; y <= realmSize/2; y++) {
                tiles.add(new Ground(new java.awt.Point(x, y)));
            }
        }
        appendToGameLog("Created " + (realmSize * realmSize) + " ground tiles");
        
        // Create player tiles using Backend.Tiles.Player
        player1Tile = new Player(new java.awt.Point(-realmSize/2 + 1, -realmSize/2 + 1), null);
        player2Tile = new Player(new java.awt.Point(realmSize/2 - 1, realmSize/2 - 1), null);
        tiles.add(player1Tile);
        tiles.add(player2Tile);
        appendToGameLog("Created 2 player tiles at opposite corners");
        
        // Add relics using Backend.Tiles.RelicTile
        int relicCount = 8;
        for (int i = 0; i < relicCount; i++) {
            int x = random.nextInt(realmSize) - realmSize/2;
            int y = random.nextInt(realmSize) - realmSize/2;
            // Avoid placing relics on player starting positions
            if ((x == -realmSize/2 + 1 && y == -realmSize/2 + 1) || 
                (x == realmSize/2 - 1 && y == realmSize/2 - 1)) {
                i--; // Retry
                continue;
            }
            tiles.add(new RelicTile(new java.awt.Point(x, y)));
        }
        appendToGameLog("Created " + relicCount + " relic tiles");
        
        // Add enemies/mobs using Backend.Tiles.Mob
        int mobCount = 5;
        for (int i = 0; i < mobCount; i++) {
            int x = random.nextInt(realmSize) - realmSize/2;
            int y = random.nextInt(realmSize) - realmSize/2;
            // Avoid placing mobs on player starting positions
            if ((x == -realmSize/2 + 1 && y == -realmSize/2 + 1) || 
                (x == realmSize/2 - 1 && y == realmSize/2 - 1)) {
                i--; // Retry
                continue;
            }
            tiles.add(new Mob(new java.awt.Point(x, y)) {
                @Override
                public String toString() {
                    return "Goblin";
                }
            });
        }
        appendToGameLog("Created " + mobCount + " mob tiles (enemies)");
        
        appendToGameLog("Realm initialized with " + tiles.size() + " total tiles");
    }
    
    private JPanel createGameBoard() {
        JPanel board = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRealm((Graphics2D) g);
            }
        };
        board.setBackground(new Color(20, 20, 40));
        board.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
            "Realm Map - Backend.Tiles System Demo",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(255, 215, 0)
        ));
        
        board.setPreferredSize(new Dimension(600, 400));
        return board;
    }
    
    private void drawRealm(Graphics2D g2d) {
        // Calculate bounds
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        
        for (Tile tile : tiles) {
            minX = Math.min(minX, tile.getX());
            maxX = Math.max(maxX, tile.getX());
            minY = Math.min(minY, tile.getY());
            maxY = Math.max(maxY, tile.getY());
        }
        
        int realmWidth = maxX - minX + 1;
        int realmHeight = maxY - minY + 1;
        
        int tileSize = Math.min(
            (boardPanel.getWidth() - 20) / realmWidth,
            (boardPanel.getHeight() - 20) / realmHeight
        );
        tileSize = Math.max(tileSize, 8); // Minimum tile size
        tileSize = Math.min(tileSize, 30); // Maximum tile size
        
        int offsetX = (boardPanel.getWidth() - realmWidth * tileSize) / 2;
        int offsetY = (boardPanel.getHeight() - realmHeight * tileSize) / 2;
        
        // Draw all tiles with visual representation
        for (Tile tile : tiles) {
            int x = offsetX + (tile.getX() - minX) * tileSize;
            int y = offsetY + (tile.getY() - minY) * tileSize;
            
            // This is where you would integrate SpriteManager:
            // BufferedImage sprite = Backend.SpriteManager.getInstance().getScaledSprite(tile, tileSize, realmSpace);
            // if (sprite != null) {
            //     g2d.drawImage(sprite, x, y, null);
            // } else {
            //     drawFallbackTile(g2d, tile, x, y, tileSize);
            // }
            
            // For now, we'll use the fallback rendering
            drawFallbackTile(g2d, tile, x, y, tileSize);
        }
        
        // Highlight current player
        Player currentPlayerTile = currentPlayer == 1 ? player1Tile : player2Tile;
        int x = offsetX + (currentPlayerTile.getX() - minX) * tileSize;
        int y = offsetY + (currentPlayerTile.getY() - minY) * tileSize;
        
        g2d.setColor(new Color(255, 255, 0, 100)); // Yellow highlight
        g2d.fillRect(x - 2, y - 2, tileSize + 4, tileSize + 4);
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x - 2, y - 2, tileSize + 4, tileSize + 4);
    }
    
    private void drawFallbackTile(Graphics2D g2d, Tile tile, int x, int y, int size) {
        Color color;
        String text = "";
        
        if (tile instanceof Player) {
            if (tile == player1Tile) {
                color = new Color(100, 100, 255); // Blue for Player 1
                text = "P1";
            } else {
                color = new Color(255, 100, 100); // Red for Player 2
                text = "P2";
            }
        } else if (tile instanceof RelicTile) {
            color = new Color(255, 215, 0); // Gold for relics
            text = "R";
        } else if (tile instanceof Mob) {
            color = new Color(255, 0, 0); // Red for mobs
            text = "M";
        } else if (tile instanceof Ground) {
            color = new Color(50, 150, 50); // Green for ground
        } else {
            color = new Color(100, 100, 100); // Gray for unknown
        }
        
        g2d.setColor(color);
        g2d.fillRect(x, y, size, size);
        g2d.setColor(color.darker());
        g2d.drawRect(x, y, size, size);
        
        if (!text.isEmpty() && size >= 12) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, Math.max(8, size / 3)));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x + (size - fm.stringWidth(text)) / 2;
            int textY = y + (size - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, textX, textY);
        }
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
        centerPanel.add(boardPanel, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        scrollPane.setPreferredSize(new Dimension(250, 400));
        centerPanel.add(scrollPane, BorderLayout.EAST);
        
        // Bottom panel for controls
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(20, 20, 40));
        
        JButton startButton = new JButton("Start Game");
        JButton resetButton = new JButton("Reset Game");
        JButton competitiveButton = new JButton("Mode: Co-op");
        JButton rulesButton = new JButton("Rules (R)");
        JButton spriteInfoButton = new JButton("Sprite Info");
        
        styleButton(startButton);
        styleButton(resetButton);
        styleButton(competitiveButton);
        styleButton(rulesButton);
        styleButton(spriteInfoButton);
        
        startButton.addActionListener(e -> startGame());
        resetButton.addActionListener(e -> resetGame());
        competitiveButton.addActionListener(e -> {
            competitive = !competitive;
            competitiveButton.setText("Mode: " + (competitive ? "Competitive" : "Co-op"));
        });
        rulesButton.addActionListener(e -> showRules());
        spriteInfoButton.addActionListener(e -> showSpriteInfo());
        
        bottomPanel.add(startButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(competitiveButton);
        bottomPanel.add(rulesButton);
        bottomPanel.add(spriteInfoButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void setupEventHandlers() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }
    
    private void setupFrame() {
        setTitle("GuildQuest - Realm Adventure with Backend.Tiles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        requestFocus();
    }
    
    private void startGame() {
        gameActive = true;
        resetGame();
        gameActive = true;
        
        gameLog.setText("");
        appendToGameLog("=== Realm Adventure Started! ===");
        appendToGameLog("Mode: " + (competitive ? "Competitive" : "Co-op"));
        appendToGameLog("Player 1: " + player1.getUsername() + " (Blue)");
        appendToGameLog("Player 2: " + player2.getUsername() + " (Red)");
        appendToGameLog("Using Backend.Tiles system!");
        appendToGameLog("Use WASD or Arrow Keys to move");
        appendToGameLog("Press R for rules");
        appendToGameLog("Collect all relics to win!");
        
        statusLabel.setText("Game Active!");
        updateDisplay();
        
        SwingUtilities.invokeLater(() -> {
            showRules();
            requestFocus();
        });
    }
    
    private void resetGame() {
        gameActive = false;
        currentPlayer = 1;
        
        // Reset realm
        initializeRealm();
        
        // Clear achievements
        player1.getAchievements().clear();
        player2.getAchievements().clear();
        
        updateBoard();
        statusLabel.setText("Game Reset - Ready!");
        gameInfoLabel.setText("Press Start Game to begin");
    }
    
    private void handleKeyPress(KeyEvent e) {
        if (!gameActive) return;
        
        // Handle rules display toggle
        if (e.getKeyCode() == KeyEvent.VK_R) {
            showRules();
            return;
        }
        
        // Handle movement input
        String direction = "";
        int dx = 0, dy = 0;
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = "UP";
                dx = 0; dy = -1;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                direction = "DOWN";
                dx = 0; dy = 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = "LEFT";
                dx = -1; dy = 0;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = "RIGHT";
                dx = 1; dy = 0;
                break;
            default:
                return; // Ignore other keys
        }
        
        Player currentPlayerTile = currentPlayer == 1 ? player1Tile : player2Tile;
        int newX = currentPlayerTile.getX() + dx;
        int newY = currentPlayerTile.getY() + dy;
        
        // Check if move is valid
        Tile targetTile = getTileAt(newX, newY);
        if (targetTile instanceof Mob && targetTile != currentPlayerTile) {
            appendToGameLog("Cannot move to enemy tile!");
            return;
        }
        
        // Process the move
        currentPlayerTile.move(dx, dy);
        
        TestUser currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        appendToGameLog(currentPlayerUser.getUsername() + " moved " + direction);
        
        // Check for relic collection
        if (targetTile instanceof RelicTile) {
            // Remove the relic from the tiles list
            tiles.remove(targetTile);
            appendToGameLog(currentPlayerUser.getUsername() + " collected a relic!");
            currentPlayerUser.addAchievement("Relic Collected!");
        }
        
        // Update display
        updateDisplay();
        
        // Check for game completion
        if (checkGameComplete()) {
            handleGameComplete();
        } else {
            // Switch turns
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            updateDisplay();
        }
        
        requestFocus();
    }
    
    private Tile getTileAt(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.getX() == x && tile.getY() == y) {
                return tile;
            }
        }
        return null;
    }
    
    private boolean checkGameComplete() {
        // Check if all relics are collected
        return tiles.stream().noneMatch(tile -> tile instanceof RelicTile);
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
            message = "Co-op Adventure Complete!\n\nTeam collected all relics!\nPlayer 1: " + player1.getAchievements().size() + " relics\nPlayer 2: " + player2.getAchievements().size() + " relics";
            title = "Mission Accomplished!";
        }
        
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText(title);
        gameInfoLabel.setText("Game Complete! Press Reset to play again or Start to begin new game.");
        appendToGameLog("=== " + title + " ===");
    }
    
    private void updateDisplay() {
        TestUser currentPlayerUser = currentPlayer == 1 ? player1 : player2;
        currentPlayerLabel.setText("Current Player: " + currentPlayerUser.getUsername() + " (Player " + currentPlayer + ")");
        
        if (gameActive) {
            long relicsLeft = tiles.stream().filter(tile -> tile instanceof RelicTile).count();
            gameInfoLabel.setText("Relics Left: " + relicsLeft + " | Mode: " + 
                (competitive ? "Competitive" : "Co-op"));
        }
        
        updateBoard();
    }
    
    private void updateBoard() {
        boardPanel.repaint();
    }
    
    private void showRules() {
        StringBuilder rules = new StringBuilder();
        rules.append("=== Realm Adventure ===\n\n");
        rules.append("OBJECTIVE:\n");
        rules.append("Collect all relics (R) in the realm!\n\n");
        
        rules.append("CONTROLS:\n");
        rules.append("• Use W/↑ to move UP\n");
        rules.append("• Use S/↓ to move DOWN\n");
        rules.append("• Use A/← to move LEFT\n");
        rules.append("• Use D/→ to move RIGHT\n");
        rules.append("• Press R to show these rules\n\n");
        
        rules.append("REALM FEATURES:\n");
        rules.append("• Uses Backend.Tiles system:\n");
        rules.append("  - Ground tiles for the base terrain\n");
        rules.append("  - Player tiles for characters\n");
        rules.append("  - RelicTile for collectibles\n");
        rules.append("  - Mob tiles for enemies\n");
        rules.append("• Ready for SpriteManager integration\n");
        rules.append("• Custom realm with randomly placed elements\n\n");
        
        rules.append("GAME MODE:\n");
        if (competitive) {
            rules.append("• Competitive: Player with most relics wins!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        } else {
            rules.append("• Co-op: Work together to collect all relics!\n");
            rules.append("• Take turns moving (Player 1 starts)\n");
        }
        
        rules.append("\nWIN CONDITION:\n");
        rules.append("• Collect all relics in the realm to win!\n");
        rules.append("• Blue (P1) and Red (P2) are the players\n");
        rules.append("• Gold (R) are the relics to collect\n");
        rules.append("• Red (M) are the mobs to avoid\n");
        rules.append("• Green tiles are ground\n");
        
        JOptionPane.showMessageDialog(this, rules.toString(), "Game Rules - Realm Adventure", JOptionPane.INFORMATION_MESSAGE);
        requestFocus();
    }
    
    private void showSpriteInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== SpriteManager Integration ===\n\n");
        info.append("To integrate with the existing SpriteManager:\n\n");
        info.append("1. In the drawRealm method, replace the fallback rendering with:\n\n");
        info.append("```java\n");
        info.append("// Get sprite from SpriteManager\n");
        info.append("Backend.SpriteManager spriteManager = Backend.SpriteManager.getInstance();\n");
        info.append("BufferedImage sprite = spriteManager.getScaledSprite(tile, tileSize, realmSpace);\n");
        info.append("if (sprite != null) {\n");
        info.append("    g2d.drawImage(sprite, x, y, null);\n");
        info.append("} else {\n");
        info.append("    drawFallbackTile(g2d, tile, x, y, tileSize);\n");
        info.append("}\n");
        info.append("```\n\n");
        info.append("2. The SpriteManager will:\n");
        info.append("• Load spritesheet.png from resources\n");
        info.append("• Map tile classes to sprite positions\n");
        info.append("• Handle Player 1 vs Player 2 sprite differences\n");
        info.append("• Provide fallback colored squares if spritesheet missing\n\n");
        info.append("3. Available tile types for sprites:\n");
        info.append("• Ground (Green)\n");
        info.append("• Player (Blue/Red)\n");
        info.append("• Mob (Red)\n");
        info.append("• RelicTile (Gold)\n");
        info.append("• NPC (Blue)\n");
        info.append("• EmptyTile (Gray)\n\n");
        info.append("4. The spritesheet.png should be 160x20 pixels\n");
        info.append("   with 8 sprites of 20x20 pixels each.\n");
        
        JOptionPane.showMessageDialog(this, info.toString(), "SpriteManager Integration Info", JOptionPane.INFORMATION_MESSAGE);
        requestFocus();
    }
    
    private void appendToGameLog(String message) {
        gameLog.append(message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RealmGameDemo();
        });
    }
}
