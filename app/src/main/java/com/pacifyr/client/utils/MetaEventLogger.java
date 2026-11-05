package com.pacifyr.client.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.pacifyr.client.BuildConfig;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Logs Meta (Facebook) standard App Events.
 * <p>
 * Never pass personal or health data (names, emails, emotions, session notes) as parameters:
 * only opaque IDs and generic content types.
 * Every call is wrapped so analytics can never crash a user flow.
 */
public final class MetaEventLogger {

    private static final String TAG = MetaEventLogger.class.getSimpleName();
    private static final String PREFS_NAME = "meta_app_events";

    public static final int MAX_RATING_VALUE = 5;

    // Content types
    public static final String CONTENT_TYPE_RESOURCE = "resource";
    public static final String CONTENT_TYPE_ARTICLE = "article";
    public static final String CONTENT_TYPE_VIDEO = "video";
    public static final String CONTENT_TYPE_COUNSELOR = "counselor";
    public static final String CONTENT_TYPE_SESSION = "session";
    public static final String CONTENT_TYPE_SESSION_PACK = "session_pack";
    public static final String CONTENT_TYPE_SESSION_EXTENSION = "session_extension";

    // Registration methods
    public static final String REGISTRATION_METHOD_FACEBOOK = "facebook";
    public static final String REGISTRATION_METHOD_PHONE = "phone";

    // Custom (non-standard) parameter describing where a Contact / StartTrial came from
    public static final String PARAM_SOURCE = "source";
    public static final String SOURCE_CRISIS_HOTLINE = "crisis_hotline";
    public static final String SOURCE_CONNECT_COUNSELOR = "connect_counselor";
    public static final String SOURCE_SUPPORT_MESSAGE = "support_message";
    public static final String SOURCE_FREE_MINUTES = "free_minutes";
    public static final String SOURCE_ZERO_COST_BOOKING = "zero_cost_booking";

    private MetaEventLogger() {
    }

    /** Call once from Application.onCreate(). */
    public static void init(Application application) {
        try {
            if (BuildConfig.DEBUG) {
                FacebookSdk.setIsDebugEnabled(true);
                FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
            }
            AppEventsLogger.activateApp(application);
        } catch (Exception e) {
            Log.e(TAG, "Meta SDK init failed", e);
        }
    }

    /** ISO 4217 currency code, mirroring {@link Utility#getPriceSign(Context)}. */
    public static String getCurrencyCode(Context context) {
        try {
            String country = PrefManager.getInstance(context).getCountry();
            if ("India".equals(country)) {
                return "INR";
            }
        } catch (Exception ignored) {
        }
        return "USD";
    }

    public static void logContact(Context context, String source) {
        Bundle params = new Bundle();
        putIfValid(params, PARAM_SOURCE, source);
        log(context, AppEventsConstants.EVENT_NAME_CONTACT, null, params);
    }

    /** contentId = the submitted search term. */
    public static void logSearch(Context context, String contentId, String contentType) {
        log(context, AppEventsConstants.EVENT_NAME_SEARCHED, null, contentParams(contentId, contentType));
    }

    public static void logCompleteRegistration(Context context, String registrationMethod) {
        Bundle params = new Bundle();
        putIfValid(params, AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, registrationMethod);
        log(context, AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, null, params);
    }

    public static void logViewContent(Context context, String contentId, String contentType) {
        log(context, AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, null, contentParams(contentId, contentType));
    }

    public static void logSubscribe(Context context) {
        log(context, AppEventsConstants.EVENT_NAME_SUBSCRIBE, null, new Bundle());
    }

    public static void logInitiateCheckout(Context context, String contentId, String contentType, double value) {
        Bundle params = contentParams(contentId, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, getCurrencyCode(context));
        log(context, AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, value, params);
    }

    /**
     * Logs StartTrial only the first time for the logged-in user and source
     * (e.g. first session that runs on free minutes).
     */
    public static void logStartTrialOnce(Context context, String source) {
        if (context == null) return;
        try {
            String userId = PrefManager.getInstance(context).getUserID();
            String key = "start_trial_" + source + "_" + (userId == null ? "" : userId);
            SharedPreferences prefs = context.getApplicationContext()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (prefs.getBoolean(key, false)) {
                return;
            }
            prefs.edit().putBoolean(key, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "logStartTrialOnce failed", e);
            return;
        }
        logStartTrial(context, source);
    }

    public static void logStartTrial(Context context, String source) {
        Bundle params = new Bundle();
        putIfValid(params, PARAM_SOURCE, source);
        log(context, AppEventsConstants.EVENT_NAME_START_TRIAL, null, params);
    }

    public static void logRate(Context context, String contentType, double ratingGiven) {
        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_MAX_RATING_VALUE, MAX_RATING_VALUE);
        putIfValid(params, AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        log(context, AppEventsConstants.EVENT_NAME_RATED, ratingGiven, params);
    }

    public static void logPurchase(Context context, String contentId, String contentType, double amount) {
        try {
            Bundle params = contentParams(contentId, contentType);
            String currency = getCurrencyCode(context);
            params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
            AppEventsLogger.newLogger(context.getApplicationContext())
                    .logPurchase(BigDecimal.valueOf(amount), Currency.getInstance(currency), params);
            debugFlush(context);
        } catch (Exception e) {
            Log.e(TAG, "logPurchase failed", e);
        }
    }

    public static void logAddPaymentInfo(Context context, boolean success) {
        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success ? 1 : 0);
        log(context, AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, null, params);
    }

    private static Bundle contentParams(String contentId, String contentType) {
        Bundle params = new Bundle();
        putIfValid(params, AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        putIfValid(params, AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        return params;
    }

    private static void putIfValid(Bundle params, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            params.putString(key, value);
        }
    }

    private static void log(Context context, String eventName, Double valueToSum, Bundle params) {
        if (context == null) {
            Log.w(TAG, "No context, skipping " + eventName);
            return;
        }
        try {
            AppEventsLogger logger = AppEventsLogger.newLogger(context.getApplicationContext());
            if (valueToSum != null) {
                logger.logEvent(eventName, valueToSum, params);
            } else {
                logger.logEvent(eventName, params);
            }
            debugFlush(context);
        } catch (Exception e) {
            Log.e(TAG, "Failed to log " + eventName, e);
        }
    }

    /** In debug builds send immediately so events show up in Events Manager → Test Events. */
    private static void debugFlush(Context context) {
        if (BuildConfig.DEBUG) {
            AppEventsLogger.newLogger(context.getApplicationContext()).flush();
        }
    }
}
