package com.pacifyr.client.network;

/**
 * Created by ceino on 27/6/17.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pacifyr.client.fragment.schedule.AppointmentModel;
import com.pacifyr.client.fragment.schedule.ScheduleData;
import com.pacifyr.client.fragment.schedule.Slot;
import com.pacifyr.client.model.AppoinmentNotification;
import com.pacifyr.client.model.InterviewModel;
import com.pacifyr.client.model.MCustomerList;
import com.pacifyr.client.model.ModelEvent;
import com.pacifyr.client.model.ModelOffer;
import com.pacifyr.client.model.ModelPamTemplates;
import com.pacifyr.client.model.ModelResources;
import com.pacifyr.client.model.ModelSpecialityByCategory;
import com.pacifyr.client.model.ModelSpecialityByEmotion;
import com.pacifyr.client.model.PrescriptionList;
import com.pacifyr.client.model.QRSuccess;
import com.pacifyr.client.model.SchedulePayment;
import com.pacifyr.client.model.UserDetailsModel;
import com.pacifyr.client.model.UserProfile;
import com.pacifyr.client.paytmpayment.TransactionResponce;
import com.pacifyr.client.utils.LogUtils;
import com.pacifyrapp.utilitylibrary.model.pacifyr.LoginModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MAdminDashboardByWeek;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MAdminDashboardMonth;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MAdminDashboardYear;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MAllSessionsWithDateRange;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MConnectSessionRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MEarningsRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MGetAllPacifyrSessions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MGetSavedCards;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MGetSessionDetails;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MGetSessionRating;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListAllEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListEmployees;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListNegativeEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MListPositiveEmotions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MPastSessions;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRatingDetail;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MRatingStat;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MReviews;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionDetailRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MSessionRoot;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.Mbonus;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelCustomerDashboard;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelCustomerDashboard2;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelFacebook;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPacifyrDashboard;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelPhoneLogin;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSearchPacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSessonsOfCustomer;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelStatistics;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespPostEditProfile;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSearchPacifyrWOption;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespSetSessionAsConnected;
import com.pacifyrapp.utilitylibrary.model.pacifyr.RespStartSessionWithPacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PRegistrationModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PSearchPacifyrWOptionRequest;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PStartSessionWithPacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostEditProfileRequest;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRateCustomerSession;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRatePacifyr;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRatePacifyrSession;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostRegisterModel;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostResponse;
import com.pacifyrapp.utilitylibrary.model.pacifyr.post.PostStopSessionWithPacifyr;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils<T> {

    static GsonUtils gson;

    public static GsonUtils getInstance() {
        if(gson==null)
            gson = new GsonUtils();
        return gson;
    }

    public PostRegisterModel gsonToPostRegisterModel(JSONObject json) {

        PostRegisterModel posts = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            posts = gson.fromJson(jsonString, PostRegisterModel.class);

        } catch (Exception e){
            e.printStackTrace();
        }
        return posts;
    }

    public LoginModel gsonToLoginModel(JSONObject json) {

        LoginModel loginModel = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            loginModel = gson.fromJson(jsonString, LoginModel.class);

        } catch (Exception e){
            e.printStackTrace();
        }
        return loginModel;
    }

    public PRegistrationModel gsonToRegistrationModel(JSONObject json) {

        PRegistrationModel users = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            users = gson.fromJson(jsonString, PRegistrationModel.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public ModelSpecialityByCategory gsonToModelSpecialityByCategory(JSONObject json) {

        ModelSpecialityByCategory model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelSpecialityByCategory.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelPhoneLogin gsonToModelPhoneLogin(JSONObject json) {

        ModelPhoneLogin users = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            users = gson.fromJson(jsonString, ModelPhoneLogin.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public ModelSpecialist gsonToModelSpecialist(JSONObject json) {

        ModelSpecialist users = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            users = gson.fromJson(jsonString, ModelSpecialist.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
    public MSessionDetailRoot gsonToMSessionDetailRoot(JSONObject json) {

        MSessionDetailRoot search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            search = gson.fromJson(jsonString, MSessionDetailRoot.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }
    public ModelSessonsOfCustomer gsonToModelSessonsOfCustomer(JSONObject json) {

        ModelSessonsOfCustomer model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelSessonsOfCustomer.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }
    public ModelStatistics gsonToModelStatistics(JSONObject json) {

        ModelStatistics model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelStatistics.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MRatingStat gsonToMRatingStat(JSONObject json) {

        MRatingStat model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MRatingStat.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }
    public MConnectSessionRoot gsonToMConnectSessionRoot(JSONObject json) {

        MConnectSessionRoot search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            search = gson.fromJson(jsonString, MConnectSessionRoot.class);
        } catch (Exception e){
            LogUtils.LOGD("session","gsonToMConnectSessionRoot exce "+e.getMessage());
            e.printStackTrace();
        }
        return search;
    }
    public ModelSearchPacifyr gsonToModelSearchPacifyr(JSONObject json) {

        ModelSearchPacifyr search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            search = gson.fromJson(jsonString, ModelSearchPacifyr.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }


    public RespSearchPacifyrWOption gsonToRespSearchPacifyrWOption(JSONObject json) {

        RespSearchPacifyrWOption search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            search = gson.fromJson(jsonString, RespSearchPacifyrWOption.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }
    public MSessionRoot gsonToMSessionRoot(JSONObject json) {

        MSessionRoot search = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            search = gson.fromJson(jsonString, MSessionRoot.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return search;
    }
    public ModelSessonsOfCustomer gsonToModelSessonsOfUser(JSONObject json) {

        ModelSessonsOfCustomer model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelSessonsOfCustomer.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }
    public ModelCustomerDashboard gsonToModelCustomerDashboard(JSONObject json) {

        ModelCustomerDashboard model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelCustomerDashboard.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelCustomerDashboard2 gsonToModelCustomerDashboard2(JSONObject json) {

        ModelCustomerDashboard2 model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelCustomerDashboard2.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelUserProfile gsonToModelUserProfile(JSONObject json) {

        ModelUserProfile model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelUserProfile.class);
        }
        catch (Exception e){
            e.printStackTrace();
            String st=e.getMessage();
        }
        return model;
    }

    public UserProfile convertToUserProfile(JSONObject json) {

        UserProfile model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, UserProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
            String st = e.getMessage();
        }
        return model;
    }


    public UserDetailsModel gsonToUserDetailsModel(JSONObject json) {

        UserDetailsModel model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, UserDetailsModel.class);
        }
        catch (Exception e){
            e.printStackTrace();
            String st=e.getMessage();
        }
        return model;
    }

    public MUser gsonToMUser(JSONObject json) {

        MUser model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MUser.class);
        }
        catch (Exception e){
            e.printStackTrace();
            String st=e.getMessage();
        }
        return model;
    }

    public MAdminDashboardByWeek gsonToMAdminDashboardByWeek(JSONObject json) {

        MAdminDashboardByWeek model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MAdminDashboardByWeek.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MAdminDashboardMonth gsonToMAdminDashboardMonth(JSONObject json) {

        MAdminDashboardMonth model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MAdminDashboardMonth.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MAdminDashboardYear gsonToMAdminDashboardYear(JSONObject json) {

        MAdminDashboardYear model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MAdminDashboardYear.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MAllSessionsWithDateRange gsonToMAllSessionsWithDateRange(JSONObject json) {

        MAllSessionsWithDateRange model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MAllSessionsWithDateRange.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public MEarningsRoot gsonToMEarningsAPI(JSONObject json) {

        MEarningsRoot model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MEarningsRoot.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MGetAllPacifyrSessions gsonToMGetAllPacifyrSessions(JSONObject json) {

        MGetAllPacifyrSessions model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MGetAllPacifyrSessions.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public MGetSavedCards gsonToMGetSavedCards(JSONObject json) {

        MGetSavedCards model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MGetSavedCards.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MGetSessionDetails gsonMGetSessionDetails(JSONObject json) {

        MGetSessionDetails model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MGetSessionDetails.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public SchedulePayment gsonToSchedulePayment(JSONObject json) {

        SchedulePayment model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, SchedulePayment.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MGetSessionRating gsonToMGetSessionRating(JSONObject json) {

        MGetSessionRating model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MGetSessionRating.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MListAllEmotions gsonToMListAllEmotionsI(JSONObject json) {

        MListAllEmotions model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MListAllEmotions.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MListEmployees gsonToMListEmployees(JSONObject json) {

        MListEmployees model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MListEmployees.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MListNegativeEmotions gsonToMListNegativeEmotions(JSONObject json) {

        MListNegativeEmotions model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MListNegativeEmotions.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelSpecialityByEmotion gsonToModelSpeciltyByEmotion(JSONObject json) {

        ModelSpecialityByEmotion model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelSpecialityByEmotion.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelSpecialityByEmotion gsonToModelSpeciltyByEmotion(JSONArray json) {

        ModelSpecialityByEmotion model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelSpecialityByEmotion.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MListPositiveEmotions gsonToMListPositiveEmotions(JSONObject json) {

        MListPositiveEmotions model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MListPositiveEmotions.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelPacifyrDashboard gsonToModelPacifyrDashboard(JSONObject json) {

        ModelPacifyrDashboard model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelPacifyrDashboard.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostEditProfileRequest gsonToPostEditProfileRequestData(JSONObject json) {

        PostEditProfileRequest model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostEditProfileRequest.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public RespPostEditProfile gsonPostEditProfileResponse(JSONObject json) {

        RespPostEditProfile model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, RespPostEditProfile.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostRateCustomerSession gsonToPostRateCustomerSession(JSONObject json) {

        PostRateCustomerSession model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostRateCustomerSession.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostRatePacifyr gsonToPostRatePacifyr(JSONObject json) {

        PostRatePacifyr model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostRatePacifyr.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostRatePacifyrSession gsonToPostRatePacifyrSession(JSONObject json) {

        PostRatePacifyrSession model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostRatePacifyrSession.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostResponse gsonToPostResponse(JSONObject json) {

        PostResponse model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostResponse.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public RespSetSessionAsConnected gsonToPostSetSessionAsConnectedResponse(JSONObject json) {

        RespSetSessionAsConnected model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, RespSetSessionAsConnected.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PostStopSessionWithPacifyr gsonToPostStopSessionWithPacifyr(JSONObject json) {

        PostStopSessionWithPacifyr model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PostStopSessionWithPacifyr.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PRegistrationModel gsonToPRegistrationModel(JSONObject json) {

        PRegistrationModel model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PRegistrationModel.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public PSearchPacifyrWOptionRequest gsonToPSearchPacifyrWOptionRequest(JSONObject json) {

        PSearchPacifyrWOptionRequest model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PSearchPacifyrWOptionRequest.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public RespSearchPacifyrWOption gsonToPSearchPacifyrWOptionResponse(JSONObject json) {

        RespSearchPacifyrWOption model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, RespSearchPacifyrWOption.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public PStartSessionWithPacifyr gsonToPStartSessionWithPacifyrRequest(JSONObject json) {

        PStartSessionWithPacifyr model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, PStartSessionWithPacifyr.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }
    public MCustomerList gsonToMCustomerList(JSONObject json) {

        MCustomerList model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MCustomerList.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }
    public RespStartSessionWithPacifyr gsonToPStartSessionWithPacifyrResponse(JSONObject json) {

        RespStartSessionWithPacifyr model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, RespStartSessionWithPacifyr.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public MRatingDetail gsonToMRatingDetail(JSONObject json) {

        MRatingDetail model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MRatingDetail.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MPastSessions gsonToMPastSessions(JSONObject json) {

        MPastSessions model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MPastSessions.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public MReviews gsonToMReviews(JSONObject json) {

        MReviews model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, MReviews.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public ModelFacebook gsonToModelFacebook(JSONObject json) {

        ModelFacebook model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelFacebook.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public Mbonus gsonToMbonus(JSONObject json) {

        Mbonus model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, Mbonus.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public ModelResources gsonToModelResources(JSONObject json) {

        ModelResources model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, ModelResources.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public List<ScheduleData> gsonToModelScheduleData(JSONArray jsonArray) {

        List<ScheduleData> model = null;
        try {
            Type listType = new TypeToken<List<ScheduleData>>(){}.getType();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            model= gson.fromJson(jsonArray.toString(), listType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }

    public List<Slot> gsonToModelSlotesModel(JSONArray jsonArray) {

        List<Slot> model = null;
        try {
            Type listType = new TypeToken<List<Slot>>(){}.getType();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            model= gson.fromJson(jsonArray.toString(), listType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    public TransactionResponce gsonToTransactionResponce(JSONObject json) {

        TransactionResponce model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString =  json.toString();
            model = gson.fromJson(jsonString, TransactionResponce.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;
    }


    //event
    public ModelEvent gsonToModelEvent(JSONObject json) {

        ModelEvent model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, ModelEvent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    //offer
    public ModelOffer gsonToModelOffer(JSONObject json) {

        ModelOffer model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, ModelOffer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    //InterviewList
    public InterviewModel gsonToInterViewModel(JSONObject json) {

        InterviewModel model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, InterviewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    //QRCode suucees then call API result

    public QRSuccess gsonToModelQRCodeResult(JSONObject json) {

        QRSuccess data = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            data = gson.fromJson(jsonString, QRSuccess.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public AppointmentModel gsonToAppointmentModel(JSONObject json) {

        AppointmentModel model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, AppointmentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    public PrescriptionList gsonToPrescriptionList(JSONObject json) {

        PrescriptionList model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, PrescriptionList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
    public AppoinmentNotification gsonToAppoinmentNotification(JSONObject json) {

        AppoinmentNotification model = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = json.toString();
            model = gson.fromJson(jsonString, AppoinmentNotification.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public List<ModelPamTemplates> gsonToModelPamTemplates(JSONArray jsonArray) {

        List<ModelPamTemplates> model = null;
        try {
            Type listType = new TypeToken<List<ModelPamTemplates>>(){}.getType();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            model= gson.fromJson(jsonArray.toString(), listType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model;

    }



}

