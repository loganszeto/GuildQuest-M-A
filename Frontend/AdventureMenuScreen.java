package gmae;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import backend.RealmSpace;

public class AdventureMenuScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JButton addQuestButton;
    private JButton backButton;
    private JButton startAdventureButton;
    private JButton viewMapButton;
    private JLabel titleLabel;
    private JList<MiniAdventure> adventureList;
    private DefaultListModel<MiniAdventure> adventureListModel;
    private JButton deleteAdventureButton;
    private JRadioButton competitiveRadio;
    private JRadioButton coOpRadio;
    private ButtonGroup modeGroup;
    private JComboBox<RealmSpace> realmSelector;
    private JLabel currentRealmLabel;
    
    // Player profiles
    private PlayerProfile player1;
    private PlayerProfile player2;
    
    // Game manager
    private TwoPlayerGameManager gameManager;
    
    public AdventureMenuScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.gameManager = new TwoPlayerGameManager();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDefaultAdventures();
    }
    
    public void setPlayers(PlayerProfile player1, PlayerProfile player2) {
        this.player1 = player1;
        this.player2 = player2;
        updateButtonStates();
    }
    
    private void initializeComponents() {
        addQuestButton = new JButton("Add Adventure");
        backButton = new JButton("Retreat");
        startAdventureButton = new JButton("Begin Selected Adventure");
        viewMapButton = new JButton("View Map");
        titleLabel = new JLabel("Mini-Adventure Menu");
        deleteAdventureButton = new JButton("Delete Adventure");
        
        competitiveRadio = new JRadioButton("Competitive");
        coOpRadio = new JRadioButton("Co-op");
        modeGroup = new ButtonGroup();
        
        realmSelector = new JComboBox<>();
        currentRealmLabel = new JLabel("Current Realm: None Selected");
        
        competitiveRadio.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        competitiveRadio.setForeground(new Color(255, 215, 0));
        competitiveRadio.setBackground(new Color(35, 35, 70));
        competitiveRadio.setSelected(true);
        
        coOpRadio.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        coOpRadio.setForeground(new Color(255, 215, 0));
        coOpRadio.setBackground(new Color(35, 35, 70));
        
        modeGroup.add(competitiveRadio);
        modeGroup.add(coOpRadio);
        
        adventureListModel = new DefaultListModel<>();
        adventureList = new JList<>(adventureListModel);
        adventureList.setBackground(new Color(60, 60, 100));
        adventureList.setForeground(Color.WHITE);
        adventureList.setFont(new Font("Arial", Font.PLAIN, 14));
        adventureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adventureList.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        
        styleButton(addQuestButton);
        styleButton(backButton);
        styleButton(startAdventureButton);
        styleButton(viewMapButton);
        styleButton(deleteAdventureButton);
        
        loadRealms();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(25, 25, 60));
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titlePanel.add(titleLabel);
        
        JPanel realmPanel = new JPanel(new FlowLayout());
        realmPanel.setBackground(new Color(25, 25, 60));
        realmPanel.add(new JLabel("Select Realm:"));
        realmPanel.add(realmSelector);
        realmPanel.add(currentRealmLabel);
        titlePanel.add(realmPanel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(45, 45, 80));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel questPanel = new JPanel(new BorderLayout());
        questPanel.setBackground(new Color(35, 35, 70));
        questPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 1),
                "Available Mini-Adventures",
                0, 0,
                new Font("Old English Text MT", Font.BOLD, 18),
                new Color(255, 215, 0)
            )
        ));
        
        JScrollPane scrollPane = new JScrollPane(adventureList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        questPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel modePanel = new JPanel(new FlowLayout());
        modePanel.setBackground(new Color(35, 35, 70));
        modePanel.add(new JLabel("Game Mode:"));
        modePanel.add(competitiveRadio);
        modePanel.add(coOpRadio);
        questPanel.add(modePanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(new Color(35, 35, 70));
        buttonPanel.add(startAdventureButton);
        buttonPanel.add(deleteAdventureButton);
        questPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(new Color(45, 45, 80));
        controlPanel.add(addQuestButton);
        controlPanel.add(viewMapButton);
        
        centerPanel.add(questPanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.NORTH);
        
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(25, 25, 60));
        bottomPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel mottoLabel = new JLabel("Choose Your Destiny Wisely");
        mottoLabel.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        mottoLabel.setForeground(new Color(255, 215, 0));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(mottoLabel, BorderLayout.CENTER);
        
        JPanel bottomButtonPanel = new JPanel(new FlowLayout());
        bottomButtonPanel.setBackground(new Color(25, 25, 60));
        bottomButtonPanel.add(backButton);
        bottomPanel.add(bottomButtonPanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        addQuestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddAdventure();
            }
        });
        
        startAdventureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartAdventure();
            }
        });
        
        deleteAdventureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteAdventure();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRetreat();
            }
        });
        
        viewMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewMap();
            }
        });
        
        adventureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        realmSelector.addActionListener(e -> {
            updateCurrentRealm();
            filterAdventuresByRealm();
        });
    }
    
    private void loadDefaultAdventures() {
        final RealmSpace defaultRealm;
        RealmSpace selectedRealm = (RealmSpace) realmSelector.getSelectedItem();
        if (selectedRealm == null) {
            defaultRealm = new RealmSpace("Mystic Realms");
        } else {
            defaultRealm = selectedRealm;
        }
        
        adventureListModel.addElement(new RelicHuntAdventure());
        
        // Add more sample adventures
        adventureListModel.addElement(new MiniAdventure() {
            @Override
            public void initialize(PlayerProfile p1, PlayerProfile p2, Map<String, Object> settings) {}
            @Override
            public void start() {}
            @Override
            public boolean acceptPlayerInput(int playerNum, String input) { return false; }
            @Override
            public void advanceTurn() {}
            @Override
            public Map<String, Object> getCurrentState() { return new HashMap<>(); }
            @Override
            public boolean isComplete() { return false; }
            @Override
            public int getWinner() { return 0; }
            @Override
            public void reset() {}
            @Override
            public String getName() { return "Timed Raid"; }
            @Override
            public String getDescription() { return "Complete objectives before time runs out!"; }
            @Override
            public RealmSpace getRealm() { return defaultRealm; }
            @Override
            public boolean supportsCoOp() { return true; }
            @Override
            public boolean supportsCompetitive() { return false; }
            @Override
            public String toString() { return getName(); } // Fix display name
        });
    }
    
    private void loadRealms() {
        realmSelector.addItem(new RealmSpace("Mystic Realms"));
        realmSelector.addItem(new RealmSpace("Shadowlands"));
        realmSelector.addItem(new RealmSpace("Crystal Mountains"));
        realmSelector.addItem(new RealmSpace("Dragon's Lair"));
        
        if (realmSelector.getItemCount() > 0) {
            realmSelector.setSelectedIndex(0);
        }
    }
    
    private void updateCurrentRealm() {
        RealmSpace selectedRealm = (RealmSpace) realmSelector.getSelectedItem();
        if (selectedRealm != null) {
            currentRealmLabel.setText("Current Realm: " + selectedRealm.getName());
        }
    }
    
    private void filterAdventuresByRealm() {
        // For now, just show all adventures
        // Could be extended to filter by realm
    }
    
    private void handleAddAdventure() {
        JOptionPane.showMessageDialog(this, 
            "Add Adventure feature coming soon!\n\nFor now, try the included Relic Hunt adventure.", 
            "Add Adventure", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Set up and start the game
        boolean success = gameManager.setupGame(player1, player2, selectedAdventure, isCompetitive);
        
        if (success) {
            String message = "Starting " + selectedAdventure.getName() + "!\n\n" + 
                selectedAdventure.getDescription() + "\n\n" +
                "Mode: " + (isCompetitive ? "Competitive" : "Co-op") + "\n" +
                "Player 1: " + player1.getCharacterName() + "\n" +
                "Player 2: " + player2.getCharacterName();
            
            JOptionPane.showMessageDialog(this, message, 
                "Adventure Starting!", JOptionPane.INFORMATION_MESSAGE);
            
            // For now, just show a simple game screen
            showAdventureGameScreen(selectedAdventure);
        }
    }
    
    private void showAdventureGameScreen(MiniAdventure adventure) {
        // Create a simple game screen
        JFrame gameFrame = new JFrame(adventure.getName());
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(800, 600);
        gameFrame.setLocationRelativeTo(this);
        
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(25, 25, 60));
        
        // Game info
        JLabel infoLabel = new JLabel("<html><div style='color: gold; font-family: Arial; font-size: 14px;'>" +
            "Adventure: " + adventure.getName() + "<br>" +
            "Mode: " + (gameManager.isCompetitive() ? "Competitive" : "Co-op") + "<br>" +
            "Current Turn: Player " + gameManager.getCurrentPlayerTurn() + "<br>" +
            "Use WASD or Arrow Keys to move<br>" +
            "Close this window to return to menu</div></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gamePanel.add(infoLabel, BorderLayout.NORTH);
        
        // Add realm map
        RealmMapPanel mapPanel = new RealmMapPanel(adventure.getRealm());
        gamePanel.add(mapPanel, BorderLayout.CENTER);
        
        // Control buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(25, 25, 60));
        
        JButton resetButton = new JButton("Reset Adventure");
        resetButton.addActionListener(e -> {
            gameManager.resetGame();
            JOptionPane.showMessageDialog(gameFrame, "Adventure reset!");
        });
        
        controlPanel.add(resetButton);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);
        
        gameFrame.add(gamePanel);
        gameFrame.setVisible(true);
    }
    
    private void handleDeleteAdventure() {
        MiniAdventure selectedAdventure = adventureList.getSelectedValue();
        if (selectedAdventure == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete \"" + selectedAdventure.getName() + "\"?",
            "Delete Adventure",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            adventureListModel.removeElement(selectedAdventure);
        }
    }
    
    private void updateButtonStates() {
        boolean hasSelection = adventureList.getSelectedValue() != null;
        boolean hasPlayers = player1 != null && player2 != null;
        
        startAdventureButton.setEnabled(hasSelection && hasPlayers);
        deleteAdventureButton.setEnabled(hasSelection);
        
        if (hasSelection) {
            MiniAdventure selectedAdventure = adventureList.getSelectedValue();
            competitiveRadio.setEnabled(selectedAdventure.supportsCompetitive());
            coOpRadio.setEnabled(selectedAdventure.supportsCoOp());
        } else {
            competitiveRadio.setEnabled(false);
            coOpRadio.setEnabled(false);
        }
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(200, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        button.setFocusPainted(false);
    }
    
    private void handleRetreat() {
        mainGUI.showLoginScreen();
    }
    
    private void handleViewMap() {
        RealmSpace currentRealm = (RealmSpace) realmSelector.getSelectedItem();
        if (currentRealm == null) {
            currentRealm = new RealmSpace("Mystic Realms");
        }
        
        RealmMapPanel mapPanel = new RealmMapPanel(currentRealm);
        JScrollPane scrollPane = new JScrollPane(mapPanel);
        
        JFrame mapFrame = new JFrame("Realm Map: " + currentRealm.getName());
        mapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mapFrame.setSize(800, 600);
        mapFrame.setLocationRelativeTo(this);
        
        JLabel infoLabel = new JLabel("Click tiles to inspect them");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mapFrame.add(infoLabel, BorderLayout.NORTH);
        mapFrame.add(scrollPane, BorderLayout.CENTER);
        
        mapFrame.setVisible(true);
    }
}
