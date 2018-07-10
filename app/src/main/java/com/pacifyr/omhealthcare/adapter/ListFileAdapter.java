package com.pacifyr.omhealthcare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.model.ModelPacifyrDocuments;

import java.util.ArrayList;

/**
 * Created by ceino on 18/5/18.
 */

public class ListFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    ArrayList<ModelPacifyrDocuments> fileList;
    Context mContext;

    public ListFileAdapter(ArrayList<ModelPacifyrDocuments> fileList, Context context) {
        this.fileList=fileList;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_file_choser_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        if(fileList.get(position).getFileType().equals("image")) {
            Bitmap bitmap = (Bitmap) fileList.get(position).getBitmap();
            viewHolder.fileImv.setImageBitmap(bitmap);
        }else{
            viewHolder.fileImv.setImageDrawable(mContext.getDrawable(R.drawable.ic_pdf));
        }

        viewHolder.closeImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout fileLlay;
        private ImageView closeImv;
        private ImageView fileImv;
        private CustomFontTextView fileNameCtxv;

        public ViewHolder(View itemView) {
            super(itemView);
            fileLlay = (RelativeLayout) itemView.findViewById(R.id.file_llay);
            closeImv = (ImageView) itemView.findViewById(R.id.close_imv);
            fileImv = (ImageView) itemView.findViewById(R.id.file_imv);
            fileNameCtxv = (CustomFontTextView) itemView.findViewById(R.id.file_name_ctxv);
        }
    }
}
