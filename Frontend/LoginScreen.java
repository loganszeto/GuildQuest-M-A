package Frontend;

import Backend.User;
import Backend.Realm;
import Backend.RealmFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JTextField playerOneField;
    private JTextField playerTwoField;
    private JButton submitButton;
    private JButton testRealmButton;
    
    public LoginScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        playerOneField = new JTextField(15);
        playerTwoField = new JTextField(15);
        submitButton = new JButton("Begin Quest");
        testRealmButton = new JButton("Test Realm");
        
        // Match AdventureMenuScreen field styling
        playerOneField.setBackground(new Color(60, 60, 100));
        playerOneField.setForeground(Color.WHITE);
        playerOneField.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        playerOneField.setFont(new Font("Arial", Font.PLAIN, 14));
        playerOneField.setPreferredSize(new Dimension(200, 30));
        
        playerTwoField.setBackground(new Color(60, 60, 100));
        playerTwoField.setForeground(Color.WHITE);
        playerTwoField.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        playerTwoField.setFont(new Font("Arial", Font.PLAIN, 14));
        playerTwoField.setPreferredSize(new Dimension(200, 30));
        
        // Match AdventureMenuScreen button styling
        submitButton.setBackground(new Color(200, 50, 50));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        submitButton.setPreferredSize(new Dimension(200, 35));
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Test Realm button styling
        testRealmButton.setBackground(new Color(50, 150, 50));
        testRealmButton.setForeground(Color.WHITE);
        testRealmButton.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        testRealmButton.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 100), 2));
        testRealmButton.setPreferredSize(new Dimension(200, 35));
        testRealmButton.setFocusPainted(false);
        testRealmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 60)); // Match AdventureMenuScreen title panel
        
        // Title
        JLabel titleLabel = new JLabel("GuildQuest - Mini Adventure Environment");
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 28)); // Match AdventureMenuScreen
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main login form
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(45, 45, 80)); // Match AdventureMenuScreen center panel
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Title for login form
        JLabel loginTitle = new JLabel("Enter the Realm");
        loginTitle.setFont(new Font("Old English Text MT", Font.BOLD, 18)); // Match AdventureMenuScreen
        loginTitle.setForeground(new Color(255, 215, 0)); // Gold color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(loginTitle, gbc);
        
        // Player 1 field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel player1Label = new JLabel("Player I:");
        player1Label.setFont(new Font("Old English Text MT", Font.BOLD, 16)); // Match AdventureMenuScreen
        player1Label.setForeground(new Color(255, 215, 0)); // Gold color
        loginPanel.add(player1Label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginPanel.add(playerOneField, gbc);
        
        // Player 2 field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        JLabel player2Label = new JLabel("Player II:");
        player2Label.setFont(new Font("Old English Text MT", Font.BOLD, 16)); // Match AdventureMenuScreen
        player2Label.setForeground(new Color(255, 215, 0)); // Gold color
        loginPanel.add(player2Label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginPanel.add(playerTwoField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(testRealmButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(buttonPanel, gbc);
        
        add(loginPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        testRealmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTestRealm();
            }
        });
    }
    
    private void handleSubmit() {
        String playerOneName = playerOneField.getText().trim();
        String playerTwoName = playerTwoField.getText().trim();
        
        if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Both players must enter names!", 
                "Names Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create User objects (player profiles live in backend.User)
        User player1 = new User(playerOneName, "Mystic Realms");
        player1.setCharacterName(playerOneName);
        User player2 = new User(playerTwoName, "Mystic Realms");
        player2.setCharacterName(playerTwoName);
        
        JOptionPane.showMessageDialog(this, 
            "Welcome to GuildQuest!\nPlayer 1: " + playerOneName + "\nPlayer 2: " + playerTwoName, 
            "Adventure Ready!", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Pass players to adventure menu
        mainGUI.showAdventureMenu(player1, player2);
    }
    
    private void handleTestRealm() {
        try {
            // Test Realm creation
            Realm testRealm = new Realm("Test Realm");
            
            // Test RealmFactory
            RealmFactory factory = new RealmFactory();
            factory.save(testRealm);
            Realm loadedRealm = factory.load("Test Realm");
            
            String realmInfo = "Realm Test Results:\n" +
                "\n=== Original Realm ===\n" +
                "Name: " + testRealm.getName() + "\n" +
                "Width: " + testRealm.getWidth() + "\n" +
                "Height: " + testRealm.getHeight() + "\n" +
                "RealmSpace: " + (testRealm.getRealmSpace() != null ? "Loaded" : "Not Loaded") +
                "\n=== RealmFactory Test ===\n" +
                "Save: Success\n" +
                "Load: " + (loadedRealm != null ? "Success" : "Failed") +
                (loadedRealm != null ? "\nLoaded Name: " + loadedRealm.getName() : "");
            
            JOptionPane.showMessageDialog(this, 
                realmInfo, 
                "Realm Test Result", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error testing Realm: " + e.getMessage(), 
                "Realm Test Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}