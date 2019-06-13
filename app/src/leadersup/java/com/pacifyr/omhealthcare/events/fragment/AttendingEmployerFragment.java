package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.pacifyr.omhealthcare.events.adapter.CurrentOpeningAdapter;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class AttendingEmployerFragment extends BaseFragment {


    public AttendingEmployerFragment() {

    }

    private ImageView img_company_logo;
    private TextView txtv_company_phone, txtv_company_mail, txtv_company_title;
    private RecyclerView rcv_attendList;
    private CurrentOpeningAdapter mCurrentOpeningAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ModelEvent.Employer object;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_attend_employer, container, false);
        img_company_logo = view.findViewById(R.id.img_company_logo);

        txtv_company_title = view.findViewById(R.id.txtv_company_title);
        txtv_company_mail = view.findViewById(R.id.txtv_company_mail);
        txtv_company_phone = view.findViewById(R.id.txtv_company_phone);
        rcv_attendList = view.findViewById(R.id.rcv_attendList);

        Bundle bundle = getArguments();
        if (isValid(bundle) && isValid(bundle.getSerializable("object")))
            object = (ModelEvent.Employer) bundle.getSerializable("object");
        if (isValid(object)) {
            txtv_company_title.setText(object.getName());
            txtv_company_mail.setText(object.getEmail());
            String phoneNumbers = "";
            if (isValid(object.getPrimaryPhone()))
                phoneNumbers = object.getPrimaryPhone();
            if (isValid(object.getPhone()))
                phoneNumbers = "," + object.getPhone();
            txtv_company_phone.setText(phoneNumbers);

            Glide.with(getActivity())
                    .load(object.getImage())
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
                    .into(img_company_logo);

            getWebData(object.getId());
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
        ((EventActivity) getActivity()).changeTitle("Attending Employer");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((EventActivity) getActivity()).changeTitle("Event Details");
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


    public void getWebData(String emplyerId) {

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
                            List<ModelEvent.Position> listEvent = data.getPositions();
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

            aq.ajax(GLOBAL_URL + "employer/" + emplyerId, JSONObject.class, cb);

        } catch (Exception ex)

        {
            hideProgress();

            ex.printStackTrace();
        }
    }

    private void setAdpater(List<ModelEvent.Position> list) {
        mCurrentOpeningAdapter = new CurrentOpeningAdapter(getActivity(), list);
        rcv_attendList.setAdapter(mCurrentOpeningAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_attendList.setLayoutManager(mLayoutManager);
        rcv_attendList.setItemAnimator(new DefaultItemAnimator());

    }


}
