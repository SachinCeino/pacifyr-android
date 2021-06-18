package com.pacifyr.client.adapter;

/**
 * Created by Samsad on 19/July/2017.
 * samsad@ceinotech.com
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.ConnectToPacifyrActivity;
import com.pacifyr.client.model.ModelSpecialityByCategory;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.home.ListPacifyrScreenActivity.customerEmotion;
import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;


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
            holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
            holder.circleImageView.setBorderWidth(3);
            holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else {
            holder.circleImageView.setBorderWidth(0);
            holder.pac_name.setTextColor(context.getResources().getColor(R.color.grey_dark));
        }

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.circleImageView.setBorderWidth(0);
                boolean isSel=specalArray[position].isSelect();
                if(!isSel) {
                    holder.circleImageView.setBorderWidth(3);
                    holder.pac_name.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.circleImageView.setBorderColor(context.getResources().getColor(R.color.colorAccent));
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
                String _description = null ;

                    if (specalArray[position].getDescription() != null){
                        _description = specalArray[position].getDescription();
                    }
                    if (_description != null && ! _description.equalsIgnoreCase("")) {
                        for (int i=0;i<specalArray.length;i++)
                        {
                            if (i==position) {
                                specalArray[position].setSelect(true);
                                holder.circleImageView.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                holder.circleImageView.setBorderWidth(3);
                            }
                            else
                                specalArray[i].setSelect(false);

                        }
                        notifyDataSetChanged();
                        showAdditionalPopUpForKLE(position, holder, v);
                    }
                    else {
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



            }
        });


        /*holder.title.setText(special.getTitle());
        holder.genre.setText(special.getGenre());
        holder.year.setText(special.getDuration());*/

    }

    private void showAdditionalPopUpForKLE(int position, MyViewHolder holder, View v) {

        View myView = LayoutInflater.from(context).inflate(R.layout.popup_additional_for_kle, null);
        final TextView _TextView_Speciality_explanation = myView.findViewById(R.id.TextView_Speciality_explanation);
        final ImageView _ImageView_speciality = myView.findViewById(R.id.ImageView_speciality);
        final TextView _TextView_Speciality_Name = myView.findViewById(R.id.TextView_Speciality_Name);
        final TextView _TextView_Ok = myView.findViewById(R.id.TextView_Ok);

        _TextView_Speciality_explanation.setText(specalArray[position].getDescription()+"");
        _TextView_Speciality_Name.setText(specalArray[position].getTitle()+"");
        Picasso.with(context).load(GLOBAL_IMAGE_URL + specalArray[position].getImage()+"").into(_ImageView_speciality);


        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),R.style.Theme_AppCompat_Light_Dialog);
        alert.setView(myView);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        _TextView_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                dialog.dismiss();
            }
        });




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
        public TextView pac_name;

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
