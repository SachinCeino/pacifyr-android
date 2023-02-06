package com.pacifyr.client.events.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.events.adapter.EventListDialogAdapter;
import com.pacifyr.client.home.CheckinActivity;
import com.pacifyr.client.model.CheckinEvent;
import com.pacifyr.client.model.InterviewModel;
import com.squareup.otto.Subscribe;

import java.util.List;


public class EventsListDialog extends DialogFragment {


    private TextView title;
    private RecyclerView rcv_eventList;
    private RecyclerView.LayoutManager mLayoutManager;
    private EventListDialogAdapter mEventListAdapter;
    private List<InterviewModel.Result> listEvent;

    public EventsListDialog() {

    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_list_events, container, false);

        title = view.findViewById(R.id.title_txv);
        rcv_eventList = view.findViewById(R.id.rcv_eventList);

        title.setText("Pending Events");
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("data") != null) {
            listEvent = (List<InterviewModel.Result>) bundle.getSerializable("data");
            if (listEvent != null)
                setAdpater(listEvent);
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
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onCheckinEvent(CheckinEvent checkinEvent) {
        if (checkinEvent.getEventId() != null) {
            getDialog().dismiss();
            Intent intent = new Intent(getActivity(), CheckinActivity.class);
            intent.putExtra("eventId", checkinEvent.getEventId());
            getActivity().startActivity(intent);
        }
    }


    private void setAdpater(List<InterviewModel.Result> list) {

        mEventListAdapter = new EventListDialogAdapter(getActivity(), list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL
                , false);
        rcv_eventList.setLayoutManager(mLayoutManager);
        rcv_eventList.setAdapter(mEventListAdapter);
        rcv_eventList.setItemAnimator(new DefaultItemAnimator());
    }


}
