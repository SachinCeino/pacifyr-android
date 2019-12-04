package com.pacifyr.client.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelSpinnerData implements Serializable{
    @SerializedName("race")
    @Expose
    private String race;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("educationStatus")
    @Expose
    private String educationStatus;

    @SerializedName("refferedOrganisation")
    @Expose
    private String refferedOrganisation;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("refferedService")
    @Expose
    private String refferedService;

    private boolean selected=false;

    public String getReferredService() {
        return refferedService;
    }

    public void setReferredService(String refferedService) {
        this.refferedService = refferedService;
    }



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getRefferedOrganisation() {
        return refferedOrganisation;
    }

    public void setReferredOrganisation(String refferedOrganisation) {
        this.refferedOrganisation = refferedOrganisation;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String geteducationStatus() {
        return educationStatus;
    }

    public void seteducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

