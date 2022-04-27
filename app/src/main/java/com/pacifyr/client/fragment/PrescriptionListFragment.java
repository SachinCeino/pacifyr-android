package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.PrescriptionAdapter;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.PrescriptionList;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class PrescriptionListFragment extends BaseFragment {


    private TextView emptyLayout;
    private RecyclerView mRecyclerViewPrescriptions;

    public PrescriptionListFragment() {
        // Required empty public constructor
    }


    public static PrescriptionListFragment newInstance(String param1, String param2) {
        PrescriptionListFragment fragment = new PrescriptionListFragment();

        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prescriptions_list, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyLayout = view.findViewById(R.id.emptyLayout);
        mRecyclerViewPrescriptions = view.findViewById(R.id.mRecyclerViewPrescriptions);
        getPrescriptionsList();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.my_prescriptions));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).hideConnectButton();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private void getPrescriptionsList() {

        try {
            setLoading();
            showProgress();
             PrefManager mPrefManager = PrefManager.getInstance(getContext());
            String token = mPrefManager.getAccessToken();
            String url = GLOBAL_URL + "prescription/list/user/"+mPrefManager.getUserID();

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        hideStatus();
                        Auth.httpStatusFilter(getContext(), status.getCode());
                        Log.w("@@@", String.valueOf(json));
                        if (isValid(json))
                        {
                            PrescriptionList mPrescriptionList = GsonUtils.getInstance().gsonToPrescriptionList(json);
                            if (isValidSize(mPrescriptionList.getResults())) {
                                emptyLayout.setVisibility(View.GONE);
                                mRecyclerViewPrescriptions.setVisibility(View.VISIBLE);
                                setPrescriptionListAdapter(mPrescriptionList.getResults());
                            }
                            else{
                                emptyLayout.setVisibility(View.VISIBLE);
                                mRecyclerViewPrescriptions.setVisibility(View.GONE);
                            }

                        }
                        else
                        {
                            emptyLayout.setVisibility(View.VISIBLE);
                            mRecyclerViewPrescriptions.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        mRecyclerViewPrescriptions.setVisibility(View.GONE);
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
            Log.w("@@@",String.valueOf(ex));
        }
    }

    private void setPrescriptionListAdapter(List<PrescriptionList.Result> results) {
        PrescriptionAdapter mAdapter = new PrescriptionAdapter(results, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewPrescriptions.setLayoutManager(mLayoutManager);
        mRecyclerViewPrescriptions.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewPrescriptions.setAdapter(mAdapter);
    }


}
