package gmae;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages two-player local games in the GMAE system.
 * Handles player profiles, game state, and turn management.
 */
public class TwoPlayerGameManager {
    private PlayerProfile player1;
    private PlayerProfile player2;
    private MiniAdventure currentAdventure;
    private int currentPlayerTurn;
    private boolean isCompetitive;
    private boolean gameActive;
    
    public TwoPlayerGameManager() {
        this.currentPlayerTurn = 1;
        this.gameActive = false;
    }
    
    /**
     * Set up a new two-player game.
     */
    public boolean setupGame(PlayerProfile p1, PlayerProfile p2, MiniAdventure adventure, boolean competitive) {
        if (p1 == null || p2 == null || adventure == null) {
            return false;
        }
        
        this.player1 = p1;
        this.player2 = p2;
        this.currentAdventure = adventure;
        this.isCompetitive = competitive;
        this.currentPlayerTurn = 1;
        
        // Initialize adventure with players
        Map<String, Object> settings = new HashMap<>();
        settings.put("mode", competitive ? "competitive" : "coop");
        settings.put("realm", adventure.getRealm().getName());
        
        adventure.initialize(p1, p2, settings);
        adventure.start();
        this.gameActive = true;
        
        return true;
    }
    
    /**
     * Process input from current player.
     */
    public boolean processInput(String input) {
        if (!gameActive || currentAdventure == null) {
            return false;
        }
        
        boolean success = currentAdventure.acceptPlayerInput(currentPlayerTurn, input);
        
        if (success) {
            // Check if adventure is complete
            if (currentAdventure.isComplete()) {
                endGame();
                return true;
            }
            
            // Switch turns
            currentPlayerTurn = currentPlayerTurn == 1 ? 2 : 1;
            
            // Advance game state
            currentAdventure.advanceTurn();
        }
        
        return success;
    }
    
    /**
     * Get the current player's profile.
     */
    public PlayerProfile getCurrentPlayer() {
        return currentPlayerTurn == 1 ? player1 : player2;
    }
    
    /**
     * Get the other player's profile.
     */
    public PlayerProfile getOtherPlayer() {
        return currentPlayerTurn == 1 ? player2 : player1;
    }
    
    /**
     * Get current game state.
     */
    public Map<String, Object> getCurrentState() {
        Map<String, Object> state = new HashMap<>();
        state.put("gameActive", gameActive);
        state.put("currentPlayerTurn", currentPlayerTurn);
        state.put("isCompetitive", isCompetitive);
        state.put("player1", player1);
        state.put("player2", player2);
        state.put("adventureState", currentAdventure != null ? currentAdventure.getCurrentState() : null);
        return state;
    }
    
    /**
     * End the current game and update player profiles.
     */
    private void endGame() {
        if (!gameActive || currentAdventure == null) {
            return;
        }
        
        int winner = currentAdventure.getWinner();
        
        if (isCompetitive) {
            // Competitive mode - update wins/losses
            if (winner == 1) {
                player1.addWin();
                player2.addLoss();
            } else if (winner == 2) {
                player2.addWin();
                player1.addLoss();
            }
        } else {
            // Co-op mode - both get completion credit
            player1.addCoOpCompletion();
            player2.addCoOpCompletion();
        }
        
        // Add to quest history
        player1.addQuestToHistory(currentAdventure.getName());
        player2.addQuestToHistory(currentAdventure.getName());
        
        gameActive = false;
    }
    
    /**
     * Reset the current game.
     */
    public void resetGame() {
        if (currentAdventure != null) {
            currentAdventure.reset();
            currentPlayerTurn = 1;
            gameActive = true;
        }
    }
    
    /**
     * Get player 1 profile.
     */
    public PlayerProfile getPlayer1() {
        return player1;
    }
    
    /**
     * Get player 2 profile.
     */
    public PlayerProfile getPlayer2() {
        return player2;
    }
    
    /**
     * Get current adventure.
     */
    public MiniAdventure getCurrentAdventure() {
        return currentAdventure;
    }
    
    /**
     * Check if game is active.
     */
    public boolean isGameActive() {
        return gameActive;
    }
    
    /**
     * Check if game is competitive.
     */
    public boolean isCompetitive() {
        return isCompetitive;
    }
    
    /**
     * Get current player turn.
     */
    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }
}
