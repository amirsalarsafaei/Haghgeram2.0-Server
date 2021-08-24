package com.SalarJavaDevGroup.Models.Events;

public class AddListEvent {
    public AddListEvent(String listName) {
        this.listName = listName;
    }

    private String listName;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
