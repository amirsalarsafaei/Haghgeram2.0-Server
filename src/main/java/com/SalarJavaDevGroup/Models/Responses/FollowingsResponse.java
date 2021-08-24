package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

import java.util.ArrayList;
import java.util.Set;

public class FollowingsResponse {
    private ArrayList<String> users;

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public FollowingsResponse(Set<User> users) {
        this.users = new ArrayList<>();
        for (User user:users)
            if (user.isActive())
                this.users.add(user.getUsername());
    }
}
