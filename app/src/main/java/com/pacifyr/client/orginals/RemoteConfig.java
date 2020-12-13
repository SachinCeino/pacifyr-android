package com.pacifyr.client.orginals;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.fragment.AppointmentsTabViewFragment;
import com.pacifyr.client.fragment.CouponCodeFragment;
import com.pacifyr.client.fragment.PrescriptionListFragment;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.ListPacifyrScreenActivity;
import com.pacifyr.client.home.MissedandDeclinedActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteConfig {
    public static String TAG=RemoteConfig.class.getName();

    public static String _appType="app_type";
    public static String _forceUpgrade="forced_upgrade";
    public static String _globalUrl="url_global";
    public static String _globalImageUrl="url_image";
    public static String _certificateKey="key_certificate";
    public static String _appId="id_app";
    public static String _authKey="key_auth";
    public static String _authSecret="secret_auth";

    public static String _stripeKey="key_stripe";

    public static String _paytmMId="M_ID";
    public static String _paytmChannelId="paytm_channel";
    public static String _paytmIndustryTypeId="paytm_channel";
    public static String _paytmWebsite="paytm_channel";
    public static String _paytmCallbackUrl="paytm_Callback";

    public static String _termsOfUse="terms_of_use";
    public static String _privacyPolicy="privacy_policy";
    public static String _copyRight="copy_right";

    public static String _apiUs="api_us";
    public static String _apiIndia="api_india";


    static FirebaseRemoteConfig mConfig;
    public static FirebaseRemoteConfig Config(){
        if(mConfig==null) mConfig = FirebaseRemoteConfig.getInstance();
        return mConfig;
    }
    public static void init(){

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)

                .build();
        Config().setConfigSettingsAsync(configSettings);
        Config().setDefaultsAsync(generateDefault());


    }

   public static String getAppType(){
        return Config().getString(_appType);
   }
   public static String getDynamicLinkMap(String link){
        for(String name: Screens){
            String l=Config().getString(name);
            if(l.contains(link))
                return name;
        }
        return DashboardActivity.class.getName();
   }
   public static void Update(Activity activity){
       Config().fetch(0)
               .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Log.d(TAG, "Config params updated: " + Config().getString(PrescriptionListFragment.class.getSimpleName()));

                           // After config data is successfully fetched, it must be activated before newly fetched
                           // values are returned.
                           Config().activate();
                       } else {
                           Log.d(TAG, "Config params not updated: " );
                       }

                   }
               });

   }

   public static Map<String,Object> generateDefault(){

        Map<String,Object> m=new HashMap();
        m.put(_appType,"default");

       // m.put(_apiIndia,"https://app.pacifyr.net/pacifyr/v1/");
        //m.put(_apiUs,"https://app.pacifyr.net/pacifyr/v1/");

        m.put(_forceUpgrade,false);
        m.put(_globalUrl,"https://app.pacifyr.net/pacifyr/v1/");
        m.put(_globalImageUrl,"https://pacifyrprod.storage.googleapis.com/");
        m.put(_certificateKey,"production");
        m.put(_appId,"61574");
        m.put(_authKey,"2qzAw4EjRen6JeC");
        m.put(_authSecret,"jjpWMb9HRUVnTj7");

        //stripe
        m.put(_stripeKey,"pk_live_GClqoTSF6tHpuUmqQET2nxsg");

        //paytm
       m.put(_paytmMId,"Pacify50992585761705");
       m.put(_paytmCallbackUrl,"WAP");
       m.put(_paytmIndustryTypeId,"Retail");
       m.put(_paytmWebsite,"WEBSTAGING");
       m.put(_paytmCallbackUrl,"https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=");

       m.put(_termsOfUse,"");
       m.put(_privacyPolicy,"");
       m.put(_copyRight,"");

       //dynamicLinks
       m.put(DashboardActivity.class.getSimpleName(),"");
       Screens.add(MissedandDeclinedActivity.class.getSimpleName());

       m.put(PrescriptionListFragment.class.getSimpleName(),"");
       Screens.add(PrescriptionListFragment.class.getSimpleName());

       m.put(AppointmentsTabViewFragment.class.getSimpleName(),"");
       Screens.add(AppointmentsTabViewFragment.class.getSimpleName());

       m.put(TermsOfUseActivity.class.getSimpleName(),"");
       Screens.add(TermsOfUseActivity.class.getSimpleName());

       m.put(CouponCodeFragment.class.getSimpleName(),"");
       Screens.add(CouponCodeFragment.class.getSimpleName());

       m.put( ListPacifyrScreenActivity.class.getSimpleName(),"");
       Screens.add( ListPacifyrScreenActivity.class.getSimpleName());
        return m;
   }

   public static ArrayList<String> Screens =new ArrayList<>();

}
