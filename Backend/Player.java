package backend;

import java.awt.Point;

public class Player extends Mob{
	private final char symbol;
	public Player(Point p, char symbol) {
		super(p);
		this.symbol = symbol;
	}

	public char getSymbol() {
		return symbol;
	}
}
