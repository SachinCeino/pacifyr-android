package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ResourcesListAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.ModelResources;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ResourcesFragment extends BaseFragment {

    List<ResourceMain.Result> resourcesList;


    private MainTextView paymentMethodCtxv;
    private MainTextView credictCardCtxv;
    private PrefManager prefM;
    private String userToken,userid;


    public ResourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_resources_title));

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
                url = GLOBAL_URL + "resources/bySpeciality";
            }
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Auth.httpStatusFilter(getContext(), status.getCode());
                        try {
                            hideProgress();
                            resourcesList = GsonUtils.getInstance().gsonToModelResources(json).getResults();
                            if (isValid(resourcesList)) {
                                emptyLayout.setVisibility(View.GONE);
                                resourcesRcv.setVisibility(View.VISIBLE);
                                setResourcesAdapter();
                            }
                            else
                            {
                                emptyLayout.setVisibility(View.VISIBLE);
                                resourcesRcv.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            hideProgress();
                            e.printStackTrace();
                            emptyLayout.setVisibility(View.VISIBLE);
                            resourcesRcv.setVisibility(View.GONE);
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
            emptyLayout.setVisibility(View.VISIBLE);
            resourcesRcv.setVisibility(View.GONE);
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


    private SearchView SearchView_Resource;
    private String searchQuery="";
    private EditText EditText_search;
    public String getSearchQuery() {
        return searchQuery;
    }
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }


    private RecyclerView resourcesRcv;
    private TextView emptyLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resourcesRcv = view.findViewById(R.id.resources_list_rcv);
        SearchView_Resource= view.findViewById(R.id.SearchView_Resource);
        emptyLayout= view.findViewById(R.id.emptyLayout);
        EditText_search=view.findViewById(R.id.EditText_search);
        
        if(isValid(resourcesList)){
            setResourcesAdapter();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        EditText_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView_Resource.setIconifiedByDefault(true);
                SearchView_Resource.setFocusable(true);
                SearchView_Resource.setIconified(false);
            }
        });
        SearchView_Resource.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText_search.setVisibility(View.GONE);
            }
        });
        SearchView_Resource.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText_search.setVisibility(View.GONE);
            }
        });
        SearchView_Resource.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                setSearchQuery(query);
                if (isValid(getSearchQuery()) ) {
                    loadSearchResources(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        SearchView_Resource.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!SearchView_Resource.isIconified()) {
                    SearchView_Resource.onActionViewCollapsed();
                    EditText_search.setVisibility(View.VISIBLE);
                }
                setSearchQuery("");

                try {
                    loadResources();
                }catch (Exception e){
                    Log.w("dsfsdfds", "sdfsdf");
                }

                return false;
            }
        });

    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if(view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void loadSearchResources(String query) {

        try {
            setLoading();
            showProgress();
            prefM = PrefManager.getInstance(getContext());
            String token = prefM.getAccessToken();
            String url = "";
            url = GLOBAL_URL + "resources/bySpeciality?searchText="+query;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getContext(), status.getCode());
                    try {
                        hideProgress();
                        resourcesList = GsonUtils.getInstance().gsonToModelResources(json).getResults();
                        if (isValid(resourcesList)) {
                            emptyLayout.setVisibility(View.GONE);
                            resourcesRcv.setVisibility(View.VISIBLE);
                            setResourcesAdapter();
                        }
                        else
                        {
                            emptyLayout.setVisibility(View.VISIBLE);
                            resourcesRcv.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        emptyLayout.setVisibility(View.VISIBLE);
                        resourcesRcv.setVisibility(View.GONE);
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
