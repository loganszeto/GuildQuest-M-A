package Frontend;

import Backend.RelicHuntGameBackend;
import Backend.TimedRaidGameBackend;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory/registry for available mini-adventures.
 * New adventures can be added in one place without editing menu logic.
 */
public final class AdventureRegistry {
    private AdventureRegistry() {}

    public static List<MiniAdventure> createDefaultAdventures() {
        List<MiniAdventure> adventures = new ArrayList<>();
        adventures.add(new RelicHuntGameBackend());
        adventures.add(new TimedRaidGameBackend());
        return adventures;
    }
}
