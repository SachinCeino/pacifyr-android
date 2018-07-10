package com.pacifyr.omhealthcare.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ceino.utilitylibrary.base.BaseAppCompactActivity;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.home.DashboardActivity;
import com.pacifyr.omhealthcare.home.PacifyrDashboardActivity;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.quickblox.services.CallService;
import com.pacifyr.omhealthcare.sign.PacifyrTermsPolicyActivity;
import com.pacifyr.omhealthcare.sign.SignActivity;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

public class SplachScreen extends BaseAppCompactActivity {

    private ProgressBar progressquest2;
    public static String trigger = "Start";
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences sharedpreferences;
    String label;
    JSONObject userObject;
    private SharedPrefsHelper sharedPrefsHelper;
    Intent mServiceIntent;
    private CallService mSensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  overridePendingTransition(0, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        NetworkService.webListEmotions(SplachScreen.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        mSensorService = new CallService(SplachScreen.this);
        mServiceIntent = new Intent(SplachScreen.this, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        try {
            //generateHashkey();
            progressquest2 = (ProgressBar) findViewById(R.id.progressquest2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isValid(PrefManager.getInstance(SplachScreen.this)))
                    {
                        if(isValid(PrefManager.getInstance(SplachScreen.this).getJsonKey()))
                        {
                            String jsonString =PrefManager.getInstance(SplachScreen.this).getJsonKey();
                            try {

                                JSONObject obj = new JSONObject(jsonString);
                                Log.d("My App", obj.toString());
                                userObject=obj.getJSONObject("user");
                                JSONObject role=userObject.getJSONObject("roleModel");
                                label=role.getString("mainLabel");
                                //label=label+"";

                            } catch (Exception t) {
                                t.printStackTrace();
                            }
                            sharedPrefsHelper = SharedPrefsHelper.getInstance();
                            if (sharedPrefsHelper.hasQbUser()) {
                                startLoginService(sharedPrefsHelper.getQbUser());
                                // startOpponentsActivity();
                                //return;
                            }

                            if(label.equals("pacifyr"))
                            {

                                NetworkService.startActionListCustomer(SplachScreen.this,0,10);
                                try {
                                    Boolean flag =userObject.getBoolean("termsAccepted");
                                    if (flag) {

                                        Intent intent = new Intent(SplachScreen.this, PacifyrDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Intent i = new Intent(SplachScreen.this, CallService.class);
                                        startService(i);
                                    }else{
                                        Intent i = new Intent(SplachScreen.this, PacifyrTermsPolicyActivity.class);
                                        startActivity(i);
                                        finish();
                                        Intent intent = new Intent(SplachScreen.this, CallService.class);
                                        startService(intent);
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(SplachScreen.this,"Network error, Please try again",Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(label.equals("customer")) {
                                DashboardActivity.start(SplachScreen.this);

                                 /*Intent intent = new Intent(SplachScreen.this, ConnectToPacifyrActivity.class);
                                intent.putExtra("display","Pacifyr");
                                startActivity(intent);
                                finish();*/


                                /*Intent intent = new Intent(SplachScreen.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                        else {
                            Intent intent = new Intent(SplachScreen.this, SignActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                    else
                    {
                        Intent intent = new Intent(SplachScreen.this, SignActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);


        }catch (Exception e)
        {
            Intent intent = new Intent(SplachScreen.this, SignActivity.class);
            startActivity(intent);
            finish();
        }
    }
    protected void startLoginService(QBUser qbUser) {
        CallService.start(this, qbUser);
    }
    /*public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.pacifyr.pacifyrapp.activity", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("samsad", "package " + info.packageName);
                Log.d("samsad", "hash  " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));//
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("samsad", e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("samsad", e.getMessage(), e);
        }
    }*/

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(SplachScreen.this.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }
}
