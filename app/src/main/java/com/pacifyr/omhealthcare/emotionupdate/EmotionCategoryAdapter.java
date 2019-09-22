package com.pacifyr.omhealthcare.emotionupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.model.ModelSpecialityByEmotion;

import java.util.ArrayList;

public class EmotionCategoryAdapter extends RecyclerView.Adapter<EmotionCategoryAdapter.MyViewHolder>{


    private final String userID;
    private final String acessToken;
    private  ArrayList<ModelSpecialityByEmotion.Results> list;
    private final PrefManager prefM;
    private Context context;
    private String category;
    private String reason_forcall;

    public EmotionCategoryAdapter(Context context,ArrayList<ModelSpecialityByEmotion.Results> mListEmotions) {
        this.context=context;
        list=mListEmotions;
        prefM = PrefManager.getInstance(context);
        userID = prefM.getUserID();
        acessToken = prefM.getAccessToken();
        //customerRole = prefM.getCustomerRole();
        //reason_forcall=null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_emotion_detail_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.commentCtx.setText(list.get(position).getTitle());

        if (list.get(position).getClicked()){
            myViewHolder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
            myViewHolder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            myViewHolder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_bg));
            myViewHolder.commentCtx.setTextColor(context.getResources().getColor(R.color.black));
        }

        myViewHolder.commentLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewHolder.commentCtx.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
                myViewHolder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));
                for(int i=0;i<list.size();i++)
                {
                    if(i==position){
                        list.get(position).setClicked(true);
                    }
                    else{
                        list.get(i).setClicked(false);
                    }
                }
                notifyDataSetChanged();

                category = list.get(position).getTitle();
                Toast.makeText(context,category,Toast.LENGTH_LONG).show();
                //customerEmotion=list.get(position).getTitle();
                /*reason_forcall = list.get(position).getTitle();
                category = list.get(position).getTitle();
*/
                }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomFontTextView commentCtx;
        public LinearLayout commentLlay;

        public MyViewHolder(View view) {
            super(view);
            commentCtx= view.findViewById(R.id.comment_ctxv);
            commentLlay = view.findViewById(R.id.comment_llay);
        }
    }


    @Override
    public int getItemCount() {
        return 0;
    }

}
