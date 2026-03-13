package Backend;

import java.util.ArrayList;
import java.util.List;

public class User{
    private String username;
    private String preferredRealm;
    private List<String> achievements;
    private List<String> questHistory;
    private PlayerCharacter activeCharacter;

    public User(String username, String preferredRealm) {
        this.username = username;
        this.preferredRealm = preferredRealm;
        this.achievements = new ArrayList<>();
        this.questHistory = new ArrayList<>();
        this.activeCharacter = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPreferredRealm() {
        return preferredRealm;
    }

    public void setPreferredRealm(String preferredRealm) {
        this.preferredRealm = preferredRealm;
    }

    public PlayerCharacter getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(PlayerCharacter activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    public void addAchievement(String achievement) {
        if (achievement != null && !achievement.isBlank()) {
            achievements.add(achievement);
        }
    }

    public void addQuestHistory(String quest) {
        if (quest != null && !quest.isBlank()) {
            questHistory.add(quest);
        }
    }

    public List<String> getAchievements() {
        return new ArrayList<>(achievements);
    }

    public List<String> getQuestHistory() {
        return new ArrayList<>(questHistory);
    }

    @Override
    public String toString() {
        return "User: " + username +
               ", Preferred Realm: " + preferredRealm +
               ", Active Character: " +
               (activeCharacter == null ? "None" : activeCharacter.getName());
    }
}
