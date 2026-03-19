import javax.swing.*;
import java.awt.*;
import Backend.*;
import Backend.Tiles.*;
import Backend.Maps.*;
import Frontend.RealmMapPanel;

public class GameModeTest extends JFrame {
    
    public GameModeTest() {
        setTitle("Game Mode Test - Relic Hunt vs Timed Raid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 700);
        
        setLayout(new BorderLayout());
        
        // Create panels for both game modes
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Relic Hunt Arena
        RealmSpace relicHuntSpace = RelicHuntArena.getSpace();
        RealmMapPanel relicHuntPanel = new RealmMapPanel(relicHuntSpace);
        JPanel relicHuntWrapper = createLabeledPanel(relicHuntPanel, "Relic Hunt Arena - Collect 8 Artifacts");
        
        // Timed Raid Window
        RealmSpace timedRaidSpace = TimedRaidWindow.getSpace();
        RealmMapPanel timedRaidPanel = new RealmMapPanel(timedRaidSpace);
        JPanel timedRaidWrapper = createLabeledPanel(timedRaidPanel, "Timed Raid Window - Beat the Clock!");
        
        mainPanel.add(relicHuntWrapper);
        mainPanel.add(timedRaidWrapper);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Add info panel at bottom
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    private JPanel createLabeledPanel(JPanel mapPanel, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.add(mapPanel, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createInfoPanel() {
        JPanel info = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel relicInfo = new JPanel(new BorderLayout());
        relicInfo.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        relicInfo.add(new JLabel("Relic Hunt: Competitive/Co-op"), BorderLayout.NORTH);
        relicInfo.add(new JLabel("• 8 relics placed strategically"), BorderLayout.WEST);
        relicInfo.add(new JLabel("• Players start in opposite corners"), BorderLayout.CENTER);
        relicInfo.add(new JLabel("• First to collect N relics wins!"), BorderLayout.EAST);
        
        JPanel raidInfo = new JPanel(new BorderLayout());
        raidInfo.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        raidInfo.add(new JLabel("Timed Raid: Beat the Clock!"), BorderLayout.NORTH);
        raidInfo.add(new JLabel("• 5 objectives in compact area"), BorderLayout.WEST);
        raidInfo.add(new JLabel("• Trap barriers create urgency"), BorderLayout.CENTER);
        raidInfo.add(new JLabel("• Complete before time runs out!"), BorderLayout.EAST);
        
        info.add(relicInfo);
        info.add(raidInfo);
        
        return info;
    }
    
    public static void main(String[] args) {
        // Test sprite manager initialization
        System.out.println("=== Game Mode Test ===");
        System.out.println("Initializing SpriteManager...");
        SpriteManager spriteManager = SpriteManager.getInstance();
        System.out.println("SpriteManager ready!\n");
        
        // Test realm creation
        System.out.println("Creating Relic Hunt Arena...");
        RealmSpace relicHunt = RelicHuntArena.getSpace();
        System.out.println("✓ Relic Hunt Arena created");
        System.out.println("  - Total tiles: " + relicHunt.getAllTiles().size());
        System.out.println("  - Players: " + (relicHunt.getPlayerOne() != null ? "P1" : "None") + 
                          " & " + (relicHunt.getPlayerTwo() != null ? "P2" : "None"));
        
        System.out.println("\nCreating Timed Raid Window...");
        RealmSpace timedRaid = TimedRaidWindow.getSpace();
        System.out.println("✓ Timed Raid Window created");
        System.out.println("  - Total tiles: " + timedRaid.getAllTiles().size());
        System.out.println("  - Players: " + (timedRaid.getPlayerOne() != null ? "P1" : "None") + 
                          " & " + (timedRaid.getPlayerTwo() != null ? "P2" : "None"));
        
        // Launch GUI comparison
        SwingUtilities.invokeLater(() -> {
            try {
                GameModeTest test = new GameModeTest();
                test.setVisible(true);
                System.out.println("\n=== GUI Launched! ===");
                System.out.println("Compare both game modes side by side!");
                System.out.println("Notice the different design philosophies:");
                System.out.println("- Relic Hunt: Large arena, strategic positioning");
                System.out.println("- Timed Raid: Compact, high-intensity action");
            } catch (Exception e) {
                System.err.println("Error launching GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
