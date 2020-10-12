package com.pacifyr.client.event;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

/**
 * Created by Mani-Ceino on 1/3/2018.
 */

public class TwilioCallEvent {
    private boolean isCallConnected;
    private boolean isVideoCall;
    private String roomName;
    private MUser pacifyer;
    private String idsession;


    public boolean isCallConnected() {
        return isCallConnected;
    }

    public void setCallConnected(boolean callConnected) {
        isCallConnected = callConnected;
    }

    public boolean isVideoCall() {
        return isVideoCall;
    }

    public void setVideoCall(boolean videoCall) {
        isVideoCall = videoCall;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public MUser getPacifyer() {
        return pacifyer;
    }

    public void setPacifyer(MUser pacifyer) {
        this.pacifyer = pacifyer;
    }

    public String getIdsession() {
        return idsession;
    }

    public void setIdsession(String idsession) {
        this.idsession = idsession;
    }
}
