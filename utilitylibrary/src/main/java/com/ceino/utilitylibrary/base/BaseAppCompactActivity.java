package com.ceino.utilitylibrary.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ceino.utilitylibrary.CustomEditText;
import com.ceino.utilitylibrary.CustomFontTextView;
import com.ceino.utilitylibrary.DeviceFitImageView;
import com.ceino.utilitylibrary.R;
import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ceino on 28/12/16.
 */

public class BaseAppCompactActivity extends CoreBaseActivity {




    //extends AppCompatActivity
    /**
     * is Valid
     */
    public KProgressHUD hud_progress = null;
    public KProgressHUD hud_status = null;
    public KProgressHUD hud_status2 = null;
    public static String runningActivty="";

    public static String getRunningActivty() {
        return runningActivty;
    }

    public static void setRunningActivty(String runningActivty) {
        BaseAppCompactActivity.runningActivty = runningActivty;
    }

    public BaseAppCompactActivity() {
    }

    public PrefManager getPrefManager()
    {

     return     PrefManager.getInstance(getBaseContext());

    }
    public PrefManager getPrefManager(Context cOntext)
    {

        return     PrefManager.getInstance(cOntext);

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
    /*public void resetSharedValues() {
        try{
            PrefManager.getInstance(getApplicationContext())clearLoginPrefAll();
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("log", "container resetSharedValues exce " + e.getMessage());
        }
    }*/
    public void setLoading() {
        hideProgress();
        if(isValid(hud_progress))
            hud_progress=null;
        hud_progress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoadingWait() {
        hideProgress();
        if(isValid(hud_progress))
        hud_progress=null;
        hud_progress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void setLoading(String message) {
        hideProgress();
        if(isValid(hud_progress))
        hud_progress=null;
        hud_progress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

    }

    public void hideProgress() {
        try {
            if(isValid(hud_progress)) if(hud_progress.isShowing()) hud_progress.dismiss();
        }catch (Exception e)
        {

        }
    }

    public void showProgress() {
        try {
            hideProgress();
            if(isValid(hud_progress)) if(!hud_progress.isShowing()) hud_progress.show();
        }catch (Exception e)
        {

        }
    }
    public void setLoadingError(String message, int color) {
        hideProgress();
        if(isValid(hud_progress))
            hud_progress=null;
        hud_progress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setWindowColor(this.getResources().getColor(color)).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 2000);
    }
    public void setLoadingError(String message) {
        hideProgress();
        if(isValid(hud_progress))
            hud_progress=null;
        hud_progress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(""+message).setAnimationSpeed(2).setWindowColor(this.getResources().getColor(R.color.md_red_500)).setCancellable(true).setMaxProgress(100).setDimAmount(0.5f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 2000);
    }
    public void setStatus(String status, int color) {
        View view = LayoutInflater.from(this).inflate(R.layout.status_dialog, null, false);
        CustomFontTextView status_dtxv = (CustomFontTextView) view.findViewById(R.id.status_dtxv);
        status_dtxv.setText("" + status);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(color)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

    }

    public void setStatus(String status, int textcolor,int color) {
        View view = LayoutInflater.from(this).inflate(R.layout.status_dialog, null, false);
        CustomFontTextView status_dtxv = (CustomFontTextView) view.findViewById(R.id.status_dtxv);
        status_dtxv.setText("" + status);
        status_dtxv.setTextColor(textcolor);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(color)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

    }

    public void setStatus(String status) {
        View view = LayoutInflater.from(this).inflate(R.layout.status_dialog, null, false);
        CustomFontTextView status_dtxv = (CustomFontTextView) view.findViewById(R.id.status_dtxv);
        status_dtxv.setText("" + status);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.md_green_400)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);

    }

    public void setAlertStatusInBlue(String title,String desc,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_yes_no, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
       CustomFontTextView dismis_dtxv = (CustomFontTextView) view.findViewById(R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);
        defSetText(title_dtxv,title);
        defSetText(desc_dtxv,desc);
        dismis_dtxv.setText(" No ");
        ok_dtxv.setText(" Yes ");
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        ok_dtxv.setOnClickListener(oklistner);
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }

    public void setAlertOKStatusInBlue(String title,String desc,String ok) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                hideStatus();


            }
        });
    }

    public void setAlertOKStatusInBlue(String title, String desc, String ok, View.OnClickListener clickListener) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if (isValid(title))
            defSetText(title_dtxv, title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv, desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(clickListener);
    }

    public void setAlertOKStatusInRed(String title, String desc, String ok, View.OnClickListener clickListener) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok_red, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if (isValid(title))
            defSetText(title_dtxv, title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv, desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(clickListener);
    }
    public void setAlertOKStatusInBlue(String title,String desc,String ok,boolean autohide) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);

        if(autohide)
        hideStatus();

        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }
    public void setAlertOKStatusInBlueStatus2(String title,String desc,String ok) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);



        hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus2();


            }
        });
    }
    public void setAlertOKStatusInBlueStatus2(String title,String desc,String ok,boolean autohide) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);

        if(autohide)
            hideStatus2();

        hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();

        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus2();


            }
        });
    }

    public void setAlertOKStatusInBlueStatus2(String title,String desc,String ok,boolean autohide,View.OnClickListener clickListener) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);

        if(autohide)
            hideStatus2();

        hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus2();

        ok_dtxv.setOnClickListener(clickListener);
    }


    public void setAlertStatusInBlue2(String title,String desc,String ok,String dismiss,View.OnClickListener oklistner) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_yes_no, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView dismis_dtxv = (CustomFontTextView) view.findViewById(R.id.dismis_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);
        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);

        defSetText(dismis_dtxv,dismiss,"NO");
        defSetText(ok_dtxv,ok,"Yes");

        hideStatus();
        hideStatus2();
        hud_status2 = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
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
   /* public void setAlertOKStatusInBlue(String title,String desc,String ok) {
        //View.OnClickListener oklistner
        View view = LayoutInflater.from(this).inflate(R.layout.alert_ok, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        CustomFontTextView desc_dtxv = (CustomFontTextView) view.findViewById(R.id.desc_dtxv);
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);

        if(isValid(title))
            defSetText(title_dtxv,title);
        else
            title_dtxv.setVisibility(View.GONE);
        defSetText(desc_dtxv,desc);
        ok_dtxv.setText(ok);
        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }*/
    public  String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    public long getElapsedMinutes(Date date2) {

        Date nowdate = new Date();
        long mili = 0;
        long elapsedminutes=0;
        try {
         //   LogUtils.LOGD("jithish", "  TACA date2 time " + date2.getTime());
          //  LogUtils.LOGD("jithish", "  TACA nowdate time " + nowdate.getTime());
            long different = date2.getTime() - nowdate.getTime();
          //  LogUtils.LOGD("jithish", "  TACA different time " + different);
//return differents;
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            elapsedminutes = different / minutesInMilli;
        }
        catch (Exception e)
        {
            //LogUtils.LOGD("exception","  TACA time "+e.getMessage());
        }
        return elapsedminutes;
    }
/* public void setAlertStatus() {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_yes_no, null, false);
        CustomFontTextView title_dtxv = (CustomFontTextView) view.findViewById(R.id.title_dtxv);
        title_dtxv.setText("Do you want to log out ?");
        CustomFontTextView dismis_dtxv = (CustomFontTextView) view.findViewById(R.id.dismis_dtxv);
        dismis_dtxv.setText(" No ");
        CustomFontTextView ok_dtxv = (CustomFontTextView) view.findViewById(R.id.ok_dtxv);
        ok_dtxv.setText(" Yes ");
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(getActivity().getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        ok_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {hideStatus(); }
        });
        dismis_dtxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();


            }
        });
    }*/
    public void showStatus() {
        if(hud_status != null) if(!hud_status.isShowing()) hud_status.show();
    }
    public void showStatus2() {
        if(hud_status2 != null) if(!hud_status2.isShowing()) hud_status2.show();
    }
    public void hideStatus2() {
        if(hud_status2 != null) if(hud_status2.isShowing()) hud_status2.dismiss();
    }
    public void hideStatus() {
        if(hud_status != null) if(hud_status.isShowing()) hud_status.dismiss();
    }

    public Boolean isValid(String text) {
        if (text != null)
            if (!text.trim().equalsIgnoreCase(""))
                return true;
        return false;

    }

    public Boolean isValid(String text,int size) {
        if (isValid(text))
            if (text.length()>=size)
                return true;
        return false;

    }

    public Boolean isValid(List list) {
        if (list != null)
            if (list.size() > 0)
                return true;
        return false;

    }

    public Boolean isValid(List list,int pos) {
        if (isValid(list))
            if (list.size() >= pos)
                return true;
        return false;

    }

    public Boolean isListSize(List list,int size) {
        if (isValid(list))
            if (list.size() == size)
                return true;
        return false;

    }

    public Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            if (list.size() >= pos)
                return true;
        return false;
    }

    public Boolean isValid(String[] list,int size) {

        if (isValid(list))
            if (list.length >= size)
                return true;
        return false;

    }

    public Boolean isValidNotNull(List list) {
        if (list != null)
            // if(list.size()>0)
            return true;
        return false;

    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }
    public Boolean isErrorStatus(String object) {
        if(isValid(object)) if(object.trim().equalsIgnoreCase("error")) return true;
        return false;

    }

    public Boolean isValidDecimalLong(long object) {
        if (object >0)
            return true;
        return false;

    }

    public Boolean isValidDecimal(int object) {
        if (object >0)
            return true;
        return false;

    }

    public Boolean isValidLessSize(int object,int size) {
        if ((object+1) <size)
            return true;
        return false;

    }

    public Boolean isValidSize(List list) {
        if(list != null) if(list.size() > 0) return true;
        return false;

    }

    public Boolean isValidMiddleSize(List list,int pos) {
        if (isValid(list))
            if ((list.size()-1) > pos && pos>0)
                return true;
        return false;

    }

    public Boolean isLastItem(List list,int pos) {
        if (isValid(list))
            if ((list.size()-1) == pos)
                return true;
        return false;

    }

   /* public Boolean isFirst(List list,int pos) {
        if (isValid(list))
            if (list.size() == pos+1)
                return true;
        return false;

    }*/
    public Boolean isValid(Object object, String error) {
        if (object != null) return true;
        ToastHandler.newInstance(this).mustShowToast("" + error);
        return false;

    }
    public Boolean isValid(boolean object, String error) {
        if (object ) return true;
        ToastHandler.newInstance(this).mustShowToast("" + error);
        return false;

    }
    public Boolean isValid(List object, String error) {

            if(isValid(object))
                return  true;
        ToastHandler.newInstance(this).mustShowToast("" + error);
        return false;

    }
    public Object checkedObject(Object object) {
        if (object != null)
            return object;
        return object;

    }

    public Boolean isValid(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return true;
        text.setError(error + "");
        return false;

    }

    public Boolean isValid(TextView text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return true;
        ToastHandler.newInstance(getBaseContext()).mustShowToast("" + error);
        return false;

    }

    public Boolean isValid(String text, String error) {
        if (text != null)
            if (!text.trim().equalsIgnoreCase(""))
                return true;
        ToastHandler.newInstance(getBaseContext()).mustShowToast("" + error);
        return false;

    }

    public Boolean isValid(Date date) {
        if (date != null)
            return true;
        return false;

    }

    public Boolean isValid(String pwd, String pwd2, String error) {

        if (pwd2.equals(pwd))
            return true;

        ToastHandler.newInstance(getBaseContext()).mustShowToast(error);
        return false;

    }

    public Boolean isMatchesError(String pwd, String pwd2, String error) {

        if (!pwd2.equals(pwd))
            return true;

        ToastHandler.newInstance(getApplicationContext()).mustShowToast(error);
        return false;

    }

    public Boolean isNOtMatches(String pwd, String pwd2) {

        if (!pwd2.equals(pwd))
            return true;


        return false;

    }

    public Boolean isMatches(String pwd, String pwd2) {

        if (pwd2.equals(pwd))
            return true;


        return false;

    }

    public Boolean isSame(String pwd, String pwd2) {

        if (pwd2.equals(pwd))
            return true;


        return false;

    }

    public Boolean isGreater(EditText text,long bid, String error) {
        long enterbid=Long.parseLong(text.getText()+"");
        if (enterbid >=bid)
                return true;
        ToastHandler.newInstance(getApplicationContext()).mustShowToast(error);
        return false;

    }

    public Boolean isValidEmail(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                if (isValidEmail(text.getText() + ""))
                    return true;
        text.setError(error + "");
        return false;

    }

    public boolean isValidMobile(CustomEditText phone_tv, String error) {
        if (phone_tv != null)
            if (isValid(phone_tv.getText() + ""))
                if (isValidMobile(phone_tv.getText().toString()))
                    return true;
        phone_tv.setError(error + "");
        return false;

        //String phone=phone_tv.getText().toString();
        //return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    public boolean isValidMobile(String txt)
    {
        return android.util.Patterns.PHONE.matcher(txt).matches();
    }

    public Boolean isValidPassword(EditText text, String error) {
        if (text != null)
            if (isValid(text.getText() + ""))
                if (isValidPassword(text.getText() + ""))
                    return true;
        text.setError(error + "");
        return false;

    }



    // validating email id
/*    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }*/
    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    // validating password with retype password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 3) {
            return true;
        }
        return false;
    }

    public String defString(EditText text, String def) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return text.getText() + "";
        return def;

    }

    public String defString(EditText text) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return text.getText() + "";
        return "";

    }

    public long defStringLong(EditText text) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return Long.parseLong(text.getText()+"");
        return 0;

    }

    public int defStringInt(EditText text) {
        if (text != null)
            if (isValid(text.getText() + ""))
                return Integer.parseInt(text.getText()+"");
        return 0;

    }

    public String defString(String text, String def) {
        if (text != null)
            return text;
        return def;

    }

    /*public String defString(Object text) {
        if (text != null)
            return text;
        return "";

    }*/
    public String defString(String text) {
        if (text != null)
            return text;
        return "";

    }

    public void showFragment(Fragment fragment,int id) {

        getSupportFragmentManager().beginTransaction().replace(id, fragment).addToBackStack("" + fragment).commit();
// fragmentShowActionBar(fragment);

    }

    public void showFragment(Fragment fragment, Bundle bundle,int id) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(id, fragment).addToBackStack("" + fragment).commit();
// fragmentShowActionBar(fragment);
    }

    public void showFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("" + fragment).commit();
// fragmentShowActionBar(fragment);

    }

    public void showFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("" + fragment).commit();
// fragmentShowActionBar(fragment);
    }

    public void showActionBar() {
        if(!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();

        }
        getSupportActionBar().setElevation(0);
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onPopUpFragment() {
        hideKeyboard();
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            //fragmentShowActionBar(getSupportFragmentManager().findFragmentById(R.id.container));
        } else {
            // showFragment(new HomeFragment());
        }
    }

    public void onBackClicked() {
        hideKeyboard();
        //showFragment(new HomeFragment());
    }

    public void setAqueryImage(AQuery aqueryImage, String urllast, int drawable)
    {
        try{
            aqueryImage.image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast,true,true,0,0,drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAqueryImage(AQuery aqueryImage, String urllast, int drawable, Boolean cached)
    {
        try{
            aqueryImage.image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast,cached,cached,0,0,drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setAqueryImage(AQuery aqueryImage, ImageView imageView, String urllast, int drawable)
    {
        try{
            aqueryImage.id(imageView).image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast,true,true,0,0,drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAqueryImage(ImageView img, String urllast, int drawable) {

        try {
            AQuery aQuery = new AQuery(img);
            if(!isValid(urllast))
                aQuery.image(drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)));
            else
                aQuery.image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast, true, true, 0, 0, drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    public void setAqueryImageUrl(ImageView img, String url, int drawable) {

        try {
            AQuery aQuery = new AQuery(img);
            if(!isValid(url))
                aQuery.image(drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)));
            else
                aQuery.image(url, true, true, 0, 0, drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    public void setAqueryImage(ImageView img, String urllast, int drawable, Boolean cached) {

        try {
            AQuery aqueryImage = new AQuery(img);
            if(!isValid(urllast))
                aqueryImage.image(drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)));
            else
                aqueryImage.image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast, cached, cached, 0, 0, drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }

    }

    public void setAqueryImage(ImageView img, ImageView imageView, String urllast, int drawable) {
        try {
            AQuery aqueryImage = new AQuery(img);
            if(!isValid(urllast))
                aqueryImage.image(drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)));
            else
                aqueryImage.id(imageView).image(UtilityNetworkService.GLOBAL_IMAGE_URL + urllast, true, true, 0, 0, drawableToBitmap(getApplicationContext().getResources().getDrawable(drawable)), 0);
        } catch (Exception e){
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public void defSetText(TextView textv, String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(TextView textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    public void defSetText(EditText textv, String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(EditText textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }
    public void defBlinkSetText(final TextView textv, String text) {
        if(isValid(textv)) {
            textv.setText(defString(text));
            textv.setVisibility(View.VISIBLE);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(isValid(textv))
                   textv.setVisibility(View.INVISIBLE);
            }
        }, 4000);
    }

    public long millisecondCalc(String s) {
        try {
            if (s.equalsIgnoreCase("5min")) {
                return TimeUnit.MINUTES.toMillis(5); // 23 minutes to milliseconds.
            } else if (s.equalsIgnoreCase("15min")) {
                return TimeUnit.MINUTES.toMillis(15); // 23 minutes to milliseconds.
            } else if (s.equalsIgnoreCase("30min")) {
                return TimeUnit.MINUTES.toMillis(30); // 23 minutes to milliseconds.
            } else if (s.equalsIgnoreCase("1hr")) {
                return TimeUnit.HOURS.toMillis(1);    // 4 hours to milliseconds.
            } else if (s.equalsIgnoreCase("2hr")) {
                return TimeUnit.HOURS.toMillis(2);    // 4 hours to milliseconds.
            } else if (s.equalsIgnoreCase("1day")) {
                return TimeUnit.DAYS.toMillis(1);     // 1 day to milliseconds.
            } else if (s.equalsIgnoreCase("2day")) {
                return TimeUnit.DAYS.toMillis(2);     // 1 day to milliseconds.
            } else if (s.equalsIgnoreCase("1week")) {
                return TimeUnit.DAYS.toMillis(7);              // 7 Days to milliseconds.
            } else if (s.equalsIgnoreCase("none")) {
                return 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        TimeUnit.SECONDS.toMillis(96); // 96 seconds to milliseconds.
        return 0;
    }



    public String millisecondString(long s) {
        try {
            if (TimeUnit.MINUTES.toMillis(5)==s) {
                return "5 min"; // 23 minutes to milliseconds.
            } else if (TimeUnit.MINUTES.toMillis(15)==s) {
                return "15 min"; // 23 minutes to milliseconds.
            }
            else if (TimeUnit.MINUTES.toMillis(15)==s) {
                return "30 min"; // 23 minutes to milliseconds.
            }
            else if (TimeUnit.HOURS.toMillis(1)==s) {
                return "1 hour"; // 23 minutes to milliseconds.
            }


            else if (TimeUnit.HOURS.toMillis(2)==s) {
                return "2 hour"; // 23 minutes to milliseconds.
            }

            else if (TimeUnit.DAYS.toMillis(1)==s) {
                return "1 day"; // 23 minutes to milliseconds.
            }

            else if (TimeUnit.DAYS.toMillis(2)==s) {
                return "2 day"; // 23 minutes to milliseconds.
            }else if (TimeUnit.DAYS.toMillis(7)==s) {
                return "1 week"; // 23 minutes to milliseconds.
            } else {
                return "none";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        TimeUnit.SECONDS.toMillis(96); // 96 seconds to milliseconds.
        return "";
    }


    public void setAlertStartCall(String title,String desc,String userimage,View.OnClickListener oklistner) {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.alert_startcall, null, false);

            DeviceFitImageView closeDimv = (DeviceFitImageView) view.findViewById(R.id.close_dimv);
            CircleImageView imageDialogIcon = (CircleImageView) view.findViewById(R.id.image_dialog_icon);
            CustomFontTextView textDialogName = (CustomFontTextView) view.findViewById(R.id.text_dialog_name);
            CustomFontTextView textDialogLastMessage = (CustomFontTextView) view.findViewById(R.id.text_dialog_message);
            RelativeLayout saveRlay = (RelativeLayout) view.findViewById(R.id.save_rlay);

            defSetText(textDialogName, title);
            defSetText(textDialogLastMessage, desc + " per hour");

            if (isValid(userimage))
                Picasso.with(getApplicationContext()).load(userimage).placeholder(R.drawable.placeholder_user).into(imageDialogIcon);

            hideStatus();
            hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
            showStatus();
            saveRlay.setOnClickListener(oklistner);
            closeDimv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideStatus();

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public  void setAlertStartCall(MUser pacifyer_obj, View.OnClickListener oklistner) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_startcall, null, false);

        DeviceFitImageView closeDimv = (DeviceFitImageView) view.findViewById(R.id.close_dimv);
        CircleImageView imageDialogIcon = (CircleImageView) view.findViewById(R.id.image_dialog_icon);
        CustomFontTextView textDialogName = (CustomFontTextView) view.findViewById(R.id.text_dialog_name);
        CustomFontTextView textDialogLastMessage = (CustomFontTextView) view.findViewById(R.id.text_dialog_message);
        RelativeLayout saveRlay = (RelativeLayout) view.findViewById(R.id.save_rlay);

        defSetText(textDialogName,pacifyer_obj.getFirstName()+""+pacifyer_obj.getLastName());
        defSetText(textDialogLastMessage, pacifyer_obj.getPriceWithSign()+" per hour");

        String userimage = UtilityNetworkService.GLOBAL_IMAGE_URL+pacifyer_obj.getImage();

        if(isValid(userimage))
            Picasso.with(getBaseContext()).load(userimage).placeholder(R.drawable.placeholder_user).into(imageDialogIcon);

        hideStatus();
        hud_status = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCustomView(view).setWindowColor(this.getResources().getColor(R.color.transparent)).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
        showStatus();
        saveRlay.setOnClickListener(oklistner);
        closeDimv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();

            }
        });
    }
}
