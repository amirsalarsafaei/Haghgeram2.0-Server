package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Events.FollowEvent;
import com.SalarJavaDevGroup.Models.Events.UnBlockButtonPressedEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.BlackListResponse;
import com.SalarJavaDevGroup.Models.Responses.SmallUserResponse;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class UserRelations {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(UserRelations.class);
    public UserRelations(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
    public void mute(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        SmallUserResponse smallUserResponse = GsonHandler.getGson().fromJson(request.data, SmallUserResponse.class);
        User target = manageUser.getUser(smallUserResponse.getUsername());
        if (target == null) {

            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "invalid target"));
            session.close();
            return;
        }
        if (user == target) {

            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "user is target"));
            session.close();
            return;
        }
        user.getMuted().add(target);
        manageUser.Save(user);
        manageUser.Save(target);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
        logger.info(user.getUsername() + " muted " + target.getUsername() );
    }

    public void block(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        SmallUserResponse smallUserResponse = GsonHandler.getGson().fromJson(request.data, SmallUserResponse.class);
        User target = manageUser.getUser(smallUserResponse.getUsername());
        if (target == null) {

            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "invalid target"));
            session.close();
            return;
        }
        if (user == target) {

            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "user is target"));
            session.close();
            return;
        }
        if (!user.getBlocked().contains(target)) {
            disconnect(user, target);
            disconnect(target, user);
            user.getBlocked().add(target);
            target.getBlockedBy().add(user);
        }
        else {
            user.getBlocked().remove(target);
            target.getBlockedBy().remove(user);
        }

        manageUser.Save(user);
        manageUser.Save(target);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
        logger.info(user.getUsername() + " blocked " + target.getUsername());
    }

    public void follow(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        FollowEvent followEvent = GsonHandler.getGson().fromJson(request.data, FollowEvent.class);
        User target = manageUser.getUser(followEvent.getUsername());
        if (target == null) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "invalid target"));
            session.close();
            return;
        }
        if (user == target) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "user is target"));
            session.close();
            return;
        }
        if (user.getBlockedBy().contains(target)) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "target is not blocked"));
            session.close();
            return;
        }
        if (user.getFollowing().contains(target)) {
            user.getFollowing().remove(target);
            target.getFollowers().remove(user);
        }
        else if (target.getFollowRequests().contains(user)) {
            target.getFollowRequests().remove(user);
            user.getPending().remove(target);
        }
        else {
            if (target.getPrivateAccount()) {
                target.getFollowRequests().add(user);
                user.getPending().add(target);
            }
            else {
                target.getFollowers().add(user);
                user.getFollowing().add(target);
                target.getEvents().add(user.getUsername() +" started following you");
            }
        }

        manageUser.Save(target);
        manageUser.Save(user);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void unblock(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        UnBlockButtonPressedEvent event = GsonHandler.getGson().fromJson(request.data, UnBlockButtonPressedEvent.class);
        User target = manageUser.getUser(event.getUsername());
        if (target == null || !user.getBlocked().contains(target)) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest, "invalid request"));
            return;
        }
        user.getBlocked().remove(target);
        target.getBlockedBy().remove(user);
        manageUser.Save(user);
        manageUser.Save(target);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));

    }

    public void blackList(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        BlackListResponse blackListResponse = new BlackListResponse(user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(blackListResponse)));
    }

    private void disconnect(User user, User from) {
        user.getFollowing().remove(from);
        user.getFollowers().remove(from);
        user.getPending().remove(from);
    }

}
