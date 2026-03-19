package Backend;

public class DefaultPopulator {
	public static void populate() {
		//create and save a few users with a few characters 
		CharacterFactory cf = new CharacterFactory();
		UserFactory uf = new UserFactory();
		
		PlayerCharacter pc1 = new PlayerCharacter("Knight",20);
		cf.save(pc1);
		PlayerCharacter pc2 = new PlayerCharacter("Archer",15);
		cf.save(pc2);
		PlayerCharacter pc3 = new PlayerCharacter("Mage",10);
		cf.save(pc3);
		User user1 = new User("Alice", "UnknownRealm");
		user1.addCharacterName("Knight");
		uf.save(user1);
		User user2 = new User("Bob", "UnknownRealm");
		user2.addCharacterName("Archer");
		uf.save(user2);
		User user3 = new User("Charlie", "UnknownRealm");
		user3.addCharacterName("Mage");
		uf.save(user3);
	}
}
