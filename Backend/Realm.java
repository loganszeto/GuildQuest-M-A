package Backend;

import java.awt.Point;
import java.util.List;

public class Realm implements Savable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final RealmSpace realmSpace;
    private String name;
    private int width;
    private int height;

    public Realm(String name) {
        this.name = name == null || name.isBlank() ? "Unknown Realm" : name;
        RealmSpace loaded = new RealmSpaceFactory().load(this.name);
        this.realmSpace = loaded != null ? loaded : new RealmSpace(this.name);
        this.height = this.realmSpace.getHeight();
        this.width = this.realmSpace.getWidth();
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public RealmSpace getRealmSpace() {
        return realmSpace;
    }

    public boolean inBounds(Point p) {
        return p != null && p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    public void addTile(Tile tile) {
        if (tile != null) {
            realmSpace.addTile(tile);
        }
    }

    public boolean isOccupied(Point p) {
        if (!inBounds(p)) return true;
        List<Tile> tiles = realmSpace.getAllAt(p);
        for (Tile t : tiles) {
            if (t != null && t.isOccupying()) return true;
        }
        return false;
    }

    /**
     * Movement/placement is handled by Mob + tiles/realmspace (not Character/Position).
     */
    public boolean tryMove(Mob mob, int dx, int dy) {
        if (mob == null) return false;
        Point from = new Point(mob.getX(), mob.getY());
        Point to = new Point(from.x + dx, from.y + dy);
        if (!inBounds(to)) return false;

        Tile top = realmSpace.getTopAt(to);
        if (top != null && top.isOccupying() && top != mob) return false;

        mob.move(dx, dy);
        return true;
    }

    @Override
    public String save() {
        return name;
    }
}
