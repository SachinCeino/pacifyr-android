package com.pacifyrapp.utilitylibrary.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.pacifyrapp.utilitylibrary.R;

import java.util.List;

/**
 * Created by ceino on 9/2/17.
 */

public class BaseUtils {

    private static BaseUtils instance = null;

    public static BaseUtils getInstance() {
        if(instance == null) {
            instance = new BaseUtils();
        }
        return instance;
    }

    public String getUpperCaseFirstLetter(String fname,String lname) {
        String name=getUpperCaseFirstLetter(defString(fname))+ " "+getUpperCaseFirstLetter(lname);
        return name;
    }



    public String getUpperCaseFirstLetter(String fname) {
        if(isValid(fname)) {
            StringBuilder rackingSystemSb = new StringBuilder(fname.toLowerCase());
            rackingSystemSb.setCharAt(0, Character.toUpperCase(rackingSystemSb.charAt(0)));
            fname = rackingSystemSb.toString();
        }
        return fname;
    }


    public static String getCostLabel(Context context) {

        PrefManager prefM = PrefManager.getInstance(context);
        String country = prefM.getCountry();
        String label = context.getString(R.string.per_hour);
        if (country.equals("India")) {
            label = context.getResources().getString(R.string.per_session);
        } else {
            label = context.getResources().getString(R.string.per_hour);
        }

        return label + " ";
    }

    public Boolean isValid(Object text)
    {
        return text != null;

    }

    public Boolean isValid(int count)
    {
        return count > 0;

    }

    public Boolean isValid(String text)
    {
        if(text!=null)
            return !text.trim().equalsIgnoreCase("");
        return  false;

    }

    public Boolean isValid(List list)
    {
        if(list!=null)
            return list.size() > 0;
        return  false;

    }

    public Boolean isValidNotNull(List list) {
        // if(list.size()>0)
        return list != null;

    }

    public String defString(EditText text, String def) {
        if(text != null) if(isValid(text.getText() + "")) return text.getText() + "";
        return def;

    }

    public String defString(EditText text) {
        if(text != null) if(isValid(text.getText() + "")) return text.getText() + "";
        return "";

    }

    public String defString(String text, String def) {
        if(text != null) return text;
        return def;

    }

    public String defString(String text) {
        if(text != null) return text;
        return "";

    }


    public void defSetText(TextView textv, String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(TextView textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    public void defSetText(EditText textv,String text, String def) {
        if(isValid(textv))
            textv.setText(defString(text,def));

    }
    public void defSetText(EditText textv,String text) {
        if(isValid(textv))
            textv.setText(defString(text));

    }

    public Boolean isValidSize(List list) {
        if (list != null) return list.size() > 0;
        return false;

    }


}
