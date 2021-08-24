package com.SalarJavaDevGroup.Models.Events;

import java.util.ArrayList;

public class AddMembersToGroupSendableEvent {
    private ArrayList<String> res;
    private int groupId;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getRes() {
        return res;
    }

    public void setRes(ArrayList<String> res) {
        this.res = res;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public AddMembersToGroupSendableEvent(AddMembersToGroupEvent addMembersToGroupEvent) {
        this.groupId = addMembersToGroupEvent.getGroupId();
        this.res = addMembersToGroupEvent.getRes();
        this.name = addMembersToGroupEvent.getName();
    }
}
