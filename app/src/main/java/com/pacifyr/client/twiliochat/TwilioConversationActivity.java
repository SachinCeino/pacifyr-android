package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.quickblox.chat.interfaces.AttachmentClick;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.quickblox.chat.ui.activity.ChatActivity;
import com.pacifyr.client.quickblox.chat.ui.activity.SelectUsersActivity;
import com.pacifyr.client.quickblox.chat.ui.widget.BottomAttachDialog;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.Message;
import com.twilio.conversations.ProgressListener;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;


public class TwilioConversationActivity extends
        PacifyrActivity implements QuickstartConversationsManagerListener, AttachmentClick {

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

    private static final int REQUESTCODE_CAMERA_IMAGE = 101;
    private static final int REQUESTCODE_GALLERY_IMAGE = 102;

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;
    private CircleImageView userCimv;

    public final static String TAG = "TwilioConversations";
    private String  userId="";

    // Update this identity for each individual user, for instance after they login
    private String identity = "CONVERSATIONS_USER";

    private TwilioMessagesAdapter messagesAdapter;

    private EditText writeMessageEditText;
    private QuickstartConversationsManager quickstartConversationsManager;

    public static String DEFAULT_CONVERSATION_NAME = "general";
    private CircleImageView civUserPic;
    private MainTextView tvUserName;
    private RecyclerView recyclerView;
    private ImageButton sendChatMessageButton;
    private PrefManager prefM;
    private LinearLayoutManager layoutManager;
    private  BottomAttachDialog bottomSheet;
    private static final String EXTRA_URL = "url";

    private static final int PICK_IMAGE = 130;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twilio_conversation_activity);

        setLoadingWait();
        showProgress();

        initVar();
        onSetActionBar();
        initView();
        loadData();
        intiListener();

    }

    private void initVar(){
        quickstartConversationsManager = new QuickstartConversationsManager();
        prefM = PrefManager.getInstance(this);
        layoutManager = new LinearLayoutManager(this);
        bottomSheet = new BottomAttachDialog(isCallActive, this, false);
    }

    private void initView(){
        recyclerView = findViewById(R.id.messagesRecyclerView);
        writeMessageEditText = findViewById(R.id.writeMessageEditText);
        sendChatMessageButton = findViewById(R.id.sendChatMessageButton);


    }


    private void loadData(){

        getUserData();

        //Set ConversationsManager Listener
        quickstartConversationsManager.setListener(this);
        // Token Method 2 - retrieve the access token from a web server or Twilio Function
        retrieveTokenFromServer();

        loadUserData();
        loadChatList();

    }

    /**
     * GetExtras TwilioConversationCreate object and check isValid
     */
    private void getUserData(){
        if (isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getSerializable("object")))
        {
            mTwilioConversationCreate= (TwilioConversationCreate) getIntent().getExtras().getSerializable("object");
            if (isValid(mTwilioConversationCreate)&& isValid(mTwilioConversationCreate.getTwilioConversationId()))
                DEFAULT_CONVERSATION_NAME=mTwilioConversationCreate.getTwilioConversationId();
        }

    }

    /**
     * Load Message adapter
     */
    private void loadChatList(){
        // for a chat app, show latest messages at the bottom
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userId = prefM.getUserID();
        messagesAdapter = new TwilioMessagesAdapter(quickstartConversationsManager, userId,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(messagesAdapter);

    }

    /**
     * Set user data / toolbar name and pic
     */
    private void loadUserData(){
        MUser pacifyr = mTwilioConversationCreate.getPacifyr();
        String imgUrl =null, name="";
        if(isValid(pacifyr)){
            name = ""+pacifyr.getFirstName();

            if(isValid(pacifyr.getImage())){
                imgUrl = ""+GLOBAL_IMAGE_URL+mTwilioConversationCreate.getPacifyr().getImage();
            }

        }else {
            Log.e("ERROR","getPacifyr data null");
        }
        leftTxv.setText(name);
        //Image
        Picasso.with(TwilioConversationActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
    }

    private void intiListener(){
        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

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
    }


    /**
     * List scroll to bottom
     */
    private void pageScrollToBottom(){
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount()+1);

    }


    private void shoImagePickerAlert(View view) {

        bottomSheet.show(getSupportFragmentManager(), "AttachBottomSheet");
    }

    public void onAttachmentsClick(View view) {
        if (Utility.checkPermission(this, "chat")) {
            shoImagePickerAlert(view);
        } else {
           // attachPendingView = view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
             if (requestCode == REQUESTCODE_CAMERA_IMAGE) {
                String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                if (isValid(pathsList)) {
                    for (String url : pathsList) {
                        resizeImage(url);
                    }
                }
            }
            else if (requestCode == REQUESTCODE_GALLERY_IMAGE) {
                String dataStr = data.getDataString();
                Uri pathsList = data.getData();
                if (isValid(pathsList)) {

                    //Log.w("file_name","imageUri: "+pathsList[0].);

                    resizeImage(pathsList.toString());



                }
            }
        }
    }


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AttachmentImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    private void resizeImage(String url) {

        Glide.with(this).load(url).override(1200, 1200).into(new Target<Drawable>() {
            @Override
            public void onLoadStarted(@androidx.annotation.Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                if (resource instanceof BitmapDrawable) {

                    File file = Utility.saveFileInCache(TwilioConversationActivity.this, ((BitmapDrawable) resource).getBitmap(), "tmp_" + System.currentTimeMillis() + ".jpg");
                    if (file != null) {

                        Uri contentUri = Uri.fromFile(file);

                        Log.w("file_name","imageUri: "+contentUri.getPath());


                        try {
                            quickstartConversationsManager.sendMedia(contentUri.getPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //attachmentPreviewAdapter.add(file);
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        Log.d(TAG, "File size : " + fileSizeInKB + " KB");
                    }
                }
            }

            @Override
            public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb) {

            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb) {

            }

            @androidx.annotation.Nullable
            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void setRequest(@androidx.annotation.Nullable Request request) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        });
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
            userCimv = findViewById(R.id.user_cimv);
            leftTxv = findViewById(R.id.left_txv);
            titleTxv = findViewById(R.id.title_txv);
            rightLlay = findViewById(R.id.right_llay);
            rightTxv = findViewById(R.id.right_txv);
            rightImv = findViewById(R.id.right_imv);
            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.VISIBLE);
            rightLlay.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.VISIBLE);
            userCimv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.INVISIBLE);
            leftLlay.setVisibility(View.VISIBLE);

            rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
            rightTxv.setText("close");

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
        Log.w(TAG,"receivedNewMessage________");
        if (isValid(messagesAdapter)) {
            messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount() + 1);
            pageScrollToBottom();
        }
    }

    @Override
    public void messageSentCallback() {
        Log.w(TAG,"messagesAdapter________");
        //f (isValid(messagesAdapter))
           // messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount()+1);
        //pageScrollToBottom();

    }

    @Override
    public void reloadMessages() {
        hideProgress();
        Log.w(TAG,"reloadMessages________");
    if (isValid(messagesAdapter))
        messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount());

        }

    @Override
    public void onClick(int pos) {
        switch (pos) {
            case 0:
                new GligarPicker().requestCode(REQUESTCODE_CAMERA_IMAGE).disableCamera(false).cameraDirect(true).withActivity(this).show();
                bottomSheet.dismiss();
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                Uri pickerInitialUri = null;
                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                if (pickerInitialUri != null) {
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                }

                startActivityForResult(intent, REQUESTCODE_GALLERY_IMAGE);

                // new GligarPicker().requestCode(REQUESTCODE_GALLERY_IMAGE).disableCamera(true).withActivity(ChatActivity.this).show();
                bottomSheet.dismiss();
                break;
            case 2:
                //todo open docs list
                bottomSheet.dismiss();
                break;
            case -1:
                bottomSheet.dismiss();
                break;
        }
    }
}
