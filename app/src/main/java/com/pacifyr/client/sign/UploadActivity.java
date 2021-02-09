package com.pacifyr.client.sign;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.opensooq.supernova.gligar.GligarPicker;
import com.pacifyr.client.R;
import com.pacifyr.client.adapter.FileChooserAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyr.client.model.interfaces.AddFileClickListner;
import com.pacifyr.client.network.ApiClient;
import com.pacifyr.client.retrofit.TestApiService;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pacifyr.client.sign.RegisterActivity.documentFileList;

public class UploadActivity extends PacifyrActivity implements AddFileClickListner {

    private static final int REQUESTCODE_CAMERA_IMAGE = 101;
    private static final int REQUESTCODE_GALLERY_IMAGE = 102;
    private static final String TAG = UploadActivity.class.getSimpleName();
    private Context context;
    private Bitmap imageBitmap;
    private RecyclerView filesRcv;
    private FileChooserAdapter fileChooserAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AddFileClickListner listner;
    public ModelPacifyrDocuments pacifyrDocuments;
    public ArrayList<Uri> uriArrayList;
    private String dir;
    private String cameraPhotoPath;
    private android.app.AlertDialog choosePicker;
    private String outputPath;

    int uPloadingCount=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        onSetActionBar();
        uriArrayList = new ArrayList<Uri>();
        context = UploadActivity.this;
        Intent i = getIntent();

        filesRcv = findViewById(R.id.files_rcv);
        listner = this;

        setAdapter();

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public int SELECT_PDF_FILE=21, SELECT_IMAGE_GALLERY =31,SELECT_IMAGE_CAMERA = 11;

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void setAdapter() {



        filesRcv.setLayoutManager(new GridLayoutManager(this, 5));
            fileChooserAdapter = new FileChooserAdapter(this, documentFileList, listner);
            //fileChooserAdapter.setClickListener(this);
            filesRcv.setAdapter(fileChooserAdapter);



    }



    private void selectImage() {

        try {
            PackageManager pm = getPackageManager();
            int hasCamPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            int hasFilePerm = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName());
            if (hasCamPerm == PackageManager.PERMISSION_GRANTED) {
                if(hasFilePerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Select PDF", "Take photo", "Choose from gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Choose file");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Select PDF")) {
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setType("application/pdf");
                                startActivityForResult(intent, SELECT_PDF_FILE);
                            } else if (options[item].equals("Take photo")) {
                                //dispatchTakePictureIntent();
                                //capturarFoto();
                                // dispatchTakePictureIntent();
                               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, SELECT_IMAGE_CAMERA);*/


                                boolean checkPermission = Utility.checkPermission(UploadActivity.this, "camera");
                                if(checkPermission) {
                                    new GligarPicker().requestCode(REQUESTCODE_CAMERA_IMAGE).disableCamera(false).cameraDirect(true).withActivity(UploadActivity.this).show();
                                }

                            } else if (options[item].equals("Choose from gallery")) {
                                boolean checkPermission = Utility.checkPermission(UploadActivity.this, "gallery");
                                if(checkPermission) {
                                    new GligarPicker().requestCode(REQUESTCODE_GALLERY_IMAGE).disableCamera(true).limit(1).withActivity(UploadActivity.this).show();
                                }
                            }
                        }
                    });
                    builder.show();
                }
                else{
                    Toast.makeText(this, "Read Storage Permission error", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(UploadActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_IMAGE_CAMERA);
                }
            }
            else {
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(UploadActivity.this, new String[] {Manifest.permission.CAMERA}, SELECT_IMAGE_CAMERA);
            }
            setAdapter();
            dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Pacifyr/";
            File newdir = new File(dir);
            newdir.mkdirs();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (resultCode != 0) {


                /*if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                    if (galImagePicker == null) {
                        galImagePicker = new ImagePicker(UploadActivity.this);
                        galImagePicker.shouldGenerateThumbnails(true);
                        galImagePicker.setImagePickerCallback(UploadActivity.this);
                    }
                    galImagePicker.submit(data);
                } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                    if (imagePicker == null) {
                        imagePicker = new CameraImagePicker(UploadActivity.this);
                        imagePicker.shouldGenerateThumbnails(true);
                        imagePicker.reinitialize(outputPath);
                        imagePicker.setImagePickerCallback(UploadActivity.this);
                    }
                    imagePicker.submit(data);
                }*/

                if (requestCode == REQUESTCODE_CAMERA_IMAGE || requestCode == REQUESTCODE_GALLERY_IMAGE) {
                    String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                    if (isValid(pathsList)) {
                        for (String url : pathsList) {
                            resizeImage(url);
                        }
                    }
                } else if (requestCode == SELECT_PDF_FILE) {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        pacifyrDocuments = new ModelPacifyrDocuments();
                        Uri uri = data.getData();
                        uriArrayList.add(uri);
                        String uriString = uri.toString();
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        File tempFile = File.createTempFile("temp", ".pdf");
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        copyStream(inputStream, outputStream);
                        outputStream.close();
                        File myFile = tempFile;
                        String path = tempFile.getPath();
                        String displayName = null;


                        pacifyrDocuments.setBitmap(imageBitmap);
                        pacifyrDocuments.setUri(uri);
                        pacifyrDocuments.setPdfFile(true);
                        pacifyrDocuments.setFileName(displayName);
                        pacifyrDocuments.setFileType("pdf");
                        pacifyrDocuments.setFile(myFile);
                        pacifyrDocuments.setFilePath(path);
                        documentFileList.add(pacifyrDocuments);

                        setAdapter();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void resizeImage(String url) {
        Glide.with(UploadActivity.this).load(url).override(1200, 1200).into(new Target<Drawable>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource instanceof BitmapDrawable) {
                    File file = Utility.saveFileInCache(UploadActivity.this, ((BitmapDrawable) resource).getBitmap(), "tmp_" + System.currentTimeMillis() + ".jpg");
                    if (file != null) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        Log.d(TAG, "File size : " + fileSizeInKB + " KB");

                        try {
                            pacifyrDocuments = new ModelPacifyrDocuments();

                            Uri contentUri = Uri.fromFile(file);
                            uriArrayList.add(contentUri);
                            pacifyrDocuments.setUri(contentUri);
                            pacifyrDocuments.setFileType("image");
                            pacifyrDocuments.setFile(file);
                            documentFileList.add(pacifyrDocuments);

                            setAdapter();

                        } catch (Exception e) {
                            Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        }
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


    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private MainTextView leftTxv;
    private LinearLayout rightLlay;
    private MainTextView headerCtxv;
    private MainTextView rightTxv;
    private DeviceFitImageView rightImv;

    private void onSetActionBar() {
        actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
        bottomPanel = findViewById(R.id.bottom_panel);
        leftLlay = findViewById(R.id.left_llay);
        backImv = findViewById(R.id.back_imv);
        leftTxv = findViewById(R.id.left_txv);
        rightLlay = findViewById(R.id.right_llay);
        rightTxv = findViewById(R.id.right_txv);
        rightImv = findViewById(R.id.right_imv);
        headerCtxv = findViewById(R.id.header_ctxv);

        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        headerCtxv.setTextSize(18);
        headerCtxv.setText(getResources().getString(R.string.kyc_document));

        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**after selecting multiple documents Upload documents T server One by One **/

                    if (isValidSize(documentFileList) && uPloadingCount<documentFileList.size()){
                        UploadFilesToserver();
                    }
                    else if (uPloadingCount<=documentFileList.size()){
                        Toast.makeText(context, "Please select a valid file to upload", Toast.LENGTH_SHORT).show(); hideProgress();}
                    else{
                        Toast.makeText(context, "No Files Selected.", Toast.LENGTH_SHORT).show();}

            }
        });
    }

    private void UploadFilesToserver() {
         UploadDocuments();
    }


    private void UploadDocuments() {
        if (uPloadingCount>=documentFileList.size())
        {
            finish();
        }
        else  {
            if (documentFileList.get(uPloadingCount).isUploaded()){
                uPloadingCount++;
                UploadFilesToserver();
            }
            else
            {
                try {


                    MultipartBody.Part profileImagePart = null;

                    //for document file
                    okhttp3.RequestBody mUploadFile= null;
                    okhttp3.RequestBody mFileFormat = null;

                    okhttp3.RequestBody mProfileImageUploadFile= null;
                    okhttp3.RequestBody mProfileImageFormat = null;

                    MultipartBody.Part[] documentParts =null;
                    MultipartBody.Part postFilePart =null;
                    File file = null;

                    if (isValidSize(documentFileList)&&uPloadingCount<documentFileList.size() )
                    {
                        setLoading("Uploading File : " + (uPloadingCount + 1));
                        showProgress();
                        documentParts = new MultipartBody.Part[documentFileList.size()];

                        Uri uri;
                        ModelPacifyrDocuments document = documentFileList.get(uPloadingCount);
                        file= document.getFile();




                        if (isValid(file))
                        {
                            if (document.getFileType().equals("image")) {

                                try {
                                    file= new File(URI.create(document.getUri().toString()));
                                }
                                catch(Exception e)
                                {
                                    file=new File(Utility.getAdvancedFilePath(getApplicationContext(),document.getUri()));
                                }

                                mUploadFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
                                mFileFormat = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"),  "jpeg");

                            } else if (document.getFileType().equals("pdf")){

                                mUploadFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("pdf"), file);
                                mFileFormat = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"),  "pdf");
                            }

                        }
                        else
                        {
                            if (isValid(document.getFileName()))
                            {
                                uPloadingCount++;
                                UploadFilesToserver();
                            }
                        }

                    }
                    else
                    {
                        Toast.makeText(context, "Files uploaded.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }


                    final TestApiService webServicesAPI = ApiClient.getClient().create(TestApiService.class);
                    Call<JsonObject> surveyResponse = null;

                    /** if Updating Profile Image Or files Call this**/
                    if (isValid(mUploadFile) && isValid(mFileFormat)) {
                        surveyResponse = webServicesAPI.editProfileWithDocuments(mUploadFile, mFileFormat, getAccessToken());
                        surveyResponse.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                try {
                                    hideProgress();
                                    JsonObject responceObject = response.body();
                                    if(isValid(responceObject)) {
                                        final JSONObject jsonObject = new JSONObject(responceObject.toString());
                                        if (jsonObject.has("fileName"))
                                        {
                                            String filename= String.valueOf(jsonObject.get("fileName"));

                                            if (isValid(filename))
                                            {
                                                documentFileList.get(uPloadingCount).setFileName(filename);
                                                documentFileList.get(uPloadingCount).setFileName(filename);
                                                documentFileList.get(uPloadingCount).setUploaded(true);

                                               /* else
                                                {
                                                    if (!isValid(AccountEditActivity.UploadedFileNames))
                                                        AccountEditActivity.UploadedFileNames=new ArrayList<>();


                                                    AccountEditActivity.UploadedFileNames.add(filename);
                                                }*/

                                                uPloadingCount++;
                                                if (uPloadingCount<documentFileList.size())
                                                {

                                                    UploadFilesToserver();
                                                }
                                                else
                                                {
                                                    Toast.makeText( getApplicationContext(), "All files Uploaded.", Toast.LENGTH_SHORT).show();
                                                    setResult(RESULT_OK);
                                                    finish();
                                                }

                                            }

                                        }

                                    } else {
                                        Toast.makeText(UploadActivity.this, "please try again  ", Toast.LENGTH_SHORT);

                                    }


                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                                hideProgress();
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        });

                    }



                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }


        }


    }

    @Override
    public void onClick(View view, int position) {
        selectImage();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", outputPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("picker_path")) {
            outputPath = savedInstanceState.getString("picker_path");
        }
    }



    /*@Override
    public void onImagesChosen(List<ChosenImage> list) {


        try {
            pacifyrDocuments = new ModelPacifyrDocuments();

            Uri contentUri = Uri.parse(list.get(0).getQueryUri());
            uriArrayList.add(contentUri);
            pacifyrDocuments.setUri(contentUri);
            pacifyrDocuments.setFileType("image");
            File newFile=new File(list.get(0).getOriginalPath());
            pacifyrDocuments.setFile(newFile);
            documentFileList.add(pacifyrDocuments);

            setAdapter();

        }catch (Exception e)
        {
            Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(String s) {
        Toast.makeText(context, String.valueOf(s), Toast.LENGTH_SHORT).show();

    }*/

  /* public File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }*/
}
