package com.pacifyr.omhealthcare.home.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.ceino.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.event.AlertTypeEvent;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.home.WelcomeTourActivity;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.PacifyrAppCompactActivity;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.ToastHandler;
import com.pacifyr.omhealthcare.utils.Utility;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ceino.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.omhealthcare.sign.RegisterVerifyActivity.phoneRegister;

/**
 * Created by Mani-Ceino on 1/2/2018.
 */

public class AccountEditActivity extends PacifyrAppCompactActivity {

    //private include include;
    private LinearLayout profileLlay;
    private CircleImageView profileCimv;
    private CustomEditText profileNameCedt;
    private CardView mailCard;
    private LinearLayout mailLlay;
    private CustomEditText mailCedt;
    private LinearLayout pnumberLlay;
    private CustomEditText pnumberCedt;
    private CardView aboutmeCard;
    private CustomEditText aboutmeCedt;
    Bitmap edit_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        try {
            profileCimv=null;
            // include = (include) findViewById(R.id.include);
            profileLlay = (LinearLayout) findViewById(R.id.profile_llay);
            profileCimv = (CircleImageView) findViewById(R.id.profile_cimv);
            profileNameCedt = (CustomEditText) findViewById(R.id.profile_name_cedt);
            mailCard = (CardView) findViewById(R.id.mail_card);
            mailLlay = (LinearLayout) findViewById(R.id.mail_llay);
            mailCedt = (CustomEditText) findViewById(R.id.mail_cedt);
            pnumberLlay = (LinearLayout) findViewById(R.id.pnumber_llay);
            pnumberCedt = (CustomEditText) findViewById(R.id.pnumber_cedt);
            aboutmeCard = (CardView) findViewById(R.id.aboutme_card);
            aboutmeCedt = (CustomEditText) findViewById(R.id.aboutme_cedt);
            onSetActionBar();
            onSetValues();
            profileCimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
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
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onSetValues exce " + e.getMessage());
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
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                       startActivityForResult(intent, CAMERA);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    boolean checkPermission = Utility.checkPermission(AccountEditActivity.this, "gallery");
                    if(checkPermission) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        try {
            LogUtils.LOGD("jithish", "HA  rq " + requestCode);
            //LogUtils.LOGD("jithish", "HA rvc" + CameraFragment.  REQUEST_VIDEO_CAPTURE);
            if (resultCode != 0) {
                   if(requestCode == GALLERY) {
                    onActivityProfileEditGallery(data);
                  } else if(requestCode == CAMERA) {
                    LogUtils.LOGD("jithish","HA on camera");
                       edit_image= (Bitmap) data.getExtras().get("data");
                       if(isValid(edit_image))
                           profileCimv.setImageBitmap(edit_image);
                   // if(bp != null) postEventOnMainThread(new ImageChoosenEvent(bp));
                    //else ToastHandler.newInstance(this).mustShowToast("Please Try Again");
                }
            }



        } catch (Exception e) {

        }
    }
    public void  onActivityProfileEditGallery(Intent data)
    {
        try{
            edit_image= getBitmapGalleryExifCode(data);
            if(isValid(edit_image,"")) {
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
                String temp=new String();


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
    private CustomFontTextView leftTxv;
    private CustomFontTextView titleTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    public void onSetActionBar() {
        try {
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        titleTxv = (CustomFontTextView) findViewById(R.id.title_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
        rightImv.setVisibility(View.INVISIBLE);
        rightTxv.setVisibility(View.VISIBLE);
        rightLlay.setVisibility(View.VISIBLE);
        leftTxv.setVisibility(View.INVISIBLE);
        backImv.setVisibility(View.VISIBLE);
        titleTxv.setVisibility(View.VISIBLE);
        leftLlay.setVisibility(View.VISIBLE);
        defSetText(titleTxv,"edit profile ");
            defSetText(rightTxv,"save");
            rightTxv.setTextColor(getResources().getColor(R.color.grey_dark_text));
        titleTxv.setTextColor(getResources().getColor(R.color.grey_dark_text));
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

                    updateSubmit();


                    //finish();
                }
            });
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA onSetActionBar exce " + e.getMessage());
        }
    }
    private void updateSubmit() {
        try{

            if(!isValid(profileNameCedt,"Please enter name"))
                return ;
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
            if(isValid(edit_image)) {
               //  startAsyntaskSave(edit_image,postRegisterModel);
                     updateProfileMultipartWeb(postRegisterModel);
            }
            else
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
                    Ion.with(AccountEditActivity.this).load(url).setHeader("AccessToken",getAccessToken()).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
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
                            hideStatus();
                            postEventOnMainThread(new NotifyEvent(true));
                            finish();



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
}