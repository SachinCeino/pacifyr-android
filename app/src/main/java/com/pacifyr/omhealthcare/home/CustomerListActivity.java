package com.pacifyr.omhealthcare.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CustomerListAdapter;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.MCustomerList;
import com.pacifyr.omhealthcare.model.interfaces.CallChatEventListner;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.FlexLayout;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.pacifyr.omhealthcare.Fragment.PacifyrDashboardFragment.mCustomerslist;
import static com.pacifyr.omhealthcare.network.NetworkService.customerList;
import static com.pacifyr.omhealthcare.network.NetworkService.listCustomer1Paged;
import static com.pacifyr.omhealthcare.network.NetworkService.modelPacifyrDashboard;

public class CustomerListActivity extends PacifyrAppCompactActivity {

    RecyclerView customerRcv;

    private int lastVisibleItem, totalItemCount;
    private boolean isLoading=false;
    private boolean isFullLoaded=false;
    private int visibleThreshold = 2;
    int flag=0;
    CustomerListAdapter adapter;
    ArrayList<MCustomerList.Results> customerAlllist=new ArrayList<MCustomerList.Results>();

    private RecyclerView.Adapter CustomerListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeLay;
    private CustomFontTextView emptyDtxv;
    //private FlexLayout emptyFlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        NetworkService.startActionListCustomer(CustomerListActivity.this,0,20);

       // log.d("Hi , i am pottan");

        //emptyFlay = (Flux) findViewById(R.id.empty_flay);
        try {
            onSetActionBar();
            emptyDtxv = (CustomFontTextView) findViewById(R.id.empty_dtxv);

            swipeLay = (SwipeRefreshLayout) findViewById(R.id.swipe_lay);
            swipeLay.setColorSchemeResources(R.color.md_blue_300, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.md_amber_300);
            swipeLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLay.setRefreshing(true);
                    NetworkService.startActionListCustomer(CustomerListActivity.this, 0, 10);

                }
            });



            setAdapter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        try
        {
            LogUtils.LOGD("SAMSAD", "FF  setAdapter " );
            if (isValid(customerList)) {
                customerAlllist = customerList.getResultsList();
                if(isValid(customerAlllist)) {
                    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    customerRcv.setLayoutManager(layoutManager);
                    customerRcv.setHasFixedSize(true);
                    ArrayList<MCustomerList.Results> list = customerList.getResultsList();
                    ArrayList<MCustomerList.Results> deleteCandidates = new ArrayList<>();
                    ArrayList<MCustomerList.Results> myBeans = list;

                    // Pass 1 - collect delete candidates
                    for (MCustomerList.Results myBean : myBeans) {
                        /*if (shallBeDeleted(myBean)) {
                            deleteCandidates.add(myBean);
                        }*/
                        if(!isValid((myBean.getUser()))){
                            deleteCandidates.add(myBean);
                        }
                    }
                    // Pass 2 - delete
                    for (MCustomerList.Results deleteCandidate : deleteCandidates) {
                        myBeans.remove(deleteCandidate);
                    }


                    adapter = new CustomerListAdapter(CustomerListActivity.this,myBeans);
                    customerRcv.setAdapter(adapter);

                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) customerRcv.getLayoutManager();


                    customerRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            /*totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            int total_count =;
                            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) &&total_count>totalItemCount) {
                                isLoading = true;
                                //    feedlist.add(null);
                                // adapter.notifyItemInserted(feedlist.size() - 1);
                                int limit =(int) total_count - customerAlllist.size();
                                int skip = customerAlllist.size();
                                if(limit > 10) {
                                    limit = 10;
                                }

                            *//*    if(limit>0)
                                    NetworkService.startActionListCustomer(getActivity(), skip, limit);*//*
                                LogUtils.LOGD("jithish", "FF setAdapter skip =" +skip+"  limit ="+limit);
                                if(limit<=0) {
                                    isLoading = false;
                                    //hideLoading();
                                }
                            }*/
                        }
                    });
                }
                else
                {
                    adapter =new CustomerListAdapter(CustomerListActivity.this);
                    customerRcv.setItemAnimator(new DefaultItemAnimator());
                    customerRcv.setAdapter(adapter);
                    //emptyFlay.setVisibility(View.VISIBLE);
                    if(flag==0)
                        emptyDtxv.setText("Loading ..");
                    else
                        defSetText(emptyDtxv,"No items in your feed");

                }
            }
            else
            {
                adapter =new CustomerListAdapter(CustomerListActivity.this);
                customerRcv.setItemAnimator(new DefaultItemAnimator());
                customerRcv.setAdapter(adapter);
                //emptyFlay.setVisibility(View.VISIBLE);
                if(flag==0)
                    emptyDtxv.setText("Loading ..");
                else
                    defSetText(emptyDtxv,"No items in your feed");
            }
            flag++;
        }
        catch (Exception ex)
        {
            LogUtils.LOGD("exception", "FF  setAdapter " + ex.getMessage());
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }



    @Override
    public void onSetValues() throws Exception {

    }

   /* @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

    }*/
   @Subscribe
   public void onNotifyEvent(NotifyEvent event) {
       if(event.isSuccess)
       {
           if (isValid(swipeLay)) {
               if (swipeLay.isRefreshing()) {
                   swipeLay.setRefreshing(false);

               }
           }
           LogUtils.LOGD("SAMSAD LOG", "FF onNotifyEvent");
           if (event.isPaged) {
               if (isValid(listCustomer1Paged)) {
                   if (isValid(customerList))
                       if (isValidSize(customerAlllist))
                           if (isValid(customerRcv)) {
                               //hideLoading();
                               //  customerAlllist.remove(null);
                               ArrayList<MCustomerList.Results> list = listCustomer1Paged.getResultsList();
                               isLoading = false;
                               if (isValid(list)) {
                                   customerAlllist.addAll(list);
                                   adapter.notifyDataSetChanged();
                                   LogUtils.LOGD("layerutils", "MF customerlist paged list " + list.size());
                                   listCustomer1Paged.setResults(list);
                                   customerList.addResults(listCustomer1Paged.getResults());
                               } else {

                                   customerAlllist.addAll(listCustomer1Paged.getResultsList());

                                   //   _listView.setIsLoading(false);
                                   adapter.notifyDataSetChanged();
                                   customerList.addResults(listCustomer1Paged.getResults());
                               }

                           }
               }
               else
               {
                   //hideLoading();
                   //isBackendError=true;
                   adapter.notifyDataSetChanged();
               }
           } else {
               //isBackendError=false;
               setAdapter();
           }
       }
       //}

   }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

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

    private void onSetActionBar() {
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
            customerRcv = (RecyclerView) findViewById(R.id.customers_rcv);
            rightImv.setVisibility(View.INVISIBLE);
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            //leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, "my customers");
            backImv.setImageDrawable(getResources().getDrawable(R.drawable.backarrow));
            //backImv.setBackgroundResource(R.drawable.leftmen);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Subscribe
    public void onChatEvent(ChatEvent event) {
        try{
            LogUtils.LOGD("session","CPA onCallEvent called " );
            if(!isValid(event.mpacifyer,"Network Error"))
                return;
            startChat(event.mpacifyer);



        }catch (Exception e)
        {
            LogUtils.LOGD("session","CPA ChatEvent exce " + e.getMessage());
        }

    }
    public CallChatEventListner callchatEventListner =new CallChatEventListner() {
        @Override
        public void onCallEventClick(CallEvent event) {

            //onCallEvent(event);
        }

        @Override
        public void onChatEventClick(ChatEvent event) {
            onChatEvent(event);
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(String e) {

        }
    };

}
