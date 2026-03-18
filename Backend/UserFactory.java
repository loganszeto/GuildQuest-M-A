package Backend;

public class UserFactory extends FileFactory<User>{
	private static String fileloc = "/Backend/UserData/";
	@Override
	public void save(User obj) {
		write(fileloc + obj.save(), obj);	
	}

	@Override
	public User load(String f) {
		return (User) objfromfile(fileloc+f);
	}

}
