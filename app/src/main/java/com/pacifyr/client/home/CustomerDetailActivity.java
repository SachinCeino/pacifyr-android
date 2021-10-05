package com.pacifyr.client.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListCustomerSessionAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.CustomerNotifyEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.quickblox.util.NestedScroll;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MPastSessions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.mPastSessions;
import static com.pacifyr.client.network.NetworkService.mPastSessionsPaged;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;

public class CustomerDetailActivity extends PacifyrAppCompactActivity {

    private LinearLayout topLlay;
    private CircleImageView profileCimv;
    private CircleImageView emotionCimv;
    private CardView bioCardView;
    private MainTextView bioCtxv;
    private MainTextView pastSessionCtxv;
    private RecyclerView pacifyrListRcv;

    private MainTextView emotionNameCtxv;

    private  PrefManager prefM;
    private  String userid,token;
    String url;
    private ListCustomerSessionAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<MEmotion> emotionList;


    private int lastVisibleItem, totalItemCount;
    private boolean isLoading = false;
    private boolean isFullLoaded = false;
    private int visibleThreshold = 2;
    int flag = 0, skip = 0, limit = 20;
    //    private TextView welcomeDtxv, emptyDtxv;
    boolean isBackendError = false;
    private SwipeRefreshLayout search_swipe_lay;
    //    private FlexLayout emptyFlay;
    private DeviceFitImageView im;

    private TextView TextView_empty;


    ArrayList<MPastSessions.Results> pastsessions_list = new ArrayList<MPastSessions.Results>();

    public static MUser custListUser = null;
    private NestedScrollView nested_scroll;

    public static void start(Context context, MUser user) {

        Intent intent = new Intent(context, CustomerDetailActivity.class);
        CustomerDetailActivity.custListUser = user;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean restoreCall;
    public static void start(Context context, MUser user,boolean restoreCall) {

        Intent intent = new Intent(context, CustomerDetailActivity.class);
        CustomerDetailActivity.custListUser = user;
        intent.putExtra("restoreCall", restoreCall);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Activity context, MUser user) {

        Intent intent = new Intent(context, CustomerDetailActivity.class);
        CustomerDetailActivity.custListUser=user;
       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        try {

            prefM = PrefManager.getInstance(CustomerDetailActivity.this);
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
            EventBus.getDefault().register(this);
            onSetActionBar();

            topLlay = findViewById(R.id.top_llay);
            profileCimv = findViewById(R.id.profile_cimv);
            emotionCimv = findViewById(R.id.emotion_cimv);
            bioCardView = findViewById(R.id.bio_card_view);
            bioCtxv = findViewById(R.id.bio_ctxv);
            pastSessionCtxv = findViewById(R.id.past_session_ctxv);
            pacifyrListRcv = findViewById(R.id.pacifyr_list_rcv);
            TextView_empty= findViewById(R.id.TextView_empty);
            emotionNameCtxv = findViewById(R.id.emotion_name_ctxv);

           /* emptyDtxv = findViewById(R.id.empty_dtxv);
            emptyFlay = findViewById(R.id.empty_flay);
            emptyFlay.setVisibility(View.VISIBLE);*/
//            swipeLay = findViewById(R.id.swipe_lay);
            nested_scroll = findViewById(R.id.nested_scroll);
//            swipeLay.setVisibility(View.VISIBLE);
            if (getIntent() != null && getIntent().getExtras() != null) {
                restoreCall = getIntent().getExtras().getBoolean("restoreCall", false);
            }

            layoutManager = new LinearLayoutManager(CustomerDetailActivity.this, LinearLayoutManager.VERTICAL, false);
            pacifyrListRcv.setLayoutManager(layoutManager);
//            swipeLay.setColorSchemeResources(R.color.md_blue_300, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.md_amber_300);
            /*swipeLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLay.setRefreshing(true);
                    webGetPastSessions(0, 20);

                }
            });*/

            setAdapter();
            webGetPastSessions(skip, limit);



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
            } else {
                NetworkService.webListEmotions(CustomerDetailActivity.this);
            }
            emotionCimv.setColorFilter(Color.parseColor("#ffffff"));

            nested_scroll.setOnScrollChangeListener(new NestedScroll() {
                @Override
                public void onScroll() {
                    skip += 20;
                    webGetPastSessions(skip, limit);
                }
            });

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
                            if (isValid(mPastSessions) &&isValid(mPastSessions.getResultsList()))
                            {
                                mPastSessionsPaged = null;
                                postEventOnMainThread(new CustomerNotifyEvent(true, false));
                                TextView_empty.setVisibility(View.GONE);
                            }
                            else {
                                TextView_empty.setVisibility(View.VISIBLE);
                            }

                        } else {
                            TextView_empty.setVisibility(View.GONE);
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
                       


                    } catch (Exception excp) {
                        hideProgress();
                        TextView_empty.setVisibility(View.VISIBLE);
                        excp.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(CustomerDetailActivity.this);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken",token);
            cb.header("AccessToken", token);
            if (!isCallActive) {
                url = GLOBAL_URL + "session/pacifyr/all?userId=" + custListUser.getId() + "&skip=" + skip + "&limit=" + limit;
            } else {
                url = GLOBAL_URL + "session/pacifyr/all?userId=" + custListUser.getId() + "&skip=" + skip + "&limit=" + limit + "&type=session_started";
            }

            aq.ajax(url, JSONObject.class, cb);
        }
        catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }
    private void setAdapter() {
        try {

           /* emptyFlay.setVisibility(View.VISIBLE);
            emptyDtxv.setText("Loading ..");*/
            if (isValid(mPastSessions)) {
                pastsessions_list = mPastSessions.getResultsList();
                if (isValid(pastsessions_list)) {
                    //  final long total_count= mPastSessions.getTotal_count();
//                    emptyFlay.setVisibility(View.GONE);
                    adapter = new ListCustomerSessionAdapter(CustomerDetailActivity.this, pastsessions_list);
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
//                    emptyFlay.setVisibility(View.VISIBLE);

                   /* if(flag==0)
                        emptyDtxv.setText("Loading ..");
                    else
                        defSetText(emptyDtxv,"No items in your feed");*/

                }
            }
            else
            {
                adapter =new ListCustomerSessionAdapter(CustomerDetailActivity.this);
                pacifyrListRcv.setItemAnimator(new DefaultItemAnimator());
                pacifyrListRcv.setAdapter(adapter);
//                emptyFlay.setVisibility(View.VISIBLE);

               /* if(flag==0)
                    emptyDtxv.setText("Loading ..");
                else
                    defSetText(emptyDtxv,"No items in your feed");*/
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
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
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
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, custListUser.getName());
            titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
            backImv.setVisibility(View.VISIBLE);
            leftTxv.setVisibility(View.GONE);

            //backImv.setBackgroundResource(R.drawable.leftmen);
            backImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (restoreCall && isCallActive) {
                        Intent i = new Intent(CustomerDetailActivity.this, VideoActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                    }
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
           /* if (isValid(swipeLay)) {
                if (swipeLay.isRefreshing()) {
                    swipeLay.setRefreshing(false);

                }
            }*/
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

    @Override
    public void onBackPressed() {

        if (restoreCall) {
            Intent i = new Intent(CustomerDetailActivity.this, VideoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        }
        finish();
    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onDestroyActivity(String event) {
        try {
            if (isValid(event)) {
                if (event.equals(Constants.DestroySubScreenAfterEndCall))
                {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
