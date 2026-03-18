package Backend;

public class CharacterFactory extends FileFactory<Character>{
	private static String fileloc = "Backend/UserData/Characters";
	@Override
	public void save(Character obj) {
		write(fileloc + obj.save(), obj);	
	}

	@Override
	public Character load(String f) {
		return (Character) objfromfile(fileloc+f);
	}

}
