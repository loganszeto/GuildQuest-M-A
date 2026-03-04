package backend;

public abstract class Mob extends Tile{
	private static boolean occupying = true;
	public Mob(int x, int y) {
		super(x, y);
	}

	private void move(int x, int y) {
		setX(getX()+x);
		setY(getY()+y);
	}
}
