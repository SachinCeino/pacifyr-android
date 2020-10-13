package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.PacifyrDashboardActivity;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.sign.RegisterActivity;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.CallChangeEmotion;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.GLOBAL_URL;


public class GetPosEmotionWhileCallAdapter extends RecyclerView.Adapter<GetPosEmotionWhileCallAdapter.ViewHolder> {

    public Activity context;
    public KProgressHUD hud_progress = null;
    ArrayList<MEmotion> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private PrefManager prefM;


    public GetPosEmotionWhileCallAdapter(Activity context, ArrayList<MEmotion> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;

        prefM = PrefManager.getInstance(context);

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
        final String imgUrl = GLOBAL_IMAGE_URL + list.get(position).getImage();
        Picasso.with(context).load(imgUrl).into(holder.imageView);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        if (list.get(position).isSelect()) {
            holder.imageView.clearColorFilter();
        } else {
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.imageView.setColorFilter(filter);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(list.get(position))) {
                    MEmotion m = list.get(position);
                    PacifyrActivity.call_emotion = list.get(position);
                    updateUserEmotion(m);

                    holder.imageView.clearColorFilter();
                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            list.get(position).setSelect(true);
                        } else {
                            list.get(i).setSelect(false);
                        }
                    }
                    notifyDataSetChanged();
                }

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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLoading() {
        if (isValid(hud_progress)) hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoadingWait() {
        if (isValid(hud_progress)) hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoading(String message) {
        if (isValid(hud_progress)) hideProgress();
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("" + message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void hideProgress() {
        if (isValid(hud_progress)) if (hud_progress.isShowing()) hud_progress.dismiss();
    }

    public void showProgress() {
        if (isValid(hud_progress)) if (!hud_progress.isShowing()) hud_progress.show();
    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public void updateUserEmotion(final MEmotion emotion) {
        //call_emotion
        try {
            prefM = PrefManager.getInstance(context);
            String token = prefM.getAccessToken();
            String userid = prefM.getUserID();

            PostRegisterModel postRegisterModel = new PostRegisterModel(emotion.getId());
            Gson gson = new Gson();
            String json = gson.toJson(postRegisterModel);
            String url = GLOBAL_URL + "user/editprofile/" + userid;
            if (isValid(postRegisterModel)) {
                com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (isValid(result)) {
                                final JSONObject jsonObject = new JSONObject(result.toString());
                                String js = result.toString();
                                String phone = jsonObject.getString("phoneNumber");
                                if (isValid(phone)) {
                                    EventBus.getDefault().post(new CallChangeEmotion(emotion));
                                    /*Intent i=new Intent(context,CallActivity.class);
                                    ((Activity) context).startActivity(i);*/
                                    ((Activity) context).finish();
                                } else {
                                    Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception eee) {
                            Toast.makeText(context, "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                            eee.printStackTrace();
                        }
                    }
                };
                Ion.with(context).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
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


   /* public void updateUserEmotion(final MEmotion emotion){
        //call_emotion

        prefM = PrefManager.getInstance(context);
        String token = prefM.getAccessToken();
        String userid = prefM.getUserID();
        try {


            JSONObject updateEmotion=new JSONObject();
            updateEmotion.putOpt("emotionId", emotion.getId());
            Gson gson = new Gson();
            String json = gson.toJson(updateEmotion);
            String url = GLOBAL_URL + "user/editprofile/" + userid;
            if (isValid(updateEmotion)) {
                com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (isValid(result)) {
                                final JSONObject jsonObject = new JSONObject(result.toString());
                                String js = result.toString();
                                String phone = jsonObject.getString("phoneNumber");
                                if (isValid(phone)) {

                                    postEventOnMainThread(new CallChangeEmotion(emotion));
                                    Toast.makeText(context, "Updated emotion", Toast.LENGTH_LONG).show();
                                    ((Activity)context).onBackPressed();
                                } else {
                                    Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception eee) {
                            Toast.makeText(context, "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                            eee.printStackTrace();
                        }
                    }
                };
                Ion.with(context).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }*/
   public void setSelected(String current_emotion) {
       if (this.list != null && this.list.size() > 0) {
           for (int i=0;i<list.size();i++)
           {
               if (list.get(i).getId().equals(current_emotion))
               {
                   this.list.get(i).setSelect(true);
                   notifyDataSetChanged();
               }
           }

       }

   }

}