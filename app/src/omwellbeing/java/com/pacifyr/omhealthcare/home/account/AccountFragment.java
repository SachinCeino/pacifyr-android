package com.pacifyr.omhealthcare.home.account;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyr.omhealthcare.customview.MainTextView;
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
    private MainTextView profileNameCtxv;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private MainTextView mailDtxv;
    private LinearLayout pnumberLlay;
    private MainTextView pnumberDtxv;
    private CardView aboutmeCard;
    private MainTextView aboutmeCtxv;

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
            profileNameCtxv = (MainTextView) view.findViewById(R.id.profile_name_ctxv);
            mailCard = (CardView) view.findViewById(R.id.mail_card);
            mailLlay = (LinearLayout) view.findViewById(R.id.mail_llay);
            mailDtxv = (MainTextView) view.findViewById(R.id.mail_dtxv);
            pnumberLlay = (LinearLayout) view.findViewById(R.id.pnumber_llay);
            pnumberDtxv = (MainTextView) view.findViewById(R.id.pnumber_dtxv);
            aboutmeCard = (CardView) view.findViewById(R.id.aboutme_card);
            aboutmeCtxv = (MainTextView) view.findViewById(R.id.aboutme_ctxv);
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

