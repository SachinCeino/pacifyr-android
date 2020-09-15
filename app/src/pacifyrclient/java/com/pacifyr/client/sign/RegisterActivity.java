package com.pacifyr.client.sign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.pacifyr.client.adapter.ListFileAdapter;

import com.pacifyr.client.model.ModelPacifyrDocuments;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Certification;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Education;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRoleModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PRegistrationModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.facebook.login.LoginManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.ion.Ion;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.adapter.CountrySpinnerAdapter;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.home.PacifyrDashboardActivity;
import com.pacifyr.client.home.WelcomeTourActivity;
import com.pacifyr.client.model.ItemData;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.App;
import com.pacifyr.client.quickblox.chat.utils.qb.callback.QbEntityCallbackImpl;
import com.pacifyr.client.quickblox.util.QBResRequestExecutor;
import com.pacifyr.client.quickblox.utils.QBEntityCallbackImpl;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.FirebaseLogging;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.Utility;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.ACCOUNT_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.APP_ID;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_KEY;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.AUTH_SECRET;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyr.client.network.NetworkService.countryData;
import static com.pacifyr.client.sign.RegisterVerifyActivity.phoneRegister;

//import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.termOfUseUrl;

/**
 * Created by ceino on 11/7/17.
 */

public class RegisterActivity extends PacifyrActivity {


    /*
    old appp id
    public static final String APP_ID = "61377";
    public static final String AUTH_KEY = "aeWG-3nzAC7M8uA";
    public static final String AUTH_SECRET = "xDWEpzRqvHZ7Ck8";
    public static final String ACCOUNT_KEY = "g1v8usiw9ynJzqxhRuyn";*/

//dev
/*    public static final String APP_ID = "60603";
    public static final String AUTH_KEY = "RMyP9v64ke8ccet";
    public static final String AUTH_SECRET = "6TAfArmeSzME8su";
    public static final String ACCOUNT_KEY = "xMeisck3BS_X6xbjYTQi";
    public static final String ACCOUNT_ID = "77234";
    public static final String TURN_SERVER = "turnserver.quickblox.com";*/

//prod
/*    public static final String APP_ID = "61574";
    public static final String AUTH_KEY = "2qzAw4EjRen6JeC";
    public static final String AUTH_SECRET = "jjpWMb9HRUVnTj7";
    public static final String ACCOUNT_KEY = "xMeisck3BS_X6xbjYTQi";
    public static final String ACCOUNT_ID = "77234";
    public static final String TURN_SERVER = "turnserver.quickblox.com";*/


    public static String qbUsrName,qbPassWord;

    public final String API_DOMAIN = "https://api.quickblox.com";
    public final String CHAT_DOMAIN = "chat.quickblox.com";
    public final String S3BUCKET = "qbprod";

    public static String appUserID;
    public static QBUser user;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private ImageView imgBack;
    private ImageView profileCimv;
    private RelativeLayout logoRlay;
    private ImageView logoBlue;
    private RelativeLayout hintLlay;
    public QBChatService chatService;
    private CustomEditText nameEdt;
    private CustomEditText emailEdt;
    private LinearLayout nextLlay;
    private RelativeLayout nextRlay;
    private MainTextView nextCtxv;
    private CustomEditText phoneEdit;
    private CustomEditText yearEdit;
    private MainTextView termsCtxv;
    private EditText terms_of_use_cbox_err,year_edt_err;



    private String qbID;
    //private String accessToken;

    LinearLayout phone_llay;
    Spinner spinner;
    CustomEditText phoneCedt;

    CheckBox checkBox;

    Bitmap edit_image;

    String fbFirstName,fbLastName,fbMail,fbImage;
    public String Remail="",Rfullname="";

    //LoopView loopView;
    //

    private String phoneNum;
    private boolean isFacebook;
    private PRegistrationModel registrationModel;
    private MUser userModel;
    public  PrefManager prefM;
    private String mainLabel;
    private String label;
    private String jsonString;

    private Context context;
    private  LinearLayout upload_layout;
    private TextView upload_textView;
    private  TextView add_photo_textView;
    private  TextView terms_of_use,privacy_ctxv;
    private TextView TextView_Upload;

    /** docs upload**/
    private RelativeLayout uploadDocLayout;
    private EditText upload_docs_ctxv;
    private int FILE_REQUEST=9;
    public static ArrayList<ModelPacifyrDocuments> documentFileList;
    private RecyclerView file_list_rcv;
    private RecyclerView.Adapter fileAdapter;
    public ArrayList<Certification> certificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_register);
            onSetActionBar();
            prefM = PrefManager.getInstance(RegisterActivity.this);
            imgBack = findViewById(R.id.img_back);
            logoRlay = findViewById(R.id.logo_rlay);
            logoBlue = findViewById(R.id.logo_blue);
            hintLlay = findViewById(R.id.hint_llay);
            nameEdt = findViewById(R.id.name_edt);
            emailEdt = findViewById(R.id.email_edt);
            nextLlay = findViewById(R.id.next_llay);
            nextRlay = findViewById(R.id.next_rlay);
            nextCtxv = findViewById(R.id.next_ctxv);
            phoneEdit = findViewById(R.id.phone_edt);
            yearEdit = findViewById(R.id.year_edt);
            checkBox = findViewById(R.id.checkBox);
            profileCimv = findViewById(R.id.profile_cimv);
            phoneCedt = findViewById(R.id.phone_cedit);
            spinner = findViewById(R.id.spinner);
            phone_llay = findViewById(R.id.phone_llay);
            privacy_ctxv = findViewById(R.id.privacy_ctxv);



            upload_layout = findViewById(R.id.upload_layout);
            TextView_Upload = findViewById(R.id.TextView_Upload);
            upload_textView= findViewById(R.id.upload_textView);
            add_photo_textView = findViewById(R.id.add_photo_textView);
            terms_of_use_cbox_err = findViewById(R.id.terms_of_use_cbox_err);
            year_edt_err = findViewById(R.id.year_edt_err);


            uploadDocLayout = findViewById(R.id.uploadDocLayout);
            upload_docs_ctxv = findViewById(R.id.upload_docs_ctxv);
            documentFileList = new ArrayList<ModelPacifyrDocuments>();
            file_list_rcv = findViewById(R.id.file_list_rcv);

            context = RegisterActivity.this;

            ArrayList<ItemData> list=new ArrayList<>();
            list.add(new ItemData("+1","US",R.drawable.flag_usa));
            list.add(new ItemData("+1","Canada",R.drawable.canada2));
            list.add(new ItemData("+91","India",R.drawable.ic_india));
            list.add(new ItemData("+971","UAE", R.drawable.flag_uae2));

            CountrySpinnerAdapter adapter=new CountrySpinnerAdapter(this,
                    R.layout.spinner_item,R.id.txt,list);
            spinner.setAdapter(adapter);

            String countryCode = prefM.getCountryCode();

            if(countryCode.equals("IN")){
                spinner.setSelection(2);
            }else if (countryCode.equals("US")){
                spinner.setSelection(0);
            }else if (countryCode.equals("CA")){
                spinner.setSelection(1);
            }
            else if (countryCode.equals("AE")){
                spinner.setSelection(3);
            }

            // loopView=(LoopView) findViewById(R.id.loop_view);

            Bundle b = getIntent().getExtras();
            if(isValid(b.getString("from"))){
                if(b.getString("from").equals("facebook")){
                    fbFirstName = b.getString("firstName");
                    fbLastName = b.getString("lastName");
                    fbMail = b.getString("email");
                    fbImage = b.getString("imageUrl");
                    isFacebook=true;
                }
            }

            if(isValid(phoneRegister)) {
                phoneEdit.setText(phoneRegister);
            }
            if(isFacebook){
                nameEdt.setText(fbFirstName+ " "+fbLastName);
                disableEditText(nameEdt);
                if(isValid(fbMail)){
                    emailEdt.setText(fbMail);
                    disableEditText(emailEdt);
                }else {
                    enableEditText(emailEdt);
                }

                if(isValid(fbImage)){
                    Picasso.with(RegisterActivity.this).load(fbImage).placeholder(R.drawable.placeholder_user).into(profileCimv);
                    upload_textView.setText("Edit photo");
                    add_photo_textView.setVisibility(View.GONE);
                    upload_textView.setVisibility(View.VISIBLE);
                }
                yearEdit.setHint("Enter Year of Birth");

                phone_llay.setVisibility(View.VISIBLE);
                phoneEdit.setVisibility(View.GONE);
            }
            phoneEdit.setFocusable(false);
            phoneEdit.setKeyListener(null);

            add_photo_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upload_layout.performClick();
                }
            });
            upload_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();      }
            });

            yearEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    year_edt_err.setError(null);
                    showDialog(yearEdit, "");
                }
            });

            emailEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    emailEdt.setError(null);
                }
            });






            nameEdt.setFilters(new InputFilter[] {
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
                            nameEdt.setError(null);
                            // TODO Auto-generated method stub
                            if(cs.equals("")){ // for backspace
                                return cs;
                            }
                            if(cs.toString().matches("[a-zA-Z ]+")){
                                return cs;
                            }
                            return "";
                        }
                    }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        terms_of_use_cbox_err.setError(null);
                    }
                }
            });


            SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                    "I have read, understood and agree to be bound by the ");
            spanTxt.append(Html.fromHtml("<font color=" + getResources().getColor(R.color.colorPrimary) + "> <u>" + "Terms of use" + "</u></font>"));
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                    Intent intent = new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    if (isValid(countryData) && countryData.getCountry().equals("India") || countryData.getCountry().equals("Wakanda"))
                    intent.putExtra("spurl", Constants.getUrlTermsIndia(getApplicationContext()));
                    else
                        intent.putExtra("spurl", Constants.getUrlTerms(getApplicationContext()));
                    intent.putExtra("title", "Terms of Use");
                    startActivity(intent);
                }
            }, spanTxt.length() - "Terms of use".length(), spanTxt.length(), 0);
            spanTxt.append(", ");
            spanTxt.append(Html.fromHtml("<font color=" + getResources().getColor(R.color.colorPrimary) + "> <u>" + "Privacy policy" + "</u></font>"));
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    if (isValid(countryData) && countryData.getCountry().equals("India") || countryData.getCountry().equals("Wakanda"))
                    intent.putExtra("spurl", Constants.getUrlPrivacyPolicyIndia(getApplicationContext()));
                    else
                        intent.putExtra("spurl", Constants.getUrlPrivacyPolicy(getApplicationContext()));
                    intent.putExtra("title", "Privacy Policy");
                    startActivity(intent);
                }
            }, spanTxt.length() - "Privacy policy".length(), spanTxt.length(), 0);

            checkBox.setMovementMethod(LinkMovementMethod.getInstance());
            checkBox.setText(spanTxt, TextView.BufferType.SPANNABLE);


            uploadDocLayout.setVisibility(View.VISIBLE);
            TextView_Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivityForResult(new Intent(context, UploadActivity.class), FILE_REQUEST);

                }
            });
            setClicks();


        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("REGISTER",e.toString());
        }
    }



    public void showDialog(final CustomEditText textView, String string){
        try {
            Calendar calendar =Calendar.getInstance();
            calendar.add(Calendar.YEAR,-18);

            SingleDateAndTimePickerDialog.Builder mSingleDateAndTimePickerDialog =   new SingleDateAndTimePickerDialog.Builder(this);
            mSingleDateAndTimePickerDialog.bottomSheet();
            mSingleDateAndTimePickerDialog.backgroundColor(getResources().getColor(R.color.grey_light));
            mSingleDateAndTimePickerDialog.titleTextColor(getResources().getColor(R.color.colorPrimary));
            mSingleDateAndTimePickerDialog.mainColor(getResources().getColor(R.color.black));
            mSingleDateAndTimePickerDialog.displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {
                            picker.selectDate(calendar);
                        }
                    });
            mSingleDateAndTimePickerDialog.displayMinutes(false);
            mSingleDateAndTimePickerDialog.displayHours(false);
            mSingleDateAndTimePickerDialog.displayDays(false);
            mSingleDateAndTimePickerDialog.maxDateRange(calendar.getTime());
            mSingleDateAndTimePickerDialog.displayMonth(true);
            mSingleDateAndTimePickerDialog.displayYears(true);
            mSingleDateAndTimePickerDialog.displayDaysOfMonth(true);
            mSingleDateAndTimePickerDialog.listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {
                            SimpleDateFormat postFormater = new SimpleDateFormat("dd-MMM-yyyy");
                            String newDateStr = postFormater.format(date);
                            Log.w("@@@Date",newDateStr);
                            yearEdit.setText(newDateStr);
                        }

                    })
                    .display();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }
    private void makeLinksFocusable(MainTextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setBackgroundColor(Color.WHITE);
    }


    public static final int GALLERY = 1;
    public static final int CAMERA = 2;

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    boolean checkPermission = Utility.checkPermission(RegisterActivity.this, "camera");
                    if(checkPermission) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    boolean checkPermission = Utility.checkPermission(RegisterActivity.this, "gallery");
                    if(checkPermission) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    upload_textView.setText("Edit photo");
                    upload_textView.setVisibility(View.VISIBLE);
                    add_photo_textView.setVisibility(View.GONE);

                }
                else if (requestCode == FILE_REQUEST) {
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
    public void  onActivityProfileEditGallery(Intent data)
    {
        try{
            edit_image= getBitmapGalleryExifCode(data);
            if(isValid(edit_image,"")) {
                profileCimv.setImageBitmap(edit_image);
                profileCimv.setImageBitmap(edit_image);
                upload_textView.setText("Edit photo");
                upload_textView.setVisibility(View.VISIBLE);

                add_photo_textView.setVisibility(View.GONE);
                // Bitmap rotateImage = getCroppedBitmap(image);
                //    postEventOnMainThread(new ImageChoosenEvent(rotateImage));
            }
        }
        catch (Exception e)
        {
            LogUtils.LOGD("jithish"," HA onActivityProfileEditGallery "+e.getMessage());
            com.pacifyr.client.utils.ToastHandler.newInstance(this).mustShowToast("Please Try Again ");
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
            com.pacifyr.client.utils.ToastHandler.newInstance(this).mustShowToast(" Please Try again ");
            //ToastHandler.newInstance(this).showToast(" Please Try again "+e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        edit_image=null;
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    private void setClicks() {
        try {
            nextRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validRegister();
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }

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
            rightTxv.setVisibility(View.INVISIBLE);
            rightLlay.setVisibility(View.INVISIBLE);
            leftTxv.setVisibility(View.INVISIBLE);
            backImv.setVisibility(View.VISIBLE);
            titleTxv.setVisibility(View.VISIBLE);
            leftLlay.setVisibility(View.VISIBLE);
            defSetText(titleTxv, getResources().getString(R.string.register));
            Typeface type = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.avantica_bold_header));
            titleTxv.setTypeface(type);
            leftLlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void validRegister() {

        try{

            PostRegisterModel postRegisterModel=new PostRegisterModel();
            if(!isFacebook) {
                if (!isValidName(nameEdt, "Please enter name"))
                    return;
                if (!isValidEmail1(emailEdt, "Please enter a valid email"))
                    return;
                if (!isValidYear(yearEdit.getText().toString(), "You must be 18 years or older to use Pacifyr"))
                    return;
                if (!isValidCheckBox(terms_of_use_cbox_err,checkBox, getResources().getString(R.string.agree_terms_alert)))
                    return;
                postRegisterModel.setLoginType("manual");
                postRegisterModel.setFirstName(nameEdt.getText().toString());
                postRegisterModel.setEmail(emailEdt.getText().toString());
                postRegisterModel.setLastName("");
                Rfullname=nameEdt.getText().toString();
                Remail=emailEdt.getText().toString();
            }else{
                if(isValid(phoneCedt)) {
                    if(!isValid(fbMail)){
                        if (!isValidEmail1(emailEdt, "Please enter a valid email"))
                            return;
                    }
                    if (!isValidCheckBox(terms_of_use_cbox_err,checkBox, getResources().getString(R.string.agree_terms_alert)))
                        return;
                    if (isValidMobile(phoneCedt.getText().toString())) {
                        countryData = (ItemData) spinner.getSelectedItem();
                        String code = countryData.getCode();
                        phoneRegister = code + phoneCedt.getText();
                        postRegisterModel.setLoginType("facebook");
                        postRegisterModel.setFirstName(fbFirstName);
                        if(isValid(fbMail)){
                            postRegisterModel.setEmail(fbMail);
                        }else{
                            postRegisterModel.setEmail(emailEdt.getText().toString());
                        }
                        postRegisterModel.setLastName(fbLastName);
                        Rfullname=fbFirstName+" "+fbLastName;
                        Remail=fbMail;
                        if(isValid(fbImage))
                        {
                            postRegisterModel.setImage(fbImage);
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this,"Please Valid enter phone number.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"Please enter phone.",Toast.LENGTH_LONG).show();
                }
            }

            postRegisterModel.setCountry(countryData.getCountry());
            postRegisterModel.setDateOfBirth(yearEdit.getText().toString());
            postRegisterModel.setPhoneNumber(phoneRegister);

            if(uploadDocLayout.getVisibility()==View.VISIBLE) {

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

            }



            // postRegisterModel.
            Gson gson = new Gson();
            // gson .setExclusionStrategies(new TestExclStrat())
            String json = gson.toJson(postRegisterModel);
            Log.w("@@@",String.valueOf(json));
            registerWeb(postRegisterModel);

        }catch (Exception e)
        {
            LogUtils.LOGD("sign","lA validRegister exception "+e.getMessage());
            e.printStackTrace();
        }
    }

    public Boolean isValidName(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return true;
        text.setError(error);
        text.requestFocus();
      //  setAlertOKStatusInWhite("Invalid",error,"OK");
        //text.setError(error + "");
        return false;

    }

    public Boolean isValidNameAndmail(EditText name,EditText mail, String error) {
        boolean flag=false,e=false;
        if (name != null)
            if (isValid(name.getText() + ""))
                flag=true;
        if (mail != null)
            if (isValid(mail.getText() + ""))
                if (isValidEmail(mail.getText() + ""))
                    flag=true;
        if(flag){
            return true;
        }
        else {
            setAlertOKStatusInWhite("Invalid", error, "OK");
            //text.setError(error + "");
            return false;
        }

    }

    public Boolean isValidEmail1(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                if (isValidEmail(text.getText() + ""))
                    return true;
        text.setError(error + "");
        text.requestFocus();
       // setAlertOKStatusInWhite("Invalid",error,"OK");
        return false;
    }

    public void setAlertOKStatusInWhite(String title,String desc,String ok) {
        try {
            //View.OnClickListener oklistner
            View view = LayoutInflater.from(this).inflate(R.layout.alert_white_ok, null, false);
            MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
            MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
            MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);

            if (isValid(title))
                defSetText(title_dtxv, title);
            else
                title_dtxv.setVisibility(View.GONE);
            defSetText(desc_dtxv, desc);
            ok_dtxv.setText(ok);
            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            //setAlertOKStatusInWhite("Register Failed", "Your mobile number is already registered", "Ok");
            ok_dtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();
                    hideProgress();
                }
            });
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private boolean isValidCheckBox(EditText terms_of_use_cbox_err,CheckBox checkBox,String error) {
        if(checkBox.isChecked())
            return true;
        else
            terms_of_use_cbox_err.setError(error);
        terms_of_use_cbox_err.requestFocus();
            //setAlertOKStatusInWhite("Invalid",error,"OK");
        return false;
    }

    public Boolean isValidYear(String text, String error) {
        if (text.equals(""))
        {
            year_edt_err.setError("Please enter year of birth");
            year_edt_err.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
        /*else {
            if (text != null)
                if (android.text.TextUtils.isDigitsOnly(text))
                    if (isValid(text + ""))
                        if (isValidMajor(text))
                            return true;


            year_edt_err.setError("Enter valid year of birth");
            year_edt_err.requestFocus();
           *//* yearEdit.setError(error + "");
            yearEdit.requestFocus();*//*
        }
       // setAlertOKStatusInWhite("Invalid",error,"OK");*/


    }

    private boolean isValidMajor(String s) {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        Integer birthYear=Integer.parseInt(s);
        int diff=currentYear-birthYear;
        return diff > 18;
    }

    private void registerWeb(final PostRegisterModel regpost) {
        try {
            setLoadingWait();
            showProgress();
            // Gson gson = new GsonBuilder().setExclusionStrategies(new TestExclStrat())
            //.serializeNulls() <-- uncomment to serialize NULL fields as well
            //.create();
            Gson gson = new Gson();
            // gson .setExclusionStrategies(new TestExclStrat())
            String json = gson.toJson(regpost);
            //{"email":"samsad@ceinotech.com","firstName":"samsd","lastName":"","loginType":"manual","password":"samsad"}
            com.koushikdutta.async.future.FutureCallback<JsonObject> callback2 = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, final JsonObject json) {
                    try {
                        //String message = json.get("message");
                        String status = json.get(getResources().getString(R.string.status)).getAsString();
                        if (json == null || status.equals(getResources().getString(R.string.error))) {
                            hideProgress();
                            String message = json.get(getResources().getString(R.string.message_string)).getAsString();
                            setAlertOKStatusInWhite(getResources().getString(R.string.registration_failed), message,"Ok");
                           // ToastHandler.newInstance(RegisterActivity.this).mustShowToast("Please try again Json is null");
                        } else {
                            final JSONObject json2 = new JSONObject(json.toString());
                            PRegistrationModel regModel = GsonUtils.getInstance().gsonToRegistrationModel(json2);
                            registrationModel = GsonUtils.getInstance().gsonToRegistrationModel(json2);
                            userModel = registrationModel.getUser();
                            appUserID=userModel.getId();
                            jsonString = json2.toString();
                            if (isValid(regModel,getResources().getString(R.string.please_try_again))) {

                                String stat = regModel.getStatus();
                                if(isValid(stat,getResources().getString(R.string.please_try_again))) {

                                    if(stat.equals(getResources().getString(R.string.success))) {
                                        PrefManager.getInstance(RegisterActivity.this).saveCountry(countryData.getCountry());


                                        MRoleModel roleModel = userModel.getRoleModel();
                                        mainLabel = roleModel.getMainLabel();
                                        label=roleModel.getLabel();
                                        quickbloxNewLogin();

                                        Bundle args =
                                                FirebaseLogging.getConstantArgsLoggedOut(RegisterActivity.this);
                                        if (userModel != null) {
                                            args.putString(FirebaseLogging.EC,
                                                    userModel.getId() + "-" + userModel.getFirstName());
                                        }
                                        FirebaseLogging.sendLog(RegisterActivity.this,
                                                FirebaseLogging.EVENT_SIGNUP_SUCCESS, args);

                                    }
                                    else{
                                        hideProgress();
                                        prefM.clear();
                                        setAlertOKStatusInWhite("Error", regModel.getMessage(), "OK");
                                        hideStatus();
                                        fbLogout();
                                        startActivity(new Intent(context,SignActivity.class));

                                        Bundle args =
                                                FirebaseLogging.getConstantArgsLoggedOut(RegisterActivity.this);
                                        if (regModel != null && regModel.getMessage() != null)
                                            args.putString(FirebaseLogging.EC, regModel.getMessage());
                                        FirebaseLogging.sendLog(RegisterActivity.this,
                                                FirebaseLogging.EVENT_SIGNUP_FAIL, args);
                                    }
                                }

                            }
                            else{
                                hideProgress();
                                prefM.clear();
                                setAlertOKStatusInRed(getResources().getString(R.string.registration_failed), getResources().getString(R.string.failed_register_message), "OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hideStatus();
                                    }
                                });
                                startActivity(new Intent(context,SignActivity.class));
                                Bundle args =
                                        FirebaseLogging.getConstantArgsLoggedOut(RegisterActivity.this);
                                args.putString(FirebaseLogging.EC, "Number already registered");
                                FirebaseLogging.sendLog(RegisterActivity.this,
                                        FirebaseLogging.EVENT_SIGNUP_FAIL, args);
                            }
                        }
                    } catch (Exception e2) {
                        hideProgress();
                        e2.printStackTrace();
                        prefM.clear();
                        startActivity(new Intent(context,SignActivity.class));
                        ToastHandler.newInstance(RegisterActivity.this).mustShowToast(getResources().getString(R.string.please_try_again));
                        Bundle args =
                                FirebaseLogging.getConstantArgsLoggedOut(RegisterActivity.this);
                        args.putString(FirebaseLogging.EC, e2.getMessage());
                        FirebaseLogging.sendLog(RegisterActivity.this,
                                FirebaseLogging.EVENT_SIGNUP_FAIL, args);
                    }
                }
            };
            String url= NetworkService.GLOBAL_URL+"register";
            Ion.with(RegisterActivity.this).load(url).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(callback2);
        } catch (Exception e){
            hideProgress();
            e.printStackTrace();
            prefM.clear();
            startActivity(new Intent(context,SignActivity.class));
        }
    }

    private void saveUserDetailsToPrefM() {
        prefM.saveCountry(userModel.getCountry());
        prefM.saveJsonKey(jsonString);
        prefM.saveUserID(userModel.getId());
        prefM.saveAccessToken(registrationModel.getAccessToken());
        prefM.saveCountry(registrationModel.getUser().getCountry());

        prefM.saveUserFirstName(userModel.getFirstName());
        prefM.saveUserUserLastName(userModel.getLastName());
        prefM.saveMail(userModel.getEmail());
        if (isValid(userModel.getPhoneNumber()))
            prefM.savePhoneNumber(userModel.getPhoneNumber());
        if (isValid(userModel.getImage())){
            prefM.saveProfileImageURL(userModel.getImage());
        }
    }

    private void updateQBUserID() {
        try {
            if(isValid(qbID)) {
                if (isValid(appUserID)) {
                    String url = GLOBAL_URL + "user/editprofile/" + appUserID;
                    if(isValid(edit_image))
                        updateProfileWithImage();
                    else
                        updateProfileWeb();

                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(RegisterActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    private void updateProfileWithImage() {
        try {
            setLoadingWait();
            showProgress();

            JSONObject postDataParams = new JSONObject();
            postDataParams.putOpt("qbid", Long.valueOf(qbID));
            String json = postDataParams.toString();
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
                        Toast.makeText(RegisterActivity.this, "please try again  ", Toast.LENGTH_SHORT);
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
            Toast.makeText(RegisterActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
            LogUtils.LOGD("session","AEA updateProfileMultipartWeb exce " + ex.getMessage());
            ex.printStackTrace();
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
                        }
                        saveProfileImageURL( NetworkService.mCurrentUser.getImageUrl());
                        getPrefManager(RegisterActivity.this).saveCountry(NetworkService.mCurrentUser.getCountry());
                        getPrefManager(RegisterActivity.this).saveUserFirstName( NetworkService.mCurrentUser.getFirstName());
                        getPrefManager(RegisterActivity.this).saveMail(NetworkService.mCurrentUser.getEmail());
                        getPrefManager(RegisterActivity.this).saveUserUserLastName(NetworkService.mCurrentUser.getLastName());

                    }catch (Exception e)
                    {
                        LogUtils.LOGD("session","AEA updateProfileMultipartWeb  exce " + e.getMessage());
                    }
                    hideProgress();
                    setStatus("User registered Successfully", R.color.md_green_400);
                    showStatus();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MUser user = registrationModel.getUser();
                            String compID = user.getCompanyId();
                            if (mainLabel.equals("pacifyr")) {
                                getPrefManager(RegisterActivity.this).saveIsOnline("true");
                                Intent i = new Intent(RegisterActivity.this, PacifyrDashboardActivity.class);
                                startActivity(i);
                                finishAffinity();
                            } else if (mainLabel.equals("customer")) {
                                //    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                                if (label.equalsIgnoreCase("employee")) {
                                    PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("employee");
                                    PrefManager.getInstance(RegisterActivity.this).saveCompanyID(compID);
                                    //PrefManager.getInstance(SignUpActivity.this).saveCompanyID(companyId);
                                    Intent i = new Intent(RegisterActivity.this, WelcomeTourActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                } else if (label.equalsIgnoreCase("customer")) {
                                    //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                    PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("customer");
                                    Intent i = new Intent(RegisterActivity.this, DiscountCodeActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                    //finish();
                                } else if (label.equalsIgnoreCase("paidMember")) {
                                    //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                    PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("paidMember");
                                    Intent i = new Intent(RegisterActivity.this, DiscountCodeActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                    //finish();
                                }
                            }
                           /* Intent i = new Intent(RegisterActivity.this, WelcomeTourActivity.class);
                            startActivity(i);
                            finish();*/
                        }
                    }, 1500);
                } else
                    com.pacifyr.client.utils.ToastHandler.newInstance(RegisterActivity.this).mustShowToast(" Registered. Please Try Login With your detils.");

            } catch (Exception e){
                //Crashlytics.logException(e);
                com.pacifyr.client.utils.ToastHandler.newInstance(RegisterActivity.this).mustShowToast("Please Try again" );
                // e.printStackTrace();
                LogUtils.LOGD("exception", "updateProfileMultipartWeb web json  " + e.getMessage());
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","AEA saveInformation exce " + e.getMessage());
        }
    }

    private void updateProfileWeb() {
        try {
            String url = GLOBAL_URL + "user/editprofile/" + appUserID;
            com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    try {
                        if (isValid(result)) {

                            final JSONObject jsonObject = new JSONObject(result.toString());
                            //String js = result.toString();
                            String phone = jsonObject.getString("phoneNumber");
                            //String messag= json.getString("message");
                            //String logstat = status.getMessage();
                            if (isValid(phone)) {
                                hideProgress();
                                setStatus("User registered Successfully", R.color.md_green_400);
                                showStatus();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       /* Intent i = new Intent(RegisterActivity.this, WelcomeTourActivity.class);
                                        startActivity(i);
                                        finish();*/
                                        hideStatus();
                                        MUser user = registrationModel.getUser();
                                        String compID = user.getCompanyId();

                                        if (mainLabel.equals("pacifyr")) {
                                            getPrefManager(RegisterActivity.this).saveIsOnline("true");
                                            Intent i = new Intent(RegisterActivity.this, PacifyrDashboardActivity.class);
                                            startActivity(i);
                                            finishAffinity();
                                        } else if (mainLabel.equals("customer")) {
                                            //    Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                                            if (label.equalsIgnoreCase("employee")) {
                                                PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("employee");
                                                PrefManager.getInstance(RegisterActivity.this).saveCompanyID(compID);
                                                //PrefManager.getInstance(SignUpActivity.this).saveCompanyID(companyId);
                                                Intent i = new Intent(RegisterActivity.this, WelcomeTourActivity.class);
                                                startActivity(i);
                                                finishAffinity();
                                            } else if (label.equalsIgnoreCase("customer")) {
                                                //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                                PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("customer");
                                                Intent i = new Intent(RegisterActivity.this, DiscountCodeActivity.class);
                                                startActivity(i);
                                                finishAffinity();
                                                //finish();
                                            } else if (label.equalsIgnoreCase("paidMember")) {
                                                //  ConnectToPacifyrActivity.start(SignUpActivity.this);
                                                PrefManager.getInstance(RegisterActivity.this).saveCustomerRole("paidMember");
                                                Intent i = new Intent(RegisterActivity.this, DiscountCodeActivity.class);
                                                startActivity(i);
                                                finishAffinity();
                                                //finish();
                                            }
                                        }


                                    }
                                }, 2000);

                            } else/* if (logstat.equals("error"))*/ {
                                Toast.makeText(RegisterActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception eee) {
                        hideProgress();
                        Toast.makeText(RegisterActivity.this, "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                        eee.printStackTrace();
                    }
                }
            };
            //AQuery aq = new AQuery(RegisterActivity.this);
            //cb.header("AccessToken", accessToken);
            //cb.header("enctype", "multipart/form-data");
            JSONObject postDataParams = new JSONObject();
            postDataParams.putOpt("qbid", Long.valueOf(qbID));
            String json = postDataParams.toString();
            HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
            map.put("data", postDataParams);
            Ion.with(context).load(url).setHeader("AccessToken", prefM.getAccessToken()).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();

        }
    }


    private void quickbloxNewLogin() {
        try {
            QBSettings.getInstance().init(RegisterActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
            //create a quickblox application session
            QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession qbSession, Bundle bundle) {
                    final String userid=appUserID;
                    String emailID=Remail;
                    String fullName=Rfullname;

                    // Register new user
                    final QBUser qbUser = new QBUser(userid, userid);
                    qbUser.setEmail(emailID);
                    qbUser.setFullName(fullName);

                    QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            saveUserDetailsToPrefM();
                            qbID=qbUser.getId().toString();
                            saveUserData(qbUser);
                            startLoginService(qbUser);
                            getPrefManager(RegisterActivity.this).saveQBID(qbID);
                            updateQBUserID();
                            quickBoxSignIn(userid);
                        }

                        @Override
                        public void onError(QBResponseException responseException) {
                            responseException.printStackTrace();
                            Toast.makeText(context,responseException.getMessage(),Toast.LENGTH_LONG).show();
                            hideProgress();
                            LoginManager mLoginManager = LoginManager.getInstance();
                            mLoginManager.logOut();
                            prefM.clear();
                            startActivity(new Intent(context,SignActivity.class));

                            Bundle args =
                                    FirebaseLogging.getConstantArgsLoggedOut(RegisterActivity.this);
                            if (responseException != null && responseException.getMessage() != null) {
                                args.putString(FirebaseLogging.EC,
                                        "QuickBlox login fail : " + responseException.getMessage());
                            }
                            FirebaseLogging.sendLog(RegisterActivity.this,
                                    FirebaseLogging.EVENT_SIGNUP_FAIL, args);
                        }
                    });

                }

                @Override
                public void onError(QBResponseException e) {
                    e.printStackTrace();
                    if (e.getHttpStatusCode() == 422 && e.getMessage().toLowerCase().contains("bad timestamp")) {
                        iOSDialogClickListener listener = new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                prefM.clearSharedAll();
                                fbLogout();
                                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                finish();
                            }
                        };
                        showIOSDialogOK("", "Your device date & time is not correct. Please correct it and try sign in.", "Settings", listener);

                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        hideProgress();
                        fbLogout();
                        prefM.clear();
                        startActivity(new Intent(context, SignActivity.class));
                    }
                }
            });

        }
        catch (Exception exc)
        {
            hideProgress();
            exc.printStackTrace();
            Toast.makeText(context,exc.getMessage(),Toast.LENGTH_LONG).show();
            fbLogout();
            prefM.clear();
            startActivity(new Intent(context,SignActivity.class));

        }

    }

    private void quickBoxSignIn(final String userId) {
        QBSettings.getInstance().init(RegisterActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBAuth.createSession().performAsync(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession result, Bundle params) {
                super.onSuccess(result, params);
                QBResRequestExecutor requestExecutor = App.getInstance().getQbResRequestExecutor();
                QBUser qbUser = new QBUser(userId, userId);
                requestExecutor.signInUser(qbUser, new QbEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle bundle) {
                        super.onSuccess(result, bundle);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        super.onError(e);
                    }
                });
            }

            @Override
            public void onError(QBResponseException responseException) {
                super.onError(responseException);
            }
        });
    }


    private void setFileAdapter() {
        if (documentFileList.size()>0) {
            file_list_rcv.setVisibility(View.VISIBLE);
            file_list_rcv.setLayoutManager(new GridLayoutManager(this, 5));
            fileAdapter = new ListFileAdapter(documentFileList, context);
            file_list_rcv.setAdapter(fileAdapter);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (isValidSize(documentFileList)){
            for (int i = 0; i < documentFileList.size(); i++) {
                if (!documentFileList.get(i).isUploaded())
                    documentFileList.remove(i);
            }
            setFileAdapter();
        }
    }
}