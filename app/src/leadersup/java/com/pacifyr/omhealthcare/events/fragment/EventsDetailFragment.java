package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.adapter.AttendingCompanyListAdapter;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class EventsDetailFragment extends BaseFragment {


    public EventsDetailFragment() {

    }

    private ImageView event_detail_image;
    private Bundle bundle;
    ModelEvent.Result mModelEvent;
    private TextView txtv_event_address, txtv_event_time, txtv_event_title, txtv_event_description, txtv_event_schedule;
    private RecyclerView rcv_attending_company;
    private AttendingCompanyListAdapter mAttendingCompanyListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_events, container, false);
        event_detail_image = view.findViewById(R.id.event_detail_image);
        txtv_event_title = view.findViewById(R.id.txtv_event_title);
        txtv_event_time = view.findViewById(R.id.txtv_event_time);
        txtv_event_address = view.findViewById(R.id.txtv_event_address);
        txtv_event_description = view.findViewById(R.id.txtv_event_description);
        txtv_event_schedule = view.findViewById(R.id.txtv_event_schedule);
        rcv_attending_company = view.findViewById(R.id.rcv_attending_company);
        if (isValid(getArguments()))
            bundle = getArguments();
        if (isValid(bundle)) {

            mModelEvent = (ModelEvent.Result) bundle.getSerializable("object");

            event_detail_image.setTransitionName("event_transition");
            txtv_event_title.setText(mModelEvent.getTitle());
            if (isValidSize(mModelEvent.getDate())) {
                txtv_event_time.setText(Utility.getDateFormatOne(mModelEvent.getDate().get(mModelEvent.getDate().size() - 1)) + " at " +
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
                            e.printStackTrace();
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
        ((EventActivity) getActivity()).changeTitle("Event Details");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((EventActivity) getActivity()).changeTitle("Events");
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

        String token = getPrefManager().getAccessToken();

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
        try {
            AQuery aq = new AQuery(getActivity());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "events/" + eventID, JSONObject.class, cb);

        } catch (Exception ex)

        {
            hideProgress();

            ex.printStackTrace();
        }
        //setAdpater(list);
    }

    private void setAdpater(List<ModelEvent.Employer> list) {
        mAttendingCompanyListAdapter = new AttendingCompanyListAdapter(getActivity(), list);
        rcv_attending_company.setAdapter(mAttendingCompanyListAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcv_attending_company.setLayoutManager(mLayoutManager);
        rcv_attending_company.setItemAnimator(new DefaultItemAnimator());

    }


}
