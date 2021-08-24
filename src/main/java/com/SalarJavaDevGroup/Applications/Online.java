package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageMessages;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Message;
import com.SalarJavaDevGroup.Models.MessageStatus;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.time.LocalDateTime;

public class Online {

    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Online.class);

    public Online(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }


    public void stillOnline(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageMessages manageMessages = new ManageMessages(session);
        User user = manageUser.getUserById(request.user);
        user.setLastOnline(LocalDateTime.now());
        for (Conversation conversation: user.getConversations()) {
            for (Message message: conversation.getMessages()) {
                if (message.getTimeSent().isAfter(LocalDateTime.now()))
                    continue;
                if (message.getUserReads().contains(user))
                    continue;
                if (message.getUser()==user)
                    continue;
                if (message.getMessageStatus().equals(MessageStatus.UnDelivered)) {
                    message.setMessageStatus(MessageStatus.UnSeen);
                    logger.info("Message " + message.getId() + " delivered" );
                }
                manageMessages.Save(message);
            }
        }
        logger.info("User " + user.getUsername() + " is online");
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }
}
