package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoleModel {

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("mainLabel")
    @Expose
    private String mainLabel;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMainLabel() {
        return mainLabel;
    }

    public void setMainLabel(String mainLabel) {
        this.mainLabel = mainLabel;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}