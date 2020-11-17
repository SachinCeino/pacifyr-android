package com.pacifyr.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utility.PdfViewer;

import java.io.File;
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
                            .load(GLOBAL_IMAGE_URL + fileList.get(position).getFileName() + "")
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
                    if (fileList.get(position).getFileName()!=null &&fileList.get(position).getFileType().equals("image"))
                    {
                        AttachmentImageActivity.start(mContext, GLOBAL_IMAGE_URL+fileList.get(position).getFileName());
                    }
                    else if (fileList.get(position).getFileName()!=null&&fileList.get(position).getFileType().equals("pdf"))
                    {
                        /*Intent i=new Intent(mContext, TermsOfUseActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("spurl",GLOBAL_IMAGE_URL+fileList.get(position).getFileName() );
                        mContext.startActivity(i);*/
                        loadPDF(GLOBAL_IMAGE_URL + fileList.get(position).getFileName());
                    }

                }
            });
        }


    }

    private void loadPDF(String url) {
        if (mContext instanceof PacifyrAppCompactActivity) {
            ((PacifyrAppCompactActivity) mContext).setLoading("Loading...");
            ((PacifyrAppCompactActivity) mContext).showProgress();
        }
        FileLoader.with(mContext)
                .load(url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("Documents", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        if (mContext instanceof PacifyrAppCompactActivity) {
                            ((PacifyrAppCompactActivity) mContext).hideProgress();
                        }
                        File loadedFile = response.getBody();
                        // do something with the file
                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(mContext, PdfViewer.class);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        if (mContext instanceof PacifyrAppCompactActivity) {
                            ((PacifyrAppCompactActivity) mContext).hideProgress();
                        }
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
