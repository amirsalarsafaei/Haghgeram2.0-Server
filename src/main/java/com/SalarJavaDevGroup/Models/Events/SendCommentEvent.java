package com.SalarJavaDevGroup.Models.Events;

public class SendCommentEvent {
    private String content;
    private byte[] image;
    private int tweet_id;
    public SendCommentEvent(String content, byte[] image, int tweet_id) {
        this.content = content;
        this.image = image;
        this.tweet_id = tweet_id;
    }

    public int getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(int tweet_id) {
        this.tweet_id = tweet_id;
    }

    public SendCommentEvent(String content, int tweet_id) {
        this.content = content;
        this.tweet_id = tweet_id;
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
