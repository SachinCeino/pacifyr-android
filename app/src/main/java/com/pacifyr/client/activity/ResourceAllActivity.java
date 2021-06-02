package com.pacifyr.client.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ResourcesCategoryListAdapter;
import com.pacifyr.client.adapter.ResourcesListAdapter;
import com.pacifyr.client.adapter.ResourcesListMainAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.ResourceCategory;
import com.pacifyr.client.model.ResourceMain;
import com.pacifyr.client.model.ResourceSpecialityList;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class ResourceAllActivity extends PacifyrActivity {

    private static final String TAG = ResourceAllActivity.class.getSimpleName();
    private WebView webView;
    private String titleText;
    private String spurl;
    //private ImageView backButt;
    //private LinearLayout backbtn;
    private int retryCount = 0;
    private String currentUrl;

    ResourceMain.Result object;
    RecyclerView RecyclerViewCategory,RecyclerViewList;
    List<ResourceSpecialityList.Result> resourcesList;
    ArrayList<ResourceCategory> categoryList=new ArrayList<>();
    private TextView emptyLayout;
    String param;
    private SearchView SearchView_Resource;
    private String searchQuery="";
    public String getSearchQuery() {
        return searchQuery;
    }
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_all_activity);
        onSetActionBar();
        RecyclerViewCategory=findViewById(R.id.RecyclerViewCategory);
        RecyclerViewList=findViewById(R.id.RecyclerViewList);
        emptyLayout=findViewById(R.id.emptyLayout);
        categoryList.add(new ResourceCategory("All",true));
        categoryList.add(new ResourceCategory("Articles",false));
        categoryList.add(new ResourceCategory("Videos",false));
        categoryList.add(new ResourceCategory("Presentations",false));
        SearchView_Resource= findViewById(R.id.SearchView_Resource);
        setUpTopListCategoryRecyclerView();


        if(isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().get("object"))){
            object= (ResourceMain.Result) getIntent().getExtras().get("object");
        }

        if (isValid(object)) {
            param = "article/video/presentation";
            param = GLOBAL_URL + "resources/all?speciality="+object.getSpeciality();
            getWebCategories(object.getSpeciality(), param);
        }


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
                }
                setSearchQuery("");

                try {
                    getWebCategories(object.getSpeciality(), param);
                }catch (Exception e){
                    Log.w("dsfsdfds", "sdfsdf");
                }

                return false;
            }
        });
    }

    private void loadSearchResources(String query) {

        try {
            setLoading();
            showProgress();
            String token = getPrefManager().getAccessToken();
            String url = param+"&searchText="+query;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        hideProgress();
                        resourcesList = GsonUtils.getInstance().gsonToModelResourcesSpecialitiesList(json).getResults();
                        if (isValid(resourcesList)) {
                            emptyLayout.setVisibility(View.GONE);
                            RecyclerViewList.setVisibility(View.VISIBLE);
                            setResourcesAdapter();
                        }
                        else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            RecyclerViewList.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
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

    private void getWebCategories(String speciality,String parameter) {

        try {
            setLoading();
            showProgress();
            String token = getPrefManager().getAccessToken();
            String url = parameter;

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Auth.httpStatusFilter(getApplicationContext(), status.getCode());
                    try {
                        hideProgress();
                        resourcesList = GsonUtils.getInstance().gsonToModelResourcesSpecialitiesList(json).getResults();
                        if (isValid(resourcesList)) {
                            emptyLayout.setVisibility(View.GONE);
                            RecyclerViewList.setVisibility(View.VISIBLE);
                            setResourcesAdapter();
                        }
                        else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            RecyclerViewList.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
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
        ResourcesListMainAdapter resourcesAdapter = new ResourcesListMainAdapter(resourcesList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerViewList.setLayoutManager(mLayoutManager);
        RecyclerViewList.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewList.setAdapter(resourcesAdapter);
    }

    private void setUpTopListCategoryRecyclerView() {
        ResourcesCategoryListAdapter resourcesAdapter = new ResourcesCategoryListAdapter(categoryList, getApplicationContext(), new ResourcesCategoryListAdapter.ItemClickedListenr() {
            @Override
            public void onItemClicked(int position) {
                param = categoryList.get(position).getTitle();
                if (param.equals("Articles")){
                    if (isValid(getSearchQuery()))
                        param=GLOBAL_URL + "resources/all?speciality="+object.getSpeciality()+"&type=article";
                    else
                    param=GLOBAL_URL + "resources/all?speciality="+object.getSpeciality()+"&type=article";

                }

                else if (param.equals("Videos")) {

                    if (isValid(getSearchQuery()))
                        param = GLOBAL_URL + "resources/all?speciality=" + object.getSpeciality() + "&type=video";
                    else
                        param = GLOBAL_URL + "resources/all?speciality=" + object.getSpeciality() + "&type=video";
                }
                else if (param.equals("Presentations"))
                {
                    if (isValid(getSearchQuery()))
                        param = GLOBAL_URL + "resources/all?speciality=" + object.getSpeciality() + "&type=presentation";
                    else
                        param=GLOBAL_URL + "resources/all?speciality="+object.getSpeciality()+"&type=presentation";
                }

                else
                {
                    param = GLOBAL_URL + "resources/all?speciality="+object.getSpeciality();
                }


                if (isValid(getSearchQuery()))
                    loadSearchResources(getSearchQuery());
                else
                getWebCategories(object.getSpeciality(), param);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerViewCategory.setLayoutManager(mLayoutManager);
        RecyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewCategory.setAdapter(resourcesAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        finish();
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }





    ///ACTION BAR FEATURES
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        titleTxv = findViewById(R.id.title_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.INVISIBLE);
        rightLlay.setVisibility(View.INVISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
        titleTxv.setTypeface(type);
        titleTxv.setText(getResources().getString(R.string.resources));
        leftLlay.setVisibility(View.VISIBLE);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
