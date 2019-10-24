package com.pacifyr.omhealthcare.events.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
import com.pacifyr.omhealthcare.events.activity.MyInterviewsActvity;
import com.pacifyr.omhealthcare.events.fragment.OffersDetailFragment;
import com.pacifyr.omhealthcare.model.InterviewModel;
import com.pacifyr.omhealthcare.model.ModelOffer;
import com.pacifyr.omhealthcare.utils.Utility;

import java.io.Serializable;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class InterViewsListAdapter extends RecyclerView.Adapter<InterViewsListAdapter.MyViewHolder> {

    List<InterviewModel.Result> list;
    Context context;


    public InterViewsListAdapter(Context context, List<InterviewModel.Result> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_interview_list, parent, false);

        return new MyViewHolder(itemView);
    }

    public InterviewModel.Result getItem(int position) {
        InterviewModel.Result results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final InterviewModel.Result object = getItem(position);
        if (isValid(object)) {


            String actualDate = "";
            String serverDate = "";
            if (isValid(object.getEvent().getDate()))
                if (isValid(object.getEvent().getDate().get(0)))
                    serverDate = object.getEvent().getDate().get(0);
            final String Display_date = Utility.getDateFormatOneWithYear(serverDate);

            if (isValid(Display_date))
                holder.offer_date.setText(Display_date);


            if (isValid(object.getInterviewStatus())) {
                holder.interview_status.setText(object.getInterviewStatus().toString());
            }


            if (isValid(object.getEmployerPosition())) {
                if (isValid(object.getEmployerPosition().getTitle())) {
                    holder.opening_title.setText(object.getEmployerPosition().getTitle());
                }

                if (isValid(object.getEmployerPosition().getLocation())) {
                    holder.txtv_event_address.setText(object.getEmployerPosition().getLocation());
                }
            }


            if (isValid(object.getEmployer())) {
                if (isValid(object.getEmployer().getImage())) {
                    Glide.with(context)
                            .load(GLOBAL_IMAGE_URL + object.getEmployer().getImage())
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
                            .error(R.drawable.ic_error_white)
                            .dontTransform()
                            .into(holder.company_logo);
                }

                if (isValid(object.getEmployer().getName())) {
                    if (isValid(object.getEmployer().getName()))
                        holder.employer_title.setText(object.getEmployer().getName());
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                   /* Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) object);
                    bundle.putString("date", Display_date);

                    Fragment mOffersDetailFragment = new OffersDetailFragment();
                    mOffersDetailFragment.setArguments(bundle);
                    ((MyInterviewsActvity) context).ShowOffersFragments(mOffersDetailFragment);
*/
                }
            });
        } else {
            return;
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
        public TextView opening_title, employer_title, offer_date, interview_status, txtv_event_address;
        public ImageView company_logo;

        public MyViewHolder(View view) {
            super(view);
            opening_title = view.findViewById(R.id.opening_title);
            employer_title = view.findViewById(R.id.employer_title);
            offer_date = view.findViewById(R.id.offer_date);
            company_logo = view.findViewById(R.id.company_logo);
            interview_status = view.findViewById(R.id.interview_status);
            txtv_event_address = view.findViewById(R.id.txtv_event_address);
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
