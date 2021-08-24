package com.SalarJavaDevGroup.Applications;

import com.SalarJavaDevGroup.Models.Events.*;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.DataOutputStream;
import java.util.*;

public class Tweets {
    private DataOutputStream dataOut;
    private static final Logger logger = LogManager.getLogger(Tweets.class);
    public Tweets(DataOutputStream dataOutputStream) {
        dataOut = dataOutputStream;
    }

    public void getTimeLine(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        ArrayList<Tweet> tweetList = new ArrayList<>();
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        for (User user1: user.getFollowing())
            if (user1.isActive() && !user.getMuted().contains(user1))
                tweetList.addAll(user1.getTweets());
        tweetList.addAll(user.getTweets());
        Collections.sort(tweetList, Compare.compare_tweet_by_date.reversed());
        TweetListResponse tweetListResponse = new TweetListResponse(tweetList);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(tweetListResponse)));
        session.close();
        logger.info("User " + user.getUsername() + " requested timeline");
    }

    public void sendTweet(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        SendTweetEvent sendTweetEvent = GsonHandler.getGson().fromJson(request.data, SendTweetEvent.class);
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageTweet manageTweet = new ManageTweet(session);
        User user = manageUser.getUserById(request.user);
        Tweet tweet = new Tweet(sendTweetEvent, user);
        user.getTweets().add(tweet);
        manageTweet.Save(tweet);
        manageUser.Save(user);
        session.close();
        logger.info("User " + user.getUsername() + " sent a tweet");
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }

    public void sendRetweet(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        SendRetweetEvent sendRetweetEvent = GsonHandler.getGson().fromJson(request.data, SendRetweetEvent.class);
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        ManageTweet manageTweet = new ManageTweet(session);
        User user = manageUser.getUserById(request.user);
        Tweet tweet = new Tweet(sendRetweetEvent, user);
        user.getTweets().add(tweet);
        logger.info("User " + user.getUsername() + " sent a retweet from tweet " + sendRetweetEvent.getRetweeted());
        manageTweet.Save(tweet);
        manageUser.Save(user);
        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }

    public void getComments(Request request) {

        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        Session session = SessionUtil.getSession();
        int id = Integer.valueOf(request.data);
        ManageTweet manageTweet = new ManageTweet(session);
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        Tweet tweet = manageTweet.getTweet(id);
        if (tweet == null){
            logger.warn("User " + user.getUsername() + " wanted to get comments for tweet that doesn't exist");
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "tweet is null"));
            return ;
        }
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.addAll(tweet.getComments());
        Collections.sort(tweets, Compare.compare_tweet_by_date);
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tmp = tweets.get(i);
            User target = tmp.getFrom();
            if (user.getBlocked().contains(tmp) || user.getBlockedBy().contains(tmp) || user.getMuted().contains(target) ||
                    (target.getPrivateAccount() && !user.getFollowing().contains(target)) || !target.isActive()) {
                tweets.remove(i);
                i--;
            }
        }
        logger.info("User " + user.getUsername() + " request tweet " + tweet.getId() + " comments");
        TweetListResponse tweetListResponse = new TweetListResponse(tweets);
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(tweetListResponse)));
        session.close();
    }

    public void likeTweet(Request request) {

        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        Session session = SessionUtil.getSession();
        LikeButtonEvent likeButtonEvent = GsonHandler.getGson().fromJson(request.data, LikeButtonEvent.class);
        int id = likeButtonEvent.getId();
        ManageTweet manageTweet = new ManageTweet(session);
        ManageUser manageUser = new ManageUser(session);
        Tweet tweet = manageTweet.getTweet(id);
        User user = manageUser.getUserById(request.user);
        if (tweet == null){
            logger.warn("User " + user.getUsername() + " wanted to toggle like for a tweet that doesn't exist");
            session.close();
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "tweet is null"));
            return ;
        }

        if (tweet.getLikes().contains(user)) {
            tweet.getLikes().remove(user);
            user.getLikes().remove(tweet);
            logger.info("User " + user.getUsername() + " Unliked tweet " + tweet.getId());
        }
        else {
            tweet.getLikes().add(user);
            user.getLikes().add(tweet);
            logger.info("User " + user.getUsername() + " liked tweet " + tweet.getId());
        }

        manageTweet.Save(tweet);
        manageUser.Save(user);
        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }

    public void reportTweet(Request request) {
        if (request.user == -1) {
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "user is null"));
            return ;
        }
        Session session = SessionUtil.getSession();
        ManageUser manageUser = new ManageUser(session);
        User user = manageUser.getUserById(request.user);
        ManageTweet manageTweet = new ManageTweet(session);
        ReportTweetEvent event = GsonHandler.getGson().fromJson(request.data, ReportTweetEvent.class);
        Tweet tweet = manageTweet.getTweet(event.getId());
        if (tweet == null) {
            logger.warn("User " + user.getUsername() + " tried to report a tweet that doesn't exist");
            StreamHandler.sendResponse(dataOut, new Response(ResponseType.Denied, "tweet is null"));
            session.close();
            return;
        }
        tweet.getReportedBy().add(user);
        if (tweet.getReportedBy().size() == 10) {
            tweet.getFrom().getTweets().remove(tweet);
            manageUser.Save(tweet.getFrom());
            tweet.setContent("This Tweet is Reported");
        }
        logger.info("User " + user.getUsername() + " reported " + tweet.getId());

        manageTweet.Save(tweet);
        session.close();
        StreamHandler.sendResponse(dataOut, new Response(ResponseType.Accepted, ""));
    }


}
