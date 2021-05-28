package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sachin Thampan on 28/10/19.
 */
public class BookingRequestPayment {
    @SerializedName("timeSlotId")
    @Expose
    private String timeSlotId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("freeMinutesUsed")
    @Expose
    private Integer freeMinutesUsed;

    @SerializedName("charged")
    @Expose
    private Boolean charged;

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public Integer getFreeMinutesUsed() {
        return freeMinutesUsed;
    }

    public void setFreeMinutesUsed(Integer freeMinutesUsed) {
        this.freeMinutesUsed = freeMinutesUsed;
    }

    public Boolean getCharged() {
        return charged;
    }

    public void setCharged(Boolean charged) {
        this.charged = charged;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPacifyrId() {
        return pacifyrId;
    }

    public void setPacifyrId(String pacifyrId) {
        this.pacifyrId = pacifyrId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
