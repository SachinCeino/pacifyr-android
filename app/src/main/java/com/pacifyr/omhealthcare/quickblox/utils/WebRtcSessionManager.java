package com.pacifyr.omhealthcare.quickblox.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ceino.utilitylibrary.ToastHandler;
import com.ceino.utilitylibrary.model.pacifyr.UserCallInfo;
import com.pacifyr.omhealthcare.base.PacifyerActivity;
import com.pacifyr.omhealthcare.home.SessionSummaryActivity;
import com.pacifyr.omhealthcare.quickblox.App;
import com.pacifyr.omhealthcare.quickblox.activities.CallActivity;
import com.pacifyr.omhealthcare.receiver.PacifyrReceiver;
import com.pacifyr.omhealthcare.utils.LogUtils;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl;

import java.util.Map;

/**
 * Created by tereha on 16.05.16.
 */
public class WebRtcSessionManager extends QBRTCClientSessionCallbacksImpl {
    //private static final String TAG = WebRtcSessionManager.class.getSimpleName();
    private static final String TAG ="app_rec";
    private static WebRtcSessionManager instance;
    private Context context;

    public static QBRTCSession currentSession,tempSession;
    public static  Map<String, String> userSessionInfo;
    public static UserCallInfo  userCallInfo;
    private WebRtcSessionManager(Context context) {
        this.context = context;
        Log.d(TAG, "WM onReceiveNewSession1 "+context);
    }

    public static WebRtcSessionManager getInstance(Context context){
        Log.d(TAG, "WM onReceiveNewSession2 "+context);
        if (instance == null){
            instance = new WebRtcSessionManager(context);
        }

        return instance;
    }

    public QBRTCSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(QBRTCSession qbCurrentSession) {
        Log.d(TAG, "WM onReceiveNewSession3 "+qbCurrentSession);
        currentSession = qbCurrentSession;
        tempSession=null;
    }

    @Override
    public void onReceiveNewSession(final QBRTCSession session) {
        Log.d(TAG, "WM onReceiveNewSession4 "+session);

        if (currentSession == null ){

            Log.d(TAG, "WM onReceiveNewSession5 "+currentSession);
            try {
                userSessionInfo = session.getUserInfo();
                userCallInfo=getOppnentUser();

                Log.d(TAG, "WM onReceiveNewSession  sessionid "+userCallInfo.sessionId);

                if( userCallInfo.sessionId!=null)
                {

                    try {
                        PacifyerActivity.sessionid=userCallInfo.sessionId;
                        setCurrentSession(session);
                        Activity activity = null;
                        activity = ((App) App.getInstance()).getCurrentActivity();
                        if(isValid(activity))
                            LogUtils.LOGD("sessionteeest123456", " SSA onCreate sessionid ");
                        if (activity instanceof SessionSummaryActivity ) {
                            activity.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tempSession=null;
                    CallActivity.start(context, true);
                }
                else
                {

                    tempSession=session;
                    Log.d(TAG, "WM onReceiveNewSession  tempSession"+tempSession);
                    if(PacifyrReceiver.temp_sessionid!=null)
                    {
                        LogUtils.LOGD("app_rec", "WM onReceiveNewSession Call ");
                        PacifyerActivity.sessionid = PacifyrReceiver.temp_sessionid;
                        WebRtcSessionManager.currentSession = WebRtcSessionManager.tempSession;
                        WebRtcSessionManager.tempSession = null;
                        PacifyrReceiver.temp_sessionid=null;
                        userCallInfo.sessionId=        PacifyerActivity.sessionid;
                        ToastHandler.newInstance(context).showToast("WM ic_call_round data recevied from backend ");
                        CallActivity.start(context, true);
                    }


                    new Handler().postDelayed(new Runnable()
                    {
                        @Override public void run() {
                            Log.d(TAG, "WM onReceiveNewSession tempSession cleaed");
                            tempSession=null;
                        }
                    },3000);



                  /*  new Handler().postDelayed(new Runnable()
                    {
                        @Override public void run() {

                            userSessionInfo = session.getUserInfo();
                            userCallInfo=getOppnentUser();

                            if( userCallInfo.sessionId!=null)
                            {
                                PacifyerActivity.sessionid=userCallInfo.sessionId;
                                setCurrentSession(session);
                                try {
                                    Activity activity = null;
                                    activity = ((AppChatNotUSe) AppChatNotUSe.getInstance()).getCurrentActivity();
                                    if(isValid(activity))
                                        if (activity instanceof SessionSummaryActivity ) {
                                            activity.finish();
                                        }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                CallActivity.start(context, true);
                                ToastHandler.newInstance( AppChatNotUSe.getInstance()).showToast("delay reciver ");
                            }
                            else
                            {
                               if(PacifyerActivity.sessionid!=null)
                               {
                                   userCallInfo.sessionId=PacifyerActivity.sessionid;
                                   CallActivity.start(context, true);
                               }


                                ToastHandler.newInstance( AppChatNotUSe.getInstance()).showToast("Error in Call");
                            }

                        }
                    }, 3000);*/
                }

            } catch (Exception e) {
                ToastHandler.newInstance( App.getInstance()).showToast("Exception in Call");
                PacifyerActivity.sessionid=null;
                userSessionInfo=null;
                e.printStackTrace();
            }
            //  OpponentsActivity.start(context, true);
            //CallActivity.start(context, true);
        }
    }
    public Boolean isValid(Object object) {
        if(object != null) return true;
        return false;

    }
    public   UserCallInfo getOppnentUser()
    {
        UserCallInfo info=new UserCallInfo();
        try{
            //info=new UserCallInfo();
            info.firstName=userSessionInfo.get("firstName");
            info.lastName=userSessionInfo.get("lastName");
            info.imageAvathar=userSessionInfo.get("imageAvathar");
            info.OpponentUserid=userSessionInfo.get("OpponentUserid");
            info.sessionId=userSessionInfo.get("sessionId");

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        Log.d(TAG, "onSessionClosed WebRtcSessionManager");

        if (session.equals(getCurrentSession())){
            setCurrentSession(null);
        }
    }
}
