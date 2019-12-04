package com.pacifyrapp.utilitylibrary.model.model.pacifyr.post;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class PostRatePacifyr
{
    private String pacifyrId;

    private String userId;

    private String rating;

    public String getPacifyrId ()
    {
        return pacifyrId;
    }

    public void setPacifyrId (String pacifyrId)
    {
        this.pacifyrId = pacifyrId;
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

    @Override
    public String toString()
    {
        return "ClassPojo [pacifyrId = "+pacifyrId+", userId = "+userId+", rating = "+rating+"]";
    }
}