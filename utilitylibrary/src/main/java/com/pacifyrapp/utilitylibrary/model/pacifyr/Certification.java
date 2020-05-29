package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ceino on 8/3/18.
 */

public class Certification implements Serializable
{
    private String id;

    private String title;

    private String duration;

    private String userId;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    private List<Object> documents = null;

    public List<Object> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Object> documents) {
        this.documents = documents;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+", duration = "+duration+", userId = "+userId+"]";
    }
}