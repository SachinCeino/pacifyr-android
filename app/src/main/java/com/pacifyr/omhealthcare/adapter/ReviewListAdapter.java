package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.model.pacifyr.MReviews;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.pacifyr.omhealthcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ceino on 15/2/18.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>{

    private ArrayList<MReviews.Results> list = new ArrayList<MReviews.Results>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ReviewListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ReviewListAdapter(Context context, ArrayList<MReviews.Results> lists) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = lists;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_reviw_item, parent, false);

        return new ReviewListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MReviews.Results result = getItem(position);
        if(isValid(result)) {
            try {
                holder.ratingBar.setRating(Float.parseFloat(result.getRating()));
                holder.qualityCtxv.setText(result.getComment());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                Date parsedTime = sdf.parse(result.getUpdatedAt());
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a");
                holder.timeCtxv.setText(sdf2.format(parsedTime));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.d("ReviewonBindViewHolder",e.getMessage());
            }
        }

    }

    public MReviews.Results getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    private void initializeViews(MUser user, MyViewHolder holder) {

    }

    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MainTextView timeCtxv;
        private RatingBar ratingBar;
        private MainTextView qualityCtxv;

        public MyViewHolder(View view) {
            super(view);
            timeCtxv = view.findViewById(R.id.time_ctxv);
            ratingBar = view.findViewById(R.id.rating_bar);
            qualityCtxv = view.findViewById(R.id.quality_ctxv);
        }
    }
}
