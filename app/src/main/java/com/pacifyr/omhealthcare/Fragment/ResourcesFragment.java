package com.pacifyr.omhealthcare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.Payment.EditCardActivity;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.activity.TermsOfUseActivity;
import com.pacifyr.omhealthcare.adapter.ListSavedCardAdapter;
import com.pacifyr.omhealthcare.adapter.ResourcesListAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.ModelResources;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ResourcesFragment extends BaseFragment {

    ArrayList<ModelResources.Results> resourcesList;


    private CustomFontTextView paymentMethodCtxv;
    private CustomFontTextView credictCardCtxv;
    private PrefManager prefM;
    private String userToken,userid;


    public ResourcesFragment() {
        // Required empty public constructor
    }


    public static ResourcesFragment newInstance(String param1, String param2) {
        ResourcesFragment fragment = new ResourcesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesList = new ArrayList<>();



    }

    private void loadResources() {
        try {
                setLoading();
                showProgress();
                prefM = PrefManager.getInstance(getContext());
                String token = prefM.getAccessToken();
                String url = GLOBAL_URL + "resources";
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {
                            hideProgress();
                            resourcesList = GsonUtils.getInstance().gsonToModelResources(json).getResultsList();
                            if (isValid(resourcesList)) {
                                setResourcesAdapter();
                            }

                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                        }
                    }
                };
                AQuery aq = new AQuery(getContext());
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                aq.ajax(url, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }

    private void setResourcesAdapter() {
        try{
            hideProgress();
            ResourcesListAdapter resourcesAdapter = new ResourcesListAdapter(resourcesList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            resourcesRcv.setLayoutManager(mLayoutManager);
            resourcesRcv.setItemAnimator(new DefaultItemAnimator());
            resourcesRcv.setAdapter(resourcesAdapter);
        }catch (Exception e)
        {
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
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }


    private RecyclerView resourcesRcv;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resourcesRcv = (RecyclerView) view.findViewById(R.id.resources_list_rcv);
        
        if(isValid(resourcesList)){
            setResourcesAdapter();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadResources();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }


}
