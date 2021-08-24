package com.SalarJavaDevGroup.Models.Events;

public class SendTweetEvent {
    private String content;
    private byte[] image;

    public SendTweetEvent(String content, byte[] image) {
        this.content = content;
        this.image = image;
    }

    public SendTweetEvent(String content) {
        this.content = content;
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
