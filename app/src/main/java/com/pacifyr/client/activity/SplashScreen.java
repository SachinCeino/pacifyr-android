package com.pacifyr.client.activity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pacifyr.client.R;
import com.pacifyr.client.base.PacifyrActivity;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.PacifyrDashboardActivity;
import com.pacifyr.client.orginals.Auth;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.orginals.DynamicLink;
import com.pacifyr.client.orginals.RemoteConfig;
import com.pacifyr.client.sign.PacifyrTermsPolicyActivity;
import com.pacifyr.client.sign.SignActivity;
import com.pacifyr.client.utils.Constants;
import com.pacifyr.client.utils.SharedPrefsHelper;
import com.pacifyr.client.utils.Utility;
import com.pacifyrapp.utilitylibrary.base.BaseAppCompactActivity;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

public class SplashScreen extends BaseAppCompactActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();
    private ProgressBar progressquest2;
    public static String trigger = "Start";
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences sharedpreferences;
    String label;
    JSONObject userObject;
    private SharedPrefsHelper sharedPrefsHelper;
    //Intent mServiceIntent;
    private ImageView image_splash;
    private boolean isNeedToOpenAppointments ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Auth.foreground(this.getClass().getName());
        FirebaseApp.initializeApp(this);
        RemoteConfig.Update(this);
        UtilityNetworkService.init(this);
       // Log.w("AUTH_SECRET",APP_ID+AUTH_KEY+AUTH_SECRET);


        PacifyrActivity.flexibleUpdatePopupShown = false;
        //  overridePendingTransition(0, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
     //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        NetworkService.webListEmotions(SplashScreen.this);
               setContentView(R.layout.activity_splash_screen);
        image_splash = findViewById(R.id.image_splash);
        if (isValid(getIntent()) && isValid(getIntent().getExtras())&&isValid(getIntent().getExtras().getBoolean("isNeedToOpenAppointments",false))) {
            isNeedToOpenAppointments = getIntent().getExtras().getBoolean("isNeedToOpenAppointments",false);
        }

        FirebaseAnalytics.getInstance(this);
        Utility.getRealTimeLogging(SplashScreen.this);


        /**place holder issue fix **/
        ColorDrawable mColorDrawable = new ColorDrawable(getResources().getColor(R.color.black));
        Picasso.with(this).load(R.drawable.splash).placeholder(mColorDrawable).into(image_splash);

        try {
            createNotificationChannel(this);
        } catch (Exception e) {
            Log.d(TAG, "Create notofocation channel err");
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();

        if( bundle!= null){
            try {
                String screen = bundle.getString("screen");
                String screen_data = bundle.getString("screen_data");
                DynamicLink.setScreenFromNotification(screen,screen_data);
            }catch (Exception e){

            }
        }

        DynamicLink.fetch(this);
        NetworkService.startActionCutomerDashboard(this);
        gatePass();
    }


    public interface DeepLinkListener {
        void onDone();
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.app_name);
            String description = "general_notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.getChannelId(context), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
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
        //stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    public void gatePass(){
        progressquest2 = findViewById(R.id.progressquest2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isValid(PrefManager.getInstance(SplashScreen.this))) {
                    if (isValid(PrefManager.getInstance(SplashScreen.this).getJsonKey())) {
                        String jsonString = PrefManager.getInstance(SplashScreen.this).getJsonKey();
                        try {

                            JSONObject obj = new JSONObject(jsonString);
                            Log.d("My App", obj.toString());
                            userObject = obj.getJSONObject("user");
                            JSONObject role = userObject.getJSONObject("roleModel");
                            label = role.getString("mainLabel");
                            //label=label+"";

                        } catch (Exception t) {
                            t.printStackTrace();
                        }
                        sharedPrefsHelper = SharedPrefsHelper.getInstance();

                        if (label.equals("pacifyr")) {

                            NetworkService.startActionListCustomer(SplashScreen.this, 0, 10);
                            try {
                                Boolean flag = userObject.getBoolean("termsAccepted");
                                if (flag) {

                                    Intent intent = new Intent(SplashScreen.this, PacifyrDashboardActivity.class);
                                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Intent i = new Intent(SplashScreen.this, PacifyrTermsPolicyActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(SplashScreen.this, "Network error, Please try again", Toast.LENGTH_LONG).show();
                            }
                        } else if (label.equals("customer")) {
                           DashboardActivity.start(SplashScreen.this,isNeedToOpenAppointments);

//                            Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);
//                            intent.putExtra("isNeedToOpenAppointments",isNeedToOpenAppointments);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
//                            startActivity(intent);

                                 /*Intent intent = new Intent(SplashScreen.this, ConnectToPacifyrActivity.class);
                                intent.putExtra("display","Pacifyr");
                                startActivity(intent);
                                finish();*/


                                /*Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();*/
                        }
                    } else {
                        Intent intent = new Intent(SplashScreen.this, SignActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    Intent intent = new Intent(SplashScreen.this, SignActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

       /* FirebaseRemoteConfig remConfig = ((App) getApplication()).getRemoteConfig();
        remConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Remote config fetch successful");
                } else {
                    Log.d(TAG, "Remote config fetch failed");
                }
                String usUrl = remConfig.getString(RemoteConfig.KEY_US_URL);
                String indiaUrl = remConfig.getString(RemoteConfig.KEY_INDIA_URL);
                boolean forceUpgrade = remConfig.getBoolean(RemoteConfig.KEY_FORCE_UPGRADE);
                Log.d(TAG, "US url = " + usUrl);
                Log.d(TAG, "India url = " + indiaUrl);
                Log.d(TAG, "Force Upgrade = " + forceUpgrade);
            }
        });*/


}


