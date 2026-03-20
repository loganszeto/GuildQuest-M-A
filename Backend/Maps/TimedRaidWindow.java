package Backend.Maps;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Backend.NPCCharacter;
import Backend.RealmSpace;
import Backend.Tiles.*;

public class TimedRaidWindow {
    //helper to save on typing out new Point every time
    private static Point p(int a, int b) {
        return new Point(a,b);
    }
    
    public static RealmSpace getSpace() {
        RealmSpace ret = new RealmSpace("TimedRaidWindow");
        Point defaultp = p(0,0);
        
        // Create a compact, high-intensity raid area with clear objectives
        // Smaller area for time pressure - 12x12 grid
        ret.addTile(new TileGroup<>(p(-6,-6), p(6,6), new Ground(defaultp)));
        
        // Primary objectives - these must be completed before time runs out
        // Central objective (main goal)
        ret.addTile(new RelicTile(p(0, 0))); // Main artifact to capture
        
        // Secondary objectives scattered around
        List<Point> secondaryObjectives = new ArrayList<>();
        secondaryObjectives.add(p(-4, -4)); // NW corner
        secondaryObjectives.add(p(4, -4));  // NE corner
        secondaryObjectives.add(p(-4, 4));  // SW corner
        secondaryObjectives.add(p(4, 4));   // SE corner
        
        for (Point obj : secondaryObjectives) {
            ret.addTile(new RelicTile(obj));
        }
        
        // Time-pressure elements - traps that create urgency
        // Dangerous paths that require quick decision making
        ret.addTile(new TileGroup<>(p(-1, -6), p(1, -4), new TrapTile(defaultp))); // Top barrier
        ret.addTile(new TileGroup<>(p(-1, 4), p(1, 6), new TrapTile(defaultp)));   // Bottom barrier
        ret.addTile(new TileGroup<>(p(-6, -1), p(-4, 1), new TrapTile(defaultp))); // Left barrier
        ret.addTile(new TileGroup<>(p(4, -1), p(6, 1), new TrapTile(defaultp)));   // Right barrier
        
        // Moving hazard zones - scattered traps for added challenge
        ret.addTile(new TrapTile(p(-2, -2)));
        ret.addTile(new TrapTile(p(2, -2)));
        ret.addTile(new TrapTile(p(-2, 2)));
        ret.addTile(new TrapTile(p(2, 2)));
        
        // NPCs that provide time-sensitive information or quests
        ret.addTile(new NPC(p(-5, 0), new NPCCharacter("Urgent Messenger", 10, "Hurry! The window is closing!")));
        ret.addTile(new NPC(p(5, 0), new NPCCharacter("Time Keeper", 15, "You have limited time!")));
        ret.addTile(new NPC(p(0, -5), new NPCCharacter("Scout", 8, "The center holds the main prize!")));
        ret.addTile(new NPC(p(0, 5), new NPCCharacter("Guard", 12, "Watch out for traps!")));
        
        // Players start at the edges - they must race to the center
        ret.addTile(new Player(p(-5, -5), null)); // Player 1 - NW start
        ret.addTile(new Player(p(5, 5), null));   // Player 2 - SE start
        
        return ret;
    }
}
