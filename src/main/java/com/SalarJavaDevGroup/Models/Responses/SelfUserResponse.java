package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.util.Compare;

import java.util.ArrayList;
import java.util.Collections;

public class SelfUserResponse {
    private TweetListResponse tweets;
    private BigUserResponse user;

    public TweetListResponse getTweets() {
        return tweets;
    }

    public void setTweets(TweetListResponse tweets) {
        this.tweets = tweets;
    }

    public BigUserResponse getUser() {
        return user;
    }

    public void setUser(BigUserResponse user) {
        this.user = user;
    }

    public SelfUserResponse(User user) {
        this.user = new BigUserResponse(user, user);
        ArrayList<Tweet> tweets = new ArrayList<>(user.getTweets());
        Collections.sort(tweets, Compare.compare_tweet_by_date.reversed());
        this.tweets = new TweetListResponse(tweets);
    }
}
