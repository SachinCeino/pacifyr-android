package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.pacifyr.omhealthcare.R;

import java.util.ArrayList;

/**
 * Created by Samsad on 1/8/17.
 */
    public class SpecilaListProfileAdapter extends RecyclerView.Adapter<SpecilaListProfileAdapter.SpecialViewHolder> {

        ArrayList<String> planetList;
        String[] spec;

        public SpecilaListProfileAdapter(ArrayList<String> planetList, Context context) {
            this.planetList = planetList;
        }

    public SpecilaListProfileAdapter(String[] spec, Context applicationContext) {
            this.spec=spec;
    }

    @Override
        public SpecialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.special_list_item,parent,false);
            SpecialViewHolder viewHolder=new SpecialViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SpecialViewHolder holder, int position) {
            //holder.image.setImageResource(R.drawable.planetimage);
            holder.text.setText(TextUtils.join(", ",spec));
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public static class SpecialViewHolder extends RecyclerView.ViewHolder{

            protected ImageView image;
            protected MainTextView text;

            public SpecialViewHolder(View itemView) {
                super(itemView);
                //image= (ImageView) itemView.findViewById(R.id.image_id);
                text = itemView.findViewById(R.id.text_id);
            }
        }
    }