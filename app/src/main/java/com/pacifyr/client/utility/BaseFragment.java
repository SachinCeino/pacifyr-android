package com.pacifyr.client.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.network.GsonUtils;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyr.client.utils.ToastHandler;
import com.pacifyrapp.utilitylibrary.CustomEditText;
import com.pacifyrapp.utilitylibrary.CustomFontTextView;
import com.pacifyrapp.utilitylibrary.DeviceFitImageView;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.otto.Subscribe;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.network.NetworkService.modelAllEmotions;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.GLOBAL_URL;
import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.STRIPE_PUBLISH_KEY;


/**
 * Created by jithish on 1/12/16.
 */

public abstract class BaseFragment extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

        loadEmotions();

    }

    private void loadEmotions() {
        if (getActivity() != null) {
            String emotionJson =
                    PrefManager.getInstance(getActivity()).getSharedString(PrefManager.JSON_ALL_EMOTIONS, null);

            String positiveEmotionJson =
                    PrefManager.getInstance(getActivity()).getSharedString(PrefManager.JSON_POSITIVE_EMOTIONS,
                            null);
            String negativeEmotionJson =
                    PrefManager.getInstance(getActivity()).getSharedString(PrefManager.JSON_NEGATIVE_EMOTIONS,
                            null);

            if (emotionJson == null) {
                NetworkService.webListEmotions(getActivity());
            } else if (modelAllEmotions == null) {
                try {
                    JSONObject emotionJsonObj = new JSONObject(emotionJson);
                    modelAllEmotions = GsonUtils.getInstance().gsonToMListAllEmotionsI(emotionJsonObj);

                } catch (JSONException je) {
                    je.printStackTrace();
                }

            }

            if (positiveEmotionJson == null) {
                NetworkService.webListPositiveEmotions(getActivity());
            } else if (NetworkService.modelListPosEmotions == null) {
                try {
                    JSONObject posEmotionJsonObj = new JSONObject(positiveEmotionJson);
                    NetworkService.modelListPosEmotions =
                            GsonUtils.getInstance().gsonToMListPositiveEmotions(posEmotionJsonObj);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }

            if (negativeEmotionJson == null) {
                NetworkService.webListNegativeEmotions(getActivity());
            } else if (NetworkService.modelListNegEmotions == null) {
                try {
                    JSONObject neativeEmotionJsonObj = new JSONObject(negativeEmotionJson);
                    NetworkService.modelListNegEmotions =
                            GsonUtils.getInstance().gsonToMListNegativeEmotions(neativeEmotionJsonObj);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }
    }

    @Subscribe
    public abstract void onNotifyEvent(NotifyEvent event);

    public abstract void onSetValues(View view) throws Exception;

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }
    public Boolean isValidNotNull(List list) {
        return list != null;

    }

    public Boolean isValidSize(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public Boolean isValid(int object) {
        return object > 0;

    }
    public Boolean isValidhtml(String text) {
        if (isValid(text)) return text.contains("http") || text.contains("https");
        return false;

    }
    public Boolean isValid(Object object, String error) {
        if(object != null) return true;
        ToastHandler.newInstance(getActivity()).mustShowToast("" + error);
        return false;

    }
    public Boolean isValidAlert(Object object, String error) {
        return object != null;
        //  setAlertInWhiteBox("",error,"OK");

    }
    public Boolean isValidAlert(List list, String error) {
        if (list != null) return list.size() > 0;
        // setAlertInWhiteBox("",error,"OK");
        return false;

    }

    public Object checkedObject(Object object) {
        if(object != null) return object;
        return object;

    }
    /*  public Boolean isValid(EditText text, String error) {
          if(text != null) if(isValid(text.getText() + "")) return true;
          text.setError(error + "");

          return false;

      }*/
    public Boolean isValid(EditText text, String error) {
        if (text != null) return isValid(text.getText() + "");
        //  setAlertInWhiteBox("",error,"OK");
        // text.setError(error + "");

        return false;

    }
    public Boolean isValid(EditText text) {
        if (text != null) return isValid(text.getText() + "");
        //  setAlertInWhiteBox("",error,"OK");
        // text.setError(error + "");

        return false;

    }
    public Boolean isValidPlusWarning(EditText text, String error) {
        if (text != null) return isValid(text.getText() + "");
        //  setAlertStatusMessage("Warning!",error,"Ok");
        return false;

    }

    public Boolean isValid(String text, String error) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        // ToastHandler.newInstance(getActivity()).mustShowToast("" + error);
        return false;

    }

    public Boolean isValid(boolean text, String error) {
        if(text) return true;
        ToastHandler.newInstance(getActivity()).mustShowToast("" + error);
        return false;

    }

    public Boolean isValid(List list, String error) {
        if (list != null) return list.size() > 0;
        //  setAlertStatusMessage("Warning", error,"Ok");
        // ToastHandler.newInstance(getActivity()).mustShowToast(""+error);
        return false;

    }

    public Boolean isValid(Date date) {
        return date != null;


    }




    public Boolean isEquals(String pwd, String pwd2) {

        return pwd2.equals(pwd);


    }

    public Boolean isValid(String pwd, String pwd2, String error) {

        if(pwd2.equals(pwd)) return true;

        ToastHandler.newInstance(getActivity()).mustShowToast(error);
        return false;

    }

    public Boolean isValidEmail(EditText text, String error) {
        if(text != null)
            if(isValid(text.getText() + "")) if(isValidEmail(text.getText() + "")) return true;
        text.setError(error + "");
        return false;

    }

    public Boolean isValidPassword(EditText text, String error) {
        if(text != null)
            if(isValid(text.getText() + "")) if(isValidPassword(text.getText() + "")) return true;
        text.setError(error + "");
        return false;

    }

    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        return pass != null && pass.length() > 3;
    }



    public Boolean isErrorStatus(String object) {
        if (isValid(object)) return object.trim().equalsIgnoreCase("error");
        return false;

    }

    public String defString(EditText text, String def) {
        if(text != null) if(isValid(text.getText() + "")) return text.getText() + "";
        return def;

    }

    public String defString(EditText text) {
        if(text != null) if(isValid(text.getText() + "")) return text.getText() + "";
        return "";

    }

    public String defString(String text, String def) {
        if(text != null) return text;
        return def;

    }

    public String defString(String text) {
        if(text != null) return text;
        return "";

    }


    public void defSetText(TextView textv,String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(TextView textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    public void defSetText(EditText textv,String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(EditText textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    public KProgressHUD hud_progress = null;
    public KProgressHUD hud_status = null;
    public void setLoading() {
//        try {
//            hud_progress = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void setLoadingWait() {
//        try {
//            hud_progress = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setLoading(String message) {
//        try {
//            hud_progress = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("" + message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public void showStatus() {
        if (getActivity() instanceof PacifyrActivity) {
            if (!((PacifyrActivity) getActivity()).isShowDialogs()) {
                return;
            }
        }
        if(hud_status != null) if(!hud_status.isShowing()) hud_status.show();
    }

    public void hideStatus() {
        if(hud_status != null) if(hud_status.isShowing()) hud_status.dismiss();
    }
    public void hideProgress() {
        if(isValid(hud_progress))if(hud_progress.isShowing()) hud_progress.dismiss();
    }

    public void showProgress() {
//        if (getActivity() instanceof PacifyrActivity) {
//            if (!((PacifyrActivity) getActivity()).isShowDialogs()) {
//                return;
//            }
//        }
//        if(isValid(hud_progress)) if(!hud_progress.isShowing()) hud_progress.show();
    }
    public void setStatus(String status, int color) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.status_dialog, null, false);
        CustomFontTextView status_dtxv = view.findViewById(R.id.status_dtxv);
        status_dtxv.setText("" + status);
        hideStatus();
        hud_status = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(getActivity().getResources().getColor(color)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

    }




    public void setAqueryImage(AQuery aqueryImage, String urllast, int drawable)
    {
        try{
            aqueryImage.image(GLOBAL_IMAGE_URL + urllast,true,true,0,0,drawableToBitmap(getActivity().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAqueryImage(AQuery aqueryImage, String urllast, int drawable, Boolean cached)
    {
        try{
            aqueryImage.image(GLOBAL_IMAGE_URL+ urllast,cached,cached,0,0,drawableToBitmap(getActivity().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setAqueryImage(AQuery aqueryImage, ImageView imageView, String urllast, int drawable)
    {
        try{
            aqueryImage.id(imageView).image(GLOBAL_IMAGE_URL + urllast,true,true,0,0,drawableToBitmap(getActivity().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setAqueryImage(ImageView img, String urllast, int drawable) {

        try {
            AQuery aQuery = new AQuery(img);
            if(!isValid(urllast))
                aQuery.image(drawableToBitmap(getActivity().getResources().getDrawable(drawable)));
            else
                aQuery.image(GLOBAL_IMAGE_URL + urllast, true, true, 0, 0, drawableToBitmap(getActivity().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public void setAqueryImage(ImageView img, String urllast, int drawable, Boolean cached) {

        try {
            AQuery aqueryImage = new AQuery(img);
            if(!isValid(urllast))
                aqueryImage.image(drawableToBitmap(getActivity().getResources().getDrawable(drawable)));
            else
                aqueryImage.image(GLOBAL_IMAGE_URL + urllast, cached, cached, 0, 0, drawableToBitmap(getActivity().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }

    }

    public void setAqueryImage(ImageView img, ImageView imageView, String urllast, int drawable) {
        try {
            AQuery aqueryImage = new AQuery(img);
            if(!isValid(urllast))
                aqueryImage.image(drawableToBitmap(getActivity().getResources().getDrawable(drawable)));
            else
                aqueryImage.id(imageView).image(GLOBAL_IMAGE_URL + urllast, true, true, 0, 0, drawableToBitmap(getActivity().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public MUser convertModel(ModelUserProfile pacifyerDetial) {
        MUser mUser=null;
        try{

            mUser=new MUser();
            mUser.setId(pacifyerDetial.getId());
            mUser.setAvailableMinutes(pacifyerDetial.getAvailableMinutes());
            mUser.setQbid(pacifyerDetial.getQbid());
            mUser.setFirstName(pacifyerDetial.getFirstName());
            mUser.setOnline(pacifyerDetial.getOnline());
            mUser.setCurrentlyAvailable(pacifyerDetial.getCurrentlyAvailable());
            mUser.setLastName(pacifyerDetial.getLastName());
            mUser.setImage(pacifyerDetial.getImage());
            mUser.setEmotion(pacifyerDetial.getEmotion());
            mUser.setEmotionId(pacifyerDetial.getEmotionId());
            mUser.setDescription(pacifyerDetial.getDescription());
            mUser.setSpecialities(pacifyerDetial.getSpecialities());
            //  mUser.setCertification(pacifyerDetial.getCertification());
            mUser.setEmail(pacifyerDetial.getEmail());
            mUser.setPhoneNumber(pacifyerDetial.getPhoneNumber());
            mUser.setPrice(pacifyerDetial.getPrice());
            mUser.setCountry(pacifyerDetial.getCountry());

            mUser.setBonusProfileId(pacifyerDetial.getBonusProfileId());
            mUser.setCompanyId(pacifyerDetial.getCompanyId());
            mUser.setReasonForTheCall(pacifyerDetial.getReasonForTheCall());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  mUser;
    }

    /*public void setLogoutAlertStatus() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_yes_no, null, false);
        MainTextView desc_dtxv = (MainTextView) view.findViewById(R.id.desc_dtxv);
        MainTextView title_dtxv = (MainTextView) view.findViewById(R.id.title_dtxv);
        desc_dtxv.setText("Do you want to log out ?");
        title_dtxv.setVisibility(View.GONE);
        MainTextView dismis_dtxv = (MainTextView) view.findViewById(R.id.dismis_dtxv);
        dismis_dtxv.setText(" No ");
        MainTextView ok_dtxv = (MainTextView) view.findViewById(R.id.ok_dtxv);
        ok_dtxv.setText(" Yes ");
        hud_status = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(getActivity().getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager mLoginManager = LoginManager.getInstance();

                if(isValid(mLoginManager))
                    mLoginManager.logOut();

                PrefManager.getInstance(getActivity()).clearSharedAll();
                signOut();
                // VideoCallRecevingService.stopAudioService(getActivity());
               // MediaThread.getInstance(getActivity().getApplicationContext()).onStop();
                hideStatus();
                deleteInstallationWeb();
                try{
                    if(isValid(mGoogleApiClient)) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {


                                    }
                                });
                    }
                }catch (Exception e)
                {

                }
                Intent intent = new Intent(getActivity(), SignActivity.class);
                getActivity(). startActivity(intent);
                getActivity(). finish();

            }
        });
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }*/

    /*public void showSettingsDialog() {
        try {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.dialog_setings ,null);

            RelativeLayout bg_rlay = (RelativeLayout) view.findViewById(R.id.bg_rlay);
            LinearLayout  topLlay = (LinearLayout) view.findViewById(R.id.top_llay);
            MainTextView settingsCtxv = (MainTextView) view.findViewById(R.id.settings_ctxv);
            MainTextView editgradesCtxv = (MainTextView) view.findViewById(R.id.editgrades_ctxv);
            MainTextView chooseschoolCtxv = (MainTextView) view.findViewById(R.id.chooseschool_ctxv);
           // MainTextView help_ctxv = (MainTextView) view.findViewById(R.id.help_ctxv);
            MainTextView contactusDtxv = (MainTextView) view.findViewById(R.id.contactus_dtxv);
            MainTextView logoutCtxv = (MainTextView) view.findViewById(R.id.logout_ctxv);

            bg_rlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((MainActivity)context).showActionBar();
                    hideStatus();
                }
            });
            editgradesCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   *//* Intent intent=new Intent(getActivity(), EditSchoolGradeActivity.class);
                    startActivity(intent);
                    hideStatus();*//*
                }
            });


            contactusDtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((MainActivity)context).showActionBar();
                   *//* Intent intent=new Intent(getActivity(), ContactUsActivity.class);
                    startActivity(intent);
                    hideStatus();*//*


                }
            });

           *//* help_ctxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((MainActivity)context).showActionBar();
                    hideStatus();
                }
            });*//*
            logoutCtxv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();
                    setLogoutAlertStatus();
                }
            });


            hud_status = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(getActivity().getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
        } catch (Exception e){
            //Crashlytics.logException(e);
            hideStatus();
            LogUtils.LOGD("exception", " Exception " + e.getMessage());
        }
    }*/
    public ArrayList<MUser> getCallParticipantlist() {
        ArrayList<MUser> userlist=new ArrayList<MUser>();
        try{
            MUser mUser=getCallUser();
            MUser mPacifyer=getCallPacifyr();
            userlist=new ArrayList<MUser>();

            if(isValid(mUser))
                userlist.add(mUser);

            if(isValid(mPacifyer))
                userlist.add(mPacifyer);
        }catch (Exception e)
        {
            userlist=new ArrayList<MUser>();
        }
        return userlist;
    }
    public MUser getCallUser() {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getUser();
        }catch (Exception e)
        {

            mUser=null;
        }
        return mUser;
    }


    public MEmotion getEmotion(String emotionid) {
        MEmotion mEmotion=null;
        try {
            LogUtils.LOGD("session","SSA getEmotion  emotionid"+emotionid);
            if(isValid( NetworkService.modelAllEmotions)) {
                ArrayList<MEmotion> arrayList = NetworkService.modelAllEmotions.getResultsList();
                for (MEmotion item : arrayList) {
                    if(item.getId().equalsIgnoreCase(emotionid)) {
                        mEmotion = item;
                        break;
                    }
                }
            }else
            {
                NetworkService.webListEmotions(getActivity());
            }
        }catch (Exception e)
        {
            LogUtils.LOGD("session","SSA e getmessage "+e.getMessage());
        }
        return mEmotion;
    }

    public MUser getCallPacifyr() {
        MUser mUser=null;
        try{

            mUser= CallActivity.mSessionDetailRoot.getPacifyr();
        }catch (Exception e)
        {
            mUser=null;
        }
        return mUser;
    }

    public boolean isPacifyer()
    {
        boolean isPacifyer=false;
        try{

            try {

                String label=PrefManager.getInstance(getActivity()).getUserType();

                if(label.equalsIgnoreCase("pacifyr"))
                    isPacifyer=true;
                else if(label.equals("customer")) {
                    isPacifyer=false;
                }
            } catch (Exception t) {
                isPacifyer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isPacifyer=false;
        }
        return isPacifyer;
    }
    public boolean isCustomer()
    {
        boolean isCustomer=false;
        try{

            try {
                String label=PrefManager.getInstance(getActivity()).getUserType();
                if(label.equals("customer")) {
                    isCustomer=true;
                }
                else if(label.equalsIgnoreCase("pacifyr"))
                    isCustomer=false;

            } catch (Exception t) {
                isCustomer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isCustomer=false;
        }
        return isCustomer;
    }

    public void deleteInstallationWeb() {
        try {
            LogUtils.LOGD("install", "deleteInstallation " );

            setLoading();
            showProgress();
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    hideProgress();
                    LogUtils.LOGD("install", "deleteInstallation json " +json);
                    LogUtils.LOGD("install", "deleteInstallation status" +status.getMessage());

                    if(json == null) {
                        ToastHandler.newInstance(getActivity()).mustShowToast("Please Try again");

                    } else {
                        try {
                            setStatus("Logout",R.color.color_scheme);
                            showStatus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    clearSharedAll();
                                    clearServices();
                                    //clearTwilio();
                                    //  Singleton.destoryInstance();
                                    hideStatus();
                                    //showSignActivty();
                                }
                            }, 500);

                             /*   ErrorMessage sucesss = GsonUtils.getInstance().gsonToErrorMessage(json);
                                if(isValid(sucesss)) {

                                    setAlertStatus("", "An email with instructions for resetting password has been sent ", "Ok", R.drawable.white_rounded_square);

                                } else
                                    ToastHandler.newInstance(getActivity()).mustShowToast("Please Try again");*/

                        } catch (Exception e){
                            //Crashlytics.logException(e);
                            hideStatus();
                            ToastHandler.newInstance(getActivity()).mustShowToast("Please Try again");
                            //e.printStackTrace();
                        }
                    }


                }
            };
            LogUtils.LOGD("install", "deleteInstallation getPrefUDID " +getPrefUDID());
            String udid=isValid(getPrefUDID())?getPrefUDID():getUDID();
            String accesstoken= PrefManager.getInstance(getApplicationContext()).getAccessToken();
            AQuery aq = new AQuery(getActivity());
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("AccessToken",""+accesstoken);
            String url = GLOBAL_IMAGE_URL + "installation/" + udid+ "";
            LogUtils.LOGD("install", "deleteInstallation " +url);
            aq.delete(url, JSONObject.class, cb);
            //aq.d
        } catch (Exception e){
            //Crashlytics.logException(e);
            hideProgress();
            LogUtils.LOGD("install", "deleteInstallation exception : " + e.getMessage());
        }
    }

    public String getUDID() {
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public void clearSharedAll() {
        if(!isValid(editor)) {
            if(!isValid(sharedpreferences))
                sharedpreferences = getActivity().getSharedPreferences(PrefManager.LOGIN_PREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
        }
        editor.clear().commit();
    }
//////////////////////////////////////////////

    public void clearServices() {
        //   clearLayerDatas();
      /*  NetworkService.mAllUsersRoot = null;
        NetworkService.mAllUsersRootPaged = null;
        NetworkService.mhangoutLive = null;*/
       /* Constants.m = null;
        Constants.question_image_data = null;
        Constants.question_title = null;
        Constants.annotedlist = null;
        Constants.sharedlist = null;
        profileimage = null;*/


    }

    /*private void signOut() {
        try {
            if(isValid(mGoogleApiClient))
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                if(mGoogleApiClient.isConnected()){
                                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                                    mGoogleApiClient.disconnect();
                                }
                            }
                        });
        }catch (Exception e)
        {

        }
    }*/
    /*public void showSignActivty() {
        if(isValid(getActivity())) if(getActivity() instanceof ListPacifyrScreenActivity)
            ((ListPacifyrScreenActivity) getActivity()).showSignActivity();
    }
*/











    public PrefManager getPrefManager()
    {

        return     PrefManager.getInstance(getApplicationContext());

    }
    public PrefManager getPrefManager(Context cOntext)
    {

        return     PrefManager.getInstance(cOntext);

    }
    public String getAccessToken() {
        return getPrefManager().getAccessToken();

    }

    public void customerconnectedSessionWeb( String sessionid) {
        try {
            ToastHandler.newInstance(getApplicationContext()).showToast("connectedSessionWeb");
            //    PacifyrActivity.call_session_detail_root=null;
            LogUtils.LOGD("call", "connectedSessionWeb getAccessToken() "+getAccessToken());
            if (!isValid(sessionid,"error Session id "+sessionid))
                return;
            if (!isValid(getAccessToken(),"Session Expired, Please Re login"))
                return;

            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    CallActivity.isConnected=true;
                    if (json == null) {
                        ToastHandler.newInstance(getApplicationContext()).showToast("Please Try again");
                        LogUtils.LOGD("call", "connectedSessionWeb json null ");
                    } else {
                        if (json != null) try {

                            LogUtils.LOGD("call", "connectedSessionWeb json  " + json);
                            //   PacifyrActivity.call_session_detail_root= GsonUtils.getInstance().gsonToMConnectSessionRoot(json);
                            LogUtils.LOGD("call", "connectedSessionWeb json  " + json);
                        } catch (Exception e){
                            ToastHandler.newInstance(getApplicationContext()).showToast("Please Try again");
                            LogUtils.LOGD("call", "connectedSessionWeb exception : " + e.getMessage());
                        }
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());

            cb.header("AccessToken", getAccessToken());
            cb.header("Content-Type", "application/json");

            JSONObject input = new JSONObject();
            input.putOpt("id", sessionid);
            aq.post(GLOBAL_URL + "session/connected", input, JSONObject.class, cb);
        } catch (Exception e){
            //  PacifyrActivity. call_session_detail_root=null;
            LogUtils.LOGD("call", "connectedSessionWeb end exception : " + e.getMessage());
        }
    }

    /**
     * Shared Preference
     */

    public boolean isCardExist() {
        return getSharedBoolean(PrefManager.CARD_EXIST, false);
    }
    public void saveCardExist(boolean isExist ) {
        putSharedBoolean(PrefManager.CARD_EXIST, isExist);
    }



    public boolean getSharedBoolean(String KEY, boolean defValue) {
        return PrefManager.getInstance(getApplicationContext()).getSharedBoolean(KEY, defValue);
    }


    public void putSharedBoolean(String KEY, boolean defValue) {
        PrefManager.getInstance(getApplicationContext()).putSharedBoolean(KEY, defValue);
    }
    public String getSharedString(String KEY, String defValue) {
        return PrefManager.getInstance(getApplicationContext()).getSharedString(KEY, defValue);
    }

    public void putSharedString(String KEY, String defValue) {
        PrefManager.getInstance(getApplicationContext()).putSharedString(KEY, defValue);
    }

    public String getPrefUDID() {
        return getSharedString(PrefManager.UDID, "");
    }

    public void saveUDID(String valude) {
        putSharedString(PrefManager.UDID, valude);
    }

    public void saveProfileImageURL(String url) {
        putSharedString(PrefManager.PROFILE_IMAGE_URL, url);

    }

    public String getProfileImageURL() {
        return getSharedString(PrefManager.PROFILE_IMAGE_URL, "");

    }
    public String getUserName() {
        return getUserFirstName()+" "+ getUserLastName();

    }
    public String getUserFirstName() {
        return getSharedString(PrefManager.UserFirstName, "");

    }

    public String getUserLastName() {
        return getSharedString(PrefManager.UserLastName, "");

    }
    public String getUserID() {
        return getSharedString(PrefManager.USERID, "");

    }


    public KProgressHUD hud_status2 = null;

    public void showStatus2() {
        if(hud_status2 != null) if(!hud_status2.isShowing()) hud_status2.show();
    }
    public void hideStatus2() {
        if(hud_status2 != null) if(hud_status2.isShowing()) hud_status2.dismiss();
    }



    public void setAlertStatusInBlue2(String title,String desc,String ok,String dismiss,View.OnClickListener oklistner) {
        if (getActivity() == null) {
            return;
        }
        View view = LayoutInflater.from(getActivity()).inflate(com.pacifyrapp.utilitylibrary.R.layout.alert_yes_no, null, false);
        CustomFontTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        CustomFontTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        CustomFontTextView dismis_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);

        defSetText(dismis_dtxv,dismiss,"NO");
        defSetText(ok_dtxv,ok,"Yes");

        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus2();


            }
        });
    }


    public void userDetailWeb(String id, AjaxCallback cb) {
        try {
            //showProgress();

            PrefManager prefM = PrefManager.getInstance(getApplicationContext());
            String token = prefM.getAccessToken();

            String url = GLOBAL_URL + "user/profile/" + id;


            AQuery aq = new AQuery(getApplicationContext());
            //cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            LogUtils.LOGD("base", "bAF userDetailWeb  exception : " + ex.getMessage());
        }
    }



    public void setAlertAddCardInBlue2() {
        try {
            if (getActivity() == null) {
                return;
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_add_credit_card, null, false);
            final CustomEditText cardNumberEdt = view.findViewById(R.id.card_number_edt);
            final CustomEditText cardMonthEdt = view.findViewById(R.id.card_month_edt);
            final CustomEditText cardYearEdt = view.findViewById(R.id.card_year_edt);
            final CustomEditText cardCvvEdt = view.findViewById(R.id.card_cvv_edt);
            final DeviceFitImageView close_dimv = view.findViewById(R.id.close_dimv);
            RelativeLayout saveRlay = view.findViewById(R.id.save_rlay);
            MainTextView saveCtxv = view.findViewById(R.id.save_ctxv);
            hideStatus();
            hideStatus2();
            hud_status2 = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus2();
            close_dimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideProgress();
                    hideStatus2();
                }
            });
            saveRlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isValidCard(cardNumberEdt,cardMonthEdt,cardYearEdt,cardCvvEdt)) {
                        Card card=  getCardValid(cardNumberEdt,cardMonthEdt,cardYearEdt,cardCvvEdt);
                        if(!isValid(card))
                            com.pacifyrapp.utilitylibrary.ToastHandler.newInstance(getActivity()).mustShowToast("Invalid Card");
                        else {
                            performCardSave(card);
                        }
                    }
                    else{
                        ToastHandler.newInstance(getActivity()).mustShowToast("Invalid Card");
                    }
                }
            });

        }catch (Exception e)
        {
            errorStatus();
            /*hideProgress();
            hideStatus2();*/
            LogUtils.LOGD("session", "setAlertAddCardInBlue2 Exception : " + e.getMessage());
        }
    }

    boolean isValidCard(CustomEditText cardNumberEdt, CustomEditText cardMonthEdt,  CustomEditText cardYearEdt, CustomEditText cardCvvEdt ) {
        boolean ret=true;
        try {

            if(!isValid(cardNumberEdt.getText()))
            {
                //Toast.makeText(getApplicationContext(), cardNo, Toast.LENGTH_LONG).show();
                cardNumberEdt.setError("Please enter valid card number");
                ret = false;
            }
            if(!isValid(cardMonthEdt.getText()))
            {
                //Toast.makeText(getApplicationContext(), month, Toast.LENGTH_LONG).show();
                cardMonthEdt.setError("Please enter valid month");
                ret = false;

            }
            if(!isValid(cardYearEdt.getText()))
            {
                //Toast.makeText(getApplicationContext(), year, Toast.LENGTH_LONG).show();
                cardYearEdt.setError("Please enter valid year");
                ret = false;

            }
            if(!isValid(cardCvvEdt.getText()))
            {
                //Toast.makeText(getApplicationContext(), cvv, Toast.LENGTH_LONG).show();
                cardCvvEdt.setError("Please enter valid cvv");
                ret = false;

            }

            Card card = new Card(cardNumberEdt.getText()+"",Integer.parseInt(cardMonthEdt.getText()+""), Integer.parseInt(cardYearEdt.getText()+""), cardCvvEdt.getText()+"");
            // Remember to validate the card object before you use it to save time.
            if (!card.validateCard()) {
                ret = false;
            }

        }
        catch (Exception e)
        {
            ret = false;
        }
        return ret;
    }

    public Card getCardValid(CustomEditText cardNumberEdt, CustomEditText cardMonthEdt,  CustomEditText cardYearEdt, CustomEditText cardCvvEdt )
    {
        Card card=null;
        try {
            card = new Card(cardNumberEdt.getText()+"",Integer.parseInt(cardMonthEdt.getText()+""), Integer.parseInt(cardYearEdt.getText()+""), cardCvvEdt.getText()+"");
            if (!card.validateCard()) {
                card = null;
                //ToastHandler.newInstance(getApplicationContext()).mustShowToast("Invalid Card");
            }
        }catch (Exception e)
        {
            card=null;
        }
        return card;

    }
    private void performCardSave(Card card ) {
        try {
            setLoadingWait();
            showProgress();
            Stripe stripe = new Stripe(getApplicationContext(), STRIPE_PUBLISH_KEY);
            stripe.createToken(card, new TokenCallback() {
                public void onSuccess(Token token) {
                    String type = token.getType();
                    Token genaratedToken = token;
                    // hideProgress();
                    saveCardDetails(genaratedToken);
                }

                public void onError(Exception error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideProgress();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            hideProgress();
        }
    }

    private void saveCardDetails(Token genaratedToken) {
        try {
            PrefManager prefM= PrefManager.getInstance(getApplicationContext());
            String userToken=prefM.getAccessToken();
            String userid=prefM.getUserID();
        /*setLoading();
        showProgress();*/
            String url = NetworkService.GLOBAL_URL+"payment/saveCard";
            AjaxCallback cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    try {
                        hideProgress();
                        String message=json.getString("message");
                        String my=json.toString();
                        if(message.equals("Card added")){

                            saveCardExist(true);
                            hideStatus2();
                            setStatus("Successfully added your card", R.color.md_green_400);
                            showStatus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideStatus();
                                    // finish();
                                }
                            }, 2000);

                        }else {
                            errorStatus();
                    /*    setStatus("Error occured. Please try again.", R.color.md_red_500);
                        showStatus();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatus();
                            }
                        }, 2000);*/
                        }
                    } catch (Exception e) {
                        errorStatus();
                    }
                }
            };
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken",userToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("userId", userid);
            jsonObject.putOpt("tokenId", genaratedToken.getId());
            aq.post(url, jsonObject, JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            errorStatus();
        }

    }

    public void errorStatus() {
        setStatus("Error,Please try later", R.color.red);
        showStatus();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                hideStatus();
                hideStatus2();
            }
        }, 2000);
    }
    public void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }



    public void setIncommingPacifyrCallAlertCardStatusInBlue(String title, String desc, String ok, View.OnClickListener listener) {
        if (getActivity() == null) {
            return;
        }

        //View.OnClickListener oklistner
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_ok_card_incomming_pacifyr_call, null, false);
        MainTextView title_dtxv = view.findViewById(R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(R.id.desc_dtxv);
        MainTextView ok_dtxv = view.findViewById(R.id.ok_dtxv);

        if (isValid(title))
            defSetText(title_dtxv, title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv, desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(listener);

    }

    public void getEventDetails(String eventID, AjaxCallback cb) {


        String token = getPrefManager().getAccessToken();


        try {
            AQuery aq = new AQuery(getActivity());

            cb.header("Content-Type", "application/json");

            cb.header("AccessToken", token);

            aq.ajax(NetworkService.GLOBAL_URL + "events/" + eventID, JSONObject.class, cb);

        } catch (Exception ex) {
            hideProgress();

            ex.printStackTrace();
        }
        //setAdpater(list);
    }





    public void setAlertStatusInWhite(String title, String desc, String ok, String dismiss, View.OnClickListener oklistner) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_white_yes_no, null, false);
        MainTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        MainTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        MainTextView dismis_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.dismis_dtxv);
        MainTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
        if (isValid(title))
            defSetText(title_dtxv, title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv, desc);

        defSetText(dismis_dtxv, dismiss, "No");
        defSetText(ok_dtxv, ok, "Yes");

        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus2();

            }
        });
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel) {
        new iOSDialogBuilder(getActivity())
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(true)
                .setPositiveListener(okLabel, new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    public void showIOSDialogOK(String title, String subTitle, String okLabel, iOSDialogClickListener listener)
    {
        new iOSDialogBuilder(getActivity())
                .setTitle(title)
                .setSubtitle(subTitle)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(okLabel,listener)
                .build().show();
    }

    /** Yes and No onclicklistner **/
    public void setAlertStatusInBlue2(String title,String desc,String ok,String dismiss,View.OnClickListener nolistner,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(getActivity()).inflate(com.pacifyrapp.utilitylibrary.R.layout.alert_yes_no, null, false);
        CustomFontTextView title_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.title_dtxv);
        CustomFontTextView desc_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.desc_dtxv);
        CustomFontTextView dismis_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = view.findViewById(com.pacifyrapp.utilitylibrary.R.id.ok_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);

        defSetText(dismis_dtxv,dismiss,"NO");
        defSetText(ok_dtxv,ok,"Yes");

        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(com.pacifyrapp.utilitylibrary.R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();
        ok_dtxv.setOnClickListener(oklistner);
        dismis_dtxv.setOnClickListener(nolistner);

    }
}