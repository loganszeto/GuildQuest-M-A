package gmae;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GMAEGUI extends JFrame {
    private LoginScreen loginScreen;
    private AdventureMenuScreen adventureMenuScreen;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public static final String LOGIN_CARD = "LOGIN";
    public static final String ADVENTURE_MENU_CARD = "ADVENTURE_MENU";
    
    public GMAEGUI() {
        initializeComponents();
        setupFrame();
        showLoginScreen();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginScreen = new LoginScreen(this);
        adventureMenuScreen = new AdventureMenuScreen(this);
        
        mainPanel.add(loginScreen, LOGIN_CARD);
        mainPanel.add(adventureMenuScreen, ADVENTURE_MENU_CARD);
    }
    
    private void setupFrame() {
        setTitle("GuildQuest - Mini Adventure Environment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        try {
            setIconImage(createGameIcon());
        } catch (Exception e) {
            // Icon not critical
        }
        
        add(mainPanel);
    }
    
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    
    public void showAdventureMenu() {
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public void showAdventureMenu(PlayerProfile player1, PlayerProfile player2) {
        adventureMenuScreen.setPlayers(player1, player2);
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    private Image createGameIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillRect(14, 2, 4, 20);
        g2d.fillRect(12, 20, 8, 4);
        g2d.fillRect(14, 24, 4, 6);
        
        g2d.dispose();
        return icon;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GMAEGUI game = new GMAEGUI();
            game.setVisible(true);
        });
    }
}
