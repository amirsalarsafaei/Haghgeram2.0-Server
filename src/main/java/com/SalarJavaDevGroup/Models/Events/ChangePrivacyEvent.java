package com.SalarJavaDevGroup.Models.Events;

import com.SalarJavaDevGroup.Models.LastSeen;

public class ChangePrivacyEvent {
    private LastSeen lastSeen;
    private boolean deActive, Private;

    public ChangePrivacyEvent(LastSeen lastSeen, boolean deActive, boolean Private) {
        this.lastSeen = lastSeen;
        this.deActive = deActive;
        this.Private = Private;
    }

    public LastSeen getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LastSeen lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isDeActive() {
        return deActive;
    }

    public void setDeActive(boolean deActive) {
        this.deActive = deActive;
    }

    public boolean isPrivate() {
        return Private;
    }

    public void setPrivate(boolean aPrivate) {
        Private = aPrivate;
    }
}
