package Backend.Tiles;

import java.awt.Point;

import Backend.NPCCharacter;

public class NPC extends Mob{
	private NPCCharacter c;
	public NPC(Point p, NPCCharacter c) {
		super(p);
		this.c = c;
	}
	public NPCCharacter getCharacter() {
		return c;
	}
}
