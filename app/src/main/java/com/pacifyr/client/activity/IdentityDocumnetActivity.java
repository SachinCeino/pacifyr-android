package com.pacifyr.client.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListFileAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.home.CustomerDetailActivity;
import com.pacifyr.client.home.account.AccountEditActivity;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.sign.UploadActivity;
import com.pacifyr.client.twilio.VideoActivity;
import com.pacifyr.client.utility.PdfViewer;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.ToastHandler;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Certification;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pacifyr.client.sign.RegisterActivity.documentFileList;
import static com.pacifyr.client.twilio.VideoActivity.isCallActive;
import static com.pacifyr.client.utils.Constants.CLOSECHILDACTIIVTY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

public class IdentityDocumnetActivity extends PacifyrActivity {

    private TextView TextView_Upload;
    private int FILE_REQUEST=9;
    private RecyclerView file_list_rcv;
    private RecyclerView.Adapter fileAdapter;
    public ArrayList<Certification> certificationList;
    private RelativeLayout next_rlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        EventBus.getDefault().register(this);
        onSetActionBar();
        TextView_Upload=findViewById(R.id.TextView_Upload);
        file_list_rcv = findViewById(R.id.file_list_rcv);
        next_rlay=findViewById(R.id.next_rlay);
        TextView_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(IdentityDocumnetActivity.this, UploadActivity.class), FILE_REQUEST);

            }
        });

        next_rlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostRegisterModel postRegisterModel=new PostRegisterModel();
                if (isValidSize(documentFileList))
                {

                    List<Object> list=new ArrayList<>();
                    certificationList=new ArrayList<>();
                    for (int i=0;i<documentFileList.size();i++)
                    {
                        if (documentFileList.get(i).isUploaded())
                            list.add(documentFileList.get(i).getFileName());

                    }
                    Certification certification=new Certification();
                    certification.setDocuments(list);
                    certificationList.add(certification);
                    postRegisterModel.setCertification(certificationList);
                }
                else
                {
                    certificationList=new ArrayList<>();
                    postRegisterModel.setCertification(certificationList);
                }


               if (isValidSize(certificationList))
               {
                   Gson gson = new Gson();
                   // gson .setExclusionStrategies(new TestExclStrat())
                   String json = gson.toJson(postRegisterModel);
                   Log.w("@@@",String.valueOf(json));
                   UpdateWeb(postRegisterModel);
               }
               else
               {
                   Toast.makeText(IdentityDocumnetActivity.this, "No ID uploaded", Toast.LENGTH_SHORT).show();
               }
            }
        });

        setValues();
    }

    private void setValues() {

        if(isValid(NetworkService.mCurrentUser)) {
            documentFileList=null;
            documentFileList=new ArrayList<>();
            if (isValid(NetworkService.mCurrentUser.getCertificationList()))
            {
                for (int i=0;i<NetworkService.mCurrentUser.getCertificationList().size();i++)
                {
                    if (isValid(NetworkService.mCurrentUser.getCertificationList().get(i).getDocuments()))
                    {
                        for (int j=0;j<NetworkService.mCurrentUser.getCertificationList().get(i).getDocuments().size();j++)
                        {
                            ModelPacifyrDocuments model=new ModelPacifyrDocuments();
                            model.setUploaded(true);
                            model.setFileName((String) NetworkService.mCurrentUser.getCertificationList().get(i).getDocuments().get(j));

                            if (model.getFileName().contains("pdf"))
                                model.setFileType("pdf");
                            else
                                model.setFileType("image");
                            documentFileList.add(model);

                        }

                    }
                }

                if (isValidSize(documentFileList))
                {
                    setFileAdapter();
                    TextView_Upload.setText("Edit");
                }
                else
                {
                    TextView_Upload.setText("Upload");
                }
            }

        }
    }

    private void UpdateWeb(PostRegisterModel postRegisterModel) {

        try {
            if (!isValid(getUserID(),"Session ecpired, please re login"))
                return;
            setLoadingWait();
            showProgress();
            Gson gson = new Gson();
            String json = gson.toJson(postRegisterModel);
            Log.w("sdfsdfsd",json.toString());
            hideKeyboard();
            String url = GLOBAL_URL + "user/editprofile/" + getUserID();


            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback= new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    hideProgress();
                    hideKeyboard();
                    try {
                        if (isValid(result)) {
                            final JSONObject jsonObject = new JSONObject(result.toString());

                                Intent i = new Intent(IdentityDocumnetActivity.this, VideoActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);
                                finish();

                        }
                        else
                            ToastHandler.newInstance(IdentityDocumnetActivity.this).mustShowToast( " Please try again ");
                    } catch (Exception eee) {
                        hideProgress();
                        ToastHandler.newInstance(IdentityDocumnetActivity.this).mustShowToast("Error :"+eee.getMessage());
                        eee.printStackTrace();
                    }
                }


            };
            Ion.with(IdentityDocumnetActivity.this)
                    .load(url)
                    .setHeader("AccessToken", getAccessToken())
                    .setHeader("enctype", "multipart/form-data")
                    .setMultipartParameter("data", json)
                    .asJsonObject()
                    .setCallback(futureCallback);
        }
        catch (Exception ex)
        {
            hideProgress();
            LogUtils.LOGD("session","AEA updateProfileWeb exce " + ex.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        documentFileList=null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onCloseActivity(Object event) {
        try {
            if (isValid(event) && event.equals(CLOSECHILDACTIIVTY)) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (isCallActive) {
            Intent i = new Intent(IdentityDocumnetActivity.this, VideoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        }
        finish();

    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        try {
            LogUtils.LOGD("jithish", "HA  rq " + requestCode);
            //LogUtils.LOGD("jithish", "HA rvc" + CameraFragment.  REQUEST_VIDEO_CAPTURE);
            if (resultCode != 0) {

                if (requestCode == FILE_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        //documentFileList = data.getParcelableArrayListExtra("documentFileList");
                        if (isValid(documentFileList)) {
                            setFileAdapter();
                        }
                    }
                }
            }


        } catch (Exception e) {

        }
    }

    private void setFileAdapter() {

        if (documentFileList.size()>0) {
            file_list_rcv.setVisibility(View.VISIBLE);
            file_list_rcv.setLayoutManager(new GridLayoutManager(this, 5));
            fileAdapter = new ListFileAdapter(documentFileList, IdentityDocumnetActivity.this);
            file_list_rcv.setAdapter(fileAdapter);
        }
    }


    ///ACTION BAR FEATURES
    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private MainTextView titleTxv;
    private LinearLayout rightLlay;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
       try {
           actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
           bottomPanel = findViewById(R.id.bottom_panel);
           leftLlay = findViewById(R.id.left_llay);
           backImv = findViewById(R.id.back_imv);
           leftTxv = findViewById(R.id.left_txv);
           titleTxv = findViewById(R.id.title_txv);
           rightLlay = findViewById(R.id.right_llay);
           rightTxv = findViewById(R.id.right_txv);
           rightImv = findViewById(R.id.right_imv);
           rightImv.setVisibility(View.INVISIBLE);
           rightTxv.setVisibility(View.INVISIBLE);
           rightLlay.setVisibility(View.INVISIBLE);
           leftTxv.setVisibility(View.INVISIBLE);
           backImv.setVisibility(View.VISIBLE);
           titleTxv.setVisibility(View.VISIBLE);
           titleTxv.setText(getResources().getString(R.string.identity_document));
           leftLlay.setVisibility(View.VISIBLE);
           leftLlay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if (isCallActive)
                   {
                       Intent i = new Intent(IdentityDocumnetActivity.this, VideoActivity.class);
                       i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                       startActivity(i);
                   }
                   finish();

               }
           });
       }catch (Exception e)
       {
           Log.w("asfs",e);
           Log.w("asfs",e);
       }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
