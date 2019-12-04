package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Installation {

    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("arn")
    @Expose
    private String arn;
    @SerializedName("udid")
    @Expose
    private String udid;
    @SerializedName("devicetoken")
    @Expose
    private String devicetoken;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("applicationname")
    @Expose
    private String applicationname;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplicationname() {
        return applicationname;
    }

    public void setApplicationname(String applicationname) {
        this.applicationname = applicationname;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}