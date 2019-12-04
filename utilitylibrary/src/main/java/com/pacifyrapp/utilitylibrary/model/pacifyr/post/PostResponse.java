package com.pacifyrapp.utilitylibrary.model.pacifyr.post;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class PostResponse
{
    private String message;

    private String status;

    private String messageExtended;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getMessageExtended ()
    {
        return messageExtended;
    }

    public void setMessageExtended (String messageExtended)
    {
        this.messageExtended = messageExtended;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", messageExtended = "+messageExtended+"]";
    }
}