package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Simple test version of GMAEGUI that doesn't depend on problematic backend files
 */
public class TestGMAEGUI extends JFrame {
    private TestLoginScreen loginScreen;
    private SimpleAdventureMenuScreen adventureMenuScreen;
    private TestGameScreen gameScreen;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public static final String LOGIN_CARD = "LOGIN";
    public static final String ADVENTURE_MENU_CARD = "ADVENTURE_MENU";
    public static final String GAME_SCREEN_CARD = "GAME_SCREEN";
    
    public TestGMAEGUI() {
        initializeComponents();
        setupFrame();
        showLoginScreen();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginScreen = new TestLoginScreen(this);
        adventureMenuScreen = new SimpleAdventureMenuScreen(this);
        gameScreen = new TestGameScreen(this);
        
        mainPanel.add(loginScreen, LOGIN_CARD);
        mainPanel.add(adventureMenuScreen, ADVENTURE_MENU_CARD);
        mainPanel.add(gameScreen, GAME_SCREEN_CARD);
    }
    
    private void setupFrame() {
        setTitle("GuildQuest - Test Adventure Environment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        try {
            setIconImage(createGameIcon());
        } catch (Exception e) {
            // Icon creation failed, continue without icon
        }
        
        add(mainPanel);
        setVisible(true);
    }
    
    private Image createGameIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillRect(14, 2, 4, 20);
        g2d.fillRect(12, 20, 8, 4);
        g2d.fillRect(14, 24, 4, 6);
        
        g2d.setColor(new Color(200, 50, 50));
        g2d.fillOval(10, 8, 12, 12);
        
        g2d.setColor(new Color(50, 50, 200));
        g2d.fillOval(22, 8, 12, 12);
        
        g2d.dispose();
        return icon;
    }
    
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    
    public void showTestAdventureMenu(TestUser player1, TestUser player2) {
        adventureMenuScreen.setPlayers(player1, player2);
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public void showGameScreen(MiniAdventure adventure, TestUser player1, TestUser player2, boolean competitive) {
        gameScreen.startAdventure(adventure, player1, player2, competitive);
        cardLayout.show(mainPanel, GAME_SCREEN_CARD);
    }
    
    public void showAdventureMenu() {
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TestGMAEGUI();
        });
    }
}
