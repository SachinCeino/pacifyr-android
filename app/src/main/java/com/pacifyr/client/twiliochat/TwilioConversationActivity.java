package com.pacifyr.client.twiliochat;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.MessageMediaModel;
import com.pacifyr.client.quickblox.chat.interfaces.AttachmentClick;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.quickblox.chat.ui.widget.BottomAttachDialog;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.picasso.Picasso;
import com.twilio.conversations.Message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;
import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyr.client.utils.Utility.MY_PERMISSIONS_REQUEST_CHAT_PERMISSION;


public class TwilioConversationActivity extends
        PacifyrActivity implements QuickstartConversationsManagerListener, AttachmentClick {


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
    private static final int REQUESTCODE_FILE = 103;

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv,tvStatus;
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
    private RecyclerView recyclerView;
    private ImageButton sendChatMessageButton;
    private PrefManager prefM;
    private LinearLayoutManager layoutManager;
    private  BottomAttachDialog bottomSheet;
    private static final String EXTRA_URL = "url";

    private static final int PICK_IMAGE = 130;
    Uri imageUri;
    View attachPendingView;
    private ArrayList<MessageMediaModel> messageList;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DEFAULT_CONVERSATION_NAME = "general";
        if(quickstartConversationsManager != null){
            quickstartConversationsManager.leaveFromChat();
        }
    }

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
        messageList = new ArrayList<>();
        quickstartConversationsManager = new QuickstartConversationsManager();
        prefM = PrefManager.getInstance(this);
        layoutManager = new LinearLayoutManager(this);
        bottomSheet = new BottomAttachDialog(isCallActive, this, true);
    }

    private void initView(){
        recyclerView = findViewById(R.id.messagesRecyclerView);
        writeMessageEditText = findViewById(R.id.writeMessageEditText);
        sendChatMessageButton = findViewById(R.id.sendChatMessageButton);
        tvStatus = findViewById(R.id.tv_status);



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

            Log.w("getTwilioConversationId","getTwilioConversationId: "+DEFAULT_CONVERSATION_NAME );
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
        messagesAdapter = new TwilioMessagesAdapter(messageList, userId,this);
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

        //back button click
        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
        //Chat send Button click
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

        //Text change listener
        writeMessageEditText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                quickstartConversationsManager.messageTyping();
            }

        });
    }


    /**
     * List scroll to bottom
     */
    private void pageScrollToBottom(){
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount()+1);

    }


    /**
     * Show bottom sheet for select attach type
     */
    private void shoImagePickerAlert(View view) {

        bottomSheet.show(getSupportFragmentManager(), "AttachBottomSheet");
    }

    /**
     * Check Gallery permission and show image picker
     */
    public void onAttachmentsClick(View view) {
        if (Utility.checkPermission(this, "chat")) {
            shoImagePickerAlert(view);
        } else {
            Log.w("requestCode","attachPendingView: ");
            attachPendingView = view;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w("requestCode","requestCode: "+requestCode);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CHAT_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    shoImagePickerAlert(attachPendingView);

                } else {
                    permissionDenied();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * When User denied storage or gallery permission, will show the Alert dialog
     */
    private void permissionDenied(){
        //If User was asked permission before and denied
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Permission needed");
        alertDialogBuilder.setMessage("Storage permission needed for accessing photos");
        alertDialogBuilder.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", TwilioConversationActivity.this.getPackageName(),
                        null);
                intent.setData(uri);
                TwilioConversationActivity.this.startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: Cancelling");
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
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
               // String dataStr = data.getDataString();


                 if (isValid(data.getClipData())) {
                     ClipData mClipData = data.getClipData();
                     for (int i = 0; i < mClipData.getItemCount(); i++) {
                         ClipData.Item item = mClipData.getItemAt(i);
                         Uri uri = item.getUri();
                         // display your images
                         resizeImage(uri.toString());
                     }
                 } else if (isValid(data.getData())) {
                     Uri uri = data.getData();
                     // display your image
                     resizeImage(uri.toString());
                 }
            } else if(requestCode ==  REQUESTCODE_FILE){

                 if (isValid(data.getClipData())) {
                     ClipData mClipData = data.getClipData();
                     for (int i = 0; i < mClipData.getItemCount(); i++) {
                         ClipData.Item item = mClipData.getItemAt(i);
                         Uri uri = item.getUri();

                        try {
                            ParcelFileDescriptor fd = this.getContentResolver().openFileDescriptor(uri, "r");
                            Long fileSizeInBytes = fd.getStatSize();
                            FileInputStream fileInputStream = new FileInputStream(fd.getFileDescriptor());
                            quickstartConversationsManager.sendMedia(fileInputStream, "FILE", fileSizeInBytes);
                        } catch (FileNotFoundException e) {
                        }
                     }
                 } else if (isValid(data.getData())) {
                     Uri uri = data.getData();

                     try {
                         ParcelFileDescriptor fd = this.getContentResolver().openFileDescriptor(uri, "r");
                         Long fileSizeInBytes = fd.getStatSize();
                         FileInputStream fileInputStream = new FileInputStream(fd.getFileDescriptor());
                         quickstartConversationsManager.sendMedia(fileInputStream, "FILE", fileSizeInBytes);
                     } catch (FileNotFoundException e) {
                     }
                 }
             }
        }
    }

    /**
     * Api call for all message read status
     */
    public void apiCallMessageRead(){

        PrefManager prefM= PrefManager.getInstance(this);
        String userToken=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String conversationId = mTwilioConversationCreate.getTwilioConversationId();


        String url = GLOBAL_URL+"twilio/webhooks/message.read";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    String jsonS = json.toString();
                    Log.w(TAG, "Messeaage read: "+jsonS);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


            AQuery aq = new AQuery(TwilioConversationActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",userToken);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("twilioConversationId",conversationId);
            jsonObject.putOpt("userId",userid);
            aq.post(url,jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            //hideProgress();
            ex.printStackTrace();
        }
    }

    /**
     * Show typing indicator on title bar
     */
    private void showTyping(Boolean visible){
        if(visible){
            tvStatus.setVisibility(View.VISIBLE);
        }else {
            tvStatus.setVisibility(View.GONE);
        }

    }

    public void pdfReaderIntent(String url,String title){

        setLoading();
        showProgress();
        FileLoader.with(this)
                .load(url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        hideProgress();
                        File loadedFile = response.getBody();
                        // do something with the file
                        Log.d(TAG, "File downloaded ");
                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(TwilioConversationActivity.this, PdfViewer.class);
                        intent.putExtra("title",title);
                        intent.putExtra("restoreCall",true);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        hideProgress();
                        Log.d(TAG, "File download failed ");
                    }
                });
    }



    public static void start(Context context, String url) {
        Intent intent = new Intent(context, AttachmentImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    /**
     * Resize Image for upload
     */
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
                        long fileSizeInBytes = file.length();

                        try {
                            FileInputStream fileInputStream = new FileInputStream(
                                    TwilioConversationActivity.this.getContentResolver()
                                            .openFileDescriptor(contentUri, "r").getFileDescriptor());
                            quickstartConversationsManager.sendMedia(fileInputStream,"IMAGE",fileSizeInBytes);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //attachmentPreviewAdapter.add(file);
                        //long fileSizeInBytes = file.length();
                        //long fileSizeInKB = fileSizeInBytes / 1024;
                        //Log.d(TAG, "File size : " + fileSizeInKB + " KB");
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


    /**
     * Show/Hide progress dialog
     */
    private void visibilityProgressDialog(Boolean isVisibility){
            if(isVisibility){
                setProgressBar();
                showProgress();
            } else{
                hideProgress();
            }

    }

    /**
     * Set progress value
     */
    private void setValueProgressDialog(long value) {
        int toInt = (int)value;
        setProgressValue(toInt);
    }




    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void messageTyping(Boolean visible) {
        Log.w(TAG,"messageTyping________");
        if (isValid(messagesAdapter)) {
            showTyping(visible);
        }
    }

    @Override
    public void uploadProgress(Boolean visible) {
        visibilityProgressDialog(visible);
    }

    @Override
    public void setProgressValue(long value) {
        setValueProgressDialog(value);
    }


    @Override
    public void receivedNewMessage(Message message) {
        Log.w(TAG,"receivedNewMessage________");


        if (isValid(messagesAdapter)) {
            messageList.add(new MessageMediaModel(message,""));
            messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount() + 1);
            pageScrollToBottom();
            apiCallMessageRead();
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
    public void reloadMessages(List<Message> result) {
        hideProgress();
        Log.w(TAG,"reloadMessages________");
    if (isValid(messagesAdapter) && isValid(result))
        messageList.clear();
        for (int i=0; i< result.size();i++){
            messageList.add(new MessageMediaModel(result.get(i),""));
        }
        //messageList.addAll(result);
        messagesAdapter.notifyItemInserted(messagesAdapter.getItemCount());
        apiCallMessageRead();
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
                //intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
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
                Intent intentFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intentFile.addCategory(Intent.CATEGORY_OPENABLE);
                intentFile.setType("application/pdf");
                //intentFile.putExtra(EXTRA_ALLOW_MULTIPLE, true);
                Uri filePickerInitialUri = null;
                // Optionally, specify a URI for the file that should appear in the
                // system file picker when it loads.
                if (filePickerInitialUri != null) {
                    intentFile.putExtra(DocumentsContract.EXTRA_INITIAL_URI, filePickerInitialUri);
                }

                startActivityForResult(intentFile, REQUESTCODE_FILE);
                bottomSheet.dismiss();
                break;
            case -1:
                bottomSheet.dismiss();
                break;
        }
    }
}
