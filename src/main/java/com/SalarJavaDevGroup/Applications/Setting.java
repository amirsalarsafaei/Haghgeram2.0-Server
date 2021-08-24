package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageTokens;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Events.EditProfileEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.RequestType;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.FollowRequestsStatusResponse;
import com.SalarJavaDevGroup.Models.Responses.UserInformationResponse;
import com.SalarJavaDevGroup.Models.Token;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Setting {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Setting.class);
    public Setting(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void deleteAccount(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageTokens manageTokens = new ManageTokens(session);
        User user = manageUser.getUserById(request.user);
        user.setDeleted(true);
        ArrayList<Token> list = new ArrayList<>(manageTokens.getAllTokens());
        for (int i = 0; i < list.size(); i++) {
            Token token = list.get(i);
            if (token.getId() == request.user) {
                SessionUtil.deleteObj(token, session);
                list.remove(i);
                i--;
            }
        }
        logger.info("User " + user.getUsername() + " deleted account" );
        manageUser.Save(user);
        session.close();
    }

    public void editProfile(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        EditProfileEvent event = GsonHandler.getGson().fromJson(request.data, EditProfileEvent.class);
        String error = "";
        if (event.getEmail() != null && event.getEmail().length() > 0 && !event.getEmail().equals(user.getEmail()) && manageUser.emailExists(event.getEmail()))
            error += "Email Already Exists!-";
        if (event.getPhoneNumber() != null && event.getPhoneNumber().length() > 0 &&
                (user.getPhoneNumber() != null ||!event.getPhoneNumber().equals(user.getPhoneNumber()) )&&
                    manageUser.phoneExists(user.getPhoneNumber()))
            error += "Phone Already Exists!-";
        if (event.getEmail() == null || event.getEmail().length() == 0)
            error += "Email can't be empty!-";
        if (event.getPassword() == null || event.getPassword().length() == 0)
            error += "Password can't be empty!-";
        if (event.getEmail() != null && event.getEmail().length() > 0 && !Validate.validEmail(event.getEmail()))
            error += "Invalid Email (Format should be example@somedomain.xyz)-";
        if (event.getPhoneNumber() != null && event.getPhoneNumber().length() > 0 && !Validate.validPhone(event.getPhoneNumber())) {
            error += "Invalid Phone Number (The Phone should start with 0 and have 11 digits)-" ;
        }
        if (error.length() > 0) {
            logger.info("User wanted to edit profile but error hit Error : " + error);
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, error));
            return;
        }

        user.setName(event.getName());
        user.setFamilyName(event.getFamilyName());
        user.setEmail(event.getEmail());
        user.setPhoneNumber(event.getPhoneNumber());
        user.setPassword(event.getPassword());
        user.setBio(event.getBio());
        user.setBirthDate(event.getBirthDate());
        if (event.getImage() != null)
            user.setImage(event.getImage());
        logger.info("User " + user.getUsername() + " edited");
        manageUser.Save(user);
        session.close();
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, ""));
    }

    public void getEditProfileInfo(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        UserInformationResponse response = new UserInformationResponse(user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(response)));
        logger.info("User " + user.getUsername() + " requested user information for edit profile");
        session.close();
    }

    public void getFollowRequests(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, "invalid user"));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        FollowRequestsStatusResponse response = new FollowRequestsStatusResponse(user);
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(response)));
        session.close();
        logger.info("User " + user.getUsername() + " request follow requests");
    }
}
