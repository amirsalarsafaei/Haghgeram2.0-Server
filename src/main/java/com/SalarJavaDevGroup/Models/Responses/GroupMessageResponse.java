package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.Models.UserList;

import java.util.ArrayList;

public class GroupMessageResponse {
    private ArrayList<String> followings, groups, conversations;
    private ArrayList<Integer> groupsId;

    public ArrayList<Integer> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(ArrayList<Integer> groupsId) {
        this.groupsId = groupsId;
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

    public ArrayList<String> getConversations() {
        return conversations;
    }

    public GroupMessageResponse(User user) {
        followings = new ArrayList<>();
        groups = new ArrayList<>();
        conversations = new ArrayList<>();
        groupsId = new ArrayList<>();
        for (User following: user.getFollowing())
            if (following.isActive())
                followings.add(following.getUsername());
        for (UserList userList: user.getLists()) {
            groups.add(userList.getName());

        }
        for (Conversation conversation:user.getConversations()) {
            conversations.add(conversation.getName(user));
            groupsId.add(conversation.getId());
        }
    }

    public void setConversations(ArrayList<String> conversations) {
        this.conversations = conversations;
    }
}
