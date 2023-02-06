package com.pacifyr.client.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.pacifyr.client.R;

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

    public Drawable getImage(Context context) {
        if (this.type == RESUME_ZONE) {
            return context.getResources().getDrawable(R.drawable.image_resume);
        } else if (this.type == MOCK_INTERVIEW) {
            return context.getResources().getDrawable(R.drawable.image_mock);
        } else if (this.type == ELEVATED_ZONE) {
            return null;
        } else if (this.type == FRESH_SESH) {
            return null;
        } else if (this.type == DRESS_FOR_SUCCESS) {
            return context.getResources().getDrawable(R.drawable.image_dress_success);
        }
        return null;
    }


    public String getDescription(Context context) {
        if (this.type == RESUME_ZONE) {
            return context.getResources().getString(R.string.desc_resume_zone);
        } else if (this.type == MOCK_INTERVIEW) {
            return context.getResources().getString(R.string.desc_mock_interview);
        } else if (this.type == ELEVATED_ZONE) {
            return context.getResources().getString(R.string.desc_elevated);
        } else if (this.type == FRESH_SESH) {
            return context.getResources().getString(R.string.desc_fresh_sesh);
        } else if (this.type == DRESS_FOR_SUCCESS) {
            return context.getResources().getString(R.string.desc_dress_success);
        }
        return "";
    }

    public String getDescription2(Context context) {
        if (this.type == RESUME_ZONE) {
            return context.getResources().getString(R.string.desc_resume_zone_2);
        }
        return "";
    }


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
