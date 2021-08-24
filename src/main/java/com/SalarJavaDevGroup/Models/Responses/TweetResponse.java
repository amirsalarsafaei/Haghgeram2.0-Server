package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.util.Convertor;

import java.util.ArrayList;

public class TweetResponse {
    private int id;
    private SmallUserResponse smallUserResponse;
    private String content;
    private ArrayList<Integer> likes;
    private int comments;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private byte[] image;

    private TweetResponse retweeted;
    public int getId() {
        return id;
    }

    public TweetResponse getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(TweetResponse retweeted) {
        this.retweeted = retweeted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SmallUserResponse getSmallUserResponse() {
        return smallUserResponse;
    }

    public void setSmallUserResponse(SmallUserResponse smallUserResponse) {
        this.smallUserResponse = smallUserResponse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Integer> likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public TweetResponse(Tweet tweet) {
        id = tweet.getId();
        smallUserResponse = new SmallUserResponse(tweet.getFrom());
        content = tweet.getContent();
        likes = Convertor.UserSetToIdArrayList(tweet.getLikes());
        comments = tweet.getComments().size();
        image = tweet.getImage();
        if (tweet.getRetweeted() != null)
            retweeted = new TweetResponse(tweet.getRetweeted());
    }
}
