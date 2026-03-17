package backend;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Backend game-state + rules for the "Relic Hunt" mini-adventure.
 * UI/adventure adapters (e.g. in package gmae) should delegate to this class.
 */
public class RelicHuntGame {
    public static final int MAP_WIDTH = 20;
    public static final int MAP_HEIGHT = 15;
    public static final int RELIC_COUNT = 10;
    public static final int ENEMY_COUNT = 5;

    private final RealmSpace realm;
    private final Map<Point, Boolean> relics;
    private final Map<Point, Mob> enemies;

    private Player player1Entity;
    private Player player2Entity;
    private User player1Profile;
    private User player2Profile;

    private boolean competitive;
    private boolean gameActive;
    private int currentPlayer;

    public RelicHuntGame() {
        this.realm = new RealmSpace("Mystic Realms");
        this.relics = new HashMap<>();
        this.enemies = new HashMap<>();
        reset();
    }

    public RealmSpace getRealm() {
        return realm;
    }

    public void initialize(User player1, User player2, boolean competitive) {
        this.player1Profile = player1;
        this.player2Profile = player2;
        this.competitive = competitive;
        reset();
    }

    public void start() {
        gameActive = true;
    }

    public boolean acceptPlayerInput(int playerNum, String input) {
        if (!gameActive || playerNum != currentPlayer) return false;
        if (input == null) return false;

        Player currentEntity = playerNum == 1 ? player1Entity : player2Entity;
        Point currentPos = new Point(currentEntity.getX(), currentEntity.getY());
        Point newPos = new Point(currentPos);

        switch (input.toLowerCase()) {
            case "w":
            case "up":
                newPos.y = Math.max(0, newPos.y - 1);
                break;
            case "s":
            case "down":
                newPos.y = Math.min(MAP_HEIGHT - 1, newPos.y + 1);
                break;
            case "a":
            case "left":
                newPos.x = Math.max(0, newPos.x - 1);
                break;
            case "d":
            case "right":
                newPos.x = Math.min(MAP_WIDTH - 1, newPos.x + 1);
                break;
            default:
                return false;
        }

        Tile targetTile = realm.getTopAt(newPos);
        if (targetTile instanceof Mob && targetTile != currentEntity) {
            return false;
        }

        currentEntity.move(newPos.x - currentPos.x, newPos.y - currentPos.y);

        if (Boolean.TRUE.equals(relics.get(newPos))) {
            relics.put(newPos, false);
            if (playerNum == 1 && player1Profile != null) player1Profile.addAchievement("Relic Collected!");
            if (playerNum == 2 && player2Profile != null) player2Profile.addAchievement("Relic Collected!");
        }

        return true;
    }

    public void advanceTurn() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public boolean isComplete() {
        int collectedRelics = 0;
        for (Boolean collected : relics.values()) {
            if (!collected) collectedRelics++;
        }
        return !relics.isEmpty() && collectedRelics == relics.size();
    }

    public int getWinner() {
        if (!competitive) return 0;
        if (player1Profile == null || player2Profile == null) return 0;

        int p1 = player1Profile.getAchievements().size();
        int p2 = player2Profile.getAchievements().size();
        if (p1 > p2) return 1;
        if (p2 > p1) return 2;
        return 0;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("gameActive", gameActive);
        state.put("currentPlayer", currentPlayer);
        state.put("isCompetitive", competitive);
        state.put("player1", player1Profile);
        state.put("player2", player2Profile);
        state.put("player1Pos", player1Entity == null ? null : new Point(player1Entity.getX(), player1Entity.getY()));
        state.put("player2Pos", player2Entity == null ? null : new Point(player2Entity.getX(), player2Entity.getY()));
        state.put("relics", new HashMap<>(relics));
        state.put("enemies", new HashMap<>(enemies));

        int collectedRelics = 0;
        for (Boolean collected : relics.values()) {
            if (!collected) collectedRelics++;
        }
        state.put("relicsCollected", collectedRelics);
        state.put("totalRelics", relics.size());
        return state;
    }

    public void reset() {
        gameActive = false;
        currentPlayer = 1;

        realm.clearTiles();
        relics.clear();
        enemies.clear();

        player1Entity = new Player(new Point(0, 0), 'W');
        player2Entity = new Player(new Point(MAP_WIDTH - 1, MAP_HEIGHT - 1), 'M');

        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (Math.random() < 0.7) {
                    realm.addTile(new Ground(new Point(x, y)));
                }
            }
        }

        for (int i = 0; i < RELIC_COUNT; i++) {
            int x = (int) (Math.random() * MAP_WIDTH);
            int y = (int) (Math.random() * MAP_HEIGHT);
            relics.put(new Point(x, y), true);
        }

        for (int i = 0; i < ENEMY_COUNT; i++) {
            int x = (int) (Math.random() * (MAP_WIDTH - 2)) + 1;
            int y = (int) (Math.random() * (MAP_HEIGHT - 2)) + 1;
            Point enemyPos = new Point(x, y);
            Mob enemy = new Mob(enemyPos) {
                @Override
                public String toString() {
                    return "Goblin";
                }
            };
            enemies.put(enemyPos, enemy);
        }

        realm.addTile(player1Entity);
        realm.addTile(player2Entity);
        for (Mob enemy : enemies.values()) {
            realm.addTile(enemy);
        }
    }
}

