package com.pacifyr.omhealthcare.event;

/**
 * Created by Mani-Ceino on 1/9/2018.
 */

public class CallDetailEvent {
    public Boolean isSuccess=false;

    public CallDetailEvent() {
        this.isSuccess = true;
    }
    public CallDetailEvent(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
