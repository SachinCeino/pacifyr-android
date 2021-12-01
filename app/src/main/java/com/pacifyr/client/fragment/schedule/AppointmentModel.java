package com.pacifyr.client.fragment.schedule;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppointmentModel implements Serializable {
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    public class Result implements Serializable {

        @SerializedName("timeSlotId")
        @Expose
        private String timeSlotId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("startTimeGMT")
        @Expose
        private String startTimeGMT;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("pacifyr")
        @Expose
        private MUser pacifyr;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("pacifyrId")
        @Expose
        private String pacifyrId;
        @SerializedName("user")
        @Expose
        private Object user;
        @SerializedName("scheduleId")
        @Expose
        private String scheduleId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        @SerializedName("rejected")
        @Expose
        private Boolean rejected;

        @SerializedName("extended")
        @Expose
        private Boolean extended;


        @SerializedName("rejectReason")
        @Expose
        private Object rejectReason;

        @SerializedName("cancelled")
        @Expose
        private Boolean cancelled;

        @SerializedName("status")
        @Expose
        private String status;

        public Boolean getExtended() {
            return extended;
        }

        public void setExtended(Boolean extended) {
            this.extended = extended;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Boolean getRejected() {
            return rejected;
        }

        @SerializedName("preVisitNote")
        @Expose
        private Object preVisitNote;
        @SerializedName("cancelReason")
        @Expose
        private Object cancelReason;
        @SerializedName("refunds")
        @Expose
        private List<Refund> refunds = null;

        @SerializedName("confirmed")
        @Expose
        private Boolean confirmed;

        @SerializedName("completed")
        @Expose
        private Boolean completed;


        @SerializedName("visitNoteInternal")
        @Expose
        private Object visitNoteInternal;

        @SerializedName("visitNote")
        @Expose
        private Object visitNote;

        @SerializedName("isCompleted")
        @Expose
        private Boolean isCompleted;
        @SerializedName("statusText")
        @Expose
        private String statusText;

        @SerializedName("transactionId")
        @Expose
        private String transactionId;

        public List<Refund> getRefunds() {
            return refunds;
        }

        public void setRefunds(List<Refund> refunds) {
            this.refunds = refunds;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public Object getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(Object cancelReason) {
            this.cancelReason = cancelReason;
        }

        public Boolean getisCompleted() {
            return isCompleted;
        }

        public void setisCompleted(Boolean isCompleted) {
            this.isCompleted = isCompleted;
        }


        public Object getVisitNoteInternal() {
            return visitNoteInternal;
        }

        public void setVisitNoteInternal(Object visitNoteInternal) {
            this.visitNoteInternal = visitNoteInternal;
        }

        public Object getVisitNote() {
            return visitNote;
        }

        public void setVisitNote(Object visitNote) {
            this.visitNote = visitNote;
        }

        public Boolean getConfirmed() {
            return confirmed;
        }

        public void setConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
        }

        public Boolean getCompleted() {
            return completed;
        }

        public void setCompleted(Boolean completed) {
            this.completed = completed;
        }

        public Object getPreVisitNote() {
            return preVisitNote;
        }

        public void setPreVisitNote(Object preVisitNote) {
            this.preVisitNote = preVisitNote;
        }


        public void setRejected(Boolean rejected) {
            this.rejected = rejected;
        }

        public Object getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(Object rejectReason) {
            this.rejectReason = rejectReason;
        }

        public Boolean getCancelled() {
            return cancelled;
        }

        public void setCancelled(Boolean cancelled) {
            this.cancelled = cancelled;
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

        public String getStartTimeGMT() {
            return startTimeGMT;
        }

        public void setStartTimeGMT(String startTimeGMT) {
            this.startTimeGMT = startTimeGMT;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    public class Refund implements Serializable
    {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("amount")
        @Expose
        private float amount;
        @SerializedName("chargeId")
        @Expose
        private String chargeId;
        @SerializedName("timeSlotBookingId")
        @Expose
        private String timeSlotBookingId;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("refundId")
        @Expose
        private Object refundId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("status")
        @Expose
        private String status;
        private final static long serialVersionUID = 228508218246701734L;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getChargeId() {
            return chargeId;
        }

        public void setChargeId(String chargeId) {
            this.chargeId = chargeId;
        }

        public String getTimeSlotBookingId() {
            return timeSlotBookingId;
        }

        public void setTimeSlotBookingId(String timeSlotBookingId) {
            this.timeSlotBookingId = timeSlotBookingId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getRefundId() {
            return refundId;
        }

        public void setRefundId(Object refundId) {
            this.refundId = refundId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
