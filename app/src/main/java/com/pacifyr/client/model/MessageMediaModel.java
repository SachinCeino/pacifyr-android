package com.pacifyr.client.model;

import com.twilio.conversations.Message;

public class MessageMediaModel {
    public Message message;
    public String mediaUrl = "";

    public MessageMediaModel(Message message, String mediaUrl){
        this.message = message;
        this.mediaUrl = mediaUrl;
    }
}
