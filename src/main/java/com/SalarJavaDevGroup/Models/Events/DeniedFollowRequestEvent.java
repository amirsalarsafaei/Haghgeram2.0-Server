package com.SalarJavaDevGroup.Models.Events;

public class DeniedFollowRequestEvent {
    private String username;
    private boolean mute;

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public DeniedFollowRequestEvent(String username, boolean mute) {
        this.username = username;
        this.mute = mute;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
