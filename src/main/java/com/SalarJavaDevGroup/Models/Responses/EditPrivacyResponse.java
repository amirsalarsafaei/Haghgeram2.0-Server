package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.LastSeen;
import com.SalarJavaDevGroup.Models.User;

public class EditPrivacyResponse {

    public EditPrivacyResponse(User user) {
        lastSeen = user.getLastSeenSetting();
        DeActive = !user.isActive();
        Private = user.getPrivateAccount();
    }

    private LastSeen lastSeen;
    private boolean DeActive, Private;

    public LastSeen getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LastSeen lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isDeActive() {
        return DeActive;
    }

    public void setDeActive(boolean deActive) {
        DeActive = deActive;
    }

    public boolean isPrivate() {
        return Private;
    }

    public void setPrivate(boolean Private) {
        this.Private = Private;
    }
}
