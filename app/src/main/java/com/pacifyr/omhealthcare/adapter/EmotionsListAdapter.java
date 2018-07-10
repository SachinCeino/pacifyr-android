package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.omhealthcare.Fragment.LastSssionFragment.selEmotion;


public class EmotionsListAdapter extends RecyclerView.Adapter<EmotionsListAdapter.MyViewHolder>{

        private List<MEmotion> emotions;
        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public DeviceFitImageView emotionIMG;
            public CustomFontTextView emotion_title;
            public CircleImageView img_blue_bg;

            public MyViewHolder(View view) {
                super(view);
                emotionIMG=(DeviceFitImageView) view.findViewById(R.id.emotion_dimv);
                emotion_title=(CustomFontTextView) view.findViewById(R.id.emotion_title);
                img_blue_bg=(CircleImageView)view.findViewById(R.id.img_blue_bg);
            }
        }


        public EmotionsListAdapter(List<MEmotion> emotions) {
            this.emotions = emotions;

        }

        public EmotionsListAdapter(List<MEmotion> emotions, Context context) {
            this.emotions = emotions;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_emitions_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*ModelSpecialist.Results[] special = specialList.get(position).getResults();*/
            String imgUrl=GLOBAL_IMAGE_URL+emotions.get(position).getImage();
            holder.emotion_title.setText(emotions.get(position).getTitle());
            String tit=emotions.get(position).getTitle();
            //Picasso.with(context).load(imgUrl).into(holder.emotionIMG);
            Resources res = context.getResources();

            //final ImageView image = (ImageView) findViewById(R.id.imageId);
            final int newColor = res.getColor(R.color.grey_fit);
            final int trnsparentColor = res.getColor(R.color.transparent);
/*
            if(tit.equals("Angry")){
                holder.emotionIMG.setImageResource(R.drawable.anger);

            }
            else if(tit.equals("Anxious")){
                holder.emotionIMG.setImageResource(R.drawable.anxity);

            }
            else if(tit.equals("Envious")){
                holder.emotionIMG.setImageResource(R.drawable.envy);

            }
            else if(tit.equals("Fearful")){
                holder.emotionIMG.setImageResource(R.drawable.fear);

            }
            else if(tit.equals("Hateful")){
                holder.emotionIMG.setImageResource(R.drawable.hate);
            }
            else if(tit.equals("Hurt")){
                holder.emotionIMG.setImageResource(R.drawable.hurt);
            }
            else if(tit.equals("Lonely")){
                holder.emotionIMG.setImageResource(R.drawable.lonelines);
            }
            else if(tit.equals("Sad")){
                holder.emotionIMG.setImageResource(R.drawable.sad);
            }
            else if(tit.equals("Shocked"))
            {
                holder.emotionIMG.setImageResource(R.drawable.shocked);
            }
            else if(tit.equals("Amused")){
                holder.emotionIMG.setImageResource(R.drawable.amusement);
            }
            else if(tit.equals("Awed")){
                holder.emotionIMG.setImageResource(R.drawable.awe);
            }
            else if(tit.equals("Confident")){
                holder.emotionIMG.setImageResource(R.drawable.confidence);
            }
            else if(tit.equals("Grateful")){
                holder.emotionIMG.setImageResource(R.drawable.gratitude);
            }
            else if(tit.equals("Hopeful")){
                holder.emotionIMG.setImageResource(R.drawable.hope);
            }
            else if(tit.equals("Inspired")){
                holder.emotionIMG.setImageResource(R.drawable.inspiration);
            }
            else if(tit.equals("Interested")){
                holder.emotionIMG.setImageResource(R.drawable.interest);
            }
            else if(tit.equals("Joyful")){
                holder.emotionIMG.setImageResource(R.drawable.joy);

            }
            else if(tit.equals("Loved")){
                holder.emotionIMG.setImageResource(R.drawable.love);
            }
            else if(tit.equals("Proud")){
                holder.emotionIMG.setImageResource(R.drawable.pride);
            }
            else if(tit.equals("Relieved")){
                holder.emotionIMG.setImageResource(R.drawable.relife);
            }
            else if(tit.equals("Serene")){
                holder.emotionIMG.setImageResource(R.drawable.serenity);
            }*/
            Picasso.with(context).load(NetworkService.GLOBAL_IMAGE_URL + emotions.get(position).getImage()).placeholder(R.drawable.serenity).into(holder.emotionIMG);
            Log.i("iiiimmmmmmmmm","l: "+NetworkService.GLOBAL_IMAGE_URL + emotions.get(position).getImage());

            boolean b=emotions.get(position).getClicked();

            if(emotions.get(position).getClicked())
            {
             holder.emotionIMG.clearColorFilter();
                //holder.emotionIMG.setColorFilter(trnsparentColor, PorterDuff.Mode.SRC_ATOP);
                holder.img_blue_bg.setVisibility(View.VISIBLE);
                setSelectedIndex(position);

                final int nColor = res.getColor(R.color.white);
                holder.emotionIMG.setColorFilter(nColor, PorterDuff.Mode.SRC_ATOP);

//                ColorMatrix matrix = new ColorMatrix();
//                matrix.setSaturation(0);
//                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//                holder.emotionIMG.setColorFilter(filter);

            }
            else {
                holder.img_blue_bg.setVisibility(View.GONE);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.emotionIMG.setColorFilter(filter);

             /*   switch (position)
                {
                    case 0 :holder.emotionIMG.setColorFilter(R.color.grey, PorterDuff.Mode.SRC_IN); break;
                    case 1 :holder.emotionIMG.setColorFilter(R.color.grey_dark, PorterDuff.Mode.SRC_IN); break;
                    case 2 :holder.emotionIMG.setColorFilter(R.color.transparent_black_percent_90, PorterDuff.Mode.SRC_IN); break;
                    case 3 :holder.emotionIMG.setColorFilter(R.color.blue, PorterDuff.Mode.SRC_IN); break;
                    case 4 :holder.emotionIMG.setColorFilter(R.color.red, PorterDuff.Mode.SRC_IN); break;
                    case 5 :holder.emotionIMG.setColorFilter(R.color.black, PorterDuff.Mode.SRC_IN); break;
                    case 6 :holder.emotionIMG.setColorFilter(R.color.black_pacifyr, PorterDuff.Mode.SRC_IN); break;
                    case 7 :holder.emotionIMG.setColorFilter(R.color.black_translucent, PorterDuff.Mode.SRC_IN); break;
                    case 8 :holder.emotionIMG.setColorFilter(R.color.grey_transparent_10, PorterDuff.Mode.SRC_IN); break;
                    case 9 :holder.emotionIMG.setColorFilter(R.color.grey_transparent_50, PorterDuff.Mode.SRC_IN); break;
                    case 10 :holder.emotionIMG.setColorFilter(R.color.transparent_black_percent_15, PorterDuff.Mode.DST_IN); break;
                    case 11 :holder.emotionIMG.setColorFilter(R.color.transparent_black_percent_90, PorterDuff.Mode.DST_IN); break;
                    case 12 :holder.emotionIMG.setColorFilter(R.color.transparent_white_percent_85, PorterDuff.Mode.DST_IN); break;
                    case 13 :holder.emotionIMG.setColorFilter(R.color.transparent_white_percent_10, PorterDuff.Mode.DST_IN); break;
                    default :holder.emotionIMG.setColorFilter(R.color.transparent_white_percent_85, PorterDuff.Mode.DST_IN);break;

                }*/
              //  holder.emotionIMG.setColorFilter(newColor, PorterDuff.Mode.OVERLAY);
               // holder.emotionIMG.setColorFilter(R.color.transparent_white_percent_55, PorterDuff.Mode.MULTIPLY);
            }

            holder.emotionIMG.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i=0;i<emotions.size();i++)
                    {
                        if (i==position){
                            emotions.get(position).setClicked(true);
                            holder.emotionIMG.clearColorFilter();
                            selEmotion = emotions.get(position);
                            setSelectedIndex(position);
                        }
                        else
                            emotions.get(i).setClicked(false);

                    }
                    notifyDataSetChanged();
                }
            });


        /*holder.title.setText(special.getTitle());
        holder.genre.setText(special.getGenre());
        holder.year.setText(special.getDuration());*/

        }

    int selectedIndex = -1;
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public int getSelectedIndex(){
        return  selectedIndex;
    }
    public MEmotion getSelected(){
        return  getItem(selectedIndex);
    }
    public  MEmotion getItem (int position) {
     MEmotion results = null;
        if (isValid(emotions, position))
            results = emotions.get(position);
        return results;
    }

    public Boolean isValid(List list)
    {
        if(list!=null)
            if(list.size()>0)
                return  true;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            if(list.size()>=position)
                return  true;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        if(object!=null)
            return  true;
        return  false;

    }

    public Boolean isValid(String text) {
        if(text != null) if(!text.trim().equalsIgnoreCase("")) return true;
        return false;

    }
        @Override
        public int getItemCount() {
            return emotions.size();
        }
    }