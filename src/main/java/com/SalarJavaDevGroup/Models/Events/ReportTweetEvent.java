package com.SalarJavaDevGroup.Models.Events;

public class ReportTweetEvent {
    private int id;

    public ReportTweetEvent(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
