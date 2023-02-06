package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PreviousPrescription implements Serializable {
    @SerializedName("prescription")
    @Expose
    private Prescription prescription;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -1215534994363142993L;

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public class Prescription implements Serializable
    {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("timeSlotBookingId")
        @Expose
        private String timeSlotBookingId;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sessionId")
        @Expose
        private String sessionId;
        @SerializedName("prescriptionFile")
        @Expose
        private String prescriptionFile;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("pacifyr")
        @Expose
        private Object pacifyr;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("pacifyrId")
        @Expose
        private String pacifyrId;
        @SerializedName("user")
        @Expose
        private Object user;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = -3293144808206991688L;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getPrescriptionFile() {
            return prescriptionFile;
        }

        public void setPrescriptionFile(String prescriptionFile) {
            this.prescriptionFile = prescriptionFile;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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
}
