package com.pacifyr.client.twilio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import android.util.Log;
import android.util.Rational;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.ActivityChangeEmotionFromCall;
import com.pacifyr.client.adapter.EmotionSpecialityAdapter;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallChangeEmotion;
import com.pacifyr.client.event.CallDetailEvent;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.CallTimerEvent;
import com.pacifyr.client.event.PicInPic;
import com.pacifyr.client.event.TwilioCallEvent;
import com.pacifyr.client.fragment.PrescriptionWhileCallFragment;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.home.SessionSummaryActivity;
import com.pacifyr.client.interfaces.EmpMinutesListener;
import com.pacifyr.client.model.CallStatus;
import com.pacifyr.client.model.PrescriptionList;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.quickblox.fragments.AudioConversationFragment;
import com.pacifyr.client.quickblox.fragments.VideoConversationFragment;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.quickblox.utils.PermissionsChecker;
import com.pacifyr.client.quickblox.utils.RingtonePlayer;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.sample.core.utils.Toaster;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.twilio.audioswitch.AudioDevice;
import com.twilio.audioswitch.AudioDevice.BluetoothHeadset;
import com.twilio.audioswitch.AudioDevice.Earpiece;
import com.twilio.audioswitch.AudioDevice.Speakerphone;
import com.twilio.audioswitch.AudioDevice.WiredHeadset;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.video.AudioCodec;
import com.twilio.video.CameraCapturer;
import com.twilio.video.CameraCapturer.CameraSource;
import com.twilio.video.ConnectOptions;
import com.twilio.video.EncodingParameters;
import com.twilio.video.G722Codec;
import com.twilio.video.H264Codec;
import com.twilio.video.IsacCodec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.OpusCodec;
import com.twilio.video.PcmaCodec;
import com.twilio.video.PcmuCodec;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.twilio.video.Vp8Codec;
import com.twilio.video.Vp9Codec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.quickblox.activities.SettingsActivity;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.isRelease;
import static com.pacifyr.client.utils.Constants.getCountryChecks;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.isProd;

public class VideoActivity extends PacifyrActivity {
    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "VideoActivity";

    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";
    public static final String PREF_AUDIO_CODEC = "audio_codec";
    public static final String PREF_AUDIO_CODEC_DEFAULT = OpusCodec.NAME;
    public static final String PREF_VIDEO_CODEC = "video_codec";
    public static final String PREF_VIDEO_CODEC_DEFAULT = Vp8Codec.NAME;
    public static final String PREF_SENDER_MAX_AUDIO_BITRATE = "sender_max_audio_bitrate";
    public static final String PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT = "0";
    public static final String PREF_SENDER_MAX_VIDEO_BITRATE = "sender_max_video_bitrate";
    public static final String PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT = "0";
    public static final String PREF_VP8_SIMULCAST = "vp8_simulcast";
    public static final String PREF_ENABLE_AUTOMATIC_SUBSCRIPTION = "enable_automatic_subscription";
    public static final boolean PREF_ENABLE_AUTOMATIC_SUBSCRIPTION_DEFAULT = true;
    public static final boolean PREF_VP8_SIMULCAST_DEFAULT = false;

    private static final String[] VIDEO_CODEC_NAMES = new String[] {
            Vp8Codec.NAME, H264Codec.NAME, Vp9Codec.NAME
    };

    private static final String[] AUDIO_CODEC_NAMES = new String[] {
            IsacCodec.NAME, OpusCodec.NAME, PcmaCodec.NAME, PcmuCodec.NAME, G722Codec.NAME
    };


    private String accessToken;
    private MUser pacifyerDetial;
    private boolean isVideoCall;
    private String roomName;
    private String SessionId;

    private Room room;

    private int previousAudioMode;
    private boolean previousMicrophoneMute;
    private AudioManager audioManager;


    private LocalParticipant localParticipant;
    private AudioCodec audioCodec;
    private VideoCodec videoCodec;
    private EncodingParameters encodingParameters;
    private VideoView primaryVideoView;
    private VideoView thumbnailVideoView;
    private SharedPreferences preferences;
    private CameraCapturerCompat cameraCapturerCompat;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private FloatingActionButton connectActionFab;
    private FloatingActionButton switchCameraActionFab;
    private FloatingActionButton localVideoActionFab;
    private FloatingActionButton muteActionFab;
    private ProgressBar reconnectingProgressBar;
    private AlertDialog connectDialog;
    private String remoteParticipantIdentity;
    public static boolean isCallActive = false;
    /*
     * Audio management
     */
    private AudioSwitch audioSwitch;
    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private VideoRenderer localVideoView;
    private boolean disconnectedFromOnDestroy;
    private boolean enableAutomaticSubscription;

    //Call HangUp
    private ImageView button_hangup_call;
    private ToggleButton micToggleVideoCall,toggle_speaker,toggle_camera;


    private TextView billingStatus,emotion_ctxv,reason_ctxv;
    private TextView text_remaining_minutes,audio_text_remaining_minutes;
    private CountDownTimer countDownTimer;
    protected boolean isStarted;
    protected Chronometer timerChronometer,chronometer_timer_audio_call;
    boolean isFirsttime = false;
    public CountDownTimer timer;
    private EmpMinutesListener empMinutesListener;
    private ImageView toggle_Max_Min,ImageView_emotion,ImageView_chat,ImageView_prescription;
    private LinearLayout call_overlay,LinearLayout_chat,certificate_View,more_rlay;
    private RelativeLayout emotion_rlay;
    private String change_Emotion_name;
    private DeviceFitImageView emotion_dimv;
    private CircleImageView userDimv;
    private static final String FREE_MINUTES_10 = "freeMinutes10";
    private static final String FREE_MINUTES_5 = "freeMinutes5";
    private static final String FREE_MINUTES_OVER = "freeMinutesOver";
    private static final String PAYTM_PAYMENT_DONE = "cardSetup";
    boolean freeMinuteAlert10, freeMinuteAlert5, freeMinuteAlert0, freeMinuteAlertMore10 = false; // track which of the 3 alerts are shown. 10, 5 or 0;
    private TextView connectionStatusLocal;
    private TextView username_ctxv;
    private LinearLayout layout_info_about_audio_call;
    private TextView TextView_call_type;
    private CountDownTimer OutGoingCallTimer =null;
    private boolean isProviderConnected=false;

    public EmpMinutesListener getEmpMinutesListener() {
        return empMinutesListener;
    }

    public void setEmpMinutesListener(EmpMinutesListener empMinutesListener) {
        this.empMinutesListener = empMinutesListener;
    }
    private RingtonePlayer ringtonePlayer;
    private BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String type = intent.getStringExtra("type");
            Log.d("receiver", "Got message: " + type);
            if (type != null && type.equals("billing")) {
                billingStatus.setVisibility(View.VISIBLE);
                setBillingStarted(true);
                if (isEmployee()) {
                    setEmpMinutesListener(empMinutesListener);
                    checkEmpAvailableMinutes();
                }
                try {
                    stopTimer();
                    startTimer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private EmpMinutesListener mEmpMinutesListener = new EmpMinutesListener() {
        @Override
        public void onMinutesLeft(long minutes) {
           /* setAlertStatusInBlue2("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.",
                    "Add Card", "Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAlertAddCardInBlue2();
                        }
                    });*/

             showIOSDialogOKCancel("Warning", "Your monthly free sessions is expiring soon. Please enter your Credit Card to continue service.", "Add Card", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        setAlertAddCardInBlue2();

                    }
                }, "Cancel");

        }

        @Override
        public void expired() {
            try {

                isCallActive=false;
                Toast.makeText(getApplicationContext(), "Your monthly free sessions has expired. Please enter your Credit Card to continue.", Toast.LENGTH_LONG).show();
                hangUpCurrentSessionMe();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void tick(long minutes) {
            text_remaining_minutes.setText(minutes + " free minutes remaining");
            text_remaining_minutes.setVisibility(View.VISIBLE);

        }
    };


    public void checkEmpAvailableMinutes() {
        boolean cardExist = isCardExist();
        if (!cardExist && isEmployee()) {
            if (NetworkService.mCurrentUser == null) {
                return;
            }
            long freeMinutes = NetworkService.mCurrentUser.getAvailableMinutes();
            if (freeMinutes > 0) {
                long freeMillis = freeMinutes * 60 * 1000;
                final long ONE_MINUTE = 60 * 1000;
                countDownTimer = new CountDownTimer(freeMillis, ONE_MINUTE) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minutesLeft = millisUntilFinished / (60 * 1000);

                        if (empMinutesListener != null) {
                            empMinutesListener.tick(minutesLeft);
                        }
                        if (minutesLeft <= 0 && !freeMinuteAlert0) {
                            if (empMinutesListener != null) {
                                empMinutesListener.expired();
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_OVER, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert0 = true;
                                        }
                                    }
                                });
                            }
                        } else if (minutesLeft <= 5 && !freeMinuteAlert5) {
                            if (empMinutesListener != null) {
                                empMinutesListener.onMinutesLeft(minutesLeft);
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_5, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert5 = true;
                                        }
                                    }
                                });
                            }
                        } else if (minutesLeft <= 10 && !freeMinuteAlert10) {
                            if (empMinutesListener != null) {
                                empMinutesListener.onMinutesLeft(minutesLeft);
                            }
                            if (getCallPacifyr() != null) {
                                new NetworkService().sendCardAddedNotification(getCallPacifyr().getId(), FREE_MINUTES_10, new AjaxCallback<JSONObject>() {
                                    @Override
                                    public void callback(String url, JSONObject json, AjaxStatus status) {
                                        if (json != null) {
                                            freeMinuteAlert10 = true;
                                        }

                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, minutesLeft + " minutes left");
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (empMinutesListener != null) {
                            empMinutesListener.onMinutesLeft(0);
                        }
                    }
                };
                countDownTimer.start();
            }
        }
    }


    public static void start(Context context,String accessToken,String roomName,MUser pacifyerDetial,boolean isVideoCall) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("accessToken",accessToken);
        intent.putExtra("pacifyerDetial",pacifyerDetial);
        intent.putExtra("roomName",roomName);
        intent.putExtra("isVideoCall",isVideoCall);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
      try {
          EventBus.getDefault().register(this);
          primaryVideoView = findViewById(R.id.primary_video_view);
          thumbnailVideoView = findViewById(R.id.thumbnail_video_view);
          reconnectingProgressBar = findViewById(R.id.reconnecting_progress_bar);

          connectActionFab = findViewById(R.id.connect_action_fab);
          switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
          localVideoActionFab = findViewById(R.id.local_video_action_fab);
          muteActionFab = findViewById(R.id.mute_action_fab);
          button_hangup_call= findViewById(R.id.button_hangup_call);
          micToggleVideoCall = findViewById(R.id.toggle_mic);
          toggle_speaker = findViewById(R.id.toggle_speaker);
          toggle_camera=findViewById(R.id.toggle_camera);
          billingStatus = findViewById(R.id.billing_status);
          text_remaining_minutes = findViewById(R.id.text_remaining_minutes);
          text_remaining_minutes.setVisibility(View.GONE);
          timerChronometer = findViewById(R.id.chronometer_timer_video_call);
          chronometer_timer_audio_call = findViewById(R.id.chronometer_timer_audio_call);
          ImageView_prescription = findViewById(R.id.ImageView_prescription);
          userDimv = findViewById(R.id.user_dimv);
          LinearLayout_chat= findViewById(R.id.LinearLayout_chat);
          certificate_View= findViewById(R.id.certificate_View);
          more_rlay= findViewById(R.id.more_rlay);
          ImageView_emotion=findViewById(R.id.ImageView_emotion);
          toggle_Max_Min = findViewById(R.id.toggle_Max_Min);
          emotion_rlay = findViewById(R.id.emotion_rlay);
          emotion_dimv = findViewById(R.id.emotion_dimv);
          reason_ctxv = findViewById(R.id.reason_ctxv);
          emotion_ctxv = findViewById(R.id.emotion_ctxv);
          ImageView_chat = findViewById(R.id.ImageView_chat);
          call_overlay = findViewById(R.id.call_overlay);
          connectionStatusLocal = findViewById(R.id.connectionStatusLocal);
          username_ctxv = findViewById(R.id.username_ctxv);
          layout_info_about_audio_call = findViewById(R.id.layout_info_about_audio_call);
          TextView_call_type= findViewById(R.id.TextView_call_type);

          this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                          WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                          WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                  WindowManager.LayoutParams.FLAG_FULLSCREEN |
                          WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                          WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

          //  ringtonePlayer = new RingtonePlayer(this, R.raw.beep);
          ringtonePlayer = new RingtonePlayer(this, R.raw.beep);
          ringtonePlayer.play(true);
          checker = new PermissionsChecker(VideoActivity.this);
          /*
           * Get shared preferences to read settings
           */
          preferences = PreferenceManager.getDefaultSharedPreferences(this);

          /*
           * Setup audio management and set the volume control stream
           */
          audioSwitch = new AudioSwitch(getApplicationContext());
          audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
          savedVolumeControlStream = getVolumeControlStream();
          setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
          if (isValid(getIntent()))
          {
              if (isValid(getIntent().getExtras()))
              {
                  accessToken=getIntent().getExtras().getString("accessToken");
                  roomName=getIntent().getExtras().getString("roomName");
                  pacifyerDetial= (MUser) getIntent().getExtras().getSerializable("pacifyerDetial");
                  isVideoCall=getIntent().getExtras().getBoolean("isVideoCall");
                  //SessionId=getIntent().getExtras().getString("SessionId");
              }
          }
          if (isVideoCall){
              primaryVideoView.setVisibility(View.VISIBLE);
              TextView_call_type.setText(getResources().getString(R.string.video_call));
              toggle_speaker.setVisibility(View.GONE);
              toggle_camera.setVisibility(View.VISIBLE);
              audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
              audioManager.setSpeakerphoneOn(true);
          }
          else {
              primaryVideoView.setVisibility(View.GONE);
              TextView_call_type.setText(getResources().getString(R.string.audio_call));
              toggle_speaker.setVisibility(View.VISIBLE);
              toggle_camera.setVisibility(View.GONE);
              audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
              audioManager.setSpeakerphoneOn(false);
          }
          /*
           * Check camera and microphone permissions. Needed in Android M.
           */
          if (!checkPermissionForCameraAndMicrophone()) {
              requestPermissionForCameraAndMicrophone();
          } else {
              createAudioAndVideoTracks();

          }

          intializeUI();
          connectToRoom(roomName);

          button_hangup_call.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  isCallActive=false;
                    if (isValid(ringtonePlayer))
                        ringtonePlayer.stop();
                  if (SessionId != null) {
                      hangUpCurrentSessionMe();
                      room.disconnect();
                  }

              }
          });

          micToggleVideoCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                  if (audioManager!=null)
                  {
                      boolean isMute = audioManager.isMicrophoneMute();
                      if (isMute)
                      {
                          audioManager.setMicrophoneMute(false);
                      }
                      else
                      {
                          audioManager.setMicrophoneMute(true);
                      }

                      int icon = isMute ?
                              R.drawable.call_mic_on : R.drawable.call_mic_off;
                      muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                              VideoActivity.this, icon));
                  }
              }
          });

          toggle_Max_Min.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (call_overlay.getOrientation() == LinearLayout.HORIZONTAL) {
                      call_overlay.setOrientation(LinearLayout.VERTICAL);
                      emotion_rlay.setVisibility(View.VISIBLE);
                      ImageView_emotion.setVisibility(View.GONE);
                      call_overlay.setBackgroundColor(Color.TRANSPARENT);
                      toggle_Max_Min.setImageDrawable(getResources().getDrawable(R.drawable.minimize_));
                  }
                  else
                  {
                      call_overlay.setOrientation(LinearLayout.HORIZONTAL);
                      toggle_Max_Min.setImageDrawable(getResources().getDrawable(R.drawable.maximize_));
                      emotion_rlay.setVisibility(View.GONE);
                      ImageView_emotion.setVisibility(View.VISIBLE);
                      call_overlay.setBackground(getResources().getDrawable(R.drawable.call_overlay_background));
                  }
              }
          });
                if (!isVideoCall)
                {
                    audioManager.setSpeakerphoneOn(false);
                }
          toggle_speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                  if (audioManager!=null)
                  {
                      boolean isSpeakerOn = audioManager.isSpeakerphoneOn();
                      if (isSpeakerOn)
                      {
                          audioManager.setSpeakerphoneOn(false);
                      }
                      else
                      {
                          audioManager.setSpeakerphoneOn(true);
                      }

                      int icon = isSpeakerOn ?
                              R.drawable.speaker_light_off : R.drawable.speaker_light;
                      muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                              VideoActivity.this, icon));
                  }
              }
          });


          toggle_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if (cameraCapturerCompat != null) {
                      CameraSource cameraSource = cameraCapturerCompat.getCameraSource();
                      cameraCapturerCompat.switchCamera();
                      if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                          thumbnailVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                      } else {
                          primaryVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                      }
                  }

              }
          });

          emotion_rlay.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(getApplicationContext(), ActivityChangeEmotionFromCall.class);
                  if (isValid(change_Emotion_name)) {
                      intent.putExtra("emotion", change_Emotion_name);
                  }
                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);

              }
          });

          ImageView_emotion.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  emotion_rlay.performClick();
              }
          });


          ImageView_chat.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(isValid(pacifyerDetial))
                      startChat(pacifyerDetial);
              }
          });

          ImageView_prescription.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  callPrescriptionData();
              }
          });

          more_rlay.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (isValid(pacifyerDetial)) {
                      CustomerDetailActivity.start(getApplicationContext(), pacifyerDetial);
                  }
              }
          });

          certificate_View.setVisibility(View.VISIBLE);
          certificate_View.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (isValid(NetworkService.mCurrentUser) && isValidSize(NetworkService.mCurrentUser.getCertificationList())) {
                      if (isValid(NetworkService.mCurrentUser.getCertificationList().get(0)) && isValidSize(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments())
                              && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0))
                              && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0))) {
                          String fileName=NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString();
                          if (isValid(fileName)) {
                           /* Intent i=new Intent(getActivity(), TermsOfUseActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("spurl",GLOBAL_IMAGE_URL+fileName);
                            getActivity().startActivity(i);*/
                              if (fileName.contains(".pdf")) {
                                  loadPDF(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString(),getResources().getString(R.string.kyc_document));
                              } else if (fileName.toLowerCase().contains(".jpg") || fileName.toLowerCase().contains(".jpeg") || fileName.toLowerCase().contains(".png")) {
                                  AttachmentImageActivity.start(VideoActivity.this, GLOBAL_IMAGE_URL + fileName, true);
                              } else {
                                  Toast.makeText(VideoActivity.this, "Unknown document format. Cannot open", Toast.LENGTH_LONG).show();
                              }

                          } else {
                              Toast.makeText(VideoActivity.this, getResources().getString(R.string.No_identity_document_available), Toast.LENGTH_LONG).show();
                          }
                      } else {
                          Toast.makeText(VideoActivity.this, getResources().getString(R.string.No_identity_document_available), Toast.LENGTH_LONG).show();
                      }
                  }
                  else {
                      Toast.makeText(VideoActivity.this, getResources().getString(R.string.No_identity_document_available), Toast.LENGTH_LONG).show();
                  }
              }
          });

          if (!isVideoCall)
          {
              layout_info_about_audio_call.setVisibility(View.VISIBLE);
              if (isValid(pacifyerDetial))
              {
                  if (isValid(pacifyerDetial.getFirstName()))
                  {
                      username_ctxv.setText(pacifyerDetial.getFirstName());
                  }
                  if (isValid(pacifyerDetial.getImageUrl()))
                  {
                      String image = pacifyerDetial.getImageUrl();
                      if(isValid(image)) {
                          Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.placeholder_image).into(userDimv);
                      }
                  }
              }
          }

          userDimv.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
               /* MUser mUser = getCallUser();
                if (isValid(mUser) && isValid(mUser.getId()))
                    ProfileScreenActivity.start(getApplicationContext(), mUser.getId(), false);*/
              }
          });
          getCurrentUserProfileInfo();

          /**out going call time after 30 seconds it will automatically end
           * marked as missed call **/
          startOutGoingCallTimer();
      }
      catch (Exception e)
      {
          Log.w("www",String.valueOf(e));
      }
    }



    private void hideToolBarAndButtons() {


       button_hangup_call.setVisibility(View.GONE);
        emotion_rlay.setVisibility(View.GONE);
        //userRlay.setVisibility(View.GONE);
        billingStatus.setVisibility(View.GONE);
       // call_layout.setVisibility(View.INVISIBLE);
        emotion_rlay.setVisibility(View.GONE);
        call_overlay.setVisibility(View.GONE);
        Log.d(TAG, "hideToolBarAndButtons");

    }

    private void showToolBarAndButtons() {

        //info_overlay_llay.setVisibility(View.VISIBLE);
        call_overlay.setVisibility(View.VISIBLE);
       // userRlay.setVisibility(View.VISIBLE);
       // call_layout.setVisibility(View.VISIBLE);
        if(call_overlay.getOrientation() == LinearLayout.VERTICAL)
        {
            emotion_rlay.setVisibility(View.VISIBLE);
        }
        else {
            emotion_rlay.setVisibility(View.GONE);
        }

        if (hasBillingStarted()) {
            billingStatus.setVisibility(View.VISIBLE);
        } else {
            billingStatus.setVisibility(View.INVISIBLE);
            button_hangup_call.setVisibility(View.VISIBLE);
        }


        Log.d(TAG, "showToolBarAndButtons");

    }


    private void startOutGoingCallTimer() {
        OutGoingCallTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (room != null && room.getRemoteParticipants().size()>0 && room.getState() == Room.State.CONNECTED) {
                    cancelOutGoingCallTimer();
                }
            }

            public void onFinish() {
                if (room != null && room.getRemoteParticipants().size()==0 || room.getState() == Room.State.CONNECTING) {
                    room.disconnect();
                    missedSessionWeb(SessionId);

                }
            }

        }.start();
    }

    //cancel OutGoingCallTimer
    void cancelOutGoingCallTimer() {
        if(OutGoingCallTimer!=null)
            OutGoingCallTimer.cancel();
    }

    private void getCurrentUserProfileInfo() {

        if (isValid(getPrefManager().getUserID())) {
            new NetworkService().incomingVideoCallUserEmotion(getPrefManager().getUserID(), new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    MEmotion emotion = null;
                    String image = null;

                    if (isValid(json)) {

                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                        if (isValid(NetworkService.mCurrentUser)) {
                            if (isValid(NetworkService.mCurrentUser.getEmotion()))
                                PacifyrActivity.call_emotion=NetworkService.mCurrentUser.getEmotion();
                            setEmotionViews(NetworkService.mCurrentUser.getEmotion());
                        }
                    }
                }
            });
        }
    }

    private void setEmotionViews(MEmotion emotion) {
        try{
            setResaonViews();
            String image=null;

            if(isValid(emotion.getImage())) {
                emotion_dimv.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(NetworkService.GLOBAL_IMAGE_URL + emotion.getImage()).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
                Picasso.with(getApplicationContext()).load(NetworkService.GLOBAL_IMAGE_URL + emotion.getImage()).placeholder(R.drawable.placeholder_image).into(ImageView_emotion);

            }
            if(isValid(emotion.getTitle())) {
                change_Emotion_name=emotion.getId();
                emotion_ctxv.setVisibility(View.VISIBLE);
                defSetText(emotion_ctxv,"Emotion: "+emotion.getTitle());

            }



        }catch (Exception e)
        {
            e.printStackTrace();
            Log.w("@@@@setEmotionViews",String.valueOf(e));
        }
    }


    private void setResaonViews() {
        try{

            reason_ctxv.setVisibility(View.VISIBLE);

                if (isValid(NetworkService.mCurrentUser)) {
                    if (isValid(NetworkService.mCurrentUser.getSelectedSpecialityCategory()))
                        defSetText(reason_ctxv, "Cause: " + NetworkService.mCurrentUser.getSelectedSpecialityCategory());
                    else if (isValid(NetworkService.mCurrentUser.getSelectedSpeciality()))
                        defSetText(reason_ctxv, "Cause: " + NetworkService.mCurrentUser.getSelectedSpeciality());
                } else {
                    defSetText(reason_ctxv, "Cause: " + EmotionSpecialityAdapter.reason_forcall);
                }


        }catch (Exception e)
        {
            reason_ctxv.setVisibility(View.GONE);
        }
    }


    public void callPrescriptionData() {
        try {
            setLoading();
            showProgress();
            PrefManager mPrefManager = PrefManager.getInstance(this);
            String token = mPrefManager.getAccessToken();
            String url = GLOBAL_URL + "prescription/list/user/"+mPrefManager.getUserID();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        Log.w("@@@",String.valueOf(json));
                        if (isValid(json))
                        {
                            PrescriptionList mPrescriptionList = GsonUtils.getInstance().gsonToPrescriptionList(json);
                            if (isValidSize(mPrescriptionList.getResults())) {

                                if (isValid(mPrescriptionList.getResults().get(0)) && isValid(mPrescriptionList.getResults().get(0).getText()))
                                {
                                    if (mPrescriptionList.getResults().get(0).getSessionId().equals(sessionid))
                                    {
                                        String mPrescriptionFile=mPrescriptionList.getResults().get(0).getText();
                                        if(isValid(mPrescriptionFile))
                                        {
                                        showPrescriptionFragment(mPrescriptionFile);

                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_prescription), Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
            Log.w("@@@",String.valueOf(ex));
        }

    }

    public void showPrescriptionFragment(String mPrescriptionFile) {
        Fragment fragment = new PrescriptionWhileCallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mPrescriptionFile", mPrescriptionFile);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }


    private void webGetSessionDetail(String sessionId) {

        try {
            setLoading();
            showProgress();

            if(!isValid(sessionId,"Network Interupted")) {
                finish();
                return;
            }
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    com.pacifyr.client.utils.LogUtils.LOGD("session", " PACA webGetSessionDetail callback result" + json + " " + status.getMessage());
                    if(!isValid(json,"Network Interupted"))
                    {
                        finish();
                    }
                    else {
                        try {
                            CallActivity.mSessionDetailRoot= GsonUtils.getInstance().gsonToMSessionDetailRoot(json);
                           /* if (isValid(getCallUser()))
                            {
                                setEmotionViews();
                            }*/

                        } catch (Exception e){
                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedIn(VideoActivity.this);
                            if (status != null) {
                                args.putString(FirebaseLogging.EC,
                                        "webGetSessionDetail=null,err=" + e.getMessage());
                            }
                            FirebaseLogging.sendLog(VideoActivity.this,
                                    FirebaseLogging.EVENT_INCOMING_CALL_FAIL,
                                    args);
                            finish();
                            com.pacifyr.client.utils.LogUtils.LOGD("session", " PACA webGetSessionDetail callback exce" + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getAccessToken());
            String url = "session/"+sessionId;
            aq.ajax(NetworkService.GLOBAL_URL + url, JSONObject.class, cb);
        } catch (Exception e){
            hideProgress();
            LogUtils.LOGD("session","PACA webGetSessionDetail exce " + e.getMessage());
            finish();
        }

    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_video_activity, menu);
        audioDeviceMenuItem = menu.findItem(R.id.menu_audio_device);

        /*
         * Start the audio device selector after the menu is created and update the icon when the
         * selected audio device changes.
         */
        audioSwitch.start((audioDevices, audioDevice) -> {
            updateAudioDeviceIcon(audioDevice);
            return Unit.INSTANCE;
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_audio_device:
                showAudioDevices();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            boolean cameraAndMicPermissionGranted = true;

            for (int grantResult : grantResults) {
                cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks();
            } else {
                Toast.makeText(this,
                        R.string.permissions_needed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(eventReceiver, new IntentFilter(CallEvent.CALL_EVENT));
        audioCodec = getAudioCodecPreference(PREF_AUDIO_CODEC,
                PREF_AUDIO_CODEC_DEFAULT);
        videoCodec = getVideoCodecPreference(PREF_VIDEO_CODEC,
                PREF_VIDEO_CODEC_DEFAULT);
        enableAutomaticSubscription = getAutomaticSubscriptionPreference(PREF_ENABLE_AUTOMATIC_SUBSCRIPTION,
                PREF_ENABLE_AUTOMATIC_SUBSCRIPTION_DEFAULT);
        final EncodingParameters newEncodingParameters = getEncodingParameters();

        /*
         * If the local video track was released when the app was put in the background, recreate.
         */
        if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
            localVideoTrack = LocalVideoTrack.create(this,
                    true,
                    cameraCapturerCompat.getVideoCapturer(),
                    LOCAL_VIDEO_TRACK_NAME);
            localVideoTrack.addRenderer(localVideoView);
            
            if (localParticipant != null) {
                localParticipant.publishTrack(localVideoTrack);
                if (!newEncodingParameters.equals(encodingParameters)) {
                    localParticipant.setEncodingParameters(newEncodingParameters);
                }
            }
        }
        encodingParameters = newEncodingParameters;

    }

    private EncodingParameters getEncodingParameters() {
        final int maxAudioBitrate = Integer.parseInt(
                preferences.getString(PREF_SENDER_MAX_AUDIO_BITRATE,
                        PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT));
        final int maxVideoBitrate = Integer.parseInt(
                preferences.getString(PREF_SENDER_MAX_VIDEO_BITRATE,
                        PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT));

        return new EncodingParameters(maxAudioBitrate, maxVideoBitrate);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(eventReceiver);
        /*
         * Release the local video track before going in the background. This ensures that the
         * camera can be used by other applications while this app is in the background.
         */
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /*
         * Tear down audio management and restore previous volume stream
         */
        isCallActive=false;
        audioSwitch.stop();
        setVolumeControlStream(savedVolumeControlStream);

        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }
        if (isValid(ringtonePlayer))
            ringtonePlayer.stop();

        cancelOutGoingCallTimer();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    R.string.permissions_needed,
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void createAudioAndVideoTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME);

        // Share your camera
        cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());
        localVideoTrack = LocalVideoTrack.create(this,
                true,
                cameraCapturerCompat.getVideoCapturer(),
                LOCAL_VIDEO_TRACK_NAME);
        primaryVideoView.setMirror(true);
        localVideoTrack.addRenderer(primaryVideoView);
        localVideoView = primaryVideoView;
    }

    private CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraSource.FRONT_CAMERA)) ?
                (CameraSource.FRONT_CAMERA) :
                (CameraSource.BACK_CAMERA);
    }


    private void connectToRoom(String roomName) {
        configureAudio(true);
       try {

        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                   .roomName(roomName)
                   .audioTracks(Collections.singletonList(localAudioTrack))
                   .videoTracks(Collections.singletonList(localVideoTrack))
                   .build();
           // Connect to room
           room = Video.connect(this, connectOptions, new Room.Listener() {
               @Override
               public void onConnected(Room room) {
                   VideoActivity.this.room=room;
                   localParticipant = room.getLocalParticipant();
                   if (isValid(connectionStatusLocal))
                   connectionStatusLocal.setText("Connecting...");
                   if(!isValid(SessionId))
                       startSessionWeb(roomName,accessToken,pacifyerDetial, isVideoCall);
                   for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                       addRemoteParticipant(remoteParticipant);
                       break;
                   }
               }

               @Override
               public void onConnectFailure(Room room, TwilioException e) {
                   audioSwitch.deactivate();
                   intializeUI();
                   if (isValid(ringtonePlayer))
                       ringtonePlayer.stop();
               }

               @Override
               public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {

               }

               @Override
               public void onReconnected(@NonNull Room room) {

               }

               @Override
               public void onDisconnected(Room room, TwilioException e) {
                   Log.w("####","onDisconnected");
                   localParticipant = null;
                   isCallActive=false;
                   if (isValid(ringtonePlayer))
                       ringtonePlayer.stop();
                 //  reconnectingProgressBar.setVisibility(View.GONE);
                   VideoActivity.this.room = null;
                   // Only reinitialize the UI if disconnect was not called from onDestroy()
                   if (!disconnectedFromOnDestroy) {
                       audioSwitch.deactivate();
                       intializeUI();
                       moveLocalVideoToPrimaryView();
                       hangUpCurrentSessionMe();
                   }
               }

               @Override
               public void onRecordingStarted(Room room) {
               }

               @Override
               public void onRecordingStopped(Room room) {
               }

               @Override
               public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                   Log.w("####","onParticipantConnected");
                   if (isValid(connectionStatusLocal))
                   connectionStatusLocal.setText("Connected");
                   if (isValid(ringtonePlayer))
                       ringtonePlayer.stop();
                   startTimer();
                   addRemoteParticipant(remoteParticipant);
                   isProviderConnected=true;

               }

               @Override
               public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {
                   Log.w("####","onParticipantDisconnected");
                   removeRemoteParticipant(remoteParticipant);
                   isCallActive=false;
                   if (room != null && room.getState() != Room.State.DISCONNECTED) {
                       room.disconnect();
                       stopTimer();
                       hangUpCurrentSessionMe();
                   }

               }

               @Override
               public void onDominantSpeakerChanged(@NonNull Room room, @Nullable RemoteParticipant remoteParticipant) {

               }

           });

       }
       catch(Exception e){
           Log.w("@@@",String.valueOf(e));
           Log.w("@@@",String.valueOf(e));
        }

    }

    private void configureAudio(boolean enable) {
        if (enable) {
            previousAudioMode = audioManager.getMode();
            // Request audio focus before making any device switch.
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            /*
             * Always disable microphone mute during a WebRTC call.
             */
            previousMicrophoneMute = audioManager.isMicrophoneMute();
            audioManager.setMicrophoneMute(false);
        } else {
            audioManager.setMode(previousAudioMode);
            audioManager.abandonAudioFocus(null);
            audioManager.setMicrophoneMute(previousMicrophoneMute);
        }
        audioManager.setSpeakerphoneOn(true);
    }

    /*
     * The initial state when there is no active room.
     */
    private void intializeUI() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_video_call_white_24dp));
        connectActionFab.show();
        connectActionFab.setOnClickListener(connectActionClickListener());
        switchCameraActionFab.show();
        switchCameraActionFab.setOnClickListener(switchCameraClickListener());
        localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());
        muteActionFab.show();
        muteActionFab.setOnClickListener(muteClickListener());
    }

    /*
     * Show the current available audio devices.
     */
    private void showAudioDevices() {
        AudioDevice selectedDevice = audioSwitch.getSelectedAudioDevice();
        List<AudioDevice> availableAudioDevices = audioSwitch.getAvailableAudioDevices();

        if (selectedDevice != null) {
            int selectedDeviceIndex = availableAudioDevices.indexOf(selectedDevice);

            ArrayList<String> audioDeviceNames = new ArrayList<>();
            for (AudioDevice a : availableAudioDevices) {
                audioDeviceNames.add(a.getName());
            }

            new AlertDialog.Builder(this)
                    .setTitle("R.string.room_screen_select_device")
                    .setSingleChoiceItems(
                            audioDeviceNames.toArray(new CharSequence[0]),
                            selectedDeviceIndex,
                            (dialog, index) -> {
                                dialog.dismiss();
                                AudioDevice selectedAudioDevice = availableAudioDevices.get(index);
                                updateAudioDeviceIcon(selectedAudioDevice);
                                audioSwitch.selectDevice(selectedAudioDevice);
                            }).create().show();
        }
    }

    /*
     * Update the menu icon based on the currently selected audio device.
     */
    private void updateAudioDeviceIcon(AudioDevice selectedAudioDevice) {
        int audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;

        if (selectedAudioDevice instanceof BluetoothHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_bluetooth_white_24dp;
        } else if (selectedAudioDevice instanceof WiredHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_headset_mic_white_24dp;
        } else if (selectedAudioDevice instanceof Earpiece) {
            audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;
        } else if (selectedAudioDevice instanceof Speakerphone) {
            audioDeviceMenuIcon = R.drawable.ic_volume_up_white_24dp;
        }

        audioDeviceMenuItem.setIcon(audioDeviceMenuIcon);
    }

    /*
     * Get the preferred audio codec from shared preferences
     */
    private AudioCodec getAudioCodecPreference(String key, String defaultValue) {
        final String audioCodecName = preferences.getString(key, defaultValue);

        switch (audioCodecName) {
            case IsacCodec.NAME:
                return new IsacCodec();
            case OpusCodec.NAME:
                return new OpusCodec();
            case PcmaCodec.NAME:
                return new PcmaCodec();
            case PcmuCodec.NAME:
                return new PcmuCodec();
            case G722Codec.NAME:
                return new G722Codec();
            default:
                return new OpusCodec();
        }
    }

    /*
     * Get the preferred video codec from shared preferences
     */
    private VideoCodec getVideoCodecPreference(String key, String defaultValue) {
        final String videoCodecName = preferences.getString(key, defaultValue);

        switch (videoCodecName) {
            case H264Codec.NAME:
                return new H264Codec();
            case Vp9Codec.NAME:
                return new Vp9Codec();
            default:
                return new Vp8Codec();
        }
    }

    private boolean getAutomaticSubscriptionPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }


    /*
     * The actions performed during disconnect.
     */
    private void setDisconnectAction() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_call_end_white_24px));
        connectActionFab.show();
        connectActionFab.setOnClickListener(disconnectClickListener());
    }

    /*
     * Creates an connect UI dialog
     */
    private void showConnectDialog() {
        EditText roomEditText = new EditText(this);
        connectDialog = Dialog.createConnectDialog(roomEditText,
                connectClickListener(roomEditText),
                cancelConnectDialogClickListener(),
                this);
        connectDialog.show();
    }

    /*
     * Called when remote participant joins the room
     */
    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            Snackbar.make(connectActionFab,
                    "Multiple participants are not currently support in this UI",
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        remoteParticipantIdentity = remoteParticipant.getIdentity();

        /*
         * Add remote participant renderer
         */
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                addRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }

        /*
         * Start listening for participant events
         */
        remoteParticipant.setListener(remoteParticipantListener());
    }

    /*
     * Set primary view as renderer for participant video track
     */
    private void addRemoteParticipantVideo(VideoTrack videoTrack) {
        if (isVideoCall)
        {
            moveLocalVideoToThumbnailView();
            primaryVideoView.setMirror(false);
            videoTrack.addRenderer(primaryVideoView);
        }

    }

    private void moveLocalVideoToThumbnailView() {

            try {
                if (thumbnailVideoView.getVisibility() == View.GONE) {
                    thumbnailVideoView.setVisibility(View.VISIBLE);
                    localVideoTrack.removeRenderer(primaryVideoView);
                    localVideoTrack.addRenderer(thumbnailVideoView);
                    localVideoView = thumbnailVideoView;
                    thumbnailVideoView.setMirror(cameraCapturerCompat.getCameraSource() ==
                            CameraSource.FRONT_CAMERA);
                }
            }
            catch (Exception e)
            {

            }
    }

    /*
     * Called when remote participant leaves the room
     */
    @SuppressLint("SetTextI18n")
    private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {
        if (!remoteParticipant.getIdentity().equals(remoteParticipantIdentity)) {
            return;
        }

        /*
         * Remove remote participant renderer
         */
        if (!remoteParticipant.getRemoteVideoTracks().isEmpty()) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Remove video only if subscribed to participant track
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                removeParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        moveLocalVideoToPrimaryView();
    }

    private void removeParticipantVideo(VideoTrack videoTrack) {
        videoTrack.removeRenderer(primaryVideoView);
    }

    private void moveLocalVideoToPrimaryView() {
        if (isVideoCall)
        {
            if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                thumbnailVideoView.setVisibility(View.GONE);
                if (localVideoTrack != null) {
                    localVideoTrack.removeRenderer(thumbnailVideoView);
                    localVideoTrack.addRenderer(primaryVideoView);
                }
                localVideoView = primaryVideoView;
                primaryVideoView.setMirror(cameraCapturerCompat.getCameraSource() ==
                        CameraSource.FRONT_CAMERA);
            }
        }
    }




    @SuppressLint("SetTextI18n")
    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getVideoTrack(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
                if (isValid(remoteVideoTrackPublication.getVideoTrack()))
                addRemoteParticipantVideo(remoteVideoTrackPublication.getVideoTrack());
            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onAudioTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
            }

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));
            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));
            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {
                Log.i(TAG, String.format("onDataTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));
                /* if (remoteParticipant == null) {
                    addParticipant(remoteParticipant);
                }*/
                if (isValid(remoteVideoTrack))
                addRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));
                removeParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onVideoTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
                Snackbar.make(connectActionFab,
                        String.format("Failed to subscribe to %s video track",
                                remoteParticipant.getIdentity()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.w("wdqfq","onAudioTrackEnabled");
            }

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.w("wdqfq","onAudioTrackDisabled");
            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.w("wdqfq","onVideoTrackEnabled");
            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.w("wdqfq","onVideoTrackDisabled");
            }
        };
    }


    private DialogInterface.OnClickListener connectClickListener(final EditText roomEditText) {
        return (dialog, which) -> {
            /*
             * Connect to room
             */
           // connectToRoom(roomEditText.getText().toString());
        };
    }

    private View.OnClickListener disconnectClickListener() {
        return v -> {
            /*
             * Disconnect from room
             */
            if (room != null) {
                room.disconnect();
            }
            intializeUI();
        };
    }

    private View.OnClickListener connectActionClickListener() {
        return v -> showConnectDialog();
    }

    private DialogInterface.OnClickListener cancelConnectDialogClickListener() {
        return (dialog, which) -> {
            intializeUI();
            connectDialog.dismiss();
        };
    }

    private View.OnClickListener switchCameraClickListener() {
        return v -> {
            if (cameraCapturerCompat != null) {
                CameraSource cameraSource = cameraCapturerCompat.getCameraSource();
                cameraCapturerCompat.switchCamera();
                if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                    thumbnailVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                } else {
                    primaryVideoView.setMirror(cameraSource == CameraSource.BACK_CAMERA);
                }
            }
        };
    }

    private View.OnClickListener localVideoClickListener() {
        return v -> {
            /*
             * Enable/disable the local video track
             */
            if (localVideoTrack != null) {
                boolean enable = !localVideoTrack.isEnabled();
                localVideoTrack.enable(enable);
                int icon;
                if (enable) {
                    icon = R.drawable.ic_videocam_white_24dp;
                    switchCameraActionFab.show();
                } else {
                    icon = R.drawable.ic_videocam_off_black_24dp;
                    switchCameraActionFab.hide();
                }
                localVideoActionFab.setImageDrawable(
                        ContextCompat.getDrawable(VideoActivity.this, icon));
            }
        };
    }

    private View.OnClickListener muteClickListener() {
        return v -> {
            /*
             * Enable/disable the local audio track. The results of this operation are
             * signaled to other Participants in the same Room. When an audio track is
             * disabled, the audio is muted.
             */
            if (localAudioTrack != null) {
                boolean enable = !localAudioTrack.isEnabled();
                localAudioTrack.enable(enable);
                int icon = enable ?
                        R.drawable.ic_mic_white_24dp : R.drawable.ic_mic_off_black_24dp;
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                        VideoActivity.this, icon));
            }
        };
    }


    public void setAvaiableTimer(long availableseconds) {
        try {
            isFirsttime = false;
            if (availableseconds < 30) {

                    if (!getCountryChecks().contains(getPrefManager().getCountry()))
                        postEventOnMainThread(new CallTimerEvent(true));
                    return;
            }
            availableseconds = availableseconds * 1000;
            Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            //Log.d("session", "BCF setAvaiableTimer availableseconds = " + availableseconds);
            timer = new CountDownTimer(availableseconds, 10000) {

                public void onTick(long millisUntilFinished) {
                    Log.d("session", "BCF CountDownTimer millisUntilFinished = " + millisUntilFinished);
                    if ((300 >= (millisUntilFinished / 10000) && !isFirsttime)) {
                        isFirsttime = true;
                        setAlertStatusInBlue2("Add Card", "Your free minutes are expired. Please continue enjoying our services by updating your credit card information", "Ok", "NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideProgress();
                                hideStatus2();
                                setAlertAddCardInBlue2();
                                //  ToastHandler.newInstance(getActivity()).mustShowToast("Works Under progress");

                                // AlertAddCreditCardActivity.start(getActivity());
                            }
                        });
                    }

                }

                public void onFinish() {
                    Log.d("session", "BCF CountDownTimer onFinish  ");
                    postEventOnMainThread(new CallTimerEvent(true));
                }

            };
        } catch (Exception e) {
            Log.d("session", "BCF setAvaiableTimer exception = " + e.getMessage());
        }
    }

    public void stopTimer() {


        if (!isVideoCall)
        {

            if (chronometer_timer_audio_call != null) {
                chronometer_timer_audio_call.stop();
                isStarted = false;
            }
        }
        else {
            if (timerChronometer != null) {
                timerChronometer.stop();
                isStarted = false;
            }
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    public void startTimer() {
        if (!isStarted) {

            if (isVideoCall) {
                timerChronometer.setVisibility(View.VISIBLE);
                timerChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        int h = (int) (time / 3600000);
                        int m = (int) (time - h * 3600000) / 60000;
                        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                        String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                        chronometer.setText(t);
                    }
                });

                timerChronometer.setBase(SystemClock.elapsedRealtime());
                timerChronometer.start();
                if (isCustomer())
                    if (!isCardExist()) {
                        if (isValid(NetworkService.mCurrentUser)) {
                            long time = NetworkService.mCurrentUser.getAvailableMinutes();
                            setAvaiableTimer((time * 60) - 15);

                        }


                    }
                isStarted = true;
            }
            else {
                chronometer_timer_audio_call.setVisibility(View.VISIBLE);
                chronometer_timer_audio_call.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        int h = (int) (time / 3600000);
                        int m = (int) (time - h * 3600000) / 60000;
                        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                        String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                        chronometer.setText(t);
                    }
                });

                chronometer_timer_audio_call.setBase(SystemClock.elapsedRealtime());
                chronometer_timer_audio_call.start();
                if (isCustomer())
                    if (!isCardExist()) {
                        if (isValid(NetworkService.mCurrentUser)) {
                            long time = NetworkService.mCurrentUser.getAvailableMinutes();
                            setAvaiableTimer((time * 60) - 15);

                        }


                    }
                isStarted = true;
            }
        }
    }

    public void hangUpCurrentSessionMe() {
        Log.d(TAG, "hangUpCurrentSessionMe");
        Bundle args =
                FirebaseLogging.getConstantArgsLoggedIn(VideoActivity.this);
        if (getCallPacifyr() != null) {
            args.putString(FirebaseLogging.EC, getCallPacifyr().getId());
        }
        FirebaseLogging.sendLog(VideoActivity.this,
                isVideoCall ? FirebaseLogging.EVENT_VIDEOCALL_STOPPED :
                        FirebaseLogging.EVENT_AUDIOCALL_STOPPED,
                args);
        LogUtils.LOGD("call","hangUpCurrentSession");
        stopSessionTracking();
        //jpppppppppppppp
        try {
            LogUtils.LOGD("call","CA hangUpCurrentSessionMe sessionid"+SessionId);
            if(isOpponentConnected())
            {

                if (hasBillingStarted())
                {
                    SessionSummaryActivity.start(VideoActivity.this, SessionId);
                    getPrefManager().saveSessionId(null);
                }
                else
                {
                    missedSessionWeb(SessionId);
                }
            } else if (!hasBillingStarted()) {
                showToast("Thank you for the call. You are not billed as Service was not provided");
                if (isProd==false &&isRelease==false)
                {
                    if (isProviderConnected==false){
                        ToastHandler.newInstance(getApplicationContext()).showToast("Pacifyr is busy with another call");
                    }else {
                        ToastHandler.newInstance(getApplicationContext()).showToast("Thank you for the call. You are not billed as Service was not provided");
                    }
                }
            }

            if (SessionId != null) {
                stopSessionWeb(SessionId);
            }
            finish();
        }
        catch (Exception e)
        {
            LogUtils.LOGD("call","CA hangUpCurrentSessionMe Exception e "+ e.getMessage());
        }
        isCallActive = false;
        BusProvider.getInstance().post(new CallStatus(isCallActive));
        //jpppppppppppppppppp

    }

    private boolean isOpponentConnected() {
        if (isValid(room) && isValid(room.getRemoteParticipants()) && room.getRemoteParticipants().size()>1)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public MUser getCallPacifyr() {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getPacifyr();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }


    public MUser getCallUser() {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }


    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.shortToast(message);
            }
        });
    }

    public CallTimerEvent callTimerEvent =null;
    @Subscribe
    public void onCallTimerEvent(CallTimerEvent event) {
        try{
            if(event.isFinishCall) {
                callTimerEvent=event;

            }
        }catch (Exception e)
        {
            Log.d("session", "CA onCallTimerEvent exception = " + e.getMessage());
        }

    }

    public boolean isCustomer()
    {
        boolean isCustomer=false;
        try{

            try {
                String label= PrefManager.getInstance(getApplicationContext()).getUserType();
                if(label.equals("customer")) {
                    isCustomer=true;
                }
                else if(label.equalsIgnoreCase("pacifyr"))
                    isCustomer=false;

            } catch (Exception t) {
                isCustomer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isCustomer=false;
        }
        return isCustomer;
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangedEmotionEvent(CallChangeEmotion event) {
        try {
            if (isValid(event.getEmotion()))
                if (isValid(event.getEmotion().getImage())) {
                    emotion_dimv.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(NetworkService.GLOBAL_IMAGE_URL + event.getEmotion().getImage()).placeholder(R.drawable.placeholder_image).into(emotion_dimv);
                    Picasso.with(this).load(NetworkService.GLOBAL_IMAGE_URL + event.getEmotion().getImage()).placeholder(R.drawable.placeholder_image).into(ImageView_emotion);
                }

            if (isValid(event.getEmotion().getTitle())) {
                emotion_ctxv.setVisibility(View.VISIBLE);
                change_Emotion_name=event.getEmotion().getId();
                defSetText(emotion_ctxv,"Emotion: "+event.getEmotion().getTitle());


            }

            if (isCallActive) {
                Intent i = new Intent(VideoActivity.this, VideoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallConnected(TwilioCallEvent mTwilioCallEvent) {
        isCallActive=true;
        if (isValid(mTwilioCallEvent.getIdsession()))
        {
            SessionId= mTwilioCallEvent.getIdsession();
            webGetSessionDetail(SessionId);
        Log.w("WWWWW","onCallConnected");
        }

    }

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
                    CallActivity.isMissed=true;
                    if (json == null) {

                        ToastHandler.newInstance(getApplicationContext()).showToast("Please Try again");
                        LogUtils.LOGD("call", "missedSessionWeb json null ");
                    } else {
                        if (json != null) try {

                            LogUtils.LOGD("call", "missedSessionWeb json  " + json);
                        } catch (Exception e){
                            ToastHandler.newInstance(getApplicationContext()).showToast("Please Try again");
                            LogUtils.LOGD("call", "missedSessionWeb exception : " + e.getMessage());
                        }
                    }
                    finish();
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

    /**Picture-in-picture Support **/

    @Override
    protected void onUserLeaveHint() {
        if (isCallActive) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder pictureParams = new PictureInPictureParams.Builder();
                pictureParams.setAspectRatio(new Rational(1200, 720)).build();
                enterPictureInPictureMode(pictureParams.build());
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {

        onPictureInPicture(new PicInPic(isInPictureInPictureMode));
        setShowDialogs(!isInPictureInPictureMode);
        if (!isInPictureInPictureMode) {
            maximizePicInPicView();
        }
    }

    public void onPictureInPicture(PicInPic picInPic) {
        if (picInPic.isEnabled()) {
            hideToolBarAndButtons();
        } else {
            showToolBarAndButtons();
        }
    }


    @Override
    public void onBackPressed() {

    }
}
