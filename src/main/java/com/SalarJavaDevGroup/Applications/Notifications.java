package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Events.AcceptFollowRequestEvent;
import com.SalarJavaDevGroup.Models.Events.DeniedFollowRequestEvent;
import com.SalarJavaDevGroup.Models.Responses.NotificationsResponse;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class Notifications {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Notifications.class);
    public Notifications(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void getNotifications(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        NotificationsResponse response = new NotificationsResponse(user);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(response)));
        logger.info("User " + user.getUsername() + " requested notifications");
    }

    public void acceptFollowRequest(Request request) {

        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        AcceptFollowRequestEvent event = GsonHandler.getGson().fromJson(request.data, AcceptFollowRequestEvent.class);
        User target = manageUser.getUser(event.getUsername());
        if (!user.getFollowRequests().contains(target)) {
            logger.warn(user.getUsername() + " wants to accept request that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, ""));
            return;
        }
        target.getAccepted().add(user);
        user.getFollowRequests().remove(target);
        target.getFollowing().add(user);
        user.getFollowers().add(target);
        target.getPending().remove(user);
        logger.info(user.getId() + " accepted " + target.getId());
        manageUser.Save(user);
        manageUser.Save(target);
        session.close();

        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void denyFollowRequest(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        DeniedFollowRequestEvent event = GsonHandler.getGson().fromJson(request.data, DeniedFollowRequestEvent.class);
        User target = manageUser.getUser(event.getUsername());
        if (!user.getFollowRequests().contains(target)){
            logger.warn(user.getUsername() + " wants to deny request that doesn't exist");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, ""));
            return;
        }
        user.getFollowRequests().remove(target);
        target.getDenied().add(user);
        target.getPending().remove(user);
        if (!event.isMute()) {
            target.getEvents().add(user.getUsername() + " denied your request");
        }
        manageUser.Save(user);
        manageUser.Save(target);
        logger.info(user.getId() + " denied " + target.getId());
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }
}
