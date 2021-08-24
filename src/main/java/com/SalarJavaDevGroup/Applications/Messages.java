package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.ManageDB.ManageMessages;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Events.DeleteMessageEvent;
import com.SalarJavaDevGroup.Models.Events.EditMessageEvent;
import com.SalarJavaDevGroup.Models.Message;
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

public class Messages {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Messages.class);
    public Messages(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }


    public void editMessage(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("invalid-user")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ManageMessages manageMessages = new ManageMessages(session);
        EditMessageEvent event = GsonHandler.getGson().fromJson(request.data, EditMessageEvent.class);
        Message message = manageMessages.getMessage(event.getId());
        if (message == null) {
            logger.warn(user.getUsername() + " wants to edit message that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.InvalidData, properties.loadDialog("message-invalid")));
            return;
        }
        if (message.getUser() != user) {
            logger.warn(user.getUsername() + " wants to edit message that doesn't belong to him");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, properties.loadDialog("message-belong")));
            return;
        }
        message.setContent(event.getNewContent());
        logger.info(message.getId() + " edited");
        manageMessages.Save(message);
        logger.info("Message " + message.getId() + " saved");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void deleteMessage(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        DeleteMessageEvent event = GsonHandler.getGson().fromJson(request.data, DeleteMessageEvent.class);
        ManageMessages manageMessages = new ManageMessages(session);
        ManageConversation manageConversation = new ManageConversation(session);
        Message message = manageMessages.getMessage(event.getId());
        if (message == null) {
            logger.warn(user.getUsername() + " wants to delete message that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.InvalidData, properties.loadDialog("message-invalid")));
            return;
        }
        if (message.getUser() != user) {
            logger.warn(user.getUsername() + " wants to delete message that doesn't belong to him");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, properties.loadDialog("message-belong")));
            return;
        }
        Conversation conversation = message.getConversation();
        conversation.getMessages().remove(message);
        logger.info(message.getId() + " deleted");
        manageConversation.Save(conversation);
        logger.info("Conversation " + conversation.getId() + " saved");
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }
}
