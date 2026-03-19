package Backend.Tiles;

import java.awt.Point;

import Backend.NPCCharacter;

public class Enemy extends NPC{

	private int damage;
	
	public Enemy(Point p, NPCCharacter c) {
		super(p, c);
		damage = 1;
	}
	
	public int getDamage() {
		return damage;
	}

}
