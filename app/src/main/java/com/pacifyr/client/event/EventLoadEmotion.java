package com.pacifyr.client.event;

/**
 * Created by Sachin Thampan on 27/2/20.
 */
public class EventLoadEmotion {

    private final boolean isLoaded;

    public EventLoadEmotion(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
