package com.pacifyr.client.quickblox.chat.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.snackbar.Snackbar;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.chat.ui.adapter.AttachmentPreviewAdapter;
import com.pacifyr.client.quickblox.chat.ui.adapter.ChatAdapter;
import com.pacifyr.client.quickblox.chat.ui.widget.AttachmentPreviewAdapterView;
import com.pacifyr.client.quickblox.chat.utils.chat.ChatHelper;
import com.pacifyr.client.quickblox.chat.utils.qb.PaginationHistoryListener;
import com.pacifyr.client.quickblox.chat.utils.qb.QbChatDialogMessageListenerImp;
import com.pacifyr.client.quickblox.chat.utils.qb.QbDialogHolder;
import com.pacifyr.client.quickblox.chat.utils.qb.QbDialogUtils;
import com.pacifyr.client.quickblox.chat.utils.qb.QbUsersHolder;
import com.pacifyr.client.quickblox.chat.utils.qb.VerboseQbChatConnectionListener;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;


//com.pacifyr.pacifyrapp.quickblox.chat.ui.activity.BaseActivity
public class ChatActivity extends PacifyrAppCompactActivity implements ImagePickerCallback {
    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    public static final String EXTRA_DIALOG_ID = "dialogId";

    private ProgressBar progressBar;
    private ListView messagesListView;
    private EditText messageEditText;

    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;

    private ChatAdapter chatAdapter;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;

    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    public static MUser mPacifyr;
    private String myUserId;
    private ArrayList<String> opponentIds;
    private AlertDialog choosePicker;
    private CameraImagePicker imagePicker;
    private ImagePicker galImagePicker;
    private String outputPath;
    private View sendButton;

    private static final long SEND_TYPING_STATUS_DELAY = 3000;
    public static final long TYPING_STATUS_DELAY = 2000;
    public static final long TYPING_STATUS_INACTIVITY_DELAY = 10000;
    private QBUser currentUser;
    private TextView typingStatus;
    private TextView TextView_noMessages;


    public static void startForResult(Activity activity, int code, QBChatDialog dialogId,MUser mPaci) {
        mPacifyr=null;
        mPacifyr=mPaci;
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
        activity.startActivityForResult(intent, code);
    }
    public static void start(Context activity, QBChatDialog dialogId,MUser mPaci) {
        if (Utility.checkPermission(activity, "write")) {
            mPacifyr = null;
            mPacifyr = mPaci;
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
            activity.startActivity(intent);
        }
    }
    public static void startForResult(Activity activity, int code, QBChatDialog dialogId) {
        if (Utility.checkPermission(activity, "write")) {

            mPacifyr = null;

            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
            activity.startActivityForResult(intent, code);
        }
    }
    public static void start(Context activity, QBChatDialog dialogId) {
        if (Utility.checkPermission(activity, "write")) {

            mPacifyr = null;

            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", outputPath);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quickblox_activit_chat);

        showNotificationPopups(false);
        myUserId = PrefManager.getInstance(this).getUserID();
        typingStatus = findViewById(R.id.tv_typing_status);
        LogUtils.LOGD("jp", "ChatActivity onCreate");
        LogUtils.LOGD("jp",  "onCreate ChatActivity on Thread ID = " + Thread.currentThread().getId());

        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
        TextView_noMessages=findViewById(R.id.TextView_noMessages);

       if (ChatHelper.getCurrentUser() != null) {
            currentUser = ChatHelper.getCurrentUser();
        } else {
            finish();
        }
        if (!isValid(qbChatDialog))
            finish();

            try {
                try {
                    qbChatDialog.initForChat(QBChatService.getInstance());
                } catch (IllegalArgumentException ie) {
                    ie.printStackTrace();
                    Toast.makeText(ChatActivity.this, "Sorry, cannot chat with this user. Reason " + ie.getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                chatMessageListener = new ChatMessageListener();

                qbChatDialog.addMessageListener(chatMessageListener);
                qbChatDialog.addIsTypingListener(new TypingStatusListener());

                initChatConnectionListener();

                initViews();
                initChat();
            }
            catch (Exception e)
            {
                Log.w("@@@",String.valueOf(e));
            }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (qbChatDialog != null) {
            outState.putString(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(EXTRA_DIALOG_ID));
        }
        if (savedInstanceState.containsKey("picker_path")) {
            outputPath = savedInstanceState.getString("picker_path");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
    }

    @Override
    public void onBackPressed() {
        releaseChat();
        sendDialogId();

        if (isValid(isCallActive) && isCallActive) {
            Intent i = new Intent(ChatActivity.this, VideoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);

        MenuItem menuItemLeave = menu.findItem(R.id.menu_chat_action_leave);
        MenuItem menuItemAdd = menu.findItem(R.id.menu_chat_action_add);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_chat_action_delete);
        if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
            menuItemLeave.setVisible(false);
            menuItemAdd.setVisible(false);
        } else {
            menuItemDelete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_chat_action_info:
                ChatInfoActivity.start(this, qbChatDialog);
                return true;

            case R.id.menu_chat_action_add:
                SelectUsersActivity.startForResult(this, REQUEST_CODE_SELECT_PEOPLE, qbChatDialog);
                return true;

            case R.id.menu_chat_action_leave:
                leaveGroupChat();
                return true;

            case R.id.menu_chat_action_delete:
                deleteChat();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        setResult(RESULT_OK, result);
    }

    private void leaveGroupChat() {
        ProgressDialogFragment.show(getSupportFragmentManager());
        ChatHelper.getInstance().exitFromDialog(qbChatDialog, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbDialog, Bundle bundle) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                QbDialogHolder.getInstance().deleteDialog(qbDialog);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                showErrorSnackbar(R.string.error_leave_chat, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaveGroupChat();
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(
                        SelectUsersActivity.EXTRA_QB_USERS);

                updateDialog(selectedUsers);
            } else if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (galImagePicker == null) {
                    galImagePicker = new ImagePicker(ChatActivity.this);
                    galImagePicker.shouldGenerateThumbnails(true);
                    galImagePicker.setImagePickerCallback(ChatActivity.this);
                }
                galImagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (imagePicker == null) {
                    imagePicker = new CameraImagePicker(ChatActivity.this);
                    imagePicker.shouldGenerateThumbnails(true);
                    imagePicker.reinitialize(outputPath);
                    imagePicker.setImagePickerCallback(ChatActivity.this);
                }
                imagePicker.submit(data);
            }
        }
    }

/*    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                attachmentPreviewAdapter.add(file);
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        showErrorSnackbar(0, e, null);
    }

    @Override
    public void onImagePickClosed(int requestCode) {
        // ignore
    }*/

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.list_chat_messages);
    }

    public void onSendChatClick(View view) {
        sendButton = view;
        try {
            qbChatDialog.sendStopTypingNotification();
        } catch (XMPPException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    TextView_noMessages.setVisibility(View.GONE);
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            TextView_noMessages.setVisibility(View.GONE);
            sendChatMessage(text, null);

        }
    }

    public void onAttachmentsClick(View view) {
//        new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
        shoImagePickerAlert(view);
    }
    String[] items = null;
    private void shoImagePickerAlert(View view) {
        if (!isCallActive) {
            items = new String[]{"Camera", "Gallery"};
        } else {
            items = new String[]{"Gallery"};
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equalsIgnoreCase("Camera")) {
                    imagePicker = new CameraImagePicker(ChatActivity.this);
                    imagePicker.shouldGenerateThumbnails(true);
                    imagePicker.setImagePickerCallback(ChatActivity.this);
                    outputPath = imagePicker.pickImage();
                } else if (items[which].equalsIgnoreCase("Gallery")) {
                    galImagePicker = new ImagePicker(ChatActivity.this);
                    galImagePicker.shouldGenerateThumbnails(true);
                    galImagePicker.setImagePickerCallback(ChatActivity.this);
                    galImagePicker.pickImage();
                }
            }
        });
        builder.setTitle("Choose");
        if (choosePicker != null && choosePicker.isShowing()) {
            choosePicker.dismiss();
        }
        choosePicker = builder.create();
        choosePicker.show();

    }

    public void showMessage(QBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void initViews() {
        onSetActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);

        messagesListView = _findViewById(R.id.list_chat_messages);
        messagesListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        messagesListView.setStackFromBottom(true);
        messageEditText = _findViewById(R.id.edit_chat_message);
        messageEditText.addTextChangedListener(new TextInputWatcher());

        progressBar = _findViewById(R.id.progress_chat);
        attachmentPreviewContainerLayout = _findViewById(R.id.layout_attachment_preview_container);

        attachmentPreviewAdapter = new AttachmentPreviewAdapter(this,
                new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                    @Override
                    public void onAttachmentCountChanged(int count) {
                        attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                    }
                },
                new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                    @Override
                    public void onAttachmentUploadError(QBResponseException e) {
                        showErrorSnackbar(0, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onAttachmentsClick(v);
                            }
                        });
                    }
                });
        AttachmentPreviewAdapterView previewAdapterView = _findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);
    }


    private class TextInputWatcher implements TextWatcher {
        private Timer timer = new Timer();
        private long lastSendTime = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (SystemClock.uptimeMillis() - lastSendTime > SEND_TYPING_STATUS_DELAY) {
                lastSendTime = SystemClock.uptimeMillis();
                try {
                    qbChatDialog.sendIsTypingNotification();
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        qbChatDialog.sendStopTypingNotification();
                    } catch (XMPPException | SmackException.NotConnectedException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }, TYPING_STATUS_DELAY);
        }
    }



    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void sendChatMessage(String text, QBAttachment attachment) {
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            chatMessage.addAttachment(attachment);
        } else {
            chatMessage.setBody(text);
        }
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(true);

        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()){
            Toaster.shortToast("You're still joining a group chat, please wait a bit");
            return;
        }

        try {
            if (sendButton != null) {
                sendButton.setEnabled(false);
            }
            qbChatDialog.sendMessage(chatMessage, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    if (sendButton != null) {
                        sendButton.setEnabled(true);
                    }
                    if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                        showMessage(chatMessage);
                    }

                    if (opponentIds != null && opponentIds.size() > 0 && opponentIds.size() < 20) {
                        for (int i = 0; i < opponentIds.size(); i++) {
                            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                                @Override
                                public void callback(String url, JSONObject json, AjaxStatus status) {
                                    if (json != null) {
                                        Log.d(TAG, "sendChatNotification Response " + json.toString());
                                    } else {
                                        Log.d(TAG, "sendChatNotification Response null");
                                    }
                                }
                            };
                            if (attachment != null) {
                                new NetworkService().sendChatNotification(opponentIds.get(i), "chat", attachment.getType(), cb);
                            } else if (text != null) {
                                new NetworkService().sendChatNotification(opponentIds.get(i), "chat", text, cb);
                            }
                        }
                    }
                    if (attachment != null) {
                        attachmentPreviewAdapter.remove(attachment);
                    } else {
                        messageEditText.setText("");
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    if (sendButton != null) {
                        sendButton.setEnabled(true);
                    }
                    Toaster.shortToast("Message not sent " + e.getMessage());
                }
            });


        } catch (Exception e) {
            if (sendButton != null) {
                sendButton.setEnabled(true);
            }
            Log.w(TAG, e);
            Toaster.shortToast("Message not sent " + e.getMessage());
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                finish();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                snackbar = returnshowErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }
    private void updateDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbChatDialog, selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        loadDialogUsers();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        showErrorSnackbar(R.string.chat_info_add_people_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialog(selectedUsers);
                                    }
                                });
                    }
                }
        );
    }

    private void loadDialogUsers() {
        Log.d(TAG, "loadDialogUsers");
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                Log.d(TAG, "loadDialogUsers success -> " + users != null ? users.toString() :
                        "null");
                setChatNameToActionBar();
                opponentIds = getOpponents(users);
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "loadDialogUsers err -> ");
                showErrorSnackbar(R.string.chat_load_users_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadDialogUsers();
                            }
                        });
            }
        });
    }

    private ArrayList<String> getOpponents(ArrayList<QBUser> users) {
        ArrayList<String> oppIds = new ArrayList<>();
        if (users != null && users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {

                try {
                    if (myUserId != null && !users.get(i).getLogin().equals(myUserId)) {
                        oppIds.add(users.get(i).getLogin());
                    }

                }
                catch (Exception e)
                {
                    Log.w("QQQQ", e + "Position" + i);
                }
            }
        }
        return oppIds;
    }

    /*private void setChatNameToActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(chatName);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }*/
    private void setChatNameToActionBar() {
        try {


           /* if(isValid(mPacifyr)) {
                if (isPacifyer()) {
                    String chatName1 = QbDialogUtils.getDialogNamepacifyr(qbChatDialog);
                    if (isValid(leftTxv)) defSetText(leftTxv, "" + chatName1);

                    String imgUrl = QbDialogUtils.getDialogImagepacifyr(qbChatDialog);
                    if (imgUrl == null)
                        Picasso.with(ChatActivity.this).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
                    else
                        Picasso.with(ChatActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
                }
                if (isCustomer()) {
                    String chatName2 = QbDialogUtils.getDialogName(qbChatDialog);
                    if (isValid(leftTxv)) defSetText(leftTxv, "" + chatName2);


                    String imgUrl = QbDialogUtils.getDialogImage(qbChatDialog);
                    if (imgUrl == null)
                        Picasso.with(ChatActivity.this).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
                    else
                        Picasso.with(ChatActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);

                }
            }*/
            if (isPacifyer()) {
                String chatName1 = QbDialogUtils.getOpposteUserName(qbChatDialog,ChatActivity.this);
                if (isValid(leftTxv)) defSetText(leftTxv, "" + chatName1);

                String imgUrl = QbDialogUtils.getDialogImageChat(qbChatDialog,ChatActivity.this);
                if (imgUrl == null)
                    Picasso.with(ChatActivity.this).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
                else
                    Picasso.with(ChatActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
            }
            if (isCustomer()) {
                String chatName2 = QbDialogUtils.getOpposteUserName(qbChatDialog,ChatActivity.this);
                if (isValid(leftTxv)) defSetText(leftTxv, "" + chatName2);


                String imgUrl = QbDialogUtils.getDialogImageChat(qbChatDialog,ChatActivity.this);
                if (imgUrl == null)
                    Picasso.with(ChatActivity.this).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);
                else
                    Picasso.with(ChatActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(userCimv);

            }
/*

            if(isValid(mPacifyr.getImageUrl()))
                Picasso.with(ChatActivity.this).load(mPacifyr.getImageUrl()).into(userCimv);

*/




        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order


                Collections.reverse(messages);
                if (chatAdapter == null) {
                    chatAdapter = new ChatAdapter(ChatActivity.this, qbChatDialog, messages);
                    chatAdapter.setPaginationHistoryListener(new PaginationHistoryListener() {
                        @Override
                        public void downloadMore() {
                            loadChatHistory();
                        }
                    });
                    chatAdapter.setOnItemInfoExpandedListener(new ChatAdapter.OnItemInfoExpandedListener() {
                        @Override
                        public void onItemInfoExpanded(final int position) {
                            if (isLastItem(position)) {
                                // HACK need to allow info textview visibility change so posting it via handler
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesListView.setSelection(position);
                                    }
                                });
                            } else {
                                messagesListView.smoothScrollToPosition(position);
                            }
                        }

                        private boolean isLastItem(int position) {
                            return position == chatAdapter.getCount() - 1;
                        }
                    });
                    if (unShownMessages != null && !unShownMessages.isEmpty()) {
                        List<QBChatMessage> chatList = chatAdapter.getList();
                        for (QBChatMessage message : unShownMessages) {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        }
                    }
                    messagesListView.setAdapter(chatAdapter);
                    messagesListView.setDivider(null);
                    if (!isValidSize(chatAdapter.getList()))
                    {
                        TextView_noMessages.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        TextView_noMessages.setVisibility(View.GONE);
                    }

                } else {
                    chatAdapter.addList(messages);
                    messagesListView.setSelection(messages.size());

                }
                progressBar.setVisibility(View.GONE);

                Bundle params =
                        FirebaseLogging.getConstantArgsLoggedIn(ChatActivity.this);
                FirebaseLogging.sendLog(ChatActivity.this,
                        FirebaseLogging.EVENT_CHAT_HISTORY_LOADED, params);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                snackbar = returnshowErrorSnackbar(R.string.connection_error, e, null);

                Bundle params =
                        FirebaseLogging.getConstantArgsLoggedIn(ChatActivity.this);
                FirebaseLogging.sendLog(ChatActivity.this,
                        FirebaseLogging.EVENT_CHAT_HISTORY_LOAD_FAILED, params);
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void scrollMessageListDown() {
        messagesListView.setSelection(messagesListView.getCount() - 1);
    }

    private void deleteChat() {
        ChatHelper.getInstance().deleteDialog(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.dialogs_deletion_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteChat();
                            }
                        });
            }
        });
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(getSnackbarAnchorView()) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        /*Glide.with(ChatActivity.this).load(Uri.parse(list.get(0).getQueryUri()))
                .asBitmap().into(new Target<Bitmap>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Log.d(TAG, "Exception " + e.getMessage());
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Log.d(TAG, "Resdy ");
                if (resource != null){
                    byte[] bytes = Utility.bitmapToByteArray(resource);
                    File tempFile = Utility.getTempFile(ChatActivity.this, "temp_image.tmp");
                    boolean status = Utility.writeBytesToFile(bytes, tempFile);
                    if (status){
                        if (tempFile != null) {
                            attachmentPreviewAdapter.add(tempFile);
                        }
                        else{
                            showIOSDialogOK("Error", "Cannot use this file", "OK");
                        }
                    }
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {

            }

            @Override
            public void getSize(SizeReadyCallback cb) {

            }

            @Override
            public void setRequest(Request request) {

            }

            @Override
            public Request getRequest() {
                return null;
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
        });*/
        File file = new File(list.get(0).getOriginalPath());
        if (file != null) {
            attachmentPreviewAdapter.add(file);
        } else {
            showIOSDialogOK("Error", "Cannot use this file", "OK");
        }
    }

    @Override
    public void onError(String s) {
        showIOSDialogOK("Error", s, "OK");
    }

    public class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            showMessage(qbChatMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPacifyr=null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private CircleImageView userCimv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;


    protected void onSetActionBar() {


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

        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(ChatActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();*/
               // onBackPressed();
                // DashboardActivity.start(ConnectToPacifyrActivity.this);
            }
        });
        rightLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(ChatActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();*/
                onBackPressed();
             /*   if(isCustomer()){
                    DashboardActivity.start(ChatActivity.this);
                else
                  onBackPressed();*/
                // DashboardActivity.start(ConnectToPacifyrActivity.this);
            }
        });
        setChatNameToActionBar();
    }

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/




    private class TypingStatusListener implements QBChatDialogTypingListener {
        private ArrayList<String> currentTypingUserNames = new ArrayList<>();
        private HashMap<Integer, Timer> usersTimerMap = new HashMap<>();

        @Override
        public void processUserIsTyping(final String dialogID, final Integer userID) {
            Integer currentUserID = currentUser.getId();
            if (dialogID != null && dialogID.equals(qbChatDialog.getDialogId()) && userID != null && !userID.equals(currentUserID)) {
                updateTypingInactivityTimer(dialogID, userID);
                QBUser user = QbUsersHolder.getInstance().getUserById(userID);
                if (user != null && user.getFullName() != null) {
                    addUserToTypingList(user);
                } else {
                    Log.d(TAG, "Loading unknown typing user with ID: " + userID);
                    QBUsers.getUser(userID).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            Log.d(TAG, "User " + qbUser.getId() + " Loaded from Server");
                            QbUsersHolder.getInstance().putUser(qbUser);
                            addUserToTypingList(qbUser);
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.d(TAG, "Loading User Error: " + e.getMessage());
                        }
                    });
                }
            }
        }

        private void addUserToTypingList(QBUser user) {
            String userName = TextUtils.isEmpty(user.getFullName())? user.getLogin() : user.getFullName();
            if (!TextUtils.isEmpty(userName) && !currentTypingUserNames.contains(userName) && usersTimerMap.containsKey(user.getId())) {
                currentTypingUserNames.add(userName);
            }
            typingStatus.setText(makeStringFromNames());
            typingStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void processUserStopTyping(String dialogID, Integer userID) {
            Integer currentUserID = currentUser.getId();
            if (dialogID != null && dialogID.equals(qbChatDialog.getDialogId()) && userID != null && !userID.equals(currentUserID)) {
                stopInactivityTimer(userID);
                QBUser user = QbUsersHolder.getInstance().getUserById(userID);
                if (user != null && user.getFullName() != null) {
                    removeUserFromTypingList(user);
                }
            }
        }

        private void removeUserFromTypingList(QBUser user) {
            String userName = user.getFullName();
            if (!TextUtils.isEmpty(userName)) {
                currentTypingUserNames.remove(userName);
            }
            typingStatus.setText(makeStringFromNames());
            if (TextUtils.isEmpty(makeStringFromNames())) {
                typingStatus.setVisibility(View.GONE);
            }
        }

        private void updateTypingInactivityTimer(final String dialogID, final Integer userID) {
            stopInactivityTimer(userID);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d("Typing Status", "User with ID " + userID + " Did not refresh typing status. Processing stop typing");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processUserStopTyping(dialogID, userID);
                        }
                    });
                }
            }, TYPING_STATUS_INACTIVITY_DELAY);
            usersTimerMap.put(userID, timer);
        }

        private void stopInactivityTimer(Integer userID) {
            if (usersTimerMap.get(userID) != null) {
                try {
                    usersTimerMap.get(userID).cancel();
                } catch (NullPointerException ignored) {

                } finally {
                    usersTimerMap.remove(userID);
                }
            }
        }

        private String makeStringFromNames() {
            String result = "";
            int usersCount = currentTypingUserNames.size();
            if (usersCount == 1) {
                String firstUser = currentTypingUserNames.get(0);

                if (firstUser.length() <= 20) {
                    result = firstUser + " " + getString(R.string.typing_postfix_singular);
                } else {
                    result = firstUser.subSequence(0, 19).toString() +
                            getString(R.string.typing_ellipsis) +
                            " " + getString(R.string.typing_postfix_singular);
                }

            } else if (usersCount == 2) {
                String firstUser = currentTypingUserNames.get(0);
                String secondUser = currentTypingUserNames.get(1);

                if ((firstUser + secondUser).length() > 20) {
                    if (firstUser.length() >= 10) {
                        firstUser = firstUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }

                    if (secondUser.length() >= 10) {
                        secondUser = secondUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                }
                result = firstUser + " and " + secondUser + " " + getString(R.string.typing_postfix_plural);

            } else if (usersCount > 2) {
                String firstUser = currentTypingUserNames.get(0);
                String secondUser = currentTypingUserNames.get(1);
                String thirdUser = currentTypingUserNames.get(2);

                if ((firstUser + secondUser + thirdUser).length() <= 20) {
                    result = firstUser + ", " + secondUser + " and " + thirdUser + " " + getString(R.string.typing_postfix_plural);
                } else if ((firstUser + secondUser).length() <= 20) {
                    result = firstUser + ", " + secondUser + " and " + (currentTypingUserNames.size() - 2) + " more " + getString(R.string.typing_postfix_plural);
                } else {
                    if (firstUser.length() >= 10) {
                        firstUser = firstUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                    if (secondUser.length() >= 10) {
                        secondUser = secondUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                    result = firstUser + ", " + secondUser +
                            " and " + (currentTypingUserNames.size() - 2) + " more " + getString(R.string.typing_postfix_plural);
                }
            }
            return result;
        }
    }

/*
    @Override
    public void onResumeFinished() {
        if (ChatHelper.getInstance().isLogged()) {
            if (qbChatDialog == null) {
                qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
            }
            returnToChat();
        } else {
            showProgressDialog(R.string.dlg_loading);
            ChatHelper.getInstance().loginToChat(SharedPrefsHelper.getInstance().getQbUser(), new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    returnToChat();
                    hideProgressDialog();
                }

                @Override
                public void onError(QBResponseException e) {
                    hideProgressDialog();
                    finish();
                }
            });
        }
    }
*/



}
