package com.pacifyr.client.orginals;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.pacifyr.client.activity.TermsOfUseActivity;
import com.pacifyr.client.fragment.AppointmentsTabViewFragment;
import com.pacifyr.client.fragment.CouponCodeFragment;
import com.pacifyr.client.fragment.PrescriptionListFragment;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.home.ListPacifyrScreenActivity;
import com.pacifyr.client.home.MissedandDeclinedActivity;
import com.pacifyrapp.utilitylibrary.interfaces.RemoteConfigInterface;
import com.pacifyrapp.utilitylibrary.orginals.RemoteConfigHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteConfig implements RemoteConfigInterface {
    public static String TAG = RemoteConfig.class.getName();

    public static String _appType = "app_type";
    public static String _forceUpgrade = "forced_upgrade";
    public static String _globalUrl = "url_global";
    public static String _globalImageUrl = "url_image";
    public static String _certificateKey = "key_certificate";
    public static String _appId = "id_app";
    public static String _authKey = "key_auth";
    public static String _authSecret = "secret_auth";

    public static String _stripeKey="key_stripe";

    public static String _paytmMId="M_ID";
    public static String _paytmChannelId="paytm_channel";
    public static String _paytmIndustryTypeId="paytm_industry";
    public static String _paytmWebsite="paytm_website";
    public static String _paytmCallbackUrl="paytm_Callback";

    public static String _paytmMId_prod="M_ID_prod";
    public static String _paytmChannelId_prod="paytm_channel_prod";
    public static String _paytmIndustryTypeId_prod="paytm_industrial_prod";
    public static String _paytmWebsite_prod="paytm_paytm_website_prod";
    public static String _paytmCallbackUrl_prod="paytm_Callback_prod";

    public static String _termsOfUse="terms_of_use";
    public static String _privacyPolicy="privacy_policy";
    public static String _copyRight="copy_right";

    public static String _apiUs="api_us";
    public static String _apiIndia="api_india";
    public static String _pamGlobalUrlProd="url_global_pam_prod";
    public static String _pamGlobalImageUrlProd="url_image_pam_prod";
    public static String _pamAppIdProd="id_app_pam_prod";
    public static String _pamAuthKeyProd="key_auth_pam_prod";
    public static String _pamAuthSecretProd="secret_pam_prod";

    public static String _pamGlobalUrl="url_global_pam";
    public static String _pamGlobalImageUrl="url_image_pam";
    public static String _appIdPam="id_app_pam";
    public static String _authKeyPam = "key_auth_pam";
    public static String _authSecretPam = "secret_auth_pam";
    public static String _pamStripeKey = "key_stripe_pam";
    public static String _certificateKeyDev = "development";
    public static String _pamGlobalUrlNonPamDev = "url_global_nonpam_dev";
    public static String _pamGlobalImageUrlNonPamDev = "url_image_nonpam_dev";
    public static String _pamStripeKeyNonPamDev = "key_stripe_nonpam_dev";
    public static String _appIdNonPamDev = "id_app_nonpam_dev";
    public static String _authKeyNonPamDev = "key_auth_nonpam_dev";
    public static String _authSecretNonPamDev = "secret_auth_nonpam_dev";
    public static String _devStripeKey = "stripe_dev";
    private static RemoteConfig instance;

    static FirebaseRemoteConfig mConfig;

    public static FirebaseRemoteConfig Config() {
        if (mConfig == null) mConfig = FirebaseRemoteConfig.getInstance();
        Log.w("Config", "FirebaseRemoteConfig");
        RemoteConfigHelper.setmFirebaseRConfig(mConfig, generateDefault());
        return mConfig;
    }

    public static RemoteConfig init() {
        Log.w("Config", "FirebaseRemoteConfiginit");
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)

                .build();
        Config().setConfigSettingsAsync(configSettings);
        Config().setDefaultsAsync(generateDefault());
        if (instance == null) {
            instance = new RemoteConfig();
        }
        return instance;
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
                           RemoteConfigHelper.setmFirebaseRConfig(mConfig, generateDefault());
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

        //pam production
       m.put(_pamGlobalUrlProd,"http://pam.pacifyr.net:8080/pacifyr/v1/");
       m.put(_pamGlobalImageUrlProd,"https://pam-prod.storage.googleapis.com/");
       m.put(_pamAppIdProd,"79939");
       m.put(_pamAuthKeyProd,"fVKU-SnfR-PV6FV");
       m.put(_pamAuthSecretProd,"eFYX-M4yjuCQTcK");
       //pam dev
       m.put(_pamGlobalUrl,"http://pam_test.pacifyr.net:8080/pacifyr/v1/");
       m.put(_pamGlobalImageUrl,"https://pacifyrtest.storage.googleapis.com/");
       m.put(_devStripeKey,"pk_test_FAVrlQ9hMG3RELCIcWUJpdZf");
       m.put(_appIdPam,"78571");
       m.put(_authKeyPam,"6ZSW67fV5vcUOc7");
       m.put(_authSecretPam,"7mQzMYGczcznz6z");
       //not pam dev
       m.put(_pamGlobalUrlNonPamDev,"http://testapp.pacifyr.net:8080/pacifyr/v1/");
       m.put(_pamGlobalImageUrlNonPamDev,"https://pacifyrtest.storage.googleapis.com/");
       m.put(_appIdNonPamDev,"60603");
       m.put(_authKeyNonPamDev,"RMyP9v64ke8ccet");
       m.put(_authSecretNonPamDev,"6TAfArmeSzME8su");
       m.put(_certificateKeyDev,"development");
        //stripe
        m.put(_stripeKey,"pk_live_GClqoTSF6tHpuUmqQET2nxsg");


       //paytm test
       m.put(_paytmMId,"Pacify50992585761705");
       m.put(_paytmIndustryTypeId,"Retail");
       m.put(_paytmWebsite,"WEBSTAGING");
       m.put(_paytmChannelId,"WAP");
       m.put(_paytmCallbackUrl,"https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=");
       //paytm _prod
       m.put(_paytmMId_prod,"Pacify79411204626683");
       m.put(_paytmIndustryTypeId_prod,"ECommerce");
       m.put(_paytmWebsite_prod,"DEFAULT");
       m.put(_paytmChannelId_prod,"WAP");
       m.put(_paytmCallbackUrl_prod,"https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=");

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
    private static Map<String, FirebaseRemoteConfigValue> defultVal=new HashMap();
    public static String getDefaultConfigForKey(String key){
        if(null !=mConfig){
            if(null==defultVal||defultVal.isEmpty()) {
                defultVal=mConfig.getAll();}
        }
        if (defultVal.containsKey(key)) {
            FirebaseRemoteConfigValue val = defultVal.get(key);
            Log.w("RemoteConfigs", "setDefaultConfigForKey:" + key + "|" + val);
            return val.asString();
        }
        return null;
    }

    public static ArrayList<String> Screens = new ArrayList<>();

    @Override //For UtilityNetworkService.java in UtilityLibrary
    public String getDefaultValue(String key) {
        if (null != mConfig) {
            if (null == defultVal || defultVal.isEmpty()) {
                defultVal = mConfig.getAll();
            }
        }
        if (defultVal.containsKey(key)) {
            FirebaseRemoteConfigValue val = defultVal.get(key);
            Log.w("RemoteConfigs", "setDefaultConfigForKey:" + key + "|" + val);
            return val.asString();
        }
        return null;
    }
}
