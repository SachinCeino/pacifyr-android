package com.pacifyr.client.twiliochat;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.twilio.conversations.Message;

public class TwilioMessagesAdapter extends RecyclerView.Adapter<TwilioMessagesAdapter.ViewHolder> {


    QuickstartConversationsManager quickstartConversationsManager;
    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView messageTextView;

        ViewHolder(TextView textView) {
            super(textView);
            messageTextView = textView;
        }
    }

    TwilioMessagesAdapter(QuickstartConversationsManager quickstartConversationsManager) {
    this.quickstartConversationsManager=quickstartConversationsManager;
    }

    @NonNull
    @Override
    public TwilioMessagesAdapter
            .ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        TextView messageTextView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_text_view, parent, false);
        return new ViewHolder(messageTextView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = quickstartConversationsManager.getMessages().get(position);
        String messageText = String.format("%s: %s", message.getAuthor(), message.getMessageBody());
        holder.messageTextView.setText(messageText);
    }

    @Override
    public int getItemCount() {
        return quickstartConversationsManager.getMessages().size();
    }
}