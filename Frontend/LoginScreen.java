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
    private JLabel titleLabel;
    
    public LoginScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        playerOneField = new JTextField(20);
        playerTwoField = new JTextField(20);
        submitButton = new JButton("Begin Quest");
        backButton = new JButton("Retreat");
        titleLabel = new JLabel("GuildQuest - Mini Adventure Environment");
        
        playerOneField.setText("Enter Player Name");
        playerTwoField.setText("Enter Player Name");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(25, 25, 60));
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titlePanel.add(titleLabel);
        
  
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(45, 45, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        

        JPanel loginForm = new JPanel(new GridBagLayout());
        loginForm.setBackground(new Color(35, 35, 70));
        loginForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 1),
                "Enter the Realm",
                0, 0,
                new Font("Old English Text MT", Font.BOLD, 18),
                new Color(255, 215, 0)
            )
        ));
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(8, 8, 8, 8);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        

        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        JLabel playerOneLabel = new JLabel("Player I:");
        playerOneLabel.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        playerOneLabel.setForeground(new Color(255, 215, 0));
        loginForm.add(playerOneLabel, gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridy = 0;
        playerOneField.setBackground(new Color(60, 60, 100));
        playerOneField.setForeground(Color.WHITE);
        playerOneField.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        playerOneField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginForm.add(playerOneField, gbcForm);
        
    
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        JLabel playerTwoLabel = new JLabel("Player II:");
        playerTwoLabel.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        playerTwoLabel.setForeground(new Color(255, 215, 0));
        loginForm.add(playerTwoLabel, gbcForm);
        
        gbcForm.gridx = 1;
        gbcForm.gridy = 1;
        playerTwoField.setBackground(new Color(60, 60, 100));
        playerTwoField.setForeground(Color.WHITE);
        playerTwoField.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        playerTwoField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginForm.add(playerTwoField, gbcForm);
        
        gbcForm.gridx = 0;
        gbcForm.gridy = 2;
        gbcForm.gridwidth = 2;
        gbcForm.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(35, 35, 70));
        
        submitButton.setBackground(new Color(200, 50, 50));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        submitButton.setFocusPainted(false);
        
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
        backButton.setFocusPainted(false);
        
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        loginForm.add(buttonPanel, gbcForm);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(loginForm, gbc);
        
      
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
      
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(25, 25, 60));
        bottomPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel mottoLabel = new JLabel("Adventure Awaits the Brave");
        mottoLabel.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        mottoLabel.setForeground(new Color(255, 215, 0));
        bottomPanel.add(mottoLabel);
        
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
                playerOneField.setText("Enter Player Name");
                playerTwoField.setText("Enter Player Name");
            }
        });
        

        playerOneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (playerOneField.getText().equals("Enter Player Name")) {
                    playerOneField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (playerOneField.getText().isEmpty()) {
                    playerOneField.setText("Enter Player Name");
                }
            }
        });
        
        playerTwoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (playerTwoField.getText().equals("Enter Player Name")) {
                    playerTwoField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (playerTwoField.getText().isEmpty()) {
                    playerTwoField.setText("Enter Player Name");
                }
            }
        });
    }
    
    private void handleSubmit() {
        String playerOneUsername = playerOneField.getText().trim();
        String playerTwoUsername = playerTwoField.getText().trim();
   
        if (playerOneUsername.isEmpty() || playerOneUsername.equals("Enter Player Name") ||
            playerTwoUsername.isEmpty() || playerTwoUsername.equals("Enter Player Name")) {
            JOptionPane.showMessageDialog(this, 
                "Both players must have names to enter the realm!", 
                "Names Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, 
            "The realm welcomes you!\nPlayer I: " + playerOneUsername + "\nPlayer II: " + playerTwoUsername, 
            "Quest Accepted!", 
            JOptionPane.INFORMATION_MESSAGE);
        
        mainGUI.showAdventureMenu();
    }
}
