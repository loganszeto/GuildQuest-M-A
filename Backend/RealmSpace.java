package Backend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RealmSpace{
	private List<Tile> tiles; //order matters: higher index means will be drawn on top
	private String name;
	private Player player1;
	private Player player2;
	private List<Mob> mobs;
	
	public RealmSpace() {
		tiles = new ArrayList<Tile>();
		player1 = null;
		player2 = null;
		mobs = new ArrayList<Mob>();
		this.name = "Unknown Realm";
	}
	
	public RealmSpace(String name) {
		this();
		this.name = name;
	}
	
	public void addTile(Tile t) {
		tiles.add(t);
		
		//add mobs to list for resolving and detect player 1 and 2
		if(t instanceof Mob) {
			mobs.add((Mob) t);
			if(t instanceof Player) {
				if(player1!=null) {
					player2 = (Player) t;
				}
				else {
					player1 = (Player) t;
				}
			}
		}
	}
	
	public Tile getTopAt(Point p) {
		ListIterator<Tile> listIterator = tiles.listIterator(tiles.size());
        while (listIterator.hasPrevious()) {
        		Tile t = listIterator.previous();
        		if(t.inBounds(p)) return t;
        }
        return null;
	}
	
	public List<Tile> getTopN(Point p, int n) {
		List<Tile> ret = new ArrayList<>();
		int got = 0;
		ListIterator<Tile> listIterator = tiles.listIterator(tiles.size());
        while (listIterator.hasPrevious() && got<n) {
        		Tile t = listIterator.previous();
        		if(t.inBounds(p)) {ret.add(t); got++;}
        }
        return ret;
		
	}
	public List<Tile> getAllAt(Point p) {
		List<Tile> ret = new ArrayList<>();
		ListIterator<Tile> listIterator = tiles.listIterator(tiles.size());
        while (listIterator.hasPrevious()) {
        		Tile t = listIterator.previous();
        		if(t.inBounds(p)) ret.add(t);
        }
        return ret;
	}
	
	public List<Tile> getAllTiles() {
		return new ArrayList<>(tiles);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName(); // Return realm name for JComboBox display
	}
	
	public Player getPlayerOne() {
		return player1;
	}
	
	public Player getPlayerTwo() {
		return player2;
	}
	
	public List<Mob> getMobList(){
		return mobs;
	}

}
