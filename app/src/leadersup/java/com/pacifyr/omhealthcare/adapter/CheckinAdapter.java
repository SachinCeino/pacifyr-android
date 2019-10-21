package com.pacifyr.omhealthcare.adapter;

import android.animation.Animator;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.model.CheckinModel;
import com.pacifyr.omhealthcare.model.MarkEvent;

import java.util.ArrayList;

/**
 * Created by Sachin Thampan on 18/10/19.
 */
public class CheckinAdapter extends RecyclerView.Adapter<CheckinAdapter.CheckinHolder> {

    private final ArrayList<CheckinModel> items;
    private final Context context;
    private int expandedItem;

    public CheckinAdapter(Context context, ArrayList<CheckinModel> items) {
        this.items = items;
        this.context = context;
        this.expandedItem = -1;
    }

    @NonNull
    @Override
    public CheckinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkin_item, parent, false);
        return new CheckinHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckinHolder holder, int position) {
        CheckinModel checkinModel = items.get(position);

        holder.slNo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_notcomplete));
        if (expandedItem == -1 && !checkinModel.isComplete()) {
            expandedItem = position;
        }
        if (checkinModel.isComplete()) {
            hideView(holder.button_complete);
            hideView(holder.button_cancel);
            hideView(holder.text_item_desc);
            hideView(holder.image_action);
            holder.slNo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_completed));
        } else {
            if (expandedItem == position) {
                showView(holder.button_complete);
                showView(holder.button_cancel);
                showView(holder.text_item_desc);
                showView(holder.image_action);
            }
        }
        if (expandedItem == position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));

        } else {
            hideView(holder.button_complete);
            hideView(holder.button_cancel);
            hideView(holder.text_item_desc);
            hideView(holder.image_action);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.transparent_black_1));
        }


        holder.text_item.setText(checkinModel.getTitle());
        holder.text_item_desc.setText(Html.fromHtml(checkinModel.getDescription(context)));
        holder.image_action.setImageDrawable(checkinModel.getImage(context));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedItem = position;
                notifyDataSetChanged();
            }
        });

        holder.button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkEvent markEvent = new MarkEvent(checkinModel.getEventId(), null,
                        checkinModel.getType());
                BusProvider.getInstance().post(markEvent);
            }
        });

        holder.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(position).setExpanded(false);
                expandedItem = -1;
                notifyDataSetChanged();
            }
        });
    }

    private void hideView(View view) {
        view.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void showView(View view) {
        view.animate().alpha(1.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public boolean isAllComplete() {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isComplete())
                return false;
        }
        return true;
    }

    public class CheckinHolder extends RecyclerView.ViewHolder {

        public final ImageView slNo;
        public final TextView text_item;
        private final Button button_complete;
        private final TextView text_item_desc;
        private final Button button_cancel;
        private final ImageView image_action;

        public CheckinHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.text_slno);
            text_item = itemView.findViewById(R.id.text_item);
            text_item_desc = itemView.findViewById(R.id.text_item_description);
            button_complete = itemView.findViewById(R.id.button_complete);
            button_cancel = itemView.findViewById(R.id.button_cancel);
            image_action = itemView.findViewById(R.id.image_action);
        }
    }
}
