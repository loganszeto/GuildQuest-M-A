package backend;

public class Character implements Savable {
    private String name;
    private int health;

    public Character(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            return;
        }
        health = Math.max(0, health - amount);
    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }
        health += amount;
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String toString() {
        return name + " HP=" + health;
    }

    @Override
    public String save() {
        return toString();
    }
}