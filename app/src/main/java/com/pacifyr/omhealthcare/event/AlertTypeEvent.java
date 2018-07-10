package com.pacifyr.omhealthcare.event;

import java.util.Date;

/**
 * Created by ceino on 21/3/17.
 */

public class AlertTypeEvent {

    String id,type,desc;
    String collection;
    public Date receiveddate;
   public  boolean isSilentNotify=false;

    public AlertTypeEvent(String id,  String desc,String type, String collection) {
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.collection = collection;
        this.receiveddate = new Date();
    }

    public AlertTypeEvent(boolean isSilentNotify,String collection) {
        this.id = null;

        this.desc = null;
        this.collection = collection;
        this.receiveddate = new Date();
        this.isSilentNotify = isSilentNotify;
    }
    public AlertTypeEvent(boolean isSilentNotify,String type,String collection) {
        this.id = null;
        this.type = type;
        this.desc = null;
        this.collection = collection;
        this.receiveddate = new Date();
        this.isSilentNotify = isSilentNotify;
    }
    public AlertTypeEvent(String id, String type) {
        this.id = id;
        this.type = type;
        this.receiveddate = new Date();
    }

    public AlertTypeEvent(String id, String desc, String type) {
        this.id = id;
        this.desc = desc;
        this.type = type;
        this.receiveddate = new Date();
    }

   /* public AlertTypeEvent(String id, String desc, String type, String collection) {
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.collection = collection;
    }*/

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return id;
    }

    public void setTitle(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
