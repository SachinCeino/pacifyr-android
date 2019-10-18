package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.adapter.EventListAdapter;
import com.pacifyr.omhealthcare.events.adapter.OfferListAdapter;
import com.pacifyr.omhealthcare.model.ModelOffer;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class OfferssListFragment extends BaseFragment {


    public OfferssListFragment() {

    }


    private RecyclerView rcv_offerList;
    private RecyclerView.LayoutManager mLayoutManager;
    private OfferListAdapter mOfferListAdapter;
    private RelativeLayout _mRelativeLayout_empty_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_offers, container, false);

        rcv_offerList = view.findViewById(R.id.rcv_offerList);
        _mRelativeLayout_empty_layout = view.findViewById(R.id.empty_layout);
        _mRelativeLayout_empty_layout.setVisibility(View.GONE);

        getWebData();


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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
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


    private void setAdpater(List<ModelOffer.Result> list) {

        mOfferListAdapter = new OfferListAdapter(getActivity(), list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL
                , false);
        rcv_offerList.setLayoutManager(mLayoutManager);
        rcv_offerList.setAdapter(mOfferListAdapter);
        rcv_offerList.setItemAnimator(new DefaultItemAnimator());
    }


    public void getWebData() {

        setLoading();
        showProgress();

        String token = getPrefManager().getAccessToken();

        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {

                    hideProgress();
                    if (isValid(json)) {
                        ModelOffer data = GsonUtils.getInstance().gsonToModelOffer(json);
                        if (isValid(data)) {
                            List<ModelOffer.Result> listOffer = data.getResults();
                            if (isValid(listOffer)) {
                                _mRelativeLayout_empty_layout.setVisibility(View.GONE);
                                setAdpater(listOffer);
                            } else {
                                _mRelativeLayout_empty_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        if (isValid(status.getMessage())) {
                            _mRelativeLayout_empty_layout.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } catch (Exception e) {

                    hideProgress();
                    _mRelativeLayout_empty_layout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getActivity());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(GLOBAL_URL + "offer/list/byUser?userId=" + getPrefManager().getUserID() + "&skip=0&limit=10", JSONObject.class, cb);

        } catch (Exception ex) {
            hideProgress();

            ex.printStackTrace();
        }

    }

}
