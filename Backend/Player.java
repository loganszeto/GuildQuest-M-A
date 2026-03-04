package backend;

import java.awt.Point;

public class Player extends Mob{
	private Character c;
	public Player(Point p, Character c) {
		super(p);
		this.c = c;
	}
}
