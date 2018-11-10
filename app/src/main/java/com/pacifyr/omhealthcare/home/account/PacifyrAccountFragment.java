package com.pacifyr.omhealthcare.home.account;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
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
    private CustomFontTextView profileNameCtxv;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private CustomFontTextView mailDtxv;
    private LinearLayout pnumberLlay;
    private CustomFontTextView pnumberDtxv;
    private CardView aboutmeCard;
    private CustomFontTextView aboutmeCtxv;
    private CustomFontTextView priceCtxv;
    private CustomFontTextView reviewCtxv;
    private CustomFontTextView isonlineCtxv;
    private ImageView onlineOfflineImv;
    private RatingBar profileRating;
    private PrefManager prefM;
    private CustomFontTextView snapshotCtxv;
    private CustomFontTextView websiteCtxv;

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
            profileLlay = (LinearLayout) view.findViewById(R.id.profile_llay);
            profileCimv = (CircleImageView) view.findViewById(R.id.profile_cimv);
            profileNameCtxv = (CustomFontTextView) view.findViewById(R.id.profile_name_ctxv);
            mailCard = (CardView) view.findViewById(R.id.mail_card);
            mailLlay = (LinearLayout) view.findViewById(R.id.mail_llay);
            mailDtxv = (CustomFontTextView) view.findViewById(R.id.mail_dtxv);
            pnumberLlay = (LinearLayout) view.findViewById(R.id.pnumber_llay);
            pnumberDtxv = (CustomFontTextView) view.findViewById(R.id.pnumber_dtxv);
            aboutmeCard = (CardView) view.findViewById(R.id.aboutme_card);
            aboutmeCtxv = (CustomFontTextView) view.findViewById(R.id.aboutme_ctxv);
            snapshotCtxv = (CustomFontTextView) view.findViewById(R.id.snapshot_ctxv);
            websiteCtxv = (CustomFontTextView) view.findViewById(R.id.website_ctxv);
            educationCard = (CardView) view.findViewById(R.id.education_card);

            educationRecyclerView = (RecyclerView) view.findViewById(R.id.education_rcv);

            priceCtxv = (CustomFontTextView) view.findViewById(R.id.price_ctxv);
            reviewCtxv = (CustomFontTextView) view.findViewById(R.id.review_ctxv);
            isonlineCtxv = (CustomFontTextView) view.findViewById(R.id.isonline_ctxv);
            onlineOfflineImv = (ImageView) view.findViewById(R.id.online_offline_imv);
            profileRating = (RatingBar) view.findViewById(R.id.profile_rating);

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
        onlineOfflineImv = (ImageView) view.findViewById(R.id.online_offline_imv);
        isonlineCtxv = (CustomFontTextView) view.findViewById(R.id.isonline_ctxv);
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
                    certificateRecyclerView = (RecyclerView) view.findViewById(R.id.certification_rcv);
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

