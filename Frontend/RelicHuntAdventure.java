package Frontend;

import Backend.RealmSpace;
import Backend.RelicHuntGameBackend;
import Backend.User;
import java.util.Map;

/**
 * Sample mini-adventure: Relic Hunt
 * Players compete to collect relics in the realm.
 * Delegates game rules/state to backend.RelicHuntGame.
 */
public class RelicHuntAdventure implements MiniAdventure {
    public RelicHuntGameBackend game;
    
    public RelicHuntAdventure() {
        // Don't create the game immediately to avoid realm loading issues
        this.game = null;
    }
    
    @Override
    public void initialize(User player1, User player2, Map<String, Object> settings) {
        // Create the game only when initializing
        if (game == null) {
            game = new RelicHuntGameBackend();
        }
        boolean competitive = settings != null && "competitive".equals(settings.get("mode"));
        game.initialize(player1, player2, competitive);
    }
    
    @Override
    public void start() {
        if (game != null) {
            game.start();
        }
    }
    
    @Override
    public boolean acceptPlayerInput(int playerNum, String input) {
        return game != null ? game.acceptPlayerInput(playerNum, input) : false;
    }
    
    @Override
    public void advanceTurn() {
        if (game != null) {
            game.advanceTurn();
        }
    }
    
    @Override
    public Map<String, Object> getCurrentState() {
        return game != null ? game.getCurrentState() : new java.util.HashMap<>();
    }
    
    
    @Override
    public boolean isComplete() {
        return game != null ? game.isComplete() : false;
    }
    
    @Override
    public int getWinner() {
        return game != null ? game.getWinner() : 0;
    }
    
    @Override
    public void reset() {
        if (game != null) {
            game.reset();
        }
    }
    
    @Override
    public String getName() {
        return "Relic Hunt";
    }
    
    @Override
    public String toString() {
        return getName(); // Return the adventure name for JList display
    }
    
    @Override
    public String getDescription() {
        return "Compete to collect ancient relics scattered throughout the realm. Use WASD or arrow keys to move.";
    }
    
    @Override
    public RealmSpace getRealm() {
        return game != null ? game.getRealmSpace() : new RealmSpace("Default");
    }
    
    @Override
    public boolean supportsCoOp() {
        return true;
    }
    
    @Override
    public boolean supportsCompetitive() {
        return true;
    }
}
