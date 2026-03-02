package gmae;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdventureMenuScreen extends JPanel {
    private GMAEGUI mainGUI;
    private JButton addQuestButton;
    private JButton backButton;
    private JLabel titleLabel;
    private JList<Quest> questList;
    private DefaultListModel<Quest> questListModel;
    private JButton startQuestButton;
    private JButton deleteQuestButton;
    private JRadioButton competitiveRadio;
    private JRadioButton coOpRadio;
    private ButtonGroup modeGroup;
    
    public AdventureMenuScreen(GMAEGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDefaultQuests();
    }
    
    private void initializeComponents() {
        addQuestButton = new JButton("Add Quest");
        backButton = new JButton("Retreat");
        titleLabel = new JLabel("Quest Board");
        startQuestButton = new JButton("Begin Selected Quest");
        deleteQuestButton = new JButton("Delete Quest");
        
        competitiveRadio = new JRadioButton("Competitive");
        coOpRadio = new JRadioButton("Co-op");
        modeGroup = new ButtonGroup();
        
        competitiveRadio.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        competitiveRadio.setForeground(new Color(255, 215, 0));
        competitiveRadio.setBackground(new Color(35, 35, 70));
        competitiveRadio.setSelected(true);
        
        coOpRadio.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        coOpRadio.setForeground(new Color(255, 215, 0));
        coOpRadio.setBackground(new Color(35, 35, 70));
        
        modeGroup.add(competitiveRadio);
        modeGroup.add(coOpRadio);
        
        questListModel = new DefaultListModel<>();
        questList = new JList<>(questListModel);
        questList.setBackground(new Color(60, 60, 100));
        questList.setForeground(Color.WHITE);
        questList.setFont(new Font("Arial", Font.PLAIN, 14));
        questList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questList.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 1));
        
        styleButton(addQuestButton);
        styleButton(backButton);
        styleButton(startQuestButton);
        styleButton(deleteQuestButton);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(25, 25, 60));
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titlePanel.add(titleLabel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(45, 45, 80));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel questPanel = new JPanel(new BorderLayout());
        questPanel.setBackground(new Color(35, 35, 70));
        questPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 1),
                "Available Quests",
                0, 0,
                new Font("Old English Text MT", Font.BOLD, 18),
                new Color(255, 215, 0)
            )
        ));
        
        JScrollPane scrollPane = new JScrollPane(questList);
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
        buttonPanel.add(startQuestButton);
        buttonPanel.add(deleteQuestButton);
        questPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(new Color(45, 45, 80));
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
                handleAddQuest();
            }
        });
        
        startQuestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartQuest();
            }
        });
        
        deleteQuestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteQuest();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRetreat();
            }
        });
        
        questList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
    }
    
    private void loadDefaultQuests() {
        questListModel.addElement(new Quest("Relic Hunt", 
            "Ancient artifacts are scattered throughout the realm. Heroes must compete or cooperate to collect these valuable relics.", 
            "Moderate", "30-60 minutes", true));
        
        questListModel.addElement(new Quest("Timed Raid Window", 
            "The ancient dungeon is only accessible during specific celestial alignments. Complete objectives before the window closes!", 
            "Hard", "45 minutes", false));
    }
    
    private void handleAddQuest() {
        JTextField nameField = new JTextField();
        JTextArea descArea = new JTextArea(4, 20);
        JTextField difficultyField = new JTextField();
        JTextField durationField = new JTextField();
        JCheckBox supportsModesCheckBox = new JCheckBox("Supports Competitive/Co-op modes");
        supportsModesCheckBox.setSelected(true);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Quest Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descArea));
        panel.add(new JLabel("Difficulty:"));
        panel.add(difficultyField);
        panel.add(new JLabel("Duration:"));
        panel.add(durationField);
        panel.add(supportsModesCheckBox);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Quest", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descArea.getText().trim();
            String difficulty = difficultyField.getText().trim();
            String duration = durationField.getText().trim();
            
            if (!name.isEmpty() && !description.isEmpty()) {
                Quest newQuest = new Quest(name, description, difficulty, duration, supportsModesCheckBox.isSelected());
                questListModel.addElement(newQuest);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in at least the name and description!", 
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void handleStartQuest() {
        Quest selectedQuest = questList.getSelectedValue();
        if (selectedQuest == null) return;
        
        String mode = competitiveRadio.isSelected() ? "Competitive" : "Co-op";
        String message = "Starting " + selectedQuest.getName() + " in " + mode + " mode!\n\n" + selectedQuest.getDescription();
        
        if (selectedQuest.supportsModes()) {
            message += "\n\nMode: " + mode;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            message + "\n\nThis adventure cannot be paused once started.",
            "Begin " + selectedQuest.getName(),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, 
                selectedQuest.getName() + " starting soon!\n\nThe adventure awaits...", 
                "Quest Accepted!", 
                JOptionPane.INFORMATION_MESSAGE);
            
            mainGUI.showAdventureScreen();
        }
    }
    
    private void handleDeleteQuest() {
        Quest selectedQuest = questList.getSelectedValue();
        if (selectedQuest == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete \"" + selectedQuest.getName() + "\"?",
            "Delete Quest",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            questListModel.removeElement(selectedQuest);
        }
    }
    
    private void updateButtonStates() {
        boolean hasSelection = questList.getSelectedValue() != null;
        startQuestButton.setEnabled(hasSelection);
        deleteQuestButton.setEnabled(hasSelection);
        
        if (hasSelection) {
            Quest selectedQuest = questList.getSelectedValue();
            competitiveRadio.setEnabled(selectedQuest.supportsModes());
            coOpRadio.setEnabled(selectedQuest.supportsModes());
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
    
    public static class Quest {
        private String name;
        private String description;
        private String difficulty;
        private String duration;
        private boolean supportsModes;
        
        public Quest(String name, String description, String difficulty, String duration, boolean supportsModes) {
            this.name = name;
            this.description = description;
            this.difficulty = difficulty;
            this.duration = duration;
            this.supportsModes = supportsModes;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getDifficulty() { return difficulty; }
        public String getDuration() { return duration; }
        public boolean supportsModes() { return supportsModes; }
        
        @Override
        public String toString() {
            return name + " (" + difficulty + ") - " + duration;
        }
    }
}
