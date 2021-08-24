package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Networking.Request;
import com.SalarJavaDevGroup.Models.Networking.Response;
import com.SalarJavaDevGroup.Models.Networking.ResponseType;
import com.SalarJavaDevGroup.Models.Responses.TweetListResponse;
import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.StreamHandler.StreamHandler;
import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.util.Compare;
import com.SalarJavaDevGroup.util.GsonHandler;
import com.SalarJavaDevGroup.util.SessionUtil;
import com.SalarJavaDevGroup.util.properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Explore {
    private DataOutputStream dataOutputStream;
    private static final Logger logger = LogManager.getLogger(Explore.class);
    public Explore(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
    public void getDiscovery(Request request) {

        if (request.user == -1) {
            StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Denied, properties.loadDialog("user-invalid")));
        }
        Session session = SessionUtil.getSession();
        ManageTweet manageTweet = new ManageTweet(session);
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        List tweetList = manageTweet.getAllTweets();
        for (int i = 0; i < tweetList.size(); i++) {
            Tweet t = (Tweet) tweetList.get(i);
            User tweetUser = t.getFrom();
            if (user.getBlocked().contains(tweetUser) || user.getMuted().contains(tweetUser)
                || user.getBlockedBy().contains(tweetUser) ||
                    (tweetUser.getPrivateAccount() && !user.getFollowing().contains(tweetUser)) ||
                        !tweetUser.isActive()) {
                tweetList.remove(i);
                i--;
            }
        }
        ArrayList<Tweet> tweets = new ArrayList<>(tweetList);
        Collections.sort(tweets, Compare.compare_tweet_by_date);
        TweetListResponse tweetListResponse = new TweetListResponse(tweets);
        logger.info(user.getUsername() + " fetched discovery");
        StreamHandler.sendResponse(dataOutputStream, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(tweetListResponse)));
        session.close();
    }
}
