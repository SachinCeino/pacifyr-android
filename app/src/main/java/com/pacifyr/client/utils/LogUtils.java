package com.pacifyr.client.utils;


import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.pacifyr.client.BuildConfig;

import java.io.File;
import java.io.FileWriter;


public class LogUtils {
	 private static final String LOG_PREFIX = "pacifyr";
	    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
	    private static final int MAX_LOG_TAG_LENGTH = 23;

	    public static String makeLogTag(String str) {
	        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
	            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
	        }

	        return LOG_PREFIX + str;
	    }

	    /**
         this when obfuscating class names!* Don't use
	     */
	    public static String makeLogTag(Class cls) {
	        return makeLogTag(cls.getSimpleName());
	    }

	    public static void LOGD(final String tag, String message) {
		//Log.d(tag, message);
	        //noinspection PointlessBooleanExpression,ConstantConditions
	        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
	            Log.d(tag, message);
	        }
			//saveConIDOnSD(message);
		//	Log.d(tag, message);

			/*if (BuildConfig.DEBUG || Log.isLoggable("jp", Log.DEBUG)) {
				Log.d("jp", message);
			}
			Log.d("jp", message);*/
	    }

	    /*{"userid":"5a1e9f183272232e375012aa","arn":"arn:aws:sns:us-east-1:271048179034:endpoint\/GCM\/PacifyrAndoidDev\/ae557443-d753-3980-a3fb-ef10b2c521c6","udid":"a47ac759e78acd1e","devicetoken":"APA91bG6hepAeV68zE1WbWyPPY9BrkTKksK9FoWGGXzxouy4w2jDYTIAv2UzbokaQeK9kK0-1xK3yrlq9iN6mDBtix2fMKt29oPNmfablYKfXHRQ_I13rDhvb8Ur5CWfNTfK57Q8Jmhr","deviceType":"android","applicationname":"PacifyrAndoidDev"}*/
	public static void LOGD( String message) {
		//Log.d(tag, message);
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (BuildConfig.DEBUG || Log.isLoggable("jp", Log.DEBUG)) {
			Log.d("jp", message);
		}
		Log.d("jp", message);
	}
	    public static void LOGD(final String tag, String message, Throwable cause) {
	        //noinspection PointlessBooleanExpression,ConstantConditions
	        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
	            Log.d(tag, message, cause);
	        }
	    }



	    public static void LOGV(final String tag, String message) {
	        //noinspection PointlessBooleanExpression,ConstantConditions
	        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
	            Log.v(tag, message);
	        }
	    }

	    public static void LOGV(final String tag, String message, Throwable cause) {
	        //noinspection PointlessBooleanExpression,ConstantConditions
	        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
	            Log.v(tag, message, cause);
	        }
	    }
	    
	    public static void ShowToast(Context context,String message)
	    {
	    	if(BuildConfig.DEBUG)
	    	{
	    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	    	}
	    }

	    public static void mustShowToast(Context context,String message)
		    {
		    		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		    }
	    public static void LOGI(final String tag, String message) {
	        Log.i(tag, message);
	    }

	    public static void LOGI(final String tag, String message, Throwable cause) {
	        Log.i(tag, message, cause);
	    }

	    public static void LOGW(final String tag, String message) {
	        Log.w(tag, message);
	    }

	    public static void LOGW(final String tag, String message, Throwable cause) {
	        Log.w(tag, message, cause);
	    }

	    public static void LOGE(final String tag, String message) {
	        Log.e(tag, message);
	    }

	    public static void LOGE(final String tag, String message, Throwable cause) {
	        Log.e(tag, message, cause);
	    }

	    private LogUtils() {
	    }


	public static void saveConIDOnSD( String purpose) {
		try {
			//  String url="";
			//  String modelname="m"+purpose;
			//   String Upper_purpose=purpose.toUpperCase();
			File root = new File(Environment.getExternalStorageDirectory(), "Pacifyer");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, "LogNotes");
			//FileWriter writer = new FileWriter(gpxfile);
			FileWriter writer = new FileWriter(gpxfile.getAbsoluteFile(), true);
			//    writer.append(" public static final String ACTION_"+Upper_purpose+" = \"action."+purpose+"\";");
			writer.append(" \n  "+purpose);
			writer.flush();
			writer.close();
			// Toast.makeText(context, "Log Saved", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtils.LOGD("exception", "saveConIDOnSD  Exception"+e.getMessage());
		}
	}
}
