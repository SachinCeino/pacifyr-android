package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.adapter.EventListAdapter;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.BaseFragment;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class EventsListFragment extends BaseFragment {


    public EventsListFragment() {

    }


    private RecyclerView rcv_eventList;
    private RecyclerView.LayoutManager mLayoutManager;
    private EventListAdapter mEventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_events, container, false);

        rcv_eventList = view.findViewById(R.id.rcv_eventList);
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
                        // Log.w("@@@@",String.valueOf(json));

                        ModelEvent data = GsonUtils.getInstance().gsonToModelEvent(json);
                        if (isValid(data)) {
                            List<ModelEvent.Result> listEvent = data.getResults();
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

            aq.ajax(GLOBAL_URL + "events/list?skip=0&limit=10", JSONObject.class, cb);

        } catch (Exception ex)

        {
            hideProgress();

            ex.printStackTrace();
        }

    }

    private void setAdpater(List<ModelEvent.Result> list) {

        mEventListAdapter = new EventListAdapter(getActivity(), list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL
                , false);
        rcv_eventList.setLayoutManager(mLayoutManager);
        rcv_eventList.setAdapter(mEventListAdapter);
        rcv_eventList.setItemAnimator(new DefaultItemAnimator());
    }


}
