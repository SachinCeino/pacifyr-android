package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnreadCountModel {
    @SerializedName("count")
    @Expose
    public int count = 0;
    @SerializedName("status")
    @Expose
    public String status;

    public UnreadCountModel(int count, String status){
        this.count = count;
        this.status = status;
    }
}
