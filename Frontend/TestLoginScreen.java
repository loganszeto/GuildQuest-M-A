package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple test login screen that creates TestUser objects
 */
public class TestLoginScreen extends JPanel {
    private TestGMAEGUI mainGUI;
    private JTextField player1Field;
    private JTextField player2Field;
    private JButton loginButton;
    
    public TestLoginScreen(TestGMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        player1Field = new JTextField("Player1", 15);
        player2Field = new JTextField("Player2", 15);
        loginButton = new JButton("Start Adventure");
        
        styleButton(loginButton);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 80));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 40));
        
        // Title
        JLabel titleLabel = new JLabel("GuildQuest - Test Adventure", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 215, 0));
        
        // Login panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBackground(new Color(20, 20, 40));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        loginPanel.add(new JLabel("Player 1 Name:"));
        loginPanel.add(player1Field);
        loginPanel.add(new JLabel("Player 2 Name:"));
        loginPanel.add(player2Field);
        loginPanel.add(new JLabel()); // Empty space
        loginPanel.add(loginButton);
        
        // Instructions
        JLabel instructions = new JLabel(
            "<html><div style='color: white; text-align: center; font-family: Arial;'>" +
            "Welcome to the GuildQuest Test Adventure!<br><br>" +
            "This is a demonstration of the game mechanics including:<br>" +
            "• Two-player turn-based movement (WASD or Arrow Keys)<br>" +
            "• Relic collection gameplay<br>" +
            "• Competitive and Co-op modes<br>" +
            "• Win/lose conditions and game rules<br><br>" +
            "Enter player names and click Start Adventure to begin!</div></html>",
            JLabel.CENTER
        );
        instructions.setForeground(Color.WHITE);
        
        add(titleLabel, BorderLayout.NORTH);
        add(instructions, BorderLayout.CENTER);
        add(loginPanel, BorderLayout.SOUTH);
        
        setPreferredSize(new Dimension(600, 400));
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Allow Enter key to login
        ActionListener enterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        };
        player1Field.addActionListener(enterListener);
        player2Field.addActionListener(enterListener);
    }
    
    private void handleLogin() {
        String player1Name = player1Field.getText().trim();
        String player2Name = player2Field.getText().trim();
        
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter names for both players!", 
                "Missing Names", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (player1Name.equals(player2Name)) {
            JOptionPane.showMessageDialog(this, 
                "Players must have different names!", 
                "Duplicate Names", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create test users
        TestUser player1 = new TestUser(player1Name);
        TestUser player2 = new TestUser(player2Name);
        
        // Show adventure menu
        mainGUI.showTestAdventureMenu(player1, player2);
    }
}
