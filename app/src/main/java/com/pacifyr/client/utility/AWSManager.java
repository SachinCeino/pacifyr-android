package com.pacifyr.client.utility;

import android.content.Context;

import com.pacifyr.client.R;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;


/**
 * Created by jp on 23/5/16.
 */


public class AWSManager {

    public static final String ARN = "arn:aws:sns:us-east-1:271048179034:app/GCM/Android_OMHealth_Development";
    public static final String AWS_PROJECT = "OMDev";

    public AWSManager() {

    }

    /**
     * pacifyr,om are using different key for installation AWS server
     * according to the right package Name changing the key from calling this function
     *
     * @param context**/
    public static String getProjectName(Context context) {
      String  PACKAGE_NAME = context.getPackageName();
        if (PACKAGE_NAME.equals("com.pacifyr.pacifyrapp"))
        {
            if (UtilityNetworkService.isProd) {
                //Prod
                return "PacifyrUserAndroidDev";
            } else {
                //Dev
                return "PacifyrAndroidDev2";
            }
        }
        else if (PACKAGE_NAME.equals("com.ceino.pacifyr.omhealthcare")){
            return  "OMDev";
        } else if (PACKAGE_NAME.equals("com.pacifyr.leadersup")) {
            if (UtilityNetworkService.isProd) {
                //Prod
                return "leadersupprod";
            } else {
                //Dev
                return "leadersupdev";
            }
        } else if (PACKAGE_NAME.equals(UtilityNetworkService.PACKAGE_PAM)) {
            if (UtilityNetworkService.isProd) {
                //Prod
                return "PAMClientAndroid";
            } else {
                //Dev
                return "PAMClientTestAndroid";
            }
        }
        else if (PACKAGE_NAME.equals(UtilityNetworkService.PACKAGE_MEDCARE)) {
            if (UtilityNetworkService.isProd) {
                //Prod
                return context.getString(R.string.APP_SOURCE);
            } else {
                //Dev
                return context.getString(R.string.APP_SOURCE);
            }
        }
        else {
            return "";
        }

    }

}