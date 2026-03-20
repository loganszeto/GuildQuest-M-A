package Frontend;

import Backend.User;

/**
 * Simple RBAC-style checks for local two-player actions.
 */
public class AccessControlService {
    public enum Action {
        START_ADVENTURE,
        OPEN_SETTINGS,
        DELETE_ADVENTURE
    }

    public boolean canPerform(Action action, User actingUser, User hostUser) {
        if (actingUser == null) {
            return false;
        }
        if (action == Action.OPEN_SETTINGS) {
            return true;
        }
        // Host-only actions in this local setup.
        return hostUser != null && hostUser.getUsername().equals(actingUser.getUsername());
    }
}
