package com.SalarJavaDevGroup.Models.Events;

public class AddMemberToGroupViewEvent {
    private int group_id;

    public AddMemberToGroupViewEvent(int group_id) {
        this.group_id = group_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
