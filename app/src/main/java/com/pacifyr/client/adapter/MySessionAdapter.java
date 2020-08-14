package com.pacifyr.client.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.LastSessionActivity;
import com.pacifyr.client.home.MySessionsActivity;
import com.pacifyr.client.home.OfficeVisitSummary;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;

/**
 * Created by ceino on 14/12/17.
 */

public class MySessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<MSessionDetailRoot> list;
    Context context;
    ArrayList<MEmotion> emotionList;

    String userid,token;
    private boolean firstItemEditable = true;
    private int firstValidSessionPos = -1;


    public MySessionAdapter(Context context, ArrayList<MSessionDetailRoot> list,String userid,String token) {
        this.list = list;
        this.context=context;
        if (modelAllEmotions != null && modelAllEmotions.getResultsList() != null) {
            emotionList = modelAllEmotions.getResultsList();
        } else {
            emotionList = null;
        }
        this.userid = userid;
        this.token = token;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rcv=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mysession_item, parent, false);

        if (viewType==1) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_mysession_missed_item, parent, false);
            rcv = new MissedViewHolder(itemView);
        }else if(viewType==2){
            itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_event_empty, parent, false);
            rcv = new ViewHolderEmpty(itemView);
        } else if (viewType == 3) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_mysession_service_not_provided_item, parent, false);
            rcv = new ServiceNotProvided(itemView);
        } else if (viewType == 4) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_mysession_office_visit, parent, false);
            rcv = new OfficeVisit(itemView);
        } else {
            rcv = new MyViewHolder(itemView);
        }

        return rcv;
    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/

    public void setFirstItemEditable(boolean firstItemEditable) {
        this.firstItemEditable = firstItemEditable;
    }

    public MSessionDetailRoot getItem (int position) {
        MSessionDetailRoot results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder topholder, final int position) {
        if (topholder != null) {

            if (topholder instanceof  MyViewHolder) {
                MyViewHolder holder = ( MyViewHolder) topholder;

                final MSessionDetailRoot results=getItem(position);

                setViewholder(holder,results,position);
                //loadingViewHolder.progressBar.setIndeterminate(true);
            }else if (topholder instanceof  MissedViewHolder) {
                MissedViewHolder missedViewHolder = (MissedViewHolder) topholder;
                setViewholder(missedViewHolder,position);
            } else if (topholder instanceof ServiceNotProvided) {
                ServiceNotProvided serviceNotProvided = (ServiceNotProvided) topholder;
                setViewholder(serviceNotProvided, position);
            } else if (topholder instanceof OfficeVisit) {
                OfficeVisit officeVisit = (OfficeVisit) topholder;
                setViewholder(officeVisit, position);
            }

        }


    }

    public boolean isCustomer()
    {
        boolean isCustomer=false;
        try{

            try {

                String label= PrefManager.getInstance(context).getUserType();
                if(label.equals("customer")) {
                    isCustomer=true;
                }
                else if(label.equalsIgnoreCase("pacifyr"))
                    isCustomer=false;

            } catch (Exception t) {
                isCustomer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isCustomer=false;
        }
        return isCustomer;
    }


    public String  getUserType()
    {
        String label="customer";
        try{

            try {

                label= PrefManager.getInstance(context).getUserType();


            } catch (Exception t) {
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            label="customer";
        }
        return label;
    }

    private void setViewholder(MyViewHolder holder, final MSessionDetailRoot results, int position) {

        try {
            if (firstValidSessionPos == -1)
                firstValidSessionPos = position;
            String sessionType=results.getSessionType();
            MSessionRating[] rating = results.getSessionRatings();
            MSessionRating customerRatings = null;

            MUser pac = results.getPacifyr();
            String name = pac.getFirstName();
            String time = results.getStartTime();
            holder.nameUserCtxv.setText(results.getPacifyr().getFirstName());
            String imgUrl=results.getPacifyr().getImageUrl();

            if (context.getPackageName().equalsIgnoreCase("com.pacifyr.pam"))
            {
                holder.after_Layout.setVisibility(View.GONE);
                holder.before_Layout.setVisibility(View.GONE);
            }
            if(isValid(imgUrl)){
                Glide.with(context)
                        .load(imgUrl)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model,
                                                       Target<GlideDrawable> target, boolean isFirstResource) {
                                e.printStackTrace();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .dontTransform()
                        .into(holder.userCimv);
            }
            String callType = results.getSessionType();
            if(callType.equals("audio")) {
                holder.calltype_imv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (callType.equals("video")) {
                holder.calltype_imv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }
            //Picasso.with(context).load(imgUrl).into(holder.userCimv);

            holder.priceCtxv.setText(Utility.getPriceSign(context)+String.format("%.2f", results.getSessionCost()));

            holder.callTimeCtxv.setText(results.getDuration());


            if (rating != null) {
                for (int j = 0; j < rating.length; j++) {
                    if (isValid(rating[j].getType())) {
                        String type = rating[j].getType();
                   /* if(type.equals("customer")){
                        customerRatings = rating[j];
                    }*/
                        if (type.equals(getUserType())) {
                            customerRatings = rating[j];
                        }
                    }
                }
            }
            if (emotionList != null) {
                for (int i = 0; i < emotionList.size(); i++) {
                    String id = emotionList.get(i).getId();
                    if (isValid(customerRatings)) {
                        if (isValid(customerRatings.getAfterSessionEmotionId())) {
                            String afterSessionEmotionId = customerRatings.getAfterSessionEmotionId();
                            if (id.equals(afterSessionEmotionId)) {
                                holder.afterFeelCtxv.setText(emotionList.get(i).getTitle());
                                //Picasso.with(context).load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage()).into(holder.afterEmotionImg);
                                Glide.with(context)
                                        .load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage())
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model,
                                                                       Target<GlideDrawable> target, boolean isFirstResource) {
                                                e.printStackTrace();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                                           Target<GlideDrawable> target,
                                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                                return false;
                                            }
                                        })
                                        .dontTransform()
                                        .into(holder.afterEmotionImg);

                            }
                        }
                    }
                    if (isValid(customerRatings)) {
                        if (isValid(customerRatings.getBeforeSessionEmotionId())) {
                            String beforeSessionEmotionId = customerRatings.getBeforeSessionEmotionId();
                            if (id.equals(beforeSessionEmotionId)) {
                                holder.beforeFeelCtxv.setText(emotionList.get(i).getTitle());
                                // Picasso.with(context).load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage()).into(holder.beforeEmotionImg);
                                Glide.with(context)
                                        .load(GLOBAL_IMAGE_URL + emotionList.get(i).getImage())
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model,
                                                                       Target<GlideDrawable> target, boolean isFirstResource) {
                                                e.printStackTrace();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                                           Target<GlideDrawable> target,
                                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                                return false;
                                            }
                                        })
                                        .dontTransform()
                                        .into(holder.beforeEmotionImg);
                            }
                        }
                    }
                }
            }

            /*
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            Date parsedTime = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateForm
            at("MM-dd-yyyy'  'hh:mm a");
            String formattedDate = sdf2.format(parsedTime);*/

            holder.dateCtxv.setText(Utility.getStartFormatDate(time, context));


            holder.topLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean editable = true;
                    if (position > firstValidSessionPos || !firstItemEditable) {
                        editable = false;
                    }
                    try {
                            String sessionID = results.getId();
                        LastSessionActivity.start(context, sessionID, editable);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });


        }catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD("MSA setViewholder:"+ex.getMessage());
        }
    }

    private void setViewholder(MissedViewHolder missedViewHolder, int position) {
        try {
            final MSessionDetailRoot results=getItem(position);
            missedViewHolder.nameUserCtxv.setText(results.getPacifyr().getFirstName());
            String time = results.getStartTime();
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            Date parsedTime = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a");
            String formattedDate = sdf2.format(parsedTime);*/
            missedViewHolder.dateCtxv.setText(Utility.getStartFormatDate(time, context));

            String  imgUrl = results.getPacifyr().getImageUrl();
            if(isValid(imgUrl)){
                Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user)).error(context.getResources().getDrawable(R.drawable.placeholder_user)).into(missedViewHolder.userCimv);
            }

            String sessionType = list.get(position).getSessionType();
            if(sessionType.equals("audio")) {
                missedViewHolder.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (sessionType.equals("video")) {
                missedViewHolder.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }

            if(list.get(position).getDeclined().equals("true")){
                missedViewHolder.missedCtxv.setText("Declined Call");
            }

            missedViewHolder.topLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress();
                    try {
                        if (context instanceof MySessionsActivity)
                            ((MySessionsActivity) (context)).callchatEventListner.onChatEventClick((new ChatEvent(results.getPacifyr(), false)));
                        if (context instanceof DashboardActivity)
                        ((DashboardActivity) (context)).callchatEventListner.onChatEventClick((new ChatEvent(results.getPacifyr(), false)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Sorry, something went wrong. Please try later", Toast.LENGTH_LONG).show();
                    }
                    //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                    // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                        }
                    },500);
                }
            });


        }catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD(" MSA setViewholder ex:"+ex.getMessage());
        }
    }

    private void setViewholder(ServiceNotProvided serviceNotProvided, int position) {
        try {
            final MSessionDetailRoot results = getItem(position);
            serviceNotProvided.nameUserCtxv.setText(results.getPacifyr().getFirstName());
            String time = results.getStartTime();
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            Date parsedTime = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a");
            String formattedDate = sdf2.format(parsedTime);*/
            serviceNotProvided.dateCtxv.setText(Utility.getStartFormatDate(time, context));

            String imgUrl = results.getPacifyr().getImageUrl();
            if (isValid(imgUrl)) {
                Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user)).error(context.getResources().getDrawable(R.drawable.placeholder_user)).into(serviceNotProvided.userCimv);
            }

            String sessionType = list.get(position).getSessionType();
            if (sessionType.equals("audio")) {
                serviceNotProvided.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            } else if (sessionType.equals("video")) {
                serviceNotProvided.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }

            /*if(list.get(position).getDeclined().equals("true")){
                serviceNotProvided.missedCtxv.setText("Declined Call");
            }

            missedViewHolder.topLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress();
                    ( (DashboardActivity) (context)).callchatEventListner.onChatEventClick((new ChatEvent(results.getPacifyr(), false)));
                    //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                    // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                        }
                    },500);
                }
            });
            */

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.LOGD(" MSA setViewholder ex:" + ex.getMessage());
        }
    }

    private void setViewholder(OfficeVisit officeVisit, int position) {
        try {
            final MSessionDetailRoot results = getItem(position);
            officeVisit.nameUserCtxv.setText(results.getPacifyr().getFirstName());
            String time = results.getStartTime();
            officeVisit.dateCtxv.setText(Utility.getStartFormatDate(time, context));

            String imgUrl = results.getPacifyr().getImageUrl();
            if (isValid(imgUrl)) {
                Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user))
                        .error(context.getResources().getDrawable(R.drawable.placeholder_user))
                        .into(officeVisit.userCimv);
            }

            officeVisit.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sessionID = results.getId();
                    OfficeVisitSummary.start(context, sessionID, false);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.LOGD(" MSA setViewholder ex:" + ex.getMessage());
        }
    }

    public Boolean isValid(int object) {
        return object > 0;

    }
    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    @Override
    public int getItemViewType(int position) {
        int pos=0;
        String sessionType = list.get(position).getSessionType();
        if (list.get(position).getMissed().equals("true"))
        {
            pos = 1;

        }else if(list.get(position).getDeclined().equals("true")){

            pos = 1;

        }else if(!isValid(list.get(position).getPacifyr().getName())){
            pos = 2;
        } else if (isValid(list.get(position)) && isValid(list.get(position).getSessionType())
                && list.get(position).getSessionType().equalsIgnoreCase(MUser.VISIT_TYPE_OFFICE)) {
            pos = 4;
        } else if (!list.get(position).isBilledSession()) {
            pos = 3;
        }

        return pos;
    }
    public Boolean isValid(List list)
    {
        if(list!=null)
            return list.size() > 0;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            return list.size() >= position;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MainTextView nameUserCtxv;
        private MainTextView priceCtxv;
        private MainTextView dateCtxv;
        private MainTextView feltCtxv;
        private MainTextView beforeCallCtxv;
        private ImageView beforeEmotionImg;
        private MainTextView beforeFeelCtxv;
        private MainTextView afterCallCtxv;
        private ImageView afterEmotionImg;
        private MainTextView afterFeelCtxv;
        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private ImageView calltype_imv;
        private MainTextView callTimeCtxv;
        private LinearLayout after_Layout;
        private LinearLayout before_Layout;

        public MyViewHolder(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            callTimeCtxv = view.findViewById(R.id.call_time_ctxv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            userCimv = view.findViewById(R.id.user_cimv);
            priceCtxv = view.findViewById(R.id.price_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);
            feltCtxv = view.findViewById(R.id.felt_ctxv);
            beforeCallCtxv = view.findViewById(R.id.before_call_ctxv);
            beforeEmotionImg = view.findViewById(R.id.before_emotion_img);
            beforeFeelCtxv = view.findViewById(R.id.before_feel_ctxv);
            afterCallCtxv = view.findViewById(R.id.after_call_ctxv);
            afterEmotionImg = view.findViewById(R.id.after_emotion_img);
            afterFeelCtxv = view.findViewById(R.id.after_feel_ctxv);
            calltype_imv = view.findViewById(R.id.calltype_imv);
            before_Layout = view.findViewById(R.id.before_Layout);
            after_Layout = view.findViewById(R.id.after_Layout);


        }
    }



    public class MissedViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private MainTextView nameUserCtxv;
        private ImageView callTypeImg;
        private MainTextView missedCtxv;
        private MainTextView dateCtxv;

        public MissedViewHolder(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            userCimv = view.findViewById(R.id.user_cimv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            callTypeImg = view.findViewById(R.id.call_type_img);
            missedCtxv = view.findViewById(R.id.missed_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);

        }
    }

    public class ServiceNotProvided extends RecyclerView.ViewHolder {

        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private MainTextView nameUserCtxv;
        private ImageView callTypeImg;
        private MainTextView missedCtxv;
        private MainTextView dateCtxv;

        public ServiceNotProvided(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            userCimv = view.findViewById(R.id.user_cimv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            callTypeImg = view.findViewById(R.id.call_type_img);
            missedCtxv = view.findViewById(R.id.missed_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);

        }
    }

    public class OfficeVisit extends RecyclerView.ViewHolder {

        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private MainTextView nameUserCtxv;
        private ImageView callTypeImg;
        private MainTextView missedCtxv;
        private MainTextView dateCtxv;

        public OfficeVisit(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            userCimv = view.findViewById(R.id.user_cimv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            missedCtxv = view.findViewById(R.id.missed_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);

        }
    }


    public class ViewHolderEmpty extends RecyclerView.ViewHolder {

        public ViewHolderEmpty(View view) {
            super(view);
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
}
