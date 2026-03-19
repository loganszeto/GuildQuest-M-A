package Backend.Maps;

import java.awt.Point;

import Backend.NPCCharacter;
import Backend.RealmSpace;
import Backend.Tiles.*;

public class UnknownRealm {
	//helper to save on typing out new Point every time
	private static Point p(int a, int b) {
		return new Point(a,b);
	}
	
	public static RealmSpace getSpace() {
		RealmSpace ret = new RealmSpace("UnknownRealm");
		//for TileGroups's representatives
		Point defaultp = p(0,0);
		//the Map itself
		ret.addTile(new TileGroup<>(p(-10,-10),
				p(10,10),
				new Ground(defaultp)));
		ret.addTile(new RelicTile(p(-5,5)));
		ret.addTile(new TileGroup<>(p(0,5),
				p(1,6),
				new TrapTile(defaultp)));
		ret.addTile(new NPC(p(5,5),new NPCCharacter("Default", 10, "Villager")));
		ret.addTile(new Player(p(-1, 0),null));
		ret.addTile(new Player(p(1,0),null));
		
		return ret;
	}
}
