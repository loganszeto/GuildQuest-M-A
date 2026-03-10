package Backend;

import java.util.ArrayList;
import java.util.List;

public class Realm {
    private String name;
    private int rows;
    private int cols;
    private List<Character> characters;

    public Realm(String name, int rows, int cols) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.characters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Character> getCharacters() {
        return new ArrayList<>(characters);
    }

    public boolean isValidPosition(Position position) {
        if (position == null) {
            return false;
        }

        int row = position.getRow();
        int col = position.getCol();

        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isOccupied(Position position) {
        for (Character c : characters) {
            Position p = c.getPosition();
            if (p != null &&
                p.getRow() == position.getRow() &&
                p.getCol() == position.getCol()) {
                return true;
            }
        }
        return false;
    }

    public boolean addCharacter(Character character, Position startPos) {
        if (character == null || !isValidPosition(startPos) || isOccupied(startPos)) {
            return false;
        }

        characters.add(character);
        character.setCurrentRealm(this);
        character.setPosition(startPos);
        return true;
    }

    public boolean moveCharacter(Character character, Position newPos) {
        if (character == null || !characters.contains(character)) {
            return false;
        }

        if (!isValidPosition(newPos) || isOccupied(newPos)) {
            return false;
        }

        character.setPosition(newPos);
        return true;
    }

    public boolean removeCharacter(Character character) {
        if (character == null) {
            return false;
        }

        boolean removed = characters.remove(character);
        if (removed) {
            character.setCurrentRealm(null);
            character.setPosition(null);
        }
        return removed;
    }

    public void printRealm() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Character found = null;
                for (Character character : characters) {
                    Position p = character.getPosition();
                    if (p != null && p.getRow() == r && p.getCol() == c) {
                        found = character;
                        break;
                    }
                }

                if (found == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(found.getName().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }
}
