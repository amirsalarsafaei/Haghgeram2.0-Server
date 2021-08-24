package com.SalarJavaDevGroup.Models.Events;

public class DeleteListEvent {
    public DeleteListEvent(String listName) {
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
