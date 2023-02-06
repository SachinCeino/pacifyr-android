package com.pacifyrapp.utilitylibrary.model.model.pacifyr.post;

/**
 * Created by developer on 11/August/2017.

 */

public class PostStopSessionWithPacifyr
{
    private String pacifyrId;

    private String userId;

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

    @Override
    public String toString()
    {
        return "ClassPojo [pacifyrId = "+pacifyrId+", userId = "+userId+"]";
    }
}