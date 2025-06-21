package com.pacifyr.client.sign;

import android.app.AlertDialog;
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
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import dev.aige.wheelpicker.WheelPicker;
import dev.aige.wheelpicker.widgets.WheelYearPicker;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.ToastHandler;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
    private CircleImageView profileCimv;
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
    private  MainTextView termsCtxv;

    WheelYearPicker wheelYearPicker;

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
            wheelYearPicker = findViewById(R.id.wheel_date_picker_year);
            termsCtxv = findViewById(R.id.terms_of_use);
            checkBox = findViewById(R.id.checkBox);
            profileCimv = findViewById(R.id.profile_cimv);
            phoneCedt = findViewById(R.id.phone_cedit);
            spinner = findViewById(R.id.spinner);
            phone_llay = findViewById(R.id.phone_llay);

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
                }
                yearEdit.setText("Enter Year of Birth");

                phone_llay.setVisibility(View.VISIBLE);
                phoneEdit.setVisibility(View.GONE);
            }
            phoneEdit.setFocusable(false);
            phoneEdit.setKeyListener(null);

            profileCimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();      }
            });

            yearEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yearEdit.setVisibility(View.GONE);
                    wheelYearPicker.setVisibility(View.VISIBLE);

                    wheelYearPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(WheelPicker picker, Object data, int position) {
                            yearEdit.setText(data.toString());
                            yearEdit.setVisibility(View.VISIBLE);
                            yearEdit.setVisibility(View.VISIBLE);
                            yearEdit.setVisibility(View.VISIBLE);
                            wheelYearPicker.setVisibility(View.GONE);
                        }
                    });


                }
            });

           /* Spannable word = new SpannableString("I have read, understand, and agree to be bound by the ");

            word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            termsCtxv.setText(word);
            Spannable wordTwo = new SpannableString("Pacifyr Terms of Use");

            SpannableString ic_link = makeLinkSpan("Pacifyr Terms of Use", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // respond to click
                    Intent i=new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    i.putExtra("spurl", termOfUse);
                    startActivity(i);
                }
            });

            ic_link.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_pacifyr)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            termsCtxv.append(ic_link);*/

            String policyString = getResources().getString(R.string.register_terms_text);
            SpannableString spannableString = new SpannableString(policyString);
            //I have read, understand, and agree to be bound by the OMHealth Terms of Use
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    intent.putExtra("spurl", Constants.getUrlTerms(getApplicationContext()));
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.color_primary));
                    //ds.setUnderlineText(false);
                }
            };

            spannableString.setSpan(clickableSpan,54,79, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            termsCtxv.setText(spannableString);
            termsCtxv.setMovementMethod(LinkMovementMethod.getInstance());





            /*SpannableString ss = new SpannableString("I have read, understand, and agree to be bound by the Pacifyr Terms of Use");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent i=new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    i.putExtra("spurl", termOfUse);
                    startActivity(i);
                   *//* startActivity(new Intent(RegisterActivity.this, TermsOfUseActivity.class));*//*

                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, 54, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            termsCtxv.setText(ss);
            termsCtxv.setMovementMethod(LinkMovementMethod.getInstance());
            termsCtxv.setHighlightColor(getResources().getColor(R.color.blue_pacifyr));*/

           /* termsCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(RegisterActivity.this,TermsOfUseActivity.class);
                    i.putExtra("spurl", termOfUse);
                    startActivity(i);

                }
            });*/



            nameEdt.setFilters(new InputFilter[] {
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
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
            setClicks();
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("REGISTER",e.toString());
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
            defSetText(titleTxv, "Register");
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
                if (!isValidNameAndmail(nameEdt, emailEdt, "Enter valid name and email "))
                    return;
                if (!isValidName(nameEdt, "Please enter name"))
                    return;
                if (!isValidEmail1(emailEdt, "Please enter a valid email"))
                    return;
                if (!isValidYear(yearEdit.getText().toString(), "You must be 18 years or older to use Pacifyr"))
                    return;
                if (!isValidCheckBox(checkBox, getResources().getString(R.string.agree_terms_alert)))
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
                    if (!isValidCheckBox(checkBox, getResources().getString(R.string.agree_terms_alert)))
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
            postRegisterModel.setDateOfBirth("01-Jan-"+yearEdit.getText().toString());
            postRegisterModel.setPhoneNumber(phoneRegister);
            // postRegisterModel.
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
        setAlertOKStatusInWhite("Invalid",error,"OK");
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
        //text.setError(error + "");
        setAlertOKStatusInWhite("Invalid",error,"OK");
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

    private boolean isValidCheckBox(CheckBox checkBox,String error) {
        if(checkBox.isChecked())
            return true;
        else
            setAlertOKStatusInWhite("Invalid",error,"OK");
        return false;
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
                            ToastHandler.newInstance(RegisterActivity.this).mustShowToast("Please try again Json is null");
                        } else {
                            final JSONObject json2 = new JSONObject(json.toString());
                            PRegistrationModel regModel = GsonUtils.getInstance().gsonToRegistrationModel(json2);
                            registrationModel = GsonUtils.getInstance().gsonToRegistrationModel(json2);
                            userModel = registrationModel.getUser();
                            appUserID=userModel.getId();
                            jsonString = json2.toString();
                            if (isValid(regModel,getResources().getString(R.string.please_try_again))) {

                                String stat=regModel.getStatus().toString();
                                if(isValid(stat,getResources().getString(R.string.please_try_again))) {

                                    if(stat.equals(getResources().getString(R.string.success))) {
                                        PrefManager.getInstance(RegisterActivity.this).saveCountry(countryData.getCountry());


                                        MRoleModel roleModel = userModel.getRoleModel();
                                        mainLabel = roleModel.getMainLabel();
                                        label=roleModel.getLabel();
                                        quickbloxNewLogin();

                                    }
                                    else{
                                        hideProgress();
                                        prefM.clear();
                                        setAlertOKStatusInWhite("Error", regModel.getMessage(), "OK");
                                        hideStatus();
                                        fbLogout();
                                        startActivity(new Intent(context,SignActivity.class));
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
                            }
                        }
                    } catch (Exception e2) {
                        hideProgress();
                        e2.printStackTrace();
                        prefM.clear();
                        startActivity(new Intent(context,SignActivity.class));
                        ToastHandler.newInstance(RegisterActivity.this).mustShowToast(getResources().getString(R.string.please_try_again));
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

        if (isValid(userModel.getPhoneNumber()))
            prefM.savePhoneNumber(userModel.getPhoneNumber());

        prefM.saveUserFirstName(userModel.getFirstName());
        prefM.saveUserUserLastName(userModel.getLastName());
        prefM.saveMail(userModel.getEmail());
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

/*    private void quickbloxLogin() {
        try {

            QBSettings.getInstance().init(RegisterActivity.this, APP_ID, AUTH_KEY, AUTH_SECRET);
            QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
            //create a quickblox application session
            QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle params) {
                    // success
                  *//*  Toast.makeText(RegisterActivity.this, "AppChatNotUSe session created!",
                            Toast.LENGTH_LONG).show();*//*
                    PrefManager prefM = PrefManager.getInstance(RegisterActivity.this);
                    final String userid=appUserID;
                    String emailID=prefM.getEmail();
                    String fullName=prefM.getUserName();

                    // Register new user
                    final QBUser qbUser = new QBUser(userid, userid);
                    qbUser.setEmail(emailID);
                    qbUser.setFullName(fullName);

                    QBUsers.signUp(qbUser, new QBEntityCallbackImpl<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbuser, Bundle args) {
                            // success
                           *//* Toast.makeText(RegisterActivity.this, "User signed up!",
                                    Toast.LENGTH_LONG).show();*//*
                            sharedpreferences = getSharedPreferences(mypreference, Context.
                                    MODE_PRIVATE);
                            qbID=qbuser.getId().toString();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            getPrefManager(RegisterActivity.this).saveQBID(qbID);
                            editor.commit();
                            updateQBUserID();
                         }

                        @Override
                        public void onError(QBResponseException responseException) {
                            super.onError(responseException);
                            responseException.printStackTrace();
                            hideProgress();

                        }
                    });
                }

            });
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }

    }*/


}