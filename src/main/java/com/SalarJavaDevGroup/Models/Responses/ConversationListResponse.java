package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.util.Compare;

import java.util.ArrayList;
import java.util.Collections;

public class ConversationListResponse {
    private ArrayList<ConversationPreviewResponse> conversations;

    public ArrayList<ConversationPreviewResponse> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<ConversationPreviewResponse> conversations) {
        this.conversations = conversations;
    }

    public ConversationListResponse(User user) {
        ArrayList<Conversation> conversations = new ArrayList<>(user.getConversations());
        Collections.sort(conversations, new Compare.compare_conversations(user));
        this.conversations = new ArrayList<>();
        for (Conversation conversation : conversations)
            this.conversations.add(new ConversationPreviewResponse(conversation, user));

    }

}
