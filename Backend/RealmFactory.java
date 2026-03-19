package Backend;

public class RealmFactory extends FileFactory<Realm>{
	private static String fileloc = "Backend/MapData/";
	@Override
	public void save(Realm obj) {
		write(fileloc + obj.save(), obj);
	}

	@Override
	public Realm load(String f) {
		Realm r = (Realm) objfromfile(fileloc+f);
		r.getRealmSpace();
		return r;
	}

}
