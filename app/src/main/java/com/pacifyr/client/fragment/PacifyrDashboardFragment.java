package com.pacifyr.client.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.CustomerListActivity;
import com.pacifyr.client.home.EarningsActivity;
import com.pacifyr.client.home.MissedandDeclinedActivity;
import com.pacifyr.client.home.RatingDetailActivity;
import com.pacifyr.client.home.ReviewListingActivity;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.ui.activity.DialogsActivity;
import com.pacifyr.client.quickblox.chat.utils.qb.QbDialogHolder;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelStatistics;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.pacifyr.client.adapter.CustomerListAdapter.custListUser;
import static com.pacifyr.client.network.NetworkService.lastSession;
import static com.pacifyr.client.network.NetworkService.modelPacifyrDashboard;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class PacifyrDashboardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static MUser[] mCustomerslist;


    //private RelativeLayout currentRatingRlay;
    private DeviceFitImageView starDimv;
    private MainTextView ratingCtxv;
    private MainTextView ratingSessionCtxv;
    private MainTextView ratingStarsCtxv;
    private RelativeLayout lastSessionRlay;
    private CircleImageView lastSessionDimv;
    private MainTextView lastSsnPacfyrCtxv, no_pacifyr_ctxv;
    //no_lastsession_ctxv;
    private LinearLayout sessionLlay;
    private DeviceFitImageView videoCamImg;
    private MainTextView lastSessionMinuteTxv;
    private MainTextView daysTxv;
    private RelativeLayout cirPrefPacifyr;
    private MainTextView sessionsTxv;
    private MainTextView earningHoursCtxv;
    private LinearLayout week3Llay;
    private MainTextView earnedCtxv;
    private RelativeLayout customersRlay;
    private LinearLayout llayTop;
    private CircleImageView customer1Dimav;
    private CircleImageView customer2Dimav;
    private LinearLayout llayBottom;
    private CircleImageView customer3Dimav;
    private DeviceFitImageView searchDimv;
    private MainTextView thisWeekCtxv;
    private MainTextView earingSessionsCtxv;
   // private Switch onlineSwitch;

    private DeviceFitImageView onlineSwitch;
    private MainTextView dateCtxv;
    private MainTextView earningMinutesCtxv;
    private LinearLayout ratingInnerLlay;
    private DeviceFitImageView search_dimv;

    private LinearLayout thisWeekRatingLlay;
    private LinearLayout ratingSessionLlay;
    private LinearLayout star5Llay;
    //private LinearLayout last_session_top_llay;
    private MainTextView zeroReviewCtxv;
    private LinearLayout last_session_top_llay;
    private MainTextView no_lastsession_ctxv;

    private MainTextView unreadChatCtxv;
    private MainTextView missedCallCtxv;

    private SwipeRefreshLayout swipeLayout;

    MUser prefUser1,prefUser2,prefUser3;

    PrefManager prefM;
    int itrator;
    private MainTextView lastSessionCostCtxv;

    public Integer unreadCount =0;

    private boolean isClickedOnChat=false;
    private int badgeCount=0;
    private String numberOfMissed;
    private DeviceFitImageView chatSwitch;

    public PacifyrDashboardFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        badgeCount = 0;

        prefM = PrefManager.getInstance(getContext());

        NetworkService.startActionPacifyrDashboard(getActivity());



        ShortcutBadger.applyCount(getActivity(), badgeCount); //for 1.1.4+
        // ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3

    }

    @Override
    public void onSetValues(View view) throws Exception {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pacifyr_dashboard,container,false);
        inintailzieView(view);
        prefM = PrefManager.getInstance(getContext());

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        search_dimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), CustomerListActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void inintailzieView(View view) {
         try {
             swipeLayout = view.findViewById(R.id.pacifyr_swipe_layout);
             onlineSwitch = view.findViewById(R.id.online_switch);
             chatSwitch = view.findViewById(R.id.chat_switch);
             search_dimv = view.findViewById(R.id.search_dimv);
             //currentRatingRlay = (RelativeLayout) view.findViewById(R.id.rating_inner_llay);
             ratingInnerLlay = view.findViewById(R.id.rating_inner_llay);
             starDimv = view.findViewById(R.id.star_dimv);
             zeroReviewCtxv = view.findViewById(R.id.zero_review_ctxv);
             ratingCtxv = view.findViewById(R.id.rating_ctxv);
             ratingSessionCtxv = view.findViewById(R.id.rating_session_ctxv);
             ratingStarsCtxv = view.findViewById(R.id.rating_stars_ctxv);
             lastSessionRlay = view.findViewById(R.id.last_session_rlay);
             lastSessionDimv = view.findViewById(R.id.last_session_dimv);
             lastSsnPacfyrCtxv = view.findViewById(R.id.lastSsn_pacfyr_ctxv);
             sessionLlay = view.findViewById(R.id.session_llay);
             videoCamImg = view.findViewById(R.id.video_cam_img);
             lastSessionMinuteTxv = view.findViewById(R.id.last_session_minute_txv);
             lastSessionCostCtxv = view.findViewById(R.id.last_session_cost_txv);
             daysTxv = view.findViewById(R.id.days_txv);
             earingSessionsCtxv = view.findViewById(R.id.sessions_ctxv);
             cirPrefPacifyr = view.findViewById(R.id.cir_pref_pacifyr);
            // sessionsTxv = view.findViewById(R.id.sessions_txv);
             earningHoursCtxv = view.findViewById(R.id.earning_hours_ctxv);
             earningMinutesCtxv = view.findViewById(R.id.earning_minutes_ctxv);
             week3Llay = view.findViewById(R.id.week3_llay);
             earnedCtxv = view.findViewById(R.id.earned_ctxv);
             customersRlay = view.findViewById(R.id.customers_rlay);
             dateCtxv = view.findViewById(R.id.date_ctxv);
             llayTop = view.findViewById(R.id.llay_top);
             customer1Dimav = view.findViewById(R.id.customer1_dimv);
             customer2Dimav = view.findViewById(R.id.customer2_dimv);
             llayBottom = view.findViewById(R.id.llay_bottom);
             customer3Dimav = view.findViewById(R.id.customer3_dimv);
             searchDimv = view.findViewById(R.id.search_dimv);
             thisWeekCtxv = view.findViewById(R.id.this_week_ctxv);
             thisWeekRatingLlay = view.findViewById(R.id.this_week_rating_llay);
             ratingSessionLlay = view.findViewById(R.id.rating_session_llay);
             star5Llay = view.findViewById(R.id.stars5_llay);
             no_lastsession_ctxv = view.findViewById(R.id.no_lastsession_ctxv);
             last_session_top_llay = view.findViewById(R.id.last_session_top_llay);
             unreadChatCtxv = view.findViewById(R.id.unread_chat_ctxv);
             missedCallCtxv = view.findViewById(R.id.missed_call_ctxv);


             setOnlineViews();

             setChatViews();
           /*  PrefManager prefM = PrefManager.getInstance(getContext());
             String isOnline=prefM.getIsOnline();
             if(isOnline.equals("true"))
             {
                 onlineSwitch.setSelected(true);
             }else {
                 onlineSwitch.setSelected(false);
             }*/


            // LocalDate now = new LocalDate();
             Date date = new Date();
             DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
             String nowDate = df.format(date);
             dateCtxv.setText(nowDate);

             setValuesPacifyerViews();


             missedCallCtxv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     try {
                         Log.i("misssssssssd","m:");
                         MissedandDeclinedActivity.start(getActivity());
                         //startActivity(new Intent(getActivity(), MissedandDeclinedActivity.class));
                         /*getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment, new PacifyrSessionsFragment()).commit();
                         //showFragment(new PacifyrSessionsFragment());
                         MainTextView titleTxv = (MainTextView) getActivity().findViewById(R.id.title_txv);
                         Log.i("emooooooojiie","e:");
                         defSetText(titleTxv, "My Sessions");*/
                         //Toast.makeText(getContext(),"sessions rating",Toast.LENGTH_LONG).show();
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
             });


             unreadChatCtxv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     DialogsActivity.start(getActivity());
                     /*showFragment(new PacifyrMessageFragment(/));*/
                     //defSetText(titleTxv, "Messages");

                 }
             });

             onlineSwitch.setVisibility(View.VISIBLE);
             onlineSwitch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     try {
                         onlineSwitch.setClickable(false);
                         toggleOnline(onlineSwitch);

                         //editProfile();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });


             chatSwitch.setVisibility(View.VISIBLE);
             chatSwitch.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     try {
                         chatSwitch.setClickable(false);
                         toggleChat(chatSwitch);

                         //editProfile();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });

             customer1Dimav.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(isValid(prefUser1)) {
                         custListUser = prefUser1;
                         CustomerDetailActivity.start(getContext());
                     }

                 }
             });



             customer2Dimav.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(isValid(prefUser2)) {
                         custListUser = prefUser2;
                         CustomerDetailActivity.start(getContext());
                     }
                 }
             });

             customer3Dimav.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(isValid(prefUser3)) {
                         custListUser = prefUser3;
                         CustomerDetailActivity.start(getContext());
                     }
                 }
             });
            /* onlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     try {
                         // do something, the isChecked will be
                         // true if the switch is in the On position
                         if (isChecked) {
                             NetworkService.editProfileObject = new JSONObject();
                             NetworkService.editProfileObject.putOpt("isOnline", true);
                             NetworkService.webEditProfile(getContext());
                             PrefManager prefM = PrefManager.getInstance(getContext());
                             prefM.saveIsOnline("true");

                         } else {
                             NetworkService.editProfileObject = new JSONObject();
                             NetworkService.editProfileObject.putOpt("isOnline", false);
                             NetworkService.webEditProfile(getContext());
                             PrefManager prefM = PrefManager.getInstance(getContext());
                             prefM.saveIsOnline("false");
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });*/


             lastSessionRlay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     //  LastSessionActivity.start(getActivity());

                 }
             });
             cirPrefPacifyr.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     EarningsActivity.start(getActivity());

                 }
             });

             thisWeekRatingLlay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i= new Intent(getContext(),RatingDetailActivity.class);
                     i.putExtra("type","ratingDetail");
                     startActivity(i);
                     //Toast.makeText(getContext(),"this wee rating",Toast.LENGTH_LONG).show();
                 }
             });
             ratingSessionLlay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //hideRightLayActionbar();
                     try {
                         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pacifyr_content_fragment, new PacifyrSessionsFragment()).commit();
                         //showFragment(new PacifyrSessionsFragment());
                         MainTextView titleTxv = getActivity().findViewById(R.id.title_txv);
                         Log.i("emooooooojiif","e:");
                         defSetText(titleTxv, "My Sessions");
                         //Toast.makeText(getContext(),"sessions rating",Toast.LENGTH_LONG).show();
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }
             });
             star5Llay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Intent i= new Intent(getContext(),RatingDetailActivity.class);
                     i.putExtra("type","fiveStar");
                     startActivity(i);
                     //Toast.makeText(getContext(),"5 rating",Toast.LENGTH_LONG).show();
                 }
             });
                     ratingInnerLlay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   //  RatingDetailActivity.start(getContext());
                 }
             });

         }catch (Exception e)
         {
             e.printStackTrace();
         }

    }

    @Override
    public void onResume() {
        super.onResume();
        handleActionPacifyrDashboard();
        ShortcutBadger.applyCount(getActivity(), badgeCount);

       /* pref = getActivity().getSharedPreferences("mybadgeValue",Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putInt("badgeValue", badgeCount);
        edt.commit();*/

        prefM.saveBadgeValue(badgeCount);
    }

    private void handleActionPacifyrDashboard(){

        prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"dashboard/pacifyr/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    hideProgress();
                    modelPacifyrDashboard= GsonUtils.getInstance().gsonToModelPacifyrDashboard(json);
                    setValuesPacifyerViews();
                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            ex.printStackTrace();
        }
    }

    private void setOnlineViews() {
        try{
            String isOnline=prefM.getIsOnline();
            if(isOnline.equals("true"))
            {
                onlineSwitch.setSelected(true);
            }else {
                onlineSwitch.setSelected(false);
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void setChatViews() {
        try{
            String isOnline=prefM.getIsChatSubscriptionRequired();
            if(isOnline.equals("true"))
            {
                chatSwitch.setSelected(true);
            }else {
                chatSwitch.setSelected(false);
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void toggleChat(View view) {

        try{

            if (!view.isSelected()) {

                NetworkService.editProfileObject = new JSONObject();
                NetworkService.editProfileObject.putOpt("isChatSubscriptionRequired", true);
                editProfileWeb("Chat enabled Now");
                prefM.saveIsChatSubscriptionRequired("true");

            } else {

                NetworkService.editProfileObject = new JSONObject();
                NetworkService.editProfileObject.putOpt("isChatSubscriptionRequired", false);
                editProfileWeb("Chat disabled now ");
                prefM.saveIsChatSubscriptionRequired("false");
            }
            setChatViews();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void toggleOnline(View view) {

        try{

            if (!view.isSelected()) {


                NetworkService.editProfileObject = new JSONObject();
                NetworkService.editProfileObject.putOpt("isOnline", true);

                editProfileWeb("Online Now");
                prefM.saveIsOnline("true");

            } else {

                NetworkService.editProfileObject = new JSONObject();
                NetworkService.editProfileObject.putOpt("isOnline", false);
                editProfileWeb("Offline now ");
                prefM.saveIsOnline("false");
            }
         setOnlineViews();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void editProfileWeb(final String string) {
        try {
            setLoading();
            showProgress();
            PrefManager prefM = PrefManager.getInstance(getActivity());
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();
            if (isValid(NetworkService.editProfileObject)) {
                String url = NetworkService.GLOBAL_URL + "user/editprofile/" + userid;
                com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            hideProgress();
                            onlineSwitch.setClickable(true);
                            chatSwitch.setClickable(true);
                            if (isValid(result)) {
                                final JSONObject jsonObject = new JSONObject(result.toString());
                                String js = result.toString();
                                String phone = jsonObject.getString("phoneNumber");
                                if (isValid(phone)) {
                                    Toast.makeText(getActivity(), ""+string, Toast.LENGTH_SHORT).show();
                                } else {
                                   // Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception eee) {
                            //hideProgress();
                            Toast.makeText(getActivity(), "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_SHORT).show();
                            eee.printStackTrace();
                        }
                    }
                };
                String json = NetworkService.editProfileObject.toString();
                Ion.with(getActivity()).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setValuesPacifyerViews() {
        try {
            hideProgress();
            if (isValid(modelPacifyrDashboard)) {

                try {
                    if(isValid(modelPacifyrDashboard.getCustomers())) {

                        mCustomerslist = modelPacifyrDashboard.getCustomers();

                        int okFlag = 0;
                        for (itrator = 0; itrator < mCustomerslist.length; itrator++) {
                            String imgUrl = mCustomerslist[itrator].getImageUrl();

                            if (isValid(imgUrl)) {
                                if (okFlag == 0) {
                                    customer1Dimav.setVisibility(View.VISIBLE);
                                    Picasso.with(getContext()).load(imgUrl).into(customer1Dimav);
                                    prefUser1 = mCustomerslist[itrator];
                                    okFlag++;
                                    continue;
                                }
                                if (okFlag == 1) {
                                    customer2Dimav.setVisibility(View.VISIBLE);
                                    prefUser2 = mCustomerslist[itrator];
                                    Picasso.with(getContext()).load(imgUrl).into(customer2Dimav);
                                    okFlag++;
                                    continue;
                                }
                                if (okFlag == 2) {
                                    prefUser3 = mCustomerslist[itrator];
                                    customer3Dimav.setVisibility(View.VISIBLE);
                                    Picasso.with(getContext()).load(imgUrl).into(customer3Dimav);
                                    okFlag++;
                                    break;
                                }
                            }
                        }
                    }
                }
                catch (Exception exc){
                    exc.printStackTrace();
                }

                Collection<QBChatDialog> dialogList = (QbDialogHolder.getInstance().getDialogs().values());
                ArrayList<QBChatDialog> list = new ArrayList<>(dialogList);
                Set<String> dialogSet = new HashSet<String>();

                Iterator<QBChatDialog> iterator = dialogList.iterator();
                while (iterator.hasNext()) {
                    dialogSet.add(iterator.next().getDialogId());
                }

                QBRestChatService.getTotalUnreadMessagesCount(dialogSet,null).performAsync(new QBEntityCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer, Bundle bundle) {

                        unreadCount=integer;
                        //Toast.makeText(getApplicationContext(),Integer.toString(integer),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });


                badgeCount = 0;
                if (isValid(unreadCount)){
                    if(unreadCount>0)
                    {
                        unreadChatCtxv.setVisibility(View.VISIBLE);
                        unreadChatCtxv.setText(unreadCount + " unread messages");
                        badgeCount+=unreadCount;
                    }else {
                        isClickedOnChat=true;
                        unreadChatCtxv.setVisibility(View.GONE);
                    }
                }

                numberOfMissed = modelPacifyrDashboard.getUser().getNumberOfMissedCalls();
                int val = modelPacifyrDashboard.getUser().getNumberOfMissedCallsInt();
                if(val>0){
                    missedCallCtxv.setVisibility(View.VISIBLE);
                    missedCallCtxv.setText(numberOfMissed+ " missed/declined calls");
                    badgeCount+=val;
                }else {
                    missedCallCtxv.setVisibility(View.GONE);
                }

                /*pref = getActivity().getSharedPreferences("mybadgeValue",Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putInt("badgeValue", badgeCount);
                edt.commit();*/

                prefM.saveBadgeValue(badgeCount);



                try {
                    lastSession = modelPacifyrDashboard.getLastSession();
                    if (isValid(lastSession)) {
                        

                        last_session_top_llay.setVisibility(View.VISIBLE);
                        no_lastsession_ctxv.setVisibility(View.GONE);
                      /*  lastSessionDimv.setVisibility(View.VISIBLE);
                        sessionLlay.setVisibility(View.VISIBLE);
                        daysTxv.setVisibility(View.VISIBLE);*/
                        lastSsnPacfyrCtxv.setText(lastSession.getUser().getFirstName() + " " + lastSession.getUser().getLastName());
                        Picasso.with(getContext()).load( lastSession.getUser().getImageUrl()).into(lastSessionDimv);
                        lastSessionMinuteTxv.setText(lastSession.getDuration());
                        lastSessionCostCtxv.setText(", "+ Utility.getPriceSign(getApplicationContext())+lastSession.getPacifyrEarnings());
                        String endTime = lastSession.getEndTime();
                        daysTxv.setText(getDaysSincePosted(endTime));
                        String type = lastSession.getSessionType();
                        if (type.equals("audio")) {
                            videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.connectaudio1));
                        } else if (type.equals("video")) {
                            videoCamImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_video_cam_blue));
                        }
                        String duration = lastSession.getDuration();
                        String[] split = lastSession.getDuration().split(":");
                        // sessionsTxv.setText(lastWeek.getTotalSessions());
                    }
                    else
                    {
                        last_session_top_llay.setVisibility(View.GONE);
                        no_lastsession_ctxv.setVisibility(View.VISIBLE);
                     /*   daysTxv.setVisibility(View.INVISIBLE);
                        lastSessionDimv.setVisibility(View.INVISIBLE);
                        sessionLlay.setVisibility(View.GONE);
                        lastSsnPacfyrCtxv.setVisibility(View.VISIBLE);*/
                        defSetText(no_lastsession_ctxv,"No sessions yet");

                    }
                    /*if (isValid(lastSession)) {
                        String[] split = lastSession.getDuration().split(":");
                        // sessionsTxv.setText(lastWeek.getTotalSessions());
                        hoursTxv.setText(split[0]);
                        // minutesCtxv.setText(" "+split[1]);
                        //paidTxv.setText("$"+lastSession.getSessionCost());
                    }*/
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                try {

                    if (isValid(modelPacifyrDashboard.getRating())) {
                        if (isValid(modelPacifyrDashboard.getUser().getNumberOfReviews())) {
                            zeroReviewCtxv.setVisibility(View.VISIBLE);
                            zeroReviewCtxv.setText(modelPacifyrDashboard.getUser().getNumberOfReviews() + " reviews");

                            zeroReviewCtxv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ReviewListingActivity.start(getContext());
                                }
                            });
                        }
                        ratingCtxv.setText(modelPacifyrDashboard.getRating().getRating());
                        ratingSessionCtxv.setText(modelPacifyrDashboard.getRating().getNumberOfSessions());
                        ratingStarsCtxv.setText(modelPacifyrDashboard.getRating().getFiveStarRatingCount());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                /*if(isValid(modelPacifyrDashboard.getRating()))
                {
                    ratingCtxv.setText(modelPacifyrDashboard.getRating().getRating());
                    ratingSessionCtxv.setText(modelPacifyrDashboard.getRating().getNumberOfSessions());
                    ratingStarsCtxv.setText(modelPacifyrDashboard.getRating().getFiveStarRatingCount());
                }*/
                try {
                    ModelStatistics statistics = modelPacifyrDashboard.getTodayStatistics();
                    if (isValid(statistics)) {
                        String duration = statistics.getDuration();
                        String earing = Utility.getPriceSign(getContext()) + statistics.getEarnings();
                        earnedCtxv.setText(earing);
                        earingSessionsCtxv.setText(statistics.getTotalSessions());
                        //String hours = duration.substring(3,5);
                        //String mint = duration.substring(6,8);
                        String timestampStr = statistics.getDuration();
                        String[] tokens = timestampStr.split(":");
                        int hours = Integer.parseInt(tokens[0]);
                        int minutes = Integer.parseInt(tokens[1]);
                        int seconds = Integer.parseInt(tokens[2]);

                        earningMinutesCtxv.setText(String.valueOf(hours));
                        earningMinutesCtxv.setText(String.valueOf(minutes));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }



            }
            else
            {
                last_session_top_llay.setVisibility(View.GONE);
                no_lastsession_ctxv.setVisibility(View.VISIBLE);
                     /*   daysTxv.setVisibility(View.INVISIBLE);
                        lastSessionDimv.setVisibility(View.INVISIBLE);
                        sessionLlay.setVisibility(View.GONE);
                        lastSsnPacfyrCtxv.setVisibility(View.VISIBLE);*/
                defSetText(no_lastsession_ctxv,"No sessions yet");

            }
        } catch (Exception e) {

            e.printStackTrace();
            hideProgress();

        }
    }

    public String getDaysSincePosted(String endTime) {



        long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());

//      //  Log.e("ofsettt==", Calendar.getInstance().getTime().getTimezoneOffset() + "");


        System.out.println("time offset"+myTimeOffset);


        long day = 86400000;
        long hour = 3600000;
        long minute = 60000;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        String value = null;
        Calendar postedDay = Calendar.getInstance();
        try {
            date = df.parse(endTime);
            postedDay.setTime(date);


            long newtime = postedDay.getTimeInMillis() + myTimeOffset;

            postedDay.setTimeInMillis(newtime);

            //Log.e("Server Time=====>>", postedDay.getTime() + "");
            //Log.e("Current Time=====>>" , Calendar.getInstance().getTime()+ "");

            System.out.println("newtime....."+newtime);


            days = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / day;

            System.out.println("days...."+days);

            hours = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / hour;

            System.out.println("hours...."+hours+"   "+Calendar.getInstance().getTimeInMillis());

            minutes = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / minute;


            System.out.println("minutes...."+minutes);

            if (days == 1) {
                value = "Yesterday";

            } else if (days > 1) {
                value = days + " days ago";

            } else if (days == 0) {


                if (hours > 12) {
                    value = "Today";
                } else {


                    if (hours == 0) {

                        if(minutes<0)
                            minutes=0;

                        if (minutes == 0) {

                            value = "Moments ago";
                        } else {
                            value = minutes + " min ago";
                        }


                    } else {
                        value = hours + " hour ago";
                    }

                }

            }


            return value;

        } catch (Exception e){
            e.printStackTrace();
            e.printStackTrace();
            // TODO Auto-generated catch block
            e.printStackTrace();
            postedDay.setTimeInMillis(Long.valueOf(endTime));
            days = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / day;

            hours = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / hour;
            minutes = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / minute;

            if (days == 1) {
                value = "Yesterday";

            } else if (days > 1) {
                value = days + " days ago";

            } else if (hour == 0) {

                value = minutes + " min ago";
            } else if (minutes == 0) {

                value = "moments ago";
            } else if (days == 0) {


                if (hours > 12) {
                    value = "Today";
                } else {
                    value = hours + " hour ago";
                }
            }

            return value;

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }





    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {

        try {

            setValuesPacifyerViews();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
                setLoading();
                showProgress();
                handleActionPacifyrDashboard();
            }
        }, 2000);
    }
}
