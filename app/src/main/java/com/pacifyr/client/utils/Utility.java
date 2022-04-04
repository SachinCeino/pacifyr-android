package com.pacifyr.client.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.pacifyr.client.R;
import com.pacifyr.client.activity.SplashScreen;
import com.pacifyr.client.home.DashboardActivity;
import com.pacifyr.client.model.HttpStatus;
import com.pacifyr.client.model.PermissionModel;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.pacifyr.client.sign.PacifyrRequestAccessActivity.getPath;

/**
 * Created by User on 01-06-2016.
 */
public class Utility {

    private static final int MY_PERMISSIONS_REQUEST_ESSENTIAL = 130;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION = 131;
    private static final String TAG = Utility.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_CHAT_PERMISSION = 132;
    public static final int MY_PERMISSIONS_REQUEST_AUDIO_CALL_PERMISSION = 133;
    public static final int MY_PERMISSIONS_REQUEST_VIDEO_CALL_PERMISSION = 134;
    public static int colourcode = 2;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 125;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 126;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 127;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 128;
    public static final int MY_PERMISSIONS_REQUEST_READ_AUDIO = 129;



    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }


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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
                        alertBuilder.setMessage("External storage permission is required to " +
                                "proceed");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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


            } else if (type.equals("videocall")) {

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
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_VIDEO_CALL_PERMISSION);
                                if (alert != null)
                                    alert.dismiss();
                            }
                        });

                    } else {
                        //LogUtils.LOGD("jithish","audiochecks......1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_VIDEO_CALL_PERMISSION);
                    }
                    return false;
                } else {
                    return true;
                }


            } else if (type.equals("audiocall")) {

                //LogUtils.LOGD("jithish","audiochecks......");

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO)) {
                        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_dialog, null);
                        alertBuilder.setView(view);
                        Button positive = view.findViewById(R.id.button_dialog_positive);
                        TextView title = view.findViewById(R.id.text_dialog_title);
                        TextView message = view.findViewById(R.id.text_dialog_message);
                        title.setText("Permission Required");
                        message.setText("Audio recording permission is required for making audio calls");
                        alertBuilder.setCancelable(true);
                        final AlertDialog alert = alertBuilder.create();
                        alert.show();
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_AUDIO_CALL_PERMISSION);
                                if (alert != null)
                                    alert.dismiss();
                            }
                        });

                    } else {
                        //LogUtils.LOGD("jithish","audiochecks......1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_AUDIO_CALL_PERMISSION);
                    }
                    return false;
                } else {
                    return true;
                }


            } else if (type.equals("readphone")) {

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
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    } else {
                        //LogUtils.LOGD("jithish","audiochecks..jjjjj....1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_AUDIO);
                    }
                    return false;
                } else {
                    return true;
                }

            } else if (type.equals("location")) {

                //LogUtils.LOGD("jithish","audiochecks......");

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission required");
                        alertBuilder.setMessage("Your location is required to search for Counselors around you");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    } else {
                        //LogUtils.LOGD("jithish","audiochecks..jjjjj....1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION_PERMISSION);
                    }
                    return false;
                } else {
                    return true;
                }

            } else if (type.equals("chat")) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission required");
                        alertBuilder.setMessage("Camera & Storage permissions are required to proceed");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                                        }, MY_PERMISSIONS_REQUEST_CHAT_PERMISSION);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        return true;
                    } else {
                       // LogUtils.LOGD("jithish","audiochecks..jjjjj....1");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CHAT_PERMISSION);
                        return false;
                    }

                } else {
                    return true;
                }

            } else {
                return true;
            }

        }
        return true;
    }


    public static PermissionModel checkDevicePermission(final Context context, String type) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            switch (type){
                case "chat":{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                            alertBuilder.setCancelable(true);

                            alertBuilder.setTitle("Permission required");
                            alertBuilder.setMessage("Camera & Storage permissions are required to proceed");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                                    }, MY_PERMISSIONS_REQUEST_CHAT_PERMISSION);
                                }
                            });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                            return new PermissionModel(true, false);

                        } else {
                            LogUtils.LOGD("jithish","audiochecks..jjjjj....1");
                            ActivityCompat.requestPermissions((Activity) context, new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CHAT_PERMISSION);

                            return new PermissionModel(false, false);
                        }

                    } else {
                        return new PermissionModel(false, true);
                    }
                }
            }
        }
        return new PermissionModel(false, true);
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


    public static Boolean isValid(String text) {
        if (text != null)
            return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public static Boolean isValid(String text,int size) {
        if (isValid(text))
            return text.length() >= size;
        return false;

    }

    public static Boolean isValid(List list) {
        if (list != null)
            return list.size() > 0;
        return false;

    }

    public static Boolean isValid(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;

    }

    public static Boolean isListSize(List list,int size) {
        if (isValid(list))
            return list.size() == size;
        return false;

    }

    public static Boolean isValidPos(List list,int pos) {
        if (isValid(list))
            return list.size() >= pos;
        return false;
    }

    public static Boolean isValid(String[] list,int size) {

        if (isValid(list))
            return list.length >= size;
        return false;

    }

    public static Boolean isValidNotNull(List list) {
        // if(list.size()>0)
        return list != null;

    }

    public static Boolean isValid(Object object) {
        return object != null;

    }

    public static File getTempFile(Context context, String fileName) {
        File file = null;
        try {
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
            e.printStackTrace();
        }
        return file;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }

    public static boolean writeBytesToFile(byte[] data, File file) {
        if (file != null && file.exists()) {
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                bos.close();

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    public static String getRelativeDateTimeString(Context context, String input) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));


        SimpleDateFormat outFormat = new SimpleDateFormat("d.MM");
        PrefManager prefM = PrefManager.getInstance(context);
        outFormat.setTimeZone(TimeZone.getDefault());

        Date date = null;
        String dayname="";
        Date formattedDate = null;
        try {
            date = format.parse(input);
            System.out.println("Date ->" + date);

            if(DateUtils.isToday(date.getTime())){
                dayname = getTime(input);
            }else if(DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS)){
                dayname = context.getResources().getString(R.string.yesterday);
            }else {
                Date c = Calendar.getInstance().getTime();

               long dys =  getDateDiff(format, input,format.format(c));
               if(dys <= 6){
                   dayname = getWeekDay(date);
               } else {
                   SimpleDateFormat targetFormat;// = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
                   String country = prefM.getCountry();
                   if(country.equals("India")){
                       targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                   }else{
                       targetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                   }
                   //dayname = outFormat.format(date);
                   SimpleDateFormat recivedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                   recivedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                   formattedDate = recivedFormat.parse(input);

                   dayname = targetFormat.format(formattedDate).toLowerCase();
               }

            }


            return dayname;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getWeekDay(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        dayFormat.setTimeZone(TimeZone.getDefault());
        return dayFormat.format(date);
    }

    /**
     * Get a diff between two dates
     *
     * @param oldDate the old date
     * @param newDate the new date
     * @return the diff value, in the days
     */
    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getFormattedDate(Context context,String input) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));


        SimpleDateFormat outFormat = new SimpleDateFormat("MMMM d");

        outFormat.setTimeZone(TimeZone.getDefault());

        Date date = null;
        String dayname="";

        try {
            date = format.parse(input);
            System.out.println("Date ->" + date);

            if(DateUtils.isToday(date.getTime())){
                dayname = context.getResources().getString(R.string.today);
            }else if(DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS)){
                dayname = context.getResources().getString(R.string.yesterday);
            }else {
                dayname = outFormat.format(date);
            }


            return dayname;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime(String input) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat outFormat = null;

            outFormat = new SimpleDateFormat("HH:mm");

        outFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = format.parse(input);
            return outFormat.format(date).toLowerCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            outFormat = new SimpleDateFormat("dd-MM-yyyy");
        }
        outFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = format.parse(input);
            System.out.println("Date ->" + date);
            return outFormat.format(date).toLowerCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateAndTime(String input) {
        String outPutString = "";
        //example Aug 7,10 30:am
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

    public static String getDateFormatOneWithYear(String input) {
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
            outPutString = monthString + "," + day + " " + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outPutString;
    }


    public static String convertTime(String time) {
        if (time.equals("12:00")){
            return "12:00 pm";}
        else if (time.equals("12:30")){
            return "12:30 pm";}
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt = null;
        try {
            dt = sdf.parse(time + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String d = sdfs.format(dt);
        d = d.replace("AM", "am").replace("PM","pm");
        return d;
    }



    public static long converToSecondsForJoin(String data) {
        long millis = 0;//2022-04-04T09:09:13.211Z //1649063353211

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = null;
            try {
                date = format.parse(data);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                // Get timezone offset then use it to adjust the return value
                int offset = cal.getTimeZone().getOffset(cal.getTimeInMillis());
                millis= cal.getTimeInMillis() + offset;

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return millis;
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

    public static long converToMilliSecondsFormatb(String date) {
        long millis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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

        return System.currentTimeMillis();
    }

    public static long getCurrentMaxMilliseconds()
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static String convertMillisecondsTodate(Long dateInMillis) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(dateInMillis));
//get date like 2019-06-14
        return dateString;
    }


    public static String convertDateToMilliseconds(List<String> date) {

        String display_Date = "";
        ArrayList<Long> milliseconds = new ArrayList<>();
        for (int position = 0; position < date.size(); position++) {
            milliseconds.add(Utility.converToMilliSeconds(date.get(position)));
        }

        if (isValid(milliseconds)) {
            Collections.sort(milliseconds);
            display_Date = Utility.convertMillisecondsTodate(milliseconds.get(milliseconds.size() - 1));
        }

        // pasing date like 2019-06-14
        return display_Date;
    }


    /**
     * if country is India then dd-MM-yyyy
     * else
     * ** MM-dd-yyyy
     **/
    public static String getStartFormatDate(String iiiiii, Context mContext) {
        // Date daty=null;
        Date formattedDate = null;
        PrefManager prefM = PrefManager.getInstance(mContext);
        String dateat = "";
        try {
            SimpleDateFormat targetFormat;// = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
            String country = prefM.getCountry();
            if (country.equals("India")) {
                targetFormat = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a", Locale.ENGLISH);
            } else {
                targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
            }
            // SimpleDateFormat sdf=new SimpleDateFormat();


            SimpleDateFormat recivedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            recivedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            formattedDate = recivedFormat.parse(iiiiii);

            dateat = targetFormat.format(formattedDate);
            //daty=targetFormat.parse(dateat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateat.toLowerCase();
    }


    public static String getDateAndTimeForAppointmentList(String input) {

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
            outPutString = outPutString + ", " + timeFormat.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outPutString.toLowerCase();
    }

    public static long getLocalTimeFromUtc(String input) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = format.parse(input);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static long getLocalTimeZone(String input) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = format.parse(input);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCorrectRoundedDuration(String input) {

        String[] split=input.split(":");
        String hrs= split[0];
        String mnts=  split[1];
        String seconds=  split[2];

        if (Integer.parseInt(seconds)>30){
            mnts= String.valueOf(Integer.parseInt(mnts)+1);
            if (mnts.length()==1)
            {
                mnts="0"+mnts;
            }
        }
        if (hrs.equals("0"))
            return "00"+":"+mnts;
        else
            return hrs+":"+mnts;


    }


    public static String timeZoneOffset()
    {
        Calendar calendar = Calendar.getInstance( Locale.getDefault());
        String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        Log.w("fff",timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5));
        return timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5);
    }

    public static String getTimeStamp() {
        Date mDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return df.format(mDate);
    }

    public static void getUserCreatedDayMonthYear(String dateFromServer) {
        // Date daty=null;
        Date formattedDate = null;
        String dateat = "";
        String[] values = new String[0];
        try {
            SimpleDateFormat targetFormat;// = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);
            targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat recivedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            recivedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            formattedDate = recivedFormat.parse(dateFromServer);

            DashboardActivity.CreatedDay = String.valueOf(DateFormat.format("yyyy-MM-dd", formattedDate));
            //PacifyrDashboardActivity.CreatedMonth = String.valueOf(DateFormat.format("MMM", formattedDate));
            DashboardActivity.CreatedYear = String.valueOf(DateFormat.format("yyyy", formattedDate));

            Calendar c = Calendar.getInstance();
            c.setTime(formattedDate);

            Log.w("@@@", "dsfdsf");

           /* dateat = targetFormat.format(formattedDate).toLowerCase();
            values = dateat.split("-");
            //daty=targetFormat.parse(dateat);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long converToMilliSecondsForSession(String date) {
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



    public static File saveFileInCache(Context context, Bitmap imageBitmap, String fileName) {
        File cacheFile = getTempFile(context, fileName);

        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(cacheFile));
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            return cacheFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getAdvancedFilePath(Context context, Uri uri){
        boolean isKitKat= Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;

        if(isKitKat && DocumentsContract.isDocumentUri(context,uri)){
            if (isExternalStorageDocument(uri)) {

                String docId=DocumentsContract.getDocumentId(uri);
                String[] split=docId.split(":");
                String type=split[0];

                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }
            }
            else if(isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {}
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            else if(isMediaDocument(uri)){
                String docId=DocumentsContract.getDocumentId(uri);
                String[] split=docId.split(":");
                String type=split[0];
                Uri contenturi=null;
                if("image".equals(type)){
                    contenturi= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if("video".equals(type)){
                    contenturi= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if("audio".equals(type)){
                    contenturi= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection="_id=?";
                String[] selectionarg=new String[]{split[1]};

                return getDataColumn(context,contenturi,selection,selectionarg);
            }

        }
        else if("content".equalsIgnoreCase(uri.getScheme())){
            if(isGooglePhotosUri(uri))
            {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context,uri,null,null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context,Uri uri,String selection,String[] selectionargs){
        Cursor cursor=null;
        final String column="_data";
        final String[] projection={column};
        try {
            cursor=context.getContentResolver().query(uri,projection,selection,selectionargs,null);
            if(cursor!=null && cursor.moveToFirst()){
                int index=cursor.getColumnIndexOrThrow(column);
                return  cursor.getString(index);
            }

        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }

        return null;

    }
    public static String getRealPathFromUri(Uri uri,Activity activity){
        Cursor cursor=activity.getContentResolver().query(uri,null,null,null,null);
        if(cursor==null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), "documents");
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        logDir(context.getCacheDir());
//        logDir(dir);

        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //Log.w(TAG, e);
            return null;
        }

        //logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handleDeepLink(Activity context, Intent intent, SplashScreen.DeepLinkListener linkListener) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(context, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if (deepLink != null) {
                            Log.d(TAG, "deep link " + deepLink);
                            String link = deepLink.toString();
                            if (link.contains("/")) {
                                String[] splits = link.split("/");
                                if (splits != null && splits.length > 0) {
                                    if (splits[splits.length - 2] != null && splits[splits.length - 2].equalsIgnoreCase("coupon")) {
                                        String code = splits[splits.length - 1];
                                        Log.d(TAG, "deep link code " + code);
                                        if (code != null) {
                                            PrefManager.getInstance(context).putString(PrefManager.COUPON_CODE, code);
                                        }
                                    }
                                }
                            }
                        }
                        if (linkListener != null) {
                            linkListener.onDone();
                        }

                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("handleDeepLink", "getDynamicLink:onFailure", e);
                        if (linkListener != null) {
                            linkListener.onDone();
                        }
                    }
                });
    }

    public static HttpStatus checkStatusCode(int code) {
        HttpStatus httpStatus = new HttpStatus();
        switch (code) {
            case 500: {
                return httpStatus;
            }
            case 200: {
                httpStatus.setValid(true);
                httpStatus.setMessage("Success");
                return httpStatus;
            }
            case 401: {
                httpStatus.setValid(false);
                httpStatus.setMessage("Authentication required");
                return httpStatus;
            }
            default: {
                httpStatus.setValid(false);
                httpStatus.setMessage("Something went wrong. PLease try later");
                return httpStatus;
            }
        }
    }

    public static void getRealTimeLogging(Context context) {
        String userId = PrefManager.getInstance(context).getUserID();
        if (userId == null || userId.isEmpty()) {
            PrefManager.getInstance(context).putSharedInteger(Constants.KEY_REALTIME_LOGGING, 0);
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference device_config = database.getReference(FirebaseLogging.FIREBASE_CONFIG_TABLE);
        device_config.child("log_events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange" + ":" + snapshot.getValue());

                if (snapshot != null && snapshot.getValue() != null && snapshot.getValue().equals("yes")) {
                    PrefManager.getInstance(context).putSharedInteger(Constants.KEY_REALTIME_LOGGING, 1);
                } else {
                    PrefManager.getInstance(context).putSharedInteger(Constants.KEY_REALTIME_LOGGING, -1);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        /*Query userQuery = userRef.child("userId");
        userQuery.limitToFirst(1);
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d(TAG, "Child added " + previousChildName +":" + snapshot.getValue());
                if (snapshot != null && snapshot.getValue() != null && snapshot.getValue().equals(userId)) {
                    PrefManager.getInstance(context).putSharedInteger(Constants.KEY_REALTIME_LOGGING, 1);
                    userQuery.removeEventListener(this);
                } else {
                    PrefManager.getInstance(context).putSharedInteger(Constants.KEY_REALTIME_LOGGING, -1);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d(TAG, "Child changed " + previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //Log.d(TAG, "Child removed " );
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d(TAG, "Child moved " );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG, "cancelled " );
            }
            // TODO: implement the ChildEventListener methods as documented above
            // ...
        });*/
    }

    /** Preventing double click buttons **/
    public static void preventDoubleClickButtons(final View view){
        try {
            view.setEnabled(false);
            view.postDelayed(new Runnable() {
                public void run() {
                    view.setEnabled(true);
                }
            }, 1000);
        }catch (Exception e)
        {

        }
    }

    public static String getPaymentLabel(Context applicationContext, String country) {
        if (country.equals("US"))
        {
            return applicationContext.getResources().getString(R.string.payment_label_us);
        }
        else
        {
            return applicationContext.getResources().getString(R.string.payment_label_others);
        }
    }

    public static String ConvertUpdatedDateToTime(String time)
    {
        Date formattedDate = null;
        String dateat = "";
        try {
            // SimpleDateFormat sdf=new SimpleDateFormat();
            SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a", Locale.ENGLISH);

            SimpleDateFormat recivedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            recivedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            formattedDate = recivedFormat.parse(time);

            dateat = targetFormat.format(formattedDate).toLowerCase();
            //daty=targetFormat.parse(dateat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateat;
    }

}
