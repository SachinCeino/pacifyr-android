package com.pacifyr.client.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;

import java.util.ArrayList;
import java.util.List;


public class RatingGraphAdapter extends RecyclerView.Adapter<RatingGraphAdapter.MyViewHolder> {

    Context context;
    int value;
    String[] colorsTxt;
    int pos=0;
    public RatingGraphAdapter(Context context,int value) {
        this.context = context;
        this.value=value;

        colorsTxt = context.getResources().getStringArray(R.array.colors);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.adapter_item_rating_graph, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.graphLaoyutColor.setBackgroundColor(Color.parseColor(colorsTxt[position]));

        if (pos==position)
        {
            holder.indicator.setImageDrawable(context.getResources().getDrawable(R.drawable.doctor_rating_marker));
            holder.indicator.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.indicator.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setMarker(int pos) {
        this.pos=pos;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView indicator;
        public RelativeLayout graphLaoyutColor;


        public MyViewHolder(View view) {
            super(view);
            indicator = view.findViewById(R.id.indicator);
            graphLaoyutColor = view.findViewById(R.id.graphLaoyutColor);

        }

    }
}

