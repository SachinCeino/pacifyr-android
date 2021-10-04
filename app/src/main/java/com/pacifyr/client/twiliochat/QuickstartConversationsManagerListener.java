package com.pacifyr.client.twiliochat;

import com.twilio.conversations.Message;

import java.util.List;

public interface QuickstartConversationsManagerListener {
    void receivedNewMessage(Message message);

    void messageSentCallback();

    void reloadMessages(List<Message> result);

    void messageTyping(Boolean visible);

    void uploadProgress(Boolean visible);

    void sendMediaSuccess(String type);

    void sendMediaError(String error);

    void setProgressValue(long value);
}
