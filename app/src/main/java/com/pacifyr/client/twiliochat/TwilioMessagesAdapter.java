package com.pacifyr.client.twiliochat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.utils.Utility;
import com.twilio.conversations.Message;

public class TwilioMessagesAdapter extends RecyclerView.Adapter<TwilioMessagesAdapter.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    QuickstartConversationsManager quickstartConversationsManager;
    String userId;
    class ViewHolder extends RecyclerView.ViewHolder {

        final MainTextView messageText,tvTimeRight,tvTimeBottom;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = (MainTextView) itemView.findViewById(R.id.tv_message);
            tvTimeRight = (MainTextView) itemView.findViewById(R.id.tv_time_right);
            tvTimeBottom = (MainTextView) itemView.findViewById(R.id.tv_time_bottom);

        }
    }

    TwilioMessagesAdapter(QuickstartConversationsManager quickstartConversationsManager, String userId) {
    this.quickstartConversationsManager=quickstartConversationsManager;
    this.userId=userId;
    }
    @Override
    public int getItemViewType(int position) {

        Message message = quickstartConversationsManager.getMessages().get(position);

        if (message.getAuthor().equals(userId)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public TwilioMessagesAdapter
            .ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_message_send, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_message_receive, parent, false);
            return new ViewHolder(view);
        }

        return null;
        /*TextView messageTextView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message_send, parent, false);
        return new ViewHolder(messageTextView);*/
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = quickstartConversationsManager.getMessages().get(position);
       // String messageText = String.format("%s: %s", message.getAuthor(), message.getMessageBody());
        String messageText = message.getMessageBody();
        holder.messageText.setText(messageText);

        //Time
        if(messageText.length() > 15){
            holder.tvTimeRight.setVisibility(View.GONE);
            holder.tvTimeBottom.setVisibility(View.VISIBLE);
        }else {
            holder.tvTimeBottom.setVisibility(View.GONE);
            holder.tvTimeRight.setVisibility(View.VISIBLE);
        }
        ///////////Both time will be same/////
        String time= Utility.getDate(message.getDateCreated(),true);
        holder.tvTimeRight.setText(time);
        holder.tvTimeBottom.setText(time);

        ///////////////////////////


    }

    @Override
    public int getItemCount() {
        return quickstartConversationsManager.getMessages().size();
    }
}