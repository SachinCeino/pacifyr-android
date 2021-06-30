package com.pacifyrapp.utilitylibrary.model.pacifyr;

import android.util.Log;

import com.pacifyrapp.utilitylibrary.LogUtilsutility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**

 */

public class MLastSession {
    public MSessionRating[] sessionRatings;

    private String sessionCost;

    private String creditCardCharges;

    private String connected;

    private String sessionType;

    private String endTime;

    private String updatedAt;

    private String id;

    private String startTime;

    private String pacifyrId;

    private String afterSessionPacifyrEmotionId;

    private String duration;

    private String pacifyrEarnings;

    private String stopped;

    private String createdAt;

    private MUser pacifyr;

    private String userId;

    private String afterSessionCustomerEmotionId;

    private MUser user;

    private String aggregationSessionId;

    private String customerRating;
    private String reasonForTheCall;

    public String getReasonForTheCall() {
        return reasonForTheCall;
    }

    public void setReasonForTheCall(String reasonForTheCall) {
        this.reasonForTheCall = reasonForTheCall;
    }

    public MSessionRating[] getSessionRatings ()
    {
        return sessionRatings;
    }

    public void setSessionRatings (MSessionRating[] sessionRatings)
    {
        this.sessionRatings = sessionRatings;
    }



    public String getSessionCost ()
    {
        return sessionCost;
    }

    public void setSessionCost (String sessionCost)
    {
        this.sessionCost = sessionCost;
    }

    public String getCreditCardCharges ()
    {
        return creditCardCharges;
    }

    public void setCreditCardCharges (String creditCardCharges)
    {
        this.creditCardCharges = creditCardCharges;
    }

    public String getConnected ()
    {
        return connected;
    }

    public void setConnected (String connected)
    {
        this.connected = connected;
    }

    public String getSessionType ()
    {
        return sessionType;
    }

    public void setSessionType (String sessionType)
    {
        this.sessionType = sessionType;
    }

    public String getEndTime ()
    {
        return endTime;
    }

    public void setEndTime (String endTime)
    {
        this.endTime = endTime;
    }

    public String getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getStartTime ()
    {
        return startTime;
    }

    public void setStartTime (String startTime)
    {
        this.startTime = startTime;
    }

    public String getPacifyrId ()
    {
        return pacifyrId;
    }

    public void setPacifyrId (String pacifyrId)
    {
        this.pacifyrId = pacifyrId;
    }

    public String getAfterSessionPacifyrEmotionId ()
    {
        return afterSessionPacifyrEmotionId;
    }

    public void setAfterSessionPacifyrEmotionId (String afterSessionPacifyrEmotionId)
    {
        this.afterSessionPacifyrEmotionId = afterSessionPacifyrEmotionId;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getPacifyrEarnings ()
    {
        return pacifyrEarnings;
    }

    public void setPacifyrEarnings (String pacifyrEarnings)
    {
        this.pacifyrEarnings = pacifyrEarnings;
    }

    public String getStopped ()
    {
        return stopped;
    }

    public void setStopped (String stopped)
    {
        this.stopped = stopped;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public MUser getPacifyr ()
    {
        return pacifyr;
    }

    public void setPacifyr (MUser pacifyr)
    {
        this.pacifyr = pacifyr;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getAfterSessionCustomerEmotionId ()
    {
        return afterSessionCustomerEmotionId;
    }

    public void setAfterSessionCustomerEmotionId (String afterSessionCustomerEmotionId)
    {
        this.afterSessionCustomerEmotionId = afterSessionCustomerEmotionId;
    }

    public MUser getUser ()
    {
        return user;
    }

    public void setUser (MUser user)
    {
        this.user = user;
    }

    public String getAggregationSessionId ()
    {
        return aggregationSessionId;
    }

    public void setAggregationSessionId (String aggregationSessionId)
    {
        this.aggregationSessionId = aggregationSessionId;
    }

    public String getCustomerRating ()
    {
        return customerRating;
    }

    public void setCustomerRating (String customerRating)
    {
        this.customerRating = customerRating;
    }


    public String getFormatTime(String timeInput,SimpleDateFormat df) {

        long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
        long day = 86400000;
        long hour = 3600000;
        long minute = 60000;
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        String value = null;
        String dates =null;
        Calendar postedDay = Calendar.getInstance();
        try {
            date = df.parse(timeInput);
            postedDay.setTime(date);


            long newtime = postedDay.getTimeInMillis() + myTimeOffset;
            date = new Date(newtime);
            dates = df.format(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return dates;
    }

    public String getFormatTime(String timeInput) {

        long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
        long day = 86400000;
        long hour = 3600000;
        long minute = 60000;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        String value = null;
        String dates =null;
        Calendar postedDay = Calendar.getInstance();
        try {
            date = df.parse(timeInput);
            postedDay.setTime(date);


            long newtime = postedDay.getTimeInMillis() + myTimeOffset;
            date = new Date(newtime);
            dates = df.format(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return dates;
    }
    public String getDaysSincePosted() {

        long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
        long day = 86400000;
        long hour = 3600000;
        long minute = 60000;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        String value = null;
        Calendar postedDay = Calendar.getInstance();
        try {
            date = df.parse(this.endTime);
            postedDay.setTime(date);


            long newtime = postedDay.getTimeInMillis() + myTimeOffset;

            postedDay.setTimeInMillis(newtime);

            Log.e("Server Time=====>>" , postedDay.getTime()+ "");
            Log.e("Current Time=====>>" , Calendar.getInstance().getTime()+ "");

            LogUtilsutility.LOGD("jithish","newtime....."+newtime);


            days = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / day;

            LogUtilsutility.LOGD("jithish","days...."+days);

            hours = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / hour;

            LogUtilsutility.LOGD("jithish","hours...."+hours+"   "+Calendar.getInstance().getTimeInMillis());

            minutes = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / minute;


            LogUtilsutility.LOGD("jithish","minutes...."+minutes);

            if (days == 1) {
                value = " Yesterday";

            } else if (days > 1) {
                value = String.valueOf(days) + " days ago";

            } else if (days == 0) {


                if (hours > 12) {
                   // value = " Today";
                    value = String.valueOf(hours) + " hours ago";
                } else {


                    if (hours == 0) {

                        if(minutes<0)
                            minutes=0;

                        if (minutes == 0) {

                            value = "Moments ago";
                        } else {
                            value = String.valueOf(minutes) + " minutes ago";
                        }


                    } else {
                        value = String.valueOf(hours) + " hours ago";
                    }

                }

            }


            return value;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            postedDay.setTimeInMillis(Long.valueOf(this.createdAt));
            days = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / day;

            hours = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / hour;
            minutes = (Calendar.getInstance().getTimeInMillis() - postedDay.getTimeInMillis()) / minute;

            if (days == 1) {
                value = "Yesterday";

            } else if (days > 1) {
                value = String.valueOf(days) + " days ago";

            } else if (hour == 0) {

                value = String.valueOf(minutes) + " minutes ago";
            } else if (minutes == 0) {

                value = "moments ago";
            } else if (days == 0) {
                /*if (hours > 12) {
                    value = "Today";
                } else {*/
                    value = String.valueOf(hours) + " hours ago";
                //}
            }

            return value;

        }
    }
    @Override
    public String toString()
    {
        return "ClassPojo [sessionRatings = "+sessionRatings+", sessionCost = "+sessionCost+", creditCardCharges = "+creditCardCharges+", connected = "+connected+", sessionType = "+sessionType+", endTime = "+endTime+", updatedAt = "+updatedAt+", id = "+id+", startTime = "+startTime+", pacifyrId = "+pacifyrId+", afterSessionPacifyrEmotionId = "+afterSessionPacifyrEmotionId+", duration = "+duration+", pacifyrEarnings = "+pacifyrEarnings+", stopped = "+stopped+", createdAt = "+createdAt+", pacifyr = "+pacifyr+", userId = "+userId+", afterSessionCustomerEmotionId = "+afterSessionCustomerEmotionId+", user = "+user+", aggregationSessionId = "+aggregationSessionId+", customerRating = "+customerRating+"]";
    }
}
