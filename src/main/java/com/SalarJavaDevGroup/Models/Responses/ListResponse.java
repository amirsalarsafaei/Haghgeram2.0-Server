package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.Models.UserList;

import java.util.ArrayList;

public class ListResponse {
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


    public ListResponse(UserList list) {
        listName = list.getName();
        users = new ArrayList<>();
        for (User user:list.getUsers())
            if (user.isActive())
            users.add(user.getUsername());
    }

}
