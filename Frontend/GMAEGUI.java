package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GMAEGUI extends JFrame {
    private LoginScreen loginScreen;
    private AdventureMenuScreen adventureMenuScreen;
    private RelicHuntGameScreen relicHuntGameScreen;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public static final String LOGIN_CARD = "LOGIN";
    public static final String ADVENTURE_MENU_CARD = "ADVENTURE_MENU";
    public static final String RELIC_HUNT_CARD = "RELIC_HUNT";
    
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
        relicHuntGameScreen = new RelicHuntGameScreen(this);
        
        mainPanel.add(loginScreen, LOGIN_CARD);
        mainPanel.add(adventureMenuScreen, ADVENTURE_MENU_CARD);
        mainPanel.add(relicHuntGameScreen, RELIC_HUNT_CARD);
    }
    
    private void setupFrame() {
        setTitle("GuildQuest - Mini Adventure Environment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        try {
            setIconImage(createGameIcon());
        } catch (Exception e) {

        }
        
        add(mainPanel);
    }
    
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    
    public void showAdventureMenu() {
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public void showAdventureMenu(Backend.User player1, Backend.User player2) {
        adventureMenuScreen.setPlayers(player1, player2);
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public void showGameScreen(MiniAdventure adventure, Backend.User player1, Backend.User player2, boolean competitive) {
        // Check if this is a RelicHuntAdventure and use the appropriate screen
        if (adventure instanceof RelicHuntAdventure) {
            RelicHuntAdventure relicHuntAdventure = (RelicHuntAdventure) adventure;
            relicHuntGameScreen.startAdventure(relicHuntAdventure.game, player1, player2, competitive);
            cardLayout.show(mainPanel, RELIC_HUNT_CARD);
        } else {
            // For other adventures, show a message
            JOptionPane.showMessageDialog(this, 
                "Starting adventure: " + adventure.getName() + "\n" +
                "Mode: " + (competitive ? "Competitive" : "Co-op") + "\n" +
                "This would launch the appropriate game screen.",
                "Adventure Starting", 
                JOptionPane.INFORMATION_MESSAGE);
        }
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
