package gmae;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import Backend.Ground;
import Backend.Mob;
import Backend.Player;
import Backend.RealmSpace;
import Backend.Tile;

/**
 * Sample mini-adventure: Relic Hunt
 * Players compete to collect relics in the realm.
 * Uses Ground, Mob, and Player classes from backend.
 */
public class RelicHuntAdventure implements MiniAdventure {
    private RealmSpace realm;
    private PlayerProfile player1Profile;
    private PlayerProfile player2Profile;
    private boolean competitive;
    private boolean gameActive;
    
    // Game entities
    private Backend.Player player1Entity;
    private Backend.Player player2Entity;
    private Map<Point, Boolean> relics;
    private Map<Point, Mob> enemies;
    private int currentPlayer;
    
    // Game state tracking
    private Character char1;
    private Character char2;
    
    public RelicHuntAdventure() {
        this.realm = new RealmSpace("Mystic Realms");
        this.gameActive = false;
        initializeGame();
    }
    
    private void initializeGame() {
        relics = new HashMap<>();
        enemies = new HashMap<>();
        
        // Create Player Entities
        char1 = 'W'; // Warrior symbol
        char2 = 'M'; // Mage symbol
        player1Entity = new Backend.Player(new Point(0, 0), char1);
        player2Entity = new Backend.Player(new Point(19, 14), char2);
        
        // Add ground tiles (terrain)
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                if (Math.random() < 0.7) { // 70% ground
                    realm.addTile(new Ground(new Point(x, y)));
                }
            }
        }
        
        // Place relics randomly
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * 20);
            int y = (int) (Math.random() * 15);
            relics.put(new Point(x, y), true);
        }
        
        // Add some enemies
        for (int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * 18) + 1; // Avoid edges
            int y = (int) (Math.random() * 13) + 1;
            Point enemyPos = new Point(x, y);
            
            // Create anonymous mob
            Mob enemy = new Mob(enemyPos) {
                @Override
                public String toString() {
                    return "Goblin";
                }
            };
            enemies.put(enemyPos, enemy);
        }
        
        // Add entities to realm (order matters - higher index = drawn on top)
        realm.addTile(player1Entity);
        realm.addTile(player2Entity);
        for (Mob enemy : enemies.values()) {
            realm.addTile(enemy);
        }
    }
    
    public void initialize(PlayerProfile player1, PlayerProfile player2, Map<String, Object> settings) {
        this.player1Profile = player1;
        this.player2Profile = player2;
        this.competitive = "competitive".equals(settings.get("mode"));
        this.currentPlayer = 1;
        initializeGame();
    }
    
    @Override
    public void start() {
        gameActive = true;
    }
    
    @Override
    public boolean acceptPlayerInput(int playerNum, String input) {
        if (!gameActive || playerNum != currentPlayer) {
            return false;
        }
        
        Player currentEntity = playerNum == 1 ? player1Entity : player2Entity;
        Point currentPos = new Point(currentEntity.getX(), currentEntity.getY());
        Point newPos = new Point(currentPos);
        
        // Process movement input (WASD or arrow keys)
        switch (input.toLowerCase()) {
            case "w":
            case "up":
                newPos.y = Math.max(0, newPos.y - 1);
                break;
            case "s":
            case "down":
                newPos.y = Math.min(14, newPos.y + 1);
                break;
            case "a":
            case "left":
                newPos.x = Math.max(0, newPos.x - 1);
                break;
            case "d":
            case "right":
                newPos.x = Math.min(19, newPos.x + 1);
                break;
            default:
                return false;
        }
        
        // Check for collisions with enemies
        Tile targetTile = realm.getTopAt(newPos);
        if (targetTile instanceof Mob && targetTile != currentEntity) {
            // Collision with enemy - can't move there
            return false;
        }
        
        // Update position
        currentEntity.move(newPos.x - currentPos.x, newPos.y - currentPos.y);
        
        // Check for relic collection
        if (relics.containsKey(newPos) && relics.get(newPos)) {
            relics.put(newPos, false);
            // Update player profile score
            if (playerNum == 1) {
                player1Profile.addAchievement("Relic Collected!");
            } else {
                player2Profile.addAchievement("Relic Collected!");
            }
        }
        
        return true;
    }
    
    @Override
    public void advanceTurn() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }
    
    @Override
    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("gameActive", gameActive);
        state.put("currentPlayer", currentPlayer);
        state.put("isCompetitive", competitive);
        state.put("player1", player1Profile);
        state.put("player2", player2Profile);
        state.put("player1Pos", new Point(player1Entity.getX(), player1Entity.getY()));
        state.put("player2Pos", new Point(player2Entity.getX(), player2Entity.getY()));
        state.put("relics", new HashMap<>(relics));
        state.put("enemies", new HashMap<>(enemies));
        
        // Count collected relics
        int collectedRelics = 0;
        for (Boolean collected : relics.values()) {
            if (!collected) collectedRelics++;
        }
        state.put("relicsCollected", collectedRelics);
        state.put("totalRelics", relics.size());
        
        return state;
    }
    
    @Override
    public boolean isComplete() {
        // Game ends when all relics are collected
        int collectedRelics = 0;
        for (Boolean collected : relics.values()) {
            if (!collected) collectedRelics++;
        }
        return collectedRelics == relics.size();
    }
    
    @Override
    public int getWinner() {
        if (!competitive) return 0; // Co-op - no winner
        
        // Count relics collected by each player
        int player1Relics = 0;
        int player2Relics = 0;
        
        // Check which player collected more relics
        // This is a simplified version - in reality you'd track individual player collections
        
        // For now, base winner on player profile achievements
        int player1Achievements = player1Profile.getAchievements().size();
        int player2Achievements = player2Profile.getAchievements().size();
        
        if (player1Achievements > player2Achievements) return 1;
        if (player2Achievements > player1Achievements) return 2;
        return 0; // Tie
    }
    
    @Override
    public void reset() {
        initializeGame();
    }
    
    @Override
    public String getName() {
        return "Relic Hunt";
    }
    
    @Override
    public String toString() {
        return getName(); // Return the adventure name for JList display
    }
    
    @Override
    public String getDescription() {
        return "Compete to collect ancient relics scattered throughout the realm. Use WASD or arrow keys to move.";
    }
    
    @Override
    public RealmSpace getRealm() {
        return realm;
    }
    
    @Override
    public boolean supportsCoOp() {
        return true;
    }
    
    @Override
    public boolean supportsCompetitive() {
        return true;
    }
}
