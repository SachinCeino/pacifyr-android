package com.ceino.utilitylibrary.utils;

import android.widget.EditText;
import android.widget.TextView;

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




    public Boolean isValidNotNull(List list) {
        if(list != null)
            // if(list.size()>0)
            return true;
        return false;

    }

    public Boolean isValidSize(List list) {
        if(list != null) if(list.size() > 0) return true;
        return false;

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



}
