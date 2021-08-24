package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

import java.util.ArrayList;

public class BlackListResponse {
    public BlackListResponse(User user) {
        blackList = new ArrayList<>();
        for (User blockUser:user.getBlocked()) {
            if (blockUser.isActive())
                blackList.add(blockUser.getUsername());
        }
    }


    private ArrayList<String> blackList;

    public ArrayList<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<String> blackList) {
        this.blackList = blackList;
    }
}
