package com.SalarJavaDevGroup.Models.Events;

import java.util.ArrayList;

public class SendGroupMessageEvent {
    private ArrayList<String> followings, groups;
    private ArrayList<Integer> conversations;
    private String content;
    private byte[] image;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public SendGroupMessageEvent(ArrayList<String> followings, ArrayList<String> groups, ArrayList<Integer> conversations) {
        this.followings = followings;
        this.groups = groups;
        this.conversations = conversations;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public ArrayList<Integer> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Integer> conversations) {
        this.conversations = conversations;
    }
}