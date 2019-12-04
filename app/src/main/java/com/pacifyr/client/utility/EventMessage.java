package com.pacifyr.client.utility;

/**
 * Created by ceino on 27/6/17.
 */
public class EventMessage {
    private String notification;
    public EventMessage(String notification)
    {
        this.notification = notification;
    }
    public String getNotification()
    {
        return notification;
    }
}