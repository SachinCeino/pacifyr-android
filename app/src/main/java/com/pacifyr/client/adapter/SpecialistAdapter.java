package com.pacifyr.client.adapter;

/**

 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyr.client.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.home.ListPacifyrScreenActivity.customerEmotion;
import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;


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

    public void clearSelection() {
        if (this.specialList != null && this.specialList.size() > 0) {

            for (int i=0;i<specialList.size();i++)
            {
                this.specialList.get(i).setSelect(false);
            }
            notifyDataSetChanged();
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
            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
            holder.circleImageView.setBorderWidth(3);
            holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else {
            holder.circleImageView.setBorderWidth(0);
            holder.pac_name.setTextColor(context.getResources().getColor(R.color.grey_dark));
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

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String _description = null ;

                if (specialList.get(position).getDescription() != null){
                    _description = specialList.get(position).getDescription();
                }
                if (_description != null && ! _description.equalsIgnoreCase("")) {
                    for (int i=0;i<specialList.size();i++)
                    {

                        if (i==position) {
                            specialList.get(position).setSelect(true);
                            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
                            holder.circleImageView.setBorderWidth(3);
                            holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        }

                        else{
                            specialList.get(i).setSelect(false);
                            holder.circleImageView.setBorderWidth(0);
                            holder.pac_name.setTextColor(context.getResources().getColor(R.color.grey_dark));
                        }

                    }
                    notifyDataSetChanged();
                    showAdditionalPopUpForKLE(position, holder, v);
                }
                else {
                    for (int i = 0; i < specialList.size(); i++) {
                        customerEmotion = specialList.get(position).getTitle();
                        if (i == position) {
                            specialList.get(position).setSelect(true);
                            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
                            holder.circleImageView.setBorderWidth(3);
                            holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
                            emotionClickkListener.OnEmotionClickkListener();
                            //customerEmotion=specialList.get(position).getTitle();
                            //holder.emotionIMG.clearColorFilter();}
                        } else {
                            specialList.get(i).setSelect(false);
                            holder.circleImageView.setBorderWidth(0);
                            holder.pac_name.setTextColor(context.getResources().getColor(R.color.grey_dark));
                        }

                    }
                    notifyDataSetChanged();
                }
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


    private void showAdditionalPopUpForKLE(int position, SpecialistAdapter.MyViewHolder holder, View v) {

        View myView = LayoutInflater.from(context).inflate(R.layout.popup_additional_for_kle, null);
        final TextView _TextView_Speciality_explanation = myView.findViewById(R.id.TextView_Speciality_explanation);
        final ImageView _ImageView_speciality = myView.findViewById(R.id.ImageView_speciality);
        final TextView _TextView_Speciality_Name = myView.findViewById(R.id.TextView_Speciality_Name);
        final TextView _TextView_Ok = myView.findViewById(R.id.TextView_Ok);

        _TextView_Speciality_explanation.setText(specialList.get(position).getDescription()+"");
        _TextView_Speciality_Name.setText(specialList.get(position).getTitle()+"");
        Picasso.with(context).load(GLOBAL_IMAGE_URL + specialList.get(position).getImage()+"").into(_ImageView_speciality);


        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),R.style.Theme_AppCompat_Light_Dialog);
        alert.setView(myView);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        _TextView_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<specialList.size();i++)
                {
                    customerEmotion=specialList.get(position).getTitle();
                    if (i==position) {
                        specialList.get(position).setSelect(true);
                        holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
                        holder.circleImageView.setBorderWidth(3);
                        holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
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
                dialog.dismiss();
            }
        });




    }

}
