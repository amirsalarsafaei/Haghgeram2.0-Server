package com.SalarJavaDevGroup.Models.Responses;


import com.SalarJavaDevGroup.Models.Message;
import com.SalarJavaDevGroup.Models.MessageStatus;

import java.time.LocalDateTime;

public class MessageResponse {
    private SmallUserResponse user;
    private String content;
    private byte[] image;
    private LocalDateTime timeSent;
    private TweetResponse tweet;
    private int id;
    private MessageStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MessageResponse(Message message) {
        user = new SmallUserResponse(message.getUser());
        content = message.getContent();
        if (content == null)
            content = "";
        image = message.getImage();
        timeSent = message.getTimeSent();
        if (message.getTweet() != null)
            tweet = new TweetResponse(message.getTweet());
        this.id = message.getId();
        this.status = message.getMessageStatus();
    }
    public TweetResponse getTweet() {
        return tweet;
    }

    public void setTweet(TweetResponse tweet) {
        this.tweet = tweet;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public SmallUserResponse getUser() {
        return user;
    }

    public void setUser(SmallUserResponse user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
