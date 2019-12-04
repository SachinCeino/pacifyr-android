package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ceino on 8/3/18.
 */

public class Education implements Parcelable
{
    private String id;

    private String title;

    private String duration;

    private Integer order;

    private String location;

    private String userId;

    public Education(String duration,String location,String title,Integer order){
        this.duration = duration;
        this.location = location;
        this.title = title;
        this.order = order;
    }

    protected Education(Parcel in) {
        id = in.readString();
        title = in.readString();
        duration = in.readString();
        order = in.readInt();
        location = in.readString();
        userId = in.readString();
    }

    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };

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

    public Integer getOrder ()
    {
        return order;
    }

    public void setOrder (Integer order)
    {
        this.order = order;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
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
        return "ClassPojo [id = "+id+", title = "+title+", duration = "+duration+", order = "+order+", location = "+location+", userId = "+userId+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeInt(order);
        dest.writeString(location);
        dest.writeString(userId);
    }
}