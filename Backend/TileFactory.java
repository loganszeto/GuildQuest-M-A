package backend;

import java.awt.Point;

public class TileFactory{
	public static Ground makeTile(Point p) {
		return new Ground(p);
	}
}
