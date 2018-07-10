package com.ceino.utilitylibrary.model.pacifyr.post;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class PostRateCustomerSession
{
    private String effectiveness;

    private String sessionId;

    private String userId;

    private String rating;

    private String comment;

    private String afterSessionEmotionId;

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

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
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
        return "ClassPojo [effectiveness = "+effectiveness+", sessionId = "+sessionId+", userId = "+userId+", rating = "+rating+", comment = "+comment+", afterSessionEmotionId = "+afterSessionEmotionId+"]";
    }
}