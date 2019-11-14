package com.pacifyr.omhealthcare.home.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.customview.MainTextView;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CertificationProfileAdapter;
import com.pacifyr.omhealthcare.adapter.EducationProfileAdapter;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.Utility;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Mani-Ceino on 12/29/2017.
 */

public class PacifyrAccountFragment extends BaseFragment {

    private LinearLayout profileLlay;
    private CircleImageView profileCimv;
    private MainTextView profileNameCtxv;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private MainTextView mailDtxv;
    private LinearLayout pnumberLlay;
    private MainTextView pnumberDtxv;
    private CardView aboutmeCard;
    private MainTextView aboutmeCtxv;
    private MainTextView priceCtxv;
    private MainTextView reviewCtxv;
    private MainTextView isonlineCtxv;
    private ImageView onlineOfflineImv;
    private RatingBar profileRating;
    private PrefManager prefM;
    private MainTextView snapshotCtxv;
    private MainTextView websiteCtxv;

    private CardView educationCard;


    private RecyclerView specialRecyclerView;
    private RecyclerView.Adapter specialAdapter;
    private RecyclerView.LayoutManager specialLayoutManager;

    private RecyclerView certificateRecyclerView;
    private RecyclerView.Adapter certificateAdapter;
    private RecyclerView.LayoutManager certificateLayoutManager;

    private RecyclerView educationRecyclerView;
    private RecyclerView.Adapter educationAdapter;
    private RecyclerView.LayoutManager educationLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_current_pacifyr_profile, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            prefM = PrefManager.getInstance(getContext());
            profileLlay = view.findViewById(R.id.profile_llay);
            profileCimv = view.findViewById(R.id.profile_cimv);
            profileNameCtxv = view.findViewById(R.id.profile_name_ctxv);
            mailCard = view.findViewById(R.id.mail_card);
            mailLlay = view.findViewById(R.id.mail_llay);
            mailDtxv = view.findViewById(R.id.mail_dtxv);
            pnumberLlay = view.findViewById(R.id.pnumber_llay);
            pnumberDtxv = view.findViewById(R.id.pnumber_dtxv);
            aboutmeCard = view.findViewById(R.id.aboutme_card);
            aboutmeCtxv = view.findViewById(R.id.aboutme_ctxv);
            snapshotCtxv = view.findViewById(R.id.snapshot_ctxv);
            websiteCtxv = view.findViewById(R.id.website_ctxv);
            educationCard = view.findViewById(R.id.education_card);

            educationRecyclerView = view.findViewById(R.id.education_rcv);

            priceCtxv = view.findViewById(R.id.price_ctxv);
            reviewCtxv = view.findViewById(R.id.review_ctxv);
            isonlineCtxv = view.findViewById(R.id.isonline_ctxv);
            onlineOfflineImv = view.findViewById(R.id.online_offline_imv);
            profileRating = view.findViewById(R.id.profile_rating);

            onSetValues(view);


        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onViewCreated exce " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        onlineOfflineImv = view.findViewById(R.id.online_offline_imv);
        isonlineCtxv = view.findViewById(R.id.isonline_ctxv);
        if(prefM.getIsOnline().equals("true")) {
            defSetText(isonlineCtxv, "ONLINE");
            onlineOfflineImv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online));
        }
        else{
            defSetText(isonlineCtxv, "OFFLINE");
            onlineOfflineImv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline));
        }
        showProgress();
        userDetailWeb(getUserID(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                try {
                    if (json != null) {
                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                        //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);

                        onSetValues(getView());

                    } else {
                        LogUtils.LOGD("base", "bAF userDetailWeb json null  : " + status.getMessage());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.LOGD("base", "bAF userDetailWeb callback exception : " + e.getMessage());
                }
            }
        });

    }

    @Override
    public void onSetValues(View view){
        try {
            if(isValid(NetworkService.mCurrentUser)) {
                if (isValid(NetworkService.mCurrentUser.getImageUrl())) {
                        Picasso.with(getActivity()).load(NetworkService.mCurrentUser.getImageUrl()).placeholder(R.drawable.placeholder_user).into(profileCimv);
                }
                ModelUserProfile user =NetworkService.mCurrentUser;
                defSetText(profileNameCtxv,NetworkService.mCurrentUser.getName());
                defSetText(mailDtxv,NetworkService.mCurrentUser.getEmail());
                defSetText(pnumberDtxv,NetworkService.mCurrentUser.getPhoneNumber());
                defSetText(snapshotCtxv,NetworkService.mCurrentUser.getDescription());
                String price= Utility.getPriceSign(getActivity())+NetworkService.mCurrentUser.getPrice()+" per hour";
                defSetText(priceCtxv,price);
                defSetText(reviewCtxv,user.getNumberOfReviews()+" reviews");
                if(prefM.getIsOnline().equals("true")) {
                    defSetText(isonlineCtxv, "ONLINE");
                    onlineOfflineImv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online));
                }
                else{
                    defSetText(isonlineCtxv, "OFFLINE");
                    onlineOfflineImv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline));
                }
                profileRating.setRating(Float.parseFloat(user.getRating()));
                websiteCtxv.setText(user.getWebsiteURL());


                ArrayList<ModelUserProfile.Education> edList = user.getEducationList();
                if (isValid(edList)) {
                    //education adapter

                    educationLayoutManager = new LinearLayoutManager(getActivity());
                    educationRecyclerView.setLayoutManager(educationLayoutManager);
                    educationAdapter = new EducationProfileAdapter(edList, getActivity());
                    educationRecyclerView.setAdapter(educationAdapter);
                } else {
                    educationCard.setVisibility(View.GONE);
                }


                ArrayList<ModelUserProfile.Certification> certificateList = user.getCertificationList();
                if (isValid(certificateList)) {
                    //certificate adapter
                    certificateRecyclerView = view.findViewById(R.id.certification_rcv);
                    certificateLayoutManager = new LinearLayoutManager(getActivity());
                    certificateRecyclerView.setLayoutManager(certificateLayoutManager);
                    certificateAdapter = new CertificationProfileAdapter(certificateList, getActivity());
                    certificateRecyclerView.setAdapter(certificateAdapter);
                } else {
                    CardView certificationCard = view.findViewById(R.id.certification_card);
                    certificationCard.setVisibility(View.GONE);
                }

            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onSetValues exce " + e.getMessage());
        }
    }

    public void onSetValues() throws Exception {
        try {
            if(isValid(NetworkService.mCurrentUser)) {
                if (isValid(NetworkService.mCurrentUser.getImageUrl())) {
                    Picasso.with(getActivity()).load(NetworkService.mCurrentUser.getImageUrl()).placeholder(R.drawable.placeholder_user).into(profileCimv);
                }
                defSetText(profileNameCtxv,NetworkService.mCurrentUser.getName());
                defSetText(mailDtxv,NetworkService.mCurrentUser.getEmail());
                defSetText(pnumberDtxv,NetworkService.mCurrentUser.getPhoneNumber());
                defSetText(aboutmeCtxv,NetworkService.mCurrentUser.getDescription());
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onSetValues exce " + e.getMessage());
        }
    }
    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if(event.isSuccess)
            onSetValues();
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onNotifyEvent exce " + e.getMessage());
        }

    }

}

