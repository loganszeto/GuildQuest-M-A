package Frontend;

import Backend.User;
import javax.swing.*;

/**
 * Simple test adventure that doesn't depend on backend
 */
class TestAdventure implements MiniAdventure {
    @Override
    public void initialize(User player1, User player2, java.util.Map<String, Object> settings) {
        // No initialization needed
    }
    
    @Override
    public void start() {
        // No start logic needed
    }
    
    @Override
    public boolean acceptPlayerInput(int playerNum, String input) {
        return true; // Accept all input
    }
    
    @Override
    public void advanceTurn() {
        // No turn logic needed
    }
    
    @Override
    public java.util.Map<String, Object> getCurrentState() {
        return new java.util.HashMap<>();
    }
    
    @Override
    public boolean isComplete() {
        return false; // Never complete (handled by TestGameScreen)
    }
    
    @Override
    public int getWinner() {
        return 0; // No winner (handled by TestGameScreen)
    }
    
    @Override
    public void reset() {
        // No reset needed
    }
    
    @Override
    public String getName() {
        return "Test Adventure";
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public String getDescription() {
        return "A simple test adventure to demonstrate game mechanics. Collect all relics on the board to win!";
    }
    
    @Override
    public Backend.RealmSpace getRealm() {
        return null; // No realm needed
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
