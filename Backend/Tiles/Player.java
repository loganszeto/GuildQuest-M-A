package Backend.Tiles;

import java.awt.Point;

import Backend.PlayerCharacter;

public class Player extends Mob{
	private PlayerCharacter pc;
	public Player(Point p, PlayerCharacter pc) {
		super(p);
		this.pc = pc;
	}

	public PlayerCharacter getCharacter() {
		return pc;
	}
	
	public void setPlayerCharacter(PlayerCharacter pc) {
		this.pc = pc;
	}
}
