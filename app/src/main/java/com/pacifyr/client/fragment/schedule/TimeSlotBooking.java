package com.pacifyr.client.fragment.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyr.client.model.User;

import java.io.Serializable;

/**

 */

public class TimeSlotBooking implements Serializable{

    @SerializedName("completedAt")
    @Expose
    private Object completedAt;
    @SerializedName("rejected")
    @Expose
    private Boolean rejected;
    @SerializedName("visitNoteInternal")
    @Expose
    private Object visitNoteInternal;
    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("pacifyr")
    @Expose
    private Object pacifyr;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;
    @SerializedName("timeSlotId")
    @Expose
    private String timeSlotId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("rejectReason")
    @Expose
    private Object rejectReason;
    @SerializedName("startTimeGMT")
    @Expose
    private String startTimeGMT;
    @SerializedName("preVisitNote")
    @Expose
    private Object preVisitNote;
    @SerializedName("cancelled")
    @Expose
    private Boolean cancelled;
    @SerializedName("visitNote")
    @Expose
    private Object visitNote;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cancelReason")
    @Expose
    private Object cancelReason;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("scheduleId")
    @Expose
    private String scheduleId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Object getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Object completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Object getVisitNoteInternal() {
        return visitNoteInternal;
    }

    public void setVisitNoteInternal(Object visitNoteInternal) {
        this.visitNoteInternal = visitNoteInternal;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Object getPacifyr() {
        return pacifyr;
    }

    public void setPacifyr(Object pacifyr) {
        this.pacifyr = pacifyr;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

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

    public Object getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Object rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getStartTimeGMT() {
        return startTimeGMT;
    }

    public void setStartTimeGMT(String startTimeGMT) {
        this.startTimeGMT = startTimeGMT;
    }

    public Object getPreVisitNote() {
        return preVisitNote;
    }

    public void setPreVisitNote(Object preVisitNote) {
        this.preVisitNote = preVisitNote;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Object getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(Object visitNote) {
        this.visitNote = visitNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
