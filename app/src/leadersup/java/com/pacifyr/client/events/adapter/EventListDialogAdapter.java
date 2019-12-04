package com.pacifyr.client.events.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
import com.pacifyr.client.R;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.model.CheckinEvent;
import com.pacifyr.client.model.InterviewModel;
import com.pacifyr.client.model.ModelEvent;
import com.pacifyr.client.quickblox.chat.utils.Consts;
import com.pacifyr.client.utils.Utility;

import java.util.List;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class EventListDialogAdapter extends RecyclerView.Adapter<EventListDialogAdapter.MyViewHolder> {

    List<InterviewModel.Result> list;
    Context context;


    public EventListDialogAdapter(Context context, List<InterviewModel.Result> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_event, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelEvent getItem(int position) {
        ModelEvent results = null;
        if (isValid(list, position))
            results = list.get(position).getEvent();
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelEvent object = getItem(position);
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
                    BusProvider.getInstance().post(new CheckinEvent(object.getId()));

                }
            });
        } else {
            return;
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
