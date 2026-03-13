package Backend;

import java.awt.Point;

public abstract class Tile {
	protected Point p1;
	
	public Tile(Point p) {
		p1 = p;
	}
	public boolean isOccupying() {
		return false;
	}
	public int getX() {
		return (int) p1.getX();
	}
	public void setX(int x) {
		p1.setLocation(x, p1.getY());
	}
	public int getY() {
		return (int) p1.getY();
	}
	public void setY(int y) {
		p1.setLocation(p1.getX(), y);
	}
	public boolean inBounds(Point p) {
        return p.equals(p1);
	}
}
