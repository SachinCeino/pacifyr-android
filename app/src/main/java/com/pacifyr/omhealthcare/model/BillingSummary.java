package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingSummary {

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("sessionCost")
    @Expose
    private Double sessionCost;
    @SerializedName("pacifyrEarnings")
    @Expose
    private Double pacifyrEarnings;
    @SerializedName("session")
    @Expose
    private Session session;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("isCompleted")
    @Expose
    private Boolean isCompleted;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("creditCardCharges")
    @Expose
    private Integer creditCardCharges;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(Double sessionCost) {
        this.sessionCost = sessionCost;
    }

    public Double getPacifyrEarnings() {
        return pacifyrEarnings;
    }

    public void setPacifyrEarnings(Double pacifyrEarnings) {
        this.pacifyrEarnings = pacifyrEarnings;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreditCardCharges() {
        return creditCardCharges;
    }

    public void setCreditCardCharges(Integer creditCardCharges) {
        this.creditCardCharges = creditCardCharges;
    }

}