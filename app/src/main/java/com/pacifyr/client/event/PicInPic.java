package com.pacifyr.client.event;

/**
 * Created by Sachin Thampan on 25/9/19.
 */
public class PicInPic {

    boolean enabled = false;

    public PicInPic(boolean status) {
        this.enabled = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
