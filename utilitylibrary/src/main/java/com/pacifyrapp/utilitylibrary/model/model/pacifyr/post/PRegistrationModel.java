package com.pacifyrapp.utilitylibrary.model.model.pacifyr.post;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

/**
 * Created by ceino on 27/6/17.
 */

public class PRegistrationModel {

    private String accessToken;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    private MUser user;

    public String getAccessToken ()
    {
        return accessToken;
    }

    public void setAccessToken (String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public MUser getUser ()
    {
        return user;
    }

    public void setUser (MUser user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [accessToken = "+accessToken+", status = "+status+", user = "+user+"]";
    }

}
