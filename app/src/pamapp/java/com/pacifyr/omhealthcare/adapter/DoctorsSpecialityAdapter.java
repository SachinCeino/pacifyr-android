package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.R;

import java.util.List;


import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyr.omhealthcare.home.DoctorsActivity;


public class DoctorsSpecialityAdapter extends RecyclerView.Adapter<DoctorsSpecialityAdapter.MyViewHolder> {

    private List<ModelSpecialist.Results> specialList;
    Context context;


    public interface SpecialityItemClick {
        void OnSpecialitySelected(ModelSpecialist.Results results);
    }

    SpecialityItemClick mSpecialityItemClick;

    public DoctorsSpecialityAdapter(List<ModelSpecialist.Results> specialList, Context context) {
        this.specialList = specialList;
        this.context = context;
    }

    public void addListener(SpecialityItemClick mSpecialityItemClick) {
        this.mSpecialityItemClick = mSpecialityItemClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.adapter_item_doctor_speciality, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelSpecialist.Results results = specialList.get(position);
        if (results != null)
            holder.titleSpeciality.setText(results.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpecialityItemClick.OnSpecialitySelected(results);
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleSpeciality;


        public MyViewHolder(View view) {
            super(view);
            titleSpeciality = view.findViewById(R.id.titleSpeciality);

        }

    }
}

