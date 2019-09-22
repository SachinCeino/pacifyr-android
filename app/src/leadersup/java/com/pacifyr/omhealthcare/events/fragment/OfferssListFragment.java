package com.pacifyr.omhealthcare.events.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.events.adapter.EventListAdapter;
import com.pacifyr.omhealthcare.events.adapter.OfferListAdapter;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.events.model.ModelOffer;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import java.util.List;


public class OfferssListFragment extends BaseFragment {


    public OfferssListFragment() {

    }


    private RecyclerView rcv_offerList;
    private RecyclerView.LayoutManager mLayoutManager;
    private OfferListAdapter mOfferListAdapter;
    private List<ModelOffer.Result> listEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_offers, container, false);

        rcv_offerList = view.findViewById(R.id.rcv_offerList);
        Bundle bundle = getArguments();
        if (isValid(bundle) && isValid(bundle.getSerializable("data"))) {
            listEvent = (List<ModelOffer.Result>) bundle.getSerializable("data");
            if (isValid(listEvent))
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


}
