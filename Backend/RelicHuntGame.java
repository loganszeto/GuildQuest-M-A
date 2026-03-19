package Backend;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Backend.Tiles.Enemy;
import Backend.Tiles.Ground;
import Backend.Tiles.Mob;
import Backend.Tiles.Player;
import Backend.Tiles.RelicTile;
import Backend.Tiles.Tile;

/**
 * Backend game-state + rules for the "Relic Hunt" mini-adventure.
 * UI/adventure adapters (e.g. in package gmae) should delegate to this class.
 */
public class RelicHuntGame extends GameRunner{
    public static final int RELIC_COUNT = 10;
    public static final int ENEMY_COUNT = 5;
    private final Map<Point, Boolean> relics;
    private boolean competitive;
    
    public RelicHuntGame() {
        super("RelicHunt");
        this.relics = new HashMap<>();
    }

    public void reset() {
        super.reset();
        
        relics.clear();
        RealmSpace rs = getRealmSpace();
        for (int i = 0; i < RELIC_COUNT;) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            Point p = new Point(x, y);
            List<Tile> tiles= rs.getAllAt(p);
            if(tiles.get(0) instanceof Ground && !(tiles.get(tiles.size()-1) instanceof RelicTile)) {
            		relics.put(p, true);
            		rs.addTile(new RelicTile(p));
            		i++;
            }
        }
        for (int i = 0; i < ENEMY_COUNT;) {
            int x = (int) (Math.random() * (getWidth() - 2)) + 1;
            int y = (int) (Math.random() * (getHeight() - 2)) + 1;
            Point enemyPos = new Point(x, y);
            Enemy enemy = new Enemy(enemyPos,new NPCCharacter("Goblin",3,"Goblin"));
            	List<Tile> tiles= rs.getAllAt(enemyPos);
            	if(tiles.get(0) instanceof Ground && !tiles.get(tiles.size()-1).isOccupying()) {
            		rs.addTile(enemy);
            		i++;
            	}
        }
        
    }

    public void initialize(User player1, User player2, boolean competitive) {
        super.initialize(player1, player2);
        this.competitive = competitive;
    }

    public void start() {
        setActive(true);
    }

    public boolean acceptPlayerInput(int playerNum, String input) {
        if (!isActive() || playerNum != getTurn()) return false;
        if (input == null) return false;
        RealmSpace rs = getRealmSpace();
        Player currentEntity = playerNum == 1 ? rs.getPlayerOne() : rs.getPlayerTwo();
        Point currentPos = new Point(currentEntity.getX(), currentEntity.getY());
        Point newPos = new Point(currentPos);

        switch (input.toLowerCase()) {
            case "w":
            case "up":
                newPos.y = Math.max(0, newPos.y - 1);
                break;
            case "s":
            case "down":
                newPos.y = Math.min(getHeight() - 1, newPos.y + 1);
                break;
            case "a":
            case "left":
                newPos.x = Math.max(0, newPos.x - 1);
                break;
            case "d":
            case "right":
                newPos.x = Math.min(getWidth() - 1, newPos.x + 1);
                break;
            default:
                return false;
        }

        Tile targetTile = rs.getTopAt(newPos);
        if (targetTile instanceof Mob && targetTile != currentEntity) {
            return false;
        }

        currentEntity.move(newPos.x - currentPos.x, newPos.y - currentPos.y);

        if (Boolean.TRUE.equals(relics.get(newPos))) {
            relics.put(newPos, false);
            if (playerNum == 1 && getUser1() != null) getUser1().addAchievement("Relic Collected!");
            if (playerNum == 2 && getUser2() != null) getUser2().addAchievement("Relic Collected!");
            List<Tile> tiles= rs.getAllAt(newPos);
            for(Tile t: tiles) {
            		if(t instanceof RelicTile) {
            			rs.removeTile(t);
            		}
            }
        }

        return true;
    }

    public void advanceTurn() {
        super.advanceTurn();
        //handle enemy turn if it happens
        RealmSpace rs = getRealmSpace();
        if(getTurn()==0) {
        		List<Mob> mobs = rs.getMobList();
        		for(Mob m : mobs) {
        			int[] x = {1,0-1,0};
        			int[] y = {0,1,0,-1};
        			int dir = (int) (Math.random() * 4);
        			m.move(x[dir],y[dir]);
        		}
        		super.advanceTurn();
        }
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
        if (getUser1() == null || getUser2() == null) return 0;

        int p1 = getUser1().getAchievements().size();
        int p2 = getUser2().getAchievements().size();
        if (p1 > p2) return 1;
        if (p2 > p1) return 2;
        return 0;
    }

    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("gameActive", isActive());
        state.put("currentPlayer", getTurn());
        state.put("isCompetitive", competitive);
        state.put("player1", getUser1());
        state.put("player2", getUser2());
        RealmSpace rs = getRealmSpace();
        state.put("player1Pos", rs.getPlayerOne());
        state.put("player2Pos", rs.getPlayerTwo());
        state.put("relics", new HashMap<>(relics));
        state.put("enemies", rs.getMobList());

        int collectedRelics = 0;
        for (Boolean collected : relics.values()) {
            if (!collected) collectedRelics++;
        }
        state.put("relicsCollected", collectedRelics);
        state.put("totalRelics", relics.size());
        return state;
    }

    
}

