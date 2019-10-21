package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {

    @SerializedName("date")
    @Expose
    private List<String> date = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("employers")
    @Expose
    private List<Object> employers = null;
    @SerializedName("attendingUserIds")
    @Expose
    private List<String> attendingUserIds = null;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("checkinUsers")
    @Expose
    private List<Object> checkinUsers = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("notAttendingUserIds")
    @Expose
    private List<Object> notAttendingUserIds = null;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("schedule")
    @Expose
    private String schedule;
    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("employerPositionIds")
    @Expose
    private List<String> employerPositionIds = null;
    @SerializedName("notAttendingUsers")
    @Expose
    private List<Object> notAttendingUsers = null;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("attendingUsers")
    @Expose
    private List<Object> attendingUsers = null;
    @SerializedName("employerIds")
    @Expose
    private List<String> employerIds = null;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("checkinUserIds")
    @Expose
    private List<String> checkinUserIds = null;
    @SerializedName("longitude")
    @Expose
    private Object longitude;

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Object> getEmployers() {
        return employers;
    }

    public void setEmployers(List<Object> employers) {
        this.employers = employers;
    }

    public List<String> getAttendingUserIds() {
        return attendingUserIds;
    }

    public void setAttendingUserIds(List<String> attendingUserIds) {
        this.attendingUserIds = attendingUserIds;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getCheckinUsers() {
        return checkinUsers;
    }

    public void setCheckinUsers(List<Object> checkinUsers) {
        this.checkinUsers = checkinUsers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getNotAttendingUserIds() {
        return notAttendingUserIds;
    }

    public void setNotAttendingUserIds(List<Object> notAttendingUserIds) {
        this.notAttendingUserIds = notAttendingUserIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getEmployerPositionIds() {
        return employerPositionIds;
    }

    public void setEmployerPositionIds(List<String> employerPositionIds) {
        this.employerPositionIds = employerPositionIds;
    }

    public List<Object> getNotAttendingUsers() {
        return notAttendingUsers;
    }

    public void setNotAttendingUsers(List<Object> notAttendingUsers) {
        this.notAttendingUsers = notAttendingUsers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<Object> getAttendingUsers() {
        return attendingUsers;
    }

    public void setAttendingUsers(List<Object> attendingUsers) {
        this.attendingUsers = attendingUsers;
    }

    public List<String> getEmployerIds() {
        return employerIds;
    }

    public void setEmployerIds(List<String> employerIds) {
        this.employerIds = employerIds;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getCheckinUserIds() {
        return checkinUserIds;
    }

    public void setCheckinUserIds(List<String> checkinUserIds) {
        this.checkinUserIds = checkinUserIds;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

}