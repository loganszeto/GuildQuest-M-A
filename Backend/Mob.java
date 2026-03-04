package backend;

import java.awt.Point;

public abstract class Mob extends Tile{
	public Mob(Point p) {
		super(p);
	}
	public boolean isOccupying() {
		return true;
	}
	public void move(int x, int y) {
		setX(getX()+x);
		setY(getY()+y);
	}
}
