package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.CaptureActivityPortrait;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.adapter.AttendingCompanyListAdapter;
import com.pacifyr.omhealthcare.events.views.EventTextViewBold;
import com.pacifyr.omhealthcare.home.CheckinActivity;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;


public class EventsDetailFragment extends BaseFragment {


    public static final int EVENT_SCAN = 556;
    private EventTextViewBold view_checkin;
    private IntentIntegrator qrScan;

    public EventsDetailFragment() {

    }

    private ImageView event_detail_image;
    private Bundle bundle;
    ModelEvent.Result mModelEvent;
    private TextView txtv_event_address, txtv_event_time, txtv_event_title, txtv_event_description, txtv_event_schedule;
    private RecyclerView rcv_attending_company;
    private AttendingCompanyListAdapter mAttendingCompanyListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String actualDate;
    private LinearLayout layout_checkIn;
//    private CheckBox attending_toggle;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_events, container, false);
        event_detail_image = view.findViewById(R.id.event_detail_image);
        txtv_event_title = view.findViewById(R.id.txtv_employername);
        txtv_event_time = view.findViewById(R.id.txtv_event_time);
        txtv_event_address = view.findViewById(R.id.txtv_event_address);
        txtv_event_description = view.findViewById(R.id.txtv_event_description);
        txtv_event_schedule = view.findViewById(R.id.txtv_event_schedule);
        rcv_attending_company = view.findViewById(R.id.rcv_attending_company);
        view_checkin = view.findViewById(R.id.button_checkin);

//        attending_toggle = view.findViewById(R.id.attending_toggle);


        if (isValid(getArguments()))
            bundle = getArguments();
        if (isValid(bundle)) {

            mModelEvent = (ModelEvent.Result) bundle.getSerializable("object");
            actualDate = bundle.getString("actualDate");

            event_detail_image.setTransitionName("event_transition");
            txtv_event_title.setText(mModelEvent.getTitle());
            if (isValid(actualDate)) {
                txtv_event_time.setText(Utility.getDateFormatOne(actualDate) + " at " +
                        Utility.convertTime(mModelEvent.getStartTime()) + "-" + Utility.convertTime(mModelEvent.getEndTime()));

            }
            txtv_event_address.setText(mModelEvent.getLocation());
            txtv_event_description.setText(mModelEvent.getDescription());
            txtv_event_schedule.setText(mModelEvent.getSchedule());

            Glide.with(getActivity())
                    .load(GLOBAL_IMAGE_URL + mModelEvent.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            // progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.ic_error_white)
                    .dontTransform()
                    .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                    .into(event_detail_image);

            if (isValid(mModelEvent))
                if (isValid(mModelEvent.getId()))
                    getWebData(mModelEvent.getId());



        }


        return view;
    }

    private void setUpAttending(String event_id) {

        try {
            PrefManager prefM= PrefManager.getInstance(getApplicationContext());
            String userToken=prefM.getAccessToken();
            String userid=prefM.getUserID();
            setLoading();
            showProgress();
            String url = NetworkService.GLOBAL_URL+"events/attend/"+event_id;
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();

                        String my=json.toString();
                        String message=json.toString();

                        if (json.has("status"))
                        {
                            String statusMessage= "";
                            try {
                                statusMessage = json
                                        .getString("status");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            if (isValid(statusMessage) && statusMessage.equals("success"))
                            {
//                                attending_toggle.setChecked(true);
                                if (json.has("message"))
                                {String mesg="";
                                    try {
                                        mesg=json.getString("message");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    hideStatus2();
                                    setStatus(mesg, R.color.md_green_400);
                                    showStatus();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideStatus();
                                        }
                                    }, 2000);
                                }

                            }
                            else
                            {
                                if (json.has("message"))
                                {String mesg="";
                                    try {
                                        mesg=json.getString("message");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    hideStatus2();
                                    setStatus(mesg, R.color.md_green_400);
                                    showStatus();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideStatus();
                                            getActivity().onBackPressed();
                                        }
                                    }, 2000);
                                }

                            }
                        }



                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            errorStatus();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((EventActivity) getActivity()).changeTitle("EVENT DETAILS");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((EventActivity) getActivity()).changeTitle("EVENTS");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    public void getWebData(String eventID) {

        setLoading();
        showProgress();
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {

                    hideProgress();
                    if (isValid(json)) {
                        // Log.w("@@@@",String.valueOf(json));

                        ModelEvent data = GsonUtils.getInstance().gsonToModelEvent(json);
                        if (isValid(data)) {
                            List<ModelEvent.Employer> listEvent = data.getEmployers();
                            if (isValid(listEvent))
                                setAdpater(listEvent);
                            setUpUIForAttendAndChecking(data);
                        }
                    } else {
                        if (isValid(status.getMessage())) {
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                    hideProgress();

                    e.printStackTrace();
                }
            }
        };
        getEventDetails(eventID, cb);
        qrScan = new IntentIntegrator(getActivity());
    }

    private void setUpUIForAttendAndChecking(ModelEvent data) {


        if (isValid(data.getCheckinUserIds())) {
            if (data.getCheckinUserIds().contains(getUserID())) {
                view_checkin.setText("Get ready for interviews");
                view_checkin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                view_checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CheckinActivity.class);
                        intent.putExtra("eventId", data.getId());
                        getActivity().startActivity(intent);
                    }
                });
            } else {
                view_checkin.setText("Check in");
                view_checkin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof EventActivity) {
                            ((EventActivity) getActivity()).setQRScanType(EVENT_SCAN);
                        }
                        qrScan.setPrompt("Scan a barcode");
                        qrScan.setCameraId(0);  // Use a specific camera of the device
                        qrScan.setOrientationLocked(true);
                        qrScan.setBeepEnabled(true);
                        qrScan.setCaptureActivity(CaptureActivityPortrait.class);
                        qrScan.initiateScan();

                    }
                });
            }
        }

    }

    private float getDistanceFromLocation(String latitude, String longitude) {

        double currentLatitude=((EventActivity) getActivity()).getmLatitude();
        double currentLongitude=((EventActivity) getActivity()).getmLongitude();

        double serverLatitude= Double.parseDouble(latitude);
        double serverLongitude= Double.parseDouble(longitude);


        Location loc1 = new Location("");
        loc1.setLatitude(currentLatitude);
        loc1.setLongitude(currentLongitude);
        Location loc2 = new Location("");
        loc2.setLatitude(serverLatitude);
        loc2.setLongitude(serverLongitude);
        float distanceInMeters = loc1.distanceTo(loc2);


        return distanceInMeters;



    }

    private void setAdpater(List<ModelEvent.Employer> list) {
        mAttendingCompanyListAdapter = new AttendingCompanyListAdapter(getActivity(), list);
        rcv_attending_company.setAdapter(mAttendingCompanyListAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcv_attending_company.setLayoutManager(mLayoutManager);
        rcv_attending_company.setItemAnimator(new DefaultItemAnimator());
    }






}