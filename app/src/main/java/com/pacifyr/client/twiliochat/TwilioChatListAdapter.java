package com.pacifyr.client.twiliochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.squareup.picasso.Picasso;
import com.twilio.conversations.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;

public class TwilioChatListAdapter extends RecyclerView.Adapter<TwilioChatListAdapter.ViewHolder> {

    private Context mContext;
    private List<TwilioConversationCreate> results;

    public TwilioChatListAdapter(Context mContext, List<TwilioConversationCreate> results) {
        this.mContext=mContext;
        this.results=results;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mName;
        CircleImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.mName);
            mImage=itemView.findViewById(R.id.mImage);;
        }
    }

    @NonNull
    @Override
    public TwilioChatListAdapter
            .ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_twilio_chat_participant_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TwilioConversationCreate object=results.get(position);
        if (isValid(object) && isValid(object.getPacifyr()))
        {
            holder.mName.setText(object.getPacifyr().getFirstName());
            if (isValid(object.getPacifyr().getImage()))
            {
                final String imgUrl=GLOBAL_IMAGE_URL+object.getPacifyr().getImage();
                //String imgUrl=
                Picasso.with(mContext).load(imgUrl).into(holder.mImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValid(object) && isValid(object.getTwilioConversationId()))
                        {
                            TwilioConversationActivity.start(mContext,object);
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public Boolean isValid(List list)
    {
        if(list!=null)
            return list.size() > 0;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            return list.size() >= position;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

}