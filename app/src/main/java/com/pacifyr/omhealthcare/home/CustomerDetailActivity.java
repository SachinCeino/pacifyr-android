package com.pacifyr.omhealthcare.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.FlexLayout;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.model.pacifyr.MPastSessions;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.ListCustomerSessionAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CustomerNotifyEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.mPastSessions;
import static com.pacifyr.omhealthcare.network.NetworkService.mPastSessionsPaged;
import static com.pacifyr.omhealthcare.network.NetworkService.modelAllEmotions;
public class CustomerDetailActivity extends PacifyrAppCompactActivity {

    private LinearLayout topLlay;
    private CircleImageView profileCimv;
    private CircleImageView emotionCimv;
    private CardView bioCardView;
    private CustomFontTextView bioCtxv;
    private CustomFontTextView pastSessionCtxv;
    private RecyclerView pacifyrListRcv;

    private CustomFontTextView emotionNameCtxv;

    private  PrefManager prefM;
    private  String userid,token;
    String url;
    private ListCustomerSessionAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<MEmotion> emotionList;


    private int lastVisibleItem, totalItemCount;
    private boolean isLoading=false;
    private boolean isFullLoaded=false;
    private int visibleThreshold = 2;
    int flag=0;
    private SwipeRefreshLayout swipeLay,search_swipe_lay;

    private FlexLayout emptyFlay;
    private DeviceFitImageView im;
    private CustomFontTextView welcomeDtxv,emptyDtxv;
    boolean isBackendError=false;
    ArrayList<MPastSessions.Results> pastsessions_list=new ArrayList<MPastSessions.Results>();

    public static MUser custListUser=null;
    public static void start (Context context,MUser user) {

        Intent intent = new Intent(context, CustomerDetailActivity.class);
        CustomerDetailActivity.custListUser=user;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void start (Activity context, MUser user) {

        Intent intent = new Intent(context, CustomerDetailActivity.class);
        CustomerDetailActivity.custListUser=user;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        Log.i("uuuuuurrrlllAA","u:  ");

        try {

            prefM = PrefManager.getInstance(CustomerDetailActivity.this);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
            //&toDate=%@
            onSetActionBar();

            topLlay = (LinearLayout) findViewById(R.id.top_llay);
            profileCimv = (CircleImageView) findViewById(R.id.profile_cimv);
            emotionCimv = (CircleImageView) findViewById(R.id.emotion_cimv);
            bioCardView = (CardView) findViewById(R.id.bio_card_view);
            bioCtxv = (CustomFontTextView) findViewById(R.id.bio_ctxv);
            pastSessionCtxv = (CustomFontTextView) findViewById(R.id.past_session_ctxv);
            pacifyrListRcv = (RecyclerView) findViewById(R.id.pacifyr_list_rcv);

            emotionNameCtxv = (CustomFontTextView) findViewById(R.id.emotion_name_ctxv);

            emptyDtxv = (CustomFontTextView) findViewById(R.id.empty_dtxv);
            emptyFlay = (FlexLayout) findViewById(R.id.empty_flay);
            emptyFlay.setVisibility(View.VISIBLE);
            swipeLay = (SwipeRefreshLayout)findViewById(R.id.swipe_lay);
            swipeLay.setVisibility(View.VISIBLE);

            layoutManager = new LinearLayoutManager(CustomerDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            pacifyrListRcv.setLayoutManager(layoutManager);
            swipeLay.setColorSchemeResources(R.color.md_blue_300, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.md_amber_300);
            swipeLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLay.setRefreshing(true);
                    webGetPastSessions(0, 20);

                }
            });

            Log.i("uuuuuurrrlllAA1","u:  ");
            setAdapter();
            webGetPastSessions(0,20);



            /*ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            emotionCimv.setColorFilter(filter);*/

            bioCtxv.setText(custListUser.getDescription());

            String imgUrl = custListUser.getImageUrl();
            if (isValid(imgUrl))
                Picasso.with(CustomerDetailActivity.this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(profileCimv);
            //custListUser

            String emotion = custListUser.getEmotionId();
            if(isValid(modelAllEmotions)) {
                emotionList = modelAllEmotions.getResultsList();
                for (int i = 0; i < emotionList.size(); i++) {
                    String id = emotionList.get(i).getId();
                    if (isValid(emotion)) {
                        if (id.equals(emotion)) {
                            emotionNameCtxv.setText(emotionList.get(i).getTitle());
                            Picasso.with(CustomerDetailActivity.this).load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage()).into(emotionCimv);
                        }
                    }
                }
            }else{
                NetworkService.webListEmotions(CustomerDetailActivity.this);
            }
            emotionCimv.setColorFilter(Color.parseColor("#ffffff"));

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }
    public void  showLoading() {
        try {
            pastsessions_list.add(null);
            adapter.notifyItemInserted(pastsessions_list.size() - 1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void  hideLoading() {
        try {
            pastsessions_list.remove(null);
            //  loading_rlay.setVisibility(View.GONE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void webGetPastSessions(final int skip, final int limit) {
        try
        {
            if(skip==0) {
                setLoading();
                showProgress();
            }
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                       /* String js=json.toString();
                        mPastSessions= GsonUtils.getInstance().gsonToMPastSessions(json);
                        String s = model.toString();
                        adapter =new ListCustomerSessionAdapter(CustomerDetailActivity.this,model.getResultsList());
                        pacifyrListRcv.setAdapter(adapter);*/



                        if (skip == 0) {
                            mPastSessions = GsonUtils.getInstance().gsonToMPastSessions(json);
                            mPastSessionsPaged = null;
                            postEventOnMainThread(new CustomerNotifyEvent(true, false));
                        } else {
                            if (mPastSessions != null) {
                                mPastSessionsPaged = GsonUtils.getInstance().gsonToMPastSessions(json);
                                postEventOnMainThread(new CustomerNotifyEvent(true, true));

                            } else {
                                mPastSessions = GsonUtils.getInstance().gsonToMPastSessions(json);
                                mPastSessionsPaged = null;
                                postEventOnMainThread(new CustomerNotifyEvent(true, false));
                            }
                        }

                //   setAdapter();
                       


                    }
                    catch(Exception excp){
                        hideProgress();
                        excp.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(CustomerDetailActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken",token);
            url = GLOBAL_URL+"session/customer/all?userId="+custListUser.getId()+"&skip="+skip+"&limit="+limit;
            
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }
    private void setAdapter() {
        try {
            Log.i("uuuuuurrrlllAA2","u:  ");
            LogUtils.LOGD("jithish", "FF  setAdapter " );

            emptyFlay.setVisibility(View.VISIBLE);
            emptyDtxv.setText("Loading ..");
            if(isValid(mPastSessions)) {
                Log.i("uuuuuurrrlllAA5","u:  "+mPastSessions);
                pastsessions_list= mPastSessions.getResultsList();
                Log.i("uuuuuurrrlllAA3","u:  "+pastsessions_list);
                if(isValid(pastsessions_list)) {
                  //  final long total_count= mPastSessions.getTotal_count();
                    Log.i("uuuuuurrrlllAA4","u:  "+pastsessions_list);
                    emptyFlay.setVisibility(View.GONE);
                    adapter=new ListCustomerSessionAdapter(CustomerDetailActivity.this,pastsessions_list);
                    pacifyrListRcv.setItemAnimator(new DefaultItemAnimator());
                    pacifyrListRcv.setAdapter(adapter);


/*
                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) pacifyrListRcv.getLayoutManager();
                    pacifyrListRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) &&total_count>totalItemCount) {
                                isLoading = true;
                                //    feedlist.add(null);
                                // adapter.notifyItemInserted(feedlist.size() - 1);
                                int limit =(int) total_count - pastsessions_list.size();
                                int skip = pastsessions_list.size();
                                if(limit > 10) {
                                    limit = 10;
                                }

                                if(limit>0) {
                                    if (!isBackendError) {
                                        showLoading();
                                        webGetPastSessions( skip, limit);
                                    } else {
                                        isLoading = false;
                                        hideLoading();
                                    }
                                }
                               *//* if(limit>0)
                                    NetworkService.startActionListCustomerALL(CustomerDetailActivity.this, skip, limit);*//*
                                LogUtils.LOGD("jithish", "FF setAdapter skip =" +skip+"  limit ="+limit);
                                if(limit<=0) {
                                    isLoading = false;
                                    hideLoading();
                                }
                            }
                        }
                    });*/
                }
                else
                {
                    adapter =new ListCustomerSessionAdapter(CustomerDetailActivity.this);
                    pacifyrListRcv.setItemAnimator(new DefaultItemAnimator());
                    pacifyrListRcv.setAdapter(adapter);
                    emptyFlay.setVisibility(View.VISIBLE);

                    if(flag==0)
                        emptyDtxv.setText("Loading ..");
                    else
                        defSetText(emptyDtxv,"No items in your feed");

                }
            }
            else
            {
                adapter =new ListCustomerSessionAdapter(CustomerDetailActivity.this);
                pacifyrListRcv.setItemAnimator(new DefaultItemAnimator());
                pacifyrListRcv.setAdapter(adapter);
                emptyFlay.setVisibility(View.VISIBLE);

                if(flag==0)
                    emptyDtxv.setText("Loading ..");
                else
                    defSetText(emptyDtxv,"No items in your feed");
            }
            flag++;


        }
        catch (Exception e)
        {
            LogUtils.LOGD("exception", "FF  setAdapter " + e.getMessage());

        }
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        String emotion = custListUser.getEmotionId();

        if(isValid(modelAllEmotions)) {
            emotionList = modelAllEmotions.getResultsList();
            for (int i = 0; i < emotionList.size(); i++) {
                String id = emotionList.get(i).getId();
                if (isValid(emotion)) {
                    if (id.equals(emotion)) {
                        //afterFeelCtxv.setText(emotionList.get(i).getTitle());
                        Picasso.with(CustomerDetailActivity.this).load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage()).into(emotionCimv);
                    }
                }
            }
        }

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, CustomerDetailActivity.class);
        context.startActivity(intent);
    }


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
            actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
            bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
            leftLlay = (LinearLayout) findViewById(R.id.left_llay);
            backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
            leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
            titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
            rightLlay = (LinearLayout) findViewById(R.id.right_llay);
            rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
            rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, custListUser.getName());
            titleTxv.setTextColor(getResources().getColor(R.color.white));
            backImv.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.GONE);

            //backImv.setBackgroundResource(R.drawable.leftmen);
            backImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            /*    Intent intent = new Intent(CustomerDetailActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();*/
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Subscribe
    public void onCustomerNotifyEvent(CustomerNotifyEvent event) {
        if(event.isSuccess)
        {
            if (isValid(swipeLay)) {
                if (swipeLay.isRefreshing()) {
                    swipeLay.setRefreshing(false);

                }
            }
            LogUtils.LOGD("jithish", "FF onNotifyEvent");
            if (event.isPaged) {
                if (isValid(mPastSessionsPaged)) {
                    if (isValid(mPastSessions))
                        if (isValidSize(pastsessions_list))
                            if (isValid(pacifyrListRcv)) {
                                hideLoading();
                                isLoading = false;

                                    pastsessions_list.addAll(mPastSessionsPaged.getResultsList());

                                    //   _listView.setIsLoading(false);
                                    adapter.notifyDataSetChanged();
                                    mPastSessions.addResults(mPastSessionsPaged.getResults());

                            }
                }
                else
                {
                    hideLoading();
                    isBackendError=true;
                    adapter.notifyDataSetChanged();
                }

            } else {
                isBackendError=false;
                setAdapter();
            }
        }
        //}

    }
}
