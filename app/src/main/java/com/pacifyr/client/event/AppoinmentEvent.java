package com.pacifyr.client.event;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

/**

 */

public class AppoinmentEvent {
    String typeId;
    public static final String appointment_event="appointment_event";
    public AppoinmentEvent(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
