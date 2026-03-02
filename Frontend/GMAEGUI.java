package gmae;

import javax.swing.*;
import java.awt.*;

public class GMAEGUI extends JFrame {
    private LoginScreen loginScreen;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Constants for card names (ready for future expansion)
    public static final String LOGIN_CARD = "LOGIN";
    
    public GMAEGUI() {
        // Initialize GUI components
        initializeComponents();
        
        // Setup the main frame
        setupFrame();
        
        // Show login screen by default
        showLoginScreen();
    }
    
    private void initializeComponents() {
        // Create main panel with card layout (ready for future screens)
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize login screen
        loginScreen = new LoginScreen(this);
        
        // Add login screen to card layout
        mainPanel.add(loginScreen, LOGIN_CARD);
    }
    
    private void setupFrame() {
        setTitle("GuildQuest Mini-Adventure Environment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    /**
     * Show the login screen
     */
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    
    /**
     * Placeholder methods for future implementation
     * These will be implemented as you add more screens
     */
    public void showAdventureMenu() {
        // TODO: Implement when AdventureMenuScreen is ready
        JOptionPane.showMessageDialog(this, 
            "Adventure Menu coming soon!", 
            "Feature Not Implemented", 
            JOptionPane.INFORMATION_MESSAGE);
    }
  
    /**
     * Launch the application
     */
    public void launch() {
        setVisible(true);
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set a modern look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new GMAEGUI().launch();
        });
    }
}
