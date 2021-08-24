package com.SalarJavaDevGroup.Models.Events;

public class EditMessageEvent {
    private int id;
    private String newContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public EditMessageEvent(int id, String newContent) {
        this.id = id;
        this.newContent = newContent;
    }
}
