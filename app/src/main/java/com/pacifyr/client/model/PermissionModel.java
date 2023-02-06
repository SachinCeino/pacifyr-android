package com.pacifyr.client.model;

public class PermissionModel {
    public Boolean popup;
    public Boolean allPermission;
    public PermissionModel(Boolean popup, Boolean allPermission){
        this.popup = popup;
        this.allPermission = allPermission;
    }
}