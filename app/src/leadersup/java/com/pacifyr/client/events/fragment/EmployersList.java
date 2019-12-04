package com.pacifyr.client.events.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.events.adapter.EmployerListCheckinAdapter;
import com.pacifyr.client.home.CheckinActivity;
import com.pacifyr.client.home.EmployerList;
import com.pacifyr.client.model.CheckinEvent;
import com.pacifyr.client.model.ModelEvent;
import com.pacifyr.client.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import java.util.List;


public class EmployersList extends BaseFragment {


    private RecyclerView rcv_eventList;
    private RecyclerView.LayoutManager mLayoutManager;
    private EmployerListCheckinAdapter mEventListAdapter;
    private List<ModelEvent.Employer> listEmployer;

    public EmployersList() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_list_employers, container, false);

        rcv_eventList = view.findViewById(R.id.rcv_employersList);

        if (getActivity() instanceof EmployerList) {
            ((EmployerList) getActivity()).setTitle("Employers List");
        }

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("data") != null) {
            listEmployer = (List<ModelEvent.Employer>) bundle.getSerializable("data");
            if (listEmployer != null)
                setAdpater(listEmployer);
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

    @Subscribe
    public void onCheckinEvent(CheckinEvent checkinEvent) {
        if (checkinEvent.getEventId() != null) {
            try {
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getActivity(), CheckinActivity.class);
            intent.putExtra("eventId", checkinEvent.getEventId());
            getActivity().startActivity(intent);
        }
    }


    private void setAdpater(List<ModelEvent.Employer> list) {

        mEventListAdapter = new EmployerListCheckinAdapter(getActivity(), list);
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL
                , false);
        rcv_eventList.setLayoutManager(mLayoutManager);
        rcv_eventList.setAdapter(mEventListAdapter);
        rcv_eventList.setItemAnimator(new DefaultItemAnimator());
    }



}
