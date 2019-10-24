package com.pacifyr.omhealthcare.events.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.events.fragment.AttendingEmployerFragment;
import com.pacifyr.omhealthcare.fragment.CheckInFragment;
import com.pacifyr.omhealthcare.model.ModelEvent;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;

import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class EmployerListCheckinAdapter extends RecyclerView.Adapter<EmployerListCheckinAdapter.MyViewHolder> {

    List<ModelEvent.Employer> list;
    Context context;


    public EmployerListCheckinAdapter(Context context, List<ModelEvent.Employer> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelEvent.Employer getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelEvent.Employer object = getItem(position);
        if (isValid(object)) {

            /** there is a list of dates comming, need to take the latest date from the datelist for display
             * first convert all date list to millisecndes
             * sort the list
             * pick the last item
             * then convert the milliseconds to date
             * then format the date how we are displaying.
             * **/
            holder.txtv_employer.setText(object.getName());
            inflateJobsView(holder.view_jobs, object.getPositions());
            Glide.with(context)
                    .load(GLOBAL_IMAGE_URL + object.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
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
                    .error(R.drawable.ic_error_white)
                    .dontTransform()
                    .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                    .into(holder.event_image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    Bundle args = new Bundle();
                    args.putSerializable("object", object);
                    AttendingEmployerFragment attendingEmployerFragment = new AttendingEmployerFragment();
                    attendingEmployerFragment.setArguments(args);
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.employers_container,
                            attendingEmployerFragment).addToBackStack(CheckInFragment.class.getSimpleName()).commit();

                }
            });
        } else {
            return;
        }

    }

    private void inflateJobsView(LinearLayout view_jobs, List<ModelEvent.Position> positions) {
        if (positions != null) {
            view_jobs.removeAllViewsInLayout();
            for (int i = 0; i < positions.size(); i++) {
                View jobView = LayoutInflater.from(context).inflate(R.layout.view_job_item, null);
                TextView jobText = jobView.findViewById(R.id.text_job);
                jobText.setText(positions.get(i).getTitle() + " (" + positions.get(i).getNumberOfPositions() + ")");
                view_jobs.addView(jobView);
            }
        }
    }


    public Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list, int position) {
        if (isValid(list))
            return list.size() >= position;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout view_jobs;
        public TextView txtv_employer;
        public ImageView event_image;

        public MyViewHolder(View view) {
            super(view);
            txtv_employer = view.findViewById(R.id.txtv_employername);
            view_jobs = view.findViewById(R.id.layout_jobs_container);
            event_image = view.findViewById(R.id.event_image);
        }
    }

    public class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }
}
