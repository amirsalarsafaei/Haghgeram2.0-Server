package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

import java.util.ArrayList;

public class FollowRequestsStatusResponse {

    public FollowRequestsStatusResponse(User user) {
        accepted = new ArrayList<>();
        pending = new ArrayList<>();
        rejected = new ArrayList<>();
        for (User tmp: user.getAccepted())
            if (tmp.isActive())
                accepted.add(tmp.getUsername());
        for (User tmp: user.getPending())
            if (tmp.isActive())
                pending.add(tmp.getUsername());
        for (User tmp: user.getDenied())
            if (tmp.isActive())
                rejected.add(tmp.getUsername());
    }


    private ArrayList<String> accepted, rejected, pending;

    public ArrayList<String> getAccepted() {
        return accepted;
    }

    public void setAccepted(ArrayList<String> accepted) {
        this.accepted = accepted;
    }

    public ArrayList<String> getRejected() {
        return rejected;
    }

    public void setRejected(ArrayList<String> rejected) {
        this.rejected = rejected;
    }

    public ArrayList<String> getPending() {
        return pending;
    }

    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }
}