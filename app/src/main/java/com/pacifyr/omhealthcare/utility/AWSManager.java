package com.pacifyr.omhealthcare.utility;

import com.ceino.utilitylibrary.network.UtilityNetworkService;
import com.pacifyr.omhealthcare.BuildConfig;

import static com.pacifyr.omhealthcare.activity.SplachScreen.PACKAGE_NAME;

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
     * **/
    public static String getProjectName() {
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
        }
        else {
            return "";
        }

    }

}