package com.pacifyr.client.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.adapter.ResourcesListAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.ModelResources;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.model.ResourceTodayHighlight;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ResourcesFragment extends BaseFragment {

    List<ResourceMain.Result> resourcesList;
    List<ResourceTodayHighlight.Result> mResourceTodayHighlight;


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
        ((DashboardActivity)getActivity()).showAddButton();
    }

    public static ResourcesFragment newInstance(String param1, String param2) {
        ResourcesFragment fragment = new ResourcesFragment();

        return fragment;
    }
    private ImageView Today_Image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesList = new ArrayList<>();




    }

    private void getHighLitedResource() {

        try {
            prefM = PrefManager.getInstance(getContext());
            String token = prefM.getAccessToken();
            String url = GLOBAL_URL + "resources/highlight";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getContext(), status.getCode());
                    try {
                        if (isValid(json)) {
                            mResourceTodayHighlight = GsonUtils.getInstance().gsonToResourceTodayHighlight(json).getResults();
                            if (isValid(mResourceTodayHighlight) && isValid(mResourceTodayHighlight.get(0)))
                            {
                                ResourceTodayHighlight.Result mObject=mResourceTodayHighlight.get(0);
                                if (isValid(mObject))
                                {
                                    if (isValid(mObject.getImage()))
                                    {
                                        Picasso.with(getActivity()).load(NetworkService.GLOBAL_IMAGE_URL + mObject.getImage())
                                                .placeholder(R.drawable.placeholder).into(Today_Image);
                                    }
                                    if (isValid(mObject.getAuthor()))
                                    {
                                        today_TextViewAuther.setText(mObject.getAuthor()+"");
                                    }
                                    if (isValid(mObject.getTitle()))
                                    {
                                        Today_Description.setText(mObject.getTitle()+"");
                                    }

                                    cardViewBanner.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (mObject.getType().equals("article"))
                                            {
                                                Today_ArticleIcon.setVisibility(View.VISIBLE);
                                                Intent i=new Intent(getActivity(), TermsOfUseActivity.class);
                                                i.putExtra("spurl", mObject.getUrl());
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                            }
                                            else if (mObject.getType().equals("video"))
                                            {
                                                Today_videoIcon.setVisibility(View.VISIBLE);
                                                if (mObject.getVideo()==null) {
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mObject.getUrl()));
                                                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(browserIntent);
                                                }
                                            }
                                            else
                                            {
                                                Today_ArticleIcon.setVisibility(View.VISIBLE);
                                                Intent i=new Intent(getActivity(), TermsOfUseActivity.class);
                                                i.putExtra("spurl", mObject.getUrl());
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                            }
                                        }
                                    });
                                    if (mObject.getType().equals("article"))
                                    {
                                        Today_ArticleIcon.setVisibility(View.VISIBLE);
                                    }
                                    else if (mObject.getType().equals("video"))
                                    {
                                        Today_videoIcon.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        Today_ArticleIcon.setVisibility(View.VISIBLE);
                                    }
                                    cardViewBanner.setVisibility(View.VISIBLE);
                                }
                            }
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
    private TextView emptyLayout,Today_Title,today_TextViewAuther,Today_Description,Today_ArticleIcon,Today_videoIcon;
    private CardView cardViewBanner;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resourcesRcv = view.findViewById(R.id.resources_list_rcv);
        SearchView_Resource= view.findViewById(R.id.SearchView_Resource);
        emptyLayout= view.findViewById(R.id.emptyLayout);
        EditText_search=view.findViewById(R.id.EditText_search);
        Today_Image=view.findViewById(R.id.Today_Image);
        Today_Title=view.findViewById(R.id.Today_Title);
        today_TextViewAuther=view.findViewById(R.id.today_TextViewAuther);
        Today_Description=view.findViewById(R.id.Today_Description);
        Today_ArticleIcon=view.findViewById(R.id.Today_ArticleIcon);
        Today_videoIcon=view.findViewById(R.id.Today_videoIcon);
        cardViewBanner=view.findViewById(R.id.cardViewBanner);
        
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

        getHighLitedResource();
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
        ((DashboardActivity)getActivity()).hideAddButton();

    }

    public Boolean isValid(Object object) {
        return object != null;

    }


}
