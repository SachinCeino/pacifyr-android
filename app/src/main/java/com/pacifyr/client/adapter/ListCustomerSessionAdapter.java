package com.pacifyr.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MPastSessions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyr.client.R;
import com.pacifyr.client.home.SummaryDetailActivity;
import com.pacifyr.client.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;

/**
 * Created by ceino on 23/1/18.
 */

public class ListCustomerSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //R.layout.list_customer_detail_session

    public Context mContext;
    private LayoutInflater mInflater;
    ArrayList<MPastSessions.Results> lists;

    ArrayList<MEmotion> emotionList;

    public static MPastSessions.Results resultValues;

    public ListCustomerSessionAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;

    }

    public ListCustomerSessionAdapter(Context applicationContext, ArrayList<MPastSessions.Results> list) {
        this.mInflater = LayoutInflater.from(applicationContext);
        mContext = applicationContext;
        emotionList=modelAllEmotions.getResultsList();
        lists=list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_customer_detail_session, parent, false);
        RecyclerView.ViewHolder rcv=null;


        if (viewType==-2) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_customerdetail_missed_item, parent, false);
            rcv = new MissedViewHolder(itemView);
        }else if(viewType>=0) {
            rcv = new ViewHolder(itemView);
        }

        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder topholder, int position) {
        try {
            if(topholder!=null) {
              /*  if (topholder instanceof  CustRcvAdapter.LoadingViewHolder) {
                    CustRcvAdapter.LoadingViewHolder loadingViewHolder = ( CustRcvAdapter.LoadingViewHolder) topholder;
                    loadingViewHolder.progressBar.setIndeterminate(true);
                }else*/ if (topholder instanceof  ViewHolder) {
                  ViewHolder holder = ( ViewHolder) topholder;
                    setViewholder(holder,position);
                }
                else if(topholder instanceof MissedViewHolder)
                {
                    MissedViewHolder holder = ( MissedViewHolder) topholder;
                    setMissedViewHolder(holder,position);
                }
            }
        } catch (Exception e) {
            LogUtils.LOGD("exception", "CustRcvAdapter onBindViewHolder " + e.getMessage());
        }
    }

    private void setMissedViewHolder(MissedViewHolder missedViewHolder, int position) {
        try{
            final MPastSessions.Results results=getItem(position);
            missedViewHolder.nameUserCtxv.setText(results.getPacifyr().getFirstName());
            String time= results.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
            Date parsedTime = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a");
            String formattedDate = sdf2.format(parsedTime);
            missedViewHolder.dateCtxv.setText(formattedDate);

            String  sessionType = results.getSessionType();

            if(sessionType.equals("audio")) {
                missedViewHolder.callTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (sessionType.equals("video")) {
                missedViewHolder.callTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }

            String imgUrl = results.getPacifyr().getImageUrl();
            if(isValid(imgUrl)){
                Picasso.with(mContext).load(imgUrl).placeholder(mContext.getResources().getDrawable(R.drawable.placeholder_user)).error(mContext.getResources().getDrawable(R.drawable.placeholder_user)).into(missedViewHolder.userCimv);
            }

            if(results.getDeclined()){
                missedViewHolder.missedCtxv.setText("Declined Call");
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public void setViewholder(ViewHolder holder, int position) {

        try {

            final MPastSessions.Results results = getItem(position);
            if (isValid(results)) {
                resultValues = results;
                holder. feel_rlay.setVisibility(View.VISIBLE);
                if(results.getMissed())
                    holder. feel_rlay.setVisibility(View.GONE);
                //holder.customerNameCtxv.setText(resultlist.get(position).getFirstName());
                holder.nameUserCtxv.setText(results.getPacifyr().getFirstName());
                final String imgUrl =  results.getPacifyr().getImageUrl();
                MSessionRating[] sessionRating = results.getSessionRatings();
                MSessionRating customerRating = new MSessionRating();
                MSessionRating pacfyrRating = new MSessionRating();
                String duration = results.getDuration();
                holder.durationCtxv.setText(duration);
                String endTime = results.getEndTime();
               /* SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                Date date = null;
                SimpleDateFormat df2 = new SimpleDateFormat("dd-mm-yyyy HH:mm a", Locale.ENGLISH);*/
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                SimpleDateFormat target = new SimpleDateFormat("dd-mm-yyyy, HH:mm a", Locale.ENGLISH);
                Date date2 = null;
                date2 = df.parse(endTime);
                String time = target.format(date2);
                holder.dateCtxv.setText(time);
                holder.topLlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SummaryDetailActivity.start(mContext,results.getId());
                    }
                });
                String  sessionType = results.getSessionType();
                for(int i=0;i<sessionRating.length;i++)
                {
                    if(sessionRating[i].getType().equals("customer")) {
                        customerRating =sessionRating[i];

                    }else{
                        pacfyrRating =sessionRating[i];
                    }

                }
                for(int i=0;i<emotionList.size();i++){
                    String id=emotionList.get(i).getId();
                    if(isValid(customerRating)) {
                        if (isValid(customerRating.getAfterSessionEmotionId())) {
                            String afterSessionEmotionId = customerRating.getAfterSessionEmotionId();
                            if (id.equals(afterSessionEmotionId)) {
                                holder.afterFeelCtxv.setText(emotionList.get(i).getTitle());
                                Picasso.with(mContext).load(GLOBAL_IMAGE_URL+emotionList.get(i).getImage()).into(holder.afterEmotionImg);
                            }
                        }
                    }
                    if(isValid(customerRating)) {
                        if (isValid(customerRating.getBeforeSessionEmotionId())) {
                            String beforeSessionEmotionId = customerRating.getBeforeSessionEmotionId();
                            if (id.equals(beforeSessionEmotionId)) {
                                holder.beforeFeelCtxv.setText(emotionList.get(i).getTitle());
                                Picasso.with(mContext).load(GLOBAL_IMAGE_URL+emotionList.get(i).getImage()).into(holder.beforeEmotionImg);
                            }
                        }
                    }
                    if(isValid(pacfyrRating)) {
                        if (isValid(pacfyrRating.getAfterSessionEmotionId())) {
                            String afterSessionEmotionId = pacfyrRating.getAfterSessionEmotionId();
                            if (id.equals(afterSessionEmotionId)) {
                                holder.openinonEmotionCtxv.setText(emotionList.get(i).getTitle());
                                Picasso.with(mContext).load(GLOBAL_IMAGE_URL+emotionList.get(i).getImage()).into(holder.openinonImg);

                            }
                        }
                    }
                }

                if (isValid(imgUrl))
                    Picasso.with(mContext).load(imgUrl).placeholder(R.drawable.placeholder_user).into(holder.userCimv);
                if(sessionType.equals("audio")) {
                    holder.callTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_audio_blue));
                }else if (sessionType.equals("video")) {
                    holder.callTypeImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_video_cam_blue));
                }
                //holder.customerWalletCtxv.setText(resultlist.get(position).getPrice());
                    //holder.customerWalletCtxv.setText(Utility.getPriceSign(context) + results.getTotalSpending() + ".00 Earnings to date.");
                    //holder.customerRating.setRating(Float.parseFloat(resultlist.get(position).getRating()));
                    //String rating =customerses[position].getRating();
                    /*if(isValid(rating)) {
                        Float ratin=Float.parseFloat(rating);
                        holder.customerRating.setRating(ratin);
                    }*/
                    /*holder.topLlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            custListUser = results.getUser();
                            CustomerDetailActivity.start(context);
                        }
                    });*/

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public MPastSessions.Results getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getItemCount() {
        return isValid(lists)?lists.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        int pos=-1;
        if (isValid(lists.get(position)))
        {
            pos = position;
            if (lists.get(position).getMissed())
            {
                pos = -2;

            }else if(lists.get(position).getDeclined()){

                pos = -2;

            }

        }

        return pos;
    }
    public Boolean isValid(Object object) {
        return object != null;

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
    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private MainTextView nameUserCtxv;
        private MainTextView durationCtxv;
        private MainTextView dateCtxv;
        private MainTextView feltCtxv;
        private MainTextView beforeCallCtxv;
        private ImageView beforeEmotionImg;
        private MainTextView beforeFeelCtxv;
        private MainTextView afterCallCtxv;
        private ImageView afterEmotionImg;
        private MainTextView afterFeelCtxv;
        private MainTextView yourOpenionCtxv;
        private ImageView openinonImg;
        private MainTextView openinonEmotionCtxv;
        private  RelativeLayout  feel_rlay;
        private ImageView callTypeImg;
        public ViewHolder(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            feel_rlay = view.findViewById(R.id.feel_rlay);
            userCimv = view.findViewById(R.id.user_cimv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            durationCtxv = view.findViewById(R.id.duration_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);
            feltCtxv = view.findViewById(R.id.felt_ctxv);
            beforeCallCtxv = view.findViewById(R.id.before_call_ctxv);
            beforeEmotionImg = view.findViewById(R.id.before_emotion_img);
            beforeFeelCtxv = view.findViewById(R.id.before_feel_ctxv);
            afterCallCtxv = view.findViewById(R.id.after_call_ctxv);
            afterEmotionImg = view.findViewById(R.id.after_emotion_img);
            afterFeelCtxv = view.findViewById(R.id.after_feel_ctxv);
            yourOpenionCtxv = view.findViewById(R.id.your_openion_ctxv);
            openinonImg = view.findViewById(R.id.openinon_img);
            callTypeImg = view.findViewById(R.id.call_type_img);
            openinonEmotionCtxv = view.findViewById(R.id.openinon_emotion_ctxv);
        }
    }

    public class MissedViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout topLlay;
        private CircleImageView userCimv;
        private MainTextView nameUserCtxv;
        private ImageView callTypeImg;
        private MainTextView missedCtxv;
        private MainTextView dateCtxv;
        private ImageView messageImv;

        public MissedViewHolder(View view) {
            super(view);
            topLlay = view.findViewById(R.id.top_llay);
            userCimv = view.findViewById(R.id.user_cimv);
            nameUserCtxv = view.findViewById(R.id.name_user_ctxv);
            callTypeImg = view.findViewById(R.id.call_type_img);
            missedCtxv = view.findViewById(R.id.missed_ctxv);
            dateCtxv = view.findViewById(R.id.date_ctxv);
            messageImv = view.findViewById(R.id.message_imv);
        }
    }
}
