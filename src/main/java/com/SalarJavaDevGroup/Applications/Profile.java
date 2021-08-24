package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.BigUserResponse;
import com.SalarJavaDevGroup.Models.Responses.FollowingsResponse;
import com.SalarJavaDevGroup.Models.Responses.SelfUserResponse;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class Profile {
    private final DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Profile.class);
    public Profile(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
    public void getProfile(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User target = manageUser.getUser(request.data);
        User user = manageUser.getUserById(request.user);
        if (target == null || target.getBlocked().contains(user) || !target.isActive()) {
            logger.warn("User " + user.getUsername() + " wants a user that can't see");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        BigUserResponse bigUserResponse = new BigUserResponse(user, target);
        logger.info("User " + user.getUsername() + " requested profile of " + target.getUsername());
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(bigUserResponse)));
        session.close();
    }

    public void getFollowings(Request request)  {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(new FollowingsResponse(user.getFollowing()))));
        logger.info("User " + user.getUsername() + " requested followings");
        session.close();
    }

    public void getSelfProfile(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(new SelfUserResponse(user))));
        logger.info("User " + user.getUsername() + " requested self profile");
        session.close();
    }
}
