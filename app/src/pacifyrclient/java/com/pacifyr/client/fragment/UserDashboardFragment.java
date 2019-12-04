package com.pacifyr.client.fragment;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelCustomerDashboard2;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.home.ConnectToSinglePacifyrActivity;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.LastSessionActivity;
import com.pacifyr.client.home.MySessionsActivity;
import com.pacifyr.client.home.SessionSummaryActivity;
import com.pacifyr.client.model.Session;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.client.quickblox.chat.utils.qb.QbDialogHolder;
import com.pacifyr.client.quickblox.services.CallService;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utility.EventMessage;
import com.pacifyr.client.utils.Utility;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.client.R.id.last_session_rlay;
import static com.pacifyr.client.base.PacifyerActivity.call_emotion;
import static com.pacifyr.client.network.NetworkService.lastSession;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.quickblox.sample.core.utils.ConnectivityUtils.isNetworkConnected;

public class UserDashboardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserDashboardFragment.class.getSimpleName();
    private RelativeLayout feelRlay,this_week_rlay,connect_btn_rlay;
    //private DeviceFitImageView feelImage;
    private RelativeLayout lastSessionRlay;
    private CircleImageView lastSessionDimv;
    private MainTextView lastSsnPacfyrCtxv,no_pacifyr_ctxv,no_lastsession_ctxv;
    private LinearLayout last_session_top_llay;
    private DeviceFitImageView videoCamImg;
    private MainTextView lastSessionMinuteTxv;
    private MainTextView daysTxv;
    private RelativeLayout cirPrefPacifyr;
    private LinearLayout llayTop, llay_bottom;
    private CircleImageView pacifyr1Dimv;
    private CircleImageView pacifyr2Dimv;
    private CircleImageView pacifyr4Dimv;
    private LinearLayout llayBottom;
    private CircleImageView pacifyr3Dimv;
    //private DeviceFitImageView searchDimv;
    private RelativeLayout thisWeekRlay;
    private MainTextView sessionsTxv;
    private MainTextView hoursTxv;
    private MainTextView paidTxv;
    private DeviceFitImageView feelImageDimv;
    private MainTextView feelTitleCtxv;
    private PrefManager prefM;
    private MUser prefpac;
    private MainTextView minutesCtxv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainTextView feel_err_text;
    public String EmotionTitle="";

    private MainTextView unreadChatCtxv;
    public Integer unreadCount =0;

    private boolean isClickedOnChat=false;
    private int badgeCount=0;

    public UserDashboardFragment() {
        // Required empty public constructor
    }

    private void getPermissions() {
        Utility.checkPermission(getActivity(), "essential");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefM = PrefManager.getInstance(getContext());
        NetworkService.startActionCutomerDashboard(getActivity());
        ShortcutBadger.applyCount(getActivity(), prefM.getBadgeValue()); //for 1.1.4+
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setMyAdapter();
        getPermissions();
        //}
    }

    private void loginToQuickblox() {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            CallService.start(getActivity(), sharedPrefsHelper.getQbUser());
            // startOpponentsActivity();
            //return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_dashboard,container,false);
        intializeViews(view);
        //NetworkService.startActionCutomerDashboard(getContext());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        unreadChatCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogsActivity.start(getActivity());
                unreadCount=0;
                /*showFragment(new PacifyrMessageFragment(/));*/
                //defSetText(titleTxv, "Messages");

            }
        });


        lastSessionRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (isValid(lastSession)&&isValid(lastSession.getId()))
                    {
                        String sessionID = lastSession.getId();
                        LastSessionActivity.start(getActivity(),sessionID);
                    }
                    else {
                        ConnectToPacifyrActivity.start(getActivity());
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        feelRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i= new Intent(getContext(),FeelSelectActivity.class);
                startActivity(i);*/

                ConnectToPacifyrActivity.start(getActivity());
             /*    Intent intent= new Intent(getContext(),ConnectToPacifyrActivity.class);
                intent.putExtra("display","Pacifyr");
                startActivity(intent);*/

            }
        });

        thisWeekRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isValid(modelAllEmotions))
                        MySessionsActivity.start(getActivity());
                    else
                        callAllEmotions();


                   /* getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_content_fragment, new MySessionFragment()).commit();
                    //showFragment(new PacifyrSessionsFragment());
                    MainTextView titleTxv = (MainTextView) getActivity().findViewById(R.id.title_txv);
                    defSetText(titleTxv, "My Sessions");
                    //Toast.makeText(getContext(),"sessions rating",Toast.LENGTH_LONG).show();*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        connect_btn_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToPacifyrActivity.start(getActivity());
                /*Intent intent=new Intent(getActivity(), ConnectToPacifyrActivity.class);
                intent.putExtra("display","Pacifyr");
                startActivity(intent);*/
            }
        });

        pacifyr1Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("preffffff11","p: "+prefpac);
                prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[0];
                Log.i("preffffff12","p: "+prefpac);
                ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);

                // showTheRoundDialog(0, prefpac);
            }
        });
        pacifyr2Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("preffffff21","p: "+prefpac);
                prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[1];
                Log.i("preffffff22","p: "+prefpac);
                ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);

                //showTheRoundDialog( 1,prefpac);
            }
        });
        pacifyr3Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[2];
                Log.i("preffffff3","p: "+prefpac);
                ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);

                // showTheRoundDialog(2, prefpac);
            }
        });

        pacifyr4Dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefpac = modelCustomerDashboard2.getPreferredPacifyrs()[3];
                Log.i("preffffff4","p: "+prefpac);
                ConnectToSinglePacifyrActivity.start(getActivity(), prefpac);

                // showTheRoundDialog(2, prefpac);
            }
        });

        //NetworkService.webListEmotions(getActivity());
        return view;
    }

    private void callAllEmotions() {
        PrefManager prefM = PrefManager.getInstance(getActivity());
        String token = prefM.getAccessToken();
        String userid = prefM.getUserID();
        String url = GLOBAL_URL + "emotions/ordered";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(json);
                    MySessionsActivity.start(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getActivity());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken", token);
        aq.ajax(url, JSONObject.class, cb);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_mydashbord_title));

        try {

            if (isNetworkConnected()) {
                setLoading();
                showProgress();
                handleActionCustomerDashboard();
                if (isValid(call_emotion)) {
                    // feel_err_text.setVisibility(View.GONE);
                    String emotionImg = GLOBAL_IMAGE_URL + call_emotion.getImage();
                    String emotionTitle = call_emotion.getTitle();
                    EmotionTitle = emotionTitle;
                    if (isValidhtml(emotionImg)) {
                        Picasso.with(getActivity()).load(emotionImg).into(feelImageDimv);
                        float brightness = (float) 100;
                        float[] colorMatrix = {
                                0.33f, 0.33f, 0.33f, 0, brightness, //red
                                0.33f, 0.33f, 0.33f, 0, brightness, //green
                                0.33f, 0.33f, 0.33f, 0, brightness, //blue
                                0, 0, 0, 1, 0    //alpha
                        };
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                        feelImageDimv.setColorFilter(filter);
                        feelTitleCtxv.setText(emotionTitle);
                        if (isValid(feel_err_text)) {
                            feel_err_text.setVisibility(View.GONE);

                        }

                    }
                } else {
                    //feel_err_text.setVisibility(View.VISIBLE);
                }
                getUnratedSessions();
//            loginToQuickblox();
                setChatCountInfo();
            } else {
                setAlertStatusInWhite("No Internet connection!", "Please turn on internet connection to continue.", "Exit", "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideProgress();
                        hideStatus2();
                        getActivity().finish();
                    }
                });
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("ON RESUME;",e.getMessage());
        }

    }

    private void getUnratedSessions() {
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                try {
                    if (json != null) {
                        JSONArray results = json.getJSONArray("results");
                        if (results != null && results.length() > 0) {
                            Session session = new Gson().fromJson(results.getJSONObject(0).toString(), Session.class);
                            if (session != null) {
                                SessionSummaryActivity.start(getContext(), session.getId());
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

    private void handleActionCustomerDashboard(){

        prefM= PrefManager.getInstance(getContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"dashboard/customer/"+userid;

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                try {
                    hideProgress();
                    modelCustomerDashboard2= GsonUtils.getInstance().gsonToModelCustomerDashboard2(json);
                    setMyAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                   // hideProgress();
                }
            }
        };
        try {
            hideProgress();
            AQuery aq = new AQuery(getContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
           //hideProgress();
        }
        hideProgress();
    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        try {
           //Toast.makeText(getContext(),"hiii",Toast.LENGTH_LONG).show();
         //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setMyAdapter();
          //  }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("ON RESUME;",ex.getMessage());
        }

    }

  //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMyAdapter() {
        try {

            String emotionImg = null;
            String emotionTitle = null;

            if (isValid(feelTitleCtxv))
            {
                if (EmotionTitle.equals(""))
                {
                    if(isValid(feel_err_text))
                    {feel_err_text.setVisibility(View.VISIBLE);
                        defSetText(feel_err_text,"Please select an emotion");
                    }
                }

            }

            if (isValid(modelCustomerDashboard2)) {
                final MUser[] pfc = modelCustomerDashboard2.getPreferredPacifyrs();
                ModelCustomerDashboard2.ThisWeekStatistics lastWeek = modelCustomerDashboard2.getThisWeekStatistics();
                lastSession = modelCustomerDashboard2.getLastSession();
                if(isValid(modelCustomerDashboard2.getUser().getEmotion())){
                    emotionImg =   GLOBAL_IMAGE_URL+modelCustomerDashboard2.getUser().getEmotion().getImage();
                    emotionTitle = modelCustomerDashboard2.getUser().getEmotion().getTitle();
                    EmotionTitle=emotionTitle;

                    if(isValidhtml(emotionImg)){
                       // feel_err_text.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(emotionImg).into(feelImageDimv);

                        float brightness = (float)100;

                        float[] colorMatrix = {
                                0.33f, 0.33f, 0.33f, 0, brightness, //red
                                0.33f, 0.33f, 0.33f, 0, brightness, //green
                                0.33f, 0.33f, 0.33f, 0, brightness, //blue
                                0, 0, 0, 1, 0    //alpha
                        };

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                        feelImageDimv.setColorFilter(filter);

                        feelTitleCtxv.setText(emotionTitle);
                        if(isValid(feel_err_text))
                        {
                            feel_err_text.setVisibility(View.GONE);

                        }
                    } else {
                      //  feel_err_text.setVisibility(View.VISIBLE);
                    }
                }

                /*for(int i=0;i<lastSession.getSessionRatings().length;i++)
                {
                    if(lastSession.getSessionRatings()[i].getType().equals("customer"))
                    {
                        emotionImg = lastSession.get;
                        emotionTitle;
                    }
                }*/




                if (isValid(lastSession)) {
                    last_session_top_llay.setVisibility(View.VISIBLE);
                    no_lastsession_ctxv.setVisibility(View.GONE);
                    /*lastSessionDimv.setVisibility(View.VISIBLE);
                    lastSessionRlay.setVisibility(View.VISIBLE);*/


                    lastSsnPacfyrCtxv.setText(lastSession.getPacifyr().getFirstName() + " " + lastSession.getPacifyr().getLastName());
                    Picasso.with(getContext()).load(lastSession.getPacifyr().getImageUrl()).into(lastSessionDimv);
                    String duration = lastSession.getDuration();
                    lastSessionMinuteTxv.setText(duration);
                    String type = lastSession.getSessionType();
                    if (type.equals("audio")){
                        videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.connectaudio1));
                    }
                    else if(type.equals("video")){
                        videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_video_cam_blue));
                    }
                    lastSessionMinuteTxv.setVisibility(View.VISIBLE);
                    videoCamImg.setVisibility(View.VISIBLE);
                    daysTxv.setVisibility(View.VISIBLE);
                    defSetText(daysTxv,lastSession.getDaysSincePosted());

                }
                else {
                    last_session_top_llay.setVisibility(View.GONE);
                    no_lastsession_ctxv.setVisibility(View.VISIBLE);
                /*    lastSessionDimv.setVisibility(View.INVISIBLE);
                    lastSessionRlay.setVisibility(View.INVISIBLE);
                    daysTxv.setVisibility(View.INVISIBLE);
                    lastSsnPacfyrCtxv.setVisibility(View.VISIBLE);*/
                    defSetText(no_lastsession_ctxv,getResources().getString(R.string.no_sessions_yet));

                }

                if (isValid(lastWeek)) {
                    String[] split = lastWeek.getDuration().split(":");
                    sessionsTxv.setText(lastWeek.getTotalSessions());
                    hoursTxv.setText(split[0]);
                    minutesCtxv.setText(" "+split[1]);
                    PrefManager prefM = PrefManager.getInstance(getContext());
                    String country = prefM.getCountry();
                    String sign ="$";
                    if(country.equals("India")){
                        sign=getContext().getResources().getString(R.string.rupee);
                    }else {
                        sign=getContext().getResources().getString(R.string.dollar);
                    }
                    paidTxv.setText(sign+lastWeek.getSpendings());
                }
                else {

                }

                if(isValid(pfc)) {

                    Log.i("preffffffa1","p: "+pfc);
                    if(isValid(pfc.length)) {
                        Log.i("preffffffb1","p: "+pfc.length);
                        llayTop.setVisibility(View.VISIBLE);
                        llay_bottom.setVisibility(View.VISIBLE);
                        no_pacifyr_ctxv.setVisibility(View.GONE);
                        switch (pfc.length) {
                            case 0:
                                Log.i("preffffffc1","p: "+pfc);
                                pacifyr3Dimv.setVisibility(View.GONE);
                                pacifyr2Dimv.setVisibility(View.GONE);
                                pacifyr1Dimv.setVisibility(View.GONE);
                                pacifyr4Dimv.setVisibility(View.GONE);
                                llayTop.setVisibility(View.GONE);
                                llay_bottom.setVisibility(View.GONE);
                                no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                                defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                                break;

                            case 1:
                                Log.i("preffffffc2","p: "+pfc);
                                pacifyr3Dimv.setVisibility(View.GONE);
                                pacifyr2Dimv.setVisibility(View.GONE);
                                pacifyr4Dimv.setVisibility(View.GONE);
                                Picasso.with(getContext()).load( pfc[0].getImageUrl()).into(pacifyr1Dimv);
                                break;
                            case 2:
                                Log.i("preffffffc5","p: "+pfc);
                                pacifyr4Dimv.setVisibility(View.GONE);
                                pacifyr3Dimv.setVisibility(View.GONE);
                                Log.i("preffffffc51","p: "+ pfc[0].getImageUrl());
                                Log.i("preffffffc52","p: "+pfc[1].getImageUrl());
                                Picasso.with(getActivity()).load( pfc[0].getImageUrl()).into(pacifyr1Dimv);
                                Picasso.with(getActivity()).load( pfc[1].getImageUrl()).into(pacifyr2Dimv);
                                break;
                            case 3:
                                Log.i("preffffffc3","p: "+pfc);
                                pacifyr4Dimv.setVisibility(View.GONE);
                                Picasso.with(getContext()).load( pfc[0].getImageUrl()).into(pacifyr1Dimv);
                                Picasso.with(getContext()).load( pfc[1].getImageUrl()).into(pacifyr2Dimv);
                                Picasso.with(getContext()).load( pfc[2].getImageUrl()).into(pacifyr3Dimv);
                                break;
                            default:
                                Log.i("preffffffc4","p: "+pfc);
                                Picasso.with(getContext()).load( pfc[0].getImageUrl()).into(pacifyr1Dimv);
                                Picasso.with(getContext()).load( pfc[1].getImageUrl()).into(pacifyr2Dimv);
                                Picasso.with(getContext()).load( pfc[2].getImageUrl()).into(pacifyr3Dimv);
                                Picasso.with(getContext()).load( pfc[3].getImageUrl()).into(pacifyr4Dimv);
                                break;
                        }


                    }
                    else {
                        llayTop.setVisibility(View.GONE);
                        llay_bottom.setVisibility(View.GONE);
                        no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                        defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                    }
                }
                else {
                    llayTop.setVisibility(View.GONE);
                    llay_bottom.setVisibility(View.GONE);
                    no_pacifyr_ctxv.setVisibility(View.VISIBLE);
                    defSetText(no_pacifyr_ctxv,getResources().getString(R.string.no_prefered_pacifyr));
                }
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("ON RESUME;",ex.getMessage());
        }
    }


    /*private void showTheRoundDialog(int position, MUser prefpac) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selction_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // set the custom dialog components - text, image and button
        MainTextView text = (MainTextView) dialog.findViewById(R.id.name_in_dialog_ctxv);
        MainTextView sel_speciality_ctxv = (MainTextView) dialog.findViewById(R.id.sel_speciality_ctxv);
        MainTextView no_review_ctxv = (MainTextView) dialog.findViewById(R.id.no_review_ctxv);
        RatingBar dialog_rating=(RatingBar) dialog.findViewById(R.id.dialog_rating);

        no_review_ctxv.setText(prefpac.getNumberOfReviews()+getResources().getString(R.string.reviews));
        text.setText(prefpac.getFirstName()+" "+prefpac.getLastName());
        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.sel_user_dimv);
        String spec="";
        for(int i=0;i<prefpac.getSpecialities().length;i++)
        {
            spec=spec+prefpac.getSpecialities()[i]+",";
        }
        sel_speciality_ctxv.setText(spec);
        dialog_rating.setRating(Float.parseFloat(prefpac.getRating()));
        Picasso.with(getContext()).load( prefpac.getImageUrl()).into(image);
        dialog.show();
    }*/


    @Override
    public void onSetValues(View view) throws Exception {

    }


    public void intializeViews(View view)
    {
        feelRlay = view.findViewById(R.id.feel_rlay);
        feel_err_text = view.findViewById(R.id.text_feel_error);
        connect_btn_rlay = view.findViewById(R.id.connect_btn_rlay);
        //feelImage = (DeviceFitImageView) view.findViewById(R.id.feel_image_dimv);
        lastSessionRlay = view.findViewById(last_session_rlay);
        lastSessionDimv = view.findViewById(R.id.last_session_dimv);
        swipeRefreshLayout = view.findViewById(R.id.user_swipe_layout);
        last_session_top_llay = view.findViewById(R.id.last_session_top_llay);
        no_pacifyr_ctxv = view.findViewById(R.id.no_pacifyr_ctxv);
        no_lastsession_ctxv = view.findViewById(R.id.no_lastsession_ctxv);
        lastSsnPacfyrCtxv = view.findViewById(R.id.lastSsn_pacfyr_ctxv);
        videoCamImg = view.findViewById(R.id.video_cam_img);
        lastSessionMinuteTxv = view.findViewById(R.id.last_session_minute_txv);
        daysTxv = view.findViewById(R.id.days_txv);
        cirPrefPacifyr = view.findViewById(R.id.cir_pref_pacifyr);
        llayTop = view.findViewById(R.id.llay_top);

        llay_bottom = view.findViewById(R.id.llay_bottom);
        pacifyr1Dimv = view.findViewById(R.id.pacifyr1_dimv);
        pacifyr2Dimv = view.findViewById(R.id.pacifyr2_dimv);
        llayBottom = view.findViewById(R.id.llay_bottom);
        pacifyr3Dimv = view.findViewById(R.id.pacifyr3_dimv);
        pacifyr4Dimv = view.findViewById(R.id.pacifyr4_dimv);
        thisWeekRlay = view.findViewById(R.id.this_week_rlay);
        sessionsTxv = view.findViewById(R.id.sessions_txv);
        hoursTxv = view.findViewById(R.id.hours_txv);
        minutesCtxv = view.findViewById(R.id.min_ctxv);
        paidTxv = view.findViewById(R.id.paid_amt_ctxv);
        this_week_rlay = view.findViewById(R.id.this_week_rlay);

        feelImageDimv = view.findViewById(R.id.feel_image_dimv);
        feelTitleCtxv = view.findViewById(R.id.feel_title_ctxv);
        unreadChatCtxv = view.findViewById(R.id.unread_chat_ctxv);
        defSetText(no_pacifyr_ctxv,"");
        setChatCountInfo();
    }

    private void setChatCountInfo() {

        try {
            Collection<QBChatDialog> dialogList = (QbDialogHolder.getInstance().getDialogs().values());
            ArrayList<QBChatDialog> list = new ArrayList<>(dialogList);
            Set<String> dialogSet = new HashSet<String>();

            Iterator<QBChatDialog> iterator = dialogList.iterator();
            while (iterator.hasNext()) {
                dialogSet.add(iterator.next().getDialogId());
            }

            QBRestChatService.getTotalUnreadMessagesCount(dialogSet,null).performAsync(new QBEntityCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer, Bundle bundle) {

                    unreadCount=integer;
                    badgeCount = 0;
                    if (isValid(unreadCount)){
                        if(unreadCount>0)
                        {
                            unreadChatCtxv.setVisibility(View.VISIBLE);
                            unreadChatCtxv.setText(unreadCount + " unread messages");
                            badgeCount+=unreadCount;
                            prefM.saveBadgeValue(badgeCount);
                            ShortcutBadger.applyCount(getActivity(), prefM.getBadgeValue());

                        }else {
                            isClickedOnChat=true;
                            unreadChatCtxv.setVisibility(View.GONE);
                            prefM.saveBadgeValue(badgeCount);
                            ShortcutBadger.applyCount(getActivity(), 0);
                        }
                    }

                }

                @Override
                public void onError(QBResponseException e) {
                    //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                }
            });


        }catch (Exception e)
        {
            Log.w("QQQ",String.valueOf(e));
        }


    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event){
       /* @Subscribe
        public void onNotifyEvent(NotifyEvent event) {*/

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
                setLoading();
                showProgress();
                handleActionCustomerDashboard();
                setChatCountInfo();
            }
        }, 2000);
    }
}
