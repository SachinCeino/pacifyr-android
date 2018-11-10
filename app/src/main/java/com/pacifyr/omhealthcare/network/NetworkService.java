package com.pacifyr.omhealthcare.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.model.pacifyr.LoginModel;
import com.ceino.utilitylibrary.model.pacifyr.MAdminDashboardByWeek;
import com.ceino.utilitylibrary.model.pacifyr.MAdminDashboardMonth;
import com.ceino.utilitylibrary.model.pacifyr.MAdminDashboardYear;
import com.ceino.utilitylibrary.model.pacifyr.MAllSessionsWithDateRange;
import com.ceino.utilitylibrary.model.pacifyr.MEarningsRoot;
import com.ceino.utilitylibrary.model.pacifyr.MGetAllPacifyrSessions;
import com.ceino.utilitylibrary.model.pacifyr.MGetSavedCards;
import com.ceino.utilitylibrary.model.pacifyr.MGetSessionDetails;
import com.ceino.utilitylibrary.model.pacifyr.MGetSessionRating;
import com.ceino.utilitylibrary.model.pacifyr.MLastSession;
import com.ceino.utilitylibrary.model.pacifyr.MListAllEmotions;
import com.ceino.utilitylibrary.model.pacifyr.MListEmployees;
import com.ceino.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.ceino.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.ceino.utilitylibrary.model.pacifyr.MPastSessions;
import com.ceino.utilitylibrary.model.pacifyr.ModelCustomerDashboard2;
import com.ceino.utilitylibrary.model.pacifyr.ModelPacifyrDashboard;
import com.ceino.utilitylibrary.model.pacifyr.ModelSearchPacifyr;
import com.ceino.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.ceino.utilitylibrary.model.pacifyr.RespPostEditProfile;
import com.ceino.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.ceino.utilitylibrary.model.pacifyr.RespSetSessionAsConnected;
import com.ceino.utilitylibrary.model.pacifyr.RespStartSessionWithPacifyr;
import com.ceino.utilitylibrary.model.pacifyr.post.PRegistrationModel;
import com.ceino.utilitylibrary.model.pacifyr.post.PSearchPacifyrWOptionRequest;
import com.ceino.utilitylibrary.model.pacifyr.post.PStartSessionWithPacifyr;
import com.ceino.utilitylibrary.model.pacifyr.post.PostEditProfileRequest;
import com.ceino.utilitylibrary.model.pacifyr.post.PostRateCustomerSession;
import com.ceino.utilitylibrary.model.pacifyr.post.PostRatePacifyr;
import com.ceino.utilitylibrary.model.pacifyr.post.PostRatePacifyrSession;
import com.ceino.utilitylibrary.model.pacifyr.post.PostStopSessionWithPacifyr;
import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.pacifyr.omhealthcare.R;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.event.BusProvider;
import com.pacifyr.omhealthcare.event.NotifyEvent;
import com.pacifyr.omhealthcare.model.ItemData;
import com.pacifyr.omhealthcare.model.MCustomerList;
import com.pacifyr.omhealthcare.utility.EventMessage;
import com.pacifyr.omhealthcare.utils.Constants;
import com.pacifyr.omhealthcare.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ceino on 27/6/17.
 */

public class NetworkService  extends IntentService {

     public static final String GLOBAL_URL = UtilityNetworkService.GLOBAL_URL;
    public static String GLOBAL_IMAGE_URL = UtilityNetworkService.GLOBAL_IMAGE_URL;
   // public static String GLOBAL_IMAGE_URL = UtilityNetworkService.GLOBAL_IMAGE_URL;

/*- Chat Feature - Customer list and Detial. - Bugs*/
    PrefManager prefM;
    String token;
    String userid;
    public static final String ACTION_SPECIALIST = "action.SPECIALIST";
    public static final String ACTION_ALL = "action.all";
    public static final String ACTION_SEARCH_PACIFYR="action.search.pacifyr";
    public static final String ACTION_EDIT_PROFILE = "action.edit.profile";
    public static final String ACTION_CUSTOMER_DASHBOARD="action.customer.dashboard";
    public static final String ACTION_ADMIN_DASHBOARD_WEEK="action.admin.dashboard.week";
    public static final String ACTION_ADMIN_DASHBOARD_MONTH="action.admin.dashboard.month";
    public static final String ACTION_LIST_CUSTOMER_PAGI = "action.list.customer.pagi";
    public static final String ACTION_ADMIN_DASHBOARD_YEAR="action.admin.dashboard.year";
    public static final String ACTION_PACIFYR_DASHBOARD="action.pacifyr.dashboard";
    public static final String ALL_GET_SESSIONS_DATE_RANGE="all.get.sessions.date.range";
    public static final String ACTION_EARNINGS_API="action.earnings.api";
    public static final String GET_ALL_PACIFYR_SESSIONS="get.all.pacifyr.sessions";
    public static final String ACTION_GET_SAVED_CARDS="action.get.saved.cards";
    public static final String ACTION_GET_SESSION_DETAILS="action.get.session.details";
    public static final String ACTION_GET_SESSION_RATING="action.get.session.rating";
    public static final String ACTION_GET_LIST_EMOTIONS="action.get.list.emotions";
    public static final String ACTION_GET_LIST_EMPLOYEES="action.get.list.employees";
    public static final String ACTION_GET_LIST_NEGATIVE_EMOTIONS="action.get.list.negative.emotions";
    public static final String ACTION_GET_LIST_POSITIVE_EMOTIONS="action.get.list.positive.emotions";
    public static final String ACTION_POST_EDIT_PROFILE_REQUEST="action.post.edit.profile.request";
    public static final String ACTION_RESP_SEARCH_PACIFYR_WOPTIONS="action.response.search.pacifyr.options";
    public static final String ACTION_REQ_SEARCH_PACIFYR_WOPTIONS="action.request.search.pacifyr.options";

    public static boolean isConnectSinglePacifyer=false;
    public static ModelSpecialist modelSpecialist;
    public static ModelSpecialist.Results[] modelResults;
    public static ModelCustomerDashboard2 modelCustomerDashboard2;
    public static MLastSession lastSession;

    public static ModelSearchPacifyr modelSearchPacifyr;
    public static ModelUserProfile mCurrentUser;
    public static LoginModel loginModel;
    public static List<ModelSearchPacifyr.Results> searchResults;
    public static MAdminDashboardByWeek adminDashboardWeek;
    public static MAdminDashboardMonth adminDashboardMonth;
    public static MAdminDashboardYear adminDashboardYear;
    public static MAllSessionsWithDateRange modelAllSessionsDateRange;
    public static MEarningsRoot modelEarningsAPI;
    public static MGetAllPacifyrSessions modelAllPacifyrSessions;
    public static MGetSavedCards modelGetSavedCards;
    public static MGetSessionDetails modelGetSessionDetails;
    public static MGetSessionRating modelGetSessionRating;
    public static MListAllEmotions modelAllEmotions;
    public static MListEmployees modelAllEmployees;
    public static MListNegativeEmotions modelListNegEmotions;
    public static MListPositiveEmotions modelListPosEmotions;
    public static ModelPacifyrDashboard modelPacifyrDashboard;
    public static PostEditProfileRequest pEditProfileRequest;
    public static RespPostEditProfile pEditProfileResponse;
    public static PostRateCustomerSession pRateCustomerSessiom;
    public static PostRatePacifyr pRatePacifyr;
    public static PostRatePacifyrSession pRatePacifyrSession;
    public static RespSearchPacifyrWOption pSearchPacifyrWOptionsResponse;
    public static PSearchPacifyrWOptionRequest pSearchPacifyrWOptionsRequest;
    public static PRegistrationModel pRegistartion;
    public static PostStopSessionWithPacifyr pStopSesssionPAcifyr;
    public static RespSetSessionAsConnected respSetSessionAsConnected;
    public static RespStartSessionWithPacifyr respStartSessionWithPacifyr;
    public static PStartSessionWithPacifyr pSatertSessionPacifyr;
    public static MCustomerList customerList;
    public static MCustomerList listCustomer1Paged;

    public static MPastSessions mPastSessions;
    public static MPastSessions mPastSessionsPaged;

    public static ItemData countryData;
    public static JSONObject editProfileObject=new JSONObject();

    private static final String SKIP_PARAM = "SKIP";
    private static final String LIMIT_PARAM = "LIMIT";

    public static  boolean isRelease=UtilityNetworkService.isRelease;

    public NetworkService() {
        
        super("NetworkService");

    }
    public static void startActionAll(Context context) {
        //LogUtils.LOGD("jithish", "NS startActionAll");
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_ALL);
        context.startService(intent);
    }
    public  static void startActionSearchPacifyr(Context context){
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_SEARCH_PACIFYR);
        context.startService(intent);
    }
    public static void startActionEditProfile(Context context){
        Intent intent= new Intent(context,NetworkService.class);
        intent.setAction(ACTION_EDIT_PROFILE);
        context.startService(intent);
    }

    public static void startActionListCustomer(Context context, int skip, int limit) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_LIST_CUSTOMER_PAGI);
        intent.putExtra(SKIP_PARAM, skip);
        intent.putExtra(LIMIT_PARAM, limit);
        context.startService(intent);
    }

    public static void startActionSearchPacifyrWithOption(Context context,String usrEmotion)
    {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_RESP_SEARCH_PACIFYR_WOPTIONS);
        intent.putExtra("emotion",usrEmotion);
        context.startService(intent);

    }
    public static void startActionSpecialist(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_SPECIALIST);
        context.startService(intent);
    }
    public  static void startActionCutomerDashboard(Context context){
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_CUSTOMER_DASHBOARD);
        context.startService(intent);
    }
    public  static void startActionPacifyrDashboard(Context context){
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_PACIFYR_DASHBOARD);
        context.startService(intent);
    }
    public static void webAdminDashboardByWeek(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_ADMIN_DASHBOARD_WEEK);
        context.startService(intent);
    }

    public static void webAdminDashboardMonth(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_ADMIN_DASHBOARD_MONTH);
        context.startService(intent);
    }

    public static void webAdminDashboardYear(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_ADMIN_DASHBOARD_YEAR);
        context.startService(intent);
    }
    public static void webAllSessionWithDateRange(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ALL_GET_SESSIONS_DATE_RANGE);
        context.startService(intent);
    }
    public static void webEarningsAPI(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_EARNINGS_API);
        context.startService(intent);
    }


    public static void webGetAllPAcifyrSessions(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(GET_ALL_PACIFYR_SESSIONS);
        context.startService(intent);
    }
    public static void webGetSavedCards(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_SAVED_CARDS);
        context.startService(intent);
    }
    public static void webGetSessionDetails(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_SESSION_DETAILS);
        context.startService(intent);
    }
    public static void webGetSessionRating(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_SESSION_RATING);
        context.startService(intent);
    }
    public static void webListEmotions(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_LIST_EMOTIONS);
        context.startService(intent);
    }
    public static void webListAlllEmployees(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_LIST_EMPLOYEES);
        context.startService(intent);
    }
    public static void webListNegativeEmotions(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_LIST_NEGATIVE_EMOTIONS);
        context.startService(intent);
    }
    public static void webListPositiveEmotions(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_GET_LIST_POSITIVE_EMOTIONS);
        context.startService(intent);
    }
    public static void webEditProfile(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_POST_EDIT_PROFILE_REQUEST);
        context.startService(intent);
    }



    /* public  static void startActionProfile(Context context,String usrID){
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_PROFILE);
        intent.putExtra("id",usrID);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equalsIgnoreCase(ACTION_SPECIALIST)) {
            handleActionSpecilaist();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_SEARCH_PACIFYR)) {
            handleActionSearchPacifyr();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_REQ_SEARCH_PACIFYR_WOPTIONS)) {

            String emotion = intent.getExtras().getString("emotion");
            //webSearchPacifyrWOptions(emotion);
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_CUSTOMER_DASHBOARD)) {
            handleActionCustomerDashboard();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_PACIFYR_DASHBOARD)) {
            handleActionPacifyrDashboard();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_ADMIN_DASHBOARD_WEEK)) {
            webAdminDashboardWeek();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_ADMIN_DASHBOARD_MONTH)) {
            webAdminDashboardMonth();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_ADMIN_DASHBOARD_YEAR)) {
            webAdminDashboardWeek();
        }
        else if(intent.getAction().equalsIgnoreCase(ALL_GET_SESSIONS_DATE_RANGE)) {
            webGetAllSessionsDateRange();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_EARNINGS_API)) {
            webEarningsAPI();
        }
        else if(intent.getAction().equalsIgnoreCase(GET_ALL_PACIFYR_SESSIONS)) {
            GetAllPAcifyrSessions();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_SAVED_CARDS)) {
            getSavedCards();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_SESSION_DETAILS)) {
            getSessionDetails();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_SESSION_RATING)) {
            getSessionRating();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_LIST_EMOTIONS)) {
            listAllEmotions();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_LIST_EMPLOYEES)) {
            listAllEmployees();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_LIST_NEGATIVE_EMOTIONS)) {
            listNegativeEmotions();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_GET_LIST_POSITIVE_EMOTIONS)) {
            listPositiveEmotions();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_POST_EDIT_PROFILE_REQUEST)){
            editProfile();
        }
        else if(intent.getAction().equalsIgnoreCase(ACTION_EDIT_PROFILE)) {
            Bundle bundle=intent.getExtras();
            editProfile();

    } else if(intent.getAction().equalsIgnoreCase(ACTION_LIST_CUSTOMER_PAGI)) {
        handleActionGetListCustomers(intent.getIntExtra(SKIP_PARAM, 0), intent.getIntExtra(LIMIT_PARAM, 10));
    }

    }

    private void handleActionGetListCustomers(final int skip, int limit) {
        try {
            PrefManager prefM= PrefManager.getInstance(getApplicationContext());
            String token=prefM.getAccessToken();
            String userid=prefM.getUserID();
            AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtils.LOGD("SAMSAD", " ns handleActionGetListCustomers result" + json + " " + status.getMessage());
                    if(json == null) {
                        postEventOnMainThread(new NotifyEvent(false, status));
                    } else {
                        try {
                            if (skip == 0) {
                                customerList = GsonUtils.getInstance().gsonToMCustomerList(json);
                                listCustomer1Paged = null;
                                postEventOnMainThread(new NotifyEvent(true, false));
                            } else {
                                if (customerList != null) {
                                    listCustomer1Paged = GsonUtils.getInstance().gsonToMCustomerList(json);
                                    postEventOnMainThread(new NotifyEvent(true, true));

                                } else {
                                    customerList = GsonUtils.getInstance().gsonToMCustomerList(json);
                                    listCustomer1Paged = null;
                                    postEventOnMainThread(new NotifyEvent(true, false));
                                }
                            }
                        } catch (Exception e) {
                            //Crashlytics.logException(e);
                            customerList = new MCustomerList();
                            postEventOnMainThread(new NotifyEvent(false, e));
                            LogUtils.LOGD("exception", "NS list customers  top " + e.getMessage());
                            e.printStackTrace();
                        }


                    }

                }
            };

            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);

            String url = "session/pacifyr/uniquecustomersessions?userId=" + userid+"&skip=" + skip + "&limit=" + limit ;
            aq.ajax(GLOBAL_URL + url, JSONObject.class, cb);
        }
        catch (Exception e)
        {
            //newInstance(getApplicationContext()).showToast("Please try again " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editProfile() {
        try {
            prefM = PrefManager.getInstance(getApplicationContext());
            token = prefM.getAccessToken();
            userid = prefM.getUserID();
            if (isValid(editProfileObject)) {
                String url = GLOBAL_URL + "user/editprofile/" + userid;
                com.koushikdutta.async.future.FutureCallback<JsonObject> futureCallback = new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            if (isValid(result)) {
                                final JSONObject jsonObject = new JSONObject(result.toString());
                                String js = result.toString();
                                String phone = jsonObject.getString("phoneNumber");
                                if (isValid(phone)) {
                                    //Toast.makeText(getApplicationContext(), "Updated profile", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception eee) {
                            //hideProgress();
                            Toast.makeText(getApplicationContext(), "Error :" + e.getMessage() + ", ex :" + eee.getMessage(), Toast.LENGTH_LONG).show();
                            eee.printStackTrace();
                        }
                    }
                };
                String json = editProfileObject.toString();
                Ion.with(getApplicationContext()).load(url).setHeader("AccessToken", token).setHeader("enctype", "multipart/form-data").setMultipartParameter("data", json).asJsonObject().setCallback(futureCallback);
            }
        }catch (Exception e)
        {

        }
    }

    private void listPositiveEmotions() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        String url = GLOBAL_URL+"emotions/positive";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelListPosEmotions= GsonUtils.getInstance().gsonToMListPositiveEmotions(json);
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.list_positive_emotions)));
                    postEventOnMainThread(new NotifyEvent(true, false));//succes,notpaged
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        //cb.header("AccessToken",token);
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void listNegativeEmotions() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        String url = GLOBAL_URL+"emotions/negative";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelListNegEmotions= GsonUtils.getInstance().gsonToMListNegativeEmotions(json);
                    postEventOnMainThread(new NotifyEvent(true, false));//succes,notpaged
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.list_negative_emotions)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void listAllEmployees() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        String url = GLOBAL_URL+"admin/employees?companyId="+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllEmployees= GsonUtils.getInstance().gsonToMListEmployees(json);
                    postEventOnMainThread(new NotifyEvent(true, false));
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.list_all_employees)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void listAllEmotions() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        String url = GLOBAL_URL+"emotions/ordered";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllEmotions= GsonUtils.getInstance().gsonToMListAllEmotionsI(json);
                    postEventOnMainThread(new NotifyEvent(true, false));
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.list_emotions)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void getSessionRating() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"rating/getSessionRating/596472c3dc224d16489de49b?userId="+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelGetSessionRating= GsonUtils.getInstance().gsonToMGetSessionRating(json);
                    EventBus.getDefault().post(new EventMessage(getString(R.string.get_session_rating)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void getSessionDetails() {

        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"user/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelGetSessionDetails= GsonUtils.getInstance().gsonMGetSessionDetails(json);
                    EventBus.getDefault().post(new EventMessage(getString(R.string.get_session_details)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void getSavedCards() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"payment/savedcard/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelGetSavedCards= GsonUtils.getInstance().gsonToMGetSavedCards(json);
                    EventBus.getDefault().post(new EventMessage(getString(R.string.earnings_api)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void GetAllPAcifyrSessions() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"session/all/pacifyr?userId="+userid+"&skip=0&limit=10";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllPacifyrSessions= GsonUtils.getInstance().gsonToMGetAllPacifyrSessions(json);
                    EventBus.getDefault().post(new EventMessage(getString(R.string.earnings_api)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);

    }

    private void webEarningsAPI() {

        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url=GLOBAL_URL+"earnings?pacifyrId=594cdac0dc224d0f482f2842&skip=0&limit=10&fromDate=2017-07-20&toDate=2017-07-27";
        //String url = GLOBAL_URL+"earnings?pacifyrId="+userid+"&skip=0&limit=10&fromDate=2017-07-20&toDate=2017-07-27";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelEarningsAPI= GsonUtils.getInstance().gsonToMEarningsAPI(json);
                    postEventOnMainThread(new NotifyEvent(true, false));
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.earnings_api)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void webGetAllSessionsDateRange() {
        //http://test.ceino.com:8080/pacifyr/v1/admin/sessions?fromDate=2017-08-07&toDate=2017-08-07&companyId=596dbfb1f36d287d588a46e3
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"admin/sessions?fromDate=2017-08-07&toDate=2017-08-07&companyId=596dbfb1f36d287d588a46e3";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelAllSessionsDateRange= GsonUtils.getInstance().gsonToMAllSessionsWithDateRange(json);
                    modelAllSessionsDateRange.toString();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.get_sessions_date_range)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void webAdminDashboardYear() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"admin/dashboard/byYear/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    adminDashboardYear= GsonUtils.getInstance().gsonToMAdminDashboardYear(json);
                    adminDashboardYear.toString();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_admin_dash_year)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void webAdminDashboardMonth() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"admin/dashboard/byMonth/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    adminDashboardMonth= GsonUtils.getInstance().gsonToMAdminDashboardMonth(json);
                    adminDashboardMonth.toString();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_admin_dash_month)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void webAdminDashboardWeek() {
        prefM= PrefManager.getInstance(getApplicationContext());
        token=prefM.getAccessToken();
        userid=prefM.getUserID();
        //http://test.ceino.com:8080/pacifyr/v1/admin/dashboard/byWeek/596dbfb1f36d287d588a46e3
        String url = GLOBAL_URL+"admin/dashboard/byWeek/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    adminDashboardWeek= GsonUtils.getInstance().gsonToMAdminDashboardByWeek(json);
                    adminDashboardWeek.toString();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_admin_dash_week)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);
    }

    private void handleActionSpecilaist(){

        PrefManager prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();

        String url = GLOBAL_URL+"specialities";
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelSpecialist= GsonUtils.getInstance().gsonToModelSpecialist(json);
                    modelResults=modelSpecialist.getResults();
                    modelSpecialist.toString();
                    String js=json.toString();
                    //Toast.makeText(getApplicationContext(), js, Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_message_from_service)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AQuery aq = new AQuery(getApplicationContext());
        cb.header("Content-Type", "application/json");
        cb.header("AccessToken",token);
        aq.ajax(url,JSONObject.class, cb);

    }

    private void handleActionSearchPacifyr(){

        PrefManager prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();

        String url; /*= GLOBAL_URL+"search/pacifyrs";*/
        url = GLOBAL_URL + getRoleSearchMap().get(prefM.getCustomerRole());
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    pSearchPacifyrWOptionsResponse= GsonUtils.getInstance().gsonToRespSearchPacifyrWOption(json);
                    //searchResults=modelSearchPacifyr.getResultsList();
                    pSearchPacifyrWOptionsResponse.toString();
                    String js=json.toString();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_message_srch_pacfyr)));
                } catch (Exception e) {
                    e.printStackTrace();//RespSearchPacifyrWOption
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("initiatorId", userid);
            jsonObject.putOpt("companyId",prefM.getCompanyId());
            if(prefM.getCustomerRole().equals("employee")){
                jsonObject.putOpt("includeChargeablePacifyrs",false);
            }
            jsonObject.putOpt("country",prefM.getCountry());
            jsonObject.putOpt("country", prefM.getCountry());
         //   jsonObject.putOpt("gender","Male");
            //jsonObject.putOpt("price_start", 500);
            //jsonObject.putOpt("price_end", 2000);

            List <String> list = new ArrayList<String>();
            if(isValid(PacifyerActivity.call_emotion)){
                list.add(PacifyerActivity.call_emotion.getTitle());
            }else{
                list.add("Depression");
            }
            //list.add("b");
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }

            jsonObject.putOpt("specialities",array);
            aq.post(url, jsonObject, JSONObject.class, cb);
            //aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void handleActionCustomerDashboard(){

        PrefManager prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"dashboard/customer/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelCustomerDashboard2= GsonUtils.getInstance().gsonToModelCustomerDashboard2(json);
                    String js=status.getMessage();
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.event_customer_dashboard)));
                    postEventOnMainThread(new NotifyEvent(true, false));//succes,notpaged
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void handleActionPacifyrDashboard(){

        PrefManager prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();
        String url = GLOBAL_URL+"dashboard/pacifyr/"+userid;
        url=GLOBAL_URL+"dashboard/pacifyr/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    modelPacifyrDashboard= GsonUtils.getInstance().gsonToModelPacifyrDashboard(json);
                    String js=status.getMessage();
                    String jk=status.getError();
                    //EventBus.getDefault().post(new EventMessage(getString(R.string.event_customer_dashboard)));
                    postEventOnMainThread(new NotifyEvent(true, false));//succes,notpaged
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void getBillingData(Context context, String Id, AjaxCallback cb) {

        PrefManager prefM = PrefManager.getInstance(context);
        String token = prefM.getAccessToken();
        String userid = prefM.getUserID();
        String url = GLOBAL_URL + "dashboard/pacifyr/" + userid;
        url = GLOBAL_URL + "session/billing/" + Id;

        try {
            AQuery aq = new AQuery(context);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url, JSONObject.class, cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void postEventOnMainThread(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(event);
                // bus.post(event);
            }
        });

    }

    public Boolean isValid(String text) {
        if (text != null)
            if (!text.trim().equalsIgnoreCase(""))
                return true;
        return false;

    }

    public Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }




   /* private void handleActionProfile(String id){

        PrefManager prefM= PrefManager.getInstance(getApplicationContext());
        String token=prefM.getAccessToken();
        String userid=prefM.getUserID();

        //http://test.ceino.com:8080/pacifyr/v1/user/profile/594cdac0dc224d0f482f2842
        String url = GLOBAL_URL+"user/profile/"+userid;
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    mCurrentUser= GsonUtils.getInstance().gsonToModelUserProfile(json);
                    String js=status.getMessage();
                    EventBus.getDefault().post(new EventMessage(getString(R.string.event_user_profile)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            AQuery aq = new AQuery(getApplicationContext());
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            //aq.post(url, jsonObject, JSONObject.class, cb);
            aq.ajax(url,JSONObject.class, cb);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    public HashMap<String , String> getRoleSearchMap() {
        HashMap<String , String> roleSearchApiMap = new HashMap<String , String>();

        roleSearchApiMap.put("customer", "search/pacifyrs");
        roleSearchApiMap.put("employee", "search/pacifyrs/forEmployees");
        roleSearchApiMap.put("paidMember", "search/pacifyrs/forPaidMembers");

        return roleSearchApiMap;
    }

    public void sessionTracking(String sessionId, AjaxCallback cb) {
        try {
            PrefManager prefM = PrefManager.getInstance(this);
            String token = prefM.getAccessToken();
            String userID = prefM.getUserID();
            String url = GLOBAL_URL + "session/tracker/" + sessionId + "/" + userID;
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unratedSessions(AjaxCallback cb) {
        try {
            PrefManager prefM = PrefManager.getInstance(this);
            String token = prefM.getAccessToken();
            String userID = prefM.getUserID();
            String url = GLOBAL_URL + "session/customer/unrated?userId=" + userID;
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInstallation(AjaxCallback cb) {
        try {
            String uuid = Constants.getUDID();
            PrefManager prefM = PrefManager.getInstance(this);
            String token = prefM.getAccessToken();
            String userID = prefM.getUserID();
            String url = GLOBAL_URL + "installation/" + uuid;
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            aq.ajax(url, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String toUserId, String type, AjaxCallback cb) {
        try {
            String uuid = Constants.getUDID();
            PrefManager prefM = PrefManager.getInstance(this);
            String token = prefM.getAccessToken();
            String userID = prefM.getUserID();
            String url = GLOBAL_URL + "notification/";
            AQuery aq = new AQuery(this);
            cb.header("Content-Type", "application/json");
            cb.header("AccessToken", token);
            JSONObject params = new JSONObject();
            params.putOpt("toUserId", toUserId);
            params.putOpt("fromUserId", userID);
            params.putOpt("type", type);
            aq.post(url, params, JSONObject.class, cb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
