package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.pacifyr.client.utils.Constants;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.ErrorInfo;
import com.twilio.conversations.Message;
import com.twilio.conversations.Participant;
import com.twilio.conversations.ProgressListener;
import com.twilio.conversations.StatusListener;
import com.twilio.conversations.User;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.orginals.Auth.getDeviceId;
import static com.pacifyr.client.twiliochat.TwilioConversationActivity.DEFAULT_CONVERSATION_NAME;

interface QuickstartConversationsManagerListener {
    void receivedNewMessage(Message message);
    void messageSentCallback();
    void reloadMessages(List<Message> result);
    void messageTyping(Boolean visible);
}

interface TokenResponseListener {
    void receivedTokenResponse(boolean success, @Nullable Exception exception, String token);
}

interface AccessTokenListener {
    void receivedAccessToken(@Nullable String token, @Nullable Exception exception);
}


class QuickstartConversationsManager {

    // This is the unique name of the conversation  we are using

    final private ArrayList<Message> messages = new ArrayList<>();

    private ConversationsClient conversationsClient;

    private Conversation conversation;

    private QuickstartConversationsManagerListener conversationsManagerListener;

    private String tokenURL = "";
    private Context context;

    private class TokenResponse {
        String token;
    }

    void retrieveAccessTokenFromServer(final Context context, String identity,
                                       final TokenResponseListener listener) {

        // Set the chat token URL in your strings.xml file
        this.context=context;
        tokenURL = GLOBAL_URL+Constants.TWILIO_CHAT_TOKEN_URL;
        new Thread(new Runnable() {
            @Override
            public void run() {
                retrieveToken(context,new AccessTokenListener() {
                    @Override
                    public void receivedAccessToken(@Nullable String token,
                                                    @Nullable Exception exception) {
                        if (token != null) {
                            ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
                            ConversationsClient.create(context, token, props, mConversationsClientCallback);
                           // listener.receivedTokenResponse(true,null,token);
                        } else {
                            listener.receivedTokenResponse(false, exception, "");
                        }
                    }
                });
            }
        }).start();
    }

    void initializeWithAccessToken(final Context context, final String token) {

        ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
        ConversationsClient.create(context, token, props, mConversationsClientCallback);
    }

    private void retrieveToken(Context context, AccessTokenListener listener) {

        PrefManager prefM= PrefManager.getInstance(context);
        String userToken=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"twilio/chat/generateToken";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    Log.d("TWILIO", "Response from server: " + json);
                    Gson gson = new Gson();
                    TokenResponse tokenResponse = gson.fromJson(json.toString(),TokenResponse.class);
                    String accessToken = tokenResponse.token;
                    Log.d("TWILIO", "Retrieved access token from server: " + accessToken);
                    listener.receivedAccessToken(accessToken, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(context);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("deviceId", getDeviceId(context));
            jsonObject.putOpt("pushCredentialName", "android-test-client");
            jsonObject.putOpt("deviceType", "android");
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    void leaveFromChat(){
        if(conversation!= null)
        conversation.removeListener(mDefaultConversationListener);
    }

    void messageTyping(){
        if (conversation != null) {
            Log.d("TWILIO", "Msg typing........Clint: " );
            conversation.typing();
        }
    }

    void sendMessage(String messageBody) {

        if (conversation != null) {
            Message.Options options = Message.options().withBody(messageBody);
            Log.d("TWILIO","Message created");
            conversation.sendMessage(options, new CallbackListener<Message>() {
                @Override
                public void onSuccess(Message message) {
                    if (conversationsManagerListener != null) {
                        conversationsManagerListener.messageSentCallback();
                    }
                }
            });
        }
    }

    void sendMedia(String file) throws FileNotFoundException {
        conversation.sendMessage(
                Message.options()
                        .withMedia(new FileInputStream(file), "image/jpg")
                        .withMediaFileName("file.jpg")
                        .withMediaProgressListener(new ProgressListener() {
                            @Override
                            public void onStarted() {
                                Log.d("sendMedia","Upload started");
                            }

                            @Override
                            public void onProgress(long bytes) {
                                Log.d("sendMedia","Uploaded " + bytes + " bytes");
                            }

                            @Override
                            public void onCompleted(String mediaSid) {
                                Log.d("sendMedia","Upload completed");
                            }
                        }),
                new CallbackListener<Message>() {
                    @Override
                    public void onSuccess(Message msg) {
                        Log.d("sendMedia","Successfully sent MEDIA message");
                    }

                    @Override
                    public void onError(ErrorInfo error) {
                        Log.e("sendMedia","Error sending MEDIA message");
                    }
                });
    }


    private void loadChannels() {
        if (conversationsClient == null || conversationsClient.getMyConversations() == null) {

            return;
        }
        conversationsClient.getConversation(DEFAULT_CONVERSATION_NAME, new CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) {
                    if (conversation.getStatus() == Conversation.ConversationStatus.JOINED
                            || conversation.getStatus() == Conversation.ConversationStatus.NOT_PARTICIPATING) {
                        Log.d("TWILIO", "Already Exists in Conversation: " + DEFAULT_CONVERSATION_NAME);
                        QuickstartConversationsManager.this.conversation = conversation;
                        QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                        QuickstartConversationsManager.this.loadPreviousMessages(conversation);
                    } else {
                        Log.d("TWILIO", "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                        joinConversation(conversation);
                    }
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e("TWILIO", "Error retrieving conversation: " + errorInfo.getMessage());
                createConversation(DEFAULT_CONVERSATION_NAME);
            }

        });
    }

    public void createConversation(String DEFAULT_CONVERSATION_NAME) {
        Log.d("TWILIO", "Creating Conversation: " + DEFAULT_CONVERSATION_NAME);

        conversationsClient.createConversation(DEFAULT_CONVERSATION_NAME,
                new CallbackListener<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        if (conversation != null) {
                            Log.d("TWILIO", "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                            joinConversation(conversation);
                        }
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Log.e("TWILIO", "Error creating conversation: " + errorInfo.getMessage());
                    }
                });
    }


    private void joinConversation(final Conversation conversation) {
        Log.d("TWILIO", "Joining Conversation: " + conversation.getUniqueName());
        if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {
            conversation.typing();
            QuickstartConversationsManager.this.conversation = conversation;
            Log.d("TWILIO", "Already joined default conversation");
            QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
            return;
        }


        conversation.join(new StatusListener() {
            @Override
            public void onSuccess() {
                QuickstartConversationsManager.this.conversation = conversation;
                Log.d("TWILIO", "Joined default conversation");
                QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                QuickstartConversationsManager.this.loadPreviousMessages(conversation);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e("TWILIO", "Error joining conversation: " + errorInfo.getMessage());
            }
        });
    }



    private void loadPreviousMessages(final Conversation conversation) {
        conversation.getLastMessages(100,
                new CallbackListener<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> result) {
                        messages.addAll(result);
                        if (conversationsManagerListener != null) {
                            conversationsManagerListener.reloadMessages(result);
                        }
                    }
                });
    }


    private final ConversationsClientListener mConversationsClientListener =
            new ConversationsClientListener() {

                @Override
                public void onConversationAdded(Conversation conversation) {
                    Log.w("####","onConversationAdded");

                }

                @Override
                public void onConversationUpdated(Conversation conversation, Conversation.UpdateReason updateReason) {
                    Log.w("####","onConversationUpdated");
                }

                @Override
                public void onConversationDeleted(Conversation conversation) {
                    Log.w("####","onConversationDeleted");
                }

                @Override
                public void onConversationSynchronizationChange(Conversation conversation) {
                    Log.w("####","onConversationSynchronizationChange");
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.w("####","onError");
                }

                @Override
                public void onUserUpdated(User user, User.UpdateReason updateReason) {
                    Log.w("####","onUserUpdated");
                }

                @Override
                public void onUserSubscribed(User user) {
                    Log.w("####","onUserSubscribed");
                }

                @Override
                public void onUserUnsubscribed(User user) {
                    Log.w("####","onUserUnsubscribed");
                }

                @Override
                public void onClientSynchronization(ConversationsClient.SynchronizationStatus synchronizationStatus) {
                    if (synchronizationStatus == ConversationsClient.SynchronizationStatus.COMPLETED) {
                        Log.w("####","loadChannels");
                        loadChannels();
                    }
                }

                @Override
                public void onNewMessageNotification(String s, String s1, long l) {
                    Log.w("####","onNewMessageNotification");
                }

                @Override
                public void onAddedToConversationNotification(String s) {
                    Log.w("####","onAddedToConversationNotification");
                }

                @Override
                public void onRemovedFromConversationNotification(String s) {
                    Log.w("####","onRemovedFromConversationNotification");
                }

                @Override
                public void onNotificationSubscribed() {
                    Log.w("####","onNotificationSubscribed");
                }

                @Override
                public void onNotificationFailed(ErrorInfo errorInfo) {
                    Log.w("####","onNotificationFailed");
                }

                @Override
                public void onConnectionStateChange(ConversationsClient.ConnectionState connectionState) {
                    Log.w("####","onConnectionStateChange");
                }

                @Override
                public void onTokenExpired() {
                    Log.w("####","onTokenExpired");
                }

                @Override
                public void onTokenAboutToExpire() {
                    retrieveToken(context, new AccessTokenListener() {
                        @Override
                        public void receivedAccessToken(@Nullable String token, @Nullable Exception exception) {
                            if (token != null) {
                                conversationsClient.updateToken(token, new StatusListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("TWILIO", "Refreshed access token.");
                                    }
                                });
                            }
                        }
                    });
                }
            };

    private final CallbackListener<ConversationsClient> mConversationsClientCallback =
            new CallbackListener<ConversationsClient>() {
                @Override
                public void onSuccess(ConversationsClient conversationsClient) {
                    QuickstartConversationsManager.this.conversationsClient = conversationsClient;
                    conversationsClient.addListener(QuickstartConversationsManager.this.mConversationsClientListener);
                    Log.d("TWILIO", "Success creating Twilio Conversations Client");
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.e("TWILIO", "Error creating Twilio Conversations Client: " + errorInfo.getMessage());
                }
            };


    private final ConversationListener mDefaultConversationListener = new ConversationListener() {


        @Override
        public void onMessageAdded(final Message message) {
            Log.d("TWILIO", "Message added");
            messages.add(message);
            if (conversationsManagerListener != null) {
                conversationsManagerListener.receivedNewMessage(message);
            }
        }

        @Override
        public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {
            Log.d("TWILIO", "Message updated: " + message.getMessageBody());
        }

        @Override
        public void onMessageDeleted(Message message) {
            Log.d("TWILIO", "Message deleted");
        }

        @Override
        public void onParticipantAdded(Participant participant) {
            Log.d("TWILIO", "Participant added: " + participant.getIdentity());
        }

        @Override
        public void onParticipantUpdated(Participant participant, Participant.UpdateReason updateReason) {
            Log.d("TWILIO", "Participant updated: " + participant.getIdentity() + " " + updateReason.toString());
        }

        @Override
        public void onParticipantDeleted(Participant participant) {
            Log.d("TWILIO", "Participant deleted: " + participant.getIdentity());
        }

        @Override
        public void onTypingStarted(Conversation conversation, Participant participant) {
            Log.d("TWILIO", "Started Typing: " + participant.getIdentity());
            if (conversationsManagerListener != null) {
                conversationsManagerListener.messageTyping(true);
            }
        }

        @Override
        public void onTypingEnded(Conversation conversation, Participant participant) {
            Log.d("TWILIO", "Ended Typing: " + participant.getIdentity());
            if (conversationsManagerListener != null) {
                conversationsManagerListener.messageTyping(false);
            }
        }

        @Override
        public void onSynchronizationChanged(Conversation conversation) {
            Log.d("TWILIO", "onSynchronizationChanged" + conversation);
        }
    };

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setListener(QuickstartConversationsManagerListener listener)  {
        this.conversationsManagerListener = listener;
    }
}

