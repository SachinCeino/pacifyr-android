package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ceino on 24/1/18.
 */

public class MPastSessions
{
    private Results[] results;
    private long total_count;
    public Results[] getResults ()
    {
        return results;
    }

    public ArrayList<Results> getResultsList() {
        ArrayList<Results> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<Results>(Arrays.asList(results));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (Results[] results)
    {
        ArrayList<Results> arrayList =  new ArrayList<Results>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new Results[arrayList.size()]));


    }


    public long getTotal_count ()
    {
        return total_count;
    }

    public void setTotal_count (long total_count)
    {
        this.total_count = total_count;
    }
    public void setResults (Results[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }



    public class Results
    {
        private String sessionCost;

        private String connected;

        private String endTime;

        private String id;

        private String startTime;

        private boolean declined;

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

        private String pacifyrEarnings;

        private String stopped;

        private String anonymousCall;

        private MUser pacifyr;

        private String  scheduledSessionId;

        private boolean missed;

        private MUser user;

        private String  aggregationSessionId;
        private boolean billedSession;

        public boolean isDeclined() {
            return declined;
        }

        public boolean isMissed() {
            return missed;
        }

        public boolean isBilledSession() {
            return billedSession;
        }

        public void setBilledSession(boolean billedSession) {
            this.billedSession = billedSession;
        }

        public String getSessionCost ()
        {
            return sessionCost;
        }

        public void setSessionCost (String sessionCost)
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

        public boolean getDeclined ()
        {
            return declined;
        }

        public void setDeclined (boolean declined)
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

        public void setSessionRatings (MSessionRating[] sessionRatings)
        {
            this.sessionRatings = sessionRatings;
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

        public boolean getMissed ()
        {
            return missed;
        }

        public void setMissed (boolean missed)
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
            return "ClassPojo [sessionCost = "+sessionCost+", connected = "+connected+", endTime = "+endTime+", id = "+id+", startTime = "+startTime+", declined = "+declined+", createdAt = "+createdAt+", userId = "+userId+", afterSessionCustomerEmotionId = "+afterSessionCustomerEmotionId+", customerRating = "+customerRating+", reasonForTheCall = "+reasonForTheCall+", sessionRatings = "+sessionRatings+", creditCardCharges = "+creditCardCharges+", sessionType = "+sessionType+", updatedAt = "+updatedAt+", pacifyrId = "+pacifyrId+", afterSessionPacifyrEmotionId = "+afterSessionPacifyrEmotionId+", duration = "+duration+", pacifyrEarnings = "+pacifyrEarnings+", stopped = "+stopped+", anonymousCall = "+anonymousCall+", pacifyr = "+pacifyr+", scheduledSessionId = "+scheduledSessionId+", missed = "+missed+", user = "+user+", aggregationSessionId = "+aggregationSessionId+"]";
        }
    }
}