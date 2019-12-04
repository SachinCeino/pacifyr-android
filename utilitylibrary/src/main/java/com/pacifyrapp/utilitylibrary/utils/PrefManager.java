package com.pacifyrapp.utilitylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pacifyrapp.utilitylibrary.LogUtilsutility;
import com.pacifyrapp.utilitylibrary.event.BusProvider;
import com.pacifyrapp.utilitylibrary.model.MLoginRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrefManager {


    public static final String FCM_TOKEN = "fcm_pref_token";
    //   public static final String PREFERENCES = "Gcm_Config";
    //   public static final String PREFERENCES = "Login";
    public static final String AWS_ENDPOINT = "aws_endpoint";
    public static final String UDID = "udid";
    public static final String DEVICETYPE = "android";


    public static final String PROFILE_IMAGE_URL = "profile_image_url";




    public static final String LOGIN_PREFERENCES = "Login";
    public static final String USERID = "Userid";
    public static final String PACIFYR_ID ="PacifyrID";
    public static final String CUSTOMER_ROLE = "CustomerRole";
    public static final String CLASSID = "classid";
    public static final String CLASS_ACCESS = "classaccess";
    public static final String TYPE = "Type";
    public static final String WEBURL = "webUrl";
    public static final String COMPANY_ID = "companyID";
    public static final String IS_ONLINE = "isOnline";
    public static final String IS_CHAT_SUBSCRIPTION = "isChatEnable";
    public static final String ACCESSTOKEN = "AccessToken";
    public static final String MLOGINROOT = "MLOGINROOT";
    public static final String JSON_KEY = "JSON_KEY";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String UserFirstName = "UserFirstName";
    public static final String UserLastName = "UserLastName";
    public static final String Country = "country";
    public static final String COUNTRY_CODE = "countryCode";
  //  public static final String UserImage = "UserImage";
    public static final String QB_ID="quickboxID";
    public static final String EMAIL = "EMAIL";
    public static final String FB_NAME="fbName";
    public static final String BASE_TOKEN="baseToken";
    public static final String QB_TOKEN="QBToken";
    public static final String CARD_EXIST="card_exist";//mybadgeValue
    public static final String BADGE_VALUE="badgeValue";
    public static final String PHONE_NUMBER="phoneNumber";

    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private static PrefManager instance = null;
    PrefManager()
    {

    }

    // http://graph.facebook.com/1156564127777842/picture?type=larg
    public static PrefManager getInstance(Context context) {
        if(instance == null) {
            instance = new PrefManager(context);
        }
        return instance;
    }
    public PrefManager(Context context) {
        super();
        this.context = context;
        sharedpreferences=context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
    }


    public String getUserID()
    {
        return getSharedString(USERID,"");

    }

    public String getPacifyrID()
    {
        return getSharedString(PACIFYR_ID,"");

    }

    public String getCompanyId()
    {
        return getSharedString(COMPANY_ID,"");

    }

    public String getCustomerRole()
    {
        return getSharedString(CUSTOMER_ROLE,"");

    }
    public String getIsChatSubscriptionRequired()
    {
        return getSharedString(IS_CHAT_SUBSCRIPTION,"");
    }
    public String getIsOnline()
    {
        return getSharedString(IS_ONLINE,"");
    }
    public String getJsonKey()
    {
        return getSharedString(JSON_KEY,"");
    }
    public String getUserFirstName()
    {
        return getSharedString(UserFirstName,"");
    }
    public String getUserLastName()
    {
        return getSharedString(UserLastName,"");
    }
    public String geteMail()
    {
        return getSharedString(EMAIL,"");
    }
    public String getfbName()
    {
        return getSharedString(FB_NAME,"");
    }
    public String getAccessToken()
    {
        return getSharedString(ACCESSTOKEN,"");

    }
    public String getUserName()
    {
        String fName = getSharedString(UserFirstName, "");
        String lName = getSharedString(UserLastName, "");
        return fName+" "+lName;

    }
    public String getClassID()
    {
        return getSharedString(CLASSID,"");

    }
    public String getEmail()
    {
        return  getSharedString("EMAIL", null);

    }

    public String getSessionId()
    {
        return  getSharedString("SESSION_ID", null);

    }

    public Integer getBadgeValue()
    {
        return getSharedInteger(BADGE_VALUE,0);

    }

    public void saveBadgeValue(Integer  badgeValue)
    {
        putSharedInteger(BADGE_VALUE,badgeValue);

    }


    public void saveSessionId(String  id)
    {
        putSharedString(SESSION_ID,id);

    }

    public String getProfileImageURL(String image)
    {
        return  getSharedString(PROFILE_IMAGE_URL,null);
    }
    public void saveUserID(String  classid)
    {
        putSharedString(USERID,classid);

    }

    public void savePacifyrID(String  pacifyrId)
    {
        putSharedString(PACIFYR_ID,pacifyrId);

    }

    public void saveCompanyID(String  id)
    {
        putSharedString(COMPANY_ID,id);

    }

    public void saveCustomerRole(String  role)
    {
        putSharedString(CUSTOMER_ROLE,role);

    }

    public boolean isCardExist() {
        return getSharedBoolean(PrefManager.CARD_EXIST, false);
    }
    public void saveCardExist(boolean isExist ) {
        putSharedBoolean(PrefManager.CARD_EXIST, isExist);
    }


    public boolean isPacifyer()
    {
        boolean isPacifyer=false;
        try{

            try {

                String label=getUserType();

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

                String label=getUserType();
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
    public String getUserType()
    {
        String label="error";
        try{
            String jsonString =getJsonKey();
            try {

                JSONObject obj = new JSONObject(jsonString);
                JSONObject user=obj.getJSONObject("user");
                JSONObject role=user.getJSONObject("roleModel");
                label=role.getString("mainLabel");


            } catch (Exception t) {
                label="error";
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            label="error";
        }
        return label;
    }


    public void saveIsOnline(String isOnline)
    {
        putSharedString(IS_ONLINE,isOnline);
    }

    public void saveIsChatSubscriptionRequired(String isChat)
    {
        putSharedString(IS_CHAT_SUBSCRIPTION,isChat);
    }
    public void saveLOGINRoot(MLoginRoot root)
    {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(root);

            putSharedString(MLOGINROOT, json);
        }catch (Exception e)
        {

        }
    }
    public MLoginRoot getLOGINRoot()
    {
        MLoginRoot loginRoot=null;
        try {


            String jsonString= getSharedString(MLOGINROOT,"");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            loginRoot = gson.fromJson(jsonString, MLoginRoot.class);
        }catch (Exception e)
        {

        }
        return loginRoot;
    }
    public void saveAccessToken(String accesstoken)
    {
        putSharedString(ACCESSTOKEN,accesstoken);
        Log.w("QQQQ",accesstoken);
        Log.w("QQQQ",accesstoken);

    }

    public String getQBID()
    {
        return getSharedString(QB_ID,"");
    }

    public void saveQBID(String qbid)
    {
        putSharedString(QB_ID,qbid);
    }
/*    public void saveQBID(int qbid)
    {
        putSharedLong(QB_ID,qbid);
    }
    public Long getQBID()
    {
        return getSharedLong(QB_ID);
    }*/

    public String getQBToken()
    {
        return getSharedString(QB_TOKEN,"");
    }

    public void saveQBToken(String qbToken)
    {
        putSharedString(QB_TOKEN,qbToken);
    }



    public String getBaseToken()
    {
        return getSharedString(BASE_TOKEN,"");
    }

    public void saveBaseToken(String baseToken)
    {
        putSharedString(BASE_TOKEN,baseToken);
    }


    public void saveCountry(String country) {
        //this.country = country;
        putSharedString(Country,country);
    }

    public void saveCountryCode(String country) {
        //this.country = country;
        putSharedString(COUNTRY_CODE,country);
    }
    public void saveJsonKey(String jsonString)
    {
        putSharedString(JSON_KEY,jsonString);

    }

    public void saveEmail(String emailID)
    {
        putSharedString(EMAIL,emailID);

    }

    public void saveProfileImageURL(String image)
    {
        putSharedString(PROFILE_IMAGE_URL,image);
    }
    public void saveUserFirstName(String accesstoken)
    {
        putSharedString(UserFirstName,accesstoken);
    }
    public void saveUserUserLastName(String accesstoken)
    {
        putSharedString(UserLastName,accesstoken);
    }
    public void saveMail(String mail)
    {
        putSharedString(EMAIL,mail);
    }
    public void savefbName(String fbName)
    {
        putSharedString(FB_NAME,fbName);
    }



    public void clear()
    {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void saveClassID(String classid)
    {
        putSharedString(CLASSID,classid);

    }
    public boolean getClassAccess()
    {
        return getSharedBoolean(CLASS_ACCESS,false);

    }
    public void saveClassAccess(boolean classid)
    {
        putSharedBoolean(CLASS_ACCESS,classid);

    }
    public String getTYPE()
    {
        return getSharedString(TYPE,"");

    }
    public String getRoleType()
    {
        return getSharedString(TYPE,"");

    }


    public String getCountry() {
        //this.country = country;
        return getSharedString(Country,"");
    }

    public String getPhoneNumber() {
        //this.country = country;
        return getSharedString(PHONE_NUMBER,"");
    }

    public void savePhoneNumber(String  id)
    {
        putSharedString(PHONE_NUMBER,id);

    }


    public String getCountryCode() {
        //this.country = country;
        return getSharedString(COUNTRY_CODE,"");
    }

    public String getWEBURL()
    {
        return getSharedString(WEBURL,"");

    }
    public void saveWEBURL(String  classid)
    {
        putSharedString(WEBURL,classid);

    }



    public String getRoleTYpe()
    {
        return getSharedString("Type","");

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

    private String getDayString(String createdAt, SimpleDateFormat sdf) {

        String date="";
        try{
            date= sdf.format(sdf.parse(createdAt));
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception", "getDayString exception" + e.getMessage());
        }
        return date;
    }
    private String getDayString(String pass_date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date="";
        try{
            date= sdf.format(sdf.parse(pass_date));
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception", "getDayString exception" + e.getMessage());
        }
        return date;
    }

    private Date getDate(int i,  Date now_date) {
        Date newDate =now_date;
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now_date);
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            newDate = calendar.getTime();
        }catch (Exception e)
        {
            LogUtilsutility.LOGD("exception", "getDate exception" + e.getMessage());
        }
        return newDate;
    }





    public String getSharedString(String KEY, String defValue) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        return sharedpreferences.getString(KEY, defValue);
    }

    public Integer getSharedInteger(String KEY, Integer defValue) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        return sharedpreferences.getInt(KEY, defValue);
    }

    public void putSharedString(String KEY, String value) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        editor.putString(KEY, value).commit();

    }

    public void putSharedInteger(String KEY, Integer value) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        editor.putInt(KEY, value).commit();

    }


    public boolean getSharedBoolean(String KEY, boolean defValue) {
        if(!isValid(sharedpreferences))
            sharedpreferences = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        return sharedpreferences.getBoolean(KEY, defValue);
    }

    public void putSharedBoolean(String KEY, boolean value) {
        if(!isValid(sharedpreferences))
            sharedpreferences = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        editor.putBoolean(KEY, value).commit();
    }

    private Long getSharedLong(String KEY) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        return sharedpreferences.getLong(KEY,0L);
    }

    private void putSharedLong(String KEY, long value) {
        if(!isValid(sharedpreferences))
            sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        if(!isValid(editor))
            editor = sharedpreferences.edit();
        editor.putLong(KEY, value).commit();
    }


    public void clearSharedAll() {
        if(!isValid(editor))
        {
            if(!isValid(sharedpreferences))
                sharedpreferences =context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
        }
        editor.clear().commit();
    }





    public String getString(String KEY, String defValue) {
        return sharedpreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {

        editor.putString(KEY, value).commit();
    }


    public Long getLong(String KEY) {
        return sharedpreferences.getLong(KEY,0L);
    }

    public void putLong(String KEY, long value) {

        editor.putLong(KEY, value).commit();
    }

    public String getUpperCaseFirstLetter(String fname) {
        if(isValid(fname)) {
            StringBuilder rackingSystemSb = new StringBuilder(fname.toLowerCase());
            rackingSystemSb.setCharAt(0, Character.toUpperCase(rackingSystemSb.charAt(0)));
            fname = rackingSystemSb.toString();
        }
        return fname;
    }




    public Boolean isValid(Object text)
    {
        if(text!=null)

            return  true;
        return  false;

    }

    public Boolean isValid(int count)
    {
        if(count>0)
            return  true;
        return  false;

    }
    public Boolean isValid(String text)
    {
        if(text!=null)
            if(!text.trim().equalsIgnoreCase(""))
                return  true;
        return  false;

    }
    public Boolean isValid(List list)
    {
        if(list!=null)
            if(list.size()>0)
                return  true;
        return  false;

    }


}
