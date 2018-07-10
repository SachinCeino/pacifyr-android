package com.pacifyr.omhealthcare.adapter;

/**
 * Created by Samsad on 19/July/2017.
 * samsad@ceinotech.com
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceino.utilitylibrary.CustomFontTextView;
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
    public static EmotionClickkListener emotionClickkListener;

    public static void addListener(EmotionClickkListener listener) {
        emotionClickkListener= listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CircleImageView circleImageView;
        public com.ceino.utilitylibrary.CustomFontTextView pac_name;

        public MyViewHolder(View view) {
            super(view);
            circleImageView=(CircleImageView) view.findViewById(R.id.circle_image);
            pac_name=(CustomFontTextView) view.findViewById(R.id.pac_name);
            /*title = (TextView) view.findViewById(R.id
            .title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);*/
        }
    }

/*
    public SpecialistAdapter(List<ModelSpecialist.Results> specialList) {
        this.specialList = specialList;
    }*/

    public SpecialistAdapter(List<ModelSpecialist.Results> specialList, Context context) {
        this.specialList = specialList;
        this.context=context;
    }

    @Override
    public SpecialistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.pacifyr_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SpecialistAdapter.MyViewHolder holder, final int position) {

        /*ModelSpecialist.Results[] special = specialList.get(position).getResults();*/
        String imgUrl=GLOBAL_IMAGE_URL+specialList.get(position).getImage();
        holder.pac_name.setText(specialList.get(position).getTitle());
        Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_image)).into(holder.circleImageView);

Log.i("selectttttt","s:  "+specialList.get(position).isSelect());
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
                Log.i("eeemmmm1","e: "+specialList.size());
                for (int i=0;i<specialList.size();i++)
                {
                    Log.i("selectttttt1","s:  "+specialList.get(position).isSelect());
                    Log.i("eeemmmm12","e: "+specialList.get(position).getTitle());
                    customerEmotion=specialList.get(position).getTitle();
                    if (i==position) {
                        Log.i("eeemmmm123","e: "+specialList.get(position).getTitle());
                        specialList.get(position).setSelect(true);
                        holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        holder.circleImageView.setBorderWidth(3);
                        emotionClickkListener.OnEmotionClickkListener();
                        //customerEmotion=specialList.get(position).getTitle();
                        //holder.emotionIMG.clearColorFilter();}
                    }

                    else{
                        Log.i("selectttttt2","s:  "+specialList.get(position).isSelect());
                        specialList.get(i).setSelect(false);
                        holder.circleImageView.setBorderWidth(0);
                    Log.i("eeemmmm1234","e: "+specialList.get(position).getTitle());}

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
