package gmae;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JTextField playerOneField;
    private JTextField playerTwoField;
    private JButton submitButton;
    private JButton backButton;
    
    public LoginScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        playerOneField = new JTextField(20);
        playerTwoField = new JTextField(20);
        submitButton = new JButton("Start Adventure");
        backButton = new JButton("Back");
        
        playerOneField.setText("Player One Username");
        playerTwoField.setText("Player Two Username");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(50, 50, 80));
        JLabel titleLabel = new JLabel("GuildQuest Mini-Adventure Environment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Center panel for login form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login form
        JPanel loginForm = new JPanel(new GridBagLayout());
        loginForm.setBackground(Color.WHITE);
        loginForm.setBorder(BorderFactory.createTitledBorder("Player Login"));
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        
        // Player One
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        loginForm.add(new JLabel("Player One:"), gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridy = 0;
        loginForm.add(playerOneField, gbcForm);
        
        // Player Two
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        loginForm.add(new JLabel("Player Two:"), gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridy = 1;
        loginForm.add(playerTwoField, gbcForm);
        
        // Buttons
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        gbcForm.gridwidth = 2;
        gbcForm.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        loginForm.add(buttonPanel, gbcForm);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(loginForm, gbc);
        
        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(50, 50, 80));
        bottomPanel.setPreferredSize(new Dimension(0, 50));
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear fields
                playerOneField.setText("Player One Username");
                playerTwoField.setText("Player Two Username");
            }
        });
        
        // Add focus listeners to clear placeholder text
        playerOneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (playerOneField.getText().equals("Player One Username")) {
                    playerOneField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (playerOneField.getText().isEmpty()) {
                    playerOneField.setText("Player One Username");
                }
            }
        });
        
        playerTwoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (playerTwoField.getText().equals("Player Two Username")) {
                    playerTwoField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (playerTwoField.getText().isEmpty()) {
                    playerTwoField.setText("Player Two Username");
                }
            }
        });
    }
    
    private void handleSubmit() {
        String playerOneUsername = playerOneField.getText().trim();
        String playerTwoUsername = playerTwoField.getText().trim();
        
        // Validate input
        if (playerOneUsername.isEmpty() || playerOneUsername.equals("Player One Username") ||
            playerTwoUsername.isEmpty() || playerTwoUsername.equals("Player Two Username")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid usernames for both players!", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        //PlayerProfile player1 = new PlayerProfile(playerOneUsername);
        //PlayerProfile player2 = new PlayerProfile(playerTwoUsername);
        
        // Show success message (in full version, this would show adventure menu)
        JOptionPane.showMessageDialog(this, 
            "Login successful!\nPlayer 1: " + playerOneUsername + "\nPlayer 2: " + playerTwoUsername, 
            "Login Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // For now, just show placeholder for next step
        mainGUI.showAdventureMenu();
    }
}
