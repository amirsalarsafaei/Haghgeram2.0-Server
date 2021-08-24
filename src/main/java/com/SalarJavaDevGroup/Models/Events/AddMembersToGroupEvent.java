package com.SalarJavaDevGroup.Models.Events;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class AddMembersToGroupEvent extends Event {

    public AddMembersToGroupEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

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

    public AddMembersToGroupEvent(ArrayList<String> res) {
        super(MouseEvent.ANY);
        this.res = res;
    }
}
