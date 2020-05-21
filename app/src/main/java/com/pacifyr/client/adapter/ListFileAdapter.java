package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.R;
import com.pacifyr.client.model.ModelPacifyrDocuments;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ModelPacifyrDocuments object=fileList.get(position);
        if (object!=null )
        {
            if(object.getFileType().equals("image")) {

                if (object.getFileName()!=null)
                {
                    Glide.with(mContext)
                            .load(GLOBAL_IMAGE_URL+fileList.get(position).getFileName()+"")
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model,
                                                           Target<GlideDrawable> target, boolean isFirstResource) {
                                    e.printStackTrace();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model,
                                                               Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .dontTransform()
                            .into(viewHolder.fileImv);

                }

            }else{
                if (object.getFileType().equals("pdf")){
                    viewHolder.fileImv.setImageDrawable(mContext.getDrawable(R.drawable.pdfdoc2));}
            }

            viewHolder.closeImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileList.size()!=0)
                        fileList.remove(position);
                    notifyDataSetChanged();
                }
            });



            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileList.get(position).getFileName()!=null)
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GLOBAL_IMAGE_URL+fileList.get(position).getFileName()+""));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(browserIntent);
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout fileLlay;
        private ImageView closeImv;
        private ImageView fileImv;
        private MainTextView fileNameCtxv;

        public ViewHolder(View itemView) {
            super(itemView);
            fileLlay = itemView.findViewById(R.id.file_llay);
            closeImv = itemView.findViewById(R.id.close_imv);
            fileImv = itemView.findViewById(R.id.file_imv);
            fileNameCtxv = itemView.findViewById(R.id.file_name_ctxv);
        }
    }
}
