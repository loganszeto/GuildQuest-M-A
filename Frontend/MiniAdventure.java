package gmae;

import java.awt.Point;
import java.util.Map;

/**
 * Interface that all mini-adventures must implement.
 * Provides the core functionality for GMAE mini-adventures.
 */
public interface MiniAdventure {
    
    /**
     * Initialize the adventure with player information and settings.
     * @param player1 Profile of first player
     * @param player2 Profile of second player  
     * @param settings Adventure-specific configuration
     */
    void initialize(PlayerProfile player1, PlayerProfile player2, Map<String, Object> settings);
    
    /**
     * Start or restart the adventure.
     */
    void start();
    
    /**
     * Process input from a player.
     * @param playerNum 1 or 2 for which player
     * @param input Player input (keyboard, mouse, etc.)
     * @return True if input was processed successfully
     */
    boolean acceptPlayerInput(int playerNum, String input);
    
    /**
     * Advance the game state by one turn/time unit.
     */
    void advanceTurn();
    
    /**
     * Get the current state of the adventure.
     * @return Current game state as a map
     */
    Map<String, Object> getCurrentState();
    
    /**
     * Check if the adventure is complete.
     * @return Completion status
     */
    boolean isComplete();
    
    /**
     * Get the winner of the adventure (if competitive).
     * @return 1, 2, or 0 for tie/not finished
     */
    int getWinner();
    
    /**
     * Reset the adventure to initial state.
     */
    void reset();
    
    /**
     * Get the name of this adventure.
     * @return Adventure name
     */
    String getName();
    
    /**
     * Get the description of this adventure.
     * @return Adventure description
     */
    String getDescription();
    
    /**
     * Get the realm/map this adventure takes place in.
     * @return RealmSpace for the adventure
     */
    Backend.RealmSpace getRealm();
    
    /**
     * Check if this adventure supports co-op mode.
     * @return True if co-op is supported
     */
    boolean supportsCoOp();
    
    /**
     * Check if this adventure supports competitive mode.
     * @return True if competitive is supported
     */
    boolean supportsCompetitive();
}
