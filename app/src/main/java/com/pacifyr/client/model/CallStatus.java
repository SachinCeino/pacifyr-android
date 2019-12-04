package com.pacifyr.client.model;

/**
 * Created by Sachin Thampan on 28/10/19.
 */
public class CallStatus {

    boolean active = false;

    public CallStatus(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
