package com.SalarJavaDevGroup.Models.Events;

public class ShowConversationEvent {
    private int id;

    public ShowConversationEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
