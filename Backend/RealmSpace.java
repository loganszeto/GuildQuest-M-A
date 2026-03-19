package Backend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Backend.Tiles.*;

public class RealmSpace{
	private List<Tile> tiles;
	private String name;
	private Player player1;
	private Player player2;
	private List<Mob> mobs;
	private int minx;
	private int maxx;
	private int miny;
	private int maxy;
	
	public RealmSpace() {
		tiles = new ArrayList<Tile>();
		player1 = null;
		player2 = null;
		mobs = new ArrayList<Mob>();
		this.name = "Unknown Realm";
		minx = 0;
		maxx = 0;
		miny = 0;
		maxy = 0;
	}
	
	public RealmSpace(String name) {
		this();
		this.name = name;
	}
	
	private void updateBounds(Point p) {
		minx = Math.min(p.x, minx);
        maxx = Math.max(p.x, maxx);
        miny = Math.min(p.y, miny);
        maxy = Math.max(p.y, maxy);
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
		if(t instanceof TileGroup) {
			updateBounds(((TileGroup) t).getSecondPoint());
		}
		updateBounds(new Point(t.getX(),t.getY()));
	}

	public void clearTiles() {
		tiles.clear();
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
	
	public int getHeight() {
		return maxy - miny;
	}
	
	public int getWidth() {
		return maxx - minx;
	}
	
	public boolean removeTile(Tile t) {
		if(t instanceof Mob) {
			mobs.remove(t);
		}
		return tiles.remove(t);
	}

}
