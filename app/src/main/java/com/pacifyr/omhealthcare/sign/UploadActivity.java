package com.pacifyr.omhealthcare.sign;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.FileChooserAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.model.ModelPacifyrDocuments;
import com.pacifyr.omhealthcare.model.interfaces.AddFileClickListner;
import com.pacifyr.omhealthcare.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pacifyr.omhealthcare.sign.PacifyrRequestAccessActivity.documentFileList;

public class UploadActivity extends PacifyerActivity implements AddFileClickListner {

    private Context context;
    private Bitmap imageBitmap;
    private RecyclerView filesRcv;
    private FileChooserAdapter fileChooserAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AddFileClickListner listner;
    public   ModelPacifyrDocuments pacifyrDocuments;
    public ArrayList<Uri> uriArrayList;
    private String dir;
    private String cameraPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        onSetActionBar();
        //documentsArrayList = documentFileList;
        uriArrayList = new ArrayList<Uri>();
        context = UploadActivity.this;
        Intent i = getIntent();
        /*if(isValid(this.getIntent().getParcelableArrayListExtra("FILE_LIST")))
            documentsArrayList = i.getParcelableArrayListExtra("FILE_LIST");*/

        filesRcv = (RecyclerView) findViewById(R.id.files_rcv);
        listner = (AddFileClickListner) this;

        setAdapter();

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    public int SELECT_PDF_FILE=21, SELECT_IMAGE_GALLERY =31,SELECT_IMAGE_CAMERA = 11;

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasCamPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            int hasFilePerm = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName());
            if (hasCamPerm == PackageManager.PERMISSION_GRANTED) {
                if(hasFilePerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Select PDF", "Take Photo", "Choose From Gallery", "Cancel"};
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    builder.setTitle("Choose File");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Select PDF")) {
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setType("application/pdf");
                                startActivityForResult(intent, SELECT_PDF_FILE);
                            } else if (options[item].equals("Take Photo")) {
                                //dispatchTakePictureIntent();
                                //capturarFoto();
                                dispatchTakePictureIntent();
                               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, SELECT_IMAGE_CAMERA);*/
                            } else if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, SELECT_IMAGE_GALLERY);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.pacifyr.pacifyrapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, SELECT_IMAGE_CAMERA);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            LogUtils.LOGD("onActivityResult", "HA  rq " + requestCode);
            if (resultCode != 0) {
                if(requestCode == SELECT_IMAGE_GALLERY) {
                    pacifyrDocuments = new ModelPacifyrDocuments();
                    onActivityProfileEditGallery(data);
                } else if(requestCode == SELECT_IMAGE_CAMERA) {

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(cameraPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                    //uploadImage.setImageBitmap(getBitmapFromUri(contentUri));

                    //Bundle extras = data.getExtras();
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");

                    //////Imview.setImageBitmap(imageBitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    //Uri selectedImage = data.getData();

                    //Uri selectedImage = data.getData();
                    //imageview.setImageURI(selectedImage);

                    pacifyrDocuments = new ModelPacifyrDocuments();
                    LogUtils.LOGD("SELECT_IMAGE_CAMERA","HA on camera");
                    //Uri selectedImage = data.getData();
                    uriArrayList.add(contentUri);
                    String uriString = contentUri.getPath();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    Bitmap imageBitmap= getBitmapFromUri(contentUri);

                    pacifyrDocuments.setBitmap(imageBitmap);
                    pacifyrDocuments.setCameraImage(true);
                    pacifyrDocuments.setUri(contentUri);
                    pacifyrDocuments.setFileName(displayName);
                    pacifyrDocuments.setFileType("image");
                    pacifyrDocuments.setFile(myFile);
                    pacifyrDocuments.setFilePath(path);
                    documentFileList.add(pacifyrDocuments);


                    if(isValid(documentFileList)) {
                        setAdapter();
                    }
                    // if(bp != null) postEventOnMainThread(new ImageChoosenEvent(bp));
                    //else ToastHandler.newInstance(this).mustShowToast("Please Try Again");
                } else if(requestCode == SELECT_PDF_FILE){
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        pacifyrDocuments = new ModelPacifyrDocuments();

                        /*final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        // Check for the freshest data.*/
                        Uri uri = data.getData();


                        uriArrayList.add(uri);
                        String uriString = uri.toString();
                        File myFile = new File(uriString);
                        String path = myFile.getAbsolutePath();
                        String displayName = null;

                       /* if (uriString.startsWith("content://")) {
                            Cursor cursor = null;
                            try {
                                cursor = getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                }
                            } finally {
                                cursor.close();
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.getName();
                        }*/

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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void  onActivityProfileEditGallery(Intent data)
    {
        try{

            Uri selectedImage = data.getData();
            uriArrayList.add(selectedImage);
            String uriString = selectedImage.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(selectedImage, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }


            imageBitmap= getBitmapGalleryExifCode(data);

            pacifyrDocuments.setBitmap(imageBitmap);
            pacifyrDocuments.setUri(selectedImage);
            pacifyrDocuments.setFileName(displayName);
            pacifyrDocuments.setFileType("image");
            pacifyrDocuments.setFile(myFile);
            pacifyrDocuments.setFilePath(path);
            documentFileList.add(pacifyrDocuments);

            if(isValid(imageBitmap,"")) {
                setAdapter();
                // Bitmap rotateImage = getCroppedBitmap(image);
                //    postEventOnMainThread(new ImageChoosenEvent(rotateImage));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.LOGD("Exception editgal"," HA onActivityProfileEditGallery "+e.getMessage());
            com.pacifyr.omhealthcare.utils.ToastHandler.newInstance(this).mustShowToast("Please Try Again ");
            //ToastHandler.newInstance(this).showToast("Exception "+e.getMessage());
        }
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
            //takepicture.setImageBitmap(userImage);
            /*photoPath = picturePath;
            PhotoUploadActivity.mutableBitmap = userImage;
            PhotoUploadActivity.ivPhoto.setImageBitmap(userImage);*/
            // dismiss();
            // new SavePhotoTask().execute(userImage);
            bitmap=userImage;

            //postEventOnMainThread(new ImageChoosenEvent(userImage));
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
            com.pacifyr.omhealthcare.utils.ToastHandler.newInstance(this).mustShowToast(" Please Try again ");
            //ToastHandler.newInstance(this).showToast(" Please Try again "+e.getMessage());
        }
        return bitmap;
    }

    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView headerCtxv;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;

    private void onSetActionBar() {
        actionBarTopLlay = (LinearLayout) findViewById(R.id.action_bar_top_llay);
        bottomPanel = (RelativeLayout) findViewById(R.id.bottom_panel);
        leftLlay = (LinearLayout) findViewById(R.id.left_llay);
        backImv = (DeviceFitImageView) findViewById(R.id.back_imv);
        leftTxv = (CustomFontTextView) findViewById(R.id.left_txv);
        rightLlay = (LinearLayout) findViewById(R.id.right_llay);
        rightTxv = (CustomFontTextView) findViewById(R.id.right_txv);
        rightImv = (DeviceFitImageView) findViewById(R.id.right_imv);
        headerCtxv = (CustomFontTextView) findViewById(R.id.header_ctxv);

        leftLlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerCtxv.setText("Upload Document");
        rightTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                //output.putParcelableArrayListExtra("documentFileList", uriArrayList);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        selectImage();
    }
}
