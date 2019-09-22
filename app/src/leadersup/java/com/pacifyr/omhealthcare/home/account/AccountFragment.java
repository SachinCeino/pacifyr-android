package com.pacifyr.omhealthcare.home.account;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.BaseFragment;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Mani-Ceino on 12/29/2017.
 */

public class AccountFragment extends BaseFragment {

    private LinearLayout profileLlay;
    private CircleImageView profileCimv;
    private CustomFontTextView profileNameCtxv;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private CustomFontTextView mailDtxv;
    private LinearLayout pnumberLlay;
    private CustomFontTextView pnumberDtxv,tv_ethnicity,tv_regione,tv_edustatus,tv_services,tv_organization,tv_schoolname;
    private CardView aboutmeCard;
    private CustomFontTextView aboutmeCtxv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_current_user_profile, null);

    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
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
            tv_ethnicity=view.findViewById(R.id.tv_ethnicity);
            tv_edustatus=view.findViewById(R.id.tv_edustatus);
            tv_services=view.findViewById(R.id.tv_services);
            tv_organization=view.findViewById(R.id.tv_organization);
            tv_schoolname=view.findViewById(R.id.tv_schoolname);
            tv_regione=view.findViewById(R.id.tv_regione);

            onSetValues(view);
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onViewCreated exce " + e.getMessage());
        }
    }


    @Override
    public void onSetValues(View view) throws Exception {
        try {
            if(isValid(NetworkService.mCurrentUser)) {
                if (isValid(NetworkService.mCurrentUser.getImageUrl())) {
                        Picasso.with(getActivity()).load(NetworkService.mCurrentUser.getImageUrl()).placeholder(R.drawable.placeholder_user).into(profileCimv);
                }
                defSetText(profileNameCtxv,NetworkService.mCurrentUser.getName());
                defSetText(mailDtxv,NetworkService.mCurrentUser.getEmail());
                defSetText(pnumberDtxv,NetworkService.mCurrentUser.getPhoneNumber());
                defSetText(aboutmeCtxv,NetworkService.mCurrentUser.getDescription());

                defSetText(tv_ethnicity,NetworkService.mCurrentUser.getRace());
                defSetText(tv_edustatus,NetworkService.mCurrentUser.getEducationStatus());
                defSetText(tv_services,NetworkService.mCurrentUser.getReferredServices());
                defSetText(tv_organization,NetworkService.mCurrentUser.getRefferedOrganisation());
                defSetText(tv_schoolname,NetworkService.mCurrentUser.getSchoolName());
                defSetText(tv_regione,NetworkService.mCurrentUser.getRegion());

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

