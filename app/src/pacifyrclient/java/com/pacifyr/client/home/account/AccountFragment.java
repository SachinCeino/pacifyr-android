package com.pacifyr.client.home.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.R;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.LogUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.sign.RegisterActivity.documentFileList;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;


/**

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
    private TextView TextView_Document;
    private CardView CardView_Document_Field,wallet_card;
    private TextView TextView_walletBalence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_current_user_profile, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_profile_title));
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
            TextView_Document = view.findViewById(R.id.TextView_Document);
            CardView_Document_Field = view.findViewById(R.id.CardView_Document_Field);
            wallet_card= view.findViewById(R.id.wallet_card);
            TextView_walletBalence= view.findViewById(R.id.TextView_walletBalence);
            if (getPrefManager().getCountry().equals("US"))
            {
                wallet_card.setVisibility(View.VISIBLE);
            }

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
                CardView_Document_Field.setVisibility(View.GONE);

                  if (isValid(NetworkService.mCurrentUser)
                    && isValid(NetworkService.mCurrentUser.getCertificationList())
                    && isValid(NetworkService.mCurrentUser.getCertificationList().get(0))
                    && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments())
                    && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0)))
                    {
                        String _FileName = NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString();
                        CardView_Document_Field.setVisibility(View.VISIBLE);
                        TextView_Document.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg"))
                                {
                                    AttachmentImageActivity.start(getActivity(), GLOBAL_IMAGE_URL+_FileName);
                                }
                                else if (_FileName.contains(".pdf"))
                                {
                                    loadPDF(_FileName);
                                }

                            }
                        });
                    }

                  if (isValid(NetworkService.mCurrentUser.getWalletBalance()))
                {
                    TextView_walletBalence.setText(NetworkService.mCurrentUser.getWalletBalance()+"");
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
                CardView_Document_Field.setVisibility(View.GONE);
                if (isValid(NetworkService.mCurrentUser)
                        && isValid(NetworkService.mCurrentUser.getCertificationList())
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0))
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments())
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0)))
                {
                    String _FileName = NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString();
                    CardView_Document_Field.setVisibility(View.VISIBLE);
                    TextView_Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg"))
                            {
                                AttachmentImageActivity.start(getActivity(), GLOBAL_IMAGE_URL+_FileName);
                            }
                            else if (_FileName.contains(".pdf"))
                            {
                                loadPDF(_FileName);
                            }

                        }
                    });
                }
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

    public void loadPDF(String url) {
        setLoading();
        showProgress();
        FileLoader.with(getActivity())
                .load(GLOBAL_IMAGE_URL+url, false) //2nd parameter is optioal, pass true to force load from network
                .fromDirectory("test4", FileLoader.DIR_CACHE)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        hideProgress();
                        File loadedFile = response.getBody();
                        // do something with the file

                        PdfViewer.pdf = loadedFile;
                        Intent intent = new Intent(getActivity(), PdfViewer.class);
                        intent.putExtra("title",getResources().getString(R.string.kyc_document));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        hideProgress();
                        Log.d(getClass().getName(), "File download failed ");
                    }
                });
    }
}

