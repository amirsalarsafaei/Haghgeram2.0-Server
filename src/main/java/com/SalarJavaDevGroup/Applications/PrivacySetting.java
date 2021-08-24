package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Events.ChangePrivacyEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.EditPrivacyResponse;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class PrivacySetting {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(PrivacySetting.class);
    public PrivacySetting(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void getPrivacy(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream , new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        EditPrivacyResponse response = new EditPrivacyResponse(user);
        logger.info("User " + user.getUsername() + " requested privacy setting");
        session.close();
        StreamHandler.sendResponse(dataOutputStream ,
                new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(response)));
    }

    public void changePrivacy(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream , new Response(ResponseType.Denied, ""));
            return;
        }
        ChangePrivacyEvent event = GsonHandler.getGson().fromJson(request.data, ChangePrivacyEvent.class);
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        user.setPrivateAccount(event.isPrivate());
        user.setActive(!event.isDeActive());
        user.setLastSeenSetting(event.getLastSeen());
        manageUser.Save(user);
        session.close();
        logger.info("User " + user.getUsername() + " changed privacy setting");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }
}
