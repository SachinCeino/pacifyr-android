package com.ceino.utilitylibrary.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mani-Ceino on 12/15/2017.
 */

public class MSessionRating
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

    public ArrayList<String> getCommentList ()
    {
        ArrayList<String> myList=null;
        try {
           myList = new ArrayList<String>(Arrays.asList(comment.split(",")));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return myList;
    }
    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public void setComments(ArrayList<String> categoryArrayList) {
        if(categoryArrayList!=null)
            if(categoryArrayList.size()>0)
                this.comment =android.text.TextUtils.join(",", categoryArrayList);
        //this.category = categoryArrayList.toArray(new String [categoryArrayList.size()]);
    }

    public String getNotes ()
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
        return "";
        //return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", effectiveness = "+effectiveness+", sessionId = "+sessionId+", createdAt = "+createdAt+", userId = "+userId+", rating = "+rating+", beforeSessionEmotionId = "+beforeSessionEmotionId+", type = "+type+", comment = "+comment+", notes = "+notes+", afterSessionEmotionId = "+afterSessionEmotionId+"]";
    }
}
