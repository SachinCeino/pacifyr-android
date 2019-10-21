package com.pacifyr.omhealthcare.model;

/**
 * Created by Sachin Thampan on 19/10/19.
 */
public class MarkEvent {

    String eventId, code, type;

    public MarkEvent(String id, String code, String type) {
        this.eventId = id;
        this.code = code;
        this.type = type;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
