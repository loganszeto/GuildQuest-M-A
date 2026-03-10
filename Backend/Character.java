package Backend;

public class Character {
    private String name;
    private int health;
    private Position position;
    private Realm currentRealm;

    public Character(String name, int health) {
        this.name = name;
        this.health = health;
        this.position = null;
        this.currentRealm = null;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public Position getPosition() {
        return position;
    }

    public Realm getCurrentRealm() {
        return currentRealm;
    }

    public void setCurrentRealm(Realm realm) {
        this.currentRealm = realm;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public boolean move(int dRow, int dCol) {
        if (currentRealm == null || position == null) {
            return false;
        }

        int newRow = position.getRow() + dRow;
        int newCol = position.getCol() + dCol;

        return currentRealm.moveCharacter(this, new Position(newRow, newCol));
    }

    @Override
    public String toString() {
        return name + " HP=" + health + " Pos=" + position;
    }
}