package com.pacifyr.omhealthcare.fragment.schedule;

import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyr.omhealthcare.model.Pacifyr;

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
}
