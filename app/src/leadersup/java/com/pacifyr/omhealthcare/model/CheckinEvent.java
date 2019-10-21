package com.pacifyr.omhealthcare.model;

/**
 * Created by Sachin Thampan on 19/10/19.
 */
public class CheckinEvent {

    private String eventId;

    public CheckinEvent(String id) {
        this.eventId = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
