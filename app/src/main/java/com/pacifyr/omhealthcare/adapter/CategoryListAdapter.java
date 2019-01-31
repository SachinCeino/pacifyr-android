package com.pacifyr.omhealthcare.adapter;

/**
 * Created by Samsad on 19/July/2017.
 * samsad@ceinotech.com
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.home.ConnectToPacifyrActivity;
import com.pacifyr.omhealthcare.model.ModelSpecialityByCategory;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.home.ListPacifyrScreenActivity.customerEmotion;
import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>{

    //private List<ModelSpecialityByCategory.Results> specialList;
    private ModelSpecialityByCategory.Results[] specalArray;
    Context context;

    public EmotionClickkListener categorySelectListener;
    public String selectedEmotion;
   /* public static EmotionClickkListener emotionClickkListener;

    public static void addListener(EmotionClickkListener listener) {
        emotionClickkListener= listener;
    }*/

    public void addListener(EmotionClickkListener listener) {
        categorySelectListener= listener;
    }

    public void setSelected(int position) {
        if (this.specalArray != null && this.specalArray.length > 0) {
            this.specalArray[position].setSelect(true);
            notifyDataSetChanged();
            customerEmotion = this.specalArray[position].getTitle();
            ConnectToPacifyrActivity.customerEmotion = this.specalArray[position].getTitle();
            if (categorySelectListener != null) {
                categorySelectListener.OnEmotionClickkListener();
            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*ModelSpecialist.Results[] special = specialList.get(position).getResults();*/
        String imgUrl=GLOBAL_IMAGE_URL+specalArray[position].getImage();
        holder.pac_name.setText(specalArray[position].getTitle());
        Picasso.with(context).load(imgUrl).into(holder.circleImageView);


        if(specalArray[position].isSelect())
        {
            holder.circleImageView.setBorderWidth(3);
        }
        else {
            holder.circleImageView.setBorderWidth(0);
        }

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.circleImageView.setBorderWidth(0);
                boolean isSel=specalArray[position].isSelect();
                if(!isSel) {
                    holder.circleImageView.setBorderWidth(3);
                    holder.pac_name.setTextColor(context.getResources().getColor(R.color.color_primary));
                    holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    specalArray[position].setSelect(true);
                }
                else {
                    holder.circleImageView.setBorderWidth(0);
                    holder.pac_name.setTextColor(context.getResources().getColor(R.color.grey_dark));
                    specalArray[position].setSelect(false);
                }

                notifyDataSetChanged();
            }
        });

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i=0;i<specalArray.length;i++)
                {
                    ConnectToPacifyrActivity.customerEmotion = specalArray[position].getTitle();
                    if (i==position) {
                        specalArray[position].setSelect(true);
                        holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        holder.circleImageView.setBorderWidth(3);
                        categorySelectListener.OnEmotionClickkListener();
                        //customerEmotion=specialList.get(position).getTitle();
                        //holder.emotionIMG.clearColorFilter();}
                    }
                    else
                        specalArray[i].setSelect(false);

                }
                notifyDataSetChanged();
            }
        });


        /*holder.title.setText(special.getTitle());
        holder.genre.setText(special.getGenre());
        holder.year.setText(special.getDuration());*/

    }

    public CategoryListAdapter(ModelSpecialityByCategory.Results[] specalArray, Context context) {
        this.specalArray = specalArray;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pacifyr_list, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CircleImageView circleImageView;
        public CustomFontTextView pac_name;

        public MyViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.circle_image);
            pac_name = view.findViewById(R.id.pac_name);
        }
    }

    public interface EmotionClickkListener{
        void OnEmotionClickkListener();
    }

    @Override
    public int getItemCount() {
        return specalArray.length;
    }
}
