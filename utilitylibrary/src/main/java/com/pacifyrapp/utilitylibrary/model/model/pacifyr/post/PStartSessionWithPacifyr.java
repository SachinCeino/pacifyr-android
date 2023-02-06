package com.pacifyrapp.utilitylibrary.model.model.pacifyr.post;

/**

 */


public class PStartSessionWithPacifyr
{
    private String pacifyrId;

    private String userId;

    private String sessionType;

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

    public String getSessionType ()
    {
        return sessionType;
    }

    public void setSessionType (String sessionType)
    {
        this.sessionType = sessionType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pacifyrId = "+pacifyrId+", userId = "+userId+", sessionType = "+sessionType+"]";
    }
}
