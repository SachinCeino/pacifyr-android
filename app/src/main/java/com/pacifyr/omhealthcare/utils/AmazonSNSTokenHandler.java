package com.pacifyr.omhealthcare.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.utils.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.pacifyr.omhealthcare.model.Installation;
import com.pacifyr.omhealthcare.network.NetworkService;
import com.pacifyr.omhealthcare.utility.AWSManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/* PSEUDO CODE
* retrieve the latest device token from the mobile operating system
if (the platform endpoint ARN is not stored)
  # this is a first-time registration
  call create platform endpoint
  store the returned platform endpoint ARN
endif

call get endpoint attributes on the platform endpoint ARN

if (while getting the attributes a not-found exception is thrown)
  # the platform endpoint was deleted
  call create platform endpoint with the latest device token
  store the returned platform endpoint ARN
else
  if (the device token in the endpoint does not match the latest one) or
      (get endpoint attributes shows the endpoint as disabled)
    call set endpoint attributes to set the latest device token and then enable the platform endpoint
  endif
endif
* */

public class AmazonSNSTokenHandler {

    public static String TAG = AmazonSNSTokenHandler.class.getSimpleName();
    private final Context context;
    private String token;
    private Installation installation;

    public AmazonSNSTokenHandler(Context context) {
        this.context = context;
    }

    public void register() {
        token = PrefManager.getInstance(context).getSharedString(PrefManager.FCM_TOKEN, null);
        if (token == null) {
            token = FirebaseInstanceId.getInstance().getToken();
        }
        if (token == null || token.isEmpty()) {
            ToastHandler.newInstance(context).showToast("Token empty cannot subscribe for notifications");
            Log.d(TAG, "Invalid Token exiting...");
            return;
        }
        AjaxCallback cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    if (json != null && json.has("results")) {
                        JSONArray results = json.getJSONArray("results");
                        if (results != null && results.length() > 0) {
                            installation = new Gson().fromJson(results.get(0).toString(), Installation.class);
                            if (installation != null) {
                                new GetEndPointAttributesTask(installation.getArn()).execute();
                            } else {
                                Log.d(TAG, "register installation not found ");
                            }
                        } else { // first time; call create endpoint; save arn
                            Log.d(TAG, "register response no results found ");
                            createEndPointTask(AWSManager.ARN, token, getProfileName());
                        }
                    } else {
                        Log.d(TAG, "register response no results found ");
                    }
                    Log.d(TAG, "register response status " + status.getCode());
                    Log.d(TAG, "register response " + json);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "register response " + e.getMessage());
                }
            }
        };
        new NetworkService().getInstallation(getUDID(), cb);
    }

    private void createEndPointTask(final String arn, final String gcmToken, final String userData) {
        try {
            LogUtils.LOGD("install", "createEndPointTask: ");
            new AsyncTask() {
                protected Object doInBackground(final Object... params) {

                    try {

                        CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest();

                        request.setCustomUserData(userData);

                        request.setToken(gcmToken);

                        request.setPlatformApplicationArn(arn);
                        CreatePlatformEndpointResult result = AWSManager.getSNSClient().createPlatformEndpoint(request);
                        if (isValid(result)) {
                            LogUtils.LOGD("install", "CreatePlatformEndpointResult success: " + result.getEndpointArn());
                            PrefManager.getInstance(context).putSharedString(PrefManager.AWS_ENDPOINT, result.getEndpointArn());
                            deleteInstallationWeb();
                            installationWeb();

                        }

                        // }
                    } catch (InvalidParameterException ipe) {
                        LogUtils.LOGD("install", "CreatePlatformEndpointResult Exception : " + ipe.getMessage());
                        // Invalid parameter: Token Reason: Endpoint arn:aws:sns:us-east-1:106428918470:endpoint/GCM/FluidGuy-Sample/5af0662e-31b8-3733-8aa7-c21f911b7360 already exists with the same Token, but different attributes.

                        /*String message = ipe.getErrorMessage();
                        System.out.println("Exception message: " + message);
                        LogUtils.LOGD("install", "CreatePlatformEndpointResult fail : " + message);
                        Pattern p = Pattern.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists " + "with the same Token.*");
                        Matcher m = p.matcher(message);
                        if(m.matches()) {
                            // the endpoint already exists for this token, but with
                            // additional custom data that
                            // CreateEndpoint doesn't want to overwrite. Just use the
                            // existing endpoint.
                            //endpointArn = m.group(1);
                            PrefManager.getInstance(context).putSharedString(PrefManager.AWS_ENDPOINT, m.group(1));
                            LogUtils.LOGD("install", "CreatePlatformEndpointResult fail :endpoint  " + m.group(1));
                            new SetEndPointTask(m.group(1), userData).execute();
                            installationWeb();
                        } else {
                            // rethrow exception, the input is actually bad
                            throw ipe;
                        }*/
                    } catch (Exception ex) {
                        LogUtils.LOGD("install", "CreatePlatformEndpointResult Exception : " + ex.getMessage());
                    }
                    return true;
                }
            }.execute(null, null, null);
        } catch (Exception ex) {
            LogUtils.LOGD("install", "CreatePlatformEndpointResult Exception end  : " + ex.getMessage());
        }
    }

    private void setEndpointAttributes(final String endpointArn, final String userData) {
        try {
            SetEndpointAttributesRequest setEndpointAttributesRequest = new SetEndpointAttributesRequest();
            setEndpointAttributesRequest.setEndpointArn(endpointArn);
            Map<String, String> attributes = new HashMap<>();
            attributes.put("CustomUserData", userData);
            attributes.put("Token", token);
            attributes.put("Enabled", "true");
            setEndpointAttributesRequest.setAttributes(attributes);
            AWSManager.getSNSClient().setEndpointAttributes(setEndpointAttributesRequest);

        } catch (InvalidParameterException ipe) {
            ipe.printStackTrace();
        }
    }

    public GetEndpointAttributesResult getEndpointAttributes(String endpointArn) {
        GetEndpointAttributesRequest getEndpointAttributesRequest = new GetEndpointAttributesRequest();
        getEndpointAttributesRequest.setEndpointArn(endpointArn);
        return AWSManager.getSNSClient().getEndpointAttributes(getEndpointAttributesRequest);
    }

    public void installationWeb() {
        LogUtils.LOGD("install", "installationWeb");

        try {
            final String udid = getUDID();
            LogUtils.LOGD("install", "installationWeb " + udid);
            AjaxCallback<JSONObject> cbc = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    LogUtils.LOGD("install", "installationWeb callback : " + json);
                    if (json == null) {
                        LogUtils.LOGD("install", "installationWeb fail : " + status.getMessage());
                        com.ceino.utilitylibrary.ToastHandler.newInstance(context).showToast("Install : Please Try again");

                    } else {
                        try {
                            saveUDID(udid);
                            Gson gson = new Gson();
                            String json2 = gson.toJson(json);
                            LogUtils.LOGD("install", "installationWeb suceess : " + json2);
                        } catch (Exception e) {
                            //Crashlytics.logException(e);
                            ToastHandler.newInstance(context).showToast("Please Try again");
                            LogUtils.LOGD("install", "installationWeb exception callback : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }


                }
            };

            AQuery aq = new AQuery(context);
            // cbc.header("support-app-id", "appid");
            //  cbc.header("support-app-key", "appkey");
            cbc.header("Content-Type", "application/json");
            cbc.header("AccessToken", getAccessToken());

            JSONObject input = new JSONObject();
            input.putOpt("userId", getUserID());
            input.putOpt("arn", getAWSEndPoint());
            input.putOpt("udid", udid);
            input.putOpt("devicetoken", getFcmToken());
            input.putOpt("deviceType", PrefManager.DEVICETYPE);
            input.putOpt("applicationname", AWSManager.AWS_PROJECT);
            Gson gson = new Gson();
            String json = gson.toJson(input);
            LogUtils.LOGD("install", "installationWeb : " + json);
            aq.post(NetworkService.GLOBAL_URL + "installation", input, JSONObject.class, cbc);
            LogUtils.LOGD("install", "installation url: " + NetworkService.GLOBAL_URL + "installation");

        } catch (Exception e) {
            //Crashlytics.logException(e);
            LogUtils.LOGD("install", "installationWeb Exception : " + e.getMessage());
        }
    }

    public void deleteInstallationWeb() {

        try {
            final String udid = getUDID();
            AjaxCallback<JSONObject> cbc = new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json == null) {

                    } else {

                    }


                }
            };

            AQuery aq = new AQuery(context);
            cbc.header("Content-Type", "application/json");
            cbc.header("AccessToken", getAccessToken());

            String url = NetworkService.GLOBAL_URL + "installation/" + udid + "";
            aq.delete(url, JSONObject.class, cbc);

        } catch (Exception e) {
            //Crashlytics.logException(e);
            LogUtils.LOGD("install", "installationWeb Exception : " + e.getMessage());
        }
    }

    public Boolean isValid(Object object) {
        return object != null;

    }

    public String getUDID() {
        String android_id = null;
        android_id = PrefManager.getInstance(context).getSharedString(PrefManager.UDID, null);
        if (android_id == null) {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return android_id;
    }

    public void saveUDID(String valude) {
        PrefManager.getInstance(context).putSharedString(PrefManager.UDID, valude);
    }

    public String getAccessToken() {
        return PrefManager.getInstance(context).getSharedString(PrefManager.ACCESSTOKEN, null);

    }

    public String getUserID() {
        return PrefManager.getInstance(context).getSharedString(PrefManager.USERID, null);
    }

    public String getAWSEndPoint() {
        return PrefManager.getInstance(context).getSharedString(PrefManager.AWS_ENDPOINT, null);
    }

    public String getFcmToken() {
        String token = PrefManager.getInstance(context).getSharedString(PrefManager.FCM_TOKEN, null);
        if (token == null || token.isEmpty()) {
            token = FirebaseInstanceId.getInstance().getToken();
        }
        return token;
    }

    public String getProfileName() {
        return getProfileFName() + " " + getProfileLName();

    }

    public String getProfileFName() {
        return PrefManager.getInstance(context).getSharedString(PrefManager.UserFirstName, "");

    }

    public String getProfileLName() {
        return PrefManager.getInstance(context).getSharedString(PrefManager.UserLastName, "");

    }

    public class SetEndPointTask extends AsyncTask {

        private final String arn;
        private final String data;

        public SetEndPointTask(String endPointArn, String userData) {
            this.arn = endPointArn;
            this.data = userData;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                setEndpointAttributes(arn, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetEndPointAttributesTask extends AsyncTask {

        private final String arn;

        public GetEndPointAttributesTask(String endPointArn) {
            this.arn = endPointArn;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                return getEndpointAttributes(arn);
            } catch (NotFoundException nf) {
                nf.printStackTrace();
                return nf;
            } catch (AuthorizationErrorException aee) {
                aee.printStackTrace();
                return aee;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o != null && o instanceof GetEndpointAttributesResult) {
                GetEndpointAttributesResult getEndpointAttributesResult = (GetEndpointAttributesResult) o;
                Log.d(TAG, "GetEndpointAttributesResult " + getEndpointAttributesResult.toString());
                if (!getEndpointAttributesResult.getAttributes().get("Token").equals(token)
                        || getEndpointAttributesResult.getAttributes().get("Enabled").equals("false")) {
                    new SetEndPointTask(installation.getArn(), getProfileName()).execute();
                }
            } else if (o instanceof NotFoundException || o instanceof AuthorizationErrorException) {
                createEndPointTask(AWSManager.ARN, token, getProfileName());
            }
        }
    }

    public void deleteEndpoint() {
        new DeleteEndPointTask().execute();
    }

    public class DeleteEndPointTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
                String endPoint = PrefManager.getInstance(context).getSharedString(PrefManager.AWS_ENDPOINT, null);
                if (endPoint != null) {
                    deleteEndpointRequest.setEndpointArn(endPoint);
                    AWSManager.getSNSClient().deleteEndpoint(deleteEndpointRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
