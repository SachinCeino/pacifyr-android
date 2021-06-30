package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.twilio.conversations.Conversation;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


public class TwilioConversationActivity extends PacifyrActivity implements QuickstartConversationsManagerListener {


    String ActionbarTitle="";

    @Override
    public void onPause() {
        super.onPause();

    }
    TwilioConversationCreate mTwilioConversationCreate;
    public static void start(Context context, TwilioConversationCreate mTwilioConversationCreate) {
        Intent intent = new Intent(context, TwilioConversationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("object",(Serializable) mTwilioConversationCreate);
        context.startActivity(intent);
    }
    public static void start(Context context, boolean isFromSlideMenu) {
        Intent intent = new Intent(context, TwilioConversationActivity.class);
        intent.putExtra("isFromSlideMenu",isFromSlideMenu);
        context.startActivity(intent);
    }
    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
    }

    private int retryCount = 0;

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public final static String TAG = "TwilioConversations";
    private String  userId;

    // Update this identity for each individual user, for instance after they login
    private String identity = "CONVERSATIONS_USER";

    private TwilioMessagesAdapter messagesAdapter;

    private EditText writeMessageEditText;
    private final QuickstartConversationsManager quickstartConversationsManager = new QuickstartConversationsManager();

    public static String DEFAULT_CONVERSATION_NAME = "general";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twilio_conversation_activity);

        PrefManager prefM = PrefManager.getInstance(this);
         userId = prefM.getUserID();

        onSetActionBar();
        if (isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getSerializable("object")))
        {
            mTwilioConversationCreate= (TwilioConversationCreate) getIntent().getExtras().getSerializable("object");
            if (isValid(mTwilioConversationCreate)&& isValid(mTwilioConversationCreate.getTwilioConversationId()))
                DEFAULT_CONVERSATION_NAME=mTwilioConversationCreate.getTwilioConversationId();
        }
        quickstartConversationsManager.setListener(this);

        RecyclerView recyclerView = findViewById(R.id.messagesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // for a chat app, show latest messages at the bottom
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messagesAdapter = new TwilioMessagesAdapter(quickstartConversationsManager, userId);
        recyclerView.setAdapter(messagesAdapter);

        writeMessageEditText = findViewById(R.id.writeMessageEditText);


        Button sendChatMessageButton = findViewById(R.id.sendChatMessageButton);
        sendChatMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageBody = writeMessageEditText.getText().toString();
                if (messageBody.length() > 0) {
                    quickstartConversationsManager.sendMessage(messageBody);
                    writeMessageEditText.setText("");
                }

            }
        });

        // Token Method 1 - supplied from strings.xml as the test_access_token
        // quickstartConversationsManager.initializeWithAccessToken(this, getString(R.string.test_access_token));

        // Token Method 2 - retrieve the access token from a web server or Twilio Function
        retrieveTokenFromServer();

    }

    private void retrieveTokenFromServer() {
        quickstartConversationsManager.retrieveAccessTokenFromServer(TwilioConversationActivity.this, identity, new TokenResponseListener() {
            @Override
            public void receivedTokenResponse(boolean success, @Nullable Exception exception, String token) {
                if (success) {
                    quickstartConversationsManager.initializeWithAccessToken(getApplicationContext(),token);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /* // need to modify user interface elements on the UI thread
                            if (isValid(mTwilioConversationCreate) && isValid(mTwilioConversationCreate.getTwilioConversationId()))
                            {
                                quickstartConversationsManager.createConversation(mTwilioConversationCreate.getTwilioConversationId());
                            }*/
                        }
                    });
                }
                else {
                    String errorMessage = getString(R.string.error_retrieving_access_token);
                    if (exception != null) {
                        errorMessage = errorMessage + " " + exception.getLocalizedMessage();
                    }
                   /* Toast.makeText(MainActivity.this,
                            errorMessage,
                            Toast.LENGTH_LONG)
                            .show();*/
                }
            }
        });
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public void onSetActionBar() {
        try {
            actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
            bottomPanel = findViewById(R.id.bottom_panel);
            leftLlay = findViewById(R.id.left_llay);
            backImv = findViewById(R.id.back_imv);
            leftTxv = findViewById(R.id.left_txv);
            titleTxv = findViewById(R.id.title_txv);
            rightLlay = findViewById(R.id.right_llay);
            rightTxv = findViewById(R.id.right_txv);
            rightImv = findViewById(R.id.right_imv);
            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            titleTxv.setText(getResources().getString(R.string.chat_activity));
            leftLlay.setVisibility(View.VISIBLE);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        finish();

                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void receivedNewMessage() {
        if (isValid(messagesAdapter))
            messagesAdapter.notifyDataSetChanged();

    }

    @Override
    public void messageSentCallback() {
        if (isValid(messagesAdapter))
            messagesAdapter.notifyDataSetChanged();

    }

    @Override
    public void reloadMessages() {
    if (isValid(messagesAdapter))
        messagesAdapter.notifyDataSetChanged();

        }
}
