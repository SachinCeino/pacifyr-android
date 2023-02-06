package com.pacifyr.client.twiliochat;

import androidx.annotation.NonNull;

import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.NotificationPayload;
import com.twilio.conversations.Participant;
import com.twilio.conversations.StatusListener;
import com.twilio.conversations.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TwilioMainConversationsClient {
    public static ConversationsClient mConversationsClient=null;
    public static ConversationsClient getInstance() {
        return mConversationsClient;
    }
    private TwilioMainConversationsClient() { }
}
