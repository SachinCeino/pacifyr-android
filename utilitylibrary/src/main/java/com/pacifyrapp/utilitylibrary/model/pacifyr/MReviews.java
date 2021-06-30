package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**

 */

public class MReviews
{
    private Results[] results;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    public void setResults (ArrayList<Results> list)
    {

        if(list!=null)
            this.results=list.toArray( (new Results[list.size()]));
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
        ArrayList<Results> arrayList = new ArrayList<Results>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new Results[arrayList.size()]));
    }



    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }



    public class Results
    {
        private String updatedAt;

        private String id;

        private String effectiveness;

        private String sessionId;

        private String createdAt;

        private String userId;

        private String rating;

        private String beforeSessionEmotionId;

        private String type;

        private String comment;

        private String notes;

        private String afterSessionEmotionId;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getFormatedUpdateatTime(){
            String time = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'");
                Date parsedTime = sdf.parse(getUpdatedAt());
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy'  'hh:mm a");
                time = sdf2.format(parsedTime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return time;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getEffectiveness ()
        {
            return effectiveness;
        }

        public void setEffectiveness (String effectiveness)
        {
            this.effectiveness = effectiveness;
        }

        public String getSessionId ()
        {
            return sessionId;
        }

        public void setSessionId (String sessionId)
        {
            this.sessionId = sessionId;
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

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        public String getBeforeSessionEmotionId ()
        {
            return beforeSessionEmotionId;
        }

        public void setBeforeSessionEmotionId (String beforeSessionEmotionId)
        {
            this.beforeSessionEmotionId = beforeSessionEmotionId;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public String getComment ()
        {
            return comment;
        }

        public void setComment (String comment)
        {
            this.comment = comment;
        }

        public String  getNotes ()
        {
            return notes;
        }

        public void setNotes (String notes)
        {
            this.notes = notes;
        }

        public String getAfterSessionEmotionId ()
        {
            return afterSessionEmotionId;
        }

        public void setAfterSessionEmotionId (String afterSessionEmotionId)
        {
            this.afterSessionEmotionId = afterSessionEmotionId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", effectiveness = "+effectiveness+", sessionId = "+sessionId+", createdAt = "+createdAt+", userId = "+userId+", rating = "+rating+", beforeSessionEmotionId = "+beforeSessionEmotionId+", type = "+type+", comment = "+comment+", notes = "+notes+", afterSessionEmotionId = "+afterSessionEmotionId+"]";
        }
    }
}