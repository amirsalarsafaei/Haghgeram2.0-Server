package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationsResponse {
    private ArrayList<String> events;
    private ArrayList<String> requests;

    public NotificationsResponse(User user) {
        events = new ArrayList<>(user.getEvents());
        requests = new ArrayList<>();
        for (User target: user.getFollowRequests()) {
            if (target.isActive())
                requests.add(target.getUsername());
        }
        Collections.sort(requests);
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }
}
