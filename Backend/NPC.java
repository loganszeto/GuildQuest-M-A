package Backend;

import java.awt.Point;

public class NPC extends Mob{
	private NPCCharacter c;
	public NPC(Point p, NPCCharacter c) {
		super(p);
		this.c = c;
	}
}
