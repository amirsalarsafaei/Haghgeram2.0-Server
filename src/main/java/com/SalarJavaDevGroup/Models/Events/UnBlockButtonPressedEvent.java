package com.SalarJavaDevGroup.Models.Events;

public class UnBlockButtonPressedEvent {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UnBlockButtonPressedEvent(String username) {
        this.username = username;
    }
}
