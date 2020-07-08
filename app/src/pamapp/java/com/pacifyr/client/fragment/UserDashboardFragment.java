package com.pacifyr.client.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.DoctorsActivity;
import com.pacifyr.client.home.DoctorsMoreInformationActivity;
import com.pacifyr.client.home.LearnMoreActivity;
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
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.pacifyr.client.base.PacifyrActivity.call_emotion;
import static com.pacifyr.client.network.NetworkService.modelCustomerDashboard2;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.quickblox.sample.core.utils.ConnectivityUtils.isNetworkConnected;

public class UserDashboardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = UserDashboardFragment.class.getSimpleName();
    /* private RelativeLayout feelRlay, this_week_rlay, connect_btn_rlay;
     private MainTextView lastSsnPacfyrCtxv, no_pacifyr_ctxv;
     private LinearLayout last_session_top_llay;
     private MainTextView lastSessionMinuteTxv;
     private MainTextView daysTxv;
     private RelativeLayout cirPrefPacifyr;*/
    private LinearLayout llayTop, llay_bottom;

    private PrefManager prefM;
    private MUser prefpac;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainTextView feel_err_text;
    public String EmotionTitle = "";

    private MainTextView unreadChatCtxv;
    public Integer unreadCount = 0;

    private boolean isClickedOnChat = false;
    private int badgeCount = 0;

    public static boolean isVideoVist=false;
    private View ball_virtual_visit;
    private View ball_wellness;
    private View ball_stherapy;
    private View ball_momseze;
    private View ball_pharmacy;
    private View ball_diagnostics;
    private String searchSpeciality;
    private double latitude;
    private double longitude;

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
//        setMyAdapter();
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
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
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
                unreadCount = 0;
                /*showFragment(new PacifyrMessageFragment(/));*/
                //defSetText(titleTxv, "Messages");

            }
        });


        ball_virtual_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoctorsActivity.class);
                startActivity(intent);
            }
        });
        ball_wellness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideoVist=true;
                ConnectToPacifyrActivity.start(getActivity());
            }
        });

        ball_stherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSpeciality = "Simple Therapy";
                boolean granted = Utility.checkPermission(getActivity(), "location");
                if (granted) {
                    fetchLastKnownLocation();
                }
                //else check onRequestPermissionsResult
            }
        });

        ball_momseze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchSpeciality = "Momseze";
                boolean granted = Utility.checkPermission(getActivity(), "location");
                if (granted) {
                    fetchLastKnownLocation();
                }
                //else check onRequestPermissionsResult
            }
        });

        return view;
    }

    private void OpenDoctorsInformationActivity(MUser pacifyer) {
        Intent intent = new Intent(getActivity(), DoctorsMoreInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Object", pacifyer);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.label_find_care));

        try {

            if (isNetworkConnected()) {
              /* setLoading();
               showProgress();*/
//               handleActionCustomerDashboard();
                if (isValid(call_emotion)) {
                    String emotionImg = GLOBAL_IMAGE_URL + call_emotion.getImage();
                    String emotionTitle = call_emotion.getTitle();
                    EmotionTitle = emotionTitle;
                }
                getUnratedSessions();
                loginToQuickblox();
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ON RESUME;", e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utility.MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastKnownLocation();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION) {
            fetchLastKnownLocation();
        }
    }

    private void fetchLastKnownLocation() {
        setLoading();
        showProgress();
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(LocationAvailability locationAvailability) {
                if (isValid(locationAvailability) && locationAvailability.isLocationAvailable()) {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            hideProgress();
                            if (location != null) {
                                postEventOnMainThread(location);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgress();
                        }
                    });
                } else {
                    LocationRequest locationRequest = getLocationRequest();
                    LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    locationSettingsRequest.setAlwaysShow(true);
                    Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(locationSettingsRequest.build());
                    task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            hideProgress();
                            if (locationSettingsResponse.getLocationSettingsStates().isLocationPresent()) {
                                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getActivity());
                                fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult != null && isValid(locationResult.getLocations(), 1)) {
                                            postEventOnMainThread(locationResult.getLocations().get(0));
                                        }
                                    }
                                }, Looper.myLooper());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgress();
                            if (e instanceof ResolvableApiException) {
                                try {
                                    ((ResolvableApiException) e).startResolutionForResult(getActivity(), Utility.MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
            }
        });

    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setExpirationDuration(20000);
        locationRequest.setNumUpdates(3);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(15000);
        return locationRequest;
    }

    @Subscribe
    public void onLocationFetched(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (searchSpeciality != null) {
                webSearchPacifyrWOptions(searchSpeciality);
            } else {
                Log.e(TAG, "searchSpeciality == null");
            }
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

    private void handleActionCustomerDashboard() {

        prefM = PrefManager.getInstance(getContext());
        String token = prefM.getAccessToken();
        String userid = prefM.getUserID();
        String url = GLOBAL_URL + "dashboard/customer/" + userid;

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                try {
                    hideProgress();
                    modelCustomerDashboard2 = GsonUtils.getInstance().gsonToModelCustomerDashboard2(json);
//                    setMyAdapter();
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
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
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
//            setMyAdapter();
            //  }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("ON RESUME;", ex.getMessage());
        }

    }

    //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)



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


    public void intializeViews(View view) {
        ball_virtual_visit = view.findViewById(R.id.ball_virtual_visit);
        ball_wellness = view.findViewById(R.id.ball_wellness);
        ball_stherapy = view.findViewById(R.id.ball_simple_therapy);
        ball_momseze = view.findViewById(R.id.ball_momseze);
        ball_pharmacy = view.findViewById(R.id.ball_pharmacy);
        ball_diagnostics = view.findViewById(R.id.ball_diagnostics);


        swipeRefreshLayout = view.findViewById(R.id.user_swipe_layout);

        llayTop = view.findViewById(R.id.llay_top);

        llay_bottom = view.findViewById(R.id.llay_bottom);

        unreadChatCtxv = view.findViewById(R.id.unread_chat_ctxv);
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

            QBRestChatService.getTotalUnreadMessagesCount(dialogSet, null).performAsync(new QBEntityCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer, Bundle bundle) {

                    unreadCount = integer;
                    badgeCount = 0;
                    if (isValid(unreadCount)) {
                        if (unreadCount > 0) {
                            unreadChatCtxv.setVisibility(View.VISIBLE);
                            unreadChatCtxv.setText(unreadCount + " unread messages");
                            badgeCount += unreadCount;
                            prefM.saveBadgeValue(badgeCount);
                            ShortcutBadger.applyCount(getActivity(), prefM.getBadgeValue());

                        } else {
                            isClickedOnChat = true;
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


        } catch (Exception e) {
            Log.w("QQQ", String.valueOf(e));
        }


    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(EventMessage event) {
       /* @Subscribe
        public void onNotifyEvent(NotifyEvent event) {*/

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                setLoading();
                showProgress();
//                handleActionCustomerDashboard();
                setChatCountInfo();
            }
        }, 2000);
    }

    public void webSearchPacifyrWOptions(String Speciality) {
        try {


            setLoading("loading...");
            showProgress();
            String url = NetworkService.GLOBAL_URL + "search/pacifyrs/byLocation";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String js = json.toString();
                        hideProgress();
                        if (isValid(json)) {
                            RespSearchPacifyrWOption pacifyrResponse = GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                            if (isValid(pacifyrResponse.getResultsList()) && pacifyrResponse.getResultsList().size() > 0) {

                                Intent intent = new Intent(getActivity(), LearnMoreActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Object", pacifyrResponse.getResultsList().get(0));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.sorry_could_not_complete_action), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), getString(R.string.sorry_could_not_complete_action), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgress();
                    }
                }
            };

            AQuery aq = new AQuery(getActivity());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getPrefManager().getAccessToken());
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", getPrefManager().getUserID());
            jsonObject.putOpt("latitude", latitude);
            jsonObject.putOpt("longitude", longitude);
            jsonObject.putOpt("visitType", "office");

            List<String> list = new ArrayList<String>();
            list.add(Speciality);
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
            jsonObject.putOpt("specialities", array);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
