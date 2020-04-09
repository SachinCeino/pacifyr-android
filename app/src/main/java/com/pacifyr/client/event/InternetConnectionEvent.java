package com.pacifyr.client.event;

public class InternetConnectionEvent {

    boolean isConnected;

    public InternetConnectionEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
