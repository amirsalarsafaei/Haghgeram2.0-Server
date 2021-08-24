package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Tweet;

import java.util.ArrayList;

public class TweetListResponse {
    ArrayList<TweetResponse> tweets;
    public TweetListResponse(ArrayList<Tweet> tweets) {
        this.tweets = new ArrayList<>();
        for (Tweet tweet : tweets)
            this.tweets.add(new TweetResponse(tweet));
    }
}
