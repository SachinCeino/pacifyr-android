package com.pacifyr.client.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.login.LoginManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.Payment.AlertAddCreditCardActivity;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.classes.PendingCall;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.event.TwilioCallEvent;
import com.pacifyr.client.fragment.AppointmentsTabViewFragment;
import com.pacifyr.client.fragment.UserDashboardFragment;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.PaymentActivity;
import com.pacifyr.client.home.SessionSummaryActivity;
import com.pacifyr.client.model.ApiResponse;
import com.pacifyr.client.model.AppoinmentNotification;
import com.pacifyr.client.model.BillingSummary;
import com.pacifyr.client.model.Session;
import com.pacifyr.client.model.interfaces.AvailableMinuteListner;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.paytmpayment.Paytm;
import com.pacifyr.client.paytmpayment.TransactionResponce;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.quickblox.utils.PermissionsChecker;
import com.pacifyr.client.quickblox.utils.RingtonePlayer;
import com.pacifyr.client.receiver.NotificationUtils;
import com.pacifyr.client.sign.SignActivity;
import com.pacifyr.client.twilio.TwilioAccessToken;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.twiliochat.TwilioConversationActivity;
import com.pacifyr.client.twiliochat.TwilioConversationCreate;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.GooglePlayServicesHelper;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.SharedPrefsHelper;
import com.pacifyr.client.utils.Toaster;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.CustomFontTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.base.BaseAppCompactActivity;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.Payment.AlertAddCreditCardActivity.REQUEST_CODE_STRIPE_CARD;
import static com.pacifyr.client.network.NetworkService.mCurrentUser;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyr.client.twiliochat.TwilioConversationActivity.DEFAULT_CONVERSATION_NAME;
import static com.pacifyr.client.utils.Constants.getCountryChecks;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.STRIPE_PUBLISH_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.isProd;

/**

 */

public abstract class PacifyrActivity extends BaseAppCompactActivity {

    public static final int APP_UPDATE_REQUEST_CODE = 121;
    private static final int HIGH_PRIORITY_APP_UPDATE = 5;
    private static final Integer DAYS_FOR_FLEXIBLE_APP_UPDATE = 5;
    private static final String TAG = PacifyrActivity.class.getSimpleName();
    public SharedPrefsHelper sharedPrefsHelper;
    private ProgressDialog progressDialog;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    public PermissionsChecker checker;
    private android.app.AlertDialog billSummary;
    private android.app.AlertDialog PaymentPopup;
    private final boolean showRating = false;

    String responce_message;
    String transaction_date;
    String txnAmount;

    public static int errorCount=0;

    @Override
    protected void onResume() {
        super.onResume();
    }

    private final BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String type = intent.getStringExtra("type");
            String id = intent.getStringExtra("id");
            Log.d("receiver", "Got message: " + type);
            if (type != null && type.equals("billing") &&
                    id != null && !id.isEmpty()) {
                new NetworkService().getBillingData(PacifyrActivity.this, id, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        /*
                         *this must be the first line of any httpCallBacks
                         */
                        Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                        try {
                            Log.d(TAG, "URL : " + url);
                            Log.d(TAG, "Resp JSON : " + json);
                            BillingSummary billingSummary = null;
                            if (json != null) {
                                billingSummary = new Gson().fromJson(json.toString(), BillingSummary.class);
                                if (dialog != null) {
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                showBillingSummary(billingSummary);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } /*else if (type != null && type.equals("rating")) {
                if (billSummary != null && billSummary.isShowing()) {
                    showRating = true;
                } else {
                    SessionSummaryActivity.start(PacifyrActivity.this, id);
                }
            }*/
        }
    };

    private boolean showNotification = true;
    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("message")) {
                String message = intent.getExtras().getString("message");
                String type = intent.getExtras().getString("type");
                if (message != null && message != "" && showNotification) {
                    if (type.equals("booking")) {
                        showIOSDialogOK("Message", message, "Close", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                    if (type.equals("confirmBooking") || type.equals("rejectBooking") || type.equals("reminder"))
                    {
                        showIOSDialogOKCancel("Message", message, "View", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                if (getClass().getName().contains("DashboardActivity")) {
                                    showFragment(new AppointmentsTabViewFragment());
                                }
                                else
                                {
                                    DashboardActivity.start(PacifyrActivity.this,true);
                                }
                            }
                        }, "Close");
                    }

                    else if(type.equals("twilio.conversations.new_message") || type.equals("twilioChat") ){


                        String conversation_sid = intent.getExtras().getString("conversation_sid");
                        if(isValid(DEFAULT_CONVERSATION_NAME) && isValid(conversation_sid)
                                && conversation_sid.equals(DEFAULT_CONVERSATION_NAME)){

                            return;
                        }

                        showIOSDialogOKCancel("Message", message, "View", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                Intent intent1 = new Intent(PacifyrActivity.this, TwilioConversationActivity.class);
                                intent1.putExtra("conversation_sid",conversation_sid);
                                startActivity(intent1);
                            }
                        }, "Close");
                    }
                    else
                    {
                        showIOSDialogOKCancel("Message", message, "View", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();

                            }
                        }, "Close");
                    }

                }
            }
        }
    };

    private final BroadcastReceiver generalNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("title") && intent.hasExtra("body")) {
                String title = intent.getExtras().getString("title");
                String body = intent.getExtras().getString("body");
                if (title != null && title != "" && showNotification) {
                    showIOSDialogOK(title, body, "Ok");
                }
            }
        }
    };
    private boolean showDialogs = true;
    private iOSDialog dialog;
    private boolean inProgress;

    private boolean settingUpPayment = false;

    public void makeAbeepSoundWhileShowingPopUp()
    {
       try {
            if (isCallActive) {
                RingtonePlayer ringtonePlayer = new RingtonePlayer(PacifyrActivity.this, R.raw.beep_for_popup);
                ringtonePlayer.play(false);
            }
       }catch (Exception e)
       {

       }
    }
    private final BroadcastReceiver waringToAddPayTmPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (!showDialogs) {
                maximizePicInPicView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                doPaymentSetup(intent);
                            }
                        });
                    }
                }, 2000);
                return;
            } else {
                bringCallActivityToFront();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(intent);
                            }
                        });
                    }
                }, 2000);
            }


        }
    };

    public boolean isSettingUpPayment() {
        return settingUpPayment;
    }

    public void showNotificationPopups(boolean val) {
        this.showNotification = val;
    }


    /* public String getProfileName() {
         return getProfileFName() + " " + getProfileLName();

     }*/
    // public static  String call_emotionid=null;
    public static MEmotion call_emotion = null;
    private final BroadcastReceiver poupForAppoinmetCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.hasExtra("typeId")) {
                String typeId = intent.getExtras().getString("typeId");

                if (isValid(typeId)) {
                    webGetAppointmentDetails(typeId);
                }
            }

        }
    };


    private PendingCall pendingCall;


    /*public void startCall(boolean isVideoCall, long  qbID) {
     *//*if (opponentsAdapter.getSelectedItems().size() > Consts.MAX_OPPONENTS_COUNT) {
            Toaster.longToast(String.format(getString(R.string.error_max_opponents_count),
                    Consts.MAX_OPPONENTS_COUNT));
            return;
        }*//*

        Log.d("jp", "startCall()");
        ArrayList<Integer> opponentsList = new ArrayList<Integer>();
        opponentsList.add((int) qbID);
        //ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        //QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);


        Map<String, String> userInfo = new HashMap<>();
        //   userInfo.put("user", "samsad");
        userInfo.put("lastName", getUserLastName()+"");
        userInfo.put("firstName", getUserFirstName()+"");
        userInfo.put("imageAvathar", getProfileImageURL()+"");
       // userInfo.put("OpponentUserid", oppentid+"");
      //  userInfo.put("sessionId", sessionid+"");

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);
         WebRtcSessionManager.getInstance(this).getCurrentSession().startCall(userInfo);
        PushNotificationSender.sendPushMessage(opponentsList, sharedPrefsHelper.getQbUser().getFullName());



        CallActivity.start(this, false);
        Log.d("jp", "conferenceType = " + conferenceType);
    }*/
    public AvailableMinuteListner listner = new AvailableMinuteListner() {
     /*   @Override
        public void onAvailable(ModelUserProfile results, boolean isVideoCall, long qbid) {
            //startSessionWeb(results,true,qbid);
            *//*if(isValid(results.getQbid()))
                startSessionWeb(results,isVideoCall,results.getQbid());
            else*//*
                startSessionWeb(results,isVideoCall,qbid);
        }*/


        @Override
        public void onAvailable(ModelUserProfile mCurrentUser, MUser pacifyerDetial, boolean isVideoCall) {
            //startSessionWeb(pacifyerDetial,isVideoCall);
            updateProfileStartCallWeb(pacifyerDetial, isVideoCall);
        }
       /* @Override
        public void onAvailable() {
           // startSessionWeb(pacifyerDetial,true,qbID);


        }

        @Override
        public void onAlert(long time) {
         *//*   setAlertStatusInBlue2("Add Card", time+" minutes only left. Add Credit card for further calls.", "Ok", "NO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  startSessionWeb(pacifyerDetial,true,qbID);
                }
            });*//*

        }*/

        /*     @Override
             public void onAlert(final long time,final ModelUserProfile results,final boolean isVideoCall,final long qbid) {
                 setAlertStatusInBlue2("Add Card", time+" minutes only left. Add Credit card for further calls.", "Ok", "NO", new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                      *//*    if(isValid(results.getQbid()))
                       startSessionWeb(results,isVideoCall,results.getQbid());
                    else*//*
                        startSessionWeb(results,isVideoCall,qbid);
                }
            });
        }
*/
        @Override
        public void onAlert(long time, final MUser pacifyerDetial, final boolean isVideoCall) {

            setAlertStatusInBlue2("Add Card", " You have " + time + " minutes of free call time left. Please setup payment to continue. If payment is not set up, the call will be disconnected.", "Ok", "NO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    hideProgress();
                    hideStatus2();
                    updateProfileStartCallWeb(pacifyerDetial, isVideoCall);
                }
            });
            makeAbeepSoundWhileShowingPopUp();
        }

        /*  @Override
          public void onAlert(long time, ModelUserProfile mCurrentUser, boolean isVideoCall) {
              setAlertStatusInBlue2("Add Card", time+" minutes only left. Add Credit card for further calls.", "Ok", "NO", new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                   *//*    if(isValid(results.getQbid()))
                       startSessionWeb(results,isVideoCall,results.getQbid());
                    else*//*
                    startSessionWeb(results,isVideoCall,qbid);
                }
            });
        }
*/
        @Override
        public void onPacifyerAlert(long time, ModelUserProfile mCurrentUser, MUser pacifyerDetial, boolean isVideoCall) {
            updateProfileStartCallWeb(pacifyerDetial, isVideoCall);
          /*  setAlertStatusInBlue2("Pacifyer Free limit crossed ", " Add Credit card for further calls.", "Ok", "NO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//time+" minutes only left. Add Credit card for further calls."
                    AlertAddCreditCardActivity.start(PacifyrActivity.this);
                }
            });*/
        }

        @Override
        public void onNotAvailable(long time) {
            setAlertStatusInBlue2("Add Card", "Your free minutes are expired. Please continue enjoying our services by updating your credit card information", "Ok", "NO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideProgress();
                    hideStatus2();
                    Intent intent = new Intent(NotificationUtils.STRIPCARD_ADDED_WHILE_CALL);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideProgress();
                    hideStatus2();
                    ShowTransactionFaileToUSer("Add payment cancelled");
                }
            });
            makeAbeepSoundWhileShowingPopUp();

        }

        @Override
        public void onException(Exception e) {
            setAlertOKStatusInBlue("Session Expired ", "Please try to Re-Login ", "Ok");
        }

        @Override
        public void onError(String e) {
            setAlertOKStatusInBlue("Error ", "Please try later ", "Ok");
        }


    };

    public void doPaymentSetup(Intent intent) {
        String type = intent.getStringExtra("type");
        String id = intent.getStringExtra("id");
        String message = intent.getStringExtra("message");
        Log.d("receiver", "Got message: " + type);

        if (type != null && type.equals("payment") &&
                id != null && !id.isEmpty()) {
            if (getCountryChecks().contains(getPrefManager().getCountry())) {

                showIOSDialogOKCancel("", message, Utility.getPaymentLabel(PacifyrActivity.this,getPrefManager().getCountry()), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();


                        new NetworkService().sendPaymentFailedDueToSomeReasons(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "paymentFailure", "Transaction cancelled", new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                /*
                                 *this must be the first line of any httpCallBacks
                                 */
                                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                if (json != null) {
                                    Log.w("@@@", "sendCardAddedNotificationFail messages" + json.toString());
                                }
                            }
                        });

                        Bundle args =
                                FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                        args.putString(FirebaseLogging.EC, "Payment setup cancelled");
                        FirebaseLogging.sendLog(PacifyrActivity.this,
                                FirebaseLogging.EVENT_CLIENT_PAYMENT_FAILED,
                                args);
                        dialog.dismiss();
                    }
                }, new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {

                        dialog.dismiss();
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        if (isValid(id)) {

                            if (isValid(VideoActivity.mSessionDetailRoot) && isValid(VideoActivity.mSessionDetailRoot.getPacifyr()) &&
                                    isValid(VideoActivity.mSessionDetailRoot.getPacifyr().getPrice())) {
                                String tempSessionId = id;
                                if (errorCount != 0)
                                    tempSessionId = tempSessionId + "_" + errorCount;
                                generateCheckSum(true,tempSessionId, Integer.parseInt(VideoActivity.mSessionDetailRoot.getPacifyr().getPrice()));
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Session id not valid !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                makeAbeepSoundWhileShowingPopUp();

            } else if (getPrefManager().getCountry().equalsIgnoreCase("US")) {
                setAlertStatusInBlue2("Add Card", message, "Ok", "Cancel",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideProgress();
                                hideStatus2();
                                Intent intent = new Intent(PacifyrActivity.this,
                                        AlertAddCreditCardActivity.class);
                                startActivityForResult(intent, REQUEST_CODE_STRIPE_CARD);

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideProgress();
                                hideStatus2();
                                ShowTransactionFaileToUSer("Add payment cancelled");
                            }
                        });
                makeAbeepSoundWhileShowingPopUp();
            }
        }
    }

    public static Timer trackTimer;
    public static TimerTask trackTimerTask;
    private boolean billingStarted = false;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Register receiver");
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter(NotificationUtils.NOTIFY_INTENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(generalNotificationReceiver,
                new IntentFilter(NotificationUtils.NOTIFY_INTENT_GENERAL));
        LocalBroadcastManager.getInstance(this).registerReceiver(eventReceiver, new IntentFilter(CallEvent.CALL_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(waringToAddPayTmPaymentReceiver, new IntentFilter(NotificationUtils.PAYTM_PAYMENT_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(poupForAppoinmetCall, new IntentFilter(NotificationUtils.APPOINTMENT_EVENT));

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "UnRegister receiver");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(eventReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(generalNotificationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(waringToAddPayTmPaymentReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(poupForAppoinmetCall);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checker = new PermissionsChecker(PacifyrActivity.this);

       /* if(isValid(getPrefManager().getSessionId())) {
            setAlertSessionStop("Stop session","Previous sessson is not closed.","OK");
        }*/

        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();

        loadEmotions();


    }

    private void loadEmotions() {
        if (!isValid(PrefManager.getInstance(PacifyrActivity.this).getAccessToken())) {
            Log.d(TAG, "loadEmotions Error : Not logged in");
            return;
        }
        String emotionJson =
                PrefManager.getInstance(PacifyrActivity.this).getSharedString(PrefManager.JSON_ALL_EMOTIONS, null);

        String positiveEmotionJson =
                PrefManager.getInstance(PacifyrActivity.this).getSharedString(PrefManager.JSON_POSITIVE_EMOTIONS,
                        null);
        String negativeEmotionJson =
                PrefManager.getInstance(PacifyrActivity.this).getSharedString(PrefManager.JSON_NEGATIVE_EMOTIONS,
                        null);

        if (emotionJson == null) {
            NetworkService.webListEmotions(PacifyrActivity.this);
        } else if (modelAllEmotions == null) {
            try {
                JSONObject emotionJsonObj = new JSONObject(emotionJson);
                modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(emotionJsonObj);

            } catch (JSONException je) {
                je.printStackTrace();
            }

        }

        if (positiveEmotionJson == null) {
            NetworkService.webListPositiveEmotions(PacifyrActivity.this);
        } else if (NetworkService.modelListPosEmotions == null) {
            try {
                JSONObject posEmotionJsonObj = new JSONObject(positiveEmotionJson);
                NetworkService.modelListPosEmotions =
                        GsonUtils.getInstance().gsonToMListPositiveEmotions(posEmotionJsonObj);
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        if (negativeEmotionJson == null) {
            NetworkService.webListNegativeEmotions(PacifyrActivity.this);
        } else if (NetworkService.modelListNegEmotions == null) {
            try {
                JSONObject neativeEmotionJsonObj = new JSONObject(negativeEmotionJson);
                NetworkService.modelListNegEmotions =
                        GsonUtils.getInstance().gsonToMListNegativeEmotions(neativeEmotionJsonObj);
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

    }

    public  void permissionCheck(){
        try{
            if(checker==null)
                checker = new PermissionsChecker(PacifyrActivity.this);
            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                startPermissionsActivity(false);
            }

        }catch (Exception e)
        {

        }

    }


    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }


    public void showIOSDialogOK(String title, String subTitle, String okLabel)
    {
        new iOSDialogBuilder(this)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel,new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                })
                .build().show();
        makeAbeepSoundWhileShowingPopUp();
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, iOSDialogClickListener listener)
    {
        if (!isShowDialogs()) {
            return;
        }
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = new iOSDialogBuilder(PacifyrActivity.this)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel, listener)
                .build();
        try {
            dialog.show();
            makeAbeepSoundWhileShowingPopUp();
        }
        catch (Exception e)
        {
            Log.w("###",String.valueOf(e));
        }

    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, iOSDialogClickListener listener,boolean isCancelable)
    {
        if (!isShowDialogs()) {
            return;
        }
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = new iOSDialogBuilder(PacifyrActivity.this)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(isCancelable)
                .setPositiveListener(okLabel, listener)
                .build();
        try {
            dialog.show();
            makeAbeepSoundWhileShowingPopUp();
        }
        catch (Exception e)
        {
            Log.w("###",String.valueOf(e));
        }

    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, boolean cancelable, iOSDialogClickListener listener) {
        if (!isShowDialogs()) {
            return;
        }
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = new iOSDialogBuilder(this)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(cancelable)
                .setPositiveListener(okLabel, listener)
                .build();
        dialog.show();
    }

    public void showIOSDialogOKCancel(String title, String subTitle, String okLabel, iOSDialogClickListener listener, String negativeLabel) {
        if (!isShowDialogs()) {
            return;
        }
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog = new iOSDialogBuilder(this)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel, listener)
                .setNegativeListener(negativeLabel, new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                })
                .build();
        dialog.show();
        makeAbeepSoundWhileShowingPopUp();
    }


    public void showIOSDialogOKCancel(String title, String subTitle, String okLabel, iOSDialogClickListener CancelListener, iOSDialogClickListener listener) {
        if (!isShowDialogs()) {
            return;
        }
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            dialog = new iOSDialogBuilder(this)
                    .setTitle(title)
                    .setSubtitle(subTitle)
                    .setBoldPositiveLabel(false)
                    .setCancelable(false)
                    .setPositiveListener(okLabel, listener)
                    .setNegativeListener("Cancel", CancelListener)
                    .build();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot show Dialog " + e.getMessage());
        }
    }

    public void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }

        progressDialog.setMessage(getString(messageId));

        progressDialog.show();

    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static MSessionDetailRoot call_session_detail_root = null;

    protected abstract View getSnackbarAnchorView();


    public void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }

    public void startPermissionsActivity(boolean checkOnlyAudio, Activity activity) {
        PermissionsActivity.startActivity(activity, checkOnlyAudio, Consts.PERMISSIONS);
    }


    public String getAccessToken() {
        Log.w("@@@",getPrefManager().getAccessToken());
        return getPrefManager().getAccessToken();

    }
    public boolean isCardExist() {
        return getSharedBoolean(PrefManager.CARD_EXIST, false);
    }
    public void saveCardExist(boolean isExist ) {
        putSharedBoolean(PrefManager.CARD_EXIST, isExist);
    }
    /**
     * Shared Preference
     */

    public String getSharedString(String KEY, String defValue) {
        return PrefManager.getInstance(this).getSharedString(KEY, defValue);
    }
    public boolean getSharedBoolean(String KEY, boolean defValue) {
        return PrefManager.getInstance(this).getSharedBoolean(KEY, defValue);
    }

    public void putSharedString(String KEY, String defValue) {
        PrefManager.getInstance(this).putSharedString(KEY, defValue);
    }
    public void putSharedBoolean(String KEY, boolean defValue) {
        PrefManager.getInstance(this).putSharedBoolean(KEY, defValue);
    }
    public String getPrefUDID() {
        return getSharedString(PrefManager.UDID, "");
    }

    public void saveUDID(String valude) {
        putSharedString(PrefManager.UDID, valude);
    }

    public void saveProfileImageURL(String url) {
        putSharedString(PrefManager.PROFILE_IMAGE_URL, url);

    }

    public String getProfileImageURL() {
        return getSharedString(PrefManager.PROFILE_IMAGE_URL, "");

    }

    public String getUserID() {
        return getSharedString(PrefManager.USERID, "");

    }

    public void maximizePicInPicView() {
        if (isCallActive) {
            Intent i = new Intent(PacifyrActivity.this, VideoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        }
    }

    public void bringCallActivityToFront() {
        if (!VideoActivity.isForeground) {
            Log.d(TAG, "bringCallActivityToFront");
            Intent intent = new Intent(this, VideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        } else {
            Log.d(TAG, "bringCallActivityToFront - not in foreground");
        }
    }
    public String getUserFirstName() {
        return getSharedString(PrefManager.UserFirstName, "");

    }

    public String getUserLastName() {
        return getSharedString(PrefManager.UserLastName, "");

    }
    public String getGCMToken() {
        return getSharedString(PrefManager.FCM_TOKEN, "");
    }

    public String getAWSEndPoint() {
        return getSharedString(PrefManager.AWS_ENDPOINT, "");
    }


    /*validations */

   /* public Boolean isValidName(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return true;
        setAlertOKStatusInWhite("Invalid",error,"OK");
        //text.setError(error + "");
        return false;

    }

    public Boolean isValidNameAndmail(EditText name,EditText mail, String error) {
        boolean flag=false,e=false;
        if (name != null)
            if (isValid(name.getText() + ""))
                flag=true;
        if (mail != null)
            if (isValid(mail.getText() + ""))
                if (isValidEmail(mail.getText() + ""))
                    flag=true;
        if(flag){
            return true;
        }
        else {
            setAlertOKStatusInWhite("Invalid", error, "OK");
            //text.setError(error + "");
            return false;
        }

    }

    public Boolean isValidEmail1(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                if (isValidEmail(text.getText() + ""))
                    return true;
        //text.setError(error + "");
        setAlertOKStatusInWhite("Invalid",error,"OK");
        return false;
    }*/

    public void setAlertOKStatusInWhite(String title,String desc,String ok) {
        try {
            if (!isShowDialogs()) {
                return;
            }
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.alert_white_ok, null, false);
            MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
            MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
            MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

            if (isValid(title))
                defSetText(title_dtxv, title);
            else
                title_dtxv.setVisibility(View.GONE);
            defSetText(desc_dtxv, desc);
            ok_dtxv.setText(ok);
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            //setAlertOKStatusInWhite("Register Failed", "Your mobile number is already registered", "Ok");
            ok_dtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();
                    hideProgress();
                }
            });
            makeAbeepSoundWhileShowingPopUp();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }


    public void setAlertPacifyrWillJoinShortly(String title, String desc, String ok, View.OnClickListener onClickListener) {
        try {
            if (!isShowDialogs()) {
                return;
            }
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.alert_white_ok, null, false);
            MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
            MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
            MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

            if (isValid(title))
                defSetText(title_dtxv, title);
            else
                title_dtxv.setVisibility(View.GONE);
            defSetText(desc_dtxv, desc);
            ok_dtxv.setText(ok);
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            //setAlertOKStatusInWhite("Register Failed", "Your mobile number is already registered", "Ok");
            ok_dtxv.setOnClickListener(onClickListener);
            makeAbeepSoundWhileShowingPopUp();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }


    public void setAllreadySessionIsTherNotAbleToCall(String title, String desc, String ok, View.OnClickListener onClickListener) {
        try {
            if (!isShowDialogs()) {
                return;
            }
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.alert_white_ok, null, false);
            MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
            MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
            MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

            if (isValid(title))
                defSetText(title_dtxv, title);
            else
                title_dtxv.setVisibility(View.GONE);
            defSetText(desc_dtxv, desc);
            ok_dtxv.setText(ok);
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            //setAlertOKStatusInWhite("Register Failed", "Your mobile number is already registered", "Ok");
            ok_dtxv.setOnClickListener(onClickListener);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlertAddCreditCardActivity.REQUEST_CODE_ADD_CARD) {
            if (resultCode == RESULT_OK) {
                TransactionResponce transactionResponce = new TransactionResponce();
                transactionResponce.setTransactionResponse("success");
                transactionResponce.setTransactionStatus("success");
                transactionResponce.setTransactionId("success");
                ShowTransactionSuccesResultToUSer(transactionResponce);
            }
            else {
                ShowTransactionFaileToUSer("Add payment failed");
            }

        } else if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d(TAG, "Update flow failed! RFireesult code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
//                checkForAppUpdate(PacifyrActivity.this, APP_UPDATE_REQUEST_CODE);
            }
        }
        else if (requestCode == REQUEST_CODE_STRIPE_CARD)
        {
            if (resultCode == RESULT_OK) {
                ShowTransactionSuccesResultToUSer(getResources().getString(R.string.successfully_done_payment));
            }
            else {
                ShowTransactionFaileToUSer("Add payment failed");
            }
        }
    }


    private void showBillingSummary(final BillingSummary billingSummary) {
        if (billSummary != null && billSummary.isShowing()) {
            billSummary.dismiss();
        }
        String date = Utility.getStartFormatDate(billingSummary.getSession().getCreatedAt(), getApplicationContext());
        String time = Utility.getDate(billingSummary.getSession().getCreatedAt(), true);
        View view = LayoutInflater.from(PacifyrActivity.this).inflate(R.layout.layout_bill_summary, null);
        TextView ok = view.findViewById(R.id.button_ok);
        TextView text_date = view.findViewById(R.id.text_date);
        TextView text_session_start_time = view.findViewById(R.id.text_session_start_time);
        TextView text_session_end_time = view.findViewById(R.id.text_session_end_time);
        TextView text_provider = view.findViewById(R.id.text_provider);
        TextView text_amount_charged = view.findViewById(R.id.text_amount_charged);
        TextView title = view.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.billing_summary));




        PrefManager prefM = PrefManager.getInstance(getApplicationContext());
        String country = prefM.getCountry();
        String sign = "$";
        if (country.equals("India")) {
            sign = getApplicationContext().getResources().getString(R.string.rupee);
        } else {
            sign = getApplicationContext().getResources().getString(R.string.dollar);
        }
        try {
            text_provider.setText(billingSummary.getSession().getPacifyr().getFirstName());
           // text_date.setText(date.substring(0, date.indexOf(' ')));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat targetFormat;
            if (getPrefManager().getCountry().equals("India")) {
                targetFormat = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a", Locale.ENGLISH);
            } else {
                targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
            }
            String strDate = targetFormat.format(c.getTime());
            strDate.replace("AM", "am").replace("PM","pm");
            text_date.setText(date.substring(0, date.indexOf(' ')));


            String startTime=Utility.getStartFormatDate(billingSummary.getStartTime(), getApplicationContext());
            String endTime=Utility.getStartFormatDate(billingSummary.getEndTime(), getApplicationContext());
            text_session_start_time.setText(getOnlyTime(startTime));
            text_session_end_time.setText(getOnlyTime(endTime));


            if (isValid(billingSummary.getSession()) && isValid(billingSummary.getSession().getTimeSlotBookingId()))
            text_amount_charged.setText(sign + billingSummary.getSessionCost() +getResources().getString(R.string.prepaid));
            else
                text_amount_charged.setText(sign + billingSummary.getSessionCost()+" ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billSummary != null) {
                    billSummary.dismiss();
                }

                //SessionSummaryActivity.start(getApplicationContext(), billingSummary.getSessionId());
                getUnrated(billingSummary.getSessionId());
                /*if (showRating) {
                    showRating = false;
                    if (isValid(billingSummary.getSession()) && isValid(billingSummary.getSession().getTimeSlotBookingId()))
                    { //dont show session summary for scheduled call once provider completed from appointment then user get notified the session summmary
                         }
                    else {
                        SessionSummaryActivity.start(getApplicationContext(), billingSummary.getSessionId());
                    }
                }*/
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PacifyrActivity.this);
        //builder.setTitle("Payment summary");
        builder.setView(view);
        builder.setCancelable(false);
        billSummary = builder.create();
        billSummary.show();
        makeAbeepSoundWhileShowingPopUp();
    }

    private String getOnlyTime(String endTime) {
        String[] separated = endTime.split(" ");

       return separated[2].toLowerCase()+" "+separated[3].toLowerCase()+"";
    }

    /* public String getProfileFName() {
         return getSharedString(PrefManager.FNAME, "");

     }

    */

    public void getUnrated(String sessionId) {
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                /*
                 *this must be the first line of any httpCallBacks
                 */
                Auth.httpStatusFilter(PacifyrActivity.this,status.getCode());
                try {
                    if (json != null) {
                        JSONArray results = json.getJSONArray("results");
                        if (results != null && results.length() > 0) {
                            Session session = new Gson().fromJson(results.getJSONObject(0).toString(), Session.class);
                            if (session != null) {
                                SessionSummaryActivity.start(PacifyrActivity.this, session.getId());

                            } else {
                                Log.d(TAG, " Unrated : Session object not available");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, " Unrated : " + e.getMessage());
                    // hideProgress();
                }
            }
        };
        new NetworkService().unratedSessions(cb);
    }


    public String getUserName() {
        return getUserFirstName() + " " + getUserLastName();

    }

    public void startCall(String roomName,String TwilioToken ,boolean isVideoCall,final MUser pacifyer, long  qbID,String oppentid,String idsession,boolean isAppointment,String timeSloteBookingId) {



       /* TwilioCallEvent mTwilioCallEvent=new TwilioCallEvent();
        mTwilioCallEvent.setCallConnected(true);
        mTwilioCallEvent.setIdsession(idsession);
        mTwilioCallEvent.setPacifyer(pacifyer);
        mTwilioCallEvent.setRoomName(roomName);
        mTwilioCallEvent.setVideoCall(isVideoCall);
        EventBus.getDefault().post(mTwilioCallEvent);*/
        Log.w("WWWWW","startCall");

       /* if (isValid(pacifyer.getForwardToPacifyrId())&& isValid(pacifyer.getForwardToPacifyr()))
        {
            if (isValid(pacifyer.getForwardToPacifyr().getQbid()))
            {
                qbID=pacifyer.getForwardToPacifyr().getQbid();
            }

        }
        sessionid=idsession;
        Log.d("call", "startCall sessionid"+sessionid);
        final ArrayList<Integer> opponentsList = new ArrayList<Integer>();
        opponentsList.add((int) qbID);
        //ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(PacifyrActivity.this);

        //QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        Map<String, String> userInfo = new HashMap<>();
        //   userInfo.put("user", "samsad");
        userInfo.put("lastName", getUserLastName()+"");
        userInfo.put("firstName", getUserFirstName()+"");
        userInfo.put("imageAvathar", getProfileImageURL()+"");
        userInfo.put("OpponentUserid", getUserID()+"");
        userInfo.put("sessionId", idsession + "");

        Log.d("call", "startCall lastName"+getUserLastName());
        Log.d("call", "startCall firstName"+getUserFirstName());
        Log.d("call", "startCall sessionId" + idsession);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        newQbRtcSession.startCall(userInfo);*/



        // WebRtcSessionManager.getInstance(this).getCurrentSession().startCall(userInfo);
        new Handler().postDelayed(new Runnable()
        {
            @Override public void run() {
                // onceBackpressed = false;
                ToastHandler.newInstance(PacifyrActivity.this).showToast("call started");
                //PushNotificationSender.sendPushMessage(opponentsList, getUserName());
                // PushNotificationSender.sendPushMessage(opponentsList, sharedPrefsHelper.getQbUser().getFullName());
            }
        }, 2000);

        /*//convertModel()

        CallActivity.mPacifyr = pacifyer;
        WebRtcSessionManager sessionManager = WebRtcSessionManager.getInstance(this);
        QBRTCSession currentSession = sessionManager.getCurrentSession();
        *//** Sending Notificarion to the IOS App to get Out side Call Notification **//*
        new NetworkService().sendCallNotificationToIOSDevices(currentSession.getSessionID(),isVideoCall,sessionid, qbID, getUserName());
        CallActivity.start(this, false);
        Log.d("jp", "conferenceType = " + conferenceType);*/
    }

    public void startChat(final MUser pacifyer)
    {
        startTwilioChat(pacifyer);
           /* if (!Utility.checkPermission(PacifyrActivity.this, "write")) {
                Log.d(TAG, "Write storage permission required!");
                return;
            }*/
            // setLoading("Creating Chat");
         /*   setLoading();
            showProgress();

            QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
            pagedRequestBuilder.setPage(1);
            pagedRequestBuilder.setPerPage(50);

            final  ArrayList<Integer> opponentsList = new ArrayList<Integer>();
            ArrayList<Integer> usersIds = new ArrayList<Integer>();
            //  usersIds.add(getUserID());
            if (isValid(pacifyer.getForwardToPacifyrId())&& isValid(pacifyer.getForwardToPacifyr()))
            {
                if (isValid(pacifyer.getForwardToPacifyr().getQbid()))
                {

                    usersIds.add((int)pacifyer.getForwardToPacifyr().getQbid());
                }

            }
            else
            {
                if (pacifyer.getQbid() <= 0) {
                    hideProgress();
                    Toast.makeText(PacifyrActivity.this, "Sorry, cannot chat with this Pacifyr. " +
                            "Reason : Invalid qbId", Toast.LENGTH_LONG).show();
                    return;
                }
                usersIds.add((int) pacifyer.getQbid());
            }


            QBUsers.getUsersByIDs(usersIds,pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    // createChatDialog(pacifyer,qbUsers);
                    // createChatDialog(pacifyer,qbUsers);
                    qbUsers.add(ChatHelper.getCurrentUser());
                    hideProgress();
                    if (isPrivateDialogExist(qbUsers)) {
                        qbUsers.remove(ChatHelper.getCurrentUser());
                        QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(qbUsers.get(0));
                        // isProcessingResultInProgress = false;
                        // isProcessingResultInProgress = false;

                        ChatActivity.start(PacifyrActivity.this, existingPrivateDialog,pacifyer);
                        //ChatActivity.start(this,DialogsActivity. REQUEST_DIALOG_ID_FOR_UPDATE, existingPrivateDialog);
                    } else {
                        //ProgressDialogFragment.show(getSupportFragmentManager(), com.quickblox.sample.chat.R.string.create_chat);
                        createChatDialog(pacifyer,qbUsers);
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    hideProgress();

                    showErrorSnackbar(0, e, null);
                }
            });
*/

    }
    public void startTwilioChat(final MUser pacifyer){
        setLoading();
        showProgress();

        PrefManager prefM= PrefManager.getInstance(PacifyrActivity.this);
        String userToken=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = NetworkService.GLOBAL_URL+"messages/create";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    Log.d("TWILIO", "Response from server: " + json);
                    Gson gson = new Gson();
                    if (isValid(json))
                    {
                        TwilioConversationCreate mTwilioConversationCreate = gson.fromJson(json.toString(), TwilioConversationCreate.class);
                        if (isValid(mTwilioConversationCreate) && isValid(mTwilioConversationCreate.getTwilioConversationId()))
                        {

                            TwilioConversationActivity.start(PacifyrActivity.this,mTwilioConversationCreate);
                        }

                    }


                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(PacifyrActivity.this);
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",userToken);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("pacifyrId", pacifyer.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }



    public void handlerLogoutFinish(String s) {
        try {
            setStatus(s);
            showStatus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //  setStatus("Logout",R.color.color_blue_accent);
                    //  showStatus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearSharedAll();
                            clearServices();
                            LoginManager mLoginManager = LoginManager.getInstance();
                            if (isValid(mLoginManager))
                                mLoginManager.logOut();
                            hideStatus();
                            //   NetworkService.GLOBALURL=NetworkService.SIGN_GLOBALURL;
                            showSignActivty();
                        }
                    }, 500);
                    hideStatus();
                    finish();
                }
            }, 2000);

        } catch (Exception e) {

        }
    }

    public static MSessionRoot call_session_root = null;
    // public static MConnectSessionRoot call_session_detail_root=null;


    public static String sessionid = null;

    /*
        private void startCall(boolean isVideoCall) {
            try {
                //Set conference type
                //There are two types of calls:
                // - QB_CONFERENCE_TYPE_VIDEO - for video call;
                // - QB_CONFERENCE_TYPE_AUDIO - for audio call;
                QBRTCTypes.QBConferenceType qbConferenceType = QB_CONFERENCE_TYPE_VIDEO;
                LogUtils.LOGD("jp","BVCA startCall start ");

                //Initiate opponents list
                List<Integer> opponents = new ArrayList<Integer>();
                opponents.add((int) qbID); //12345 - QBUser ID

                //Set user information
                // User can set any string key and value in user info
                // Then retrieve this data from sessions which are returned in callbacks
                // and parse them as he wish
                Map<String, String> userInfo = new HashMap<>();
                //   userInfo.put("user", "samsad");
                userInfo.put("user", qbID+"");
                //Init session
                QBRTCSignalingCallback signalingCallback = new QBRTCSignalingCallback(){
                    public void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD packetType, Integer opponentId){
                        LogUtils.LOGD("jp","BVCA onCreate  onSuccessSendingPacket");
                    }
                    public void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD packetType, Integer opponentId,
                                                     QBRTCSignalException e){
                        LogUtils.LOGD("jp","BVCA onCreate  onErrorSendingPacket"+e.getMessage());
                    }
                };


                QBRTCSession session = QBRTCClient.getInstance(this).createNewSessionWithOpponents(opponents, qbConferenceType);
                LogUtils.LOGD("jp","BVCA startCall midddle");
                session.addSignalingCallback(signalingCallback);
                //session.removeSignalingCallback(signalingCallback);


                //Start call
                session.startCall(userInfo);
                LogUtils.LOGD("jp","BVCA startCall end");

                session.addVideoTrackCallbacksListener(this);
            }
            catch (Exception ex)
            {
                LogUtils.LOGD("jp","BVCA startCall exce "+ ex.getMessage());
                ex.printStackTrace();
            }
        }*/
    public Boolean isValidLogout(String text, String error) {
        if (text != null)
            if (!text.trim().equalsIgnoreCase(""))
                return true;

        handlerLogoutFinish("" + error);
        return false;

    }

    public void handlerFinish(String s) {
        try{
            setStatus(s,R.color.white,R.color.Color_Crimson);
            showStatus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideStatus();
                    finish();
                }
            }, 3000);

        }catch (Exception e)
        {

        }
    }

    public void getSavedCardWebChat( final MUser pacifyerDetial) {
        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb " );
            PrefManager prefM= PrefManager.getInstance(PacifyrActivity.this);
            if (!isValidLogout(getUserID(),"Session expired, please re login")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }
            setLoadingWait();
            showProgress();
            String url= NetworkService.GLOBAL_URL+"payment/savedcard/"+getUserID();
            AjaxCallback cb = new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Intent i = new Intent(PacifyrActivity.this, PaymentActivity.class);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        };
                        hideProgress();
                        LogUtils.LOGD("session", "PA getSavedCardWeb callbaack "+json );
                        if(isValid(json)) {
                            String jsonString = json.toString();
                            int l=jsonString.length();
                            if(l>3) {
                                LogUtils.LOGD("session", "PA saveCardExist true " );
                                saveCardExist(true);
                                callChatSubScription(pacifyerDetial);

                                //listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                            }
                            else{
                                saveCardExist(false);
                                showIOSDialogOK("","A payment method needs to be set up.",Utility.getPaymentLabel(PacifyrActivity.this,getPrefManager().getCountry()),listener);
                            }
                        }else{
                            saveCardExist(false);
                            showIOSDialogOK("","A payment method needs to be set up.",Utility.getPaymentLabel(PacifyrActivity.this,getPrefManager().getCountry()),listener);
                        }
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        saveCardExist(false);
                        //showIOSDialogOK("","A payment method needs to be set up.","Set up Payment",listener);

                    }
                }
            };
            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",getAccessToken());
            aq.ajax(url,JSONArray.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            listner.onException(ex);
            showIOSDialogOK("","Please try again","OK");
            LogUtils.LOGD("session","PA getSavedCardWeb exce " + ex.getMessage());
        }
    }

    private void callChatSubScription(final MUser pacifyr) {
        try{
            String url = NetworkService.GLOBAL_URL+"payment/chargeChat";
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();
            /*String userid = prefM.getUserID();*/
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.toString();
                        jsonS+="sam";
                        //startChat(pacifyr);
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                startChat(pacifyr);
                                dialog.dismiss();
                            }
                        };
                        showIOSDialogOK("","Subscribed to Chat","OK",listener);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", prefM.getUserID());
            jsonObject.putOpt("pacifyrId", pacifyr.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getSavedCardWeb( final MUser pacifyerDetial,final AvailableMinuteListner listner, final boolean isVideoCall) {
        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb " );
            PrefManager prefM= PrefManager.getInstance(PacifyrActivity.this);
            if (!isValidLogout(getUserID(),"Session expired, please re login")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }
            setLoadingWait();
            showProgress();
            String url = NetworkService.GLOBAL_URL + "payment/savedcard/" + getUserID();
            AjaxCallback cb = new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        hideProgress();
                        LogUtils.LOGD("session", "PA getSavedCardWeb callbaack " + json);
                        if (isValid(json)) {
                            String jsonString = json.toString();
                            int l = jsonString.length();
                            if (l > 3) {
                                LogUtils.LOGD("session", "PA saveCardExist true ");
                                saveCardExist(true);
                                listner.onAvailable(NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);
                            } else {
                                saveCardExist(false);
                                callCardCheck(pacifyerDetial, listner, isVideoCall);
                            }
                        } else {
                            saveCardExist(false);
                            callCardCheck(pacifyerDetial, listner, isVideoCall);
                        }
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        saveCardExist(false);
                        callCardCheck(pacifyerDetial, listner, isVideoCall);
                    }
                }
            };
            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            aq.ajax(url, JSONArray.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            listner.onException(ex);

            LogUtils.LOGD("session","PA getSavedCardWeb exce " + ex.getMessage());
        }
    }

    private void callCardCheck(final MUser pacifyerDetial,final AvailableMinuteListner listner, final boolean isVideoCall) {
        try {
            if(NetworkService.mCurrentUser.getAvailableMinutes() >= 10)
                listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
            else if(NetworkService.mCurrentUser.getAvailableMinutes() < 10 && NetworkService.mCurrentUser.getAvailableMinutes() >= 5)
                listner.onAlert(NetworkService.mCurrentUser.getAvailableMinutes(), pacifyerDetial, isVideoCall);
/*            else if(pacifyerDetial.getAvailableMinutes() < 5)
                listner.onPacifyerAlert(pacifyerDetial.getAvailableMinutes(), NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);*/
            else if(NetworkService.mCurrentUser.getAvailableMinutes() < 5)
                listner.onNotAvailable(NetworkService.mCurrentUser.getAvailableMinutes());
        }catch (Exception e)
        {
            hideProgress();
            listner.onException(e);

        }
    }


    private void updateProfileStartCallWeb( final MUser pacifyerDetial, final boolean isVideoCall) {
        try {
            //only for leaders up
            // EmotionSpecialityAdapter.reason_forcall= UserDashboardFragment.LeadersUpReasonFortheCall;
            PostRegisterModel postRegisterModel=null;
            if (getPackageName().equals("com.pacifyr.pam"))
            {
                postRegisterModel = new PostRegisterModel();
            }
            else
            {
                postRegisterModel = new PostRegisterModel(PacifyrActivity.call_emotion.getId(), EmotionSpecialityAdapter.reason_forcall);
            }
            if (!isValidLogout(getUserID(),"Session expired, please re login"))
                return;
            setLoadingWait();
            showProgress();
            Gson gson = new Gson();
            String json = gson.toJson(postRegisterModel);
            hideKeyboard();
            String url = GLOBAL_URL + "user/editprofile/" + getUserID();
            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback= new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    hideProgress();
                    hideKeyboard();
                    try {
                        if (getPackageName().equals("com.pacifyr.pam")) {
                            if (isValid(PacifyrActivity.call_emotion))
                                NetworkService.mCurrentUser.setEmotionId(PacifyrActivity.call_emotion.getId());
                            if (isValid(EmotionSpecialityAdapter.reason_forcall))
                                NetworkService.mCurrentUser.setReasonForTheCall(EmotionSpecialityAdapter.reason_forcall);
                        } else
                        {
                            NetworkService.mCurrentUser.setEmotionId(PacifyrActivity.call_emotion.getId());
                            NetworkService.mCurrentUser.setReasonForTheCall(EmotionSpecialityAdapter.reason_forcall);
                        }
                        getPrefManager().savePacifyrID(pacifyerDetial.getId());
                        if (NetworkService.mCurrentUser.getAvailableMinutes() > 0 && isEmployee()) {
                            setAlertOKStatusInBlue("", "You have " + NetworkService.mCurrentUser.getAvailableMinutes() + " free minutes available", "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideStatus();
                                    startTwilioCall(isVideoCall, pacifyerDetial, false, null);
                                }
                            });
                        } else {
                            startTwilioCall(isVideoCall, pacifyerDetial, false, null);
                        }
                        //  startSessionWeb
                    /*    if (isValid(result)) {

                            //final JSONObject jsonObject = new JSONObject(result.toString());

                           // saveInformation(jsonObject);
                        }
                        else
                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast( " Please try again ");
                              */
                    } catch (Exception eee) {
                        hideProgress();
                        // com.pacifyr.pacifyrapp.utils.ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Error :"+eee.getMessage());

                    }
                }


            };
            Ion.with(PacifyrActivity.this).load(url).setHeader("AccessToken", getAccessToken()).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
        } catch (Exception ex) {
            hideProgress();
            LogUtils.LOGD("session", "PA updateProfileWeb exce " + ex.getMessage());
        }
    }

    public void startTwilioCall(boolean isVideoCall, MUser pacifyerDetial, boolean isAppointment, String timeSlotBookingId) {
        boolean hasPermissions = false;
        if (isVideoCall) {
            hasPermissions = Utility.checkPermission(PacifyrActivity.this, "videocall");
        } else {
            hasPermissions = Utility.checkPermission(PacifyrActivity.this, "audiocall");
        }
        if (hasPermissions) {
            createAccesTokenForTwilio(pacifyerDetial, isVideoCall, isAppointment, timeSlotBookingId);
        } else {
            hideStatus2();
            pendingCall = new PendingCall(pacifyerDetial, isVideoCall, isAppointment, timeSlotBookingId);
        }
    }

    public boolean isEmployee() {
        String role = PrefManager.getInstance(this).getCustomerRole();
        if (role.equals("") || !role.equals("employee")) {
            return false;
        } else return role.equals("employee");
    }

    private void createAccesTokenForTwilio(MUser pacifyerDetial, boolean isVideoCall, boolean isAppointment, String timeSlotBookingId) {
        try {
            String url = NetworkService.ACCESS_TOKEN_SERVER_URL;
            String token = getPrefManager().getAccessToken();
            String roomName= UUID.randomUUID().toString() ;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject jsonObject, AjaxStatus status) {


                    try {
                        if (isValid(jsonObject))
                        {
                            TwilioAccessToken mTwilioAccessToken=GsonUtils.getInstance().gsonToTwilioAccessToken(jsonObject);
                            if (isValid(mTwilioAccessToken) && mTwilioAccessToken.getStatus().equals("success") &&isValid(mTwilioAccessToken.getMessage())) {

                                    if (isAppointment) {
                                       // startSessionForAppointmentCallAfterConnectingTwilio(roomName, mTwilioAccessToken.getMessage(), pacifyerDetial, isVideoCall, timeSlotBookingId);
                                        VideoActivity.start(getApplicationContext(),mTwilioAccessToken.getMessage(),roomName,pacifyerDetial,isVideoCall,isAppointment,timeSlotBookingId);
                                    }else{
                                       // startSessionWeb(roomName, mTwilioAccessToken.getMessage(), pacifyerDetial, isVideoCall);
                                        VideoActivity.start(getApplicationContext(),mTwilioAccessToken.getMessage(),roomName,pacifyerDetial,isVideoCall,isAppointment,null);
                                    }

                            }
                            else
                            {
                                finish();
                            }
                        }
                        else
                        {
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("roomName", roomName);
            aq.post(url, jsonObject, JSONObject.class, cb);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void rejoinToTwilioRoom(MUser pacifyerDetial, boolean isVideoCall,boolean isAppointment,String timeSlotBookingId,String roomName,String sessionId) {
        try{
            String url = NetworkService.ACCESS_TOKEN_SERVER_URL;
            String token = getPrefManager().getAccessToken();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject jsonObject, AjaxStatus status) {


                    try {
                        if (isValid(jsonObject))
                        {
                            TwilioAccessToken mTwilioAccessToken=GsonUtils.getInstance().gsonToTwilioAccessToken(jsonObject);
                            if (isValid(mTwilioAccessToken) && mTwilioAccessToken.getStatus().equals("success") &&isValid(mTwilioAccessToken.getMessage())) {
                                VideoActivity.start(getApplicationContext(),mTwilioAccessToken.getMessage(),roomName,pacifyerDetial,isVideoCall,isAppointment,timeSlotBookingId,true,sessionId);
                            }
                            else
                            {
                                finish();
                            }
                        }
                        else
                        {
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("roomName", roomName);
            aq.post(url, jsonObject, JSONObject.class, cb);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void startSessionWeb(String roomName,String roomSID,final MUser pacifyer, final boolean isVideoCall,String timeSlotBookingId) {
        try {

            ToastHandler.newInstance(PacifyrActivity.this).showToast("startSessionWeb");
            call_session_root=null;
            call_session_detail_root=null;
            setLoading();
            showProgress();
            String roomType=isVideoCall?"video":"audio";
            LogUtils.LOGD("call", "callNotifyWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "callNotifyWeb getAccessToken() "+getAccessToken());
            if (!isValid(getUserID(),"Session Expired, Please Re login"))
                return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;
            if (isValid(pacifyer,"Session Expired, Please Re login")) {
                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        hideProgress();
                        if (json == null) {
                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
                            LogUtils.LOGD("call", "startSessionWeb json null ");
                        } else {
                            if (json != null) {
                                try {
                                    LogUtils.LOGD("call", "startSessionWeb json  " + json);
                                    call_session_root= GsonUtils.getInstance().gsonToMSessionRoot(json);
                                    String sessionJson = null;
                                    if (json.has("session")) {
                                        sessionJson = json.getString("session");
                                    }
                                    Session session = null;
                                    if (sessionJson != null) {
                                        session = new Gson().fromJson(sessionJson, Session.class);
                                    }
                                    if(isValid(call_session_root))
                                    {

                                        if(call_session_root.getStatus().equalsIgnoreCase("success"))
                                        {
                                            if (session != null) {
                                                startSessionTracking(session.getId());
                                            }
                                            TwilioCallEvent mTwilioCallEvent=new TwilioCallEvent();
                                            mTwilioCallEvent.setIdsession(session.getId());
                                            EventBus.getDefault().post(mTwilioCallEvent);
                                            //startCall(roomName[0],TwilioToken,isVideoCall,pacifyer,pacifyer.getQbid(), pacifyer.getId(),call_session_root.getSession().getId(),false,null);
                                        }
                                        else
                                        {
    //                                        stopSessionWeb(pacifyer);
                                            if (json.has("message")) {

                                                TwilioCallEvent mTwilioCallEvent=new TwilioCallEvent();
                                                mTwilioCallEvent.setError(json.getString("message"));
                                                EventBus.getDefault().post(mTwilioCallEvent);

                                            }

                                            //setAlertOKStatusInBlue("Connect call","Connect the call","OK");
                                            //startCall(isVideoCall,pacifyer, pacifyer.getQbid(), pacifyer.getId(), sessionid);

                                            //ToastHandler.newInstance(PacifyrActivity.this).mustShowToast(call_session_root.getMessage());
                                        }

                                    }
                                    else {
                                        try {

                                            JSONObject sessionobj = json.getJSONObject("session");
                                            String sessionid= sessionobj.getString("id");
                                            String message = sessionobj.has("message") ? sessionobj.getString("message") : "Sorry, some error occurred";
                                            String statusmsg= json.getString("status");
                                            if(statusmsg.equalsIgnoreCase("success")) {
                                                if (isValid(sessionid, "Please try later ,session expired ")) {
                                                   // startCall(roomName[0],TwilioToken,isVideoCall, pacifyer, pacifyer.getQbid(), pacifyer.getId(), sessionid,false,null);
    //                                                startSessionTracking();
                                                }
                                            } /*else if (statusmsg.equalsIgnoreCase("error")) {
                                                stopSessionWeb(pacifyer);
                                                ToastHandler.newInstance(PacifyrActivity.this).mustShowToast(message);
                                            } else {
                                                stopSessionWeb(pacifyer);
                                                //setAlertOKStatusInBlue("Connect call","Connect the call","OK");
                                                //startCall(isVideoCall,pacifyer, pacifyer.getQbid(), pacifyer.getId(), sessionid);
                                                //ToastHandler.newInstance(PacifyrActivity.this).mustShowToast(json.getString("message"));
                                                ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Network error. Please try again");
                                            }*/

                                        }catch (Exception e)
                                        {
                                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
                                            // e.printStackTrace();
                                        }
                                    }

                                } catch (Exception e){
                                    try {
                                        LogUtils.LOGD("call", "startSessionWeb exception : " + e.getMessage());
                                        JSONObject sessionobj = json.getJSONObject("session");
                                        String sessionid= sessionobj.getString("id");
                                        String statusmsg= json.getString("status");
                                        String message = sessionobj.has("message") ? sessionobj.getString("message") : "Sorry, some error occurred";
                                        if(statusmsg.equalsIgnoreCase("success")) {
                                            if (isValid(sessionid, "Please try later session expired")) {
                                                //startCall(roomName[0],TwilioToken,isVideoCall, pacifyer, pacifyer.getQbid(), pacifyer.getId(), sessionid,false,null);
    //                                            startSessionTracking();
                                            }
                                        } /*else if (statusmsg.equalsIgnoreCase("error")) {
                                            stopSessionWeb(pacifyer);
                                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast(message);
                                        } else {
                                            stopSessionWeb(pacifyer);
                                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Network error. Please try again");
                                            //setAlertOKStatusInBlue("Connect call","Connect the call","OK");
                                            //startCall(isVideoCall,pacifyer, pacifyer.getQbid(), pacifyer.getId(), sessionid);
                                            // ToastHandler.newInstance(PacifyrActivity.this).mustShowToast(json.getString("message"));
                                        }*/

                                    }catch (Exception ex)
                                    {
                                        ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");

                                    }
                                }
                            }
                        }
                    }
                };
                AQuery aq = new AQuery(this);
                //cb.header("support-app-id", "appid");

                //cb.header("support-app-key", "appkey");
                cb.header("AccessToken", getAccessToken());
                cb.header("Content-Type", "application/json");

                JSONObject input = new JSONObject();
                input.putOpt("userId", getUserID());
                input.putOpt("pacifyrId", pacifyer.getId());
                //     input.putOpt("deviceType", Constants.DEVICETYPE);
                input.putOpt("sessionType", roomType);
                input.putOpt("twilioRoomSID", roomSID);
                input.putOpt("twilioRoomName", roomName);
                if (isValid(timeSlotBookingId))
                input.putOpt("timeSlotBookingId",timeSlotBookingId);
                input.putOpt("appSource", Constants.getAppSource(getApplicationContext()));
                if (isValid(EmotionSpecialityAdapter.reason_forcall))
                    input.putOpt("reasonForTheCall", EmotionSpecialityAdapter.reason_forcall);
                else if (isValid(getPrefManager().getReasonFortheCall()))
                    input.putOpt("reasonForTheCall",getPrefManager().getReasonFortheCall());
                else if (isValid(mCurrentUser.getSelectedSpecialityCategory()))
                    input.putOpt("reasonForTheCall",mCurrentUser.getSelectedSpecialityCategory());
                aq.post(GLOBAL_URL + "session/start", input, JSONObject.class, cb);
            }
        } catch (Exception e2){
            hideProgress();
            call_session_root=null;
            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
        }


    }



    public void startSessionWebforAppoinmetCall(final MUser pacifyer, final boolean isVideoCall,String timeSlotBookingId) {
        startTwilioCall(isVideoCall, pacifyer, true, timeSlotBookingId);
//        createAccesTokenForTwilio(pacifyer,isVideoCall,true,timeSlotBookingId);
    }

    public void startSessionForAppointmentCallAfterConnectingTwilio(String roomNam,String TwilioToken,final MUser pacifyer, final boolean isVideoCall,String timeSlotBookingId){


    }

  /*  public void startSessionWeb(final ModelUserProfile results, final boolean isVideoCall, final long qbid) {
        try {
            ToastHandler.newInstance(PacifyrActivity.this).showToast("startSessionWeb");
            call_session_root=null;
            call_session_detail_root=null;
            setLoading();
            showProgress();
            String roomType=isVideoCall?"video":"audio";
            LogUtils.LOGD("call", "callNotifyWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "callNotifyWeb getAccessToken() "+getAccessToken());
            if (!isValid(getUserID(),"Session Expired, Please Re login"))
                return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;
            if (isValid(results,"Session Expired, Please Re login")) {
                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        hideProgress();
                        if (json == null) {
                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
                            LogUtils.LOGD("call", "startSessionWeb json null ");
                        } else {
                            if (json != null) try {
                                LogUtils.LOGD("call", "startSessionWeb json  " + json);
                                call_session_root= GsonUtils.getInstance().gsonToMSessionRoot(json);
                               if(isValid(call_session_root))
                               {
                                   if(call_session_root.getStatus().equalsIgnoreCase("success"))
                                   {

                                       startCall(isVideoCall,qbid, results.getId(),call_session_root.getSession().getId());
                                   }
                                   else
                                   {
                                       ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Error : "+call_session_root.getMessage());
                                   }

                               }
                               else {
                                   try {

                                       JSONObject sessionobj = json.getJSONObject("session");
                                       String sessionid= sessionobj.getString("id");
                                       String statusmsg= json.getString("status");
                                       if(statusmsg.equalsIgnoreCase("success")) {
                                           if (isValid(sessionid,"Please try later ,session expired "))
                                               startCall(isVideoCall, qbid, results.getId(), sessionid);
                                       }
                                       else
                                       {
                                           ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Error : "+json.getString("message"));
                                       }

                                   }catch (Exception e)
                                   {
                                       ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
                                      // e.printStackTrace();
                                   }
                               }

                            } catch (Exception e){
                                    try {
                                        LogUtils.LOGD("call", "startSessionWeb exception : " + e.getMessage());
                                         JSONObject sessionobj = json.getJSONObject("session");
                                         String sessionid= sessionobj.getString("id");
                                         String statusmsg= json.getString("status");
                                        if(statusmsg.equalsIgnoreCase("success")) {
                                            if (isValid(sessionid,"Please try later session expired"))
                                                startCall(isVideoCall, qbid, results.getId(), sessionid);
                                        }
                                        else
                                        {
                                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Error : "+json.getString("message"));
                                        }

                                      }catch (Exception ex)
                                      {
                                          ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");

                                       }
                                 }
                        }
                    }
                };
                AQuery aq = new AQuery(this);
                //cb.header("support-app-id", "appid");
                //cb.header("support-app-key", "appkey");
                cb.header("AccessToken", getAccessToken());
                cb.header("Content-Type", "application/json");

                JSONObject input = new JSONObject();
                input.putOpt("userId", getUserID());
                input.putOpt("pacifyrId", results.getId());
                //     input.putOpt("deviceType", Constants.DEVICETYPE);
                input.putOpt("sessionType", roomType);
                aq.post(GLOBAL_URL + "session/start", input, JSONObject.class, cb);
            }
        } catch (Exception e2){
            hideProgress();
            call_session_root=null;
            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Please check network connection");
        }
    }*/
 /*   public void stopSessionWeb(ModelUserProfile results, String sessionid) {
        try {
            ToastHandler.newInstance(PacifyrActivity.this).showToast("stopSessionWeb");
            LogUtils.LOGD("call", "stopSessionWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "stopSessionWeb getAccessToken() "+getAccessToken());
            if (!isValid(getUserID(),"Session Expired, Please Re login"))
                return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;
            if (isValid(results,"Session Expired, Please Re login")) {
                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        SessionSummaryActivity.start(PacifyrActivity.this);
                        if (json == null) {

                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "stopSessionWeb json null ");
                        } else {
                            if (json != null) try {
                                LogUtils.LOGD("call", "stopSessionWeb json  " + json);
                            } catch (Exception e){
                                ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                                LogUtils.LOGD("call", "stopSessionWeb exception : " + e.getMessage());
                            }
                        }
                    }
                };
                AQuery aq = new AQuery(this);

                cb.header("AccessToken", getAccessToken());
                cb.header("Content-Type", "application/json");

                JSONObject input = new JSONObject();
                input.putOpt("userId", getUserID());
                input.putOpt("pacifyrId", results.getId());
                input.putOpt("id", sessionid);
                aq.post(GLOBAL_URL + "session/stop", input, JSONObject.class, cb);
            }
        } catch (Exception e){
            LogUtils.LOGD("call", "stopSessionWeb end exception : " + e.getMessage());
        }
    }


    public void stopSessionWeb(String oppenentid, String sessionid) {
        try {
            ToastHandler.newInstance(PacifyrActivity.this).showToast("stopSessionWeb");
            LogUtils.LOGD("call", "stopSessionWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "stopSessionWeb getAccessToken() "+getAccessToken());
            if (!isValid(getUserID(),"Stop Session getUserID null"))
                return;
            if (!isValid(getAccessToken(),"Stop Session getAccessToken null"))
                return;
            LogUtils.LOGD("call", "stopSessionWeb oppenentid "+oppenentid);
            if (!isValid(oppenentid,"Stop Session Oppenant id null "))
                return;

                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {


                        SessionSummaryActivity.start(PacifyrActivity.this);
                        if (json == null) {

                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "stopSessionWeb json null ");
                        } else {
                            if (json != null) try {
                                LogUtils.LOGD("call", "stopSessionWeb json  " + json);
                            } catch (Exception e){
                                ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                                LogUtils.LOGD("call", "stopSessionWeb exception : " + e.getMessage());
                            }
                        }
                    }
                };
                AQuery aq = new AQuery(this);

                cb.header("AccessToken", getAccessToken());
                cb.header("Content-Type", "application/json");

                JSONObject input = new JSONObject();
                input.putOpt("userId", getUserID());
                input.putOpt("pacifyrId", oppenentid);
                input.putOpt("id", sessionid);
                aq.post(GLOBAL_URL + "session/stop", input, JSONObject.class, cb);

        } catch (Exception e){
            LogUtils.LOGD("call", "stopSessionWeb end exception : " + e.getMessage());
        }
    }
*/


    public void setAlertSessionStop(String title,String desc,String ok) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(com.pacifyrapp.utilitylibrary.R.layout.alert_ok, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                if(isValid(getPrefManager().getSessionId()))
                    stopSessionWeb(getPrefManager().getSessionId());
                hideStatus();


            }
        });
    }


    public void setAlertOKStatusInBlue(String title, String desc, String ok, View.OnClickListener listener) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(com.pacifyrapp.utilitylibrary.R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        CustomFontTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(listener);
    }


  /*  public void stopSessionWeb( String sessionid,int id) {
        try {
            //  ToastHandler.newInstance(PacifyrActivity.this).showToast("stopSessionWeb api called ");
            LogUtils.LOGD("call", "stopSessionWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "stopSessionWeb getAccessToken() "+getAccessToken());

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }


            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    //  SessionSummaryActivity.start(PacifyrActivity.this);
                    if (json == null) {


                        //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "stopSessionWeb json null ");
                    } else {
                        try {
                            LogUtils.LOGD("call", "stopSessionWeb json  " + json);
                        } catch (Exception e){
                            //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "stopSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            // input.putOpt("userId", getUserID());
            // input.putOpt("pacifyrId", oppenentid);
            input.putOpt("id", sessionid);
            aq.post(GLOBAL_URL + "session/stop", input, JSONObject.class, cb);
            //SessionSummaryActivity.start(PacifyrActivity.this);
        } catch (Exception e){
            LogUtils.LOGD("call", "stopSessionWeb end exception : " + e.getMessage());
        }
    }
*/

    public void stopSessionWeb( String sessionid) {
        isCallActive=false;
        Log.d(TAG, "stopSessionWeb " + sessionid);
        try {
            //  ToastHandler.newInstance(PacifyrActivity.this).showToast("stopSessionWeb api called ");
            LogUtils.LOGD("call", "stopSessionWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "stopSessionWeb getAccessToken() "+getAccessToken());

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }
            // return;
          /*
             if (!isValid(getUserID(),"Stop Session getUserID null"))
                return;

            LogUtils.LOGD("call", "stopSessionWeb oppenentid "+oppenentid);
          if (!isValid(oppenentid,"Stop Session Oppenant id null "))
                return;*/

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    //  SessionSummaryActivity.start(PacifyrActivity.this);
                    if (json == null) {


                        //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "stopSessionWeb json null ");
                    } else {
                        try {
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                            args.putString(FirebaseLogging.EC, "stopSessionWeb");

                            FirebaseLogging.sendLog(PacifyrActivity.this,
                                    FirebaseLogging.EVENT_CALL_STARTED,
                                    args);
                            LogUtils.LOGD("call", "stopSessionWeb json  " + json);
                        } catch (Exception e){
                            //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "stopSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            // input.putOpt("userId", getUserID());
            // input.putOpt("pacifyrId", oppenentid);
            input.putOpt("id", sessionid);
            aq.post(GLOBAL_URL + "session/stop", input, JSONObject.class, cb);
            Log.d(TAG, "session/stop");
        } catch (Exception e){
            LogUtils.LOGD("call", "stopSessionWeb end exception : " + e.getMessage());
        }
    }



    /*public void stopSessionWeb(MUser pacifyr) {
        try {
            //  ToastHandler.newInstance(PacifyrActivity.this).showToast("stopSessionWeb api called ");
            LogUtils.LOGD("call", "stopSessionWeb getUserID "+ getUserID());
            LogUtils.LOGD("call", "stopSessionWeb getAccessToken() "+getAccessToken());

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }
            // return;
          *//*
             if (!isValid(getUserID(),"Stop Session getUserID null"))
                return;

            LogUtils.LOGD("call", "stopSessionWeb oppenentid "+oppenentid);
          if (!isValid(oppenentid,"Stop Session Oppenant id null "))
                return;*//*

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    //  SessionSummaryActivity.start(PacifyrActivity.this);
                    if (json == null) {


                        //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "stopSessionWeb json null ");
                    } else {
                        try {
                            LogUtils.LOGD("call", "stopSessionWeb json  " + json);
                        } catch (Exception e){
                            //ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "stopSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            input.putOpt("userId", getUserID());
            String pacID;
            if(isValid(pacifyr))
                pacID = pacifyr.getId();
            else
                pacID = getPrefManager().getPacifyrID();

            input.putOpt("pacifyrId", pacID);
            //input.putOpt("id", sessionid);
            aq.post(GLOBAL_URL + "session/stop", input, JSONObject.class, cb);
            //SessionSummaryActivity.start(PacifyrActivity.this);
        } catch (Exception e){
            LogUtils.LOGD("call", "stopSessionWeb end exception : " + e.getMessage());
        }
    }*/

    public void declineSessionWeb( String sessionid) {
        try {
            // ToastHandler.newInstance(PacifyrActivity.this).showToast("declineSessionWeb");
            LogUtils.LOGD("call", "declineSessionWeb getAccessToken() "+getAccessToken());

            if (!isValid(getAccessToken(),"Decline accesstoken error"))
                return;

//                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject json, AjaxStatus status) {
//                        CallActivity.isDeclined=true;
//                        if (json == null) {
//                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
//                            LogUtils.LOGD("call", "declineSessionWeb json null ");
//                        } else {
//                            if (json != null) try {
//                                LogUtils.LOGD("call", "declineSessionWeb json  " + json);
//                            } catch (Exception e){
//                                ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
//                                LogUtils.LOGD("call", "declineSessionWeb exception : " + e.getMessage());
//                            }
//                        }
//                    }
//                };
//                AQuery aq = new AQuery(this);
//
//                cb.header("AccessToken", getAccessToken());
//                cb.header("Content-Type", "application/json");
//
//                JSONObject input = new JSONObject();
//                input.putOpt("id", sessionid);
//                aq.post(GLOBAL_URL + "session/declined", input, JSONObject.class, cb);
            LogUtils.LOGD("call", "declineSessionWeb after post");
        } catch (Exception e){
            LogUtils.LOGD("call", "declineSessionWeb end exception : " + e.getMessage());
        }
    }



  /*  public void connectedSessionDetailWeb(final String sessionid) {
        try {
            call_session_detail_root=null;
            LogUtils.LOGD("call", "connectedSessionDetailWeb getAccessToken() "+getAccessToken());
            if (!isValid(sessionid,"error Session id "+sessionid))
                return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    LogUtils.LOGD("session", " SSA connectedSessionDetailWeb callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                    }
                    else {
                        try {
                            call_session_detail_root= GsonUtils.getInstance().gsonToMSessionDetailRoot(json);
                         //   onSetValues();
                        } catch (Exception e){
                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("session", " SSA connectedSessionDetailWeb callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/"+sessionid;
            aq.ajax(NetworkService.GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("session","SSA connectedSessionDetailWeb exce " + e.getMessage());
            finish();
        }
    }
*/
/*
    public void connectedSessionWeb( String sessionid) {
        try {
           // ToastHandler.newInstance(PacifyrActivity.this).showToast("connectedSessionWeb");
            call_session_detail_root=null;
            LogUtils.LOGD("call", "connectedSessionWeb getAccessToken() "+getAccessToken());
            if (!isValid(sessionid,"error Session id "+sessionid))
            return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    CallActivity.isConnected=true;
                    if (json == null) {
                        ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "connectedSessionWeb json null ");
                    } else {
                        if (json != null) try {

                            LogUtils.LOGD("call", "connectedSessionWeb json  " + json);
                            call_session_detail_root= GsonUtils.getInstance().gsonToMConnectSessionRoot(json);
                            LogUtils.LOGD("call", "connectedSessionWeb json  " + json);
                        } catch (Exception e){
                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "connectedSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            input.putOpt("id", sessionid);
            aq.post(GLOBAL_URL + "session/connected", input, JSONObject.class, cb);
        } catch (Exception e){
            call_session_detail_root=null;
            LogUtils.LOGD("call", "connectedSessionWeb end exception : " + e.getMessage());
        }
    }*/

    public void missedSessionWeb( String sessionid) {
        Log.d(TAG, "missedSessionWeb " + sessionid);
        try {
            //  ToastHandler.newInstance(PacifyrActivity.this).showToast("missedSessionWeb");
            LogUtils.LOGD("call", "missedSessionWeb getAccessToken() "+getAccessToken());

            if (!isValid(getAccessToken(),"missed call accesstoken error"))
                return;

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {

                    if (json == null) {

                        ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                        LogUtils.LOGD("call", "missedSessionWeb json null ");
                    } else {
                        if (json != null) try {

                            LogUtils.LOGD("call", "missedSessionWeb json  " + json);
                        } catch (Exception e){
                            ToastHandler.newInstance(PacifyrActivity.this).showToast("Please Try again");
                            LogUtils.LOGD("call", "missedSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            input.putOpt("id", sessionid);
            Log.d(TAG, "session/missed");
            aq.post(GLOBAL_URL + "session/missed", input, JSONObject.class, cb);
        } catch (Exception e){
            LogUtils.LOGD("call", "missedSessionWeb end exception : " + e.getMessage());
        }
    }

    public void userDetailWeb(String id ) {
        try {
            //showProgress();

            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();
            if (!isValid(token))return;
            String url = GLOBAL_URL + "user/profile/" + id;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        if(json!=null) {
                            NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                            //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);

                            postEventOnMainThread(new NotifyEvent());

                        }
                        else
                        {
                            LogUtils.LOGD("base", "PA userDetailWeb json null  : " + status.getMessage());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.LOGD("base", "PA userDetailWeb callback exception : " + e.getMessage());
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD("base", "PA userDetailWeb  exception : " + ex.getMessage());
        }
    }

    public void getUserProfile(String id, AjaxCallback cb) {
        try {
            //showProgress();

            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + id;

            AQuery aq = new AQuery(PacifyrActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.LOGD("base", "PA userDetailWeb  exception : " + ex.getMessage());
        }
    }

    public void applyCouponCode(Context context, String couponCode) {
        if (inProgress) {
            Log.d(TAG, "applyCouponCode in progress skipping...");
            return;
        }
        Log.d(TAG, "applyCouponCode = " + couponCode);
        inProgress = true;
        setLoading();
        showProgress();
        new NetworkService().validateDiscountCode(couponCode, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                /*
                 *this must be the first line of any httpCallBacks
                 */
                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                inProgress = false;
                if (json == null) {
                    hideProgress();
                    Toast.makeText(context, getString(R.string.invalid_coupon_code), Toast.LENGTH_SHORT).show();

                    Bundle args =
                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);

                    args.putString(FirebaseLogging.EC, couponCode + " : Invalid");

                    FirebaseLogging.sendLog(PacifyrActivity.this,
                            FirebaseLogging.EVENT_AUTO_APPLY_COUPON_CODE,
                            args);
                } else {
//                            Log.d(TAG, "Resp " + json.toString());
                    final ApiResponse apiResponse = new Gson().fromJson(json.toString(), ApiResponse.class);
                    if (apiResponse.isSuccess()) {
                        PrefManager.getInstance(context).putString(PrefManager.COUPON_CODE, null);
                        PrefManager.getInstance(context).putString(PrefManager.APPLIED_COUPON_CODE, couponCode);
                        getUserProfile(getUserID(), new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                hideProgress();
                                try {
                                    if (json != null) {
                                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                        //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);
                                        if (NetworkService.mCurrentUser != null) {
                                            if (NetworkService.mCurrentUser.getRoleModel().getLabel().equals("employee")) {
                                                PrefManager prefManager = PrefManager.getInstance(context);
                                                prefManager.saveCompanyID(NetworkService.mCurrentUser.getCompanyId());
                                                prefManager.saveCustomerRole("employee");
                                                if (NetworkService.mCurrentUser != null && NetworkService.mCurrentUser.getCompany() != null) {
                                                    prefManager.saveCompanyLogo(NetworkService.mCurrentUser.getCompany().getLogo());
                                                }
                                                showIOSDialogOK("Success", apiResponse.getMessage(), "ok", new iOSDialogClickListener() {
                                                    @Override
                                                    public void onClick(iOSDialog dialog) {
                                                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                        showFragment(new UserDashboardFragment());
                                                    }
                                                });

                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                    }


                                } catch (Exception e) {
                                    Toast.makeText(context, getResources().getString(R.string.invalid_coupon_code), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });

                        Bundle args =
                                FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);

                        args.putString(FirebaseLogging.EC, "Success : " + couponCode);

                        FirebaseLogging.sendLog(PacifyrActivity.this,
                                FirebaseLogging.EVENT_AUTO_APPLY_COUPON_CODE,
                                args);
                    } else if (apiResponse.getMessage() != null) {
                        hideProgress();
                        Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Bundle args =
                                FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);

                        args.putString(FirebaseLogging.EC, couponCode + " : " + apiResponse.getMessage());

                        FirebaseLogging.sendLog(PacifyrActivity.this,
                                FirebaseLogging.EVENT_AUTO_APPLY_COUPON_CODE,
                                args);
                    }
                }
            }
        });
    }


    public void checkRoleRequest(final MUser pacifyerDetial, final AvailableMinuteListner listner,final boolean isVideoCall) {

        try {
            PrefManager prefManager = PrefManager.getInstance(PacifyrActivity.this);
            String customerRole = prefManager.getCustomerRole();

            callRequestWeb(pacifyerDetial, listner, isVideoCall);

         /*   if(customerRole.equals("customer")){

            }else if(customerRole.equals("employee")||customerRole.equals("unpaidEmployee")){

                Boolean chargable = pacifyerDetial.isSessionChargeable();

                if(!chargable){
                    createCurrentUserAndCall(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                    //listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                    listner.onAvailable(NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);
                }else{
                    callRequestWeb(pacifyerDetial, listner, isVideoCall);
                }
            }else if(customerRole.equals("paidMember")){
                createCurrentUserAndCall(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
            }*/

        }
        catch (Exception ex)
        {
            hideProgress();
            listner.onException(ex);
            ex.printStackTrace();
        }
    }

    private void createCurrentUserAndCall(ModelUserProfile mCurrentUser, final MUser pacifyerDetial, final boolean isVideoCall) {
        try{
            showProgress();
            LogUtils.LOGD("session","PA callRequestWeb called " );
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + getUserID();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if(json!=null) {
                            NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                            listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                        }
                        else
                        {
                            listner.onError(status.getMessage());
                        }


                    } catch (Exception e) {
                        //e.printStackTrace();
                        listner.onException(e);
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void callRequestWeb(final MUser pacifyerDetial, final AvailableMinuteListner listner,final boolean isVideoCall) {

        try {
            showProgress();
            LogUtils.LOGD("session","PA callRequestWeb called " );
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + getUserID();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if(json!=null) {
                            NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);

                            listner.onAvailable(NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);

                            /*if (getCountryChecks().contains(getPrefManager().getCountry()))
                                listner.onAvailable(NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);
                            else
                                getSavedCardWeb(pacifyerDetial,listner,isVideoCall);
*/
                           /* if (NetworkService.mCurrentUser.getAvailableMinutes() > 10) {
                              //  ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Free minutes :" +pacifyerDetial.getAvailableMinutes());
                                listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                            }
                            else {

                                getSavedCardWeb(pacifyerDetial,listner,isVideoCall);
                                *//*if(NetworkService.mCurrentUser.getAvailableMinutes() < 10 && NetworkService.mCurrentUser.getAvailableMinutes() > 5)
                                    listner.onAlert(NetworkService.mCurrentUser.getAvailableMinutes(), pacifyerDetial, isVideoCall);
                                else if(pacifyerDetial.getAvailableMinutes() < 5)
                                    listner.onPacifyerAlert(pacifyerDetial.getAvailableMinutes(), NetworkService.mCurrentUser, pacifyerDetial, isVideoCall);
                                else if(NetworkService.mCurrentUser.getAvailableMinutes() < 5)
                                    listner.onNotAvailable(NetworkService.mCurrentUser.getAvailableMinutes());*//*
                            }*/
                        }
                        else
                        {
                            listner.onError(status.getMessage());
                        }


                    } catch (Exception e) {
                        //e.printStackTrace();
                        listner.onException(e);
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            hideProgress();
            listner.onException(ex);
            ex.printStackTrace();
        }
    }

   /* public void callRequestWeb(final ModelUserProfile pacifyerDetial, final AvailableMinuteListner listner, final boolean isVideoCall, final long qbid ) {
        try {
            showProgress();

            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + getUserID();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        if(json!=null) {
                          NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                            //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);


                         //   updateProfile();
                            if (NetworkService.mCurrentUser.getAvailableMinutes() > 10) {
                                ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Free minutes :" +pacifyerDetial.getAvailableMinutes());
                                listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall;
                            }
                            else if (NetworkService.mCurrentUser.getAvailableMinutes() < 10 && NetworkService.mCurrentUser.getAvailableMinutes()  > 5)
                                listner.onAlert(NetworkService.mCurrentUser.getAvailableMinutes(),pacifyerDetial,isVideoCall);
                            else if (pacifyerDetial.getAvailableMinutes() < 10 || pacifyerDetial.getAvailableMinutes() < 5)
                                listner.onPacifyerAlert(pacifyerDetial.getAvailableMinutes(),pacifyerDetial,NetworkService.mCurrentUser,isVideoCall);
                            else if (NetworkService.mCurrentUser.getAvailableMinutes() < 5)
                                listner.onNotAvailable(NetworkService.mCurrentUser.getAvailableMinutes());
                            //EventBus.getDefault().post(new EventMessage(getString(R.string.event_user_profile)));

                        }
                        else
                        {
                            listner.onError(status.getMessage());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        listner.onException(e);
                    }
                }
            };

                AQuery aq = new AQuery(PacifyrActivity.this);
                //cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            listner.onException(ex);
            ex.printStackTrace();
        }
    }*/

    public void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }
    public void clearSharedAll() {

        try {
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            prefM.clearSharedAll();
        }catch (Exception e)
        {
            LogUtils.LOGD("install", "clearSharedAll Exception : " + e.getMessage());
        }

    }


    public void clearServices() {
        try {
            //  clearLayerDatas();
            NetworkService.modelAllEmployees = null;

            NetworkService.mCurrentUser = null;

        }catch (Exception e)
        {
            LogUtils.LOGD("install", "clearServices Exception : " + e.getMessage());
        }


    }
    private void showSignActivty() {
        Intent i = new Intent(PacifyrActivity.this, SignActivity.class);
        startActivity(i);
        finish();
    }


    public MUser convertModel(ModelUserProfile pacifyerDetial) {
        MUser mUser=null;
        try{

            mUser=new MUser();
            mUser.setId(pacifyerDetial.getId());
            mUser.setAvailableMinutes(pacifyerDetial.getAvailableMinutes());
            mUser.setQbid(pacifyerDetial.getQbid());
            mUser.setFirstName(pacifyerDetial.getFirstName());
            mUser.setOnline(pacifyerDetial.getOnline());
            mUser.setCurrentlyAvailable(pacifyerDetial.getCurrentlyAvailable());
            mUser.setLastName(pacifyerDetial.getLastName());
            mUser.setImage(pacifyerDetial.getImage());
            mUser.setEmotion(pacifyerDetial.getEmotion());
            mUser.setEmotionId(pacifyerDetial.getEmotionId());
            mUser.setDescription(pacifyerDetial.getDescription());
            mUser.setSpecialities(pacifyerDetial.getSpecialities());
            //  mUser.setCertification(pacifyerDetial.getCertification());
            mUser.setEmail(pacifyerDetial.getEmail());
            mUser.setPhoneNumber(pacifyerDetial.getPhoneNumber());
            mUser.setPrice(pacifyerDetial.getPrice());
            mUser.setCountry(pacifyerDetial.getCountry());
            mUser.setSessionChargeable(pacifyerDetial.isSessionChargeable());
            mUser.setBonusProfileId(pacifyerDetial.getBonusProfileId());
            mUser.setCompanyId(pacifyerDetial.getCompanyId());
            mUser.setReasonForTheCall(pacifyerDetial.getReasonForTheCall());
            mUser.setEnableScheduling(pacifyerDetial.isEnableScheduling());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  mUser;
    }



    public void setAlertAddCardInBlue2() {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.alert_add_credit_card, null, false);
            final CustomEditText cardNumberEdt = view.findViewById(R.id.card_number_edt);
            final CustomEditText cardMonthEdt = view.findViewById(R.id.card_month_edt);
            final CustomEditText cardYearEdt = view.findViewById(R.id.card_year_edt);
            final CustomEditText cardCvvEdt = view.findViewById(R.id.card_cvv_edt);
            final DeviceFitImageView close_dimv = view.findViewById(R.id.close_dimv);
            RelativeLayout saveRlay = view.findViewById(R.id.save_rlay);
            MainTextView saveCtxv = view.findViewById(R.id.save_ctxv);
            hideStatus();
            hideStatus2();
            hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus2();
            close_dimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideProgress();
                    hideStatus2();
                }
            });
            saveRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isValidCard(cardNumberEdt,cardMonthEdt,cardYearEdt,cardCvvEdt)) {
                        Card card=  getCardValid(cardNumberEdt,cardMonthEdt,cardYearEdt,cardCvvEdt);
                        if(!isValid(card))
                            ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Invalid Card");
                        else {
                            performCardSave(card);
                        }
                    }
                    else{
                        ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Invalid Card");
                    }
                }
            });

        }catch (Exception e)
        {
            errorStatus();
            /*hideProgress();
            hideStatus2();*/
            LogUtils.LOGD("session", "setAlertAddCardInBlue2 Exception : " + e.getMessage());
        }
    }

    boolean isValidCard(CustomEditText cardNumberEdt, CustomEditText cardMonthEdt,  CustomEditText cardYearEdt, CustomEditText cardCvvEdt ) {
        boolean ret=true;
        try {

            if (!isValid(cardNumberEdt.getText())) {
                //Toast.makeText(PacifyrActivity.this, cardNo, Toast.LENGTH_LONG).show();
                cardNumberEdt.setError("Please enter valid card number");
                ret = false;
            }
            if (!isValid(cardMonthEdt.getText())) {
                //Toast.makeText(PacifyrActivity.this, month, Toast.LENGTH_LONG).show();
                cardMonthEdt.setError("Please enter valid month");
                ret = false;

            }
            if (!isValid(cardYearEdt.getText())) {
                //Toast.makeText(PacifyrActivity.this, year, Toast.LENGTH_LONG).show();
                cardYearEdt.setError("Please enter valid year");
                ret = false;

            }
            if (!isValid(cardCvvEdt.getText())) {
                //Toast.makeText(PacifyrActivity.this, cvv, Toast.LENGTH_LONG).show();
                cardCvvEdt.setError("Please enter valid cvv");
                ret = false;

            }

            Card card = new Card(cardNumberEdt.getText()+"",Integer.parseInt(cardMonthEdt.getText()+""), Integer.parseInt(cardYearEdt.getText()+""), cardCvvEdt.getText()+"");
            // Remember to validate the card object before you use it to save time.
            if (!card.validateCard()) {
                ret = false;
            }

        }
        catch (Exception e)
        {
            ret = false;
        }
        return ret;
    }

    public Card getCardValid(CustomEditText cardNumberEdt, CustomEditText cardMonthEdt,  CustomEditText cardYearEdt, CustomEditText cardCvvEdt )
    {
        Card card = null;
        try {
            card = new Card(cardNumberEdt.getText() + "", Integer.parseInt(cardMonthEdt.getText() + ""), Integer.parseInt(cardYearEdt.getText() + ""), cardCvvEdt.getText() + "");
            if (!card.validateCard()) {
                card = null;
                //ToastHandler.newInstance(PacifyrActivity.this).mustShowToast("Invalid Card");
            }
        } catch (Exception e) {
            card = null;
        }
        return card;

    }
    private void performCardSave(Card card ) {
        try {
            setLoadingWait();
            showProgress();
            Stripe stripe = new Stripe(PacifyrActivity.this, STRIPE_PUBLISH_KEY);
            stripe.createToken(card, new TokenCallback() {
                public void onSuccess(Token token) {
                    String type = token.getType();
                    Token genaratedToken = token;
                    // hideProgress();
                    saveCardDetails(genaratedToken);
                }

                public void onError(Exception error) {
                    Toast.makeText(PacifyrActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    hideProgress();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(PacifyrActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            hideProgress();
        }
    }

    private void saveCardDetails(Token genaratedToken) {
        try {
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String userToken = prefM.getAccessToken();
            String userid = prefM.getUserID();
        /*setLoading();
        showProgress();*/
            String url = NetworkService.GLOBAL_URL + "payment/saveCard";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        String message = json.getString("message");
                        String my = json.toString();
                        if (message.equals("Card added")) {

                            saveCardExist(true);
                            hideStatus2();
                            setStatus("Successfully added your card", R.color.md_green_400);
                            showStatus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideStatus();
                                    // finish();
                                }
                            }, 2000);

                        } else {
                            errorStatus();
                    /*    setStatus("Error occured. Please try again.", R.color.md_red_500);
                        showStatus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatus();
                            }
                        }, 2000);*/
                        }
                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("tokenId", genaratedToken.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            errorStatus();


        }

    }

    public void errorStatus() {
        setStatus("Error,Please try later", R.color.red);
        showStatus();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                hideStatus();
                hideStatus2();
            }
        }, 2000);
    }


    public void fbLogout() {
        try{
            LoginManager mLoginManager = LoginManager.getInstance();
            if(isValid(mLoginManager))
                mLoginManager.logOut();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }

    public void showYearPicker(final MainTextView textView) {
        final Integer[] vals = {null};
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_show_year_picker, null, false);
        TextView title_dtxv = view.findViewById(R.id.title_dtxv);
        TextView ok_dtxv = view.findViewById(R.id.save_ctxv);
        TextView cancelTxv = view.findViewById(R.id.cancel_ctxv);
        ImageView closeDimv = view.findViewById(R.id.close_dimv);
        WheelYearPicker wheelYearPicker = view.findViewById(R.id.wheel_date_picker_year);
        wheelYearPicker.setSelectedItemTextColor(Color.parseColor("#3F51B5"));
        //wheelYearPicker.setIndicator(true);
        //wheelYearPicker.setIndicatorColor(R.color.black);
        wheelYearPicker.setCurved(true);
        wheelYearPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                vals[0] = (Integer) data;
            }
        });

        closeDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();

            }
        });
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid(vals[0]))
                    textView.setText(vals[0].toString());
                hideProgress();
                hideStatus();


            }
        });

        cancelTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus();


            }
        });
    }


    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }




    /**
     * This method is responsible for solving the rotation issue if exist.
     * Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public void startSessionTracking(final String sessionid) {
        //Called in PacifyrActivity
        trackTimer = new Timer();
        trackTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (sessionid == null) {
                    Log.d(TAG, "Session Id is null cannot call sessionTracking");
                    return;
                }
                new NetworkService().sessionTracking(sessionid, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        /*
                         *this must be the first line of any httpCallBacks
                         */
//                        Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                        BusProvider.getInstance().post(Utility.checkStatusCode(status.getCode())); // for call related apis
                        Log.d(TAG, "Url " + url + " -> " + json != null ? json.toString() : null);
                    }
                });
            }
        };
        trackTimer.scheduleAtFixedRate(trackTimerTask, 0, 2500);
    }

    public void stopSessionTracking() {
        Log.d(TAG, "stopSessionTracking");
        //called in CallActivity
        try {
            PacifyrActivity.trackTimer.cancel();
            PacifyrActivity.trackTimerTask.cancel();
            PacifyrActivity.trackTimer.purge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setBillingStarted(boolean billingStarted) {
        this.billingStarted = billingStarted;
    }

    public boolean hasBillingStarted() {
        return this.billingStarted;
    }



    /** PAYTM INTEGRATION
     * @param sessionid
     * @param price**/
    boolean isFromCall =false ;
    public void generateCheckSum(boolean isFromCallb ,String sessionid, int price) {
        isFromCall = isFromCallb;
       /* if (ContextCompat.checkSelfPermission(PacifyrActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PacifyrActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }*/
        //getting the tax amount first.
        String txnAmount = new DecimalFormat("####0.00").format(price);
        final Paytm paytm = new Paytm(
                com.pacifyr.client.paytmpayment.Constants.getM_ID(),
                com.pacifyr.client.paytmpayment.Constants.getChannel_ID(),
                txnAmount,
                com.pacifyr.client.paytmpayment.Constants.getWebSite(),
                com.pacifyr.client.paytmpayment.Constants.getCallBackUrl(),
                com.pacifyr.client.paytmpayment.Constants.getIndustryTypeId()
        );

        try{
            String url = NetworkService.GLOBAL_URL+"paytm/checksum/generate";
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.toString();
                        //generateCheckSum-----{"status":"success","paytmChecksum":"DK4aCSQEuEMr9OXcbskeXqHrFvDM9RCJhfJaG\/dGTeOIrGBm1BbthHxYckTC07z79Q\/2x9\/O21++coAskab5EdI7lKqoOgCP4KcSf6zOuJI="}

                        if (isValid(json))
                        {
                            if (json.has("status"))
                            {
                                String stat=json.getString("status");
                                if (stat.equals("success"))
                                {
                                    Log.w("PAYTMLOG","generateCheckSum-----"+stat);
                                    if (json.has("paytmChecksum"))
                                    {
                                        String paytmChecksum=json.getString("paytmChecksum");
                                        Log.w("PAYTMLOG","paytmChecksum-----"+paytmChecksum);
                                        initializePaytmPayment(paytmChecksum, paytm,sessionid);
                                    }else {
                                        Log.w("PAYTMLOG","generateCheckSum-----"+"paytmChecksum not available !");
                                        return;
                                    }
                                }
                                else {
                                    Toast.makeText(PacifyrActivity.this, stat, Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } else {
                                Log.w("PAYTMLOG","generateCheckSum-----"+"status not available");
                                return;
                            }
                        }
                        else {
                            Log.w("PAYTMLOG","generateCheckSum-----"+"Json not valid");
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("orderId", sessionid);
            jsonObject.putOpt("custId", prefM.getUserID());
            jsonObject.putOpt("mobileNo",prefM.getPhoneNumber());
            jsonObject.putOpt("txnAmount", paytm.getTxnAmount());
            jsonObject.putOpt("email", prefM.getEmail());
            Log.w("PAYTMLOG", "jsonObjectBeforeCreatingCheckSum-----" + jsonObject.toString());
            aq.post(url, jsonObject, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utility.MY_PERMISSIONS_REQUEST_AUDIO_CALL_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isValid(pendingCall)) {
                    startTwilioCall(pendingCall.isVideoCall(), pendingCall.getPacifyr(), pendingCall.isAppointment(),
                            pendingCall.getTimeSlotBookingId());
                }
            }
        } else if (requestCode == Utility.MY_PERMISSIONS_REQUEST_VIDEO_CALL_PERMISSION) {
            boolean allPermissionsGranted = false;
            for (int granted : grantResults) {
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                } else {
                    allPermissionsGranted = true;
                }
            }
            if (allPermissionsGranted) {
                if (isValid(pendingCall)) {
                    startTwilioCall(pendingCall.isVideoCall(), pendingCall.getPacifyr(), pendingCall.isAppointment(),
                            pendingCall.getTimeSlotBookingId());
                }
            }
        }
    }

    public void initializePaytmPayment(String checksumHash, Paytm paytm, String sessionid) {

        //getting paytm test service 'TEST'
        //
        //getting paytm Production service 'PROD'

        PaytmPGService Service;
        if (com.pacifyr.client.paytmpayment.Constants.getTestOrProd().equals("TEST"))
            Service = PaytmPGService.getStagingService("");
        else
            Service = PaytmPGService.getProductionService();


        PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", com.pacifyr.client.paytmpayment.Constants.getM_ID());
        paramMap.put("ORDER_ID", sessionid);
        paramMap.put("CUST_ID", prefM.getUserID());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl() + sessionid);
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("MOBILE_NO", prefM.getPhoneNumber());
        paramMap.put("EMAIL", prefM.getEmail());
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);
        Service.initialize(Order, null);

        settingUpPayment = true;
        if (isCallActive) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder pictureParams = new PictureInPictureParams.Builder();
                pictureParams.setAspectRatio(new Rational(getCurrentView().getWidth(),
                        getCurrentView().getHeight())).build();
                enterPictureInPictureMode(pictureParams.build());
            }
        }
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Log.w("@@@", "someUIErrorOccurred" + inErrorMessage);
                ShowTransactionFaileToUSer(inErrorMessage);
                settingUpPayment = false;
            }

            public void onTransactionResponse(Bundle inResponse) {
                Log.w("PAYTMLOG", "onTransactionResponse-----" + inResponse.toString());

                /**After completing payment call the API to confirm and once again verify from server ! **/
                if (isValid(inResponse))
                {
                    Log.w("PAYTMLOG","Bundle inResponse-----"+inResponse.toString());
                    ConfirmationFromSever(inResponse,checksumHash);
                }
                settingUpPayment = false;
            }

            public void networkNotAvailable() {
                Log.w("@@@","networkNotAvailable");
                ShowTransactionFaileToUSer("Paytm server networkNotAvailable");
                settingUpPayment = false;
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Log.w("@@@","clientAuthenticationFailed"+inErrorMessage);
                ShowTransactionFaileToUSer(inErrorMessage);
                settingUpPayment = false;
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Log.w("@@@","onErrorLoadingWebPage"+inErrorMessage);
                ShowTransactionFaileToUSer(inErrorMessage);
                settingUpPayment = false;
            }

            public void onBackPressedCancelTransaction() {
                Log.w("@@@","onBackPressedCancel");
                ShowTransactionFaileToUSer("Transaction cancelled");
                settingUpPayment = false;
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Log.w("@@@", "onTransactionCancel" + inErrorMessage);
                ShowTransactionFaileToUSer(inErrorMessage);
                settingUpPayment = false;
            }
        });
    }

    private View getCurrentView() {
        return findViewById(R.id.parent_call);
    }


    public void ConfirmationFromSever(Bundle inResponse, String checksumHash) {

        /**split values **/


        String status = inResponse.getString("STATUS");
        String bank_transaction_id = inResponse.getString("BANKTXNID");
        String transaction_id = inResponse.getString("TXNID");
        //String checksum_hash = inResponse.getString("CHECKSUMHASH");

        String payment_mode = inResponse.getString("PAYMENTMODE");
        String currency_type = inResponse.getString("CURRENCY");
        String bank_name = inResponse.getString("BANKNAME");
        String gateway_name = inResponse.getString("GATEWAYNAME");
        String responce_message = inResponse.getString("RESPMSG");
        String orderId = inResponse.getString("ORDERID");
        transaction_date = inResponse.getString("TXNDATE");
        txnAmount = inResponse.getString("TXNAMOUNT");



        try{
            String url = NetworkService.GLOBAL_URL+"paytm/checksum/verify";
            PrefManager prefM = PrefManager.getInstance(PacifyrActivity.this);
            String token = prefM.getAccessToken();
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.toString();
                        Log.w("@@@jsonS", jsonS);
                        //generateCheckSum-----{"status":"success","paytmChecksum":"DK4aCSQEuEMr9OXcbskeXqHrFvDM9RCJhfJaG\/dGTeOIrGBm1BbthHxYckTC07z79Q\/2x9\/O21++coAskab5EdI7lKqoOgCP4KcSf6zOuJI="}

                        if (isValid(json))
                        {
                            TransactionResponce object = GsonUtils.getInstance().gsonToTransactionResponce(json);
                            if (isValid(object)) {
                                if (isValid(object.getIsChecksumValid()))
                                    if (object.getIsChecksumValid() && object.getTransactionStatus().equals("success")) {
                                        // Transaction Succeeded
                                        Log.w("@@@","Payment setup failed.ShowTransactionSuccesResultToUSer");
                                        ShowTransactionSuccesResultToUSer(object);
                                    } else { // Transaction Failed

                                        ShowTransactionFaileToUSer(responce_message);
                                        Log.w("@@@","Payment setup failed.ShowTransactionFaileToUSer");

                                    }

                            }

                        }
                        else {
                            Log.w("PAYTMLOG2","generateCheckSum-----"+"Json not valid");
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(PacifyrActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("orderId", orderId);
            jsonObject.putOpt("custId", prefM.getUserID());
            jsonObject.putOpt("mobileNo", prefM.getPhoneNumber());
            jsonObject.putOpt("txnAmount", txnAmount);
            jsonObject.putOpt("email", prefM.getEmail());
            jsonObject.putOpt("checkSumHash", checksumHash);
            Log.w("PAYTMLOG","jsonObjectAfterPayTmSuccess-----"+jsonObject.toString());
            aq.post(url, jsonObject, JSONObject.class, cb);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void ShowTransactionSuccesResultToUSer(TransactionResponce object) {

        errorCount=0;
        if (PaymentPopup != null && PaymentPopup.isShowing()) {
            PaymentPopup.dismiss();
        }


        if (!isShowDialogs()) {
            if (isCallActive)
            maximizePicInPicView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = LayoutInflater.from(PacifyrActivity.this).inflate(R.layout.layout_paytm_payment_report, null);
                            TextView ok = view.findViewById(R.id.button_ok);
                            TextView transactionId = view.findViewById(R.id.transactionId);
                            TextView text_date = view.findViewById(R.id.text_date);
                            TextView text_amount_charged = view.findViewById(R.id.text_amount_charged);
                            TextView text_time= view.findViewById(R.id.text_time);
                            TextView textViewCopyTransaction= view.findViewById(R.id.textViewCopyTransaction);
                            TextView title = view.findViewById(R.id.title);
                            title.setText(getResources().getString(R.string.payment_summary));
                            try {
                                transactionId.setText(object.getTransactionId());
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat targetFormat;
                                if (getPrefManager().getCountry().equals("India")) {
                                    targetFormat = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a", Locale.ENGLISH);
                                } else {
                                    targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
                                }
                                String strDate = targetFormat.format(c.getTime());
                                strDate.replace("AM", "am").replace("PM","pm");


                                String[] separated = strDate.split(" ");
                                text_date.setText(separated[0].toLowerCase()+"");
                                text_time.setText(separated[2].toLowerCase()+" "+separated[3].toLowerCase()+"");

                                PrefManager prefM = PrefManager.getInstance(getApplicationContext());
                                String country = prefM.getCountry();
                                String sign = "$";
                                if (country.equals("India")) {
                                    sign = getApplicationContext().getResources().getString(R.string.rupee);
                                } else {
                                    sign = getApplicationContext().getResources().getString(R.string.dollar);
                                }
                                text_amount_charged.setText(sign + " " + txnAmount);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (PaymentPopup != null) {
                                        PaymentPopup.dismiss();
                                    }
                                    /** send Notification to the Provider that user Done the Payment Success fully and able to start Session now.
                                     * type "cardSetup**/

                                    EventBus.getDefault().post("SCHEDULE_PAY");
                                    EventBus.getDefault().post(Constants.PAYMENT_DONE);
                                    if (isValid(VideoActivity.mSessionDetailRoot) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr()) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr().getId()))
                                        new NetworkService().sendCardAddedNotificationAfterPaytmSuccess(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "cardSetup", new AjaxCallback<JSONObject>() {
                                            @Override
                                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                                /*
                                                 *this must be the first line of any httpCallBacks
                                                 */
//                                                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                                BusProvider.getInstance().post(Utility.checkStatusCode(status.getCode())); // for call related apis
                                                if (json != null) {
                                                    Log.w("PAYTMLOG", "sendCardAddedNotification responce" + json.toString());
                                                }
                                            }
                                        });
                                   // else
                                       // Toast.makeText(PacifyrActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_DONE,
                                            args);
                                }
                            });

                            textViewCopyTransaction.setOnTouchListener(new View.OnTouchListener() {
                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        int[] textLocation = new int[2];
                                        textViewCopyTransaction.getLocationOnScreen(textLocation);

                                        if(event.getRawX() >= textViewCopyTransaction.getRight() - textViewCopyTransaction.getTotalPaddingRight()) {
                                            /** copy transaction id to clipboard **/

                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("transactionId", transactionId.getText());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(PacifyrActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                                              return true;
                                        }

                                    }
                                    return true;
                                }
                            });

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PacifyrActivity.this);
                            builder.setView(view);
                            builder.setCancelable(false);
                            PaymentPopup = builder.create();
                            PaymentPopup.show();
                            makeAbeepSoundWhileShowingPopUp();
                        }
                    });
                }
            }, 2000);
            return;
        } else {

            if (isCallActive)
                bringCallActivityToFront();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = LayoutInflater.from(PacifyrActivity.this).inflate(R.layout.layout_paytm_payment_report, null);
                            TextView ok = view.findViewById(R.id.button_ok);
                            TextView transactionId = view.findViewById(R.id.transactionId);
                            TextView textViewCopyTransaction= view.findViewById(R.id.textViewCopyTransaction);
                            TextView text_date = view.findViewById(R.id.text_date);
                            TextView text_amount_charged = view.findViewById(R.id.text_amount_charged);
                            TextView text_time= view.findViewById(R.id.text_time);
                            TextView title = view.findViewById(R.id.title);
                            title.setText(getResources().getString(R.string.payment_summary));
                            try {
                                transactionId.setText(object.getTransactionId());

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat targetFormat;
                                if (getPrefManager().getCountry().equals("India")) {
                                    targetFormat = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a", Locale.ENGLISH);
                                } else {
                                    targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
                                }
                                String strDate = targetFormat.format(c.getTime());
                                strDate.replace("AM", "am").replace("PM","pm");

                                String[] separated = strDate.split(" ");
                                text_date.setText(separated[0].toLowerCase()+"");
                                text_time.setText(separated[2].toLowerCase()+" "+separated[3].toLowerCase()+"");

                                PrefManager prefM = PrefManager.getInstance(getApplicationContext());
                                String country = prefM.getCountry();
                                String sign = "$";
                                if (country.equals("India")) {
                                    sign = getApplicationContext().getResources().getString(R.string.rupee);
                                } else {
                                    sign = getApplicationContext().getResources().getString(R.string.dollar);
                                }
                                text_amount_charged.setText(sign + " " + txnAmount);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (PaymentPopup != null) {
                                        PaymentPopup.dismiss();
                                    }
                                    /** send Notification to the Provider that user Done the Payment Success fully and able to start Session now.
                                     * type "cardSetup**/
                                    EventBus.getDefault().post("SCHEDULE_PAY");
                                    if (isValid(VideoActivity.mSessionDetailRoot) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr()) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr().getId()))
                                        new NetworkService().sendCardAddedNotificationAfterPaytmSuccess(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "cardSetup", new AjaxCallback<JSONObject>() {
                                            @Override
                                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                                /*
                                                 *this must be the first line of any httpCallBacks
                                                 */
                                                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                                if (json != null) {
                                                    Log.w("PAYTMLOG", "sendCardAddedNotification responce" + json.toString());
                                                }
                                            }
                                        });
                                  //  else
                                      //  Toast.makeText(PacifyrActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_DONE,
                                            args);


                                }
                            });

                            textViewCopyTransaction.setOnTouchListener(new View.OnTouchListener() {
                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        int[] textLocation = new int[2];
                                        textViewCopyTransaction.getLocationOnScreen(textLocation);

                                        if(event.getRawX() >= textViewCopyTransaction.getRight() - textViewCopyTransaction.getTotalPaddingRight()) {
                                            /** copy transaction id to clipboard **/

                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("transactionId", transactionId.getText());
                                            clipboard.setPrimaryClip(clip);
                                            Toast.makeText(PacifyrActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                                            return true;
                                        }

                                    }
                                    return true;
                                }
                            });

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PacifyrActivity.this);
                            builder.setView(view);
                            builder.setCancelable(false);
                            PaymentPopup = builder.create();
                            PaymentPopup.show();
                            makeAbeepSoundWhileShowingPopUp();
                        }
                    });
                }
            }, 2000);

        }



    }



    public void ShowTransactionSuccesResultToUSer(String message) {

        errorCount=0;
        if (!isShowDialogs()) {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                if (isCallActive)
                    maximizePicInPicView();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iOSDialogClickListener listener = new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                    if (isValid(VideoActivity.mSessionDetailRoot) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr()) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr().getId()))
                                        new NetworkService().sendCardAddedNotificationAfterPaytmSuccess(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "cardSetup", new AjaxCallback<JSONObject>() {
                                            @Override
                                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                                /*
                                                 *this must be the first line of any httpCallBacks
                                                 */
//                                                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                                BusProvider.getInstance().post(Utility.checkStatusCode(status.getCode())); // for call related apis
                                                if (json != null) {
                                                    Log.w("PAYTMLOG", "sendCardAddedNotification responce" + json.toString());
                                                }
                                            }
                                        });
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    args.putString(FirebaseLogging.EC, message + "");
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_DONE,
                                            args);
                                    dialog.dismiss();
                                }
                            };
                            showIOSDialogOK("Success", message, "OK", listener,false);
                        }
                    });
                }
            }, 2000);
            return;
        }
        else {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                if (isCallActive)
                    bringCallActivityToFront();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iOSDialogClickListener listener = new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                    if (isValid(VideoActivity.mSessionDetailRoot) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr()) &&
                                            isValid(VideoActivity.mSessionDetailRoot.getPacifyr().getId()))
                                        new NetworkService().sendCardAddedNotificationAfterPaytmSuccess(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "cardSetup", new AjaxCallback<JSONObject>() {
                                            @Override
                                            public void callback(String url, JSONObject json, AjaxStatus status) {
                                                /*
                                                 *this must be the first line of any httpCallBacks
                                                 */
//                                                Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                                BusProvider.getInstance().post(Utility.checkStatusCode(status.getCode())); // for call related apis
                                                if (json != null) {
                                                    Log.w("PAYTMLOG", "sendCardAddedNotification responce" + json.toString());
                                                }
                                            }
                                        });
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    args.putString(FirebaseLogging.EC, message + "");
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_DONE,
                                            args);
                                    dialog.dismiss();
                                }
                            };
                            showIOSDialogOK("Success", message, "OK", listener,false);
                        }
                    });
                }
            }, 2000);

            return;
        }

    }


    public void ShowTransactionFaileToUSer(String message) {
        errorCount++;
        if (!isShowDialogs()) {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                if (isCallActive)
                maximizePicInPicView();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iOSDialogClickListener listener = new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {

                                    new NetworkService().sendPaymentFailedDueToSomeReasons(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "paymentFailure", message, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject json, AjaxStatus status) {
                                            /*
                                             *this must be the first line of any httpCallBacks
                                             */
                                            Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                            if (json != null) {
                                                Log.w("PAYTMLOG", "sendCardAddedNotificationFail messages" + json.toString());
                                            }
                                        }
                                    });
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    args.putString(FirebaseLogging.EC, message + "");
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_FAILED,
                                            args);
                                    dialog.dismiss();
                                }
                            };
                            showIOSDialogOK("Error", message, "OK", listener,false);
                        }
                    });
                }
            }, 2000);
            return;
        } else {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                if (isCallActive)
                    bringCallActivityToFront();
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iOSDialogClickListener listener = new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    if (isValid(VideoActivity.mSessionDetailRoot)&&isValid(VideoActivity.mSessionDetailRoot.getPacifyr()))

                                    new NetworkService().sendPaymentFailedDueToSomeReasons(VideoActivity.mSessionDetailRoot.getPacifyr().getId(), "paymentFailure", message, new AjaxCallback<JSONObject>() {
                                        @Override
                                        public void callback(String url, JSONObject json, AjaxStatus status) {
                                            /*
                                             *this must be the first line of any httpCallBacks
                                             */
                                            Auth.httpStatusFilter(getApplicationContext(),status.getCode());
                                            if (json != null) {
                                                Log.w("PAYTMLOG", "sendCardAddedNotificationFail messages" + json.toString());
                                            }
                                        }
                                    });
                                    Bundle args =
                                            FirebaseLogging.getConstantArgsLoggedIn(PacifyrActivity.this);
                                    args.putString(FirebaseLogging.EC, message + "");
                                    FirebaseLogging.sendLog(PacifyrActivity.this,
                                            FirebaseLogging.EVENT_CLIENT_PAYMENT_FAILED,
                                            args);
                                    dialog.dismiss();
                                }
                            };
                            showIOSDialogOK("Error", message, "OK", listener,false);
                        }
                    });
                }
            }, 2000);
        }


    }

    public boolean isShowDialogs() {
        return this.showDialogs;
    }

    public void setShowDialogs(boolean showDialogs) {
        this.showDialogs = showDialogs;
    }

    public MEmotion getEmotion(String emotionid) {
        MEmotion mEmotion = null;
        try {
            LogUtils.LOGD("session", "SSA getEmotion  emotionid" + emotionid);
            if (isValid(NetworkService.modelAllEmotions)) {
                ArrayList<MEmotion> arrayList = NetworkService.modelAllEmotions.getResultsList();
                for (MEmotion item : arrayList) {
                    if (item.getId().equalsIgnoreCase(emotionid)) {
                        mEmotion = item;
                        break;
                    }
                }
            } else {
                NetworkService.webListEmotions(PacifyrActivity.this);
            }
        } catch (Exception e) {
            LogUtils.LOGD("session", "SSA e getmessage " + e.getMessage());
        }
        return mEmotion;
    }


    public void setAlertStatusInWhiteNoInternet(String title,String desc,String ok,String dismiss,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_no_internet, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
        MainTextView cancel_dtxv = view.findViewById(R.id.cancel_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        defSetText(ok_dtxv,ok,"Yes");
        defSetText(cancel_dtxv,dismiss,"No");
        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(PacifyrActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);
        cancel_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideProgress();
                hideStatus2();
            }
        });

    }

    /*public void setAlertStatusCovid(String title,String desc,String ok,String dismiss,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_covid, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.VISIBLE);
        defSetText(desc_dtxv,desc);
        defSetText(ok_dtxv,ok,"Ok");
        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(PacifyrActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);

    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w("APPOINMETNOTI","onNewIntentPacifyrActivty");
    }

    public void webGetAppointmentDetails(String appointMentId) {

        try {
            setLoading();
            showProgress();
            String url = GLOBAL_URL + "schedule/getAppointment/"+appointMentId;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        if (isValid(json))
                        {
                            Log.w("APPOINMETNOTI",String.valueOf(json));
                            AppoinmentNotification mAppointmentModel = GsonUtils.getInstance().gsonToAppoinmentNotification(json);
                            if (isValid(mAppointmentModel))
                            {
                                showAppointmentReminderPopup(mAppointmentModel);
                            }
                        }


                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
        }
    }

    private boolean appointmentDialogueIsShown=false;
    public void showAppointmentReminderPopup(AppoinmentNotification mAppointmentModel) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.appoinment_call_popup_from_notification, null);

        TextView textView_intro=customLayout.findViewById(R.id.textView_intro);
        TextView mTextView_PacifyrName=customLayout.findViewById(R.id.mTextView_PacifyrName);
        CircleImageView pacifyr_profileImage=customLayout.findViewById(R.id.pacifyr_profileImage);
        TextView mTextView_ScheduleTime=customLayout.findViewById(R.id.mTextView_ScheduleTime);
        TextView TextView_initiateCallInfo =customLayout.findViewById(R.id.TextView_initiateCallInfo);
        Button Button_Dismiss  =customLayout.findViewById(R.id.Button_Dismiss);
        RelativeLayout audiocall_rlay = customLayout.findViewById(R.id.audiocall_rlay);
        RelativeLayout videocall_rlay = customLayout.findViewById(R.id.videocall_rlay);
        RelativeLayout chat_rlay = customLayout.findViewById(R.id.chat_rlay);

        builder.setView(customLayout);


        SpannableStringBuilder spanTxt = new SpannableStringBuilder("Please visit your profile page and upload your identity document (PAN/Aadhaar/EPIC/DL) if you have not done so.\n\n");
        spanTxt.append(Html.fromHtml("<font color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + "Steps to follow:" + "</b></font>"));
        spanTxt.append("\n\n1. Click on the Audio or Video button to connect to the Provider.\n");
        spanTxt.append("2. Introduction by yourself and the Provider.\n");
        spanTxt.append("3. You will receive a ");
        spanTxt.append(Html.fromHtml("<font size=14 color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + " Session Started" + "<b></font>"));
        spanTxt.append(" message.");
        textView_intro.setText(spanTxt, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder spanTxt2 = new SpannableStringBuilder("If you click on dismiss you can initiate this call from ");
        spanTxt2.append(Html.fromHtml("<font size=14 color=" + getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.black) + "><b>" + " MY APPOINTMENTS" + "<b></font>"));
        TextView_initiateCallInfo.setText(spanTxt2, TextView.BufferType.SPANNABLE);
        // create and show the alert dialog
        if (appointmentDialogueIsShown)
            return;
        AlertDialog dialog = builder.create();
        dialog.show();
        appointmentDialogueIsShown=true;
        Button_Dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                appointmentDialogueIsShown=false;
            }
        });

        if (isValid(mAppointmentModel))
        {
            if (isValid(mAppointmentModel.getPacifyr()))
            {
                isValid(mAppointmentModel.getPacifyr().getFirstName());
                {
                    mTextView_PacifyrName.setText(mAppointmentModel.getPacifyr().getFirstName());
                    if (isValid(mAppointmentModel.getPacifyr().getLastName()))
                    {
                        mTextView_PacifyrName.append(" "+mAppointmentModel.getPacifyr().getLastName()+"");
                    }
                }

                if (isValid(mAppointmentModel.getPacifyr().getImage()))
                {
                    String imgUrl= mAppointmentModel.getPacifyr().getImage();
                    if (isValid(imgUrl))
                        imgUrl= UtilityNetworkService.GLOBAL_IMAGE_URL + imgUrl;

                    if (isValidhtml(imgUrl))
                        Picasso.with(this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(pacifyr_profileImage);

                }


                audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        startSessionWebforAppoinmetCall(mAppointmentModel.getPacifyr(), false,mAppointmentModel.getId());

                    }
                });

                videocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        startSessionWebforAppoinmetCall(mAppointmentModel.getPacifyr(), true,mAppointmentModel.getId());
                        appointmentDialogueIsShown=false;

                    }
                });

                chat_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        startChat(mAppointmentModel.getPacifyr());
                        appointmentDialogueIsShown=false;

                    }
                });

            }


            if (isValid(mAppointmentModel.getStartTimeGMT()))
            {
                String mDisplayDateAndTime = Utility.getStartFormatDate(mAppointmentModel.getStartTimeGMT(),PacifyrActivity.this);
                mTextView_ScheduleTime.setText(mDisplayDateAndTime+"");
            }

        }

    }

    public void loadPDF(String url,String title) {
        setLoading();
        showProgress();
        FileLoader.with(PacifyrActivity.this)
                .load(GLOBAL_IMAGE_URL+url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        hideProgress();
                        File loadedFile = response.getBody();
                        // do something with the file
                        Log.d(TAG, "File downloaded ");
                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(PacifyrActivity.this, PdfViewer.class);
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

    public void loadPDF(String url,String title,boolean restoreCall) {
        setLoading();
        showProgress();
        FileLoader.with(PacifyrActivity.this)
                .load(GLOBAL_IMAGE_URL+url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        hideProgress();
                        File loadedFile = response.getBody();
                        // do something with the file
                        Log.d(TAG, "File downloaded ");
                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(PacifyrActivity.this, PdfViewer.class);
                        intent.putExtra("title",title);
                        intent.putExtra("restoreCall",restoreCall);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        hideProgress();
                        Log.d(TAG, "File download failed ");
                    }
                });
    }

    public void selectRemoteConfigUrl(int i) {
        if(isProd)
        {
//            if (i == 0) { //US
//                GLOBAL_URL = ((App) getApplication()).getRemoteConfig().getString(RemoteConfig.KEY_US_URL);
//            } else if (i == 2) { //India
//                GLOBAL_URL = ((App) getApplication()).getRemoteConfig().getString(RemoteConfig.KEY_INDIA_URL);
//            } else { //use default
//                UtilityNetworkService.init(this);
//            }
            UtilityNetworkService.init(this);
        }
        else {
            UtilityNetworkService.init(this);
        }
        Log.d(TAG, "GLOBAL-URL : " + GLOBAL_URL);
    }

    public static boolean flexibleUpdatePopupShown = false;
    public void checkForAppUpdate(Activity context, int UPDATE_REQUEST_CODE) {
        if (VideoActivity.isCallActive) {
            Log.d(TAG, "Call is active. Not checking for updates");
            return;
        }
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks whether the platform allows the specified type of update,
        // and current version staleness.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
           /* Log.d(TAG, "Check for app update");
            Log.d(TAG, "Update available : " + (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ? "Yes" : "No"));
            Log.d(TAG, "Stateness Days : " + appUpdateInfo.clientVersionStalenessDays());
            Log.d(TAG, "App update Info : " + appUpdateInfo.toString());*/
//            Toast.makeText(context, "App update available : " + (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ? "Yes" : "No"), Toast.LENGTH_LONG).show();
//            Log.d(TAG, "version code " + appUpdateInfo.availableVersionCode());
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) && checkIfImmediateUpdateRequired(appUpdateInfo.availableVersionCode())) {
                try {
//                    Toast.makeText(context, "Immediate app update", Toast.LENGTH_LONG).show();
                    finish();
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            context,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    Toast.makeText(context, "Immediate app update : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) && !flexibleUpdatePopupShown) {
                try {
//                    Toast.makeText(context, "Flexible app update", Toast.LENGTH_LONG).show();
                    // Create a listener to track request state updates.
                    InstallStateUpdatedListener listener = state -> {
                        // (Optional) Provide a download progress bar.
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            if (!VideoActivity.isCallActive) {
                                appUpdateManager.completeUpdate();
                            }
                        }
                        // Log state or install the update.
                    };

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(listener);
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.FLEXIBLE,
                            // The current activity making the update request.
                            context,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);
                    flexibleUpdatePopupShown = true;
                } catch (IntentSender.SendIntentException e) {
//                    Toast.makeText(context, "Flexible app update : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate();
            }
        });
        appUpdateInfoTask.addOnFailureListener(new com.google.android.play.core.tasks.OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(context, "App update : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkIfImmediateUpdateRequired(int availableVersionCode) {
        long code = FirebaseLogging.getAppVersionCode(getApplicationContext());
        boolean forceUpgrade = false;//((App) getApplication()).getRemoteConfig().getBoolean(RemoteConfig.KEY_FORCE_UPGRADE);
        return (code > 0 && (availableVersionCode - code) >= 2) || forceUpgrade;
    }
}




