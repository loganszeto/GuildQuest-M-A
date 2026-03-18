package Backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal campaign representation so the backend compiles.
 * A Campaign can be expanded later to manage sessions, realms, and adventure history.
 */
public class Campaign {
    private final String name;
    private final List<String> completedAdventures;

    public Campaign(String name) {
        this.name = name == null || name.isBlank() ? "Unnamed Campaign" : name;
        this.completedAdventures = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getCompletedAdventures() {
        return new ArrayList<>(completedAdventures);
    }

    public void addCompletedAdventure(String adventureName) {
        if (adventureName != null && !adventureName.isBlank()) {
            completedAdventures.add(adventureName);
        }
    }
}

