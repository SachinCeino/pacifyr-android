package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.pacifyr.client.home.ConnectToSinglePacifyrActivity;;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ChatSubscription;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Education;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.home.DoctorsMoreInformationActivity;
import com.pacifyr.client.home.DoctorsSearchResultActivity;
import com.pacifyr.client.home.PaymentActivity;
import com.pacifyr.client.home.ProfileScreenActivity;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.db.QbUsersDbManager;
import com.pacifyr.client.quickblox.services.CallService;
import com.pacifyr.client.quickblox.utils.PermissionsChecker;
import com.pacifyr.client.quickblox.utils.WebRtcSessionManager;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.quickblox.chat.QBChatService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;


public class PacifyrResultListViewAdapter extends RecyclerView.Adapter<PacifyrResultListViewAdapter.MyViewHolder> {


    public List<MUser> searchResults;
    private Activity context;

    public String imgURL;
    public String namePacifyr;
    public int pos;
    public String rating;
    public String review;
    public String userID;
    public String acessToken;
    public String website;
    public Education[] education;
    public String[] specialiteis;
    private String emotion;
    private String price;

    private PrefManager prefM;

    private QBUser currentUser;
    private PermissionsChecker checker;
    private QBChatService chatService;
    private QbUsersDbManager dbManager;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    //public Long qbID;

    public SharedPrefsHelper sharedPrefsHelper;

    private ArrayList<String> myList = new ArrayList();
    private ArrayList<ModelUserProfile.Certification> certificateList = new ArrayList<>();
    private ArrayList<ModelUserProfile.Education> educationsList = new ArrayList<>();
    Activity activity;
    private boolean isChatSubscriptionRequired;
    private int priceForChat;

    MainTextView chatPriceCtxv;
    private LinearLayout chatLlay;
    private RelativeLayout schedule_rlay;

    public interface OnFavoriteCliked
    {
        void onAddtofavoriteint(int position,MUser pacifyer);
    }

    private  OnFavoriteCliked monFavoriteCliked;

    public PacifyrResultListViewAdapter(ArrayList<MUser> resultsList, Activity context,OnFavoriteCliked monFavoriteCliked) {
        this.searchResults = resultsList;
        this.context = context;
        this.monFavoriteCliked=monFavoriteCliked;
    }


    @NonNull
    @Override
    public PacifyrResultListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.adapter_item_listview_pacifyrs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PacifyrResultListViewAdapter.MyViewHolder holder, int position) {

        try {
            MUser pacifyer = searchResults.get(position);
            if (isValid(pacifyer)) {

                imgURL = GLOBAL_IMAGE_URL + pacifyer.getImage();
                namePacifyr = pacifyer.getFirstName() + " " + pacifyer.getLastName();
                rating = pacifyer.getRating();
                review = pacifyer.getNumberOfReviews();
                website = pacifyer.getWebsiteURL();
                education = pacifyer.getEducation();
                specialiteis = pacifyer.getSpecialities();
                price = pacifyer.getPrice();

                holder.index_count.setText(String.valueOf(position + 1) + ".");
                if (isValid(pacifyer.getDescription())) {
                    holder.pacifyr_description.setText(pacifyer.getDescription());
                }
                if (isValid(namePacifyr)) {
                    holder.pacifyr_name.setText(namePacifyr);
                }
                if (isValid(namePacifyr)) {
                    holder.pacifyr_Price.setText(pacifyer.getPriceWithSign());
                }
                if (isValidhtml(imgURL))
                    Picasso.with(context).load(imgURL).placeholder(R.drawable.placeholder_user).into(holder.pacifyrImage);

                if (isValid(pacifyer.getAddress1()))
                holder.pacifyr_addresss.setText(pacifyer.getAddress1()+"");

                holder.pacifyr_timing.setText("Hours 10am-12am");

                holder.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, DoctorsMoreInformationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Object", pacifyer);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                holder.requestBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((DoctorsSearchResultActivity) context).showScheduleDialogueFragment(pacifyer);
                    }
                });

                holder.mTextView_connect_pacifyr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ConnectToSinglePacifyrActivity.start(context, pacifyer);
                    }
                });

                if (pacifyer.isFavorite())
                {
                    holder.favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.svg_favorite_doctor));
                }
                else {
                    holder.favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.svg_unfavorite_doctor));
                }
                holder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monFavoriteCliked.onAddtofavoriteint(position,pacifyer);
                    }
                });

            }
        } catch (Exception e) {
            Log.w("EEEEXXX", e.toString());
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView index_count;
        public CircleImageView pacifyrImage;
        public ImageView favorite;
        public TextView pacifyr_name;
        public TextView pacifyr_description;
        public TextView pacifyr_addresss;
        public TextView pacifyr_timing;
        public TextView pacifyr_Price;
        public TextView more_info;
        public LinearLayout requestBook;
        public TextView mTextView_connect_pacifyr;


        public MyViewHolder(View view) {
            super(view);
            index_count = view.findViewById(R.id.index_count);
            pacifyrImage = view.findViewById(R.id.pacifyrImage);
            favorite = view.findViewById(R.id.favorite);
            pacifyr_name = view.findViewById(R.id.pacifyr_name);
            pacifyr_description = view.findViewById(R.id.pacifyr_description);
            pacifyr_addresss = view.findViewById(R.id.pacifyr_addresss);
            pacifyr_timing = view.findViewById(R.id.pacifyr_timing);
            pacifyr_Price = view.findViewById(R.id.pacifyr_Price);
            pacifyr_addresss = view.findViewById(R.id.pacifyr_addresss);
            more_info = view.findViewById(R.id.more_info);
            requestBook = view.findViewById(R.id.requestBook);
            mTextView_connect_pacifyr = view.findViewById(R.id.connect_pacifyr);


        }

    }


/*
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        sharedPrefsHelper = SharedPrefsHelper.getInstance();


        final MUser pacifyer = (MUser) getItem(position);
        prefM = PrefManager.getInstance(context);
        acessToken = prefM.getAccessToken();
        userID = prefM.getUserID();
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            // normally use a viewholder


            if (isValid(searchResults)) {

                //v = inflater.inflate(R.layout.swipecard_connect, parent, false);
                v = inflater.inflate(R.layout.swipe_card_cons, parent, false);
                chatPriceCtxv = v.findViewById(R.id.chat_price_ctxv);
                //chatLlay = (LinearLayout) v.findViewById(R.id.chat_llay);

                schedule_rlay = v.findViewById(R.id.schedule_rlay);

                imgURL = GLOBAL_IMAGE_URL + searchResults.get(position).getImage();
                namePacifyr = searchResults.get(position).getFirstName() + " " + searchResults.get(position).getLastName();
                pos = position;
                rating = searchResults.get(position).getRating();
                review = searchResults.get(position).getNumberOfReviews();
                website = searchResults.get(position).getWebsiteURL();
                education = searchResults.get(position).getEducation();
                specialiteis = searchResults.get(position).getSpecialities();
                //emotion=searchResults.get(position % searchResults.size()).getEmotion().toString();
                price = searchResults.get(position).getPrice();

                //userID=searchResults.get(position).getId();
                boolean isOnline = searchResults.get(position).getOnline();
                ImageView indicatorDimv = v.findViewById(R.id.indicator_dimv);
                MainTextView descriptionCtxv = v.findViewById(R.id.description_ctxv);
                MainTextView indicatorCtxv = v.findViewById(R.id.indicator_ctxv);
                indicatorCtxv.setTextSize(7f);
                if (isOnline) {
                    indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                    indicatorCtxv.setText("ONLINE");

                    if (!searchResults.get(position).getCurrentlyAvailable()) {
                        indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                        indicatorCtxv.setText("In-Session");
                    }
                } else {
                    indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
                    indicatorCtxv.setText("OFFLINE");
                }
                if (isValid(searchResults.get(position).getDescription())) {
                    descriptionCtxv.setVisibility(View.VISIBLE);
                    descriptionCtxv.setText(searchResults.get(position).getDescription());
                } else {
                    descriptionCtxv.setVisibility(View.GONE);
                }

                TextView specialties = v.findViewById(R.id.specialities_ctxv);
                if (isValid(searchResults.get(position).getSpecialities())) {
                    String specl[] = searchResults.get(position).getSpecialities();
                    specialties.setText(TextUtils.join(", ", specl));
                }

                prefM = PrefManager.getInstance(context);

                String customerRole = prefM.getCustomerRole();
                if (customerRole.equals("employee")) {
                    if (!searchResults.get(position).isSessionChargeable()) {
                        // ((TextView) v.findViewById(R.id.price_dollar)).setText("Employer Designated Pacifyr");
                        ((TextView) v.findViewById(R.id.price_dollar)).setText("");
                        */
    /*((TextView) v.findViewById(R.id.perhour_ctxv)).setVisibility(View.GONE);*//*

                    } else {
                        ((TextView) v.findViewById(R.id.price_dollar)).setText(Utility.getPriceSign(context) + " " + price + " per hour");
                    }
                } else if (customerRole.equals("customer") || customerRole.equals("unpaidEmployee")) {
                    ((TextView) v.findViewById(R.id.price_dollar)).setText(Utility.getPriceSign(context) + " " + price + " per hour");
                    */
    /*((TextView) v.findViewById(R.id.perhour_ctxv)).setVisibility(View.VISIBLE);*//*

                } else if (customerRole.equals("paidMember")) {
                    //((TextView) v.findViewById(R.id.price_dollar)).setText("Employer Designated Pacifyr");
                    ((TextView) v.findViewById(R.id.price_dollar)).setText("");
                }

                chatSubsFun(position);

                //MainTextView connectCtxv=(MainTextView)v.findViewById(R.id.connect_pacifyr_ctxv);
                //connectCtxv.setText("Connect to "+searchResults.get(position).getFirstName());

                ((TextView) v.findViewById(R.id.tv_name)).setText(namePacifyr);
                //((TextView) v.findViewById(R.id.angr_mgmt)).setText(emotion);
                ((RatingBar) v.findViewById(R.id.myRatingBar)).setRating(Float.valueOf(rating));
                ((TextView) v.findViewById(R.id.item_review_tv)).setText(review + "  Reviews");

                RelativeLayout chat_rlay = v.findViewById(R.id.chat_rlay);
                RelativeLayout audiocall_rlay = v.findViewById(R.id.audiocall_rlay);
                RelativeLayout videocall_rlay = v.findViewById(R.id.videocall_rlay);
                chat_rlay.setVisibility(View.VISIBLE);

            */
/*if(pacifyer.isChatEnabled())
                chat_rlay.setVisibility(View.VISIBLE);*//*

                videocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(context,"Video call",Toast.LENGTH_SHORT).show();
                        if (pacifyer.isReadyToCall()) {
                            showProgress();
                            ((ConnectToPacifyrActivity) (activity)).callchatEventListner.onCallEventClick((new CallEvent(searchResults.get(position), true)));
                            // ConnectToPacifyrActivity.callchatEventListner.onCallEventClick(new CallEvent(searchResults.get(position), true));
                            //  postEventOnMainThread(new CallEvent(searchResults.get(position), true));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            }, 1000);

                        } else {
                            Snackbar offlineSnackbar = Snackbar.make(v, pacifyer.getName() + " is currently offline. You can either chat with " + pacifyer.getName() + " or try again later", Snackbar.LENGTH_LONG);
                            offlineSnackbar.show();
                            //ToastHandler.newInstance(context).mustShowToast(pacifyer.getName() + " is currently offline. You can either chat with " + pacifyer.getName() + " or try agein later");
                            //ToastHandler.newInstance(context).mustShowToast("Please contact later. Pacifyer not available now");
                        }


                 */
/*   if (isLoggedInChat()) {

                        callRequestWeb(getUserID(),listner,true,qbID);

                        //startCall(true,qbID);
                    }
                    if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                        startPermissionsActivity(false);
                    }*//*

                    }
                });

                audiocall_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (pacifyer.isReadyToCall()) {
                            showProgress();

                            ((ConnectToPacifyrActivity) (activity)).callchatEventListner.onCallEventClick((new CallEvent(searchResults.get(position), false)));
                            //    ConnectToPacifyrActivity.callchatEventListner.onCallEventClick((new CallEvent(searchResults.get(position), false)));
                            // postEventOnMainThread(new CallEvent(searchResults.get(position), false));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            }, 1000);

                        } else {
                            Snackbar offlineSnackbar = Snackbar.make(v, pacifyer.getName() + " is currently offline. You can either chat with " + pacifyer.getName() + " or try again later", Snackbar.LENGTH_SHORT);
                            offlineSnackbar.show();
                            //ToastHandler.newInstance(context).mustShowToast(pacifyer.getName() + " is currently offline. You can either chat with " + pacifyer.getName() + " or try agein later");
                            //ToastHandler.newInstance(context).mustShowToast("Please contact later. Pacifyer not available now");
                        }

                        //  Toast.makeText(context,"Video call",Toast.LENGTH_SHORT).show();

                 */
/*   if (isLoggedInChat()) {
                        callRequestWeb(userID,listner,false,qbID);
                        // startSessionWeb(pacifyerDetial,false,qbID);
                        //   startCall(false,qbID);
                    }
                    if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                        startPermissionsActivity(true);
                    }*//*


                    }
                });

                schedule_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ConnectToPacifyrActivity) context).showScheduleDialogueFragment(pacifyer);
                    }
                });


                chat_rlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isChatSubscriptionRequired = searchResults.get(position).isChatSubscriptionRequired();
                        try {
                            priceForChat = Integer.parseInt(searchResults.get(position).getPriceForChat());
                        } catch (NumberFormatException ne) {
                            ne.printStackTrace();
                        }


                        if (!isChatSubscriptionRequired) {
                            showProgress();
                            ((ConnectToPacifyrActivity) (activity)).callchatEventListner.onChatEventClick((new ChatEvent(searchResults.get(position), false)));
                            //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                            // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            }, 500);
                            //showIOSDialogOK("","Please purchase unlimited chat per day","OK");

                        } else if (priceForChat == 0) {
                            showProgress();
                            ((ConnectToPacifyrActivity) (activity)).callchatEventListner.onChatEventClick((new ChatEvent(searchResults.get(position), false)));
                            //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                            // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            }, 500);
                        } else {
                            //chatLlay.setVisibility(View.VISIBLE);

                            if (searchResults.get(position).getChatSubscription() == null) {
                                showIOSDialogOK("", "Please purchase unlimited chat per day", "OK");
                                //chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(ProfileScreenActivity.this) + priceForChat + " a day");
                            } else {
                                showProgress();
                                ((ConnectToPacifyrActivity) (activity)).callchatEventListner.onChatEventClick((new ChatEvent(searchResults.get(position), false)));
                                //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                                // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgress();
                                    }
                                }, 500);
                            }
                        }

                    */
/*showProgress();
                    ( (ConnectToPacifyrActivity) (activity)).callchatEventListner.onChatEventClick((new ChatEvent(searchResults.get(position), false)));
                //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                   // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                        }
                    },500);*//*

                        //   if(pacifyer.isReadyToCall())

                    */
/*else
                    {
                        ToastHandler.newInstance(context).mustShowToast("Please contact later. Pacifyer not available now");
                    }*//*



                    }
                });


                //ImageView img = (ImageView) v.findViewById(R.id.img_prof);
                //Picasso.with(context).load(imgUrl).into(img);
                CircleImageView img_prof = v.findViewById(R.id.img_prof);
                CardView cardView = v.findViewById(R.id.card_pacifyr);

                if (isValidhtml(imgURL))
                    Picasso.with(context).load(imgURL).placeholder(R.drawable.placeholder_user).into(img_prof);

                img_prof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProfileScreenActivity.start(context, searchResults.get(position).getId(), true);
                 */
/*   Intent i = new Intent(context, ProfileScreenActivity.class);
                    i.putExtra("userid", searchResults.get(position).getId());
                    context.startActivity(i);*//*

                    }
                });

           */
/* connectCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ConnectToPacifyrActivity.class);
                    i.putExtra("display",searchResults.get(position).getFirstName());
                    context.startActivity(i);
                }
            });*//*

     */
/*img_prof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("imageURL", imgURL);
                    i.putExtra("name", namePacifyr);
                    i.putExtra("position", pos);
                    i.putExtra("rating", rating);
                    i.putExtra("review", review);
                    i.putExtra("website", website);
                    i.putExtra("education", education);
                    i.putExtra("specialities", specialiteis);
                    context.startActivity(i);
                }
            });*//*

     */
/* img_video2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoChatActivity.  opponent=results;
                    showQuickblox();
                }
            });*//*

                //v.findViewById(R.id.image_test).setBackgroundResource(R.drawable.imageadd);


            }
        }

        return v;
    }
*/

    private void chatSubsFun(final int position) {
        isChatSubscriptionRequired = searchResults.get(position).isChatSubscriptionRequired();
        try {
            priceForChat = Integer.parseInt(searchResults.get(position).getPriceForChat());
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
        }

        //chatLlay
        chatPriceCtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callChatSubScription(searchResults.get(position));
                getSavedCardWebChat(searchResults.get(position));
            }
        });

        if (!isChatSubscriptionRequired) {
            //chatLlay
            chatPriceCtxv.setVisibility(View.GONE);

        } else if (priceForChat == 0) {
            //chatLlay
            chatPriceCtxv.setVisibility(View.GONE);
        } else {
            //chatLlay
            chatPriceCtxv.setVisibility(View.VISIBLE);

            if (searchResults.get(position).getChatSubscription() == null) {
                chatPriceCtxv.setText("Chat for " + Utility.getPriceSign(context) + priceForChat + " a day");
                //chatLlay
                chatPriceCtxv.setClickable(true);
            } else {
                try {
                    ChatSubscription subscription = searchResults.get(position).getChatSubscription();
                    String expiryDate = subscription.getExpiryDate();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                    Date parsedTime = sdf.parse(expiryDate);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                    Date date = new Date();
                    System.out.println(dateFormat.format(date));

                    long diff = parsedTime.getTime() - date.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    if (diffDays > 0) {
                        chatPriceCtxv.setText("Unlimited chat remaining for " + diffDays + " days");
                    } else if (diffHours > 1) {
                        chatPriceCtxv.setText("Unlimited chat remaining for " + diffHours + " hours and " + diffMinutes + " minutes");
                    } else if (diffMinutes > 0) {
                        chatPriceCtxv.setText("Unlimited chat remaining for " + diffMinutes + " minutes");
                    }
                    //chatLlay
                    chatPriceCtxv.setClickable(false);

                    //chatPriceCtxv.setText("Unlimited chat remainng for ");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel) {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel, new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyrActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                })
                .build().show();
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, iOSDialogClickListener listener) {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel, listener)
                .build().show();
    }

    public void getSavedCardWebChat(final MUser pacifyerDetial) {
        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb ");
            final PrefManager prefM = PrefManager.getInstance(context);
           /* if (!isValidLogout(getUserID(),"Session expired, please re login")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }

            if (!isValid(getAccessToken(),"Stop Session getAccessToken null")) {
                handlerLogoutFinish("Session expired, please login");
                return;
            }*/
            setLoadingWait();
            showProgress();
            String url = NetworkService.GLOBAL_URL + "payment/savedcard/" + userID;
            AjaxCallback cb = new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Intent i = new Intent(context, PaymentActivity.class);
                                context.startActivity(i);
                                dialog.dismiss();
                            }
                        };
                        hideProgress();
                        LogUtils.LOGD("session", "PA getSavedCardWeb callbaack " + json);
                        if (isValid(json)) {
                            String jsonString = json.toString();
                            int l = jsonString.length();
                            if (l > 3) {
                                LogUtils.LOGD("session", "PA saveCardExist true ");
                                //saveCardExist(true);
                                prefM.saveCardExist(true);
                                callChatSubScription(pacifyerDetial);

                                //listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                            } else {
                                //saveCardExist(false);
                                prefM.saveCardExist(false);
                                showIOSDialogOK("", "A payment method needs to be set up.", "Set up Payment", listener);
                            }
                        } else {
                            /*saveCardExist(false);*/
                            prefM.saveCardExist(false);
                            showIOSDialogOK("", "A payment method needs to be set up.", "Set up Payment", listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //saveCardExist(false);
                        //showIOSDialogOK("","A payment method needs to be set up.","Set up Payment",listener);

                    }
                }
            };
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", acessToken);
            aq.ajax(url, JSONArray.class, cb);
        } catch (Exception ex) {
            hideProgress();
            //listner.onException(ex);
            showIOSDialogOK("", "Please try again", "OK");
            LogUtils.LOGD("session", "PA getSavedCardWeb exce " + ex.getMessage());
        }
    }


    private void callChatSubScription(final MUser pacifyr) {
        try {
            String url = NetworkService.GLOBAL_URL + "payment/chargeChat";
            PrefManager prefM = PrefManager.getInstance(context);
            String token = prefM.getAccessToken();
            /*String userid = prefM.getUserID();*/
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.getString("message");
                        json.getString("message");

                        //startChat(pacifyr);
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                //startChat(pacifyr);
                                dialog.dismiss();
                            }
                        };
                        showIOSDialogOK("", "Subscribed to Chat", "OK", listener);
                        //chatPriceCtxv.setText("Unlimited chat remianing for ");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", prefM.getUserID());
            jsonObject.putOpt("pacifyrId", pacifyr.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }

    public boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    public void tryReLoginToChat() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            CallService.start(context, qbUser);
        }
    }


    public Boolean isValid(Object object) {
        return object != null;

    }


    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http");
        return false;

    }

    public KProgressHUD hud_progress = null;

    // public KProgressHUD hud_status = null;
    public void setLoading() {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoading(String message) {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("" + message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void showProgress() {
        if (hud_progress != null) if (!hud_progress.isShowing()) hud_progress.show();
    }

    public void hideProgress() {
        if (hud_progress != null) if (hud_progress.isShowing()) hud_progress.dismiss();
    }

    public void setLoadingWait() {
        hideProgress();
        if (isValid(hud_progress))
            hud_progress = null;
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void clear() {
        if (searchResults != null) {
            searchResults.clear();
            notifyDataSetChanged();
        }
    }

    public void changeOfflineOnLine(List<MUser> resultsList) {

        if (isValid(resultsList) && isValid(searchResults)) {

            for (int i = 0; i < resultsList.size(); i++) {
                for (int j = 0; j < searchResults.size(); j++) {
                    if (resultsList.get(i).getId().equals(searchResults.get(j).getId())) {

                        if (resultsList.get(i).getOnline() != searchResults.get(j).getOnline()) {
                            searchResults.get(j).setOnline(resultsList.get(i).getOnline());
                            notifyDataSetChanged();
                            //notifyDataSetInvalidated();
                        }

                    }
                }

            }
        }

    }
}
