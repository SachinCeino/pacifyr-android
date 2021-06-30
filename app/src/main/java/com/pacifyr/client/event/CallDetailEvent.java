package com.pacifyr.client.event;

/**

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
