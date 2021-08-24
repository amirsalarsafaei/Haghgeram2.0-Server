package com.SalarJavaDevGroup.Models.Events;

import java.util.ArrayList;

public class SendMessageGroupTweetEvent {
    private int tweet_id;
    private ArrayList<String> followings, groups;
    private ArrayList<Integer> conversations;

    public SendMessageGroupTweetEvent(ArrayList<String> followings, ArrayList<String> groups,
                                      ArrayList<Integer> conversations, int tweet_id) {
        this.followings = followings;
        this.groups = groups;
        this.conversations = conversations;
        this.tweet_id = tweet_id;
    }

    public int getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(int tweet_id) {
        this.tweet_id = tweet_id;
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
