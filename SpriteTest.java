import javax.swing.*;
import java.awt.*;
import Backend.*;
import Backend.Tiles.*;
import Backend.Maps.*;
import Frontend.RealmMapPanel;

public class SpriteTest extends JFrame {
    
    public SpriteTest() {
        setTitle("Sprite Rendering Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        // Create a test realm space
        RealmSpace testSpace = UnknownRealm.getSpace();
        
        // Create the map panel with sprite rendering
        RealmMapPanel mapPanel = new RealmMapPanel(testSpace);
        
        add(mapPanel, BorderLayout.CENTER);
        
        // Add info panel
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.add(new JLabel("Sprite Rendering Test - Each tile type should now show sprites instead of colors"));
        add(infoPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        // Test sprite manager initialization
        System.out.println("Initializing SpriteManager...");
        SpriteManager spriteManager = SpriteManager.getInstance();
        System.out.println("SpriteManager initialized successfully!");
        System.out.println("Sprite size: " + spriteManager.getSpriteSize() + "x" + spriteManager.getSpriteSize());
        
        // Test sprite loading for different tile types
        System.out.println("\nTesting sprite loading:");
        testSpriteForTile(new Ground(new Point(0, 0)), "Ground");
        testSpriteForTile(new Player(new Point(0, 0), null), "Player");
        testSpriteForTile(new RelicTile(new Point(0, 0)), "RelicTile");
        testSpriteForTile(new TrapTile(new Point(0, 0)), "TrapTile");
        testSpriteForTile(new NPC(new Point(0, 0), new NPCCharacter("Test", 10, "Villager")), "NPC");
        testSpriteForTile(new EmptyTile(new Point(0, 0)), "EmptyTile");
        
        // Launch GUI test
        SwingUtilities.invokeLater(() -> {
            try {
                SpriteTest test = new SpriteTest();
                test.setVisible(true);
                System.out.println("\nGUI test launched! You should see sprite-based rendering.");
            } catch (Exception e) {
                System.err.println("Error launching GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private static void testSpriteForTile(Tile tile, String tileName) {
        SpriteManager spriteManager = SpriteManager.getInstance();
        var sprite = spriteManager.getSpriteForTile(tile);
        var scaledSprite = spriteManager.getScaledSprite(tile, 30);
        
        System.out.println(tileName + ": " + 
            (sprite != null ? "✓ Sprite loaded" : "✗ No sprite") + 
            " | Scaled: " + (scaledSprite != null ? "✓" : "✗"));
    }
}
