package com.pacifyr.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**

 */

public class ModelPacifyrEducation implements Parcelable{

    public ModelPacifyrEducation(String collage,String  location,String from,String to,Integer order){
        this.title=collage;
        this.location=location;
        this.duration = from +" to "+to;
        this.order=order;
       /* this.from=from;
        this.to=to;*/
    }

    private String title;
    private String  location;
    private String duration;
    private Integer order;

    protected ModelPacifyrEducation(Parcel in) {
        title = in.readString();
        location = in.readString();
        duration =in.readString();
       /* from = in.readString();
        to = in.readString();*/
    }


    public static final Creator<ModelPacifyrEducation> CREATOR = new Creator<ModelPacifyrEducation>() {
        @Override
        public ModelPacifyrEducation createFromParcel(Parcel in) {
            return new ModelPacifyrEducation(in);
        }

        @Override
        public ModelPacifyrEducation[] newArray(int size) {
            return new ModelPacifyrEducation[size];
        }
    };

    public String getCollage() {
        return title;
    }

    public void setCollage(String collage) {
        this.title = collage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(duration);
        dest.writeInt(order);
        //dest.writeString(to);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
