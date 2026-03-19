package Backend.Tiles;

import Backend.Character;

public class NPCCharacter extends Character {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String role;

    public NPCCharacter(String name, int health, String role) {
        super(name, health);
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}