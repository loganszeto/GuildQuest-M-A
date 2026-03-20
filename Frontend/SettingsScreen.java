package Frontend;

import Backend.User;
import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends JPanel {
    private final GMAEGUI mainGUI;
    private final Settings settings;

    private JLabel themeValueLabel;
    private JLabel timeValueLabel;
    private JLabel realmValueLabel;

    private User player1;
    private User player2;

    public SettingsScreen(GMAEGUI mainGUI, Settings settings) {
        this.mainGUI = mainGUI;
        this.settings = settings;
        initializeComponents();
        setupLayout();
        refreshView();
    }

    public void setPlayers(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    private void initializeComponents() {
        themeValueLabel = new JLabel();
        timeValueLabel = new JLabel();
        realmValueLabel = new JLabel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 60));

        JLabel title = new JLabel("Settings");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Old English Text MT", Font.BOLD, 30));
        title.setForeground(new Color(255, 215, 0));
        title.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(45, 45, 80));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel themeLabel = createRowLabel("Theme:");
        JLabel timeLabel = createRowLabel("Time Display Preference:");
        JLabel realmLabel = createRowLabel("Current Realm:");

        styleValueLabel(themeValueLabel);
        styleValueLabel(timeValueLabel);
        styleValueLabel(realmValueLabel);

        JButton changeTheme = createButton("Change Theme");
        JButton changeTime = createButton("Change Time Display");
        JButton back = createButton("Back");

        changeTheme.addActionListener(e -> {
            settings.updateSettings(0);
            refreshView();
            mainGUI.applyCurrentTheme();
        });
        changeTime.addActionListener(e -> {
            settings.updateSettings(1);
            refreshView();
        });
        back.addActionListener(e -> {
            if (player1 != null && player2 != null) {
                mainGUI.showAdventureMenu(player1, player2);
            } else {
                mainGUI.showAdventureMenu();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(themeLabel, gbc);
        gbc.gridx = 1;
        center.add(themeValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        center.add(timeLabel, gbc);
        gbc.gridx = 1;
        center.add(timeValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        center.add(realmLabel, gbc);
        gbc.gridx = 1;
        center.add(realmValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(changeTheme);
        buttonRow.add(changeTime);
        buttonRow.add(back);
        center.add(buttonRow, gbc);

        add(center, BorderLayout.CENTER);
    }

    private JLabel createRowLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(255, 215, 0));
        label.setFont(new Font("Old English Text MT", Font.BOLD, 16));
        return label;
    }

    private void styleValueLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(200, 50, 50));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Old English Text MT", Font.BOLD, 14));
        b.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        b.setFocusPainted(false);
        return b;
    }

    public void refreshView() {
        themeValueLabel.setText(settings.getTheme().toString());
        timeValueLabel.setText(settings.getTdp().toString());
        realmValueLabel.setText(
                settings.getCurrentRealm() == null ? "None Selected" : settings.getCurrentRealm().getName()
        );
    }
}
