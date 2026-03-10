package backend;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RealmSpace {
	private List<Tile> tiles; //order matters: higher index means will be drawn on top
	private String name;
	
	public RealmSpace() {
		tiles = new ArrayList<Tile>();
		this.name = "Unknown Realm";
	}
	
	public RealmSpace(String name) {
		tiles = new ArrayList<Tile>();
		this.name = name;
	}
	
	public void addTile(Tile t) {
		tiles.add(t);
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
}
