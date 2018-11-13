package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("messageExtended")
    @Expose
    private Object messageExtended;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Object getMessageExtended() {
        return messageExtended;
    }

    public void setMessageExtended(Object messageExtended) {
        this.messageExtended = messageExtended;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return this.status.equals("success");
    }

}