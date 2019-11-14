package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.pacifyr.omhealthcare.Payment.EditCardActivity;
import com.pacifyr.omhealthcare.R;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.pacifyr.pacifyrapp.Payment.EditCardActivity;

/**
 * Created by ceino on 8/12/17.
 */

public class ListSavedCardAdapter extends RecyclerView.Adapter<ListSavedCardAdapter.ViewHolder> {

    public Context mContext;
    private LayoutInflater mInflater;
    private JSONArray jsonArray;

    private String type,code;

    public ListSavedCardAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;

    }

    public ListSavedCardAdapter(Context applicationContext, JSONArray json) {
        this.mInflater = LayoutInflater.from(applicationContext);
        mContext = applicationContext;
        jsonArray=json;
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
            final JSONObject obj = jsonArray.getJSONObject(0);

            holder.typeCtxv.setText(obj.getString("cardBrand"));
            holder.typeCtxv.setText("**************"+obj.getString("cardLast4"));
            holder.listLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(mContext, EditCardActivity.class);
                        String expMonth = obj.getString("cardExpiryMonth");
                        String expYear = obj.getString("cardExpiryYear");
                        String expiry = expMonth+"/"+expYear;
                        i.putExtra("number", obj.getString("cardLast4"));
                        i.putExtra("expiry",expiry);
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
            codeCtxv = itemView.findViewById(R.id.card_code_ctxv);
            listLlay = itemView.findViewById(R.id.card_list_llay);

        }
    }
}
