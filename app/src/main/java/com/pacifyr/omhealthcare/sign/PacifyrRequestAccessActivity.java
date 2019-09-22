package com.pacifyr.omhealthcare.sign;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.model.pacifyr.Education;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.ceino.utilitylibrary.model.response.ResponseMessage;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.adapter.CountrySpinnerAdapter;
import com.pacifyr.omhealthcare.adapter.ListCertificatesAdapter;
import com.pacifyr.omhealthcare.adapter.ListEducationAdapter;
import com.pacifyr.omhealthcare.adapter.ListFileAdapter;
import com.pacifyr.omhealthcare.adapter.RequestSpecialistAdapter;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.model.ItemData;
import com.pacifyr.omhealthcare.model.ModelPacifyrCertificates;
import com.pacifyr.omhealthcare.model.ModelPacifyrDocuments;
import com.pacifyr.omhealthcare.model.PostPacifyrRequest;
import com.pacifyr.omhealthcare.network.ApiClient;
import com.pacifyr.omhealthcare.network.ApiService;
import com.pacifyr.omhealthcare.network.GsonUtils;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.pacifyr.omhealthcare.utils.Utility;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pacifyr.omhealthcare.network.NetworkService.GLOBAL_URL;

public class PacifyrRequestAccessActivity extends PacifyerActivity implements RequestSpecialistAdapter.EmotionClickkListenerRequest{

    public static ItemData countryData;


    private Context context;

    String mCurrentPhotoPath;

    

    public ArrayList<Education> educationList;
    public ArrayList<ModelPacifyrCertificates> certificateList;

    public static ArrayList<ModelPacifyrDocuments> documentFileList;

    private PostPacifyrRequest modelpostPacifyrRequest;

    private ScrollView scrollView2;
    private ImageView uploadImage;
    private CustomEditText emailTxv;
    private Spinner spinner;
    private CustomEditText nameCedt;
    private CustomEditText phoneCedit;
    private CustomFontTextView birthYearTxv;
    private CustomEditText snapshotTxv;
    private CustomFontTextView textView2;
    private CustomEditText costCtxv;
    private RelativeLayout educationRlay;
    private CustomEditText educationCedt;
    private CustomFontTextView addEducationCtxv;
    private RecyclerView educationRcv;
    private RelativeLayout certificatesRlay;
    private CustomEditText certificatesCedt;
    private CustomFontTextView addCertificatesCedit;
    private RecyclerView certificatesRcv;
    //private RelativeLayout specialityRlay;
    private CustomFontTextView specialitiesCtxv;
    //private ImageView specialityImv;
    private CustomFontTextView uploadDocsCtxv;
    //private ImageView uploadRightImv;
    private TextView uploadInstructionTxv;
    private RelativeLayout requestRlay;
    private CheckBox termsofUseCbox;
    private CustomFontTextView requestCtxv;
    private View specialityView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager educationLayoutManager;
    public static  ArrayList<ModelSpecialist.Results> specialList;
    public ArrayList<ModelSpecialist.Results> selectList;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView specialityRcv;

    private  RecyclerView fileListRcv;
    private RecyclerView.Adapter fileAdapter;

    private int EDUCATION_REQUEST=5,CERTIFICATE_REQUEST=3,FILE_REQUEST=9;
    private File profileImageFile;

    private String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (savedInstanceState != null) {
                if (profileFilePath == null && savedInstanceState.getString("uri_file_path") != null) {
                    profileFilePath = Uri.parse(savedInstanceState.getString("uri_file_path"));
                }
            }
            setContentView(R.layout.activity_pacifyr_register_request);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            initializeViews();
            onSetActionBar();
            context = PacifyrRequestAccessActivity.this;
            documentFileList = new ArrayList<ModelPacifyrDocuments>();
            modelpostPacifyrRequest = new PostPacifyrRequest();
            spinner = findViewById(R.id.spinner);

            ArrayList<ItemData> list = new ArrayList<>();
            list.add(new ItemData("+1", "US", R.drawable.flag_usa));
            list.add(new ItemData("+1", "Canada", R.drawable.canada2));
            list.add(new ItemData("+91", "India", R.drawable.ic_india));
            list.add(new ItemData("+971","UAE", R.drawable.flag_uae2));
            /*list.add(new ItemData("+971","Kuwait", R.drawable.kuwait2));*/

            CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(this,
                    R.layout.spinner_item, R.id.txt, list);
            spinner.setAdapter(adapter);

            birthYearTxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showYearPicker(birthYearTxv);
                }
            });

            addEducationCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValid(educationList)) {
                        Intent intent= new Intent(context, AddPacifyrEducationActivity.class);
                        intent.putParcelableArrayListExtra("EDU_LIST",educationList);
                        startActivityForResult(intent, EDUCATION_REQUEST);
                    }else{
                        startActivityForResult(new Intent(context, AddPacifyrEducationActivity.class), EDUCATION_REQUEST);
                    }
                }
            });

            addCertificatesCedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValid(certificateList)){
                        Intent intent= new Intent(context, AddPacifyrCertificatesActivity.class);
                        intent.putParcelableArrayListExtra("CERT_LIST",certificateList);
                        startActivityForResult(intent, CERTIFICATE_REQUEST);
                    }else {
                        startActivityForResult(new Intent(context, AddPacifyrCertificatesActivity.class), CERTIFICATE_REQUEST);
                    }
                }
            });

            uploadDocsCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(isValid(documentFileList)){
                        Intent intent= new Intent(context, UploadActivity.class);
                        intent.putParcelableArrayListExtra("FILE_LIST", documentFileList);
                        startActivityForResult(intent, FILE_REQUEST);
                    }else {*/
                        startActivityForResult(new Intent(context, UploadActivity.class), FILE_REQUEST);
                   /* }*/
                }
            });


            specialitiesCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSpecialityPicker();
                }
            });

            requestRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateRequest();
                }
            });

            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    boolean validator=false;

    private void validateRequest() {
        if(isValid(imageBitmap,"Please select profile Image"))
            if(isValid(nameCedt,"Please enter Name"))
                if (isValidEmail(emailTxv,"Enter Valid Mail"))
                    if(isValidMobile(phoneCedit,"Enter valid Phone number"))
                        if(isValidYear(birthYearTxv.getText().toString(),"You must be 18 years"))
                            if(isValid(snapshotTxv,"Please describe about yourself"))
                                if(isValid(costCtxv,"Enter price"))
                                    if (isValid(educationList,"Please Choose your education"))
                                        if (isValid(certificateList,"Please choose your certification"))
                                            if(isValid(selectList,"Select your Specialities"))
                                            {//upload docs validation pending
                                                if(isValid(documentFileList,"Select your Documents")){
                                                    if(isValid(termsofUseCbox,"Please tick the Checkbox"))
                                                        processRequest();
                                                }
                                            }
    }

    private void processRequest() {
        try {
            countryData = (ItemData) spinner.getSelectedItem();
            String[] splited = nameCedt.getText().toString().split("\\s+");
            if (containsWhitespace(nameCedt.getText().toString())) {
                modelpostPacifyrRequest.setFirstName(splited[0]);
                modelpostPacifyrRequest.setLastName(splited[1]);
            } else {
                modelpostPacifyrRequest.setFirstName(nameCedt.getText().toString());
                modelpostPacifyrRequest.setLastName(" ");
            }
            modelpostPacifyrRequest.setPhoneNumber(countryData.getCode()+phoneCedit.getText().toString());
            modelpostPacifyrRequest.setEmail(emailTxv.getText().toString());
            modelpostPacifyrRequest.setCountry(countryData.getCountry());
            modelpostPacifyrRequest.setEducation(educationList);
            modelpostPacifyrRequest.setDescription(snapshotTxv.getText().toString());
            modelpostPacifyrRequest.setDateOfBirth("01-Jan-"+birthYearTxv.getText().toString());
            modelpostPacifyrRequest.setCertifications(certificateList);
            if(isValid(selectList))
                modelpostPacifyrRequest.setSpecialities(selectList);
            modelpostPacifyrRequest.setPrice(costCtxv.getText().toString());
            uploadRequest2(modelpostPacifyrRequest);
            //multipart(modelpostPacifyrRequest);
            //sendRequest(modelpostPacifyrRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private  void uploadRequest2(PostPacifyrRequest modelpostPacifyrRequest) {
        try {
            setLoadingWait();
            showProgress();

            RequestBody profileImage = RequestBody.create(okhttp3.MediaType.parse("image/*"), profileImageFile);
            MultipartBody.Part profileImagePart = MultipartBody.Part.createFormData("image", profileImageFile.getName(), profileImage);

            MultipartBody.Part[] documentParts = new MultipartBody.Part[documentFileList.size()];

            for (int index = 0; index < documentFileList.size(); index++) {
                File file;
                Uri uri;
                ModelPacifyrDocuments document = documentFileList.get(index);
                if (document.isCameraImage()) {
                    file = document.getFile();
                }else if(document.isPdfFile()){
                    Uri uri1 = document.getUri();
                    try {
                        file = new File(getPDFPath(uri1));
                    }catch (Exception e)
                    {

                        file = new File(getPath(context,uri1));
                    }

                }
                else{
                    uri = document.getUri();
                    String uriString = getRealPathFromUri(context, uri);
                    file = new File(uriString);
                }
                if (document.getFileType().equals("image")) {
                    RequestBody imageBody = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
                    documentParts[index] = MultipartBody.Part.createFormData("doc", file.getName(), imageBody);

                } else if (document.getFileType().equals("pdf")){
                    RequestBody pdfBody = RequestBody.create(okhttp3.MediaType.parse("application/pdf"), file);
                    documentParts[index] = MultipartBody.Part.createFormData("doc", file.getName(), pdfBody);
                }

            }

            Gson gson = new Gson();
            String json = gson.toJson(modelpostPacifyrRequest);

            RequestBody name = RequestBody.create(okhttp3.MediaType.parse("text/plain"), json);
            //MultipartBody.Part jsonPart = MultipartBody.Part.createFormData("data", "data", name);

            final ApiService webServicesAPI = ApiClient.getClient().create(ApiService.class);
            final Call<ResponseMessage> surveyResponse = webServicesAPI.uploadFile(documentParts, profileImagePart, name);

            surveyResponse.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                   try {
                       hideProgress();
                       ResponseMessage responseMessage = response.body();
                       String message = responseMessage.getMessage();
                       String status = responseMessage.getStatus();
                       if(status.equals("error")){
                           setStatus(message+" Please try again.", R.color.md_red_A400);
                           showStatus();
                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   hideStatus();
                               }
                           }, 3000);
                       }else {
                           setStatus("Pacifyr Request Sent Successfully", R.color.md_green_400);
                           showStatus();
                           Handler handler = new Handler();
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   hideStatus();
                                   startActivity(new Intent(context, SignActivity.class));
                                   finishAffinity();
                               }
                           }, 2000);
                       }
                   }
                   catch (Exception e){
                       e.printStackTrace();
                   }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                    hideProgress();
                    Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void showSpecialityPicker() {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_show_speciality_picker, null, false);
            TextView saveCtxv = view.findViewById(R.id.save_ctxv);
            ImageView closeImv = view.findViewById(R.id.close_dimv);
            specialityRcv = view.findViewById(R.id.speciality_rcv);
            //ImageView closeDimv = (ImageView) view.findViewById(R.id.close_dimv);
            handleActionSpecilaist();
            /*hideStatus();*/
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            saveCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValid(specialList)) {
                        selectList = new ArrayList<>();
                        for (int i = 0; i < specialList.size(); i++) {
                            if (specialList.get(i).isSelect()) {
                                selectList.add(specialList.get(i));
                            }
                        }

                        specialitiesCtxv.setText(selectList.toString());
                        hideProgress();
                        hideStatus();
                    }
                    else{
                        Toast.makeText(context,"Please wait",Toast.LENGTH_LONG).show();
                    }
                }
            });

            closeImv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideProgress();
                    hideStatus();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getPDFPath(Uri uri){
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static byte[] loadFile(String sourcePath) throws IOException
    {
        try {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(sourcePath);
                return readFully(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;

        }
    }

    public static byte[] readFully(InputStream stream) throws IOException
    {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1)
        {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static boolean containsWhitespace(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    public Boolean isValidYear(String text, String error) {
        if (text != null)
            if(android.text.TextUtils.isDigitsOnly(text))
                if (isValid(text + ""))
                    if (isValidMajor(text))
                        return true;
        //text.setError(error + "");
        setAlertOKStatusInWhite("Invalid",error,"OK");
        return false;

    }
    private boolean isValidMajor(String s) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        Integer birthYear=Integer.parseInt(s);
        int diff=currentYear-birthYear;
        return diff > 18;
    }

    public static final int GALLERY_CODE = 111;
    public static final int CAMERA_CODE = 222;

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    boolean checkPermission = Utility.checkPermission(context, "camera");
                    if(checkPermission) {
                        dispatchTakePictureIntent();
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    boolean checkPermission = Utility.checkPermission(context, "gallery");
                    if(checkPermission) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_CODE);
                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private Uri profileFilePath;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (profileFilePath != null)
            outState.putString("uri_file_path", profileFilePath.toString());
        super.onSaveInstanceState(outState);
    }



    Bitmap imageBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        try {
            LogUtils.LOGD("onActivityResult", "HA  rqCd " + requestCode);
           if (resultCode != 0) {
                if(requestCode == GALLERY_CODE) {
                   /* String filePath = (data.getData().getPath());
                    profileImageFile = new File(filePath);*/
                    Uri selectedImageURI = data.getData();
                    profileImageFile = new File(getRealPathFromUri(context,selectedImageURI));

                    imageBitmap = getBitmapFromUri(selectedImageURI);
                    //imageBitmap= getBitmapGalleryExifCode(data);
                    if(isValid(imageBitmap,"")) {
                        uploadImage.setImageBitmap(imageBitmap);
                        // Bitmap rotateImage = getCroppedBitmap(image);
                        //    postEventOnMainThread(new ImageChoosenEvent(rotateImage));
                    }
                } else if(requestCode == CAMERA_CODE) {

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);




                    profileImageFile = new File(mCurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(profileImageFile);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);

                    imageBitmap = handleSamplingAndRotationBitmap(context,contentUri);
                    //imageBitmap = getBitmapFromUri(contentUri);
                    uploadImage.setImageBitmap(imageBitmap);

                    OutputStream os = new BufferedOutputStream(new FileOutputStream(profileImageFile));
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    /*Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadImage.setImageBitmap(imageBitmap);*/

                }
                else if (requestCode == EDUCATION_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        educationList = data.getParcelableArrayListExtra("eduList");
                        setEducationAdapter();                    }
                }
                else if (requestCode == CERTIFICATE_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        certificateList = data.getParcelableArrayListExtra("certList");
                        setCertificateAdapter();
                    }
                }else if(requestCode == FILE_REQUEST){
                    if(resultCode == RESULT_OK){
                        //documentFileList = data.getParcelableArrayListExtra("documentFileList");
                        if(isValid(documentFileList)) {
                            setFileAdapter();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if (userChoosenTask.equals("Choose from Library"))
                            galleryIntent();
                    } else {
                        //code for deny
                        Toast.makeText(context,"Please allow Permission",Toast.LENGTH_LONG);
                    }
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG);
        }
    }


    /*public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/


    private void setCertificateAdapter() {
        certificatesRcv.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(this);
        certificatesRcv.setLayoutManager(mLayoutManager);
        mAdapter = new ListCertificatesAdapter(certificateList, PacifyrRequestAccessActivity.this);
        certificatesRcv.setAdapter(mAdapter);
    }

    private void setEducationAdapter() {
        educationRcv.setVisibility(View.VISIBLE);
        educationLayoutManager = new LinearLayoutManager(this);
        educationRcv.setLayoutManager(educationLayoutManager);
        mAdapter = new ListEducationAdapter(educationList, PacifyrRequestAccessActivity.this);
        educationRcv.setAdapter(mAdapter);
    }

    private void setFileAdapter() {
        fileListRcv.setVisibility(View.VISIBLE);
        fileListRcv.setLayoutManager(new GridLayoutManager(this, 5));
        fileAdapter = new ListFileAdapter(documentFileList, context);
        fileListRcv.setAdapter(fileAdapter);
    }

    public ModelSpecialist modelSpecialist;
    public ModelSpecialist.Results[] modelResults;

    private void handleActionSpecilaist(){
        try {

            PrefManager prefM = PrefManager.getInstance(context);
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL+"specialities/byCategory?category=all";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        modelSpecialist = GsonUtils.getInstance().gsonToModelSpecialist(json);
                        modelResults = modelSpecialist.getResults();
                        modelSpecialist.getResultsList().toString();
                        setAdapterSpecial();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            //cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setAdapterSpecial() {
        try {
            if(modelSpecialist!=null)
                if(modelSpecialist.getResults()!=null) {
                    specialityRcv.setLayoutManager(new GridLayoutManager(this, 3));
                    //layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                    //specialityRcv.setLayoutManager(layoutManager);
                    specialList = modelSpecialist.getResultsList();
                    adapter= new RequestSpecialistAdapter(PacifyrRequestAccessActivity.this);
                    //adapter = new ListCertificatesAdapter(certificateList, PacifyrRequestAccessActivity.this);
                    specialityRcv.setAdapter(adapter);

                }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void OnEmotionClickkListener() { }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public int REQUEST_CAMERA=1,SELECT_FILE=2;
    public String userChoosenTask;

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }



    private LinearLayout actionBarTopLlay;
    private RelativeLayout bottomPanel;
    private LinearLayout leftLlay;
    private DeviceFitImageView backImv;
    private CustomFontTextView leftTxv;
    private LinearLayout rightLlay;
    private CustomFontTextView rightTxv;
    private DeviceFitImageView rightImv;


    private void onSetActionBar() {
        try {
            actionBarTopLlay = findViewById(R.id.action_bar_top_llay);
            bottomPanel = findViewById(R.id.bottom_panel);
            leftLlay = findViewById(R.id.left_llay);
            backImv = findViewById(R.id.back_imv);
            leftTxv = findViewById(R.id.left_txv);
            rightLlay = findViewById(R.id.right_llay);
            rightTxv = findViewById(R.id.right_txv);
            rightImv = findViewById(R.id.right_imv);

            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void initializeViews() {
        try {

            nameCedt = findViewById(R.id.name_txv);
            scrollView2 = findViewById(R.id.scrollView2);
            uploadImage = findViewById(R.id.upload_image);
            emailTxv = findViewById(R.id.email_txv);
            phoneCedit = findViewById(R.id.phone_cedit);
            birthYearTxv = findViewById(R.id.birth_year_txv);
            snapshotTxv = findViewById(R.id.snapshot_txv);
            textView2 = findViewById(R.id.textView2);
            costCtxv = findViewById(R.id.cost_ctxv);
            educationRlay = findViewById(R.id.education_rlay);
            educationCedt = findViewById(R.id.education_cedt);
            addEducationCtxv = findViewById(R.id.add_education_ctxv);
            educationRcv = findViewById(R.id.education_rcv);
            certificatesRlay = findViewById(R.id.certificates_rlay);
            certificatesCedt = findViewById(R.id.certificates_cedt);
            addCertificatesCedit = findViewById(R.id.add_certificates_cedit);
            certificatesRcv = findViewById(R.id.certificates_rcv);
            //specialityRlay = (RelativeLayout) findViewById(R.id.speciality_rlay);
            specialitiesCtxv = findViewById(R.id.specialities_ctxv);
            //specialityImv = (ImageView) findViewById(R.id.speciality_imv);
            uploadDocsCtxv = findViewById(R.id.upload_docs_ctxv);
            //uploadRightImv = (ImageView) findViewById(R.id.upload_right_imv);
            uploadInstructionTxv = findViewById(R.id.upload_instruction_txv);
            requestRlay = findViewById(R.id.request_rlay);
            requestCtxv = findViewById(R.id.request_ctxv);
            termsofUseCbox = findViewById(R.id.terms_of_use_cbox);
            specialityView = findViewById(R.id.speciality_view);

            fileListRcv = findViewById(R.id.file_list_rcv);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}
