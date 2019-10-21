package com.pacifyr.omhealthcare.events.fragment;


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

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.adapter.EmployerListDialogAdapter;
import com.pacifyr.omhealthcare.home.CheckinActivity;
import com.pacifyr.omhealthcare.model.CheckinEvent;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.model.OpenEmployerEvent;
import com.squareup.otto.Subscribe;

import java.util.List;


public class EmployersListDialog extends DialogFragment {


    private TextView title;
    private RecyclerView rcv_eventList;
    private RecyclerView.LayoutManager mLayoutManager;
    private EmployerListDialogAdapter mEventListAdapter;
    private List<ModelEvent.Employer> listEmployer;

    public EmployersListDialog() {

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
        View view = inflater.inflate(R.layout.dialog_list_employers, container, false);

        title = view.findViewById(R.id.title_txv);
        rcv_eventList = view.findViewById(R.id.rcv_employersList);

        title.setText("Employers List");
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


    private void setAdpater(List<ModelEvent.Employer> list) {

        mEventListAdapter = new EmployerListDialogAdapter(getActivity(), list);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL
                , false);
        rcv_eventList.setLayoutManager(mLayoutManager);
        rcv_eventList.setAdapter(mEventListAdapter);
        rcv_eventList.setItemAnimator(new DefaultItemAnimator());
    }

    @Subscribe
    public void onOpenEmployerEvent(OpenEmployerEvent openEmployerEvent) {
        dismiss();
    }


}
