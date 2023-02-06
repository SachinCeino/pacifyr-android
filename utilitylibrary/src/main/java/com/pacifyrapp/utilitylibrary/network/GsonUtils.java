package com.pacifyrapp.utilitylibrary.network;


import com.pacifyrapp.utilitylibrary.LogUtilsutility;
import com.pacifyrapp.utilitylibrary.model.Users;
import com.pacifyrapp.utilitylibrary.model.response.RespHangoutAdd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;



/**

 */
public class GsonUtils<T> {
    static GsonUtils gson;

    public static GsonUtils getInstance() {
        if(gson==null)
             gson = new GsonUtils();
        return gson;
    }


    public Users gsonToModelUsers(JSONObject json) {

        Users users = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            users = gson.fromJson(jsonString, Users.class);

        } catch (Exception e){
            LogUtilsutility.LOGD("exception", "NS ModelClassParentDetail " + e.getMessage());
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
        return users;
    }
    public RespHangoutAdd gsonToRespHangoutAdd(JSONObject json) {

        RespHangoutAdd users = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            users = gson.fromJson(jsonString, RespHangoutAdd.class);

        } catch (Exception e){
            LogUtilsutility.LOGD("exception", "GSonUTils RespHangoutAdd " + e.getMessage());
            //Crashlytics.logException(e);
            e.printStackTrace();
        }
        return users;
    }



}

