package com.pacifyr.omhealthcare.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
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
            String url = "";
            if (getActivity().getPackageName().equals("com.pacifyr.leadersup")) {
                url = GLOBAL_URL + "resources/leadersup";
            } else {
                url = GLOBAL_URL + "resources";
            }
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

        resourcesRcv = view.findViewById(R.id.resources_list_rcv);
        
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
        return object != null;

    }


}
