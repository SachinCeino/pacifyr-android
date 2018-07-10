package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.model.pacifyr.ChatSubscription;
import com.ceino.utilitylibrary.model.pacifyr.Education;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.home.ConnectToSinglePacifyrActivity;
import com.pacifyr.omhealthcare.home.PaymentActivity;
import com.pacifyr.omhealthcare.home.ProfileScreenActivity;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class SwipeDeckBrowsePacifyerAdapter extends BaseAdapter {

    //private List<String> data;
   // public static List<RespSearchPacifyrWOption.Results> searchResults;
    public static List<MUser> searchResults;
    private Context context;

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

    private ArrayList<String> myList=new ArrayList();
    private ArrayList<ModelUserProfile.Certification> certificateList= new ArrayList<>();
    private ArrayList<ModelUserProfile.Education> educationsList= new ArrayList<>();
    private boolean isChatSubscriptionRequired;
    private int priceForChat;


    public SwipeDeckBrowsePacifyerAdapter(ArrayList<MUser> resultsList, Context context) {
        this.searchResults = resultsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        //return searchResults == null ? 0 : searchResults.size() * 20;
        //return Integer.MAX_VALUE;
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResults.get(position);
        /*int positionInList = position % searchResults.size();
        return searchResults.get(positionInList);*/
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

     //   final RespSearchPacifyrWOption.Results results= (RespSearchPacifyrWOption.Results) getItem(position);
        final MUser pacifyer= (MUser) getItem(position);
        prefM = PrefManager.getInstance(context);
        acessToken = prefM.getAccessToken();
        userID = prefM.getUserID();
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            // normally use a viewholder
            v = inflater.inflate(R.layout.test_card, parent, false);
        }
        if(isValid(searchResults)){
            imgURL = GLOBAL_IMAGE_URL + searchResults.get(position).getImage();
            namePacifyr= searchResults.get(position).getFirstName() + " " + searchResults.get(position).getLastName();
            pos=position;
            rating=searchResults.get(position).getRating();
            review=searchResults.get(position).getNumberOfReviews();
            website=searchResults.get(position).getWebsiteURL();
            education=searchResults.get(position).getEducation();
            specialiteis=searchResults.get(position).getSpecialities();
            //emotion=searchResults.get(position % searchResults.size()).getEmotion().toString();
            price=searchResults.get(position).getPrice();

            isChatSubscriptionRequired=searchResults.get(position).isChatSubscriptionRequired();
            priceForChat = Integer.parseInt(searchResults.get(position).getPriceForChat());

            LinearLayout chatLlay = v.findViewById(R.id.chat_llay);
            CustomFontTextView chat_price_ctxv = v.findViewById(R.id.chat_price_ctxv);

            chatLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //callChatSubScription(searchResults.get(position));
                    getSavedCardWebChat(searchResults.get(position));
                }
            });


            if(!isChatSubscriptionRequired)
            {
                chatLlay.setVisibility(View.GONE);

            }else if(priceForChat==0){
                chatLlay.setVisibility(View.GONE);
            }else{
                chatLlay.setVisibility(View.VISIBLE);

                if(searchResults.get(position).getChatSubscription()==null) {
                    chat_price_ctxv.setText("Chat for " + Utility.getPriceSign(context) + priceForChat + " a day");
                    chatLlay.setClickable(true);
                }else{
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

                        if (diffDays>0)
                        {
                            chat_price_ctxv.setText("Unlimited chat remainng for "+diffDays+" days");
                        }else if(diffHours>1){
                            chat_price_ctxv.setText("Unlimited chat remainng for "+diffHours+" hours and "+diffMinutes+" minutes");
                        }else if(diffMinutes>0){
                            chat_price_ctxv.setText("Unlimited chat remainng for "+diffMinutes+" minutes");
                        }
                        chatLlay.setClickable(false);

                        //chatPriceCtxv.setText("Unlimited chat remainng for ");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //userID=searchResults.get(position).getId();
            boolean isOnline=searchResults.get(position).getOnline();
            ImageView indicatorDimv=(ImageView) v.findViewById(R.id.indicator_dimv);
            CustomFontTextView descriptionCtxv =(CustomFontTextView) v.findViewById(R.id.description_ctxv);
            CustomFontTextView indicatorCtxv=(CustomFontTextView) v.findViewById(R.id.indicator_ctxv);


           /* RelativeLayout chat_rlay = (RelativeLayout) v.findViewById(R.id.chat_rlay);
            RelativeLayout audiocall_rlay = (RelativeLayout) v.findViewById(R.id.audiocall_rlay);
            RelativeLayout  videocall_rlay = (RelativeLayout)v. findViewById(R.id.videocall_rlay);
            */

            if(isOnline){
                indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                indicatorCtxv.setText("ONLINE");
                if(!searchResults.get(position).getCurrentlyAvailable())
                {
                    indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                    indicatorCtxv.setText("In-Session");
                }
            }
            else{
                indicatorDimv.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
                indicatorCtxv.setText("OFFLINE");
            }



            if (isValid(searchResults.get(position).getDescription()))
            {
                descriptionCtxv.setText(searchResults.get(position).getDescription());
            }

            TextView specialties=(TextView) v.findViewById(R.id.specialities_ctxv);
            if (isValid(searchResults.get(position).getSpecialities())) {
                String specl[] = searchResults.get(position).getSpecialities();
                specialties.setText(TextUtils.join(", ",specl));
            }

            prefM = PrefManager.getInstance(context);

            String customerRole = prefM.getCustomerRole();
            if(customerRole.equals("employee")) {
                if (!searchResults.get(position).isSessionChargeable()){
                    ((TextView) v.findViewById(R.id.price_dollar)).setText("Employer Designated Pacifyr");
                    /*((TextView) v.findViewById(R.id.perhour_ctxv)).setVisibility(View.GONE);*/
                }else {
                    ((TextView) v.findViewById(R.id.price_dollar)).setText(Utility.getPriceSign(context)+" " +price+ " per hour");
                }
            }else if(customerRole.equals("customer")){
                ((TextView) v.findViewById(R.id.price_dollar)).setText(Utility.getPriceSign(context)+" " +price+ " per hour");
                /*((TextView) v.findViewById(R.id.perhour_ctxv)).setVisibility(View.VISIBLE);*/
            }
            else if(customerRole.equals("paidMember")){
                ((TextView) v.findViewById(R.id.price_dollar)).setText("Employer Designated Pacifyr");
            }

            CustomFontTextView connectCtxv=(CustomFontTextView)v.findViewById(R.id.connect_pacifyr_ctxv);
            connectCtxv.setText("Connect to "+searchResults.get(position).getFirstName());

            ((TextView) v.findViewById(R.id.tv_name)).setText(namePacifyr);
            //((TextView) v.findViewById(R.id.angr_mgmt)).setText(emotion);

            ((RatingBar) v.findViewById(R.id.myRatingBar)).setRating(Float.valueOf(rating));
            ((TextView) v.findViewById(R.id.item_review_tv)).setText(review + "  Reviews");
            //ImageView img = (ImageView) v.findViewById(R.id.img_prof);
            //Picasso.with(context).load(imgUrl).into(img);
            CircleImageView img_prof = (CircleImageView) v.findViewById(R.id.img_prof);
            CardView cardView = (CardView) v.findViewById(R.id.card_pacifyr);

            if (isValidhtml(imgURL))
                Picasso.with(context).load(imgURL).placeholder(R.drawable.placeholder_user).into(img_prof);

            img_prof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileScreenActivity.start(context, searchResults.get(position).getId(),false);
                   /* Intent i = new Intent(context, ProfileScreenActivity.class);
                    i.putExtra("userid", searchResults.get(position).getId());
                    context.startActivity(i);*/
                }
            });

            connectCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(pacifyer.isReadyToCall())
                     ConnectToSinglePacifyrActivity.start(context,searchResults.get(position));
                    else
                    {
                        Snackbar offlineSnackbar = Snackbar.make(v, pacifyer.getName() + " is currently offline. You can either chat with " + pacifyer.getName() + " or try agein later", Snackbar.LENGTH_SHORT);
                        offlineSnackbar.show();
                        //ToastHandler.newInstance(context).mustShowToast(pacifyer.getName()+" is currently offline. You can either chat with "+pacifyer.getName()+" or try agein later");
                    }


                }
            });
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
            });*/
           /* img_video2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoChatActivity.  opponent=results;
                    showQuickblox();
                }
            });*/
            //v.findViewById(R.id.image_test).setBackgroundResource(R.drawable.imageadd);


        }

        return v;
    }

    /*public void showQuickblox() {
        Intent i=new Intent(context, VideoChatActivity.class);
        context. startActivity(i);
    }*/

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }


    public Boolean isValidhtml(String text) {
        if(isValid(text)) if(text.contains("http")||text.contains("https")) return true;
        return false;

    }

    /*private void callChatSubScription(String pacifyrID) {
        try{
            String url = GLOBAL_URL+"payment/chargeChat";
            prefM = PrefManager.getInstance(context);
            String token = prefM.getAccessToken();
            *//*String userid = prefM.getUserID();*//*
            //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        String jsonS = json.toString();
                        jsonS+="sam";


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
            jsonObject.putOpt("pacifyrId", pacifyrID);
            aq.post(url, jsonObject, JSONObject.class, cb);

           *//* {
                "userId": "594a1a4095f8d11ff498aa90",
                    "pacifyrId": "594a1a4095f8d11ff498aa90"
            }*//*

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    private void callChatSubScription(final MUser pacifyr) {
        try{
            String url = NetworkService.GLOBAL_URL+"payment/chargeChat";
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

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, iOSDialogClickListener listener)
    {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel,listener)
                .build().show();
    }
    public void getSavedCardWebChat( final MUser pacifyerDetial) {
        try {
            LogUtils.LOGD("session", "PA getSavedCardWeb " );
            final PrefManager prefM= PrefManager.getInstance(context);
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
            String url= NetworkService.GLOBAL_URL+"payment/savedcard/"+userID;
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
                        LogUtils.LOGD("session", "PA getSavedCardWeb callbaack "+json );
                        if(isValid(json)) {
                            String jsonString = json.toString();
                            int l=jsonString.length();
                            if(l>3) {
                                LogUtils.LOGD("session", "PA saveCardExist true " );
                                //saveCardExist(true);
                                prefM.saveCardExist(true);
                                callChatSubScription(pacifyerDetial);

                                //listner.onAvailable(NetworkService.mCurrentUser,pacifyerDetial,isVideoCall);
                            }
                            else{
                                //saveCardExist(false);
                                prefM.saveCardExist(false);
                                showIOSDialogOK("","A payment method needs to be set up.","Set up Payment",listener);
                            }
                        }else{
                            /*saveCardExist(false);*/
                            prefM.saveCardExist(false);
                            showIOSDialogOK("","A payment method needs to be set up.","Set up Payment",listener);
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
            cb.header("AccessToken",acessToken);
            aq.ajax(url,JSONArray.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            //listner.onException(ex);
            showIOSDialogOK("","Please try again","OK");
            LogUtils.LOGD("session","PA getSavedCardWeb exce " + ex.getMessage());
        }
    }


    public KProgressHUD hud_progress = null;
    // public KProgressHUD hud_status = null;
    public void setLoading() {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void setLoading(String message) {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void showProgress() {
        if(hud_progress != null) if(!hud_progress.isShowing()) hud_progress.show();
    }

    public void hideProgress() {
        if(hud_progress != null) if(hud_progress.isShowing()) hud_progress.dismiss();
    }
    public void setLoadingWait() {
        hideProgress();
        if(isValid(hud_progress))
            hud_progress=null;
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel)
    {
        new iOSDialogBuilder(context)
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel,new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        //Toast.makeText(PacifyerActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                })
                .build().show();
    }

}
