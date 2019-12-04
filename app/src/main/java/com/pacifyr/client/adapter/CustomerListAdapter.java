package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPacifyrDashboard;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.event.ChatEvent;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.CustomerListActivity;
import com.pacifyr.client.model.MCustomerList;
import com.pacifyr.client.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;


/**
 * Created by ceino on 18/9/17.
 */

public class CustomerListAdapter extends  RecyclerView.Adapter<CustomerListAdapter.ViewHolder>{

    public static MUser custListUser;

    private final int VIEW_TYPE_LOADING = -1;
    public ArrayList<ModelPacifyrDashboard.Customers> resultlist;
    public int totalcount=0;
    ArrayList<MCustomerList.Results> lists;
    private Context context;
    private LayoutInflater layoutInflater;

    Activity activity;
    /*public  CustomerListAdapter (Context context, ArrayList<ModelPacifyrDashboard.Customers> resultlist ) {
        this.context = context;
        this.resultlist = resultlist;
        this.layoutInflater = LayoutInflater.from(context);
    }*/

/*    public  CustomerListAdapter (Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }*/
    public  CustomerListAdapter (Activity activity) {
        this.context = activity;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }
    public CustomerListAdapter(Activity activity,ArrayList<MCustomerList.Results> mCustomerslist) {
        this.context = activity;
        this.activity = activity;
        this.lists = mCustomerslist;
        this.layoutInflater = LayoutInflater.from(activity);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customers_list_item, parent, false);

        return new ViewHolder(itemView);
    }


    public MCustomerList.Results getItem(int position) {
        return lists.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.topLlay.setVisibility(View.GONE);
            final MCustomerList.Results results = getItem(position);
            if (isValid(results)) {
                //holder.customerNameCtxv.setText(resultlist.get(position).getFirstName());
                if (isValid(results.getUser())) {
                    holder.topLlay.setVisibility(View.VISIBLE);
                    holder.customerNameCtxv.setText(results.getUser().getName());
                    //holder.customerWalletCtxv.setText(resultlist.get(position).getPrice());
                    holder.customerWalletCtxv.setText(Utility.getPriceSign(context) + results.getPrecisedTotalSpending() + " Earnings to date.");
                    //holder.customerRating.setRating(Float.parseFloat(resultlist.get(position).getRating()));
                    //String rating =customerses[position].getRating();
                    /*if(isValid(rating)) {
                        Float ratin=Float.parseFloat(rating);
                        holder.customerRating.setRating(ratin);
                    }*/
                    holder.topLlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            custListUser = results.getUser();
                            CustomerDetailActivity.start(context,custListUser);
                        }
                    });
                    holder.chat_imv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* custListUser = results.getUser();
                            CustomerDetailActivity.start(context);*/

                            showProgress();
                            ( (CustomerListActivity) (activity)).callchatEventListner.onChatEventClick((new ChatEvent(custListUser, false)));
                            //    ConnectToPacifyrActivity.callchatEventListner.onChatEventClick(new ChatEvent(searchResults.get(position), false));
                            // postEventOnMainThread(new ChatEvent(searchResults.get(position),false));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgress();
                                }
                            },500);
                        }
                    });
                    final String imgUrl =  results.getUser().getImage();
                    if (!isValidhtml(imgUrl))
                        Picasso.with(context).load(GLOBAL_IMAGE_URL+imgUrl).placeholder(R.drawable.placeholder_user).into(holder.customerCimv);
                    else
                        Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).into(holder.customerCimv);

                }else{
                    holder.topLlay.setVisibility(View.GONE);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private CircleImageView customerCimv;
        private MainTextView customerNameCtxv;
        private MainTextView customerWalletCtxv;
        private LinearLayout topLlay;
        private ImageView chat_imv;

        //private RatingBar customerRating;

        public ViewHolder(View view) {
            super(view);
            customerCimv = view.findViewById(R.id.customer_cimv);
            chat_imv = view.findViewById(R.id.chat_imv);
            topLlay = view.findViewById(R.id.top_llay);
            customerNameCtxv = view.findViewById(R.id.customer_name_ctxv);
            customerWalletCtxv = view.findViewById(R.id.customer_wallet_ctxv);
            //customerRating = (RatingBar) view.findViewById(R.id.customer_rating);
            //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            //LayerDrawable stars = (LayerDrawable) customerRating.getProgressDrawable();
            //stars.getDrawable(2).setColorFilter(0xE2CE12, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public KProgressHUD hud_progress = null;
    // public KProgressHUD hud_status = null;
    public void setLoading() {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void setLoading(String message) {
        hud_progress = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }
    public void showProgress() {
        if(hud_progress != null) if(!hud_progress.isShowing()) hud_progress.show();
    }

    public void hideProgress() {
        if(hud_progress != null) if(hud_progress.isShowing()) hud_progress.dismiss();
    }
}
