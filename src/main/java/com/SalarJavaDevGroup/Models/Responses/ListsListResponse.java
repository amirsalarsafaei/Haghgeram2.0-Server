package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.UserList;

import java.util.ArrayList;
import java.util.Set;

public class ListsListResponse {
    private ArrayList<String> lists;

    public ArrayList<String> getLists() {
        return lists;
    }

    public void setLists(ArrayList<String> lists) {
        this.lists = lists;
    }

    public ListsListResponse(ArrayList<UserList> lists) {
        this.lists = new ArrayList<>();
        for (UserList list: lists)
            this.lists.add(list.getName());
    }
}
