package Backend;

import java.awt.Point;

public class TileGroup<T extends Tile> extends Tile{
	private Point p2;
	private T representative;
	
	public TileGroup(Point p1, Point p2, T repr) {
		super(p1);
		this.p2 = p2;
		representative = repr;
	}
	
	public T getRepresentative(Point p) {
		if(inBounds(p)) return representative;
		return null;
	}
	
	public boolean inBounds(Point p) {
        int minX = Math.min(p1.x, p2.x);
        int maxX = Math.max(p1.x, p2.x);
        int minY = Math.min(p1.y, p2.y);
        int maxY = Math.max(p1.y, p2.y);
        return (p.x >= minX && p.x <= maxX) && (p.y >= minY && p.y <= maxY);
	}
	
	public boolean onEdge(Point p) {
		return inBounds(p) && (p.x == p1.x || p.x == p2.x || p.y == p1.y || p.y == p2.y);
	}
	
	// Helper methods for map visualization
	public Point getSecondPoint() {
		return p2;
	}
	
	public int getWidth() {
		return Math.abs(p2.x - p1.x) + 1;
	}
	
	public int getHeight() {
		return Math.abs(p2.y - p1.y) + 1;
	}
	
	public int getMinX() {
		return Math.min(p1.x, p2.x);
	}
	
	public int getMaxX() {
		return Math.max(p1.x, p2.x);
	}
	
	public int getMinY() {
		return Math.min(p1.y, p2.y);
	}
	
	public int getMaxY() {
		return Math.max(p1.y, p2.y);
	}
}
