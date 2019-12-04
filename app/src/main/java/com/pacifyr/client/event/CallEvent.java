package com.pacifyr.client.event;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

/**
 * Created by Mani-Ceino on 1/3/2018.
 */

public class CallEvent {
    public static final String CALL_EVENT = "call_event";
    public CallEvent(Long qbID, boolean isVideoCall) {
        this.qbID = qbID;
        this.isVideoCall = isVideoCall;
    }

    //  public boolean isSucesss=false;
  public Long qbID=null;
    public CallEvent(boolean isVideoCall) {
        this.isVideoCall = isVideoCall;
        mpacifyer=null;
        this.qbID = null;
    }

    public CallEvent(MUser mpacifyer, boolean isVideoCall) {
        this.mpacifyer = mpacifyer;
        this.isVideoCall = isVideoCall;
    }

    public MUser mpacifyer=null;
    public boolean isVideoCall=false;

    public boolean isVideoCall() {
        return isVideoCall;
    }

    public void setVideoCall(boolean videoCall) {
        isVideoCall = videoCall;
    }
}
