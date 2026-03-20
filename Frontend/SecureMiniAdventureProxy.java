package Frontend;

import Backend.RealmSpace;
import Backend.User;
import java.util.Map;

/**
 * Security proxy that validates/sanitizes user input before forwarding calls.
 */
public class SecureMiniAdventureProxy implements MiniAdventure {
    private final MiniAdventure delegate;

    public SecureMiniAdventureProxy(MiniAdventure delegate) {
        this.delegate = delegate;
    }

    public MiniAdventure getDelegate() {
        return delegate;
    }

    @Override
    public void initialize(User player1, User player2, Map<String, Object> settings) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Two valid players are required.");
        }
        delegate.initialize(player1, player2, settings);
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public boolean acceptPlayerInput(int playerNum, String input) {
        if (playerNum != 1 && playerNum != 2) {
            return false;
        }
        if (input == null) {
            return false;
        }
        String sanitized = input.trim().toLowerCase();
        if (sanitized.length() > 16) {
            return false;
        }
        return delegate.acceptPlayerInput(playerNum, sanitized);
    }

    @Override
    public void advanceTurn() {
        delegate.advanceTurn();
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return delegate.getCurrentState();
    }

    @Override
    public boolean isComplete() {
        return delegate.isComplete();
    }

    @Override
    public int getWinner() {
        return delegate.getWinner();
    }

    @Override
    public void reset() {
        delegate.reset();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    public RealmSpace getRealm() {
        return delegate.getRealm();
    }

    @Override
    public boolean supportsCoOp() {
        return delegate.supportsCoOp();
    }

    @Override
    public boolean supportsCompetitive() {
        return delegate.supportsCompetitive();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
