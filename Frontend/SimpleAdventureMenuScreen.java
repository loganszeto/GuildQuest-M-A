package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple test adventure menu that doesn't rely on RealmMapPanel
 */
public class SimpleAdventureMenuScreen extends JPanel {
    private TestGMAEGUI mainGUI;
    private TestUser player1;
    private TestUser player2;
    
    private JList<MiniAdventure> adventureList;
    private JButton startButton;
    private JButton backButton;
    private JRadioButton competitiveRadio;
    private JRadioButton coOpRadio;
    
    public SimpleAdventureMenuScreen(TestGMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        // Create adventure list
        List<MiniAdventure> adventures = new ArrayList<>();
        adventures.add(new TestAdventure());
        
        adventureList = new JList<>(adventures.toArray(new MiniAdventure[0]));
        adventureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adventureList.setSelectedIndex(0);
        
        // Mode selection
        competitiveRadio = new JRadioButton("Competitive", true);
        coOpRadio = new JRadioButton("Co-op");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(competitiveRadio);
        modeGroup.add(coOpRadio);
        
        // Buttons
        startButton = new JButton("Start Adventure");
        backButton = new JButton("Back to Login");
        
        styleButton(startButton);
        styleButton(backButton);
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
        JLabel titleLabel = new JLabel("Select Your Adventure", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 215, 0));
        
        // Adventure list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(20, 20, 40));
        listPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
            "Available Adventures",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(255, 215, 0)
        ));
        
        adventureList.setBackground(new Color(30, 30, 50));
        adventureList.setForeground(Color.WHITE);
        adventureList.setSelectionBackground(new Color(255, 215, 0));
        adventureList.setSelectionForeground(Color.BLACK);
        
        listPanel.add(new JScrollPane(adventureList), BorderLayout.CENTER);
        
        // Mode selection panel
        JPanel modePanel = new JPanel(new FlowLayout());
        modePanel.setBackground(new Color(20, 20, 40));
        modePanel.add(new JLabel("Game Mode:"));
        modePanel.add(competitiveRadio);
        modePanel.add(coOpRadio);
        
        // Player info panel
        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        playerPanel.setBackground(new Color(20, 20, 40));
        playerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
            "Players",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(255, 215, 0)
        ));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(20, 20, 40));
        buttonPanel.add(startButton);
        buttonPanel.add(backButton);
        
        // Main layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(20, 20, 40));
        centerPanel.add(listPanel, BorderLayout.CENTER);
        centerPanel.add(modePanel, BorderLayout.SOUTH);
        
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        
        updatePlayerInfo();
    }
    
    private void setupEventHandlers() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartAdventure();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainGUI.showLoginScreen();
            }
        });
        
        adventureList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            updateAdventureInfo();
        });
    }
    
    private void updatePlayerInfo() {
        removeAll();
        setupLayout();
        
        if (player1 != null && player2 != null) {
            JPanel playerPanel = new JPanel(new GridLayout(2, 1));
            playerPanel.setBackground(new Color(20, 20, 40));
            playerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                "Players",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(255, 215, 0)
            ));
            
            JLabel player1Label = new JLabel("Player 1: " + player1.getUsername());
            JLabel player2Label = new JLabel("Player 2: " + player2.getUsername());
            player1Label.setForeground(Color.WHITE);
            player2Label.setForeground(Color.WHITE);
            
            playerPanel.add(player1Label);
            playerPanel.add(player2Label);
            
            add(playerPanel, BorderLayout.EAST);
        }
        
        revalidate();
        repaint();
    }
    
    private void updateAdventureInfo() {
        MiniAdventure selected = adventureList.getSelectedValue();
        if (selected != null) {
            // Update mode availability based on adventure support
            competitiveRadio.setEnabled(selected.supportsCompetitive());
            coOpRadio.setEnabled(selected.supportsCoOp());
            
            if (!selected.supportsCompetitive()) {
                coOpRadio.setSelected(true);
            } else if (!selected.supportsCoOp()) {
                competitiveRadio.setSelected(true);
            }
        }
    }
    
    private void handleStartAdventure() {
        if (player1 == null || player2 == null) {
            JOptionPane.showMessageDialog(this, 
                "Please ensure both players are logged in before starting an adventure.", 
                "Players Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        MiniAdventure selectedAdventure = adventureList.getSelectedValue();
        if (selectedAdventure == null) return;
        
        boolean isCompetitive = competitiveRadio.isSelected();
        
        // Check if adventure supports the selected mode
        if (isCompetitive && !selectedAdventure.supportsCompetitive()) {
            JOptionPane.showMessageDialog(this, 
                "This adventure doesn't support competitive mode. Try co-op mode!", 
                "Mode Not Supported", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isCompetitive && !selectedAdventure.supportsCoOp()) {
            JOptionPane.showMessageDialog(this, 
                "This adventure doesn't support co-op mode. Try competitive mode!", 
                "Mode Not Supported", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Start the game using the SimpleGameScreen
        mainGUI.showGameScreen(selectedAdventure, player1, player2, isCompetitive);
    }
    
    public void setPlayers(TestUser player1, TestUser player2) {
        this.player1 = player1;
        this.player2 = player2;
        updatePlayerInfo();
    }
}
