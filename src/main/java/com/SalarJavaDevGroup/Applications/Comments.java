package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Events.SendCommentEvent;
import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;

public class Comments {
    private DataOutputStream dataOut;
    private static final Logger logger = LogManager.getLogger(Comments.class);
    public Comments(DataOutputStream dataOutputStream) {
        dataOut = dataOutputStream;
    }

    public void sendComment(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
            return;
        }
        Session session = SessionUtil.getSession();
        ManageTweet manageTweet = new ManageTweet(session);
        ManageUser manageUser = new ManageUser(session);
        SendCommentEvent sendCommentEvent = GsonHandler.getGson().fromJson(request.data, SendCommentEvent.class);
        User user = manageUser.getUserById(request.user);
        Tweet tweet = manageTweet.getTweet(sendCommentEvent.getTweet_id());
        Tweet comment = new Tweet(sendCommentEvent, user);
        logger.info("Comment " + comment.getId() + " Created");
        tweet.getComments().add(comment);

        manageTweet.Save(comment);
        manageTweet.Save(tweet);
        logger.info("Comment " + comment.getId() + " saved");
        logger.info("Tweet " + tweet.getId() + " saved");
        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }
}
