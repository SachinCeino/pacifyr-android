package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.model.ModelSpecialityByEmotion;
import com.pacifyr.client.network.GsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;


public class PostiveEmotionsAdapter extends RecyclerView.Adapter<PostiveEmotionsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ArrayList<MEmotion> list=new ArrayList<>();
    public  Context context;
    Activity activity;
    private boolean isMultiSelectionEnabled = false;

    public PostiveEmotionsAdapter(Context context, ArrayList<MEmotion> mListPositiveEmotions) {
        PacifyrActivity.call_emotion=null;
        this.mInflater = LayoutInflater.from(context);
        this.list = mListPositiveEmotions;
        this.context =context;
    }

    public PostiveEmotionsAdapter( Context context, ArrayList<MEmotion> mListPositiveEmotions,boolean isMultiSelectionEnabled) {
        PacifyrActivity.call_emotion=null;
        this.mInflater = LayoutInflater.from(context);
        this.list = mListPositiveEmotions;
        this.context =context;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;
        
    }

    public PostiveEmotionsAdapter(Context context,Activity activity, ArrayList<MEmotion> mListPositiveEmotions) {
        PacifyrActivity.call_emotion=null;
        this.mInflater = LayoutInflater.from(context);
        this.list = mListPositiveEmotions;
        this.context =context;
        this.activity=activity;
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
        final String imgUrl=GLOBAL_IMAGE_URL+list.get(position).getImage();
        Picasso.with(context).load(imgUrl).into(holder.imageView);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        if (list.get(position).isSelect()){
            holder.imageView.clearColorFilter();
            holder.emotion_ctxv.setTextColor(context.getResources().getColor(R.color.color_primary));
        }else {
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.imageView.setColorFilter(filter);
            holder.emotion_ctxv.setTextColor(context.getResources().getColor(R.color.grey_dark));
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

                PacifyrActivity.call_emotion=list.get(position);

                notifyDataSetChanged();
                //holder.imageView.clearColorFilter();
                //holder.imageView.setColorFilter(R.color.transparent_black_percent_25, PorterDuff.Mode.DST_OUT);

                activity.findViewById(R.id.select_emotion_rlay).setVisibility(View.GONE);
                activity.findViewById(R.id.select_emotion_detail_rlay).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.button3).setBackgroundResource(R.drawable.circle_click_btn);
                activity.findViewById(R.id.button2).setBackgroundResource(R.drawable.circle_button);
                MainTextView whyFellCtxv = activity.findViewById(R.id.why_feeling_ctxv);
               // whyFellCtxv.setText("Why are you feeling "+list.get(position).getTitle()+"?");
                whyFellCtxv.setText(context.getResources().getString(R.string.How_can_we_help_you_today));
                Button b1=activity.findViewById(R.id.button1);
                Button b2=activity.findViewById(R.id.button2);
                Button b3=activity.findViewById(R.id.button3);
                Button moreBtn=activity.findViewById(R.id.more_button);
                moreBtn.setVisibility(View.VISIBLE);
                b1.setEnabled(true);
                b1.setClickable(true);
                b2.setClickable(true);
                b2.setEnabled(true);

                /*String url = GLOBAL_URL+"specialities/byEmotion?emotionId="+emotionID;*/
                String url = GLOBAL_URL + "specialities/getCategories/byEmotion?emotionId="+emotionID;
                AjaxCallback cb = new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        try {

                            ModelSpecialityByEmotion modelSpecialityByEmotion = GsonUtils.getInstance().gsonToModelSpeciltyByEmotion(json);
                            RecyclerView detailRcv = activity.findViewById(R.id.emotion_detail_rcv);
                            detailRcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            EmotionSpecialityAdapter adapter=new EmotionSpecialityAdapter(context,activity, modelSpecialityByEmotion.getResultsList());
                            detailRcv.setAdapter(adapter);
                            //notifyDataSetChanged();
                            //notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                AQuery aq = new AQuery(context);
                cb.header("Content-Type", "application/json");
                cb.header("AccessToken",token);
                aq.ajax(url,JSONObject.class, cb);

            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return list.size();
    }

    public Boolean isValid(Object object) {
        return object != null;

    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public KProgressHUD hud_progress =null;

    public void setLoading() {
        if(isValid(hud_progress))hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoadingWait() {
        if(isValid(hud_progress))hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoading(String message) {
        if(isValid(hud_progress))hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void hideProgress() {
        if(isValid(hud_progress))if(hud_progress.isShowing()) hud_progress.dismiss();
    }

    public void showProgress() {
        if(isValid(hud_progress)) if(!hud_progress.isShowing()) hud_progress.show();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public DeviceFitImageView imageView;
        public MainTextView emotion_ctxv;

        public ViewHolder(View itemView) {
            super(itemView);
            emotion_ctxv = itemView.findViewById(R.id.emotion_ctxv);
            imageView = itemView.findViewById(R.id.emotion_dimv);

        }
    }
}