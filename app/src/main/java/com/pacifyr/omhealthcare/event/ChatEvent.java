package com.pacifyr.omhealthcare.event;

import android.util.Log;

import com.ceino.utilitylibrary.model.pacifyr.MUser;

/**
 * Created by Mani-Ceino on 1/3/2018.
 */

public class ChatEvent {
    public ChatEvent(Long qbID, boolean isVideoCall) {
        this.qbID = qbID;
        this.isSuccess = isVideoCall;
    }

    //  public boolean isSucesss=false;
  public Long qbID=null;
    public ChatEvent(boolean isVideoCall) {
        this.isSuccess = isVideoCall;
        mpacifyer=null;
        this.qbID = null;
    }

    public ChatEvent(MUser mpacifyer, boolean isVideoCall) {
        this.mpacifyer = mpacifyer;
        this.isSuccess = isVideoCall;
    }

    public MUser mpacifyer=null;
    public boolean isSuccess=false;


}
