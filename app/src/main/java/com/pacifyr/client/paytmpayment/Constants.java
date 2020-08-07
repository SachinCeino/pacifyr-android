package com.pacifyr.client.paytmpayment;

import static com.pacifyrapp.utilitylibrary.network.UtilityNetworkService.isProd;

/**
 * Created by Belal on 1/10/2018.
 */

public class Constants {

    //TEST CREDENTIALS
    public static final String M_ID = "Pacify50992585761705"; //Paytm Merchand Id we got it in paytm credentials
    public static final String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential

    public static final String WEBSITE = "WEBSTAGING";

    public static final String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";

    //  PROD CREDENTIAL
    public static final String PROD_M_ID = "emCqhm26765949296735"; //Paytm Merchand Id we got it in paytm credentials
    public static final String PROD_CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String PROD_INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential

    public static final String PROD_WEBSITE = "DEFAULT";

    public static final String TEST_OR_PROD="PROD";

    public static final String PROD_CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";


    public static String getTestOrProd() {
        if (isProd)
            return "PROD";
        else
            return "TEST";
    }


    public static String getM_ID() {
        if (isProd)
            return PROD_M_ID;
        else
            return M_ID;
    }

    public static String getChannel_ID() {
        if (isProd)
            return PROD_CHANNEL_ID;
        else
            return CHANNEL_ID;
    }

    public static String getIndustryTypeId() {
        if (isProd)
            return PROD_INDUSTRY_TYPE_ID;
        else
            return INDUSTRY_TYPE_ID;
    }


    public static String getWebSite() {
        if (isProd)
            return PROD_WEBSITE;
        else
            return WEBSITE;
    }

    public static String getCallBackUrl() {
        if (isProd)
            return PROD_CALLBACK_URL;
        else
            return CALLBACK_URL;
    }



}
