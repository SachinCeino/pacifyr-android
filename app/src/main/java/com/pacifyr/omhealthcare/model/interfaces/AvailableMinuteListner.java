package com.pacifyr.omhealthcare.model.interfaces;

import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyr.omhealthcare.model.MComments;

/**
 * Created by Mani-Ceino on 12/28/2017.
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

