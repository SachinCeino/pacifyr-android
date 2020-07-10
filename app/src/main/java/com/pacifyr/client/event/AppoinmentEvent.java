package com.pacifyr.client.event;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

/**
 * Created by Mani-Ceino on 1/3/2018.
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
