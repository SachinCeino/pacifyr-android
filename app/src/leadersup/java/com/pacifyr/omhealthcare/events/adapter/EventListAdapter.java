package com.pacifyr.omhealthcare.events.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.TransitionSet;
import android.support.v4.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.events.activity.EventActivity;
import com.pacifyr.omhealthcare.events.fragment.EventsDetailFragment;
import com.pacifyr.omhealthcare.events.model.ModelEvent;
import com.pacifyr.omhealthcare.model.MComments;
import com.pacifyr.omhealthcare.model.interfaces.CommentClick;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    List<ModelEvent.Result> list;
    Context context;


    public EventListAdapter(Context context, List<ModelEvent.Result> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_event, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelEvent.Result getItem(int position) {
        ModelEvent.Result results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelEvent.Result object = getItem(position);
        if (isValid(object)) {

            /** there is a list of dates comming, need to take the latest date from the datelist for display
             * first convert all date list to millisecndes
             * sort the list
             * pick the last item
             * then convert the milliseconds to date
             * then format the date how we are displaying.
             * **/
            String Display_date = "";
            String actualDate = "";
            if (isValid(Utility.convertDateToMilliseconds(object.getDate())))
                actualDate = Utility.convertDateToMilliseconds(object.getDate());
            Display_date = Utility.getDateAndTime(actualDate);
            if (isValid(Display_date))
                holder.txtv_event_date.setText(Display_date);
            holder.txtv_event_title.setText(object.getTitle());
            holder.txtv_event_address.setText(object.getLocation());
            holder.txtv_event_description.setText(object.getDescription());
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
                    .error(R.drawable.ic_error_white)
                    .dontTransform()
                    .override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL)
                    .into(holder.event_image);

            String finalActualDate = actualDate;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    double lan=((EventActivity) context).getmLatitude();
                    double lon=((EventActivity) context).getmLongitude();
                    if (lan!=0.0 && lon!=0.0)
                    {
                        holder.event_image.setTransitionName("event_transition");

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", object);
                        bundle.putString("actualDate", finalActualDate);

                        Fragment mEventsDetailFragment = new EventsDetailFragment();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mEventsDetailFragment.setSharedElementEnterTransition(new DetailsTransition());
                            mEventsDetailFragment.setEnterTransition(new Fade());
                            mEventsDetailFragment.setExitTransition(new Fade());
                            mEventsDetailFragment.setSharedElementReturnTransition(new DetailsTransition());
                        }


                        mEventsDetailFragment.setArguments(bundle);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.event_fragment_containe, mEventsDetailFragment)
                                .addSharedElement(holder.event_image, holder.event_image.getTransitionName())
                                .addToBackStack("new").commit();
                    }
                    else {
                        ((EventActivity) context).checkPremissionFineLocation();
                    }


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
        public TextView txtv_event_date, txtv_event_title, txtv_event_address, txtv_event_description;
        public ImageView event_image;

        public MyViewHolder(View view) {
            super(view);
            txtv_event_description = view.findViewById(R.id.txtv_event_description);
            txtv_event_address = view.findViewById(R.id.txtv_event_address);
            txtv_event_title = view.findViewById(R.id.txtv_event_title);
            txtv_event_date = view.findViewById(R.id.txtv_event_date);
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
