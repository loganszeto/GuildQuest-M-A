package Backend;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RealmSpaceFactory{
	private static String fileloc = "Backend/MapData/Spaces/";
	//does not inherit from FileFactory, because RealmSpaces will not user serialization.
	public RealmSpace load(String f) {
		File file = new File("fileloc"+f+".txt"); 

        try (Scanner scanner = new Scanner(file)) {
            RealmSpace rs = new RealmSpace(f);
            while (scanner.hasNextLine()) {
                rs.addTile(processLine(scanner));
            }
            return rs;
        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found
            System.out.println("File not found: " + file.getName());
            e.printStackTrace();
            return null;
        }
	}
	
	private Tile processLine(Scanner sc) {
		Scanner lsc = new Scanner(sc.nextLine());
		String tiletype = lsc.next();
		switch (tiletype) {
		case "Ground":
			return new Ground(new Point(lsc.nextInt(),lsc.nextInt()));
		case "Relic":
			return new RelicTile(new Point(lsc.nextInt(),lsc.nextInt()));
		case "Trap":
			return new TrapTile(new Point(lsc.nextInt(),lsc.nextInt()));
		case "TileGroup":
			return new TileGroup<>(new Point(lsc.nextInt(),lsc.nextInt()),
					new Point(lsc.nextInt(),lsc.nextInt()),
					processLine(sc));
		case "RandomGroup":
			return new RandomDistTileGroup<>(new Point(lsc.nextInt(),lsc.nextInt()),
					new Point(lsc.nextInt(),lsc.nextInt()),
					lsc.nextDouble(),
					processLine(sc));
		case "NPC":
			return new NPC(new Point(lsc.nextInt(),lsc.nextInt()),new NPCCharacter(lsc.next(),lsc.nextInt(),lsc.next()));
		case "Player":
			return new Player(new Point(lsc.nextInt(),lsc.nextInt()),null);
		default:
			return null;
		}
		
	}
}
