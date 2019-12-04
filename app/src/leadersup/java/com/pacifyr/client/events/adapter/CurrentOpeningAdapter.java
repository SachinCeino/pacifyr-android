package com.pacifyr.client.events.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pacifyr.client.R;
import com.pacifyr.client.events.activity.EventActivity;
import com.pacifyr.client.events.fragment.OpeningDetailFragment;
import com.pacifyr.client.model.ModelEvent;

import java.util.List;

/**
 * Created by ceino on 14/12/17.
 */

public class CurrentOpeningAdapter extends RecyclerView.Adapter<CurrentOpeningAdapter.MyViewHolder> {

    List<ModelEvent.Position> list;
    Context context;


    public CurrentOpeningAdapter(Context context, List<ModelEvent.Position> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_current_opening, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelEvent.Position getItem(int position) {
        ModelEvent.Position results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelEvent.Position object = getItem(position);
        if (isValid(object)) {
            if (isValid(object.getTitle()))
                holder.txtv_title.setText(object.getTitle());
            if (isValid(object.getNumberOfPositions()))
                holder.txtv_opening.setText(context.getResources().getString(R.string.number_of_opening) + object.getNumberOfPositions());
            if (isValid(object.getDescription()))
                holder.txtv_description.setText(object.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment mOpeningDetailFragment = new OpeningDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object", object);
                    mOpeningDetailFragment.setArguments(bundle);
                    ((EventActivity) context).ShowEventFragments(mOpeningDetailFragment);
                }
            });

        }

    }

    public Boolean isValid(List list) {
        if (list != null)
            if (list.size() > 0)
                return true;
        return false;

    }

    public Boolean isValid(List list, int position) {
        if (isValid(list))
            if (list.size() >= position)
                return true;
        return false;

    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }

    public Boolean isValid(String text) {
        if (text != null) if (!text.trim().equalsIgnoreCase("")) return true;
        return false;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtv_title, txtv_opening, txtv_description;


        public MyViewHolder(View view) {
            super(view);

            txtv_title = view.findViewById(R.id.txtv_title);
            txtv_opening = view.findViewById(R.id.txtv_opening);
            txtv_description = view.findViewById(R.id.txtv_description);

        }
    }

}
