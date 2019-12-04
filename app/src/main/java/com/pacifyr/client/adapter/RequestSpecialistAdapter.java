package com.pacifyr.client.adapter;

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

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyr.client.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.sign.PacifyrRequestAccessActivity.specialList;


public class RequestSpecialistAdapter extends RecyclerView.Adapter<RequestSpecialistAdapter.MyViewHolder>{


    Context context;
    public static EmotionClickkListenerRequest emotionClickkListener;

    public static void addListener(EmotionClickkListenerRequest listener) {
        emotionClickkListener= listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CircleImageView circleImageView;
        public MainTextView pac_name;

        public MyViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.circle_image);
            pac_name = view.findViewById(R.id.pac_name);

        }
    }

    public RequestSpecialistAdapter(ArrayList<ModelSpecialist.Results> list, Context context) {
        specialList = list;
        this.context=context;
    }

    public RequestSpecialistAdapter( Context context) {
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_pacifyr_emotions, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*ModelSpecialist.Results[] special = specialList.get(position).getResults();*/
        String imgUrl=GLOBAL_IMAGE_URL+specialList.get(position).getImage();
        holder.pac_name.setText(specialList.get(position).getTitle());
        Picasso.with(context).load(imgUrl).placeholder(context.getResources().getDrawable(R.drawable.placeholder_image)).into(holder.circleImageView);


       /* if(specialList.get(position).isSelect())
        {
            holder.circleImageView.setBorderWidth(3);
        }
        else {
            holder.circleImageView.setBorderWidth(0);
        }*/


        holder.circleImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CircleImageView cimv = (CircleImageView) v;




                if (!specialList.get(position).isSelect()) {
                    specialList.get(position).setSelect(true);
                    cimv.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    holder.circleImageView.setBorderWidth(3);
                }else {
                    specialList.get(position).setSelect(false);
                    holder.circleImageView.setBorderWidth(0);
                }

                /*specialList.get(position).setSelect(! specialList.get(position).isSelect());
                holder.circleImageView.setBorderColor( specialList.get(position).isSelect() ? ContextCompat.getColor(context, R.color.colorPrimary) : Color.WHITE);
                holder.circleImageView.setBorderWidth( specialList.get(position).isSelect() ? 3 : 0);*/


                //notifyDataSetChanged();
               /* for (int i=0;i<specialList.size();i++)
                {
                    customerEmotion=specialList.get(position).getTitle();
                    if (i==position) {
                        specialList.get(position).setSelect(true);
                        holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        holder.circleImageView.setBorderWidth(3);
                        //emotionClickkListener.OnEmotionClickkListener();

                        //customerEmotion=specialList.get(position).getTitle();
                        //holder.emotionIMG.clearColorFilter();}
                    }
                    else
                        specialList.get(i).setSelect(false);

                }
                notifyDataSetChanged();*/
            }
        });

    }

    public interface EmotionClickkListenerRequest{
        void OnEmotionClickkListener();
    }

    @Override
    public int getItemCount() {
        return specialList.size();
    }
}
