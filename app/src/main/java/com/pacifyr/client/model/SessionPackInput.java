package com.pacifyr.client.model;

public class SessionPackInput {
    String userId;
    String sessionPackId;

    public SessionPackInput(String userId, String sessionPackId) {
        this.userId = userId;
        this.sessionPackId = sessionPackId;
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

}
