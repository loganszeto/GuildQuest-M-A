package Backend.Maps;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Backend.NPCCharacter;
import Backend.RealmSpace;
import Backend.Tiles.*;

public class RelicHuntArena {
    //helper to save on typing out new Point every time
    private static Point p(int a, int b) {
        return new Point(a,b);
    }
    
    public static RealmSpace getSpace() {
        RealmSpace ret = new RealmSpace("RelicHuntArena");
        Point defaultp = p(0,0);
        
        // Create a balanced arena with multiple relics for competitive/co-op play
        // Ground covering the entire arena
        ret.addTile(new TileGroup<>(p(-15,-15), p(15,15), new Ground(defaultp)));
        
        // Place relics strategically around the arena - 8 total for fair competition
        List<Point> relicPositions = new ArrayList<>();
        relicPositions.add(p(-12, -12)); // Corner 1
        relicPositions.add(p(12, -12));  // Corner 2
        relicPositions.add(p(-12, 12));  // Corner 3
        relicPositions.add(p(12, 12));   // Corner 4
        relicPositions.add(p(0, -10));   // Top center
        relicPositions.add(p(0, 10));    // Bottom center
        relicPositions.add(p(-10, 0));   // Left center
        relicPositions.add(p(10, 0));    // Right center
        
        for (Point relicPos : relicPositions) {
            ret.addTile(new RelicTile(relicPos));
        }
        
        // Add some strategic obstacles/hazards
        // Trap corridors to make navigation challenging
        ret.addTile(new TileGroup<>(p(-2, -8), p(2, -6), new TrapTile(defaultp)));
        ret.addTile(new TileGroup<>(p(-2, 6), p(2, 8), new TrapTile(defaultp)));
        ret.addTile(new TileGroup<>(p(-8, -2), p(-6, 2), new TrapTile(defaultp)));
        ret.addTile(new TileGroup<>(p(6, -2), p(8, 2), new TrapTile(defaultp)));
        
        // Add NPCs for hints or as obstacles
        ret.addTile(new NPC(p(-5, -5), new NPCCharacter("Guide", 10, "Find the relics quickly!")));
        ret.addTile(new NPC(p(5, 5), new NPCCharacter("Guardian", 15, "The center is dangerous...")));
        ret.addTile(new NPC(p(0, 0), new NPCCharacter("Mystic", 20, "Time is of the essence!")));
        
        // Place players at opposite starting positions for fair competition
        ret.addTile(new Player(p(-14, -14), null)); // Player 1 - top-left corner
        ret.addTile(new Player(p(14, 14), null));   // Player 2 - bottom-right corner
        
        return ret;
    }
}
