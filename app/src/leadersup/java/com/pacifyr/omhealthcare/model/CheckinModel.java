package com.pacifyr.omhealthcare.model;

/**
 * Created by Sachin Thampan on 18/10/19.
 */
public class CheckinModel {

    public static final String RESUME_ZONE = "resumeZoneCompleted";
    public static final String ELEVATED_ZONE = "elevatedIntroZoneCompleted";
    public static final String FRESH_SESH = "freshSeshCompleted";
    public static final String MOCK_INTERVIEW = "mockInterviewCompleted";
    public static final String DRESS_FOR_SUCCESS = "dressForSuccessCompleted";
    private final String type;
    private final String eventId;
    boolean expanded = false;
    String title;
    boolean complete = false;

    public CheckinModel(String id, boolean expanded, String title, String type, boolean complete) {
        this.eventId = id;
        this.expanded = expanded;
        this.title = title;
        this.complete = complete;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
