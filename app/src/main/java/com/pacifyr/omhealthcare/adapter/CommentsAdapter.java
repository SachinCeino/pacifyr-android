package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.model.MComments;
import com.pacifyr.omhealthcare.model.interfaces.CommentClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceino on 14/12/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    List<MComments> list;
    Context context;
    CommentClick clicklistner;
    private boolean editable = true;
   /* public class MComments
    {
        public String item;
        public boolean isSelected=false;

        public MComments(String item, boolean isSelected) {
        this.item = item;
        this.isSelected = isSelected;
    }
        public MComments(String item) {
            this.item = item;
            this.isSelected = false;
        }
    }*/

    public CommentsAdapter(Context context) {
        list=new ArrayList<MComments>();
        list.add(new MComments("Excellent call"));
        list.add(new MComments("Good quality "));
        list.add(new MComments("Audio issues"));
        list.add(new MComments("Video issues"));
        list.add(new MComments("Other"));
        this.context=context;
    }

    public CommentsAdapter(List<MComments> list) {
        this.list = list;
    }

    public CommentsAdapter(Context context, CommentClick click) {
        clicklistner=click;
        list=new ArrayList<MComments>();
        list.add(new MComments("Excellent call"));
        list.add(new MComments("Good quality "));
        list.add(new MComments("Audio issues"));
        list.add(new MComments("Video issues"));
        list.add(new MComments("Other"));
        this.context=context;
    }
    public CommentsAdapter(Context context, CommentClick click,boolean isDefault) {
        clicklistner=click;
        list=new ArrayList<MComments>();
        list.add(new MComments("Excellent call"));
        list.add(new MComments("Good quality "));
        list.add(new MComments("Audio issues"));
        list.add(new MComments("Video issues"));
        list.add(new MComments("Other"));

        list.get(1).isSelected=true;
        this.context=context;
    }
    public CommentsAdapter(Context context,ArrayList<MComments> mComments, CommentClick click) {
        clicklistner=click;
        list=mComments;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_comments_item, parent, false);

        return new MyViewHolder(itemView);
    }
    public MComments getItem (int position) {
        MComments results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MComments mComment=getItem(position);
        if(isValid(mComment)) {
            holder.commentCtx.setText(mComment.item);
            holder.commentLlay.setSelected(list.get(position).isSelected);
            holder.commentCtx.setSelected(list.get(position).isSelected);
            if (editable) {
                holder.commentLlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < 5; i++) {
                            list.get(i).isSelected = false;
                        }

//                    list.get(position).isSelected = !list.get(position).isSelected;
//                    list.get(position).isSelected = true;

                        list.get(position).isSelected = true;

                        mComment.isSelected = list.get(position).isSelected;
                        if (isValid(clicklistner))
                            clicklistner.CommentCliked(mComment);
                 /*   if (mComment.item.equalsIgnoreCase("other")) {
                            if (isValid(clicklistner))
                                clicklistner.CommentCliked(mComment);
                        }*/


                        notifyDataSetChanged();
                /*holder.commentLlay.setBackground(context.getResources().getDrawable(R.drawable.layout_round_blueback));
                holder.commentLlay.setPadding(9,9,4,4);
                holder.commentCtx.setTextColor(context.getResources().getColor(R.color.white));*/
                    }
                });
            }
        }
        else
        {
            holder.commentLlay.setVisibility(View.GONE);
        }

    }
    public Boolean isValid(List list)
    {
        if(list!=null)
            return list.size() > 0;
        return  false;

    }

    public Boolean isValid(List list, int position)
    {
        if(isValid(list))
            return list.size() >= position;
        return  false;

    }

    public Boolean isValid(Object object)
    {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }
    public  ArrayList<String> getSelectedList()
    {
        ArrayList<String> checkedlist=new ArrayList<String>();
        try {
            if (isValid(list)) {
                for (MComments results : list) {
                    if (results.isSelected)
                        checkedlist.add(results.item);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return checkedlist;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomFontTextView commentCtx;
        public LinearLayout commentLlay;

        public MyViewHolder(View view) {
            super(view);
            commentCtx = view.findViewById(R.id.comment_ctxv);
            commentLlay = view.findViewById(R.id.comment_llay);
        }
    }
}
