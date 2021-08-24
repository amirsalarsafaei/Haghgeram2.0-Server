package com.SalarJavaDevGroup.Models.Events;

public class FollowEvent {
    String username;

    public String getUsername() {
        return username;
    }

    public FollowEvent(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
