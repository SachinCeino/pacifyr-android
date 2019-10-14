package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.activity.OffersActivity;
import com.pacifyr.omhealthcare.events.adapter.AttendingCompanyListAdapter;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.model.ModelOffer;
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
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class OffersDetailFragment extends BaseFragment {


    public OffersDetailFragment() {

    }

    private TextView offer_date, job_title, company_title;
    private ImageView company_logo;

    private ModelOffer.Result object;
    private Bundle bundle;
    private LinearLayout layout_bennefits;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_offers, container, false);


        offer_date = view.findViewById(R.id.offer_date);
        job_title = view.findViewById(R.id.job_title);
        company_title = view.findViewById(R.id.company_title);
        company_logo = view.findViewById(R.id.company_logo);
        layout_bennefits = view.findViewById(R.id.layout_bennefits);

        bundle = getArguments();
        if (isValid(bundle))
            object = (ModelOffer.Result) bundle
                    .getSerializable("data");
        if (isValid(bundle.getString("date")))
            offer_date.setText(": " + bundle.getString("date"));

        if (isValid(object)) {
            if (isValid(object.getEmployer().getName()))
                company_title.setText(object.getEmployer().getName() + "");

            if (isValid(object.getEmployerPosition().getTitle()))
                job_title.setText(": " + object.getEmployerPosition().getTitle() + "");

            if (isValid(object.getEmployer().getImage())) {
                Glide.with(getActivity())
                        .load(GLOBAL_IMAGE_URL + object.getEmployer().getImage())
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
                        .into(company_logo);
            }

            if (isValid(object.getEmployerPosition().getBenefits()))
                addBenefitsView(object.getEmployerPosition().getBenefits());
        }


        return view;
    }


    private void addBenefitsView(List<String> stringData) {
        for (String item : stringData) {
            TextView tv = new com.pacifyr.omhealthcare.events.views.EventTextViewMedium(getActivity());
            tv.setPadding(3, 3, 3, 3);
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_single_dot, 0, 0, 0);
            tv.setCompoundDrawablePadding(3);
            tv.setText(item);
            layout_bennefits.addView(tv);

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
        ((OffersActivity) getActivity()).changeTitle("OFFER DETAILS");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((OffersActivity) getActivity()).changeTitle("OFFERS");
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

        } catch (Exception ex) {
            hideProgress();

            ex.printStackTrace();
        }
        //setAdpater(list);
    }


}