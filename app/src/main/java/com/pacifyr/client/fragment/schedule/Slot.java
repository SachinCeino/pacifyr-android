package com.pacifyr.client.fragment.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyr.client.model.User;

import java.io.Serializable;

/**
 * Created by ceino on 11/1/18.
 */

public class Slot implements Serializable{

        @SerializedName("startTime")
        @Expose
        private String startTime;
        @SerializedName("endTime")
        @Expose
        private String endTime;
        @SerializedName("isAvailable")
        @Expose
        private Boolean isAvailable;

        @SerializedName("id")
        @Expose
        private String id;
    @SerializedName("timeSlotBooking")
    @Expose
    private TimeSlotBooking timeSlotBooking;

    @SerializedName("booked")
    @Expose
    private Boolean booked;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("default")
    @Expose
    private Boolean _default;

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("bookedUserId")
    @Expose
    private Object bookedUserId;
    @SerializedName("scheduleId")
    @Expose
    private String scheduleId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("bookedUserModel")
    @Expose
    private User bookedUserModel;

    public TimeSlotBooking getTimeSlotBooking() {
        return timeSlotBooking;
    }

    public void setTimeSlotBooking(TimeSlotBooking timeSlotBooking) {
        this.timeSlotBooking = timeSlotBooking;
    }

    public User getBookedUserModel() {
        return bookedUserModel;
    }

    public void setBookedUserModel(User bookedUserModel) {
        this.bookedUserModel = bookedUserModel;
    }

    private  boolean hideSlote;
    public boolean clicked;

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isHideSlote() {
        return hideSlote;
    }

    public void setHideSlote(boolean hideSlote) {
        this.hideSlote = hideSlote;
    }


    public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    public Boolean getBooked() {
        return booked;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }



    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getPacifyrId() {
        return pacifyrId;
    }

    public void setPacifyrId(String pacifyrId) {
        this.pacifyrId = pacifyrId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDefault() {
        return _default;
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Object getBookedUserId() {
        return bookedUserId;
    }

    public void setBookedUserId(Object bookedUserId) {
        this.bookedUserId = bookedUserId;
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
