package com.pacifyrapp.utilitylibrary.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pacifyrapp.utilitylibrary.LogUtilsutility;
import com.pacifyrapp.utilitylibrary.ToastHandler;
import com.pacifyrapp.utilitylibrary.event.BusProvider;
import com.pacifyrapp.utilitylibrary.event.NotifyEvent;
import com.pacifyrapp.utilitylibrary.model.Users;

import org.json.JSONObject;



/**
 * Created by ceino on 5/12/16.
 */

public class UtilityNetworkService extends IntentService {
    /**
     * Please Check GLOBAL_URL before making Build
     **/
//    public static String GLOBAL_URL = getGlobalUrl();
//    public static String GLOBAL_IMAGE_URL = "https://pacifyrtest.storage.googleapis.com/";
//    public static  String STRIPE_PUBLISH_KEY ="pk_test_FAVrlQ9hMG3RELCIcWUJpdZf";
//
//
//    //dev
//    //    /** Please Comment these three while bilding  Pam**/
//    // public static final String APP_ID = "60603";
//    //public static final String AUTH_KEY = "RMyP9v64ke8ccet";
//    // public static final String AUTH_SECRET = "6TAfArmeSzME8su";
//
//
//    public static String ACCOUNT_KEY = "xMeisck3BS_X6xbjYTQi";
//    public static String ACCOUNT_ID = "77234";
//    public static String TURN_SERVER = "turnserver.quickblox.com";
//    public static String CIRTIFICATE_KEY = "development";
//    public static boolean isRelease = false;
//    public static boolean isProd = false;
//
//
//    //for PAM
//    /** dev*/
//    /**
//     * Please UnComment for Pam
//     **/
//    public static String APP_ID = "78571";
//    public static String AUTH_KEY = "6ZSW67fV5vcUOc7";
//    public static String AUTH_SECRET = "7mQzMYGczcznz6z";


    /*public static final String  termOfUseUrl = "https://pacifyr.com/terms.html";
    public static final String  copyrightUrl = "https://pacifyr.com/copyright.html";
    public static final String  privacypolicyUrl = "https://pacifyr.com/privacy.html";*/

    public static final String termOfUsersUrl = "https://pacifyr.com/TermsUsers.html";
    public static final String PACKAGE_PAM = "com.pacifyr.pam";
    public static final String OM_PLAYSTORE = "https://play.google.com/store/apps/details?id=com.ceino.pacifyr.omhealthcare";
    public static boolean isRelease = true;
    public static boolean isProd = true;
    public static String ACCOUNT_KEY = "";
    public static String ACCOUNT_ID = "";
    public static String TURN_SERVER = "";
    public static String CIRTIFICATE_KEY = "";
    public static String APP_ID = "";
    public static String AUTH_KEY = "";
    public static String AUTH_SECRET = "";
    public static String GLOBAL_URL = "";
    public static String GLOBAL_IMAGE_URL = "";
    public static String STRIPE_PUBLISH_KEY = "";



    /** Production*/
    /** Please Check GLOBAL_URL before making Build**/
   /*public static final String GLOBAL_URL =getGlobalUrl() ;
    //prod quickblox
    public static final String APP_ID = "61574";
    public static String GLOBAL_IMAGE_URL = "https://pacifyrprod.storage.googleapis.com/";
    public static final String STRIPE_PUBLISH_KEY = "pk_live_GClqoTSF6tHpuUmqQET2nxsg";
    public static final String TURN_SERVER = "turnserver.quickblox.com";
    public static final String AUTH_KEY = "2qzAw4EjRen6JeC";
    public static final String AUTH_SECRET = "jjpWMb9HRUVnTj7";
    public static final String ACCOUNT_KEY = "xMeisck3BS_X6xbjYTQi";
    public static final String ACCOUNT_ID = "77234";
    public static final String CIRTIFICATE_KEY = "production";*/
    /** dev*/

    // Should call this method on Application onCreate();
    public static void init(Context context) {
        ACCOUNT_KEY = "xMeisck3BS_X6xbjYTQi";
        ACCOUNT_ID = "77234";
        TURN_SERVER = "turnserver.quickblox.com";
        CIRTIFICATE_KEY = "development";
        if (context.getPackageName().equalsIgnoreCase(PACKAGE_PAM)) {
            if (isProd) {
                GLOBAL_URL = "http://pam.pacifyr.net:8080/pacifyr/v1/";
                GLOBAL_IMAGE_URL = "https://pam-prod.storage.googleapis.com/";
                STRIPE_PUBLISH_KEY = "pk_live_GClqoTSF6tHpuUmqQET2nxsg";
                CIRTIFICATE_KEY = "production";
                APP_ID = "79939";
                AUTH_KEY = "fVKU-SnfR-PV6FV";
                AUTH_SECRET = "eFYX-M4yjuCQTcK";
            } else {
                GLOBAL_URL = "http://pam_test.pacifyr.net:8080/pacifyr/v1/";
                GLOBAL_IMAGE_URL = "https://pacifyrtest.storage.googleapis.com/";
                STRIPE_PUBLISH_KEY = "pk_test_FAVrlQ9hMG3RELCIcWUJpdZf";
                CIRTIFICATE_KEY = "development";
                APP_ID = "78571";
                AUTH_KEY = "6ZSW67fV5vcUOc7";
                AUTH_SECRET = "7mQzMYGczcznz6z";
            }

        } else {
            if (isProd) {
                GLOBAL_URL = "https://app.pacifyr.net/pacifyr/v1/";
                GLOBAL_IMAGE_URL = "https://pacifyrprod.storage.googleapis.com/";
                STRIPE_PUBLISH_KEY = "pk_live_GClqoTSF6tHpuUmqQET2nxsg";
                CIRTIFICATE_KEY = "production";

                APP_ID = "61574";
                AUTH_KEY = "2qzAw4EjRen6JeC";
                AUTH_SECRET = "jjpWMb9HRUVnTj7";
            } else {
                GLOBAL_URL = "http://testapp.pacifyr.net:8080/pacifyr/v1/";
                GLOBAL_IMAGE_URL = "https://pacifyrtest.storage.googleapis.com/";
                STRIPE_PUBLISH_KEY = "pk_test_FAVrlQ9hMG3RELCIcWUJpdZf";
                CIRTIFICATE_KEY = "development";

                APP_ID = "60603";
                AUTH_KEY = "RMyP9v64ke8ccet";
                AUTH_SECRET = "6TAfArmeSzME8su";
            }

        }
    }



    public static final String pacifyrStripeUrl = "https://pacifyr.com/stripe.html";
    public static final String MARKETPLACE_URL = "https://accesshealthnet.com/who-we-help/patients/";
    public static final String PHARMACY_URL = "https://americaspharmacysource.com";


    public static final  String root_file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/Pacifyr";



    public static final String ACTION_ALL = "action.all";
    public static final String ACTION_ALL_CLASS_TAB = "action.all.class";
    public static final String ACTION_PARENT = "action.parent";
    public static final String ACTION_BOARDCAST_OUTBOX = "action.boardcast.outbox";
    public static final String ACTION_CLASS_PHOTOS = "action.class.photos";
    public static final String ACTION_CLASS_ALBUMS = "action.class.albums";
    public static final String ACTION_CLASS_FEED = "action.class.feed";
    public static final String ACTION_CLASS_PARENT_DETAIL= "action.class.parent.detail";
    public static final String ACTION_ALL_Users= "action.all.users";
    private static final String SKIP_PARAM = "SKIP";
    private static final String LIMIT_PARAM = "LIMIT";








    public static Users users;


    public static String getGlobalUrl() {

        /**-------TEST URL--------- **/

        /**for  OM,Pacifyr,and LeadersUp**/
        //GLOBAL_URL = "http://testapp.pacifyr.net:8080/pacifyr/v1/";

        /**for only Pam**/
        GLOBAL_URL = "http://pam_test.pacifyr.net:8080/pacifyr/v1/";


        /**-----------LIVE URL----------------**/

        /**for  OM,Pacifyr,and LeadersUp**/
        //GLOBAL_URL="https://app.pacifyr.net/pacifyr/v1/";

        return GLOBAL_URL;
    }


    public UtilityNetworkService() {

        super("uNetworkService");

    }

    public static void startActionAll(Context context) {

        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_ALL);
        context.startService(intent);
    }
    public static void startActionAllClassTAb(Context context) {

        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_ALL_CLASS_TAB);
        context.startService(intent);
    }

    public static void startActionParents(Context context) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_PARENT);
        context.startService(intent);
    }
    public static void startActionClassFeed(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_CLASS_FEED);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }
    public static void startActionClassParents(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_PARENT);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }
    public static void startActionCLassPhotos(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_CLASS_PHOTOS);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }
    public static void startActionCLassAlbums(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_CLASS_ALBUMS);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }
    public static void startActionCLassParentDetail(Context context, String skip) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_CLASS_PARENT_DETAIL);
        intent.putExtra(SKIP_PARAM, skip);
        context.startService(intent);
    }
    public static void startActionUsers(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_ALL_Users);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }

    /*public static void startActionBoardCastOUTBOX(Context context, int skip, int limit) {
        Intent intent = new Intent(context, UtilityNetworkService.class);
        intent.setAction(ACTION_BOARDCAST_OUTBOX);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }*/
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equalsIgnoreCase(ACTION_ALL)) {

        }
       /* else if(intent.getAction().equalsIgnoreCase(ACTION_ALL_CLASS_TAB)) {

            handleActionGetClassParents(0,10);


        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_PARENT)) {
            handleActionGetClassParents(intent.getIntExtra(SKIP_PARAM, 0), intent.getIntExtra(LIMIT_PARAM, 10));
        }

        else if(intent.getAction().equalsIgnoreCase(ACTION_CLASS_PARENT_DETAIL)) {
            handleActionGetParentDetail(intent.getStringExtra(SKIP_PARAM));
        }*/
        /*else if(intent.getAction().equalsIgnoreCase(ACTION_BOARDCAST_OUTBOX)) {
            //handleActionGetBOARDCASTOUTBOX(intent.getIntExtra(SKIP_PARAM, 0), intent.getIntExtra(LIMIT_PARAM, 10));
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_ALL_Users)) {
            webUsers(intent.getIntExtra(SKIP_PARAM, 0), intent.getIntExtra(LIMIT_PARAM, 10));
        }*/
    }






    /*private void handleActionGetBOARDCASTOUTBOX(final int skip, int limit) {
        try {
         String schoolid = PrefManager.getInstance(getApplicationContext()).getSCHOOLID();
            String userid = PrefManager.getInstance(getApplicationContext()).getUserID();
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtilsutility.LOGD("jithish", "UNS handleActionGetBOARDCASTOUTBOX " + json);
                    if(json == null) {
                        postEventOnMainThread(new NotifyEvent(false, status));
                    } else {
                        if(json != null) try {
                            if(skip == 0) {
                                modelBoardCastOutBox = GsonUtils.getInstance().gsonToModelBoardCastOutBox(json);
                                modelBoardCastOutBoxPaged = null;
                                postEventOnMainThread(new NotifyEvent(true, false));
                            } else {
                                if(modelBoardCastOutBox != null) {
                                    modelBoardCastOutBoxPaged = GsonUtils.getInstance().gsonToModelBoardCastOutBox(json);
                                    postEventOnMainThread(new NotifyEvent(true, true));
                                } else {
                                    modelBoardCastOutBox = GsonUtils.getInstance().gsonToModelBoardCastOutBox(json);
                                    modelBoardCastOutBoxPaged = null;
                                    postEventOnMainThread(new NotifyEvent(true, false));
                                }
                            }


                        } catch (Exception e){
                            //Crashlytics.logException(e);
                            modelBoardCastOutBox = new ModelBoardCastOutBox();
                            postEventOnMainThread(new NotifyEvent(false, e));
                            LogUtilsutility.LOGD("exception", "UNS handleActionGetBOARDCASTOUTBOX e" + e.getMessage());
                            e.printStackTrace();
                        }
                    }


                }
            };

            AQuery aq = new AQuery(this);
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("Content-Type", "application/json");
            String url = GLOBAL_URL + "classes/broadcast?where=" + URLEncoder.encode("{\"schoolid\":\""+schoolid+"\",\"createdBy\":\""+userid+"\" }","UTF-8") + "&skip=" + skip + "&limit=" + limit + "";
            aq.ajax(url, JSONObject.class, cb);
            LogUtilsutility.LOGD("jithish", "UNS all handleActionGetBOARDCASTOUTBOX url " + url);
//http://test.snap-support.com:8080/topschool/api/v1/user?where=%7B%22accessToClass%22%3A%22581c09db2f914512c8966413%7D&skip=0&limit=10
        } catch (Exception e){
            //Crashlytics.logException(e);
            ToastHandler.newInstance(getApplicationContext()).mustShowToast("Please try again " + e.getMessage());
            LogUtilsutility.LOGD("exception", "UNS handleActionGetBOARDCASTOUTBOX exce " + e.getMessage());
        }
    }*/



    private void handleActionGetParentDetail(String parentid) {
        try {
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtilsutility.LOGD("jithish", "NS handleActionGetParentDetail " + json);
                    if(json == null) {
                        postEventOnMainThread(new NotifyEvent(false, status));
                    } else {
                        try {
                            //  FragmentClassParentDetail.parentDetail = GsonUtils.getInstance().gsonToModelClassParentDetail(json);
                            postEventOnMainThread(new NotifyEvent(true, false));

                        } catch (Exception e){

                            //   FragmentClassParentDetail.parentDetail  = new ModelClassParentDetail();
                            postEventOnMainThread(new NotifyEvent(false, e));
                            LogUtilsutility.LOGD("exception", "NS parent detail" + e.getMessage());
                            e.printStackTrace();
                        }
                    }


                }
            };
            AQuery aq = new AQuery(this);
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("Content-Type", "application/json");
            String url = GLOBAL_URL + "user/"+parentid;

            aq.ajax(url, JSONObject.class, cb);
            LogUtilsutility.LOGD("exception", "NS parent detail" + url);
        } catch (Exception e){
            LogUtilsutility.LOGD("exception", "NS parent detail " + e.getMessage());
            ToastHandler.newInstance(getApplicationContext()).mustShowToast("Please try again " + e.getMessage());
        }
    }
    public Boolean isValid(Object object) {
        return object != null;

    }


    /*private void webUsers(final int skip, int limit) {
        try {
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtilsutility.LOGD("utility", "uNS webUsers " + json);
                    if(json == null) {
                        users=null;
                        postEventOnMainThread(new NotifyEvent(false, status));
                    } else {
                        try {
                            Users usertemp = GsonUtils.getInstance().gsonToModelUsers(json);
                            if(isValid(usertemp)) {

                                if(skip == 0) {
                                    usersall = GsonUtils.getInstance().gsonToModelUsers(json);
                                    LogUtilsutility.LOGD("utility", "uNS webUsers  skip usersall " + usersall.getResultsList());
                                }
                                else {
                                    if(usersall != null)
                                        usersall.addResults(GsonUtils.getInstance().gsonToModelUsers(json).getResults());
                                    else usersall = GsonUtils.getInstance().gsonToModelUsers(json);
                                    LogUtilsutility.LOGD("utility", "uNS webUsers  skip else  usersall " + usersall.getResultsList());
                                }

                                if(usertemp.getTotal_count() > usersall.getResultsList().size()) {
                                    {
                                        LogUtilsutility.LOGD("utility", "uNS webUsers   usersall " + usersall.getResultsList() + " total count "+usertemp.getTotal_count() );
                                        webUsers(usersall.getResultsList().size(), 10);
                                    }
                                } else postEventOnMainThread(new NotifyEvent(true, true));

                            }


                        } catch (Exception e){
                            //Crashlytics.logException(e);
                            users = new Users();
                         //   usersPaged = null;
                            postEventOnMainThread(new NotifyEvent(false, e));
                            LogUtilsutility.LOGD("utility", "uNS webUsers " + e.getMessage());
                        }
                    }


                }
            };
            AQuery aq = new AQuery(this);
            cb.header("topschool-app-id", "appid");
            cb.header("topschool-app-key", "appkey");
            cb.header("Content-Type", "application/json");

            String schoolid=      PrefManager.getInstance(getApplicationContext()).getSCHOOLID();                                                                     //   . where={"schoolid":{"$in":["564d7e3749be00edef4dd790"]}}&skip=0&limit=10
            aq.ajax(UtilityNetworkService.GLOBAL_URL + "classes/user?where=" + URLEncoder.encode("{\"schoolid\":{\"$in\":[\""+schoolid+"\" ]}}","UTF-8"), JSONObject.class, cb);
           // aq.ajax(UtilityNetworkService.GLOBAL_URL + "classes/user?where=" + URLEncoder.encode("{\"schoolid\":{\"$in\":[\""+companyid+"\" ]}}","UTF-8")+"&skip=" +skip+"&limit="+limit, JSONObject.class, cb);
            //aq.ajax(url, JSONObject.class, cb);
           // LogUtils.LOGD("exception", "uNS webUsers" + url);
        } catch (Exception e){
            LogUtilsutility.LOGD("utility", "uNS webUsers " + e.getMessage());
            ToastHandler.newInstance(getApplicationContext()).mustShowToast("Please try again " + e.getMessage());
        }
    }*/

    /*private void appUpdateCheck()
    {
        String currentVersion, latestVersion;
        Dialog dialog;
       *//* try {
       *//**//*     PackageManager pm = this.getPackageManager();
            PackageInfo pInfo = null;

            try {
                pInfo =  pm.getPackageInfo(this.getPackageName(),0);

            } catch (Exception e1) {
                LogUtils.LOGD("utility", "uNS PackageManager " + e1.getMessage());
            }
            currentVersion = pInfo.versionName;
            try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue
                        ("itemprop","softwareVersion").first().text();

            }catch (Exception e){
                e.printStackTrace();

            }*//*



        } catch (Exception e){
            LogUtilsutility.LOGD("utility", "uNS appUpdateCheck " + e.getMessage());

        }
    }*/
    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }
}
