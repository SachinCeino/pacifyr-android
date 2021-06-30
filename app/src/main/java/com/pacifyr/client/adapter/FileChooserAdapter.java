package com.pacifyr.client.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.model.interfaces.AddFileClickListner;

import java.util.ArrayList;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

/**

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
                                .placeholder(context.getResources().getDrawable(R.drawable.placeholder_user))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        if (resource instanceof BitmapDrawable) {
                                            fHolder.fileImv.setImageBitmap(((BitmapDrawable) resource).getBitmap());
                                            documents.setBitmap(((BitmapDrawable) resource).getBitmap());
                                        }
                                        return false;
                                    }

                                })
                                .into(fHolder.fileImv);
                    }
                    else if (documents.getFileName()!=null)
                    {
                        Glide.with(context)
                                .load(GLOBAL_IMAGE_URL + documents.getFileName() + "")
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
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
        return 1;
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
