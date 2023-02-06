package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.Payment.EditCardActivity;
import com.pacifyr.client.R;
import com.pacifyr.client.model.SubscriptionSavedCard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

//import com.pacifyr.pacifyrapp.Payment.EditCardActivity;

/**

 */

public class ListSavedCardAdapter extends RecyclerView.Adapter<ListSavedCardAdapter.ViewHolder> {

    public Context mContext;
    private LayoutInflater mInflater;
    private JSONArray jsonArray;

    private String type,code;
    List<SubscriptionSavedCard> mListCards;

    public ListSavedCardAdapter(Context context, List<SubscriptionSavedCard> mListCards) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mListCards=mListCards;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            SubscriptionSavedCard object=mListCards.get(position);

            holder.typeCtxv.setText(object.getCardBrand());
            holder.typeCtxv.setText("**************"+object.getCardLast4());
            holder.listLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(mContext, EditCardActivity.class);
                        i.putExtra("object",(Serializable) object);
                        mContext.startActivity(i);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public MainTextView typeCtxv, codeCtxv;
        public LinearLayout listLlay;

        public ViewHolder(View itemView) {
            super(itemView);
            typeCtxv = itemView.findViewById(R.id.card_type_ctxv);
            listLlay = itemView.findViewById(R.id.card_list_llay);

        }
    }
}
