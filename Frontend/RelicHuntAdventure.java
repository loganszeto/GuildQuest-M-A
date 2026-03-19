package Frontend;

import Backend.RealmSpace;
import Backend.RelicHuntGame;
import Backend.User;
import java.util.Map;

/**
 * Sample mini-adventure: Relic Hunt
 * Players compete to collect relics in the realm.
 * Delegates game rules/state to backend.RelicHuntGame.
 */
public class RelicHuntAdventure implements MiniAdventure {
    public final RelicHuntGame game;
    
    public RelicHuntAdventure() {
        this.game = new RelicHuntGame();
    }
    
    @Override
    public void initialize(User player1, User player2, Map<String, Object> settings) {
        boolean competitive = settings != null && "competitive".equals(settings.get("mode"));
        game.initialize(player1, player2, competitive);
    }
    
    @Override
    public void start() {
        game.start();
    }
    
    @Override
    public boolean acceptPlayerInput(int playerNum, String input) {
        return game.acceptPlayerInput(playerNum, input);
    }
    
    @Override
    public void advanceTurn() {
        game.advanceTurn();
    }
    
    
    @Override
    public boolean isComplete() {
        return game.isComplete();
    }
    
    @Override
    public int getWinner() {
        return game.getWinner();
    }
    
    @Override
    public void reset() {
        game.reset();
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
        return game.getRealmSpace();
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
