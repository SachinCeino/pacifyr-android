package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.AppointmentsCancelRejecListAdapter;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class AppointmentsRejOrCancelledFragment extends BaseFragment {

    private PrefManager mPrefManager;
    private RecyclerView mRecyclerViewAppointments;
    private AppointmentsCancelRejecListAdapter mAppointmentsListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppointmentModel mAppointmentModel;
    private TextView emptyLayout;
    private SwipeRefreshLayout swiperefresh;


    public AppointmentsRejOrCancelledFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void getAppointmentsList() {

        try {
            setLoading();
//            showProgress();
            swiperefresh.setRefreshing(true);
            mPrefManager = PrefManager.getInstance(getContext());
            String token = mPrefManager.getAccessToken();
            String url = GLOBAL_URL + "schedule/user/" + mPrefManager.getUserID() + "/bookings/cancelled?skip=0&limit=30";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    swiperefresh.setRefreshing(false);
                    try {

//                        hideProgress();
                        hideStatus();
                        if (isValid(json))
                        {
                            mAppointmentModel = GsonUtils.getInstance().gsonToAppointmentModel(json);
                            if (isValid(mAppointmentModel.getResults())) {
                                setAppointmentListAdapter(mAppointmentModel.getResults());
                                emptyLayout.setVisibility(View.GONE);
                                mRecyclerViewAppointments.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                emptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerViewAppointments.setVisibility(View.GONE);
                            }


                        }
                        else
                        {
                            emptyLayout.setVisibility(View.VISIBLE);
                            mRecyclerViewAppointments.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        mRecyclerViewAppointments.setVisibility(View.GONE);
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            hideProgress();
            ex.printStackTrace();
        }
    }


    private void setAppointmentListAdapter(List<AppointmentModel.Result> mList) {
        try {
            hideProgress();
            mAppointmentsListAdapter = new AppointmentsCancelRejecListAdapter(mList, getActivity());
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerViewAppointments.setLayoutManager(mLayoutManager);
            mRecyclerViewAppointments.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewAppointments.setAdapter(mAppointmentsListAdapter);
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }







    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);
        mRecyclerViewAppointments = root.findViewById(R.id.mRecyclerViewAppointments);
        swiperefresh = root.findViewById(R.id.swiperefresh);
        emptyLayout=root.findViewById(R.id.emptyLayout);
        getAppointmentsList();
        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppointmentsList();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public Boolean isValid(Object object) {
        return object != null;

    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }



}
