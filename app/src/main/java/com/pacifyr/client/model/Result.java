package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("interviewStatus")
    @Expose
    private Object interviewStatus;
    @SerializedName("dressedForInterview")
    @Expose
    private Boolean dressedForInterview;
    @SerializedName("freshSeshCompleted")
    @Expose
    private Boolean freshSeshCompleted;
    @SerializedName("properGreeting")
    @Expose
    private Boolean properGreeting;
    @SerializedName("finalOutcome")
    @Expose
    private Object finalOutcome;
    @SerializedName("hadCopyOfResume")
    @Expose
    private Boolean hadCopyOfResume;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("resumeZoneCompleted")
    @Expose
    private Boolean resumeZoneCompleted;
    @SerializedName("elevatedIntroZoneCompleted")
    @Expose
    private Boolean elevatedIntroZoneCompleted;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("employerId")
    @Expose
    private Object employerId;
    @SerializedName("mockInterviewCompleted")
    @Expose
    private Boolean mockInterviewCompleted;
    @SerializedName("employer")
    @Expose
    private Object employer;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dressForSuccessCompleted")
    @Expose
    private Boolean dressForSuccessCompleted;
    @SerializedName("event")
    @Expose
    private ModelEvent event;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Object getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(Object interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public Boolean getDressedForInterview() {
        return dressedForInterview;
    }

    public void setDressedForInterview(Boolean dressedForInterview) {
        this.dressedForInterview = dressedForInterview;
    }

    public Boolean getFreshSeshCompleted() {
        return freshSeshCompleted;
    }

    public void setFreshSeshCompleted(Boolean freshSeshCompleted) {
        this.freshSeshCompleted = freshSeshCompleted;
    }

    public Boolean getProperGreeting() {
        return properGreeting;
    }

    public void setProperGreeting(Boolean properGreeting) {
        this.properGreeting = properGreeting;
    }

    public Object getFinalOutcome() {
        return finalOutcome;
    }

    public void setFinalOutcome(Object finalOutcome) {
        this.finalOutcome = finalOutcome;
    }

    public Boolean getHadCopyOfResume() {
        return hadCopyOfResume;
    }

    public void setHadCopyOfResume(Boolean hadCopyOfResume) {
        this.hadCopyOfResume = hadCopyOfResume;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getResumeZoneCompleted() {
        return resumeZoneCompleted;
    }

    public void setResumeZoneCompleted(Boolean resumeZoneCompleted) {
        this.resumeZoneCompleted = resumeZoneCompleted;
    }

    public Boolean getElevatedIntroZoneCompleted() {
        return elevatedIntroZoneCompleted;
    }

    public void setElevatedIntroZoneCompleted(Boolean elevatedIntroZoneCompleted) {
        this.elevatedIntroZoneCompleted = elevatedIntroZoneCompleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Object employerId) {
        this.employerId = employerId;
    }

    public Boolean getMockInterviewCompleted() {
        return mockInterviewCompleted;
    }

    public void setMockInterviewCompleted(Boolean mockInterviewCompleted) {
        this.mockInterviewCompleted = mockInterviewCompleted;
    }

    public Object getEmployer() {
        return employer;
    }

    public void setEmployer(Object employer) {
        this.employer = employer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDressForSuccessCompleted() {
        return dressForSuccessCompleted;
    }

    public void setDressForSuccessCompleted(Boolean dressForSuccessCompleted) {
        this.dressForSuccessCompleted = dressForSuccessCompleted;
    }

    public ModelEvent getEvent() {
        return event;
    }

    public void setEvent(ModelEvent event) {
        this.event = event;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}