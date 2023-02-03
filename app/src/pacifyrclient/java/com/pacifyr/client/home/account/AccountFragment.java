package com.pacifyr.client.home.account;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.ui.activity.AttachmentImageActivity;
import com.pacifyr.client.utility.BaseFragment;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


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
    private TextView label_balance;
    private TextView label_document_type;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof DashboardActivity) {

            // ((DashboardActivity) getActivity()).changeBottomMenuIcon(R.id.bnm_providers);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_current_user_profile, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) getActivity()).changeActionBarTitleForFragments(getResources().getString(R.string.nav_profile_title));
        ((DashboardActivity) getActivity()).hideConnectButton();
        userDetailWeb(getUserID(), new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                try {
                    if (json != null) {
                        NetworkService.mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);
                        //ProfileScreenActivity.pacifyerDetial = GsonUtils.getInstance().gsonToModelUserProfile(json);

                        setUpProfileValues(GsonUtils.getInstance().gsonToModelUserProfile(json));

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

    private void setUpProfileValues(ModelUserProfile modelUserProfile) {

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
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0))) {
                    String _FileName = NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString();
                    if (isValid(_FileName)) {
                        String iconLabel = "";
                        if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg")) {
                            iconLabel = "PIC";
                        } else if (_FileName.contains(".pdf")) {
                            iconLabel = "DOC";
                        }
                        label_document_type.setText(iconLabel);
                    }
                    CardView_Document_Field.setVisibility(View.VISIBLE);
                    TextView_Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg")) {
                                AttachmentImageActivity.start(getActivity(), GLOBAL_IMAGE_URL + _FileName);
                            } else if (_FileName.contains(".pdf")) {
                                loadPDF(_FileName);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.file_format_not_supported), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    CardView_Document_Field.setVisibility(View.VISIBLE);
                    label_document_type.setText("");
                    TextView_Document.setText(Html.fromHtml(getString(R.string.upload_document)));
                    TextView_Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (getActivity() instanceof DashboardActivity) {
                                Intent intent = new Intent(getActivity(), AccountEditActivity.class);
                                intent.putExtra("scrollDown", true);
                                startActivity(intent);

                            }
                        }
                    });
                }

                if (isValid(NetworkService.mCurrentUser.getCompany())) {
                    if (NetworkService.mCurrentUser.getCompany().getEnableWalletBalance()) {
                        if (NetworkService.mCurrentUser.getWalletBalance() != 0) {
                            label_balance.setText(getString(R.string.available_plan_amount_caps));
                            TextView_walletBalence.setText("$ " + NetworkService.mCurrentUser.getWalletBalance() + " plan amount");
                            wallet_card.setVisibility(View.VISIBLE);
                        }
                    } else if (NetworkService.mCurrentUser.getCompany().getEnablePredefinedSessions()) {
                        TextView_walletBalence.setText(NetworkService.mCurrentUser.getNumberOfPredefinedSessions() + " plan sessions");
                        label_balance.setText(getString(R.string.available_plan_sessions_caps));
                        wallet_card.setVisibility(View.VISIBLE);
                    } else if (NetworkService.mCurrentUser.getCompany().getEnableFreeminutes()) {
                        TextView_walletBalence.setText(NetworkService.mCurrentUser.getAvailableMinutes()
                                + " " + getString(R.string.plan_minutes));
                        label_balance.setText(getString(R.string.available_plan_minutes_caps));
                        wallet_card.setVisibility(View.VISIBLE);
                    }
                } else if (isValid(NetworkService.mCurrentUser.getAvailableMinutes())) {
                    TextView_walletBalence.setText(NetworkService.mCurrentUser.getAvailableMinutes() + " plan minutes");
                    label_balance.setText(getString(R.string.available_plan_minutes_caps));
                    wallet_card.setVisibility(View.VISIBLE);
                }

                /*if (getPrefManager().getCountry().equals("US") &&isValid(NetworkService.mCurrentUser.getWalletBalance()))
                {

                    if (NetworkService.mCurrentUser.getWalletBalance()!=0)
                    {
                        TextView_walletBalence.setText("$ "+NetworkService.mCurrentUser.getWalletBalance()+"");
                        wallet_card.setVisibility(View.VISIBLE);
                    }

                }*/

            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AF onSetValues exce " + e.getMessage());
        }
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
            label_document_type = view.findViewById(R.id.label_document_type);
            wallet_card = view.findViewById(R.id.wallet_card);
            TextView_walletBalence = view.findViewById(R.id.TextView_walletBalence);
            label_balance = view.findViewById(R.id.label_balance);

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
                defSetText(profileNameCtxv, NetworkService.mCurrentUser.getName());
                defSetText(mailDtxv, NetworkService.mCurrentUser.getEmail());
                defSetText(pnumberDtxv, NetworkService.mCurrentUser.getPhoneNumber());
                defSetText(aboutmeCtxv, NetworkService.mCurrentUser.getDescription());
                CardView_Document_Field.setVisibility(View.GONE);

                if (isValid(NetworkService.mCurrentUser)
                        && isValid(NetworkService.mCurrentUser.getCertificationList())
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0))
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments())
                        && isValid(NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0))) {
                    String _FileName = NetworkService.mCurrentUser.getCertificationList().get(0).getDocuments().get(0).toString();
                    CardView_Document_Field.setVisibility(View.VISIBLE);
                    if (isValid(_FileName)) {
                        String iconLabel = "";
                        if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg")) {
                            iconLabel = "PIC";
                        } else if (_FileName.contains(".pdf")) {
                            iconLabel = "DOC";
                        }
                        label_document_type.setText(iconLabel);
                        //TextView_Document.setText(_FileName);
                    }
                    TextView_Document.setText(Html.fromHtml(getString(R.string.view_document)));
                    TextView_Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg")) {
                                AttachmentImageActivity.start(getActivity(), GLOBAL_IMAGE_URL + _FileName);
                            } else if (_FileName.contains(".pdf")) {
                                loadPDF(_FileName);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.file_format_not_supported), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                if (isValid(NetworkService.mCurrentUser.getCompany())) {
                    if (NetworkService.mCurrentUser.getCompany().getEnableWalletBalance()) {

                        if (NetworkService.mCurrentUser.getWalletBalance() != 0) {
                            TextView_walletBalence.setText("$ " + NetworkService.mCurrentUser.getWalletBalance()
                                    + " " + getString(R.string.plan_amount));
                            wallet_card.setVisibility(View.VISIBLE);
                            label_balance.setText(getString(R.string.available_plan_amount_caps));
                        }

                    } else if (NetworkService.mCurrentUser.getCompany().getEnablePredefinedSessions()) {
                        TextView_walletBalence.setText(NetworkService.mCurrentUser.getNumberOfPredefinedSessions()
                                + " " + getString(R.string.plan_sessions));
                        label_balance.setText(getString(R.string.available_plan_sessions_caps));
                        wallet_card.setVisibility(View.VISIBLE);
                    } else if (NetworkService.mCurrentUser.getCompany().getEnableFreeminutes()) {
                        TextView_walletBalence.setText(NetworkService.mCurrentUser.getAvailableMinutes()
                                + " " + getString(R.string.plan_minutes));
                        label_balance.setText(getString(R.string.available_plan_minutes_caps));
                        wallet_card.setVisibility(View.VISIBLE);
                    }
                } else if (isValid(NetworkService.mCurrentUser.getAvailableMinutes())) {
                    TextView_walletBalence.setText(NetworkService.mCurrentUser.getAvailableMinutes()
                            + " " + getString(R.string.plan_minutes));
                    label_balance.setText(getString(R.string.available_plan_minutes_caps));
                    wallet_card.setVisibility(View.VISIBLE);
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
                    TextView_Document.setText(Html.fromHtml(getString(R.string.view_document)));
                    TextView_Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (_FileName.contains(".jpg") || _FileName.contains(".png") || _FileName.contains(".jpeg")) {
                                AttachmentImageActivity.start(getActivity(), GLOBAL_IMAGE_URL + _FileName);
                            } else if (_FileName.contains(".pdf")) {
                                loadPDF(_FileName);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.file_format_not_supported), Toast.LENGTH_SHORT).show();
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

