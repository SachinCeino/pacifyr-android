package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PrescriptionList implements Serializable {
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
    public class Result {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sessionId")
        @Expose
        private String sessionId;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("pacifyr")
        @Expose
        private Pacifyr pacifyr;
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

        @SerializedName("prescriptionFile")
        @Expose
        private String prescriptionFile;

        public String getPrescriptionFile() {
            return prescriptionFile;
        }

        public void setPrescriptionFile(String prescriptionFile) {
            this.prescriptionFile = prescriptionFile;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Pacifyr getPacifyr() {
            return pacifyr;
        }

        public void setPacifyr(Pacifyr pacifyr) {
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
