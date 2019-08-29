package com.pacifyr.omhealthcare.events.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.fragment.AttendingEmployerFragment;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.quickblox.sample.core.utils.ResourceUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class AttendingCompanyListAdapter extends RecyclerView.Adapter<AttendingCompanyListAdapter.MyViewHolder> {

    List<ModelEvent.Employer> list;
    Context context;


    public AttendingCompanyListAdapter(Context context, List<ModelEvent.Employer> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_attending_companies, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelEvent.Employer getItem(int position) {
        ModelEvent.Employer results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelEvent.Employer object = getItem(position);

        holder.txtv_title.setText(object.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mAttendingEmployerFragment = new AttendingEmployerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", object);
                mAttendingEmployerFragment.setArguments(bundle);
                ((EventActivity) context).ShowEventFragments(mAttendingEmployerFragment);
            }
        });

        if (isValid(object.getImage()))
        {
            Glide.with(context)
                    .load(GLOBAL_IMAGE_URL + object.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            // progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.ic_launcher)
                    .dontTransform()
                    .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                    .into(holder.img_company_logo);

        }


    }

    public Boolean isValid(List list) {
        if (list != null)
            if (list.size() > 0)
                return true;
        return false;

    }

    public Boolean isValid(List list, int position) {
        if (isValid(list))
            if (list.size() >= position)
                return true;
        return false;

    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }

    public Boolean isValid(String text) {
        if (text != null) if (!text.trim().equalsIgnoreCase("")) return true;
        return false;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtv_title;
        public CircleImageView img_company_logo;


        public MyViewHolder(View view) {
            super(view);

            txtv_title = view.findViewById(R.id.txtv_title);
            img_company_logo=view.findViewById(R.id.img_company_logo);

        }
    }

}
