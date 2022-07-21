package com.pacifyr.client.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.home.ConnectToSinglePacifyrActivity;
import com.pacifyr.client.network.NetworkService;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**

 */

public class FavPRoviderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MUser[] preferredPacifyrs;
    private Context mContext;


    public FavPRoviderListAdapter(MUser[] preferredPacifyrs, Context mContext) {
        this.preferredPacifyrs = preferredPacifyrs;
        this.mContext=mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rcv=null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_fav_providers_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    private MUser getItem(int position) {
        return preferredPacifyrs[position];
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

            MyViewHolder mMyViewHolder = (MyViewHolder) holder;
            final MUser mObject  = getItem(position);
            setMyViewHolder(mMyViewHolder,mObject,position);






    }



    private void setMyViewHolder(MyViewHolder holder, MUser mObject, int position) {

        if (isValid(mObject)) {
                    try {
                        /*holder.TextView_ProviderName.setText(mObject.getFirstName()
                                + " " + mObject.getLastName());*/
                        holder.TextView_ProviderName.setText(mObject.getFirstName());
                        if (isValid(mObject.getImage()))
                            Picasso.with(mContext).load(NetworkService.GLOBAL_IMAGE_URL + mObject.getImage())
                                    .placeholder(R.drawable.ic_person).into(holder.ImagView_Provider);
                    }catch (Exception e)
                    {
                        Log.w("ERROR",String.valueOf(e));
                    }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectToSinglePacifyrActivity.start(mContext, mObject);
            }
        });

    }










    @Override
    public int getItemCount() {
        return preferredPacifyrs.length;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView ImagView_Provider;
        private TextView TextView_ProviderName;


        public MyViewHolder(View itemView) {
            super(itemView);
            ImagView_Provider = itemView.findViewById(R.id.ImagView_Provider);
            TextView_ProviderName = itemView.findViewById(R.id.TextView_ProviderName);


        }
    }


    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list, int position) {
        if (isValid(list))
            return list.size() >= position;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }




}
