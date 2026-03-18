import Backend.Realm;
import Backend.RealmFactory;

public class RealmFactoryTest {
    public static void main(String[] args) {
        System.out.println("=== RealmFactory Test ===\n");
        
        // Test 1: Create a realm
        System.out.println("Test 1: Creating realm...");
        Realm testRealm = new Realm("TestDungeon");
        System.out.println("✓ Created realm: " + testRealm.getName());
        System.out.println("  Width: " + testRealm.getWidth());
        System.out.println("  Height: " + testRealm.getHeight());
        System.out.println("  RealmSpace: " + (testRealm.getRealmSpace() != null ? "Present" : "NULL"));
        
        // Test 2: Save realm
        System.out.println("\nTest 2: Saving realm...");
        RealmFactory factory = new RealmFactory();
        try {
            factory.save(testRealm);
            System.out.println("✓ Realm saved successfully");
        } catch (Exception e) {
            System.out.println("✗ Save failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test 3: Load realm
        System.out.println("\nTest 3: Loading realm...");
        try {
            Realm loadedRealm = factory.load("TestDungeon");
            if (loadedRealm != null) {
                System.out.println("✓ Realm loaded successfully");
                System.out.println("  Name: " + loadedRealm.getName());
                System.out.println("  Width: " + loadedRealm.getWidth());
                System.out.println("  Height: " + loadedRealm.getHeight());
                System.out.println("  RealmSpace: " + (loadedRealm.getRealmSpace() != null ? "Present" : "NULL"));
            } else {
                System.out.println("✗ Load returned null");
            }
        } catch (Exception e) {
            System.out.println("✗ Load failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
