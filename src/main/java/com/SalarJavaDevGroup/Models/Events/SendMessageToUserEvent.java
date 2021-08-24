package com.SalarJavaDevGroup.Models.Events;

public class SendMessageToUserEvent {
    private String content, target;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
