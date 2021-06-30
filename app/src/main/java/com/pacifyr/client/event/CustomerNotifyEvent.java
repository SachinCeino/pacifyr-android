package com.pacifyr.client.event;

import com.androidquery.callback.AjaxStatus;

/**

 */
public class CustomerNotifyEvent {

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


    //public  Feed.Results[] results=null;
   // public  User[] users=null;
   /* public NotifyEvent( Channel.Results channel){

        this.isSuccess=false;
        this.channel=channel;
        this.classname=null;

    }*/
    public CustomerNotifyEvent(Boolean isSuccess){

        this.isSuccess=isSuccess;

        this.status=status;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
        this.isUpdate=false;

    }
    public CustomerNotifyEvent(Boolean isSuccess, Exception e){

        this.isSuccess=isSuccess;
        this.e=e;
        this.status=status;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
        this.isUpdate=false;
    }
    public CustomerNotifyEvent(Boolean isSuccess, AjaxStatus status){

        this.isSuccess=isSuccess;
        this.status=status;
        this.e=e;
        this.isUpdate=false;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public CustomerNotifyEvent(Boolean isSuccess, Boolean isPaged){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.e=e;
        this.isUpdate=false;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public CustomerNotifyEvent(Boolean isSuccess, Boolean isPaged, Boolean isUpdate){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.isUpdate=isUpdate;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }
    public CustomerNotifyEvent(Boolean isSuccess, Boolean isPaged, Boolean isUpdate, Boolean isBidhistoryDeleted){

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

    public CustomerNotifyEvent(){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }

    public CustomerNotifyEvent(int height, int width){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.height=height;
        this.width=width;
    }

    public CustomerNotifyEvent(int photolistsize){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=null;
        this.photolistsize=photolistsize;

    }


    public CustomerNotifyEvent(String pririty){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
        this.priority=pririty;
        this.height=height;
        this.width=width;
    }
}