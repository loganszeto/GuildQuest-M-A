package Backend;

public class NPCCharacter extends Character {
    private String role;

    public NPCCharacter(String name, int health, String role) {
        super(name, health);
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}