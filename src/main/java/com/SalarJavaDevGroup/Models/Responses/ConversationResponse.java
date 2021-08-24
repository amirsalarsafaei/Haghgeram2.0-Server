package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.ManageDB.ManageMessages;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Message;
import com.SalarJavaDevGroup.Models.MessageStatus;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.util.Compare;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class ConversationResponse {
    private ArrayList<MessageResponse> messages;
    private String name;
    private int id;
    private boolean group;

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public ConversationResponse(Conversation conversation, User user, Session session) {
        id = conversation.getId();
        name = conversation.getName(user);
        ArrayList<Message> messages = new ArrayList<>(conversation.getMessages());
        Collections.sort(messages, Compare.compare_message_by_date);
        this.messages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTimeSent().isBefore(LocalDateTime.now())) {
                this.messages.add(new MessageResponse(message));
                if (message.getMessageStatus().equals(MessageStatus.UnSeen))
                    if (message.getUser() != user) {
                        message.setMessageStatus(MessageStatus.Seen);
                        message.getUserReads().add(user);
                        ManageMessages manageMessages = new ManageMessages(session);
                        manageMessages.Save(message);
                    }
            }
        }
        group = conversation.isGroup();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageResponse> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
