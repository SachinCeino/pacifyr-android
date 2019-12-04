package com.pacifyr.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ceino on 17/4/18.
 */

public class ModelPacifyrCertificates implements Parcelable{

    public ModelPacifyrCertificates(String title, String  year){
        this.title=title;
        this.duration=year;
    }

    private String title;
    private String  duration;


    protected ModelPacifyrCertificates(Parcel in) {
        title = in.readString();
        duration = in.readString();
    }

    public static final Creator<ModelPacifyrCertificates> CREATOR = new Creator<ModelPacifyrCertificates>() {
        @Override
        public ModelPacifyrCertificates createFromParcel(Parcel in) {
            return new ModelPacifyrCertificates(in);
        }

        @Override
        public ModelPacifyrCertificates[] newArray(int size) {
            return new ModelPacifyrCertificates[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(duration);
    }
}
