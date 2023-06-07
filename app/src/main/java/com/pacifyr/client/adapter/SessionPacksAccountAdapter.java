package com.pacifyr.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.model.SessionPacks;

import java.util.List;

public class SessionPacksAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SessionPacks.Result> items;

    public SessionPacksAccountAdapter(List<SessionPacks.Result> items) {
        this.items = items;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_session_packs_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.label_name.setText(items.get(holder.getAdapterPosition()).getSessionPack().getName());
        itemHolder.label_provider.setText(items.get(holder.getAdapterPosition()).getProvider().getFirstName()
                + " " + items.get(holder.getAdapterPosition()).getProvider().getLastName());
        itemHolder.value_no_of_sessions.setText(items.get(holder.getAdapterPosition()).getNumberOfSessions() + "");
        itemHolder.value_rem_sessions.setText(items.get(holder.getAdapterPosition()).getNumberOfRemainingSessions() + "");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setButtonState(Context context, Button button, boolean enabled, int label) {
        if (!enabled) {
            button.setText(context.getString(label));
            button.setBackground(context.getDrawable(R.drawable.blue_round_square));
            button.setTextColor(context.getColor(R.color.white));
        } else {
            button.setText(context.getString(label));
            button.setBackground(context.getDrawable(R.drawable.blue_border_curved));
            button.setTextColor(context.getColor(R.color.colorPrimary));
        }
        button.setEnabled(enabled);
    }

    public interface BuyListener {
        void onBuy(String sessionPackId, boolean subscribed);

        void onSubscribe(String sessionPackId, boolean subscribed);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private final TextView value_no_of_sessions;
        private final TextView label_name;
        private final TextView label_provider;
        private final TextView value_rem_sessions;

        public ItemHolder(View itemView) {
            super(itemView);
            value_no_of_sessions = itemView.findViewById(R.id.value_sessions);
            label_name = itemView.findViewById(R.id.label_name);
            label_provider = itemView.findViewById(R.id.label_provider);
            value_rem_sessions = itemView.findViewById(R.id.value_rem_sessions);
        }
    }
}
