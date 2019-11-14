package com.pacifyr.omhealthcare.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelCustomerDashboard2;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.CaptureActivityPortrait;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.fragment.EventsListDialog;
import com.pacifyr.omhealthcare.home.ConnectToPacifyrActivity;
import com.pacifyr.omhealthcare.home.LastSessionActivity;
import com.pacifyr.omhealthcare.home.SessionSummaryActivity;
import com.pacifyr.omhealthcare.model.InterviewModel;
import com.pacifyr.omhealthcare.model.Session;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.omhealthcare.quickblox.chat.utils.qb.QbDialogHolder;
import com.pacifyr.omhealthcare.quickblox.services.CallService;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utility.EventMessage;
import com.pacifyr.omhealthcare.utils.Utility;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.base.PacifyerActivity.call_emotion;
import static com.pacifyr.omhealthcare.network.NetworkService.lastSession;
import static com.pacifyr.omhealthcare.network.NetworkService.modelCustomerDashboard2;

public class UserDashboardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserDashboardFragment.class.getSimpleName();

    public UserDashboardFragment() {
        // Required empty public constructor
    }

    private void getPermissions() {
        Utility.checkPermission(getActivity(), "essential");
    }

    private IntentIntegrator _QrScanner;
    private ImageView _ImageView_CheckIn;
    private ImageView _ImageView_Events;
    private ImageView _ImageView_LastSession;
    private ImageView __ImageView_BrandUp;

    private MainTextView _UnRead_Chat_CoutLayout;
    public Integer _unReadCount =0;
    private RelativeLayout _RelativeLayout_Connect;
    private PrefManager _PrefManager;
    private SwipeRefreshLayout _swipeRefreshLayout;
    public String _EmotionTitle="";
    private int _Badge_Count=0;
    private boolean _isClicked_OnChat=false;

    //for leaders up demo purpose reason for the call is assigning
    public static String LeadersUpReasonFortheCall="";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _PrefManager = PrefManager.getInstance(getContext());

        NetworkService.startActionCutomerDashboard(getActivity());
        ShortcutBadger.applyCount(getActivity(), _PrefManager.getBadgeValue());
        getPermissions();

    }

    private void loginToQuickblox() {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            CallService.start(getActivity(), sharedPrefsHelper.getQbUser());

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_leadersup_user_dashboard,container,false);
        intializeViews(view);
        _swipeRefreshLayout.setOnRefreshListener(this);
        _swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        setMyAdapter();

        _UnRead_Chat_CoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogsActivity.start(getActivity());
                _unReadCount=0;

            }
        });

        _ImageView_LastSession.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ImageViewInsideclickCheck(v, event);
            }
        });

        _ImageView_Events.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ImageViewInsideclickCheck(v, event);
            }
        });

        __ImageView_BrandUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ImageViewInsideclickCheck(v, event);
            }
        });

        _ImageView_CheckIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ImageViewInsideclickCheck(v, event);
            }
        });

        _RelativeLayout_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToPacifyrActivity.start(getActivity());
            }
        });

        return view;
    }

    private boolean ImageViewInsideclickCheck(View v, MotionEvent event) {

        /**
         * https://stackoverflow.com/questions/28090386/create-imageview-that-is-round-so-click-will-work-on-round-area-only-android
         * **/

        float centerX, centerY, touchX, touchY, radius;
        centerX = v.getWidth() / 2;
        centerY = v.getHeight() / 2;
        touchX = event.getX();
        touchY = event.getY();
        radius = centerX;

        if (Math.pow(touchX - centerX, 2)
                + Math.pow(touchY - centerY, 2) < Math.pow(radius, 2)) {
            switch (v.getId()) {
                case R.id._ImageView_Events:
                    EventActivity.start(getActivity());
                    break;

                case R.id._ImageView_BrandUp:
                    Intent i = new Intent(getActivity(), TermsOfUseActivity.class);
                    i.putExtra("spurl", getResources().getString(R.string.go_to_e_learning_url));
                    i.putExtra("title", "E-LEARNING");
                    startActivity(i);
                    break;

                case R.id._ImageView_CheckIn:
                    _QrScanner = new IntentIntegrator(getActivity());
                    _QrScanner.setPrompt("Scan a barcode");
                    _QrScanner.setCameraId(0);  // Use a specific camera of the device
                    _QrScanner.setOrientationLocked(true);
                    _QrScanner.setBeepEnabled(true);
                    _QrScanner.setCaptureActivity(CaptureActivityPortrait.class);
                    _QrScanner.initiateScan();
                    break;

                case R.id._ImageView_LastSession:
                    try {
                        if (isValid(lastSession) && isValid(lastSession.getId())) {
                            String sessionID = lastSession.getId();
                            LastSessionActivity.start(getActivity(), sessionID);
                        } else {
                            ConnectToPacifyrActivity.start(getActivity());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
            }

            return false;
        } else {
            System.out.println("Outside Circle");
            return true;
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPendingEventChekins();
    }

    private void getPendingEventChekins() {
        new NetworkService().getIncompleteEventCheckins(new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    InterviewModel response = new Gson().fromJson(json.toString(),
                            InterviewModel.class);
                    if (response != null && response.getResults() != null && response.getResults().size() > 0) {
                        Bundle args = new Bundle();
                        args.putSerializable("data", (Serializable) response.getResults());
                        EventsListDialog eventsListFragment = new EventsListDialog();
                        eventsListFragment.setArguments(args);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev =
                                getActivity().getSupportFragmentManager().findFragmentByTag(
                                        "dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        eventsListFragment.show(ft, "dialog");

                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            setLoading();
            showProgress();
            handleActionCustomerDashboard();
            if (isValid(call_emotion)) {

                String emotionImg = GLOBAL_IMAGE_URL + call_emotion.getImage();
                String emotionTitle = call_emotion.getTitle();
                _EmotionTitle=emotionTitle;
                if (isValidhtml(emotionImg)) {
                    // Picasso.with(getActivity()).load(emotionImg).into(feelImageDimv);
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



                }
            } else {

            }
            getUnratedSessions();
            loginToQuickblox();
            setChatCountInfo();

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

        _PrefManager= PrefManager.getInstance(getContext());
        String token=_PrefManager.getAccessToken();
        String userid=_PrefManager.getUserID();
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

            setMyAdapter();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

    }

    //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMyAdapter() {
        try {

            String emotionImg = null;
            String emotionTitle = null;

            if (isValid(modelCustomerDashboard2)) {
                final MUser[] pfc = modelCustomerDashboard2.getPreferredPacifyrs();
                ModelCustomerDashboard2.ThisWeekStatistics lastWeek = modelCustomerDashboard2.getThisWeekStatistics();
                lastSession = modelCustomerDashboard2.getLastSession();
                if(isValid(modelCustomerDashboard2.getUser().getEmotion())) {
                    emotionImg = GLOBAL_IMAGE_URL + modelCustomerDashboard2.getUser().getEmotion().getImage();
                    emotionTitle = modelCustomerDashboard2.getUser().getEmotion().getTitle();
                    _EmotionTitle = emotionTitle;

                    if (isValidhtml(emotionImg)) {

                        // Picasso.with(getActivity()).load(emotionImg).into(feelImageDimv);

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


                    }
                }
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("ON RESUME;",ex.getMessage());
        }
    }


    @Override
    public void onSetValues(View view) throws Exception {

    }


    public void intializeViews(View view)
    {
        _ImageView_Events = view.findViewById(R.id._ImageView_Events);
        _RelativeLayout_Connect = view.findViewById(R.id._RelativeLayout_Connect);
        _ImageView_LastSession = view.findViewById(R.id._ImageView_LastSession);
        _swipeRefreshLayout = view.findViewById(R.id._swipeRefreshLayout);
        __ImageView_BrandUp = view.findViewById(R.id._ImageView_BrandUp);
        _UnRead_Chat_CoutLayout = view.findViewById(R.id.unread_chat_ctxv);
        _ImageView_CheckIn=view.findViewById(R.id._ImageView_CheckIn);
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

                    _unReadCount=integer;
                    _Badge_Count = 0;
                    if (isValid(_unReadCount)){
                        if(_unReadCount>0)
                        {
                            _UnRead_Chat_CoutLayout.setVisibility(View.VISIBLE);
                            _UnRead_Chat_CoutLayout.setText(_unReadCount + " unread messages");
                            _Badge_Count+=_unReadCount;
                            _PrefManager.saveBadgeValue(_Badge_Count);
                            ShortcutBadger.applyCount(getActivity(), _PrefManager.getBadgeValue());

                        }else {
                            _isClicked_OnChat=true;
                            _UnRead_Chat_CoutLayout.setVisibility(View.GONE);
                            _PrefManager.saveBadgeValue(_Badge_Count);
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
                _swipeRefreshLayout.setRefreshing(false);
                setLoading();
                showProgress();
                handleActionCustomerDashboard();
                setChatCountInfo();
            }
        }, 2000);
    }
}
