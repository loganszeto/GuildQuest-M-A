package gmae;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple login screen for GMAE - just gets two Player names
 */
public class LoginScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JTextField playerOneField;
    private JTextField playerTwoField;
    private JButton submitButton;
    
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
        
        // Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(submitButton, gbc);
        
        add(loginPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
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
        
        // Create PlayerProfile objects
        PlayerProfile player1 = new PlayerProfile(playerOneName, playerOneName);
        PlayerProfile player2 = new PlayerProfile(playerTwoName, playerTwoName);
        
        JOptionPane.showMessageDialog(this, 
            "Welcome to GuildQuest!\nPlayer 1: " + playerOneName + "\nPlayer 2: " + playerTwoName, 
            "Adventure Ready!", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Pass players to adventure menu
        mainGUI.showAdventureMenu(player1, player2);
    }
}
