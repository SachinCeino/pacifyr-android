package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.InternetConnectionEvent;
import com.pacifyr.client.netconnectionlistener.ConnectionCheckBroadcast;
import com.pacifyr.client.network.NetworkService;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.twilio.conversations.Message;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TwilioCoversationListActivity extends PacifyrActivity implements QuickstartConversationsManagerListener{



    String ActionbarTitle="";
    public static void start(Context context, boolean isFromSlideMenu) {
        Intent intent = new Intent(context, TwilioCoversationListActivity.class);
        intent.putExtra("isFromSlideMenu",isFromSlideMenu);
        context.startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mLinearLayoutManager != null)
        currentVisiblePosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();

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
    private final QuickstartConversationsManager quickstartConversationsManager = new QuickstartConversationsManager();
    private TextView mEmptyLayout;
    private RecyclerView RecyclerView_chatParticipants;
    private TwilioChatListAdapter mTwilioChatListAdapter;
    int currentVisiblePosition = 0;
    private List<TwilioConversationCreate> results = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private String userid ="";
    private PrefManager prefM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twilio_conversation_list_activity);
        mEmptyLayout=findViewById(R.id.mEmptyLayout);
        RecyclerView_chatParticipants=findViewById(R.id.RecyclerView_chatParticipants);

        prefM = PrefManager.getInstance(TwilioCoversationListActivity.this);
        userid=prefM.getUserID();

        onSetActionBar();

        //retrieveTokenFromServer();
        setUpParticipantsListAdapter();

    }


    public void getConversationParticipantsList(){
        setLoading();
        showProgress();

        String userToken=prefM.getAccessToken();

        String url = NetworkService.GLOBAL_URL+"messages/customer?skip=0&limit=25";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    Log.d("TWILIO", "Response from server: " + json);
                    Gson gson = new Gson();
                    if (isValid(json))
                    {
                        TwilioChatParticipants mTwilioChatParticipants = gson.fromJson(json.toString(), TwilioChatParticipants.class);
                        if (isValid(mTwilioChatParticipants) && isValidSize(mTwilioChatParticipants.getResults()))
                        {
                            mEmptyLayout.setVisibility(View.GONE);
                            results.clear();
                            results.addAll(mTwilioChatParticipants.getResults());
                            mTwilioChatListAdapter.notifyDataSetChanged();
                            //setUpParticipantsListAdapter(mTwilioChatParticipants.getResults());
                        }
                        else
                        {
                            mEmptyLayout.setVisibility(View.VISIBLE);
                        }

                    }


                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        AQuery aq = new AQuery(TwilioCoversationListActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        try {
            aq.ajax(url, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUpParticipantsListAdapter() {
        mTwilioChatListAdapter=new TwilioChatListAdapter(this,results,userid);
         mLinearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        RecyclerView_chatParticipants.setLayoutManager(mLinearLayoutManager);
        RecyclerView_chatParticipants.setAdapter(mTwilioChatListAdapter);

    }

    public void clearUnreadCount(TwilioConversationCreate item, int position){
        if (ConnectionCheckBroadcast.CheckInternetConnection(this)) {
            if (isValid(item.getTwilioConversationId()))
            {
                results.get(position).getUnreadStatus().clear();
                mTwilioChatListAdapter.notifyItemChanged(position);

                TwilioConversationActivity.start(this,item);

            }

        }else {

            noInternetDialog();
        }

    }


    private void retrieveTokenFromServer() {
        quickstartConversationsManager.retrieveAccessTokenFromServer(TwilioCoversationListActivity.this, "", new TokenResponseListener() {
            @Override
            public void receivedTokenResponse(boolean success, @Nullable Exception exception, String token) {
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
            titleTxv.setText(getResources().getString(R.string.messages));
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

    private void noInternetDialog(){
        setAlertStatusInWhiteNoInternet("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus2();
                finishAffinity();
            }
        });
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChanged(InternetConnectionEvent event) {
        try {
            if (isValid(event) && isShowDialogs()) {
                if (!event.isConnected())
                {
                    noInternetDialog();
                }
                else
                {
                    hideProgress();
                    hideStatus2();
                    onResume();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectionCheckBroadcast.CheckInternetConnection(this)) {
            getConversationParticipantsList();
        } else {
            noInternetDialog();

        }
        if(mLinearLayoutManager != null){
            mLinearLayoutManager.scrollToPosition(currentVisiblePosition);
            currentVisiblePosition = 0;
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void receivedNewMessage(Message message) {
        Log.w("TAG","receivedNewMessage________");
    }

    @Override
    public void messageSentCallback() {
        Log.w("TAG","messageSentCallback________");
    }

    @Override
    public void reloadMessages(List<Message> result) {
        Log.w("TAG","reloadMessages________");
    }

    @Override
    public void messageTyping(Boolean visible) {

    }

    @Override
    public void uploadProgress(Boolean visible) {

    }

    @Override
    public void sendMediaSuccess(String type) {

    }

    @Override
    public void sendMediaError(String error) {

    }

    @Override
    public void setProgressValue(long value) {

    }


}
