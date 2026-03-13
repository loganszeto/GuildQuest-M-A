package Backend;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class RandomDistTileGroup<T extends Tile> extends TileGroup<T>{
	private double prob;
	private Map<Point, T> gencache;
	public RandomDistTileGroup(Point p1, Point p2, T repr, double prob) {
		super(p1, p2, repr);
		this.prob = prob;
		gencache = new HashMap<>();
	}
	
	public T getRepresentative(Point p) {
		T rep = super.getRepresentative(p);
		if(inBounds(p)) {
			if(gencache.containsKey(p)) {
				return rep;
			}
			return gencache.put(p, Math.random()<=prob?rep:null);
		}
		return null;
	}
	

}
