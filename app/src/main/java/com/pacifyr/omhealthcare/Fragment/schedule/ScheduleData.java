package com.pacifyr.omhealthcare.Fragment.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ceino on 11/1/18.
 */

public class ScheduleData implements Serializable{

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("timeZoneOffset")
    @Expose
    private Object timeZoneOffset;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("date")
    @Expose
    private String date;

    boolean setselected=false;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(Object timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPacifyrId() {
        return pacifyrId;
    }

    public void setPacifyrId(String pacifyrId) {
        this.pacifyrId = pacifyrId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSetselected() {
        return setselected;
    }

    public void setSetselected(boolean setselected) {
        this.setselected = setselected;
    }
}
