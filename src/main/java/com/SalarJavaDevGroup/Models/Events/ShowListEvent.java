package com.SalarJavaDevGroup.Models.Events;

public class ShowListEvent {
    private String listEvent;

    public String getListEvent() {
        return listEvent;
    }

    public void setListEvent(String listEvent) {
        this.listEvent = listEvent;
    }

    public ShowListEvent(String listEvent) {
        this.listEvent = listEvent;
    }
}
