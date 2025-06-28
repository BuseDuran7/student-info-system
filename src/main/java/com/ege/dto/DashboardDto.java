// 12. DashboardDto - Ana sayfa için
package com.ege.dto;

import java.util.List;
import java.util.Map;

public class DashboardDto {
    private String userRole;
    private String welcomeMessage;
    private Map<String, Object> statistics;
    private List<String> recentActivities;
    private List<String> notifications;
    private List<String> quickActions;

    // Getters and setters
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getWelcomeMessage() { return welcomeMessage; }
    public void setWelcomeMessage(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }

    public Map<String, Object> getStatistics() { return statistics; }
    public void setStatistics(Map<String, Object> statistics) { this.statistics = statistics; }

    public List<String> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<String> recentActivities) { this.recentActivities = recentActivities; }

    public List<String> getNotifications() { return notifications; }
    public void setNotifications(List<String> notifications) { this.notifications = notifications; }

    public List<String> getQuickActions() { return quickActions; }
    public void setQuickActions(List<String> quickActions) { this.quickActions = quickActions; }
}