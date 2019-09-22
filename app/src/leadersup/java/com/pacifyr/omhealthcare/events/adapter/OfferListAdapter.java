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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.events.activity.OffersActivity;
import com.pacifyr.omhealthcare.events.fragment.OffersDetailFragment;
import com.pacifyr.omhealthcare.events.model.ModelOffer;
import com.pacifyr.omhealthcare.quickblox.chat.utils.Consts;
import com.pacifyr.omhealthcare.utils.Utility;

import java.io.Serializable;
import java.util.List;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_IMAGE_URL;

/**
 * Created by ceino on 14/12/17.
 */

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.MyViewHolder> {

    List<ModelOffer.Result> list;
    Context context;


    public OfferListAdapter(Context context, List<ModelOffer.Result> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_offer, parent, false);

        return new MyViewHolder(itemView);
    }

    public ModelOffer.Result getItem(int position) {
        ModelOffer.Result results = null;
        if (isValid(list, position))
            results = list.get(position);
        return results;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelOffer.Result object = getItem(position);
        if (isValid(object)) {


            String actualDate = "";
            final String Display_date = Utility.getDateFormatOneWithYear(object.getStartDate() + "");
            if (isValid(Display_date))
                holder.offer_date.setText("Starting date :" + Display_date);
            if (isValid(object.getEmployerPosition())) {
                if (isValid(object.getEmployerPosition().getTitle()))
                    holder.offer_title.setText(object.getEmployerPosition().getTitle());
            }

            if (isValid(object.getEmployer())) {
                if (isValid(object.getEmployer().getName()))
                    holder.company_title.setText(object.getEmployer().getName());
            }


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


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) object);
                    bundle.putString("date", Display_date);

                    Fragment mOffersDetailFragment = new OffersDetailFragment();
                    mOffersDetailFragment.setArguments(bundle);
                    ((OffersActivity) context).ShowOffersFragments(mOffersDetailFragment);

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
        public TextView company_title, offer_title, offer_date;
        public ImageView company_logo;

        public MyViewHolder(View view) {
            super(view);
            company_title = view.findViewById(R.id.company_title);
            offer_title = view.findViewById(R.id.offer_title);
            offer_date = view.findViewById(R.id.offer_date);
            company_logo = view.findViewById(R.id.company_logo);
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
