package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.MiddleWare.TokenHandling;
import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Events.AuthEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.EditPrivacyResponse;
import com.SalarJavaDevGroup.Models.Responses.UserDetails;
import com.SalarJavaDevGroup.Models.Responses.UserInformationResponse;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.Validate;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class Authenticate {
    private static final Logger logger = LogManager.getLogger(Authenticate.class);
    private DataOutputStream dataOutputStream;
    public Authenticate(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
    public void login(Request request) {
        Session session = SessionUtil.getSession();
        AuthEvent authEvent = GsonHandler.getGson().fromJson(request.data, AuthEvent.class);
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUser(authEvent.username);
        if (user == null || !user.getPassword().equals(authEvent.password) && !user.isDeleted()) {
            logger.error("invalid username and password sent");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-pass-wrong")));
            return;
        }
        String token = TokenHandling.generateSafeToken(user, session);
        UserDetails userDetails = new UserDetails(user.getUsername(), user.getId(), token, new EditPrivacyResponse(user),
                new UserInformationResponse(user));
        logger.info("User with username " + user.getUsername() + " logged in");
        StreamHandler.sendResponse(dataOutputStream,
                new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(userDetails)));
        session.close();
    }


    public void signUp(Request request) {
        Session session = SessionUtil.getSession();
        AuthEvent authEvent = GsonHandler.getGson().fromJson(request.data, AuthEvent.class);
        User user = new User(authEvent);
        String error = "";
        ManageUser manageUser = new ManageUser(session);
        if (user.getUsername() != null && user.getUsername().length() > 0 && manageUser.usernameExists(user.getUsername()))
            error += properties.loadDialog("username-exist");
        if (user.getEmail() != null && user.getEmail().length() > 0 && manageUser.emailExists(user.getEmail()))
            error += properties.loadDialog("email-exist");
        if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0 &&
                manageUser.phoneExists(user.getPhoneNumber()))
            error += properties.loadDialog("phone-exist");
        if (user.getUsername() == null || user.getUsername().length() == 0)
            error += properties.loadDialog("username-empty");
        if (user.getEmail() == null || user.getEmail().length() == 0)
            error += properties.loadDialog("email-empty");
        if (user.getPassword() == null || user.getPassword().length() == 0)
            error += properties.loadDialog("pass-empty");
        if (user.getEmail() != null && user.getEmail().length() > 0 && !Validate.validEmail(user.getEmail()))
            error += properties.loadDialog("email-invalid");
        if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0 && !Validate.validPhone(user.getPhoneNumber())) {
            error += properties.loadDialog("phone-invalid") ;
        }
        if (error.length() > 0) {
            logger.error("Couldn't register because " + error);
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, error));
            return;
        }

        logger.info("User made with" + user.getUsername() );
        manageUser.Save(user);
        Conversation conversation = new Conversation(user);
        user.getConversations().add(conversation);
        ManageConversation manageConversation = new ManageConversation(session);
        manageConversation.Save(conversation);
        manageUser.Save(user);
        logger.info(user.getUsername() + " user saved");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Created, ""));
        session.close();
    }
}
