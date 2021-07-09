package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.utils.Utility;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Message;

public class TwilioMessagesAdapter extends RecyclerView.Adapter<TwilioMessagesAdapter.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Context context;
    QuickstartConversationsManager quickstartConversationsManager;
    String userId;
    class ViewHolder extends RecyclerView.ViewHolder {

        final MainTextView messageText,tvTimeRight,tvTimeBottom;
        final ImageView ivImage;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = (MainTextView) itemView.findViewById(R.id.tv_message);
            tvTimeRight = (MainTextView) itemView.findViewById(R.id.tv_time_right);
            tvTimeBottom = (MainTextView) itemView.findViewById(R.id.tv_time_bottom);
            ivImage =  itemView.findViewById(R.id.iv_image);

        }


    }

    TwilioMessagesAdapter(QuickstartConversationsManager quickstartConversationsManager, String userId, Context context) {
    this.quickstartConversationsManager=quickstartConversationsManager;
    this.userId=userId;
    this.context=context;
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
       // holder.setIsRecyclable(false);
        Log.d("TAG", "re run");
        holder.ivImage.setVisibility(View.GONE);
        Message message = quickstartConversationsManager.getMessages().get(position);
       // String messageText = String.format("%s: %s", message.getAuthor(), message.getMessageBody());
        String messageText = message.getMessageBody();
        holder.messageText.setText(messageText);

        if (message.hasMedia()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            final String[] url = {null};

                message.getMediaContentTemporaryUrl(new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String mediaContentUrl) {
                        url[0] = mediaContentUrl;
                        if(holder.ivImage.getDrawable() == null) {
                            Log.d("TAG", mediaContentUrl);
                            Glide.with(App.getInstance())
                                    .load(mediaContentUrl)
                                    .skipMemoryCache(false)
                                    .signature(new ObjectKey(mediaContentUrl))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.ivImage);

                        }
                        }
                });

            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(url.length != 0){
                        TwilioConversationActivity.start(context,url[0]);
                    }

                }
            });
            Log.d("TAG", "mediaContentUrl"+message.getMediaFileName());
        }



        //Time
        if(messageText.length() > 15 || message.hasMedia()){
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