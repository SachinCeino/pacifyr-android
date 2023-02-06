package com.pacifyr.client.classes;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

public class PendingCall {

    private final MUser pacifyr;
    private final boolean isVideoCall;
    private final boolean isAppointment;
    private final String timeSlotBookingId;

    public PendingCall(MUser pacifyerDetial, boolean isVideoCall, boolean isAppointment, String timeSlotBookingId) {
        this.pacifyr = pacifyerDetial;
        this.isVideoCall = isVideoCall;
        this.isAppointment = isAppointment;
        this.timeSlotBookingId = timeSlotBookingId;
    }

    public MUser getPacifyr() {
        return pacifyr;
    }

    public boolean isVideoCall() {
        return isVideoCall;
    }

    public boolean isAppointment() {
        return isAppointment;
    }

    public String getTimeSlotBookingId() {
        return timeSlotBookingId;
    }
}
