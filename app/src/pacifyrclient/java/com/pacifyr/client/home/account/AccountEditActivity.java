package com.pacifyr.client.home.account;

import android.Manifest;
import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.ListFileAdapter;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.sign.UploadActivity;
import com.pacifyr.client.utility.PacifyrAppCompactActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.ToastHandler;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Certification;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.sign.RegisterActivity.documentFileList;
import static com.pacifyr.client.utils.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;

/**

 */

public class AccountEditActivity extends PacifyrAppCompactActivity {

    private static final int REQUESTCODE_CAMERA_IMAGE = 101;
    private static final int REQUESTCODE_GALLERY_IMAGE = 102;
    private static final String TAG = AccountEditActivity.class.getSimpleName();
    //private include include;
    private LinearLayout profileLlay;
    private CircleImageView profileCimv;
    private CustomEditText profileNameCedt;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private EditText mailCedt;
    private LinearLayout pnumberLlay;
    private CustomEditText pnumberCedt;
    private CardView aboutmeCard;
    private EditText aboutmeCedt;
    Bitmap edit_image;

    /** docs upload**/
    private CardView uploadDocLayout;
    private EditText upload_docs_ctxv;

    private int FILE_REQUEST=9;
    private RecyclerView file_list_rcv;
    private RecyclerView.Adapter fileAdapter;
    private TextView TextView_Upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        try {
            profileCimv=null;
            // include = (include) findViewById(R.id.include);
            profileLlay = findViewById(R.id.profile_llay);
            profileCimv = findViewById(R.id.profile_cimv);
            profileNameCedt = findViewById(R.id.profile_name_cedt);
            mailCard = findViewById(R.id.mail_card);
            mailLlay = findViewById(R.id.mail_llay);
            mailCedt = findViewById(R.id.mail_cedt);
            pnumberLlay = findViewById(R.id.pnumber_llay);
            pnumberCedt = findViewById(R.id.pnumber_cedt);
            aboutmeCard = findViewById(R.id.aboutme_card);
            aboutmeCedt = findViewById(R.id.aboutme_cedt);


            uploadDocLayout = findViewById(R.id.uploadDocLayout);
            upload_docs_ctxv = findViewById(R.id.upload_docs_ctxv);
            documentFileList = new ArrayList<ModelPacifyrDocuments>();
            file_list_rcv = findViewById(R.id.file_list_rcv);
            TextView_Upload = findViewById(R.id.TextView_Upload);

            onSetActionBar();
            onSetValues();
            profileCimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });



            uploadDocLayout.setVisibility(View.VISIBLE);
            TextView_Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    startActivityForResult(new Intent(getApplicationContext(), UploadActivity.class), FILE_REQUEST);

                }
            });

        } catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onCreate exce " + e.getMessage());
        }
    }

    @Override
    public void onSetValues() throws Exception {
        try {
            if(isValid(NetworkService.mCurrentUser)) {
                if (isValid(NetworkService.mCurrentUser.getImageUrl())) {
                    Picasso.with(AccountEditActivity.this).load(NetworkService.mCurrentUser.getImageUrl()).placeholder(R.drawable.placeholder_user).into(profileCimv);
                }
                defSetText(profileNameCedt,NetworkService.mCurrentUser.getName());
                defSetText(mailCedt,NetworkService.mCurrentUser.getEmail());
                defSetText(pnumberCedt,NetworkService.mCurrentUser.getPhoneNumber());
                defSetText(aboutmeCedt,NetworkService.mCurrentUser.getDescription());
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
                    }
                }

            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onSetValues exce " + e.getMessage());
        }
    }

    private void setFileAdapter() {
        if (documentFileList.size()>0) {
            file_list_rcv.setVisibility(View.VISIBLE);
            file_list_rcv.setLayoutManager(new GridLayoutManager(this, 5));
            fileAdapter = new ListFileAdapter(documentFileList, AccountEditActivity.this);
            file_list_rcv.setAdapter(fileAdapter);
        }
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }
    public static final int GALLERY = 1;
    public static final int CAMERA = 2;
    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    boolean checkPermission = Utility.checkPermission(AccountEditActivity.this, "camera");
                    if(checkPermission) {
                        new GligarPicker().requestCode(REQUESTCODE_CAMERA_IMAGE).disableCamera(false).cameraDirect(true).limit(1).withActivity(AccountEditActivity.this).show();
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {

                    try {
                        if (ActivityCompat.checkSelfPermission(AccountEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AccountEditActivity.this);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Permission necessary");
                            alertBuilder.setMessage("External storage permission is necessary");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AccountEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_GALLERY_IMAGE);
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();


                        } else {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            Uri pickerInitialUri=null;
                            // Optionally, specify a URI for the file that should appear in the
                            // system file picker when it loads.
                            if (pickerInitialUri != null) {
                                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                            }

                            startActivityForResult(intent, REQUESTCODE_GALLERY_IMAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUESTCODE_GALLERY_IMAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    Uri pickerInitialUri=null;
                    // Optionally, specify a URI for the file that should appear in the
                    // system file picker when it loads.
                    if (pickerInitialUri != null) {
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                    }

                    startActivityForResult(intent, REQUESTCODE_GALLERY_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        try {
            LogUtils.LOGD("jithish", "HA  rq " + requestCode);
            //LogUtils.LOGD("jithish", "HA rvc" + CameraFragment.  REQUEST_VIDEO_CAPTURE);
            if (resultCode != 0) {

                if (requestCode == REQUESTCODE_GALLERY_IMAGE) {
                    String dataStr = data.getDataString();
                    Uri pathsList = data.getData();
                    if (isValid(pathsList)) {
                        resizeImage(pathsList.toString());

                    }
                }
                if (requestCode == REQUESTCODE_CAMERA_IMAGE ) {
                    String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                    if (isValid(pathsList)) {
                        for (String url : pathsList) {
                            resizeImage(url);
                        }
                    }
                }
                   /*if(requestCode == GALLERY) {
                    onActivityProfileEditGallery(data);
                  } else if(requestCode == CAMERA) {
                    LogUtils.LOGD("jithish","HA on camera");
                       edit_image= (Bitmap) data.getExtras().get("data");
                       if(isValid(edit_image))
                           profileCimv.setImageBitmap(edit_image);
                   // if(bp != null) postEventOnMainThread(new ImageChoosenEvent(bp));
                    //else ToastHandler.newInstance(this).mustShowToast("Please Try Again");
                }*/
            }


        } catch (Exception e) {

        }
    }

    private void resizeImage(String url) {
        Glide.with(AccountEditActivity.this).load(url).override(1200, 1200).into(new Target<Drawable>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                edit_image = null;
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource instanceof BitmapDrawable) {
                    File file = Utility.saveFileInCache(AccountEditActivity.this, ((BitmapDrawable) resource).getBitmap(), "tmp_" + System.currentTimeMillis() + ".jpg");
                    if (file != null) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        Log.d(TAG, "File size : " + fileSizeInKB + " KB");

                        edit_image = ((BitmapDrawable) resource).getBitmap();
                        profileCimv.setImageBitmap(edit_image);
                    }
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb) {

            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb) {

            }

            @Nullable
            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void setRequest(@Nullable Request request) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    public void onActivityProfileEditGallery(Intent data) {
        try {
            edit_image = getBitmapGalleryExifCode(data);
            if (isValid(edit_image, "")) {
                profileCimv.setImageBitmap(edit_image);
                // Bitmap rotateImage = getCroppedBitmap(image);
                //    postEventOnMainThread(new ImageChoosenEvent(rotateImage));
            }
        }
        catch (Exception e)
        {
            LogUtils.LOGD("jithish"," HA onActivityProfileEditGallery "+e.getMessage());
            ToastHandler.newInstance(this).mustShowToast("Please Try Again ");
            //ToastHandler.newInstance(this).showToast("Exception "+e.getMessage());
        }

    }

    String error_msg="Error creating media file, check storage permissions: ";
    KProgressHUD hud;
    public void startAsyntaskSave(final Bitmap image, final PostRegisterModel postRegisterModel) {
        new AsyncTask<String, String, File>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                hud = KProgressHUD.create(AccountEditActivity.this)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel("Please wait")
                        // .setDetailsLabel("Downloading Data")
                        .setCancellable(false)
                        .setMaxProgress(100)
                        .show();
                if(isValid(hud))
                    hud.setProgress(0);
            }
            @Override
            protected File doInBackground(String... params) {
                File file = null;
                String temp = "";


                    try {
                        try {
                         /*   if(download_file_url.contains("https"))
                                file = downloadHtppsFile();
                            else
                                file = downloadFile();*/
                     /*       file = downloadFile();*/


                            file= storeImage( image);



                        } catch (Exception e){

                            ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( error_msg+" "+e.getMessage());
                       /*     setTextError(
                                    "File Not Supported"+e.getMessage(),
                                    Color.RED);*/

                            if(file.exists())
                                file.delete();
                            e.printStackTrace();
                        }
                    } catch (Exception e){
                        ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( error_msg);
                     /*   setTextError(
                                "File Not Supported",
                                Color.RED);*/
                        if(file.exists())
                            file.delete();
                        e.printStackTrace();
                    }

                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                try {
                    if(isValid(hud))
                        hud.dismiss();

                        if (file != null) {
                          //  updateQuickbloxUser(file,postRegisterModel);
                           // updateProfileMultipartWeb(postRegisterModel);
                        } else {
                            ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( "Check Permissions");
                        }
                } catch (Exception e){
                    ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( "Check Permissions");
                    //Crashlytics.logException(e);
                   /* if(isValid(download_file_url))
                        downloadAndOpenPDFAuto();*/
                   // e.printStackTrace();
                }
            }


        }.execute();

    }




    private File storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        FileOutputStream fos =null;
        if (pictureFile == null) {
            ToastHandler.newInstance(this).mustShowToast("Error creating media file, check storage permissions: ");
       /*     Log.d("jp",
                    "Error creating media file, check storage permissions: ");// e.getMessage());*/
            return null;
        }
        try {
          fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            if(pictureFile.exists())
                pictureFile.delete();
            Log.d("jp", "File not found: " + e.getMessage());
        } catch (IOException e) {
            if(pictureFile.exists())
                pictureFile.delete();
            Log.d("jp", "Error accessing file: " + e.getMessage());
        }
        catch (Exception e) {
            if(pictureFile.exists())
                pictureFile.delete();
            Log.d("jp", "Error accessing file: " + e.getMessage());
        }
        finally {
            if(fos!=null) try {
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return pictureFile;
    }
    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        File mediaFile=null;
        try {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
   /*     File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + AccountEditActivity.this.getPackageName()
                + "/Files");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + AccountEditActivity.this.getPackageName()
                + "/Files");*/

            File mediaStorageDir = new File(UtilityNetworkService.root_file_path);

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if(!mediaStorageDir.exists()) {
                if(!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            String mImageName = "PROFILE_" + timeStamp + ".png";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        }catch (Exception e)
        {
            mediaFile=null;
        }
        return mediaFile;
    }
    private Bitmap getBitmapGalleryExifCode(Intent data)
    {
        Bitmap bitmap=null;
        try {

            Log.e("photo from gallery", "-------------------------------");

            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            System.out.println(picturePath);

            File destination = new File(picturePath);


            FileInputStream in = null;
            try {
                in = new FileInputStream(destination);
            } catch (Exception e)
            {
            //Crashlytics.logException(e);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            //  options.iToastHandler.newInstance(this).mustShowToast(" getImageBitmap  "+e.getMessage());nSampleSize = RegisterPage3.calculateInSampleSize(options, 100, 100);
            options.inSampleSize = 2;
            Bitmap userImage = BitmapFactory.decodeStream(in, null, options);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(picturePath);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }     //Since API Level 5
            String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            System.out.println(exifOrientation);

            if (Integer.parseInt(exifOrientation) == 6) {
                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                userImage = Bitmap.createBitmap(userImage, 0, 0, userImage.getWidth(), userImage.getHeight(), matrix, true);
            }
            //     takepicture.setImageBitmap(userImage);
  /*          photoPath = picturePath;
            PhotoUploadActivity.mutableBitmap = userImage;
            PhotoUploadActivity.ivPhoto.setImageBitmap(userImage);*/
            // dismiss();
            // new SavePhotoTask().execute(userImage);
            //
            bitmap=userImage;
            //postEventOnMainThread(new ImageChoosenEvent(userImage));
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
            ToastHandler.newInstance(this).mustShowToast(" Please Try again ");
            //ToastHandler.newInstance(this).showToast(" Please Try again "+e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        edit_image=null;
    }

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
        rightTxv.setVisibility(View.VISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, "EDIT PROFILE");
            defSetText(rightTxv,"save");
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ActionBarCommonFont.otf");
            titleTxv.setTypeface(type);
            rightTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        titleTxv.setTextColor(getResources().getColor(R.color._color_action_bar_text_color));
        //backImv.setBackgroundResource(R.drawable.leftmen);
        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            rightLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!isValidEmail(mailCedt,"Please enter email address"))
                        return ;
                    if(!isValid(profileNameCedt,"Please enter name"))
                        return ;

                    if (mailCedt.getText().toString().equals(NetworkService.mCurrentUser.getEmail()))
                    {
                        //no changes in email
                        updateSubmit();
                    }
                    else
                    {
                        //changes in email address so need to update in email address so need to call new api
                        UpdateEmailAddress(mailCedt.getText().toString());
                    }



                    //finish();
                }
            });
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onSetActionBar exce " + e.getMessage());
        }
    }

    private void UpdateEmailAddress(String email) {

        try {

            setLoading("loading...");
            showProgress();
            String url = NetworkService.GLOBAL_URL + "user/changeEmail";

            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus statuss) {
                    try {
                        String js = json.toString();
                        String message="";
                        String status="";
                        hideProgress();
                        //{"message":"Succesfully updated email.","status":"success"}
                        if (isValid(json)) {
                            if (json.has("message")) {
                                try {
                                    message = json.getString("message");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            if (json.has("status")) {
                                try {
                                    status = json.getString("status");
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            if (status.equals("success"))
                            {
                                updateSubmit();
                            }
                            else
                            {
                                hideStatus2();
                                setStatus(message, R.color.red);
                                showStatus();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideStatus();
                                    }
                                }, 2000);
                            }



                        }

                         else {
                            Toast.makeText(getApplicationContext(), getString(R.string.sorry_could_not_complete_action), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideProgress();
                    }
                }
            };

            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", getPrefManager().getAccessToken());
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("email", email);
            aq.post(url, jsonObject, JSONObject.class, cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateSubmit() {
        try{

          //  if(!isValidEmail(mailCedt,"Please enter a valid email"))
          //      return ;
         /*   if (!isValidYear(yearEdit.getText().toString(),"You must be 18 years or older to use Pacifyr"))
                return;
            if (!isValidCheckBox(checkBox,getResources().getString(R.string.agree_terms_alert)))
                return;*/



            PostRegisterModel postRegisterModel=new PostRegisterModel();
            postRegisterModel.setFirstName(profileNameCedt.getText()+"");
           // postRegisterModel.setEmail(mailCedt.getText()+"");
            postRegisterModel.setDescription(aboutmeCedt.getText()+"");
            postRegisterModel.setLastName("");
       //     postRegisterModel.setLoginType("manual");
           // postRegisterModel.setPhoneNumber(phoneRegister);




                    List<Object> list=new ArrayList<>();
                    ArrayList<Certification> mCertificate = new ArrayList<>();
                    if (isValidSize(documentFileList))
                    {

                        for (int i=0;i<documentFileList.size();i++)
                        {
                            if (documentFileList.get(i).isUploaded())
                                list.add(documentFileList.get(i).getFileName());

                        }

                        if (isValidSize(NetworkService.mCurrentUser.getCertificationList())) {
                            mCertificate=NetworkService.mCurrentUser.getCertificationList();
                            Certification InsuranceObject=new Certification();
                            if (isValid(mCertificate.get(0)))
                                InsuranceObject = mCertificate.get(0);
                            InsuranceObject.setDocuments(list);
                        }
                        else{
                            Certification CertificationObject=new Certification();
                            CertificationObject.setDocuments(list);
                            mCertificate.add(CertificationObject);
                        }


                        postRegisterModel.setCertification(mCertificate);
                    }
                    else if (!isValidSize(documentFileList)&&!isValidSize(NetworkService.mCurrentUser.getCertificationList()))
                    {
                        postRegisterModel.setCertification(mCertificate);
                    }
                    else
                    {
                        if (isValidSize(NetworkService.mCurrentUser.getCertificationList())) {
                            mCertificate=NetworkService.mCurrentUser.getCertificationList();
                            Certification InsuranceObject=new Certification();
                            if (isValid(mCertificate.get(0)))
                                InsuranceObject = mCertificate.remove(0);
                            InsuranceObject.setDocuments(list);
                            mCertificate.add(InsuranceObject);
                        }
                        postRegisterModel.setCertification(mCertificate);
                    }



            if(isValid(edit_image)) {
               //  startAsyntaskSave(edit_image,postRegisterModel);
                     updateProfileMultipartWeb(postRegisterModel);
            }

           updateProfileWeb(postRegisterModel);

        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA updateSubmit exce " + e.getMessage());
        }
    }



    private void updateProfileWeb( PostRegisterModel postRegisterModel) {
        try {

            if (!isValid(getUserID(),"Session ecpired, please re login"))
                return;
            setLoadingWait();
            showProgress();
            Gson gson = new Gson();
            String json = gson.toJson(postRegisterModel);
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

                                     saveInformation(jsonObject);
                                }
                                else
                                ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( " Please try again ");
                                /*    String js=result.toString();

                                    String phone = jsonObject.getString("phoneNumber");
                                  //  String code = jsonObject.getString("code");
                                    if (isValid(phone)) {


                                        try {
                                            ModelUserProfile mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(jsonObject);
                                            if (isValid(mCurrentUser)) {
                                                NetworkService.mCurrentUser = mCurrentUser;
                                            }
                                            else {


                                                String firstName = jsonObject.getString("firstName");
                                                NetworkService.mCurrentUser.setFirstName(firstName);
                                                String email = jsonObject.getString("email");
                                                NetworkService.mCurrentUser.setEmail(email);
                                                String description = jsonObject.getString("description");
                                                NetworkService.mCurrentUser.setFirstName(description);
                                                String image = jsonObject.getString("image");
                                                NetworkService.mCurrentUser.setImage(image);
                                            }
                                        }catch (Exception ex)
                                        {
                                            LogUtils.LOGD("session","AEA updateProfileWeb exce " + ex.getMessage());
                                        }
                                        hideProgress();
                                        setStatus("Updated Profile", R.color.md_green_400);
                                        showStatus();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                edit_image = null;
                                                hideStatus();
                                                postEventOnMainThread(new NotifyEvent(true));

                                                finish();
                                            }
                                        }, 2000);

                                    } else*//* if (logstat.equals("error"))*//* {
                                        ToastHandler.newInstance(AccountEditActivity.this).mustShowToast( "Error :"+e.getMessage());
                                    }
                                */
                            } catch (Exception eee) {
                                hideProgress();
                                ToastHandler.newInstance(AccountEditActivity.this).mustShowToast("Error :"+eee.getMessage());
                                eee.printStackTrace();
                            }
                        }


                    };
            Ion.with(AccountEditActivity.this)
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

    private void updateProfileMultipartWeb(PostRegisterModel register){
        try {

            if (!isValid(getUserID(),"Session expired, please re login"))
                return;
            setLoadingWait();
            showProgress();
        Gson gson = new Gson();
        String json = gson.toJson(register);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data", json);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
            edit_image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();
        params.put("image", data);


        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                hideProgress();
                if(json == null) {


                    Toast.makeText(AccountEditActivity.this, "please try again  ", Toast.LENGTH_SHORT);
                    LogUtils.LOGD("exception", " profile up web json null ");

                } else {
                    saveInformation(json);
                }


            }
        };

        AQuery aq = new AQuery(this);
            cb.header("AccessToken", getAccessToken());
        cb.header("enctype", "multipart/form-data");
            String url = GLOBAL_URL + "user/editprofile/" + getUserID();
        aq.ajax(url, params, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            hideProgress();
            LogUtils.LOGD("session","AEA updateProfileMultipartWeb exce " + ex.getMessage());
        }
    }

    private void saveInformation(JSONObject json) {
        try {
            try {
                LogUtils.LOGD("exception", " profile up web json  " + json);
                String phone = json.getString("phoneNumber");



                if(isValid(phone)) {
                    try {


                        ModelUserProfile mCurrentUser = GsonUtils.getInstance().gsonToModelUserProfile(json);


                        if(isValid(mCurrentUser))
                            NetworkService.mCurrentUser=mCurrentUser;
                        else {

                            String image = json.getString("image");
                            NetworkService.mCurrentUser.setImage(image);
                            String firstName = json.getString("firstName");
                            NetworkService.mCurrentUser.setFirstName(firstName);
                            String email = json.getString("email");
                            NetworkService.mCurrentUser.setEmail(email);
                            String description = json.getString("description");
                            NetworkService.mCurrentUser.setFirstName(description);

                            try {
                                String lastName = json.getString("lastName");
                                NetworkService.mCurrentUser.setLastName(lastName);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }


                        saveProfileImageURL( NetworkService.mCurrentUser.getImageUrl());
                        getPrefManager().saveUserFirstName( NetworkService.mCurrentUser.getFirstName());
                        getPrefManager().saveMail(NetworkService.mCurrentUser.getEmail());

                        getPrefManager().saveUserUserLastName(NetworkService.mCurrentUser.getLastName());
                        if(isValid(NetworkService.mCurrentUser))
                            updateQuickbloxUser();
                        else
                        {
                            setStatus("Updated Profile");
                            showStatus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    edit_image = null;
                                    hideStatus();
                                    postEventOnMainThread(new NotifyEvent(true));
                                    finish();



                                }
                            }, 1200);
                        }

                    }catch (Exception e)
                    {
                        LogUtils.LOGD("session","AEA updateProfileMultipartWeb  exce " + e.getMessage());
                    }


                } else
                    ToastHandler.newInstance(AccountEditActivity.this).mustShowToast("Please Try again");

            } catch (Exception e){
                //Crashlytics.logException(e);
                ToastHandler.newInstance(AccountEditActivity.this).mustShowToast("Please Try again" );
                // e.printStackTrace();
                LogUtils.LOGD("exception", "updateProfileMultipartWeb web json  " + e.getMessage());
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA saveInformation exce " + e.getMessage());
        }
    }


    private void updateQuickbloxUser() {
     //   setLoading("Quickblox update");
        setLoading("");
        showProgress();
        try{
            QBUser user = new QBUser();
            user.setId(Integer.valueOf(getPrefManager().getQBID()));
            if(isValid(NetworkService.mCurrentUser.getImageUrl())) {
                user.setCustomData(NetworkService.mCurrentUser.getImageUrl());
               // ToastHandler.newInstance(AccountEditActivity.this).mustShowToast("uimage url "+NetworkService.mCurrentUser.getImageUrl());
            }

            user.setFullName(NetworkService.mCurrentUser.getName());
            user.setEmail(NetworkService.mCurrentUser.getEmail());
            QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>(){
                @Override
                public void onSuccess(QBUser user, Bundle args) {
              hideProgress();
                    setStatus("Updated Profile ");
                    showStatus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edit_image = null;


                            try {
                                hideStatus();
                                postEventOnMainThread(new NotifyEvent(true));
                                finish();
                            }catch (Exception e)
                            {

                            }

                        }
                    }, 1200);
                }

                @Override
                public void onError(QBResponseException errors) {
             hideProgress();
                    //setStatus("Updated Error Profile"+errors);
                    setStatus("Updated  Profile");
                    showStatus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edit_image = null;
                            hideStatus();
                            postEventOnMainThread(new NotifyEvent(true));
                            finish();



                        }
                    }, 1200);

                }
            });

        }catch (Exception e)
        {
            hideProgress();
           // setStatus("Updated Exception Profile"+e.getMessage());
            setStatus("Updated  Profile");
            showStatus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edit_image = null;
                    hideStatus();
                    postEventOnMainThread(new NotifyEvent(true));
                    finish();



                }
            }, 1200);
        }
    }
    @Subscribe
    public void onNotifyEvent(NotifyEvent event) {
        try {
            if(event.isSuccess)
                onSetValues();
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onNotifyEvent  exce " + e.getMessage());
        }

    }


    @Subscribe
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isValidSize(documentFileList))
            for (int i=0;i<documentFileList.size();i++)
            {
                if (!documentFileList.get(i).isUploaded())
                    documentFileList.remove(i);
            }
        setFileAdapter();

    }
}