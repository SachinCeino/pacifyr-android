package com.pacifyrapp.utilitylibrary.orginals;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class RemoteConfigHelper {
    public static FirebaseRemoteConfig mFirebaseRConfig;
    public static String _certificateKey = "key_certificate";

    public static String _appType = "app_type";
    public static String _forceUpgrade = "forced_upgrade";
    public static String _globalUrl = "url_global";
    public static String _globalImageUrl = "url_image";
    public static String _appId = "id_app";
    public static String _authKey = "key_auth";
    public static String _authSecret = "secret_auth";
    public static String _paytmMId = "M_ID";

    public static String _stripeKey = "key_stripe";
    public static String _paytmChannelId = "paytm_channel";
    public static String _paytmIndustryTypeId = "paytm_industry";
    public static String _paytmWebsite = "paytm_website";
    public static String _paytmCallbackUrl = "paytm_Callback";
    public static String _paytmMId_prod = "M_ID_prod";
    public static String _paytmChannelId_prod = "paytm_channel_prod";
    public static String _paytmIndustryTypeId_prod = "paytm_industrial_prod";
    public static String _paytmWebsite_prod = "paytm_paytm_website_prod";
    public static String _paytmCallbackUrl_prod = "paytm_Callback_prod";
    public static String _termsOfUse = "terms_of_use";
    public static String _privacyPolicy = "privacy_policy";
    public static String _copyRight = "copy_right";
    public static String _apiUs = "api_us";
    public static String _apiIndia = "api_india";
    public static String _pamStripeKey = "key_stripe_pam";
    public static String _pamGlobalUrlProd = "url_global_pam_prod";
    public static String _pamGlobalImageUrlProd = "url_image_pam_prod";
    public static String _pamAppIdProd = "id_app_pam_prod";
    public static String _pamAuthKeyProd = "key_auth_pam_prod";
    public static String _pamAuthSecretProd = "secret_pam_prod";

    public static String _pamGlobalUrl = "url_global_pam";
    public static String _pamGlobalImageUrl = "url_image_pam";
    public static String _appIdPam = "id_app_pam";
    public static String _authKeyPam = "key_auth_pam";
    public static String _authSecretPam = "secret_auth_pam";
    public static String _certificateKeyDev = "development";
    public static String _pamStripeKeyNonPamDev = "key_stripe_nonpam_dev";
    public static String _pamGlobalUrlNonPamDev = "url_global_nonpam_dev";
    public static String _pamGlobalImageUrlNonPamDev = "url_image_nonpam_dev";
    public static String _devStripeKey = "stripe_dev";
    public static String _appIdNonPamDev = "id_app_nonpam_dev";
    public static String _authKeyNonPamDev = "key_auth_nonpam_dev";
    public static String _authSecretNonPamDev = "secret_auth_nonpam_dev";
    private static Map<String, Object> defultVal = new HashMap();

    public FirebaseRemoteConfig getmFirebaseRConfig() {
        return mFirebaseRConfig;
    }

    public static void setmFirebaseRConfig(FirebaseRemoteConfig mFirebaseRConfig, Map<String, Object> defaultMap) {

        RemoteConfigHelper.mFirebaseRConfig = mFirebaseRConfig;
        if(defaultMap==null||defaultMap.isEmpty()){return;}
        for(String key: defaultMap.keySet()){
            defultVal.put(key,defaultMap.get(key));
        }
    }
    public static String getDefaultConfigForKey(String key){
        return mFirebaseRConfig.getString(key);
//mFirebaseRConfig.
//        if(null !=mFirebaseRConfig){
//            if(null==defultVal||defultVal.isEmpty()) {
//               // defultVal=mFirebaseRConfig.getAll();
//            }
//        }
//        if(defultVal.containsKey(key)){
//            Object val=defultVal.get(key);
//            Log.w("RemoteConfigs","setDefaultConfigForKey:"+key+"|"+val);
//            return String.valueOf(val);
//        }
//        return null;
    }
}
