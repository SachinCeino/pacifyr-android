package com.pacifyr.client.model.interfaces;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelUserProfile;

/**

 */

public interface AvailableMinuteListner
    {
       // public abstract void onAvailable(final ModelUserProfile results, final boolean isVideoCall, final long qbid);
        public abstract void onAvailable(final ModelUserProfile mCurrentUser,final MUser pacifyerDetial, boolean isVideoCall);
        //public abstract void onAvailable(final MUser results, final boolean isVideoCall);
        /*public abstract void onAvailable();
        public abstract void onAlert(long time);*/
       // public abstract void onAlert(long time,final ModelUserProfile results, final boolean isVideoCall, final long qbid);
        public abstract void onAlert(long time,final MUser pacifyerDetial, final boolean isVideoCall);
      //  public abstract void onAlert(long time,final ModelUserProfile mCurrentUser, final boolean isVideoCall);
        public abstract void onPacifyerAlert( long time,final ModelUserProfile mCurrentUser,final MUser pacifyerDetial, final boolean isVideoCall);
        public abstract void onNotAvailable(long time);
        public abstract void onException(Exception e);
        public abstract void onError(String e);


    }

