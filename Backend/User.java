package backend;

import java.util.ArrayList;
import java.util.List;

public class User implements Savable {
    private String username;
    private String characterName;
    private String preferredRealm;
    private List<String> achievements;
    private List<String> questHistory;
    private int wins;
    private int losses;
    private int coOpCompletions;
    private PlayerCharacter activeCharacter;

    public User(String username, String preferredRealm) {
        this.username = username;
        this.characterName = username;
        this.preferredRealm = preferredRealm;
        this.achievements = new ArrayList<>();
        this.questHistory = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.coOpCompletions = 0;
        this.activeCharacter = null;
    }

    public String getUsername() {
        return username;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        if (characterName != null && !characterName.isBlank()) {
            this.characterName = characterName;
        }
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

    public void addQuestToHistory(String quest) {
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

    public int getWins() {
        return wins;
    }

    public void addWin() {
        wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void addLoss() {
        losses++;
    }

    public int getCoOpCompletions() {
        return coOpCompletions;
    }

    public void addCoOpCompletion() {
        coOpCompletions++;
    }

    public double getWinRate() {
        int total = wins + losses;
        return total > 0 ? (double) wins / total : 0.0;
    }

    @Override
    public String toString() {
        return characterName + " (" + username + ") - Wins: " + wins + " Losses: " + losses;
    }

    @Override
    public String save() {
        return toString();
    }
}
