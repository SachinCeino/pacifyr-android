package com.pacifyr.client.event;

/**
 * Created by developer on 1/3/2018.
 */

public class ResourceTabClickedEvent {
    private boolean isResourceTabClickedEvent;

    public ResourceTabClickedEvent(boolean isResourceTabClickedEvent) {
        this.isResourceTabClickedEvent = isResourceTabClickedEvent;
    }

    public boolean isConnect() {
        return isResourceTabClickedEvent;
    }

    public void setConnect(boolean isResourceTabClickedEvent) {
        isResourceTabClickedEvent = isResourceTabClickedEvent;
    }
}
