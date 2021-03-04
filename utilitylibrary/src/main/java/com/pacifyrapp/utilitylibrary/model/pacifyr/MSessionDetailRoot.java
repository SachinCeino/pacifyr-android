package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MSessionDetailRoot
{
    private double sessionCost;

    private String connected;

    private String endTime;

    private String id;

    private String startTime;

    private String declined;

    private String createdAt;

    private String userId;

    private String afterSessionCustomerEmotionId;

    private String customerRating;

    private String reasonForTheCall;

    private MSessionRating[] sessionRatings;

    private String creditCardCharges;

    private String sessionType;

    private String updatedAt;

    private String pacifyrId;

    private String afterSessionPacifyrEmotionId;

    private String duration;

    private double pacifyrEarnings;

    private String stopped;

    private String anonymousCall;

    private MUser pacifyr;

    private MUser user;

    private String scheduledSessionId;

    private String missed;

    private boolean billedSession;

    private String prescriptionFile;


    private String aggregationSessionId;
    private  String  timeSlotBookingId;
    private  String twilioRoomSID;
    private String twilioRoomName;

    public String getTwilioRoomName() {
        return twilioRoomName;
    }

    public void setTwilioRoomName(String twilioRoomName) {
        this.twilioRoomName = twilioRoomName;
    }

    public String getTwilioRoomSID() {
        return twilioRoomSID;
    }

    public void setTwilioRoomSID(String twilioRoomSID) {
        this.twilioRoomSID = twilioRoomSID;
    }

    public String getTimeSlotBookingId() {
        return timeSlotBookingId;
    }

    public void setTimeSlotBookingId(String timeSlotBookingId) {
        this.timeSlotBookingId = timeSlotBookingId;
    }

    public String getPrescriptionFile() {
        return prescriptionFile;
    }

    public void setPrescriptionFile(String prescriptionFile) {
        this.prescriptionFile = prescriptionFile;
    }

    public double getSessionCost ()
    {
        return sessionCost;
    }

    public void setSessionCost (double sessionCost)
    {
        this.sessionCost = sessionCost;
    }

    public String getConnected ()
    {
        return connected;
    }

    public void setConnected (String connected)
    {
        this.connected = connected;
    }

    public String getEndTime ()
    {
        return endTime;
    }

    public void setEndTime (String endTime)
    {
        this.endTime = endTime;
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

    public String getDeclined ()
    {
        return declined;
    }

    public void setDeclined (String declined)
    {
        this.declined = declined;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
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

    public String getCustomerRating ()
    {
        return customerRating;
    }

    public void setCustomerRating (String customerRating)
    {
        this.customerRating = customerRating;
    }

    public String getReasonForTheCall ()
    {
        return reasonForTheCall;
    }

    public void setReasonForTheCall (String reasonForTheCall)
    {
        this.reasonForTheCall = reasonForTheCall;
    }

    public MSessionRating[] getSessionRatings ()
    {
        return sessionRatings;
    }
    public ArrayList<MSessionRating> getSessionRatingList ()
    {
        ArrayList<MSessionRating> arrayList = null;
        try {
            if(sessionRatings != null) {
                arrayList = new ArrayList<MSessionRating>(Arrays.asList(sessionRatings));

            }
        } catch (Exception e){
            arrayList = null;
        }
        return arrayList;
    }

    public void setSessionRatings (MSessionRating[] sessionRatings)
    {
        this.sessionRatings = sessionRatings;
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


    public String getMyTime(String timeInput) {
        String returnDate = null;
        try {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'",Locale.ENGLISH);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            Date date = formatter.parse(timeInput);// Or whatever IST is supposed to be
            //String dat = formatter.format(timeInput);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a",Locale.ENGLISH);
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            returnDate = outputFormat.format(date);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return returnDate;
    }




    public String getFormatTime(String timeInput) {
        String dates = null;
        try {

            //int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();

            long myTimeOffset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
            long day = 86400000;
            long hour = 3600000;
            long minute = 60000;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'", Locale.ENGLISH);
            Date date = null;
            long days = 0;
            long hours = 0;
            long minutes = 0;
            String value = null;

            Calendar postedDay = Calendar.getInstance();
            date = df.parse(timeInput);
            postedDay.setTime(date);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy'  'hh:mm a",Locale.ENGLISH);
            long now = System.currentTimeMillis() + myTimeOffset;
            long newtime = postedDay.getTimeInMillis() + myTimeOffset;
            date = new Date(newtime);
            //dates = df.format(date);
            dates = outputFormat.format(date);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return dates;
    }

    public String getCreditCardCharges ()
    {
        return creditCardCharges;
    }

    public void setCreditCardCharges (String creditCardCharges)
    {
        this.creditCardCharges = creditCardCharges;
    }

    public String getSessionType ()
    {
        return sessionType;
    }

    public void setSessionType (String sessionType)
    {
        this.sessionType = sessionType;
    }

    public String getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
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

    public double getPacifyrEarnings ()
    {
        return pacifyrEarnings;
    }

    public String getPacifyrEarningsWithSign (String  country)
    {
        if (country.equals("India"))
            return " ₹ "+pacifyrEarnings;
        else
            return " $ "+pacifyrEarnings;
    }

    public void setPacifyrEarnings (double pacifyrEarnings)
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

    public String getAnonymousCall ()
    {
        return anonymousCall;
    }

    public void setAnonymousCall (String anonymousCall)
    {
        this.anonymousCall = anonymousCall;
    }

    public MUser getPacifyr ()
    {
        return pacifyr;
    }

    public void setPacifyr (MUser pacifyr)
    {
        this.pacifyr = pacifyr;
    }

    public String getScheduledSessionId ()
    {
        return scheduledSessionId;
    }

    public void setScheduledSessionId (String scheduledSessionId)
    {
        this.scheduledSessionId = scheduledSessionId;
    }

    public String getMissed ()
    {
        return missed;
    }

    public void setMissed (String missed)
    {
        this.missed = missed;
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

    @Override
    public String toString()
    {
        return "";
       // return "ClassPojo [sessionCost = "+sessionCost+", connected = "+connected+", endTime = "+endTime+", id = "+id+", startTime = "+startTime+", declined = "+declined+", createdAt = "+createdAt+", userId = "+userId+", afterSessionCustomerEmotionId = "+afterSessionCustomerEmotionId+", customerRating = "+customerRating+", reasonForTheCall = "+reasonForTheCall+", sessionRatings = "+sessionRatings+", creditCardCharges = "+creditCardCharges+", sessionType = "+sessionType+", updatedAt = "+updatedAt+", pacifyrId = "+pacifyrId+", afterSessionPacifyrEmotionId = "+afterSessionPacifyrEmotionId+", duration = "+duration+", pacifyrEarnings = "+pacifyrEarnings+", stopped = "+stopped+", anonymousCall = "+anonymousCall+", pacifyr = "+pacifyr+", scheduledSessionId = "+scheduledSessionId+", missed = "+missed+", user = "+user+", aggregationSessionId = "+aggregationSessionId+"]";
    }


    public boolean isBilledSession() {
        return billedSession;
    }

    public void setBilledSession(boolean billedSession) {
        this.billedSession = billedSession;
    }
}
