package com.pacifyr.client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.model.SessionPacks;
import com.pacifyr.client.utils.Utility;

import java.util.List;

public class SessionPackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SessionPacks.Result> items;
    private final BuyListener buyListener;

    public SessionPackAdapter(List<SessionPacks.Result> items, BuyListener buyListener) {
        this.items = items;
        this.buyListener = buyListener;
    }

    public void setItems(List<SessionPacks.Result> items, boolean first) {
        if (first) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            this.items.addAll(items);
            notifyItemInserted(getItemCount());
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_packs_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.label_name.setText(items.get(holder.getAdapterPosition()).getName());
        itemHolder.label_description.setText(items.get(holder.getAdapterPosition()).getDescription());
        itemHolder.value_no_of_sessions.setText(items.get(holder.getAdapterPosition()).getNumberOfSessions() + " sessions");
        itemHolder.value_price.setText(Utility.getPriceSign(holder.itemView.getContext()) + items.get(holder.getAdapterPosition()).getPrice() + "");
        itemHolder.value_duration.setText(items.get(holder.getAdapterPosition()).getSlotInterval() + "mins / session");
        if (items.get(holder.getAdapterPosition()).getExpired()) {
            itemHolder.value_expired.setVisibility(View.VISIBLE);
        } else {
            itemHolder.value_expired.setVisibility(View.GONE);
        }
        itemHolder.actionBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyListener != null) {
                    buyListener.onBuy(items.get(itemHolder.getAdapterPosition()).getId(),
                            items.get(itemHolder.getAdapterPosition()).getSubscribed());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface BuyListener {
        void onBuy(String sessionPackId, boolean subscribed);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {


        private final View actionBuy;
        private final TextView value_price;
        private final TextView value_duration;
        private final TextView value_no_of_sessions;
        private final TextView label_description;
        private final TextView label_name;
        private final TextView value_expired;

        public ItemHolder(View itemView) {
            super(itemView);
            actionBuy = itemView.findViewById(R.id.action_buy);
            value_price = itemView.findViewById(R.id.value_price);
            value_duration = itemView.findViewById(R.id.value_duration);
            value_no_of_sessions = itemView.findViewById(R.id.value_no_of_sessions);
            label_description = itemView.findViewById(R.id.label_description);
            label_name = itemView.findViewById(R.id.label_name);
            value_expired = itemView.findViewById(R.id.value_expired);
        }
    }
}
