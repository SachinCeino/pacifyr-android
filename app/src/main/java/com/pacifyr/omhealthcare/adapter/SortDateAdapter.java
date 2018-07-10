package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyr.omhealthcare.R;

import java.util.List;

/**
 * Created by ceino on 25/1/18.
 */

public class SortDateAdapter extends RecyclerView.Adapter<SortDateAdapter.MyViewHolder>{

    private List<String > data;
    Context context;

    public SortDateAdapter(Context context,List<String> strings) {
        this.data = strings;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sort_date, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titleCtxv.setText(data.get(position));
        holder.topLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomFontTextView titleCtxv;
        public LinearLayout topLlay;
        public RelativeLayout topRlay;

        public MyViewHolder(View view) {
            super(view);
            topRlay = (RelativeLayout) view.findViewById(R.id.top_rlay);
            topLlay=(LinearLayout) view.findViewById(R.id.top_llay);
            titleCtxv = (CustomFontTextView) view.findViewById(R.id.title_ctxv);
        }
    }
}
