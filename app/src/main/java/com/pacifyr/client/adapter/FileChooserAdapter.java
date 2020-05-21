package com.pacifyr.client.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.R;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.model.interfaces.AddFileClickListner;
import com.pacifyr.client.utils.Utility;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 16/5/18.
 */

public class FileChooserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ModelPacifyrDocuments> list;
    private AddFileClickListner listner;

    public FileChooserAdapter(Context context, ArrayList<ModelPacifyrDocuments> documentFileList, AddFileClickListner listner) {
        this.list = documentFileList;
        this.context = context;
        this.listner=listner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder=null;
        View fileView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_file_choser_item, parent, false);
        if (viewType==0) {
            fileView = LayoutInflater.from(context).inflate(R.layout.list_file_chooser_default, parent, false);
            viewHolder = new ViewHolder2(fileView);
        }else {
            viewHolder = new FileChoserHolder(fileView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder!=null) {
            if (holder instanceof FileChoserHolder) {
                FileChoserHolder fHolder = (FileChoserHolder) holder;
                ModelPacifyrDocuments documents = list.get(position);

                if (documents.getFileType().equals("image")) {

                    Bitmap bitmap = documents.getBitmap();
                    if (bitmap!=null)
                        fHolder.fileImv.setImageBitmap(bitmap);
                    else if (documents.getUri()!=null)
                    {
                        Glide.with(context)
                                .load(documents.getUri())
                                .asBitmap()
                                .placeholder(context.getResources().getDrawable(R.drawable.placeholder_user))
                                .listener(new RequestListener<Uri, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        fHolder.fileImv.setImageBitmap(resource);
                                        documents.setBitmap(resource);
                                        documents.setFile(Utility.saveFileInCache(context,
                                                resource, "temp_profile_"+String.valueOf(position)+".jpg"));
                                        return false;
                                    }
                                })
                                .into(fHolder.fileImv);
                    }
                    else if (documents.getFileName()!=null)
                    {
                        Glide.with(context)
                                .load(GLOBAL_IMAGE_URL+documents.getFileName()+"")
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model,
                                                               Target<GlideDrawable> target, boolean isFirstResource) {
                                        //  e.printStackTrace();
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
                                .into(fHolder.fileImv);

                    }


                }

                fHolder.closeImv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (list.size()!=0)
                            list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }else if (holder instanceof ViewHolder2) {
                ViewHolder2 defaultHolder = (ViewHolder2) holder;
                defaultHolder.addDocumentImv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.onClick(v,position);
                    }
                });
            }

        }


    }



    @Override
    public int getItemCount() {
        return list.size()+1;
    }


    public class FileChoserHolder extends RecyclerView.ViewHolder {

        private RelativeLayout fileLlay;
        private ImageView closeImv;
        private ImageView fileImv;
        private MainTextView fileNameCtxv;

        public FileChoserHolder(View itemView) {
            super(itemView);
            fileLlay = itemView.findViewById(R.id.file_llay);
            closeImv = itemView.findViewById(R.id.close_imv);
            fileImv = itemView.findViewById(R.id.file_imv);
            fileNameCtxv = itemView.findViewById(R.id.file_name_ctxv);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView addDocumentImv;
        public ViewHolder2(View itemView) {
            super(itemView);
            addDocumentImv = itemView.findViewById(R.id.add_document_imv);

        }

    }

    @Override
    public int getItemViewType(int position) {
        int pos=11;
        if (position==list.size())
        {   pos = 0;
        }
        return pos;
    }

}
