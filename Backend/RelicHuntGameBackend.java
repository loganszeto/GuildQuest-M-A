package Backend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Backend.Tiles.Enemy;
import Backend.Tiles.Ground;
import Backend.Tiles.Player;
import Backend.Tiles.RelicTile;
import Backend.Tiles.Tile;
import Frontend.MiniAdventure;

/**
 * Standalone backend for Relic Hunt game that doesn't depend on GameRunner
 * Contains all game logic and can be used with any UI implementation
 * Also implements MiniAdventure interface for direct integration
 */
public class RelicHuntGameBackend implements MiniAdventure {
    public static final int RELIC_COUNT = 5;
    public static final int ENEMY_COUNT = 3;
    public static final int MAX_HEALTH = 3;
    public static final int BOARD_SIZE = 10;

    private RealmSpace realm;
    private final Map<Point, Boolean> relics;
    private List<Point> enemies = new ArrayList<>();
    private boolean competitive;
    private boolean gameActive;
    private int currentPlayer;
    
    // Player positions
    private int player1X = 0, player1Y = 0;
    private int player2X = 9, player2Y = 9;
    
    // Health system
    private int player1Health;
    private int player2Health;
    
    // Game statistics
    private int relicsCollected = 0;
    private int player1Relics = 0;
    private int player2Relics = 0;
    
    // User profiles
    private User player1Profile;
    private User player2Profile;
    
    private final Random random = new Random();

    public RelicHuntGameBackend() {
        this.realm = new RealmSpace("RelicHunt");
        this.relics = new HashMap<>();
        this.enemies = new ArrayList<>();
        initializeRealm();
        reset();
    }

    private void initializeRealm() {
        // Add ground tiles to create the board
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                realm.addTile(new Ground(new Point(x, y)));
            }
        }
    }

    public void initialize(User player1, User player2, boolean competitive) {
        this.player1Profile = player1;
        this.player2Profile = player2;
        this.competitive = competitive;
        reset();
    }
    
    // MiniAdventure interface implementation
    @Override
    public void initialize(User player1, User player2, Map<String, Object> settings) {
        this.player1Profile = player1;
        this.player2Profile = player2;
        this.competitive = settings != null && "competitive".equals(settings.get("mode"));
        reset();
    }

    public void start() {
        gameActive = true;
    }

    public void reset() {
        gameActive = false;
        currentPlayer = 1;
        player1X = 0;
        player1Y = 0;
        player2X = 9;
        player2Y = 9;
        player1Health = MAX_HEALTH;
        player2Health = MAX_HEALTH;
        relicsCollected = 0;
        player1Relics = 0;
        player2Relics = 0;
        
        // Clear existing tiles except ground
        List<Tile> allTiles = new ArrayList<>(realm.getAllTiles());
        for (Tile tile : allTiles) {
            if (!(tile instanceof Ground)) {
                realm.removeTile(tile);
            }
        }
        
        // Clear and randomly place relics
        relics.clear();
        int relicsPlaced = 0;
        while (relicsPlaced < RELIC_COUNT) {
            int x = random.nextInt(BOARD_SIZE);
            int y = random.nextInt(BOARD_SIZE);
            Point p = new Point(x, y);
            
            // Don't place relics on starting positions
            if ((x == 0 && y == 0) || (x == 9 && y == 9)) continue;
            if (!relics.containsKey(p)) {
                relics.put(p, true);
                realm.addTile(new RelicTile(p));
                relicsPlaced++;
            }
        }
        
        // Clear and randomly place enemies
        enemies.clear();
        int enemiesPlaced = 0;
        while (enemiesPlaced < ENEMY_COUNT) {
            int x = random.nextInt(BOARD_SIZE);
            int y = random.nextInt(BOARD_SIZE);
            Point enemyPos = new Point(x, y);
            
            // Don't place enemies on starting positions or relics
            if ((x == 0 && y == 0) || (x == 9 && y == 9)) continue;
            if (relics.containsKey(enemyPos)) continue;
            if (!enemies.contains(enemyPos)) {
                enemies.add(enemyPos);
                realm.addTile(new Enemy(enemyPos, new NPCCharacter("Enemy", 3, "Goblin")));
                enemiesPlaced++;
            }
        }
        
        // Place players
        realm.addTile(new Player(new Point(0, 0), player1Profile != null ? player1Profile.getActiveCharacter() : null));
        realm.addTile(new Player(new Point(9, 9), player2Profile != null ? player2Profile.getActiveCharacter() : null));
    }

    public boolean acceptPlayerInput(int playerNum, String input) {
        if (!gameActive || playerNum != currentPlayer) return false;
        
        int currentX = playerNum == 1 ? player1X : player2X;
        int currentY = playerNum == 1 ? player1Y : player2Y;
        int newX = currentX, newY = currentY;
        
        switch (input.toLowerCase()) {
            case "w":
            case "up":
                newY = Math.max(0, newY - 1);
                break;
            case "s":
            case "down":
                newY = Math.min(BOARD_SIZE - 1, newY + 1);
                break;
            case "a":
            case "left":
                newX = Math.max(0, newX - 1);
                break;
            case "d":
            case "right":
                newX = Math.min(BOARD_SIZE - 1, newX + 1);
                break;
            default:
                return false;
        }
        
        // Check bounds
        if (newX < 0 || newX >= BOARD_SIZE || newY < 0 || newY >= BOARD_SIZE) {
            return false;
        }
        
        // Check collision with other player
        if ((currentPlayer == 1 && newX == player2X && newY == player2Y) ||
            (currentPlayer == 2 && newX == player1X && newY == player1Y)) {
            return false;
        }
        
        // Check collision with enemy
        Point newPos = new Point(newX, newY);
        if (enemies.contains(newPos)) {
            // Take damage and lose turn
            damagePlayer(playerNum, 1);
            return false;
        }
        
        // Process the move
        // Remove old player position
        Point oldPos = new Point(currentX, currentY);
        List<Tile> oldTiles = realm.getAllAt(oldPos);
        for (Tile tile : oldTiles) {
            if (tile instanceof Player) {
                realm.removeTile(tile);
                break;
            }
        }
        
        // Update player position
        if (currentPlayer == 1) {
            player1X = newX;
            player1Y = newY;
        } else {
            player2X = newX;
            player2Y = newY;
        }
        
        // Add player at new position
        PlayerCharacter character = currentPlayer == 1 ? 
            (player1Profile != null ? player1Profile.getActiveCharacter() : null) :
            (player2Profile != null ? player2Profile.getActiveCharacter() : null);
        realm.addTile(new Player(newPos, character));
        
        // Check for relic collection
        if (relics.containsKey(newPos) && relics.get(newPos)) {
            relics.put(newPos, false);
            relicsCollected++;
            if (currentPlayer == 1) {
                player1Relics++;
            } else {
                player2Relics++;
            }
            
            // Remove relic tile from realm
            List<Tile> newTiles = realm.getAllAt(newPos);
            for (Tile tile : newTiles) {
                if (tile instanceof RelicTile) {
                    realm.removeTile(tile);
                    break;
                }
            }
        }
        
        return true;
    }

    public void advanceTurn() {
        if (gameActive) {
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            
            // Move enemies randomly
            List<Point> newEnemyPositions = new ArrayList<>();
            for (Point enemy : enemies) {
                int[] dx = {0, 1, 0, -1};
                int[] dy = {-1, 0, 1, 0};
                int dir = random.nextInt(4);
                int newX = Math.max(0, Math.min(BOARD_SIZE - 1, enemy.x + dx[dir]));
                int newY = Math.max(0, Math.min(BOARD_SIZE - 1, enemy.y + dy[dir]));
                newEnemyPositions.add(new Point(newX, newY));
                
                // Move enemy in realm
                List<Tile> enemyTiles = realm.getAllAt(enemy);
                for (Tile tile : enemyTiles) {
                    if (tile instanceof Enemy) {
                        realm.removeTile(tile);
                        break;
                    }
                }
                realm.addTile(new Enemy(new Point(newX, newY), new NPCCharacter("Enemy", 3, "Goblin")));
            }
            enemies = newEnemyPositions;
        }
    }

    public void damagePlayer(int playerNum, int damage) {
        if (playerNum == 1) {
            player1Health = Math.max(0, player1Health - damage);
        } else if (playerNum == 2) {
            player2Health = Math.max(0, player2Health - damage);
        }
    }

    public boolean isComplete() {
        return relicsCollected >= RELIC_COUNT;
    }

    public int isLost() {
        if (player1Health <= 0) return 1;
        if (player2Health <= 0) return 2;
        return -1; // Game ongoing
    }

    public int getWinner() {
        if (!competitive) return 0;
        return player1Relics > player2Relics ? 1 : (player2Relics > player1Relics ? 2 : 0);
    }

    public int getTurn() {
        return currentPlayer;
    }

    public boolean isActive() {
        return gameActive;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("gameActive", gameActive);
        state.put("currentPlayer", currentPlayer);
        state.put("isCompetitive", competitive);
        state.put("relicsCollected", relicsCollected);
        state.put("totalRelics", RELIC_COUNT);
        state.put("player1Health", player1Health);
        state.put("player2Health", player2Health);
        state.put("maxHealth", MAX_HEALTH);
        state.put("player1Relics", player1Relics);
        state.put("player2Relics", player2Relics);
        state.put("player1X", player1X);
        state.put("player1Y", player1Y);
        state.put("player2X", player2X);
        state.put("player2Y", player2Y);
        return state;
    }

    // Getters for rendering
    public RealmSpace getRealmSpace() {
        return realm;
    }

    public int getPlayer1X() { return player1X; }
    public int getPlayer1Y() { return player1Y; }
    public int getPlayer2X() { return player2X; }
    public int getPlayer2Y() { return player2Y; }
    public Map<Point, Boolean> getRelics() { return relics; }
    public List<Point> getEnemies() { return enemies; }
    public int getBoardSize() { return BOARD_SIZE; }
    public int getPlayerHealth(int playerNum) {
        return playerNum == 1 ? player1Health : player2Health;
    }
    public int getRelicsCollected() { return relicsCollected; }
    public int getTotalRelics() { return RELIC_COUNT; }
    public boolean isCompetitive() { return competitive; }
    
    // MiniAdventure interface implementation (additional methods only)
    @Override
    public String getName() {
        return "Relic Hunt";
    }
    
    @Override
    public String toString() {
        return getName(); // Use getName() for JList display
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
