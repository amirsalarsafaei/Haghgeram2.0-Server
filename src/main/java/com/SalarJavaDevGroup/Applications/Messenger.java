package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.ManageDB.ManageMessages;
import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.Models.*;
import com.SalarJavaDevGroup.Models.Events.*;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.ConversationListResponse;
import com.SalarJavaDevGroup.Models.Responses.ConversationResponse;
import com.SalarJavaDevGroup.Models.Responses.GroupMessageResponse;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;

public class Messenger {
    private DataOutputStream dataOut;
    private static final Logger logger = LogManager.getLogger(Messenger.class);
    public Messenger(DataOutputStream dataOut) {
        this.dataOut = dataOut;
    }

    public void getConversationList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ConversationListResponse conversationListResponse = new ConversationListResponse(user);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(conversationListResponse)));
        logger.info(user.getUsername() + " requested all conversations");
        session.close();
    }

    public void getGroupMessageList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        GroupMessageResponse groupMessageResponse = new GroupMessageResponse(user);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(groupMessageResponse)));
        logger.info(user.getUsername() + " group message response");
        session.close();
    }

    public void getConversation(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        User user = manageUser.getUserById(request.user);
        ShowConversationEvent event = GsonHandler.getGson().fromJson(request.data, ShowConversationEvent.class);
        Conversation conversation = manageConversation.getConversation(event.getId());
        if (!conversation.getUsers().contains(user)) {
            logger.warn(user.getUsername() + " wants a conversation that's not init");
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.BadRequest, ""));
            return;
        }

        ConversationResponse conversationResponse = new ConversationResponse(conversation, user, session);
        StreamHandler.sendResponse(dataOut,
                new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(conversationResponse)));

        logger.info(user.getUsername() + " fetched conversation " + conversation.getId());
        session.close();
    }


    public void sendMessageToConversation(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        ManageMessages manageMessages = new ManageMessages(session);
        User user = manageUser.getUserById(request.user);
        SendMessageToConversationEvent event = GsonHandler.getGson().fromJson(request.data,
                SendMessageToConversationEvent.class);
        Conversation conversation = manageConversation.getConversation(event.getId());
        if(!conversation.getUsers().contains(user)) {
            logger.warn(user.getUsername() + " wants to send message to conversation that's not in it");
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.BadRequest, ""));
            return;
        }
        Message message = new Message(event, user, conversation);
        conversation.getMessages().add(message);
        logger.info("Message " + message.getId() + " added to conversation " + conversation.getId());
        manageMessages.Save(message);
        manageConversation.Save(conversation);

        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }

    public void sendGroupMessage(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        ManageMessages manageMessages = new ManageMessages(session);
        User user = manageUser.getUserById(request.user);
        SendGroupMessageEvent event = GsonHandler.getGson().fromJson(request.data, SendGroupMessageEvent.class);
        ArrayList<Conversation> conversations = new ArrayList<>();
        for (String usernames: event.getFollowings()) {
            User target = manageUser.getUser(usernames);
            if (target != null || user.getFollowing().contains(target)) {
                Conversation conversation = ConversationToUser(user, target, session);
                if (!conversations.contains(conversation))
                    conversations.add(conversation);
            }
        }
        for (UserList userList: user.getLists()) {
            boolean flag = false;
            for (String groupName: event.getGroups()) {
                if (groupName.equals(userList.getName())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                for (User target:userList.getUsers()) {
                    Conversation conversation = ConversationToUser(user, target, session);
                    if (!conversations.contains(conversation))
                        conversations.add(conversation);
                }
            }
        }
        for (int conv_id: event.getConversations()) {
            Conversation conversation = manageConversation.getConversation(conv_id);
            if (!conversations.contains(conversation))
                conversations.add(conversation);
        }
        for(Conversation conversation : conversations) {
            Message message = new Message(user, event.getContent(), event.getImage(), conversation);
            manageMessages.Save(message);
            conversation.getMessages().add(message);
        }

        for (Conversation conversation: conversations)
            manageConversation.Save(conversation);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
        session.close();
        logger.info(user.getUsername() + " sent a group message");
    }


    private Conversation ConversationToUser(User from, User to, Session session) {
        for (Conversation conversation : from.getConversations()) {
            if (!conversation.isGroup()) {
                for (User user : conversation.getUsers()) {
                    if (user == to) {
                        return conversation;
                    }
                }
            }
        }
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        Conversation conversation = new Conversation(from, to);
        manageConversation.Save(conversation);
        from.getConversations().add(conversation);
        to.getConversations().add(conversation);
        manageUser.Save(from);
        manageUser.Save(to);
        return conversation;
    }

    public void sendMessageToUser(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        SendMessageToUserEvent event = GsonHandler.getGson().fromJson(request.data, SendMessageToUserEvent.class);
        ManageUser manageUser = new ManageUser(session);
        ManageMessages manageMessages = new ManageMessages(session);
        ManageConversation manageConversation = new ManageConversation(session);
        User user = manageUser.getUserById(request.user);
        User target = manageUser.getUser(event.getTarget());
        if (target == null) {
            logger.warn(user.getUsername() + " wants to send message to user that doesn't exist");
            session.close();
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.BadRequest, ""));
            return;
        }
        if (!user.getFollowing().contains(user)) {
            logger.warn(user.getUsername() + " wants to send a message to user that doesn't follow");
            session.close();
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.BadRequest, ""));
            return;
        }
        Conversation conversation = ConversationToUser(user, target, session);
        Message message = new Message(user, event.getContent(), null, conversation);
        logger.info("Message " + message.getId() + " sent to conversation " + conversation.getId());
        manageMessages.Save(message);
        conversation.getMessages().add(message);
        manageConversation.Save(conversation);
        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }

    public void sendGroupTweetMessage(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, ""));
            return;
        }
        System.out.println(request.data);
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageConversation manageConversation = new ManageConversation(session);
        ManageMessages manageMessages = new ManageMessages(session);
        ManageTweet manageTweet = new ManageTweet(session);

        User user = manageUser.getUserById(request.user);
        SendMessageGroupTweetEvent event = GsonHandler.getGson().fromJson(request.data, SendMessageGroupTweetEvent.class);
        Tweet tweet = manageTweet.getTweet(event.getTweet_id());
        if (tweet == null) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "invalid tweet"));
            return;
        }
        ArrayList<Conversation> conversations = new ArrayList<>();
        for (String usernames: event.getFollowings()) {
            User target = manageUser.getUser(usernames);
            if (target != null || user.getFollowing().contains(target)) {
                Conversation conversation = ConversationToUser(user, target, session);
                if (!conversations.contains(conversation))
                    conversations.add(conversation);
            }
        }
        for (UserList userList: user.getLists()) {
            boolean flag = false;
            for (String groupName: event.getGroups()) {
                if (groupName.equals(userList.getName())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                for (User target:userList.getUsers()) {
                    Conversation conversation = ConversationToUser(user, target, session);
                    if (!conversations.contains(conversation))
                        conversations.add(conversation);
                }
            }
        }
        for (int conv_id: event.getConversations()) {
            Conversation conversation = manageConversation.getConversation(conv_id);
            if (!conversations.contains(conversation))
                conversations.add(conversation);
        }
        for(Conversation conversation : conversations) {
            Message message = new Message(tweet, user, conversation);
            manageMessages.Save(message);
            conversation.getMessages().add(message);
        }

        for (Conversation conversation: conversations)
            manageConversation.Save(conversation);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
        session.close();
        logger.info(user.getUsername() + " sent a group message");
    }

}
