package gmae;

import java.util.ArrayList;
import java.util.List;

/**
 * Player profile for GMAE system.
 * Stores player information connected to GuildQuest domain.
 */
public class PlayerProfile {
    private String username;
    private String characterName;
    private String preferredRealm;
    private List<String> questHistory;
    private List<String> achievements;
    private int wins;
    private int losses;
    private int coOpCompletions;
    
    public PlayerProfile(String username, String characterName) {
        this.username = username;
        this.characterName = characterName;
        this.preferredRealm = "Mystic Realms";
        this.questHistory = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
        this.coOpCompletions = 0;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getCharacterName() {
        return characterName;
    }
    
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
    
    public String getPreferredRealm() {
        return preferredRealm;
    }
    
    public void setPreferredRealm(String preferredRealm) {
        this.preferredRealm = preferredRealm;
    }
    
    public List<String> getQuestHistory() {
        return new ArrayList<>(questHistory);
    }
    
    public void addQuestToHistory(String questName) {
        questHistory.add(questName);
    }
    
    public List<String> getAchievements() {
        return new ArrayList<>(achievements);
    }
    
    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }
    
    public int getWins() {
        return wins;
    }
    
    public void addWin() {
        this.wins++;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public void addLoss() {
        this.losses++;
    }
    
    public int getCoOpCompletions() {
        return coOpCompletions;
    }
    
    public void addCoOpCompletion() {
        this.coOpCompletions++;
    }
    
    public double getWinRate() {
        int total = wins + losses;
        return total > 0 ? (double) wins / total : 0.0;
    }
    
    @Override
    public String toString() {
        return characterName + " (" + username + ") - Wins: " + wins + " Losses: " + losses;
    }
}
