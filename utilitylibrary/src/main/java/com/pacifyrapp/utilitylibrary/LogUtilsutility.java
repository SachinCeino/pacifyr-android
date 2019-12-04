package com.pacifyrapp.utilitylibrary;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;



public class LogUtilsutility {

	//groupid: 585c8b9a5d5a4b0b60a8e981
	 private static final String LOG_PREFIX = "ceino";
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
	 Log.d(tag, message);
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

	    private LogUtilsutility() {
	    }


	// getsmart -
	/*
	* magazine Details
	* ChildName
	* MagazinProfileNew
	* container
	*
	* */

		/*
	* helpscreen
	*
	* */


	/*{
		"results": [
		{
			"updatedAt": "2017-01-30 10:20:47",
				"_id": "588f137f03daa80474deffd1",
				"image": "https://developer.android.com/images/home/n-preview-hero.png",
				"title": "first",
				"desc": "first desc",

		},
		{
			"updatedAt": "2017-01-24 08:28:25",
				"_id": "588f137f03daa80474deffd1",
				"image": "https://developer.android.com/images/home/n-preview-hero.png",
				"title": "second",
				"desc": "second dec",

		},
		{
			"updatedAt": "2016-04-21 12:50:59",
				"_id": "588f137f03daa80474deffd1",
				"image": "https://developer.android.com/images/home/n-preview-hero.png",
				"title": "third",
				"desc": "third third desc",

		}
		]
	}*/














}
