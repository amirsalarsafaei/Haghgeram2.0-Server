package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.*;
import com.SalarJavaDevGroup.Models.Events.HyperLinkEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.ConversationResponse;
import com.SalarJavaDevGroup.Models.Responses.HyperLinkResponse;
import com.SalarJavaDevGroup.Models.Responses.TweetResponse;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.swing.event.HyperlinkEvent;
import java.io.DataOutputStream;

public class HyperLink {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(HyperLink.class);
    public HyperLink(DataOutputStream dataOutputStream) {
        this.dataOutputStream= dataOutputStream;
    }

    public void hyperLink(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, ""));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageTweet manageTweet = new ManageTweet(session);
        User user = manageUser.getUserById(request.user);
        HyperLinkEvent event = GsonHandler.getGson().fromJson(request.data, HyperLinkEvent.class);
        String hyperLinkStr = event.getHyperlink();
        if (isHyperLinkType( hyperLinkStr,"Tweet")) {
            int id = getHyperLinkId(hyperLinkStr,"Tweet");
            Tweet tweet = manageTweet.getTweet(id);
            if (tweet == null) {
                logger.warn(user.getUsername() + " tried to hyperlink a tweet that doesn't exist");
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, properties.loadDialog("tweet-exist")));
                session.close();
                return;
            }
            User tweetUser = tweet.getFrom();
            if (user.getBlocked().contains(tweetUser) || user.getMuted().contains(tweetUser)
                    || user.getBlockedBy().contains(tweetUser) ||
                        (tweetUser.getPrivateAccount() && !user.getFollowing().contains(tweetUser)) ||
                            !tweetUser.isActive() || tweet.getReportedBy().size() >= 10) {
                logger.warn(user.getUsername() + " tried to hyperlink a tweet that cant view it");
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, properties.loadDialog("tweet-available")));
                session.close();
                return;
            }
            logger.info(user.getUsername() + " fetched tweet" + tweet.getId() +" from hyperlink");
            StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.Accepted,
                                GsonHandler.getGson().toJson(new HyperLinkResponse(new TweetResponse(tweet)))));
        }
        else if (isHyperLinkType( hyperLinkStr,"Conversation")) {
            int id = getHyperLinkId(hyperLinkStr,"Conversation");
            if (event.getHyperLinkText() == HyperLinkText.Tweet) {
                logger.warn(user.getUsername() + " wants conversation but in tweets");
                StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest,
                        properties.loadDialog("hyper-invalid")));
                session.close();
                return;
            }
            ManageConversation manageConversation = new ManageConversation(session);
            Conversation conversation = manageConversation.getConversation(id);
            if (conversation == null) {
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, properties.loadDialog("conv-exist")));
                logger.warn(user.getUsername() + " wants conversation that doesn't exist");
                session.close();
                return;
            }
            if (!conversation.getUsers().contains(user)) {
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, "The Conversation is not available"));
                logger.warn(user.getUsername() + " wants conversation that isn't in it");
                session.close();
                return;
            }
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(new HyperLinkResponse(new ConversationResponse(conversation, user, session)))));
        }
        else if (isHyperLinkType(hyperLinkStr, "Bot")) {
            int id = getHyperLinkId(hyperLinkStr,"Bot");
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.BadRequest,
                    properties.loadDialog("bot-ready")));
            logger.warn(user.getUsername() + " wants bot but still not developed");
        }
        else if (isHyperLinkType(hyperLinkStr, "invite")) {
            int id = getHyperLinkId(hyperLinkStr,"Conversation");
            ManageConversation manageConversation = new ManageConversation(session);
            Conversation conversation = manageConversation.getConversation(id);
            if (conversation == null) {
                logger.warn(user.getUsername() + " wants a conversation that doesn't exist");
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, properties.loadDialog("group-exist")));
                session.close();
                return;
            }
            if (!conversation.isGroup()) {
                logger.warn(user.getUsername() + " wants a conversation that isn't group");
                StreamHandler.sendResponse(dataOutputStream,
                        new Response(ResponseType.InvalidData, properties.loadDialog("group-exist")));
                session.close();
                return;
            }
            conversation.getUsers().add(user);
            logger.info(user.getUsername() + " added to group " + conversation.getId());
            user.getConversations().add(conversation);
            manageConversation.Save(conversation);
            logger.info("Conversation " + conversation.getId() + " saved");
            manageUser.Save(user);
            logger.info("User " + user.getId() + " saved");
            StreamHandler.sendResponse(dataOutputStream,
                    new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(new HyperLinkResponse(HyperLinkType.GroupInvite))));
        }
        else {
            logger.warn(user.getUsername() + " sent an invalid hyperlink");
            StreamHandler.sendResponse(dataOutputStream,
                    new Response(ResponseType.InvalidData, properties.loadDialog("hyper-invalid")));
        }
        session.close();
    }

    private int getHyperLinkId(String hyperLinkStr, String type) {
        return Integer.parseInt(hyperLinkStr.substring(type.length()));
    }

    private boolean isHyperLinkType(String hyperLinkStr,String type) {
        return hyperLinkStr.startsWith(type) && isNumber(hyperLinkStr.substring(type.length()));
    }


    private boolean isNumber(String num) {
        for (char ch: num.toCharArray()) {
            if (ch < '0' || ch > '9')
                return false;
        }
        return true;
    }
}
