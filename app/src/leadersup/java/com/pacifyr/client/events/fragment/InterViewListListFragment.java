package com.pacifyr.client.events.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.events.adapter.InterViewsListAdapter;
import com.pacifyr.client.events.adapter.OfferListAdapter;
import com.pacifyr.client.model.InterviewModel;
import com.pacifyr.client.model.ModelOffer;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.List;

import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;


public class InterViewListListFragment extends BaseFragment {


    public InterViewListListFragment() {

    }


    private RecyclerView _rcv_InterViewsList;
    private RecyclerView.LayoutManager _mLayoutManager;
    private InterViewsListAdapter _InterViewsListAdapter;
    private RelativeLayout _mRelativeLayout_empty_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_interviews, container, false);

        _rcv_InterViewsList = view.findViewById(R.id._rcv_InterViewsList);
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


    private void setAdpater(List<InterviewModel.Result> list) {

        _InterViewsListAdapter = new InterViewsListAdapter(getActivity(), list);
        _mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL
                , false);
        _rcv_InterViewsList.setLayoutManager(_mLayoutManager);
        _rcv_InterViewsList.setAdapter(_InterViewsListAdapter);
        _rcv_InterViewsList.setItemAnimator(new DefaultItemAnimator());
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
                        InterviewModel data = GsonUtils.getInstance().gsonToInterViewModel(json);
                        if (isValid(data)) {
                            List<InterviewModel.Result> listOffer = data.getResults();
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
            aq.ajax(GLOBAL_URL + "interview/list/byUser?userId=" + getPrefManager().getUserID() + "&skip=0&limit=25", JSONObject.class, cb);
            //aq.ajax(GLOBAL_URL + "events/checkins/byUser/" + getPrefManager().getUserID(), JSONObject.class, cb);

        } catch (Exception ex) {
            hideProgress();

            ex.printStackTrace();
        }

    }

}
