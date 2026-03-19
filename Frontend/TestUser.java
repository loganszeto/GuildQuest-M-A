package Frontend;

import javax.swing.*;

/**
 * Simple test user that doesn't depend on backend
 */
class TestUser {
    private String username;
    private java.util.List<String> achievements;
    
    public TestUser(String username) {
        this.username = username;
        this.achievements = new java.util.ArrayList<>();
    }
    
    public String getUsername() {
        return username;
    }
    
    public java.util.List<String> getAchievements() {
        return achievements;
    }
    
    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }
}
