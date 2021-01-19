package com.pacifyrapp.utilitylibrary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Company implements Serializable {

    @SerializedName("createPacifyrs")
    @Expose
    private Boolean createPacifyrs;
    @SerializedName("customerRoleId")
    @Expose
    private String customerRoleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("freeMinutes")
    @Expose
    private Object freeMinutes;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("appSource")
    @Expose
    private String appSource;
    @SerializedName("platform")
    @Expose
    private Object platform;

    public Boolean getCreatePacifyrs() {
        return createPacifyrs;
    }

    public void setCreatePacifyrs(Boolean createPacifyrs) {
        this.createPacifyrs = createPacifyrs;
    }

    public String getCustomerRoleId() {
        return customerRoleId;
    }

    public void setCustomerRoleId(String customerRoleId) {
        this.customerRoleId = customerRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getFreeMinutes() {
        return freeMinutes;
    }

    public void setFreeMinutes(Object freeMinutes) {
        this.freeMinutes = freeMinutes;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppSource() {
        return appSource;
    }

    public void setAppSource(String appSource) {
        this.appSource = appSource;
    }

    public Object getPlatform() {
        return platform;
    }

    public void setPlatform(Object platform) {
        this.platform = platform;
    }


}