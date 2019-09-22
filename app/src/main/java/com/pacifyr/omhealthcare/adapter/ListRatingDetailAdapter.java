package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.ceino.utilitylibrary.model.pacifyr.MRatingDetail;
import com.ceino.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.ceino.utilitylibrary.model.pacifyr.MSessionRating;
import com.pacifyr.omhealthcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListRatingDetailAdapter  extends RecyclerView.Adapter<ListRatingDetailAdapter.ViewHolder> {

    private List<MRatingDetail.Results> objects = new ArrayList<MRatingDetail.Results>();

    private LayoutInflater layoutInflater;

    List<MSessionDetailRoot> list;
    Context context;
    ArrayList<MEmotion> emotionList;

    String userid,token;

    public ListRatingDetailAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ListRatingDetailAdapter(Context applicationContext, ArrayList<MRatingDetail.Results> list, String userid, String token) {
        this.context = applicationContext;
        this.layoutInflater = LayoutInflater.from(applicationContext);
        objects=list;
        this.userid=userid;
        this.token=token;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rcv=null;
        View itemView;
        itemView = LayoutInflater.from(context).inflate(R.layout.list_rating_detail, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MSessionRating[] rating = objects.get(position).getSessionRatings();
        if(isValid(rating)) {
            MSessionRating pacifyrRating = new MSessionRating();
            MSessionRating customerRating = new MSessionRating();
            for (int i = 0; i < rating.length; i++) {
                if (rating[i].getType().equals("pacifyr")) {
                    pacifyrRating = rating[i];
                } else if (rating[i].getType().equals("customer")) {
                    customerRating = rating[i];
                }
            }
            if (isValid(pacifyrRating)) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                    SimpleDateFormat target = new SimpleDateFormat(" dd-MM-yyyy ", Locale.ENGLISH);
                    Date date = null;
                    date = df.parse(pacifyrRating.getUpdatedAt());
                    String time = target.format(date);
                    holder.dateCtxv.setText(time);
                    String ratings = customerRating.getRating();
                    if (isValid(customerRating.getRating()))
                        holder.raingBar.setRating(Float.parseFloat(customerRating.getRating()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String sessionType = objects.get(position).getSessionType();
            if(sessionType.equals("audio")) {
                holder.typeIcoImv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio_blue));
            }else if (sessionType.equals("video")) {
                holder.typeIcoImv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video_cam_blue));
            }
        }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
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

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private View view1;
        private RatingBar raingBar;
        private CustomFontTextView dateCtxv;
        private ImageView typeIcoImv;

        public ViewHolder(View itemView) {
            super(itemView);

            //view1 = (View) itemView.findViewById(R.id.view1);
            raingBar = itemView.findViewById(R.id.raing_bar);
            dateCtxv = itemView.findViewById(R.id.date_ctxv);
            typeIcoImv = itemView.findViewById(R.id.type_ico_imv);
        }
    }
}
