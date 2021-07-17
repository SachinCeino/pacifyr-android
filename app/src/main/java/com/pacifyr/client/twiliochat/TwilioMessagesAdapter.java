package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.utils.Utility;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Message;

import java.util.ArrayList;

public class TwilioMessagesAdapter extends RecyclerView.Adapter<TwilioMessagesAdapter.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Context context;
    ArrayList<Message> messageList;
    String userId;
    String nextMsgDate ="";
    class ViewHolder extends RecyclerView.ViewHolder {

        final MainTextView messageText,tvTimeRight,tvTimeBottom,tvDate;
        final ImageView ivImage;
        final ProgressBar progressMessage;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = (MainTextView) itemView.findViewById(R.id.tv_message);
            tvTimeRight = (MainTextView) itemView.findViewById(R.id.tv_time_right);
            tvTimeBottom = (MainTextView) itemView.findViewById(R.id.tv_time_bottom);
            ivImage =  itemView.findViewById(R.id.iv_image);
            tvDate =  itemView.findViewById(R.id.tv_date);
            progressMessage =  itemView.findViewById(R.id.progress_message);

        }


    }

    TwilioMessagesAdapter(ArrayList<Message> messageList, String userId, Context context) {
    this.messageList=messageList;
    this.userId=userId;
    this.context=context;
    }
    @Override
    public int getItemViewType(int position) {

        Message message =  messageList.get(position);

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

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // holder.setIsRecyclable(false);
        Log.d("TAG", "re run");
        holder.ivImage.setVisibility(View.GONE);
        holder.tvDate.setVisibility(View.GONE);
        holder.progressMessage.setVisibility(View.GONE);



        Message message = messageList.get(position);


        /////// Date //////////
        String date = Utility.getFormattedDate(context,message.getDateCreated());
        holder.tvDate.setText(date);
        //Check last item
        if(0 == position){
            holder.tvDate.setVisibility(View.VISIBLE);
        } else {
            nextMsgDate = Utility.getFormattedDate(context,messageList.get(position - 1).getDateCreated());

            if(nextMsgDate.isEmpty() || !nextMsgDate.equals(date)){
                holder.tvDate.setVisibility(View.VISIBLE);
                nextMsgDate = date;
            }else {
                holder.tvDate.setVisibility(View.GONE);
            }
        }
        //---------------------//


        String messageText = message.getMessageBody();
        holder.messageText.setText(messageText);

        if (message.hasMedia()) {

            holder.ivImage.setVisibility(View.VISIBLE);


            final String[] url = {null};


                holder.progressMessage.setVisibility(View.VISIBLE);
                message.getMediaContentTemporaryUrl(new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String mediaContentUrl) {

                        url[0] = mediaContentUrl;

                        if(message.getMediaType().equals("application/pdf")){

                            Glide.with(context)
                                    .load(R.drawable.ic_pdf)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            holder.progressMessage.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            holder.progressMessage.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .skipMemoryCache(false)
                                    .override(100,100)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.ivImage);

                        }else{

                        if(holder.ivImage.getDrawable() == null) {
                            Log.d("TAG", mediaContentUrl);

                            Glide.with(context)
                                    .load(mediaContentUrl)

                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            holder.progressMessage.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            holder.progressMessage.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .skipMemoryCache(false)
                                    .signature(new ObjectKey(mediaContentUrl))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.ivImage);

                        }else {
                            holder.progressMessage.setVisibility(View.GONE);
                        }
                        }
                    }
                });

            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if (url.length != 0) {
                            if(message.getMediaType().equals("application/pdf")) {
                                ((TwilioConversationActivity) context).pdfReaderIntent(url[0], message.getMediaFileName());

                            } else {
                                TwilioConversationActivity.start(context, url[0]);
                            }

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
        return messageList.size();
    }
}