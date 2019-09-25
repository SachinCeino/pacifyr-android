package com.pacifyr.omhealthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.MSessionRating;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.home.LastSessionActivity;
import com.pacifyr.omhealthcare.home.PacifyrDashboardActivity;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.network.NetworkService.modelAllEmotions;


/**
 * Created by ceino on 10/1/18.
 */

/*public class PacifyrSessionAdapter {
}*/
public class PacifyrSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<MSessionDetailRoot> list;
    Context context;
    ArrayList<MEmotion> emotionList;
    Activity activity;
    String userid,token;


    public PacifyrSessionAdapter(Context context, ArrayList<MSessionDetailRoot> list, String userid, String token) {
        this.list = list;
        this.context=context;
        activity=activity;
        emotionList=modelAllEmotions.getResultsList();
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
        }else {
            rcv = new MyViewHolder(itemView);
        }

        return rcv;
    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/

    public MSessionDetailRoot getItem (int position) {
        MSessionDetailRoot results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder topholder, final int position) {


        if(topholder!=null) {
            if (topholder instanceof  MyViewHolder) {
                MyViewHolder holder = ( MyViewHolder) topholder;

                final MSessionDetailRoot results=getItem(position);

                setViewholder(holder,results,position);
                //loadingViewHolder.progressBar.setIndeterminate(true);
            }else if (topholder instanceof  MissedViewHolder) {
                MissedViewHolder missedViewHolder = (MissedViewHolder) topholder;
                setViewholder(missedViewHolder,position);
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
                t.printStackTrace();
            }
        }catch (Exception e)
        {
            label="customer";
        }
        return label;
    }

    private void setViewholder(MyViewHolder holder, final MSessionDetailRoot results, int position) {
        try {

            String sessionType=results.getSessionType();
            MSessionRating[] rating = results.getSessionRatings();
            MSessionRating customerRatings = null;
            for(int j=0;j<rating.length;j++)
            {
                if(isValid(rating[j].getType()))
                {
                    String type = rating[j].getType();
                   /* if(type.equals("customer")){
                        customerRatings = rating[j];
                    }*/
                    if(type.equals(getUserType())){
                        customerRatings = rating[j];
                    }
                }
            }

            for(int i=0;i<emotionList.size();i++){
                String id=emotionList.get(i).getId();
                if(isValid(customerRatings)) {
                   if (isValid(customerRatings.getAfterSessionEmotionId())) {
                        String afterSessionEmotionId = customerRatings.getAfterSessionEmotionId();
                        if (id.equals(afterSessionEmotionId)) {
                            holder.afterFeelCtxv.setText(emotionList.get(i).getTitle());
                            Picasso.with(context).load(GLOBAL_IMAGE_URL+emotionList.get(i).getImage()).into(holder.afterEmotionImg);
                        }
                    }
                }
                if(isValid(customerRatings)) {
                    if (isValid(customerRatings.getBeforeSessionEmotionId())) {
                        String beforeSessionEmotionId = customerRatings.getBeforeSessionEmotionId();
                        if (id.equals(beforeSessionEmotionId)) {
                            holder.beforeFeelCtxv.setText(emotionList.get(i).getTitle());
                            String imgUrl = emotionList.get(i).getImage();
                            Picasso.with(context).load(GLOBAL_IMAGE_URL+emotionList.get(i).getImage()).into(holder.beforeEmotionImg);
                        }
                    }
                }
            }
            MUser pac = results.getUser();
            String name = pac.getFirstName();
            holder.nameUserCtxv.setText(pac.getFirstName());
            String imgUrl=pac.getImage();
            if(isValidhtml(imgUrl)){
                Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user)).error(context.getResources().getDrawable(R.drawable.placeholder_user)).into(holder.userCimv);
                //Picasso.with(context).load(imgUrl).into(holder.userCimv);
            }else{
                Picasso.with(context).load(GLOBAL_IMAGE_URL+imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user)).error(context.getResources().getDrawable(R.drawable.placeholder_user)).into(holder.userCimv);
            }

            if(sessionType.equals("audio")) {
                holder.calltype_imv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (sessionType.equals("video")) {
                holder.calltype_imv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }

            holder.priceCtxv.setText(Utility.getPriceSign(context) +String.format("%.2f",results.getPacifyrEarnings()));


          /*  SimpleDateFormat inputFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.mmm'Z'", Locale.US);
            //inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a");*/
            // Adjust locale and zone appropriately

            String t =results.getStartTime();
            String time = Utility.getStartFormatDate(t, context);
            holder.dateCtxv.setText(time);




            //Send a message

            /*Date date = inputFormat.parse(time);
            String outputText = outputFormat.format(date);*/


            holder.dateCtxv.setText(time);


            holder.topLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String sessionID = results.getId();
                        LastSessionActivity.start(context,sessionID);
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
        }
    }

    public Boolean isValid(int object) {
        return object > 0;

    }
    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    private void setViewholder(MissedViewHolder missedViewHolder, int position) {
        try {
            final MSessionDetailRoot results=getItem(position);
            missedViewHolder.nameUserCtxv.setText(results.getUser().getFirstName());

            String  t = results.getStartTime();
            String time = Utility.getStartFormatDate(t, context);
            missedViewHolder.dateCtxv.setText(time);

            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            Date parsedTime = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a");*/

           /* String formattedDate = sdf2.format(parsedTime);*/

            String sessionType = list.get(position).getSessionType();
            if(sessionType.equals("audio")) {
                missedViewHolder.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (sessionType.equals("video")) {
                missedViewHolder.callTypeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }


            String imgUrl = results.getUser().getImageUrl();
            if(isValid(imgUrl)){
                Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_user)).error(context.getResources().getDrawable(R.drawable.placeholder_user)).into(missedViewHolder.userCimv);
                //Picasso.with(context).load(imgUrl).into(holder.userCimv);
            }

            //Picasso.with(context).load(GLOBAL_IMAGE_URL+).into(missedViewHolder.userCimv);
            if(list.get(position).getDeclined().equals("true")){
                missedViewHolder.missedCtxv.setText("Declined Call");
            }

            missedViewHolder.topLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress();
                    ( (PacifyrDashboardActivity) (context)).callchatEventListner.onChatEventClick((new ChatEvent(results.getUser(), false)));
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
        }
    }

    @Override
    public int getItemViewType(int position) {
        int pos=0;
        if (list.get(position).getMissed().equals("true"))
        {
            pos = 1;

        }else if(list.get(position).getDeclined().equals("true")){

            pos = 1;

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
        private final ImageView calltype_imv;
        private CustomFontTextView nameUserCtxv;
        private CustomFontTextView priceCtxv;
        private CustomFontTextView dateCtxv;
        private CustomFontTextView feltCtxv;
        private CustomFontTextView beforeCallCtxv;
        private ImageView beforeEmotionImg;
        private CustomFontTextView beforeFeelCtxv;
        private CustomFontTextView afterCallCtxv;
        private ImageView afterEmotionImg;
        private CustomFontTextView afterFeelCtxv;
        private LinearLayout topLlay;
        private CircleImageView userCimv;

        public MyViewHolder(View view) {
            super(view);
            calltype_imv = view.findViewById(R.id.calltype_imv);
            topLlay = view.findViewById(R.id.top_llay);
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
        }
    }



    public class MissedViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private CustomFontTextView nameUserCtxv;
        private ImageView callTypeImg;
        private CustomFontTextView missedCtxv;
        private CustomFontTextView dateCtxv;

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

