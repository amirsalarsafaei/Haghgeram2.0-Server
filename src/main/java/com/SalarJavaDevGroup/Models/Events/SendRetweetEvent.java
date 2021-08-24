package com.SalarJavaDevGroup.Models.Events;

public class SendRetweetEvent {
    private String content;
    private byte[] image;
    private int retweeted;
    public SendRetweetEvent(String content, byte[] image, int retweeted) {
        this.content = content;
        this.image = image;
        this.retweeted = retweeted;
    }

    public int getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(int retweeted) {
        this.retweeted = retweeted;
    }

    public SendRetweetEvent(String content, int retweeted) {
        this.content = content;
        this.retweeted = retweeted;
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
