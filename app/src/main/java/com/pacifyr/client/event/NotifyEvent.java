package com.pacifyr.client.event;

import com.androidquery.callback.AjaxStatus;
import com.pacifyr.client.model.ModelSpecialityByEmotion;

/**
 * Created by ceino on 12/10/15.
 */
public class NotifyEvent {

    public String emotionType;
    public Boolean isSuccess=true;
    public Boolean isPaged=false;
    public Boolean isUpdate=false;
    public Boolean isBidhistoryDeleted=false;
    //public Channel.Results channel=null;
    public Exception e=null;
    public  String priority =null;

    public int height, width;
    public  int photolistsize =0;
    AjaxStatus status=null;
    String classname =null;

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ModelSpecialityByEmotion specialityByEmotion;


    //public  Feed.Results[] results=null;
   // public  User[] users=null;
   /* public NotifyEvent( Channel.Results channel){

        this.isSuccess=false;
        this.channel=channel;
        this.classname=null;

    }*/
    public NotifyEvent(Boolean isSuccess){

        this.isSuccess=isSuccess;

        this.status=status;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
        this.isUpdate=false;

    }
    public NotifyEvent(Boolean isSuccess,Exception e){

        this.isSuccess=isSuccess;
        this.e=e;
        this.status=status;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
        this.isUpdate=false;
    }
    public NotifyEvent(Boolean isSuccess,AjaxStatus status){

        this.isSuccess=isSuccess;
        this.status=status;
        this.e=e;
        this.isUpdate=false;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public NotifyEvent(Boolean isSuccess,Boolean isPaged){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.e=e;
        this.isUpdate=false;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public NotifyEvent(Boolean isSuccess,Boolean isPaged,Boolean isUpdate){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.isUpdate=isUpdate;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public NotifyEvent(Boolean isSuccess,Boolean isPaged,Boolean isUpdate,Boolean isBidhistoryDeleted){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.isUpdate=isUpdate;
        this.isBidhistoryDeleted=isBidhistoryDeleted;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
   /* public NotifyEvent(Boolean isSuccess,Boolean isPaged,Feed.Results[] results){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.e=e;
        this.results=results;
        this.classname=null;
    }
    public NotifyEvent(Boolean isSuccess,Boolean isPaged,User[] users){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.e=e;
        this.users=users;
        this.classname=classname;
    }*/

    public NotifyEvent(){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }

    public NotifyEvent(int height, int width){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.height=height;
        this.width=width;
    }

    public NotifyEvent(int photolistsize){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.photolistsize=photolistsize;

    }


    public NotifyEvent(String pririty){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=pririty;
        this.height=height;
        this.width=width;
    }

    public NotifyEvent(ModelSpecialityByEmotion modelSpecialityByEmotion,String type) {
        this.specialityByEmotion = modelSpecialityByEmotion;
        this.emotionType = type;
    }
}