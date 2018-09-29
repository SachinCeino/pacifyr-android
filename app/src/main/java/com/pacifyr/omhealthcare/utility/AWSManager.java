package com.pacifyr.omhealthcare.utility;

/**
 * Created by jp on 23/5/16.
 */


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;

public class AWSManager {

     /*public static final String ARN = "arn:aws:sns:us-east-1:271048179034:app/GCM/PacifyrAndroidDev";
    public static final String AWS_PROJECT = "PacifyrAndroidDev";
    private static final String _ACCESS_KEY_ID = "AKIAJZRHYKM2SMPZQ5MQ";
    private static final String _SECRET_KEY = "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";*/

    public static final String ARN = "arn:aws:sns:us-east-1:106428918470:app/GCM/GetSmartLive";
    public static final String AWS_PROJECT = "GetSmartLive";
    /*public static final String ARN = "arn:aws:sns:us-east-1:271048179034:app/GCM/OMDev";
    public static final String AWS_PROJECT = "OMDev";*/
    private static final String _ACCESS_KEY_ID = "AKIAJWUXPHXZKRXITPHA";
    private static final String _SECRET_KEY = "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";
    private static AmazonSNSClient snsClient = null;


    /**
     * returns the instance of the amazon SNS Client for this app */
    public static AmazonSNSClient getSNSClient(){
        if(snsClient == null){
            snsClient = new AmazonSNSClient( new BasicAWSCredentials( _ACCESS_KEY_ID, _SECRET_KEY ) );
            snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        }
        return snsClient;
    }

}