package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Events.AddMembersToGroupEvent;
import com.SalarJavaDevGroup.Models.Events.AddMembersToGroupSendableEvent;
import com.SalarJavaDevGroup.Models.Events.LeaveGroupEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;

public class Conversations {
    private final DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Conversations.class);
    public Conversations(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void leaveGroup(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        LeaveGroupEvent event = GsonHandler.getGson().fromJson(request.data, LeaveGroupEvent.class);
        ManageConversation manageConversation = new ManageConversation(session);
        Conversation conversation = manageConversation.getConversation(event.getGroup_id());
        if (conversation == null) {
            logger.error("Request for invalid leave group " + event.getGroup_id());
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid Conversation"));
            session.close();
            return;
        }
        if (!conversation.getUsers().contains(user)) {
            logger.error(user.getUsername() + " sent a request for conversation is not in");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest,
                    properties.loadDialog("user-must-in-conversation")));
            session.close();
            return;
        }
        user.getConversations().remove(conversation);
        conversation.getUsers().remove(user);
        logger.info(user.getUsername() + " left from group " + conversation.getId());
        manageUser.Save(user);
        logger.info(user.getId() + " saved");
        manageConversation.Save(conversation);
        logger.info(conversation.getId() + " saved");
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void createGroup(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        User user = manageUser.getUserById(request.user);
        AddMembersToGroupSendableEvent event = GsonHandler.getGson().fromJson(request.data, AddMembersToGroupSendableEvent.class);
        ArrayList<User> users = new ArrayList<>();
        for (String username : event.getRes()) {
            User target = manageUser.getUser(username);
            if (target == null || !user.getFollowing().contains(target) || !user.isActive()) {
                logger.warn(user.getUsername() + " wants to add " + target.getUsername() + " to group but is denied");
                continue;
            }
            users.add(target);
        }
        users.add(user);
        Conversation conversation = new Conversation(users);
        logger.info(user.getUsername() + " created a group with a name " + event.getName());
        conversation.setName(event.getName());
        manageConversation.Save(conversation);
        logger.info("Conversation" + conversation.getId() + " saved");
        for (User tmp:users) {
            tmp.getConversations().add(conversation);
            logger.info(tmp.getUsername() + " added to the group " + conversation.getId());
            manageUser.Save(user);
            logger.info("User " + tmp.getId() + " saved");
        }

        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void addUsersToGroup(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ManageConversation manageConversation = new ManageConversation(session);
        AddMembersToGroupSendableEvent event = GsonHandler.getGson().fromJson(request.data,
                AddMembersToGroupSendableEvent.class);
        Conversation conversation = manageConversation.getConversation(event.getGroupId());

        if (conversation == null) {
            logger.warn(user.getUsername() + " wants to add users to invalid group");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("conv-exist")));
            return;
        }


        if (!conversation.isGroup()) {
            logger.warn(user.getUsername() + " wants to add users to a conversation that is not a group");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("conv-group")));
            return;
        }
        if (!conversation.getUsers().contains(user)) {
            logger.warn(user.getUsername() + " wants to add users to a group that is not in it");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-must-in-conversation")));
            return;
        }

        ArrayList<User> users = new ArrayList<>();

        for (String username: event.getRes()) {
            User target = manageUser.getUser(username);
            if (target == null || !user.getFollowing().contains(target) || conversation.getUsers().contains(target) ||
                !target.isActive()) {
                logger.warn(user.getUsername() + " wants to add " + target.getUsername() + " to group but is denied");
                continue;
            }
            users.add(target);
            target.getConversations().add(conversation);
            logger.info(user.getUsername() + " added " + target.getUsername() + " to group " + conversation.getId());
        }
        conversation.getUsers().addAll(users);
        logger.info("added users to group");
        manageConversation.Save(conversation);
        logger.info("Conversation " + conversation.getId() + " saved");
        for (User user1: users) {
            manageUser.Save(user1);
            logger.info("User " + user.getId() + " saved");
        }
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

}
