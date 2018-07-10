package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.ChatEvent;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ceino on 14/12/17.
 */

public class MissedCallsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<MSessionDetailRoot> list;
    Context context;
    //ArrayList<MissedSession.Results> missedList;

    String userid,token;


    public MissedCallsAdapter(ArrayList<MSessionDetailRoot> missedList, Context context) {
        this.list = missedList;
        this.context=context;

        for (int i=0;i<list.size();i++){

        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder rcv=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mysession_item, parent, false);
        itemView = LayoutInflater.from(context).inflate(R.layout.list_mysession_missed_item, parent, false);
        rcv = new MissedViewHolder(itemView);
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
        MissedViewHolder missedViewHolder = (MissedViewHolder) topholder;
        setViewholder(missedViewHolder,position);
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


    /*public String  getUserType()
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
    }*/

    private void setViewholder(MissedViewHolder missedViewHolder, int position) {
        try {
            final MSessionDetailRoot results=getItem(position);
            missedViewHolder.nameUserCtxv.setText(results.getUser().getFirstName());
            String time= results.getEndTime();

            missedViewHolder.dateCtxv.setText(results.getStartFormatDate(time));

            String  imgUrl = results.getUser().getImageUrl();
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


        }catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD(" MSA setViewholder ex:"+ex.getMessage());
        }
    }

    public Boolean isValid(int object) {
        if(object > 0) return true;
        return false;

    }
    public Boolean isValidhtml(String text) {
        if(isValid(text)) if(text.contains("http")||text.contains("https")) return true;
        return false;

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
            if(list.size()>0)
                return  true;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            if(list.size()>=position)
                return  true;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        if(object!=null)
            return  true;
        return  false;

    }

    public Boolean isValid(String text) {
        if(text != null) if(!text.trim().equalsIgnoreCase("")) return true;
        return false;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
        private ImageView calltype_imv;
        private CustomFontTextView callTimeCtxv;

        public MyViewHolder(View view) {
            super(view);
            topLlay = (LinearLayout) view.findViewById(R.id.top_llay);
            callTimeCtxv = (CustomFontTextView) view.findViewById(R.id.call_time_ctxv);
            nameUserCtxv = (CustomFontTextView) view.findViewById(R.id.name_user_ctxv);
            userCimv = (CircleImageView) view.findViewById(R.id.user_cimv);
            priceCtxv = (CustomFontTextView) view.findViewById(R.id.price_ctxv);
            dateCtxv = (CustomFontTextView) view.findViewById(R.id.date_ctxv);
            feltCtxv = (CustomFontTextView) view.findViewById(R.id.felt_ctxv);
            beforeCallCtxv = (CustomFontTextView) view.findViewById(R.id.before_call_ctxv);
            beforeEmotionImg = (ImageView) view.findViewById(R.id.before_emotion_img);
            beforeFeelCtxv = (CustomFontTextView) view.findViewById(R.id.before_feel_ctxv);
            afterCallCtxv = (CustomFontTextView) view.findViewById(R.id.after_call_ctxv);
            afterEmotionImg = (ImageView) view.findViewById(R.id.after_emotion_img);
            afterFeelCtxv = (CustomFontTextView) view.findViewById(R.id.after_feel_ctxv);
            calltype_imv = (ImageView) view.findViewById(R.id.calltype_imv);

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
            topLlay = (LinearLayout) view.findViewById(R.id.top_llay);
            userCimv = (CircleImageView) view.findViewById(R.id.user_cimv);
            nameUserCtxv = (CustomFontTextView) view.findViewById(R.id.name_user_ctxv);
            callTypeImg = (ImageView) view.findViewById(R.id.call_type_img);
            missedCtxv = (CustomFontTextView) view.findViewById(R.id.missed_ctxv);
            dateCtxv = (CustomFontTextView) view.findViewById(R.id.date_ctxv);

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
