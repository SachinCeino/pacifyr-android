package com.pacifyr.client.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.CertificationProfileAdapter;
import com.pacifyr.client.adapter.EducationProfileAdapter;
import com.pacifyr.client.adapter.SpecilaListProfileAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.ScheduleBottomSheetFragment;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.PermissionsActivity;
import com.pacifyr.client.quickblox.db.QbUsersDbManager;
import com.pacifyr.client.quickblox.utils.Consts;
import com.pacifyr.client.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.client.utility.EventMessage;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Certification;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ChatSubscription;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.quickblox.activities.CallActivity.isCallActive;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ProfileScreenActivity extends PacifyrAppCompactActivity {


    private LinearLayout profileLlay;
    private MainTextView profileNameCtxv;
    private MainTextView connect_pacifyr_ctxv;
    private AppCompatRatingBar profileRating;
    private MainTextView ratingCtxv;
    private CircleImageView profileCimv;
    private MainTextView isonlineCtxv;
    private MainTextView dollarSignCtxv;
    private MainTextView priceCtxv;
    private MainTextView cost_label;
    private CardView snapshotCard;
    private MainTextView snapshotCtxv;
    private CardView blogCard;
    private MainTextView websiteCtxv;
    private CardView specialityCard;
    private RecyclerView specialRcv;
    private CardView educationCard;
    private RecyclerView educationRcv;
    private CardView certificationCard;
    private RecyclerView certificationRcv;
    private ImageView indicateOnline;
    private RelativeLayout topRlay;

    public String userID;
    public Long qbID;
    public Integer position;
    public static ModelUserProfile pacifyerDetial;
    public static MUser mpacifyerDetial;

  //  public static UserDetailsModel userDetailsModel;

    private RecyclerView SpecialRecyclerView;
    private RecyclerView.Adapter SpecialAdapter;
    private RecyclerView.LayoutManager SpecialLayoutManager;

    private RecyclerView CertificateRecyclerView;
    private RecyclerView.Adapter CertificateAdapter;
    private RecyclerView.LayoutManager CertificateLayoutManager;

    private RecyclerView EducationRecyclerView;
    private RecyclerView.Adapter EducationAdapter;
    private RecyclerView.LayoutManager EducationLayoutManager;

    private LinearLayout call_llay;
    private ArrayList<String> myList=new ArrayList();
    private ArrayList<Certification> certificateList= new ArrayList<>();
    private ArrayList<ModelUserProfile.Education> educationsList= new ArrayList<>();
    private String imgUrl;
    private CircleImageView startVideoCimv;
    private CircleImageView startAudioCimv;
    private CircleImageView startChatCimv;
    private QBChatService chatService;

    private LinearLayout chatLlay;
    private MainTextView chatPriceCtxv;

  //  private PermissionsChecker checker;
    private QBUser currentUser;
  //  private ArrayList<QBUser> currentOpponentsList;
    private QbUsersDbManager dbManager;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;

    private static boolean isFromConnectPacifyr=false;
    private boolean isChatSubscriptionRequired;
    private int priceForChat;
    private PrefManager prefM;
    private Context context;
    private ImageView schedule_rlay;
    private LinearLayout linearLayout_chat_price_description_layout;

    public static void start(Context context,String id,boolean flag ) {
        Intent intent = new Intent(context, ProfileScreenActivity.class);
        intent.putExtra("userid",id);
        isFromConnectPacifyr=flag;
        context.startActivity(intent);
    }
    public static void start(Context context,boolean isFromConnectPacifyr ) {
        Intent intent = new Intent(context, ProfileScreenActivity.class);

        isFromConnectPacifyr=isFromConnectPacifyr;
        context.startActivity(intent);
    }
    public static void start(Context context ) {
        Intent intent = new Intent(context, ProfileScreenActivity.class);

        isFromConnectPacifyr=false;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        linearLayout_chat_price_description_layout = findViewById(R.id.chat_price_layout);
        if (getApplicationContext().getPackageName().equals("com.pacifyr.leadersup"))
            linearLayout_chat_price_description_layout.setVisibility(View.GONE);

        initFields();
        //checker = new PermissionsChecker(ProfileScreenActivity.this);
      //  permissionCheck();
        context = ProfileScreenActivity.this;
        prefM = PrefManager.getInstance(context);
        Bundle extras = getIntent().getExtras();
        //position=extras.getInt("position");
        //String image=searchResults.get(position).getImage();
        userID = extras.getString("userid");
        onSetActionBar();
        initializeViews();
        if (savedInstanceState == null)
        {
            try {
                Bundle extras2 = getIntent().getExtras();
                //position=extras.getInt("position");
                //String image=searchResults.get(position).getImage();
                userID = extras2.getString("userid");
                //NetworkService.startActionProfile(this,userID);

                int delay = 0; // delay for 0 sec.
                int period = 10000; // repeat every 10 sec.
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask()
                {
                    public void run()
                    {
                        handleActionProfile(userID);
                        //Call function
                    }
                }, delay, period);

                if(isFromConnectPacifyr) {
                    connect_pacifyr_ctxv.setVisibility(View.GONE);
                    call_llay.setVisibility(View.VISIBLE);
                }
                else
                {
                    connect_pacifyr_ctxv.setVisibility(View.VISIBLE);
                    call_llay.setVisibility(View.GONE);

                }


                connect_pacifyr_ctxv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isValid(mpacifyerDetial,"Please check network connecction"))
                            return;
                        ConnectToSinglePacifyrActivity.start(ProfileScreenActivity.this,mpacifyerDetial);
                    }
                });

                startVideoCimv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValid(isCallActive) && isCallActive)
                            return;
                        if (!isValid(mpacifyerDetial, "Please check network connection"))
                            return;

                        if(!mpacifyerDetial.isReadyToCall()) {

                            boolean isOnline = mpacifyerDetial.getOnline();
                            String reason = "";
                            if (isOnline) {
                                if (!mpacifyerDetial.getCurrentlyAvailable()) {
                                    reason = " in a session";
                                }
                            } else {
                                reason = " offline";
                            }
                            offlineSnackbar = Snackbar.make(topRlay, mpacifyerDetial.getName() +
                                    " is currently " + reason + ". You can either chat with " + mpacifyerDetial.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                            View snackbarView = offlineSnackbar.getView();
                            TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                            snackTextView.setMaxLines(4);
                            offlineSnackbar.show();
                            //ToastHandler.newInstance(ProfileScreenActivity.this).mustShowToast(mpacifyerDetial.getName() + " is currently offline. You can either chat with " + mpacifyerDetial.getName() + " or try agein later");
                            return;
                        }


                        if (checker.lacksPermissions(Consts.PERMISSIONS)) {

                            startPermissionsActivity(false,ProfileScreenActivity.this);

                            if(isLoggedInChat()) {

                                setAlertStartCall(mpacifyerDetial.getFirstName()+""+mpacifyerDetial.getLastName(),
                                        Utility.getPriceSign(ProfileScreenActivity.this) + mpacifyerDetial.getPrice(), NetworkService.GLOBAL_IMAGE_URL + mpacifyerDetial.getImage()
                                        , new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hideStatus();
                                                checkRoleRequest(mpacifyerDetial, listner, true);
                                            }
                                        });

                                //callRequestWeb(pacifyer_obj, listner, true);
                            }

                        }
                        else {
                            if(isLoggedInChat()) {

                                setAlertStartCall(mpacifyerDetial.getFirstName()+""+mpacifyerDetial.getLastName(),
                                        Utility.getPriceSign(ProfileScreenActivity.this) + mpacifyerDetial.getPrice(), NetworkService.GLOBAL_IMAGE_URL + mpacifyerDetial.getImage()
                                        , new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hideStatus();
                                                checkRoleRequest(mpacifyerDetial, listner, true);
                                            }
                                        });

                                //callRequestWeb(pacifyer_obj, listner, true);
                            }
                        }


            /*            if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                            startPermissionsActivity(false,ProfileScreenActivity.this);
                        }
                        else {
                            if(isLoggedInChat()) {
                                //callRequestWeb(mpacifyerDetial, listner, true);
                                if(mpacifyerDetial.isReadyToCall()) {
                                    checkRoleRequest(mpacifyerDetial, listner, true);
                                }
                                else
                                {
                                    ToastHandler.newInstance(ProfileScreenActivity.this).mustShowToast(mpacifyerDetial.getName()+" is offline. Please try later.");
                                }

                            }
                        }*/
                    }
                });

                startAudioCimv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValid(isCallActive) && isCallActive)
                            return;
                        if(!isValid(mpacifyerDetial,"Please check network connecction"))
                            return;

                        if(!mpacifyerDetial.isReadyToCall()) {

                            offlineSnackbar = Snackbar.make(topRlay, mpacifyerDetial.getName() + " is currently offline. You can either chat with " + mpacifyerDetial.getName() + " or try again later.", Snackbar.LENGTH_LONG);
                            View snackbarView = offlineSnackbar.getView();
                            TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                            snackTextView.setMaxLines(4);
                            offlineSnackbar.show();
                            return;
                        }

                        if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {

                            startPermissionsActivity(true,ProfileScreenActivity.this);

                            if(isLoggedInChat()) {

                                setAlertStartCall(mpacifyerDetial.getFirstName()+""+mpacifyerDetial.getLastName(),
                                        Utility.getPriceSign(ProfileScreenActivity.this) + mpacifyerDetial.getPrice(), NetworkService.GLOBAL_IMAGE_URL + mpacifyerDetial.getImage()
                                        , new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hideStatus();
                                                checkRoleRequest(mpacifyerDetial, listner, true);
                                            }
                                        });

                                //callRequestWeb(pacifyer_obj, listner, true);
                            }
                        }
                        else {

                            if(isLoggedInChat()) {

                                setAlertStartCall(mpacifyerDetial.getFirstName()+""+mpacifyerDetial.getLastName(),
                                        Utility.getPriceSign(ProfileScreenActivity.this) + mpacifyerDetial.getPrice(), NetworkService.GLOBAL_IMAGE_URL + mpacifyerDetial.getImage()
                                        , new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                hideStatus();
                                                checkRoleRequest(mpacifyerDetial, listner, false);
                                            }
                                        });

                                //callRequestWeb(pacifyer_obj, listner, true);
                            }
                        }


                 /*       if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                            startPermissionsActivity(true,ProfileScreenActivity.this);
                        }
                        else {

                            if(isLoggedInChat()) {

                                if(mpacifyerDetial.isReadyToCall()) {
                                    checkRoleRequest(mpacifyerDetial, listner, false);
                                }
                                else
                                {
                                    ToastHandler.newInstance(ProfileScreenActivity.this).mustShowToast(mpacifyerDetial.getName()+" is offline. Please try later.");
                                }
                                //callRequestWeb(mpacifyerDetial, listner, false);
                            }
                        }*/
                    }
                });

                startChatCimv.setVisibility(View.VISIBLE);

               /* if(mpacifyerDetial.isChatEnabled())
                    startChatCimv.setVisibility(View.VISIBLE);*/

                startChatCimv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        isChatSubscriptionRequired = mpacifyerDetial.isChatSubscriptionRequired();
                        priceForChat = Integer.parseInt(mpacifyerDetial.getPriceForChat());

                        if (getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
                        {
                            startChat(mpacifyerDetial);
                        }
                        else
                        {
                            if(!isChatSubscriptionRequired) {
                                startChat(mpacifyerDetial);
                                //showIOSDialogOK("","Please purchase unlimited chat per day","OK");

                            }else if(priceForChat==0){
                                startChat(mpacifyerDetial);
                            }else {
                                //chatLlay.setVisibility(View.VISIBLE);

                                if (mpacifyerDetial.getChatSubscription() == null) {
                                    showIOSDialogOK("","Please purchase unlimited chat per day","OK");
                                    //chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                                } else {
                                    startChat(mpacifyerDetial);
                                }
                            }
                        }


                        /*ChatSubscription subscription = mpacifyerDetial.getChatSubscription();

                        if(!isValid(subscription)){
                            showIOSDialogOK("","Please purchase unlimited chat per day","OK");
                        }else {
                            startChat(mpacifyerDetial);
                        }*/
                        //   ToastHandler.newInstance(getActivity()).mustShowToast("Works Under Progress");

                    }
                });

                schedule_rlay = findViewById(R.id.schedule_rlay);
                if (getApplicationContext().getPackageName().equals("com.pacifyr.leadersup") ||
                        getApplicationContext().getPackageName().equals("com.pacifyr.pam")) {
                    schedule_rlay.setVisibility(View.VISIBLE);
                } else {
                    schedule_rlay.setVisibility(View.GONE);
                }

                schedule_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showScheduleDialogueFragment(mpacifyerDetial);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        }
    }

    private void showScheduleDialogueFragment(MUser mpacifyerDetial) {

        if (isValid(mpacifyerDetial)) {
            if (getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
            {
                if (isValid(getPrefManager().getGENDER()) && isValid(getPrefManager().getZIP())) {
                    ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("OBJECT", mpacifyerDetial);
                    bottomSheetFragment.setArguments(bundle);
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                }
                else
                { showMandatoryFieldsPopUp();
                }
            }
            else
            {
                ScheduleBottomSheetFragment bottomSheetFragment = new ScheduleBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("OBJECT", mpacifyerDetial);
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleActionProfile(userID);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handleActionProfile(userID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // check if the request code is same as what is passed  here it is 2
            if(requestCode == PermissionsActivity.PERMISSION_EXTERNAL_REQUEST_CODE) {
                boolean granted = data.getBooleanExtra("permissions", false);
                boolean audioonly = data.getBooleanExtra(PermissionsActivity.CHECK_ONLY_AUDIO, false);
                if(granted) {
                    if(audioonly) {
                        if(checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                            callRequestWeb(mpacifyerDetial, listner, false);
                        }
                    }
                    else {
                        if(checker.lacksPermissions(Consts.PERMISSIONS)) {
                            callRequestWeb(mpacifyerDetial, listner, true);
                        }
                    }

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.profile_llay);
    }


    private void initFields() {
      /*  Bundle extras = getIntent(
      ).getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }*/

        currentUser = sharedPrefsHelper.getQbUser();
        dbManager = QbUsersDbManager.getInstance(ProfileScreenActivity.this);
        webRtcSessionManager = WebRtcSessionManager.getInstance(ProfileScreenActivity.this);
    }


    private void handleActionProfile(String id) {
        try {
            showProgress();
            //prefrence manager is set at login and signup
            PrefManager prefM = PrefManager.getInstance(ProfileScreenActivity.this);
            String token = prefM.getAccessToken();
        /*String userid = prefM.getUserID();*/
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            String url = GLOBAL_URL + "user/profile/" + id;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);
                        mpacifyerDetial=GsonUtils.getInstance().gsonToMUser(json);

                        //userDetailsModel=GsonUtils.getInstance().gsonToUserDetailsModel(json);
                        updateProfile();
                    /*if(isValid(pacifyerDetial)) {
                        updateProfile();
                    }
                    else {
                        updateProfile(json);
                    }*/
                        EventBus.getDefault().post(new EventMessage(getString(R.string.event_user_profile)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            try {
                AQuery aq = new AQuery(ProfileScreenActivity.this);
                //cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                aq.ajax(url, JSONObject.class, cb);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }



    public void updateProfile()
    {
        try {

            if (isValid(pacifyerDetial.getQbid()))
                qbID=pacifyerDetial.getQbid();
            hideProgress();
            if(isValid(pacifyerDetial.getImageUrl())) {
                imgUrl = pacifyerDetial.getImageUrl();
                if (isValidhtml(imgUrl))
                    Picasso.with(this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(profileCimv);
            }
            if (isValid(pacifyerDetial.getDescription()))
                snapshotCtxv.setText(pacifyerDetial.getDescription());
                else
                snapshotCard.setVisibility(View.GONE);

            if (isValid(pacifyerDetial.getFirstName()) && isValid(pacifyerDetial.getLastName()))
                profileNameCtxv.setText(pacifyerDetial.getFirstName() + pacifyerDetial.getLastName());
            else if(isValid(pacifyerDetial.getFirstName()))
                profileNameCtxv.setText(pacifyerDetial.getFirstName());

            if(isValid(pacifyerDetial.getPrice()))
            {
                priceCtxv.setText(Utility.getPriceSign(ProfileScreenActivity.this)+" "+pacifyerDetial.getPrice());
            }



            if (isValid(pacifyerDetial.getRating())){
                profileRating.setRating(Float.valueOf(pacifyerDetial.getRating()));
            }

            if (isValid(pacifyerDetial.getNumberOfReviews()))
            {
                ratingCtxv.setText(pacifyerDetial.getNumberOfReviews() + " Reviews");
            }

            if (isValid(pacifyerDetial.getWebsiteURL())) {
                websiteCtxv.setText(pacifyerDetial.getWebsiteURL());
            }
            else {
                blogCard.setVisibility(View.GONE);
            }

            if (pacifyerDetial.getOnline())
            {
                indicateOnline.setImageDrawable(getResources().getDrawable(R.drawable.online));
                isonlineCtxv.setText("ONLINE");
            }else {
                indicateOnline.setImageDrawable(getResources().getDrawable(R.drawable.offline));
                isonlineCtxv.setText("OFFLINE");
            }

            //String ed[] = searchResults.get(position).getEducation();

            myList = new ArrayList<String>();
            for (int i = 0; i < pacifyerDetial.getSpecialities().length; i++) {
                String data = pacifyerDetial.getSpecialities()[i];
                myList.add(data);
            }

            //getting list values

            if (isValid(pacifyerDetial.getSpecialities())) {

                String[] spec=pacifyerDetial.getSpecialities();

                MainTextView spCtxv = findViewById(R.id.special_list_ctxv);
                spCtxv.setText(TextUtils.join(", ",spec));
                //Speciality adapter
               /* SpecialRecyclerView = (RecyclerView) findViewById(R.id.special_rcv);
                SpecialLayoutManager = new LinearLayoutManager(this);
                SpecialRecyclerView.setLayoutManager(SpecialLayoutManager);
                SpecialAdapter = new SpecilaListProfileAdapter(spec, ProfileScreenActivity.this);
                SpecialRecyclerView.setAdapter(SpecialAdapter);*/
            } else {
                specialityCard.setVisibility(View.GONE);
            }
            educationsList = pacifyerDetial.getEducationList();
            if (isValid(educationsList)) {
                //education adapter
                EducationRecyclerView = findViewById(R.id.education_rcv);
                EducationLayoutManager = new LinearLayoutManager(this);
                EducationRecyclerView.setLayoutManager(EducationLayoutManager);
                EducationAdapter = new EducationProfileAdapter(educationsList, ProfileScreenActivity.this);
                EducationRecyclerView.setAdapter(EducationAdapter);
            } else {
                educationCard.setVisibility(View.GONE);
            }
            certificateList = pacifyerDetial.getCertificationList();
            if (isValid(certificateList)) {
                //certificate adapter
                CertificateRecyclerView = findViewById(R.id.certification_rcv);
                CertificateLayoutManager = new LinearLayoutManager(this);
                CertificateRecyclerView.setLayoutManager(CertificateLayoutManager);
                CertificateAdapter = new CertificationProfileAdapter(certificateList, ProfileScreenActivity.this);
                CertificateRecyclerView.setAdapter(CertificateAdapter);
            } else {
                certificationCard.setVisibility(View.GONE);
            }

            isChatSubscriptionRequired = mpacifyerDetial.isChatSubscriptionRequired();
            priceForChat = Integer.parseInt(mpacifyerDetial.getPriceForChat());

            LinearLayout chatLlay = findViewById(R.id.chat_llay);
            MainTextView chat_price_ctxv = findViewById(R.id.chat_price_ctxv);

            chatLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getSavedCardWebChat(mpacifyerDetial);
                    //callChatSubScription(mpacifyerDetial.getId());
                }
            });

            if (getPackageName().equals("com.pacifyr.pam"))
           {
               if (isValid(mpacifyerDetial.getEmotion()))
               {
                   PacifyrActivity.call_emotion=mpacifyerDetial.getEmotion();
               }
               linearLayout_chat_price_description_layout.setVisibility(View.GONE);
           }
           else
           {
               if(!isChatSubscriptionRequired)
               {
                   chatLlay.setVisibility(View.GONE);

               }else if(priceForChat==0){
                   chatLlay.setVisibility(View.GONE);
               }else{
                   chatLlay.setVisibility(View.VISIBLE);

                   if(mpacifyerDetial.getChatSubscription()==null) {
                       chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                       chatLlay.setClickable(true);
                   }else{
                       try {
                           ChatSubscription subscription = mpacifyerDetial.getChatSubscription();
                           String expiryDate = subscription.getExpiryDate();

                           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                           Date parsedTime = sdf.parse(expiryDate);

                           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                           Date date = new Date();
                           System.out.println(dateFormat.format(date));

                           long diff = parsedTime.getTime() - date.getTime();

                           long diffSeconds = diff / 1000 % 60;
                           long diffMinutes = diff / (60 * 1000) % 60;
                           long diffHours = diff / (60 * 60 * 1000) % 24;
                           long diffDays = diff / (24 * 60 * 60 * 1000);

                           if (diffDays>0)
                           {
                               chat_price_ctxv.setText("Unlimited chat remaining for "+diffDays+" days");
                           }else if(diffHours>1){
                               chat_price_ctxv.setText("Unlimited chat remaining for "+diffHours+" hours and "+diffMinutes+" minutes");
                           }else if(diffMinutes>0){
                               chat_price_ctxv.setText("Unlimited chat remaining for "+diffMinutes+" minutes");
                           }
                           chatLlay.setClickable(false);

                           //chatPriceCtxv.setText("Unlimited chat remainng for ");
                       }
                       catch (Exception e)
                       {
                           e.printStackTrace();
                       }
                   }
               }
           }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }
    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public void getSavedCardWebChat( final MUser pacifyerDetial) {
        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb " );
            PrefManager prefM= PrefManager.getInstance(ProfileScreenActivity.this);
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
                                Intent i = new Intent(ProfileScreenActivity.this, PaymentActivity.class);
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
                                showIOSDialogOK("","A payment method needs to be set up.","Set up Payment");
                            }
                        }else{
                            saveCardExist(false);
                            showIOSDialogOK("","A payment method needs to be set up.","Set up Payment");
                        }
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        saveCardExist(false);
                        //showIOSDialogOK("","A payment method needs to be set up.","Set up Payment",listener);

                    }
                }
            };
            AQuery aq = new AQuery(ProfileScreenActivity.this);
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
            PrefManager prefM = PrefManager.getInstance(ProfileScreenActivity.this);
            String token = prefM.getAccessToken();
            /*String userid = prefM.getUserID();*/
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.getString("message");
                        json.getString("message");

                            //startChat(pacifyr);
                            iOSDialogClickListener listener = new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    //startChat(pacifyr);
                                    dialog.dismiss();
                                }
                            };
                            showIOSDialogOK("", "Subscribed to Chat", "OK");
                            //chatPriceCtxv.setText("Unlimited chat remianing for ");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(ProfileScreenActivity.this);
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

    Snackbar offlineSnackbar;

    private void initializeViews() {

        startVideoCimv = findViewById(R.id.start_video_cimv);
        startAudioCimv = findViewById(R.id.start_audio_cimv);
        startChatCimv = findViewById(R.id.start_chat_cimv);
        indicateOnline = findViewById(R.id.online_offline_imv);
        profileLlay = findViewById(R.id.profile_llay);
        call_llay = findViewById(R.id.call_llay);
        profileNameCtxv = findViewById(R.id.profile_name_ctxv);
//        pacifyrRoleCtxv = (MainTextView) findViewById(R.id.pacifyr_role_ctxv);
        profileRating = findViewById(R.id.profile_rating);
        ratingCtxv = findViewById(R.id.rating_ctxv);
        profileCimv = findViewById(R.id.profile_cimv);
        isonlineCtxv = findViewById(R.id.isonline_ctxv);
        dollarSignCtxv = findViewById(R.id.dollar_sign_ctxv);
        priceCtxv = findViewById(R.id.price_ctxv);
        cost_label = findViewById(R.id.per_hour_ctxv);
        snapshotCard = findViewById(R.id.snapshot_card);
        snapshotCtxv = findViewById(R.id.snapshot_ctxv);
        blogCard = findViewById(R.id.blog_card);
        websiteCtxv = findViewById(R.id.website_ctxv);
        connect_pacifyr_ctxv = findViewById(R.id.connect_pacifyr_ctxv);
        specialityCard = findViewById(R.id.speciality_card);
        specialRcv = findViewById(R.id.special_rcv);
        educationCard = findViewById(R.id.education_card);
        educationRcv = findViewById(R.id.education_rcv);
        certificationCard = findViewById(R.id.certification_card);
        topRlay = findViewById(R.id.prof_top_rlay);

        cost_label.setText(Utility.getCostLabel(ProfileScreenActivity.this));

    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
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
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv, "PROFILE");
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ActionBarCommonFont.otf");
        titleTxv.setTypeface(type);
        //titleTxv.setText("Add Credit Card");
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //Used this function when profile response is changed and got exception
    private void updateProfile(JSONObject user) {
        try {

            if (isValid(pacifyerDetial.getQbid()))
                qbID=pacifyerDetial.getQbid();
            hideProgress();

             String imgUrl =  user.getString("image");
            profileRating.setRating(Float.valueOf(user.getString("rating")));
            profileNameCtxv.setText(user.getString("firstName") + user.getString("lastName"));
            ratingCtxv.setText(user.getString("numberOfReviews") + " Reviews");

            if (isValid(imgUrl))
                if(imgUrl.contains("http")||imgUrl.contains("https"))
                    imgUrl=imgUrl;
                else
                    imgUrl= UtilityNetworkService.GLOBAL_IMAGE_URL + imgUrl;

            if (isValidhtml(imgUrl))
                Picasso.with(this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(profileCimv);

            if (isValid(user.getString("description"))) {
                snapshotCtxv.setText(user.getString("description"));
            }
            else {
                snapshotCard.setVisibility(View.GONE);
            }

            if (isValid(user.getString("websiteURL"))) {
                websiteCtxv.setText(user.getString("websiteURL"));
            }
            else {
                blogCard.setVisibility(View.GONE);
            }
            myList = new ArrayList<String>();
            JSONArray specialArray=user.getJSONArray("specialities");
            for (int i=0;i<specialArray.length();i++)
            {
                String data = specialArray.getString(i);
                myList.add(data);
            }
            if (isValid(myList)) {
                //Speciality adapter
                SpecialRecyclerView = findViewById(R.id.special_rcv);
                SpecialLayoutManager = new LinearLayoutManager(this);
                SpecialRecyclerView.setLayoutManager(SpecialLayoutManager);
                SpecialAdapter = new SpecilaListProfileAdapter(myList, ProfileScreenActivity.this);
                SpecialRecyclerView.setAdapter(SpecialAdapter);
            } else {
                specialityCard.setVisibility(View.GONE);
            }


            // educationsList = pacifyerDetial.getEducationList();
            JSONArray eduArray=user.getJSONArray("education");
            if (isValid(eduArray)) {
                //education adapter
                EducationRecyclerView = findViewById(R.id.education_rcv);
                EducationLayoutManager = new LinearLayoutManager(this);
                EducationRecyclerView.setLayoutManager(EducationLayoutManager);
                // EducationAdapter = new EducationProfileAdapter(eduArray, ProfileScreenActivity.this);
                EducationRecyclerView.setAdapter(EducationAdapter);
            } else {
                educationCard.setVisibility(View.GONE);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void callChatSubScription(String pacifyrID) {
        try{
            String url = NetworkService.GLOBAL_URL+"payment/chargeChat";
            prefM = PrefManager.getInstance(ProfileScreenActivity.this);
            String token = prefM.getAccessToken();
            /*String userid = prefM.getUserID();*/
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.toString();
                        jsonS+="sam";


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(ProfileScreenActivity.this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", prefM.getUserID());
            jsonObject.putOpt("pacifyrId", pacifyrID);
            aq.post(url, jsonObject, JSONObject.class, cb);

           /* {
                "userId": "594a1a4095f8d11ff498aa90",
                    "pacifyrId": "594a1a4095f8d11ff498aa90"
            }*/

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
