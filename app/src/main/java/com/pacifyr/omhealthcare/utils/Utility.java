package com.pacifyr.omhealthcare.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ceino.utilitylibrary.utils.PrefManager;
import com.pacifyr.omhealthcare.R;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by User on 01-06-2016.
 */
public class Utility {

    private static final int MY_PERMISSIONS_REQUEST_ESSENTIAL = 130;
    public static int colourcode=2;


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 125;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 126;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 127;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 128;
    public static final int MY_PERMISSIONS_REQUEST_READ_AUDIO = 129;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, String type) {

        //getPriceSign();
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            if (type.equals("gallery")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else if (type.equals("camera")) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return false;
                } else {
                    return true;
                }
            } else if (type.equals("ic_call_round")) {


                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
                    }
                    return false;
                } else {
                    return true;
                }
            } else if (type.equals("write")) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }

            }else if(type.equals("contact")){

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                    return false;
                } else {
                    return true;
                }


            }else if(type.equals("audio")){

                //LogUtils.LOGD("jithish","audiochecks......");

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        //LogUtils.LOGD("jithish","audiochecks......1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }
                    return false;
                } else {
                    return true;
                }


            } else if (type.equals("essential")) {

                //LogUtils.LOGD("jithish","audiochecks......");

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO)
                            || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_dialog, null);
                        alertBuilder.setView(view);
                        Button positive = view.findViewById(R.id.button_dialog_positive);
                        TextView title = view.findViewById(R.id.text_dialog_title);
                        TextView message = view.findViewById(R.id.text_dialog_message);
                        title.setText("Permission Required");
                        message.setText("Audio and video recording permissions are required for making calls");
                        alertBuilder.setCancelable(true);
                        final AlertDialog alert = alertBuilder.create();
                        alert.show();
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_ESSENTIAL);
                                if (alert != null)
                                    alert.dismiss();
                            }
                        });

                    } else {
                        //LogUtils.LOGD("jithish","audiochecks......1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_ESSENTIAL);
                    }
                    return false;
                } else {
                    return true;
                }


            }else if(type.equals("readphone")) {

                //LogUtils.LOGD("jithish","audiochecks......");

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_AUDIO);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        //LogUtils.LOGD("jithish","audiochecks..jjjjj....1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_AUDIO);
                    }
                    return false;
                } else {
                    return true;
                }

            }else {
                return true;
            }

        }
        return true;
    }

    public static String getPriceSign(Context context) {

        PrefManager prefM = PrefManager.getInstance(context);
        String country = prefM.getCountry();
        String sign ="$";
        if(country.equals("India")){
            sign=context.getResources().getString(R.string.rupee);
        }else {
            sign=context.getResources().getString(R.string.dollar);
        }

        return sign+" ";
    }


    public static Boolean isValid(String text) {
        if (text != null)
            if (!text.trim().equalsIgnoreCase(""))
                return true;
        return false;

    }

    public static Boolean isValid(String text,int size) {
        if (isValid(text))
            if (text.length()>=size)
                return true;
        return false;

    }

    public static Boolean isValid(List list) {
        if (list != null)
            if (list.size() > 0)
                return true;
        return false;

    }

    public static Boolean isValid(List list,int pos) {
        if (isValid(list))
            if (list.size() >= pos)
                return true;
        return false;

    }

    public static Boolean isListSize(List list,int size) {
        if (isValid(list))
            if (list.size() == size)
                return true;
        return false;

    }

    public static Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            if (list.size() >= pos)
                return true;
        return false;
    }

    public static Boolean isValid(String[] list,int size) {

        if (isValid(list))
            if (list.length >= size)
                return true;
        return false;

    }

    public static Boolean isValidNotNull(List list) {
        if (list != null)
            // if(list.size()>0)
            return true;
        return false;

    }

    public static Boolean isValid(Object object) {
        if (object != null)
            return true;
        return false;

    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.GERMANY);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.GERMANY);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    public static String getDate(String input, boolean timeOnly) {
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat outFormat = null;
        if (timeOnly) {
            outFormat = new SimpleDateFormat("hh:mm a");
        } else {
            outFormat = new SimpleDateFormat("MMM dd,yyyy");
        }
        outFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = format.parse(input);
            System.out.println("Date ->" + date);
            return outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateAndTime(String input) {
        String outPutString = "";
        //example Aug 7,10 30:am
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat outFormat = null;
        SimpleDateFormat timeFormat = null;
        outFormat = new SimpleDateFormat("MMM dd");
        timeFormat = new SimpleDateFormat("hh:mm a");
        outFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = format.parse(input);
            outPutString = outFormat.format(date);
            outPutString = outPutString + "," + timeFormat.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outPutString;
    }


    public static String getDateFormatOne(String input) {
        String outPutString = "";
        //example Sun, 4 Aug

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(input);

            String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
            String day = (String) DateFormat.format("dd", date); // 20
            String monthString = (String) DateFormat.format("MMM", date); // Jun
            String monthNumber = (String) DateFormat.format("MM", date); // 06
            String year = (String) DateFormat.format("yyyy", date); // 2013

            dayOfTheWeek = dayOfTheWeek.substring(0, 3).toUpperCase();
            outPutString = dayOfTheWeek + "," + day + " " + monthString;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outPutString;
    }


    public static String convertTime(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt = null;
        try {
            dt = sdf.parse(time + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfs.format(dt);
    }


    public static long converToMilliSeconds(String date) {
        long millis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = null;
        try {

            Date ddd = sdf.parse(date);
            millis = ddd.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millis;
    }

    public static long getCurrentDateinMilliSeconds() {
        long millis = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        millis = calendar.get(Calendar.DATE);

        return millis;
    }


}
