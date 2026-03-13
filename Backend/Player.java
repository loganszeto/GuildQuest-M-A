package Backend;

import java.awt.Point;

public class Player extends Mob{
	private PlayerCharacter c;
	public Player(Point p, PlayerCharacter c) {
		super(p);
		this.c = c;
	}
}
