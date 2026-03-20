package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import Backend.RelicHuntGameBackend;

public class GMAEGUI extends JFrame {
    @FunctionalInterface
    private interface AdventureScreenLauncher {
        void launch(MiniAdventure adventure, Backend.User player1, Backend.User player2, boolean competitive);
    }

    private LoginScreen loginScreen;
    private AdventureMenuScreen adventureMenuScreen;
    private RelicHuntGameScreen relicHuntGameScreen;
    private SettingsScreen settingsScreen;
    private Settings settings;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private final Map<Class<?>, AdventureScreenLauncher> adventureLaunchers = new LinkedHashMap<>();
    
    public static final String LOGIN_CARD = "LOGIN";
    public static final String ADVENTURE_MENU_CARD = "ADVENTURE_MENU";
    public static final String RELIC_HUNT_CARD = "RELIC_HUNT";
    public static final String SETTINGS_CARD = "SETTINGS";
    
    public GMAEGUI() {
        initializeComponents();
        setupFrame();
        showLoginScreen();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        settings = new Settings();
        
        loginScreen = new LoginScreen(this);
        adventureMenuScreen = new AdventureMenuScreen(this);
        relicHuntGameScreen = new RelicHuntGameScreen(this);
        settingsScreen = new SettingsScreen(this, settings);
        
        mainPanel.add(loginScreen, LOGIN_CARD);
        mainPanel.add(adventureMenuScreen, ADVENTURE_MENU_CARD);
        mainPanel.add(relicHuntGameScreen, RELIC_HUNT_CARD);
        mainPanel.add(settingsScreen, SETTINGS_CARD);
        registerAdventureLaunchers();
        applyCurrentTheme();
    }

    private void registerAdventureLaunchers() {
        adventureLaunchers.put(RelicHuntGameBackend.class, (adventure, player1, player2, competitive) -> {
            RelicHuntGameBackend relicHuntBackend = (RelicHuntGameBackend) adventure;
            relicHuntGameScreen.startAdventure(relicHuntBackend, player1, player2, competitive);
            cardLayout.show(mainPanel, RELIC_HUNT_CARD);
        });
    }
    
    private void setupFrame() {
        setTitle("GuildQuest - Mini Adventure Environment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        try {
            setIconImage(createGameIcon());
        } catch (Exception e) {

        }
        
        add(mainPanel);
    }
    
    public void showLoginScreen() {
        cardLayout.show(mainPanel, LOGIN_CARD);
    }
    
    public void showAdventureMenu() {
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }
    
    public void showAdventureMenu(Backend.User player1, Backend.User player2) {
        adventureMenuScreen.setPlayers(player1, player2);
        cardLayout.show(mainPanel, ADVENTURE_MENU_CARD);
    }

    public void showSettingsScreen(Backend.User player1, Backend.User player2) {
        settingsScreen.setPlayers(player1, player2);
        settingsScreen.refreshView();
        cardLayout.show(mainPanel, SETTINGS_CARD);
    }

    public Settings getSettings() {
        return settings;
    }

    public void applyCurrentTheme() {
        ThemePalette palette = ThemePalette.forTheme(settings.getTheme());
        applyThemeRecursively(mainPanel, palette);
        mainPanel.setBackground(palette.frameBg);
        repaint();
    }

    private void applyThemeRecursively(Component component, ThemePalette palette) {
        if (component instanceof JPanel) {
            component.setBackground(palette.panelBg);
        } else if (component instanceof JLabel) {
            component.setForeground(palette.textFg);
        } else if (component instanceof JButton) {
            component.setBackground(palette.buttonBg);
            component.setForeground(palette.buttonFg);
        } else if (component instanceof JRadioButton) {
            component.setBackground(palette.accentBg);
            component.setForeground(palette.textFg);
        } else if (component instanceof JTextArea) {
            component.setBackground(palette.accentBg);
            component.setForeground(palette.textFg);
        } else if (component instanceof JTextField) {
            component.setBackground(palette.accentBg);
            component.setForeground(palette.textFg);
        } else if (component instanceof JList) {
            component.setBackground(palette.accentBg);
            component.setForeground(palette.textFg);
        } else if (component instanceof JComboBox) {
            component.setBackground(palette.accentBg);
            component.setForeground(palette.textFg);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyThemeRecursively(child, palette);
            }
        }
    }
    
    public void showGameScreen(MiniAdventure adventure, Backend.User player1, Backend.User player2, boolean competitive) {
        MiniAdventure targetAdventure = unwrapIfSecureProxy(adventure);
        for (Map.Entry<Class<?>, AdventureScreenLauncher> entry : adventureLaunchers.entrySet()) {
            if (entry.getKey().isInstance(targetAdventure)) {
                entry.getValue().launch(targetAdventure, player1, player2, competitive);
                return;
            }
        }
        JOptionPane.showMessageDialog(this,
            "No launcher registered for: " + targetAdventure.getName() + "\n" +
            "Register it in GMAEGUI.registerAdventureLaunchers().",
            "Missing Launcher",
            JOptionPane.WARNING_MESSAGE);
    }

    private MiniAdventure unwrapIfSecureProxy(MiniAdventure adventure) {
        if (adventure instanceof SecureMiniAdventureProxy) {
            return ((SecureMiniAdventureProxy) adventure).getDelegate();
        }
        return adventure;
    }
    
    private Image createGameIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillRect(14, 2, 4, 20);
        g2d.fillRect(12, 20, 8, 4);
        g2d.fillRect(14, 24, 4, 6);
        
        g2d.dispose();
        return icon;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GMAEGUI game = new GMAEGUI();
            game.setVisible(true);
        });
    }
}
