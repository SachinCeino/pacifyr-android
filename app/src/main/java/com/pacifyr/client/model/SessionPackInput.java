package com.pacifyr.client.model;

public class SessionPackInput {
    String userId;
    String sessionPackId;

    boolean isSubscribed;

    public SessionPackInput(String userId, String sessionPackId, boolean isSubscribed) {
        this.userId = userId;
        this.sessionPackId = sessionPackId;
        this.isSubscribed = isSubscribed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionPackId() {
        return sessionPackId;
    }

    public void setSessionPackId(String sessionPackId) {
        this.sessionPackId = sessionPackId;
    }


    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
