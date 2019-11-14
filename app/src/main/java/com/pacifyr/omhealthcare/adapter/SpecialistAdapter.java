package com.pacifyr.omhealthcare.adapter;

/**
 * Created by Samsad on 19/July/2017.
 * samsad@ceinotech.com
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyr.omhealthcare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.home.ListPacifyrScreenActivity.customerEmotion;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;


public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.MyViewHolder>{

    private List<ModelSpecialist.Results> specialList;
    Context context;
    EmotionClickkListener emotionClickkListener;

    public SpecialistAdapter(List<ModelSpecialist.Results> specialList, Context context) {
        this.specialList = specialList;
        this.context = context;

    }

    public void addListener(EmotionClickkListener listener) {
        emotionClickkListener = listener;
    }

    public void setSelected(int position) {
        if (this.specialList != null && this.specialList.size() > 0) {
            this.specialList.get(position).setSelect(true);
            notifyDataSetChanged();
            customerEmotion = this.specialList.get(position).getTitle();
            if (emotionClickkListener != null) {
                emotionClickkListener.OnEmotionClickkListener();
            }
        }
    }

    public void clear() {
        if (this.specialList != null) {
            this.specialList.clear();
            notifyDataSetChanged();
        }
    }

/*
    public SpecialistAdapter(List<ModelSpecialist.Results> specialList) {
        this.specialList = specialList;
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CircleImageView circleImageView;
        public MainTextView pac_name;

        public MyViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.circle_image);
            pac_name = view.findViewById(R.id.pac_name);
            /*title = (TextView) view.findViewById(R.id
            .title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);*/
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.pacifyr_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*ModelSpecialist.Results[] special = specialList.get(position).getResults();*/
        String imgUrl=GLOBAL_IMAGE_URL+specialList.get(position).getImage();
        holder.pac_name.setText(specialList.get(position).getTitle());
        Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_image)).into(holder.circleImageView);

        if(specialList.get(position).isSelect())
        {
            holder.circleImageView.setBorderWidth(3);
        }
        else {
            holder.circleImageView.setBorderWidth(0);
        }

       /* holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.circleImageView.setBorderWidth(0);
                boolean isSel=specialList.get(position).isSelect();
                if(!isSel) {
                    holder.circleImageView.setBorderWidth(3);
                    holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    specialList.get(position).setSelect(true);
                }
                else {
                    holder.circleImageView.setBorderWidth(0);
                    specialList.get(position).setSelect(false);
                }

                notifyDataSetChanged();
            }
        });*/

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i=0;i<specialList.size();i++)
                {
                    customerEmotion=specialList.get(position).getTitle();
                    if (i==position) {
                        specialList.get(position).setSelect(true);
                        holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        holder.circleImageView.setBorderWidth(3);
                        emotionClickkListener.OnEmotionClickkListener();
                        //customerEmotion=specialList.get(position).getTitle();
                        //holder.emotionIMG.clearColorFilter();}
                    }

                    else{
                        specialList.get(i).setSelect(false);
                        holder.circleImageView.setBorderWidth(0);
                    }

                }
                notifyDataSetChanged();
              //  notifyDataSetChanged();
            }
        });
        /*holder.title.setText(special.getTitle());
        holder.genre.setText(special.getGenre());
        holder.year.setText(special.getDuration());*/

    }

    public interface EmotionClickkListener{
        void OnEmotionClickkListener();
    }

    @Override
    public int getItemCount() {
        return specialList.size();
    }
}
