package com.SalarJavaDevGroup.Models.Events;

public class LikeButtonEvent {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LikeButtonEvent(int id) {
        this.id = id;
    }
}
