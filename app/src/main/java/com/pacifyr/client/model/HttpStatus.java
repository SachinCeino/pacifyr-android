package com.pacifyr.client.model;

public class HttpStatus {
    boolean valid = false;
    String message = "NA";

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
