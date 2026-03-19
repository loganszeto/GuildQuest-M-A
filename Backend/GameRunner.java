package Backend;

import Backend.Tiles.Player;

public abstract class GameRunner {
	private Realm realm;
	private RealmSpace map;
    private User player1Profile;
    private User player2Profile;
    private Player player1Entity;
    private Player player2Entity;
    private boolean gameActive;
    private int currentTurn;
    private int mapheight;
    private int mapwidth;
    
    public GameRunner(String rs) {
    		this.realm = new RealmFactory().load(rs);
    		
    		reset();
    }
    
    public void reset() {
    		this.map = realm.getRealmSpace();
		this.mapheight = map.getHeight();
		this.mapwidth = map.getWidth();
		currentTurn = 1;
		gameActive = false;
    }
    
    public void initialize(User player1, User player2) {
    		this.player1Profile = player1;
        this.player2Profile = player2;
        player1Entity = map.getPlayerOne();
        player1Entity.setPlayerCharacter((PlayerCharacter) player1Profile.loadCharacter(player1Profile.getCharacters().get(0)));
        player2Entity = map.getPlayerTwo();
        player2Entity.setPlayerCharacter((PlayerCharacter) player2Profile.loadCharacter(player2Profile.getCharacters().get(0)));

    }
    
    public void advanceTurn() {
    		currentTurn = currentTurn++%3;
    }
    
    public int getHeight() {
    		return mapheight;
    }
    public int getWidth() {
    		return mapwidth;
    }
    public Realm getRealm() {
    		return realm;
    }
    public RealmSpace getRealmSpace() {
    		return map;
    }
    public boolean isActive() {
    		return gameActive;
    }
    public void setActive(boolean b) {
    		gameActive = b;
    }
    public int getTurn() {
    		return currentTurn;
    }
    public User getUser1() {
    		return player1Profile;
    }
    public User getUser2() {
		return player2Profile;
    }
    public abstract boolean isComplete();
    public abstract int getWinner();
    
    
}
