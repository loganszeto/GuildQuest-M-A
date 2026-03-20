package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Backend.User;
import Backend.RealmSpace;

public class AdventureMenuScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JButton addQuestButton;
    private JButton backButton;
    private JButton startAdventureButton;
    private JButton settingsButton;
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
    private User player1;
    private User player2;
    
    // Game manager
    private TwoPlayerGameManager gameManager;
    private AccessControlService accessControlService;
    
    public AdventureMenuScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.gameManager = new TwoPlayerGameManager();
        this.accessControlService = new AccessControlService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDefaultAdventures();
    }
    
    public void setPlayers(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        updateButtonStates();
    }
    
    private void initializeComponents() {
        addQuestButton = new JButton("Add Adventure");
        backButton = new JButton("Retreat");
        startAdventureButton = new JButton("Begin Selected Adventure");
        settingsButton = new JButton("Settings");
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
        styleButton(settingsButton);
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
        controlPanel.add(settingsButton);
        controlPanel.add(addQuestButton);
        
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

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSettings();
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
        List<MiniAdventure> adventures = AdventureRegistry.createDefaultAdventures();
        for (MiniAdventure adventure : adventures) {
            adventureListModel.addElement(adventure);
        }
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
            mainGUI.getSettings().setCurrentRealm(selectedRealm);
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
        if (!accessControlService.canPerform(AccessControlService.Action.START_ADVENTURE, player1, player1)) {
            JOptionPane.showMessageDialog(
                this,
                "Access denied: only host can start adventures.",
                "Permission Denied",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        MiniAdventure secureAdventure = new SecureMiniAdventureProxy(selectedAdventure);
        boolean success = gameManager.setupGame(player1, player2, secureAdventure, isCompetitive);
        
        if (success) {
            // Start the game using the new GameScreen
            mainGUI.showGameScreen(secureAdventure, player1, player2, isCompetitive);
        }
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
            if (!accessControlService.canPerform(AccessControlService.Action.DELETE_ADVENTURE, player1, player1)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Access denied: only host can delete adventures.",
                    "Permission Denied",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
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
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        button.setFocusPainted(false);
    }
    
    private void handleRetreat() {
        mainGUI.showLoginScreen();
    }

    private void handleSettings() {
        mainGUI.showSettingsScreen(player1, player2);
    }
}
