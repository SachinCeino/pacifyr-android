package com.pacifyr.client.model;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pacifyr.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ceino on 11/1/18.
 */

public class Country {

    private String country;
    private String code;
    private Integer imageId;

    public Country(String text, Integer imageId){
        this.country=text;
        this.imageId=imageId;
    }

    public Country(String text, String country, Integer imageId){
        this.country=country;
        this.code=text;
        this.imageId=imageId;

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
    private static ArrayList<Country> list;
    public static ArrayList<Country> getAvailableCountryList(){
       if(list==null) {
           list=new ArrayList<>();
           list.add(new Country("+1", "US", R.drawable.flag_usa));

           list.add(new Country("+1", "CA", R.drawable.canada2));
           list.add(new Country("+91", "IN", R.drawable.ic_india));
           list.add(new Country("+971", "AE", R.drawable.flag_uae2));

       }
        return list;
    }

    public static String getCurrentCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        Log.d("Country",countryCodeValue+" ");
        return countryCodeValue;
    }
    public static int getIndex(Context context, List<Country> l){
        String Code=getCurrentCountryCode(context);
        for(int i=0;i<l.size();i++){
            Country d=l.get(i);
            if(d.country.equalsIgnoreCase(Code))return i;
        }
        return 0;
    }


}
