package com.pacifyr.omhealthcare.sign;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyr.omhealthcare.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

    private List<ModelSpinnerData> dataSet;
    private Context mContext;


    public ServiceAdapter(List<ModelSpinnerData> data, Context context) {
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.adapter_item_layout_service, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelSpinnerData dataModel = dataSet.get(position);

        if (dataModel!=null)
        {
            holder.txtName.setText(dataModel.getReferredService());
            if (dataModel.isSelected())
                holder.mChekbox.setChecked(true);
            else
                holder.mChekbox.setChecked(false);



            holder.mChekbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (position == dataSet.size() - 1) {
                        for (int i = 0; i < dataSet.size() - 1; i++) {
                            dataSet.get(i).setSelected(false);
                            notifyItemChanged(i);
                        }
                        dataSet.get(dataSet.size() - 1).setSelected(true);
                        notifyItemChanged(dataSet.size() - 1);
                    } else {
                        dataSet.get(dataSet.size() - 1).setSelected(false);
                        notifyItemChanged(dataSet.size() - 1);
                        boolean checked = ((CheckBox) v).isChecked();
                        // Check which checkbox was clicked
                        if (checked)
                            dataModel.setSelected(true);
                        else
                            dataModel.setSelected(false);

                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mChekbox.performClick();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        MainTextView txtName;
        CheckBox mChekbox;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_service_name);
            mChekbox = itemView.findViewById(R.id.check_box);
        }
    }

}