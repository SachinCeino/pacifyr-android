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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.MessageMediaModel;
import com.pacifyr.client.utils.Utility;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Message;

import java.util.ArrayList;

public class TwilioMessagesAdapter extends RecyclerView.Adapter<TwilioMessagesAdapter.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_SENT_IMAGE = 2;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED_IMAGE = 4;
    Context context;
    ArrayList<MessageMediaModel> messageList;
    String userId;
    String nextMsgDate ="";
    class ViewHolder extends RecyclerView.ViewHolder {

        final MainTextView messageText,tvTimeRight,tvTimeBottom,tvDate;
        final ImageView ivFile, ivImage;
        final ProgressBar progressMessage;
        final CardView cardDate;
        final ConstraintLayout clBody;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = (MainTextView) itemView.findViewById(R.id.tv_message);
            tvTimeRight = (MainTextView) itemView.findViewById(R.id.tv_time_right);
            tvTimeBottom = (MainTextView) itemView.findViewById(R.id.tv_time_bottom);
            ivImage =  itemView.findViewById(R.id.iv_image);
            ivFile =  itemView.findViewById(R.id.iv_file);
            tvDate =  itemView.findViewById(R.id.tv_date);
            cardDate =  itemView.findViewById(R.id.card_date);
            progressMessage =  itemView.findViewById(R.id.progress_message);
            clBody =  itemView.findViewById(R.id.constraintLayout2);

        }


    }

    TwilioMessagesAdapter(ArrayList<MessageMediaModel> messageList, String userId, Context context) {
    this.messageList=messageList;
    this.userId=userId;
    this.context=context;
    }
    @Override
    public int getItemViewType(int position) {

        Message message =  messageList.get(position).message;

        if (message.getAuthor().equals(userId) && message.hasMedia()) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT_IMAGE;
        } else if(message.getAuthor().equals(userId)&& !message.hasMedia()) {
            return VIEW_TYPE_MESSAGE_SENT;
        }else if(!message.getAuthor().equals(userId)&& message.hasMedia()) {
            return VIEW_TYPE_MESSAGE_RECEIVED_IMAGE;
        }else if(!message.getAuthor().equals(userId)&& !message.hasMedia()){
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else  return VIEW_TYPE_MESSAGE_RECEIVED;

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
        } else if (viewType == VIEW_TYPE_MESSAGE_SENT_IMAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_message_send_image, parent, false);
            return new ViewHolder(view);
        }else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED_IMAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_message_receive_image, parent, false);
            return new ViewHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            //holder.setIsRecyclable(false);
            Log.d("TAG", "re run");
            holder.ivImage.setVisibility(View.GONE);
            holder.ivFile.setVisibility(View.GONE);
            holder.cardDate.setVisibility(View.GONE);
            holder.tvTimeRight.setVisibility(View.GONE);
            holder.tvTimeBottom.setVisibility(View.GONE);
            holder.progressMessage.setVisibility(View.GONE);


            Message message = messageList.get(position).message;


        ///////Background ///////
        if(message.getAuthor().equals(userId)) {
            if ((messageList.size() -1 )!= position) {
                String msgAuthor = message.getAuthor();
                String nextMsgAuthor = messageList.get(position + 1).message.getAuthor();
                String nxtDate = Utility.getFormattedDate(context, messageList.get(position + 1).message.getDateCreated());
                String msgDate = Utility.getFormattedDate(context, message.getDateCreated());
                if (msgAuthor.equals(nextMsgAuthor) && (nxtDate.equals(msgDate))) {
                    holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_send_bubble));
                } else{
                    holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_send_bubble_arrow));
                }
            } else {
                holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_send_bubble_arrow));

            }
        } else {
            if ((messageList.size() -1 )!= position) {
                String msgAuthor = message.getAuthor();
                String nextMsgAuthor = messageList.get(position + 1).message.getAuthor();
                String nxtDate = Utility.getFormattedDate(context, messageList.get(position + 1).message.getDateCreated());
                String msgDate = Utility.getFormattedDate(context, message.getDateCreated());
                if (msgAuthor.equals(nextMsgAuthor) && (nxtDate.equals(msgDate))) {
                    holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_receive_bubble));
                } else{
                    holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_receive_bubble_arrow));
                }
            } else {
                holder.clBody.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_bg_receive_bubble_arrow));

            }
        }

        //--------------------//


            /////// Date //////////
            String date = Utility.getFormattedDate(context, message.getDateCreated());
            holder.tvDate.setText(date);

            //Check last item
            if (0 == position) {
                holder.cardDate.setVisibility(View.VISIBLE);

            } else {
                nextMsgDate = Utility.getFormattedDate(context, messageList.get(position - 1).message.getDateCreated());

                if (nextMsgDate.isEmpty() || !nextMsgDate.equals(date)) {
                    holder.cardDate.setVisibility(View.VISIBLE);
                    nextMsgDate = date;

                } else {
                    holder.cardDate.setVisibility(View.GONE);

                }
            }
            //---------------------//





            String messageText = message.getMessageBody();
            holder.messageText.setText(messageText);

            if (message.hasMedia()) {
                holder.progressMessage.setVisibility(View.VISIBLE);
                String mediaUrl = messageList.get(position).mediaUrl;
                //Set view
                if (message.getMediaType().equals("application/pdf")) {
                    holder.ivImage.setVisibility(View.GONE);
                    holder.ivFile.setVisibility(View.VISIBLE);
                } else {
                    holder.ivFile.setVisibility(View.GONE);
                    holder.ivImage.setVisibility(View.VISIBLE);
                }


                if (Utility.isValid(mediaUrl)) {

                    if (message.getMediaType().equals("application/pdf")) {
                        holder.progressMessage.setVisibility(View.GONE);

                        holder.ivFile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!mediaUrl.isEmpty()) {
                                    ((TwilioConversationActivity) context).pdfReaderIntent(mediaUrl, message.getMediaFileName());
                                }

                            }
                        });
                    }else {

                        Glide.with(context)
                                .load(mediaUrl)
                                .transform(new CenterCrop(),new RoundedCorners(37))
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
                                //.override(w, h)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ivImage);


                        holder.ivImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!mediaUrl.isEmpty()) {
                                    if (message.getMediaType().equals("application/pdf")) {
                                        ((TwilioConversationActivity) context).pdfReaderIntent(mediaUrl, message.getMediaFileName());

                                    } else {
                                        TwilioConversationActivity.start(context, mediaUrl);
                                    }

                                }

                            }
                        });
                    }

                } else {
                    Glide.with(holder.ivImage).clear(holder.ivImage);

                    message.getMediaContentTemporaryUrl(new CallbackListener<String>() {
                        @Override
                        public void onSuccess(String mediaContentUrl) {

                            messageList.get(position).mediaUrl = mediaContentUrl;
                            notifyItemChanged(position);

                        }
                    });
                }

                Log.d("TAG", "mediaContentUrl" + message.getMediaFileName());
            } else {
                holder.ivImage.setVisibility(View.GONE);
                Glide.with(holder.ivImage).clear(holder.ivImage);
            }


            //Time
            if (messageText.length() > 15 || message.hasMedia()) {
                holder.tvTimeRight.setVisibility(View.GONE);
                holder.tvTimeBottom.setVisibility(View.VISIBLE);
            } else {
                holder.tvTimeBottom.setVisibility(View.GONE);
                holder.tvTimeRight.setVisibility(View.VISIBLE);
            }
            ///////////Both time will be same/////
            String time = Utility.getTime(message.getDateCreated());
            holder.tvTimeRight.setText(time);
            holder.tvTimeBottom.setText(time);

            ///////////////////////////

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}