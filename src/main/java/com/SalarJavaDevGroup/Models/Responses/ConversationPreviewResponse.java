package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Message;
import com.SalarJavaDevGroup.Models.User;

public class ConversationPreviewResponse {
    private String preview, name;
    private int unread, id;
    private boolean group;

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public ConversationPreviewResponse(Conversation conversation, User user) {
        unread = conversation.unread(user);
        name = conversation.getName(user);
        if (conversation.getLatestMessage() != null)
            preview = conversation.getLatestMessage().getContent();
        else
            preview = "";
        id = conversation.getId();
        group = conversation.isGroup();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
