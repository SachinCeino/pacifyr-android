package com.pacifyr.omhealthcare.emotionupdate;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.ModelSpecialityByEmotion;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;


public class NegativeChangeEmotionsAdapter extends RecyclerView.Adapter<NegativeChangeEmotionsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    ArrayList<MEmotion> list = new ArrayList<>();
    public Context context;
    public Activity activity;


    public NegativeChangeEmotionsAdapter(Context context, ArrayList<MEmotion> mListPositiveEmotions) {
        PacifyerActivity.call_emotion=null;
        this.mInflater = LayoutInflater.from(context);
        this.list = mListPositiveEmotions;
        this.context = context;

    }


    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.emotion_poitive_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.emotion_ctxv.setText(list.get(position).getTitle());
        final String imgUrl=UtilityNetworkService.GLOBAL_IMAGE_URL+list.get(position).getImage();
        Picasso.with(context).load(imgUrl).into(holder.imageView);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        if (list.get(position).isSelect()){
            holder.imageView.clearColorFilter();
        }else {
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.imageView.setColorFilter(filter);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager prefM= PrefManager.getInstance(context);
                String token=prefM.getAccessToken();
                String emotionID;
                emotionID = list.get(position).getId();
                holder.imageView.clearColorFilter();
                for(int i=0;i<list.size();i++)
                {
                    if(i==position){
                        list.get(position).setSelect(true);
                    }
                    else{
                        list.get(i).setSelect(false);
                    }
                }

                //PacifyerActivity.call_emotion=list.get(position);

                notifyDataSetChanged();
                String url = GLOBAL_URL + "specialities/getCategories/byEmotion?emotionId="+emotionID;
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) { try {
                           /* hideProgress();*/
                            ModelSpecialityByEmotion modelSpecialityByEmotion = GsonUtils.getInstance().gsonToModelSpeciltyByEmotion(json);
                            postEventOnMainThread(new NotifyEvent(modelSpecialityByEmotion,"negative"));
                        }
                            catch (Exception e) {
                            e.printStackTrace();
                        }
                        }
                    };
                AQuery aq = new AQuery(context);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken", token);
                aq.ajax(url, JSONObject.class, cb);
            }
        });
    }

    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
            }
        });

    }


    // total number of cells
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public DeviceFitImageView imageView;
        public CustomFontTextView emotion_ctxv;

        public ViewHolder(View itemView) {
            super(itemView);
            emotion_ctxv = itemView.findViewById(R.id.emotion_ctxv);
            imageView = itemView.findViewById(R.id.emotion_dimv);

        }
    }



    public KProgressHUD hud_progress = null;
    public KProgressHUD hud_status = null;

    public void setLoading() {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoadingWait() {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoading(String message) {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void showStatus() {
        if(hud_status != null) if(!hud_status.isShowing()) hud_status.show();
    }

    public void hideStatus() {
        if(hud_status != null) if(hud_status.isShowing()) hud_status.dismiss();
    }
    public void hideProgress() {
        if(isValid(hud_progress))if(hud_progress.isShowing()) hud_progress.dismiss();
    }

    public void showProgress() {
        if(isValid(hud_progress)) if(!hud_progress.isShowing()) hud_progress.show();
    }


    public Boolean isValid(Object object) {
        return object != null;

    }
}
