package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import java.io.Serializable;
import java.util.List;

public class AppoinmentNotification implements Serializable {
    @SerializedName("completedAt")
    @Expose
    private Object completedAt;
    @SerializedName("bookingRequestPaymentId")
    @Expose
    private Object bookingRequestPaymentId;
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
    private MUser pacifyr;
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
    private String preVisitNote;
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
    private User user;
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

    public Object getBookingRequestPaymentId() {
        return bookingRequestPaymentId;
    }

    public void setBookingRequestPaymentId(Object bookingRequestPaymentId) {
        this.bookingRequestPaymentId = bookingRequestPaymentId;
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

    public MUser getPacifyr() {
        return pacifyr;
    }

    public void setPacifyr(MUser pacifyr) {
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

    public String getPreVisitNote() {
        return preVisitNote;
    }

    public void setPreVisitNote(String preVisitNote) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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


    public class CouponCode {

        @SerializedName("discountPercentage")
        @Expose
        private Integer discountPercentage;
        @SerializedName("companyId")
        @Expose
        private Object companyId;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("validityInDays")
        @Expose
        private Integer validityInDays;
        @SerializedName("freeMinutes")
        @Expose
        private Integer freeMinutes;
        @SerializedName("id")
        @Expose
        private String id;

        public Integer getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(Integer discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public Object getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Object companyId) {
            this.companyId = companyId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getValidityInDays() {
            return validityInDays;
        }

        public void setValidityInDays(Integer validityInDays) {
            this.validityInDays = validityInDays;
        }

        public Integer getFreeMinutes() {
            return freeMinutes;
        }

        public void setFreeMinutes(Integer freeMinutes) {
            this.freeMinutes = freeMinutes;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
