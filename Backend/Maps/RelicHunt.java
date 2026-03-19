package Backend.Maps;

import java.awt.Point;

import Backend.RealmSpace;
import Backend.Tiles.*;

public class RelicHunt {
	private static Point p(int a, int b) {
		return new Point(a,b);
	}
	
	public static RealmSpace getSpace() {
		RealmSpace ret = new RealmSpace("RelicHunt");
		//for TileGroups's representatives
		Point defaultp = p(0,0);
		//the Map itself
		ret.addTile(new RandomDistTileGroup<>(p(0,0),
				p(30,30),
				0.8,
				new Ground(defaultp)));
		ret.addTile(new Player(p(1,1),null));
		ret.addTile(new Player(p(29,29),null));
		return ret;
	}
}
