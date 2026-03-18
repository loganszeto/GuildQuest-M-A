package Backend;

import java.awt.Point;

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
