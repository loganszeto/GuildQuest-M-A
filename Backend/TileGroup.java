package backend;

public class TileGroup<T extends Tile> extends Tile{
	private int x2;
	private int y2;
	
	public TileGroup(int x1, int x2, int y1, int y2) {
		super(x1,y1);
		this.x2 = x2;
		this.y2 = y2;
	}
	@Override
	public void interact(Tile t) {
		// TODO Auto-generated method stub
		
	}
	
	public T getRepresentative() {
		return new T();
	}
	
	public boolean inBounds(int x, int y) {
		return true; //do later
	}
	
	public boolean onEdge(int x, int y) {
		return true;
	}
}
