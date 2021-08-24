package com.SalarJavaDevGroup.Models.Events;

public class AcceptFollowRequestEvent {
    private String username;

    public AcceptFollowRequestEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
