package com.SalarJavaDevGroup.Models.Events;

import java.util.ArrayList;

public class AddUsersToListCompletedEvent {
    private String listName;
    private ArrayList<String> users;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public AddUsersToListCompletedEvent(ArrayList<String> users, String listName) {
        this.users = users;
        this.listName = listName;
    }
}
