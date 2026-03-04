package backend;

public abstract class Tile {
	private int x;
	private int y;
	private static boolean occupying = false;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isOccupying() {
		return occupying;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public abstract void interact(Tile t);
}
