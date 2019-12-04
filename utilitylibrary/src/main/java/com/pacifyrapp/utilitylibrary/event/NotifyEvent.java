package com.pacifyrapp.utilitylibrary.event;


import com.androidquery.callback.AjaxStatus;

/**
 * Created by ceino on 12/10/15.
 */
public class NotifyEvent {
    //8d,b8
    public Boolean isSuccess=true;
    public Boolean isPaged=false;
    //public Channel.Results channel=null;
    public Exception e=null;
 AjaxStatus status=null;
    String classname =null;
    public  String priority =null;
   // public Message message =null;
    public int height, width;
    public  int photolistsize =0;


    //public  Feed.Results[] results=null;
   // public  User[] users=null;
   /* public NotifyEvent( Channel.Results channel){

        this.isSuccess=false;
        this.channel=channel;
        this.classname=null;

    }*/
   /* public NotifyEvent(Boolean isSuccess,Message message){

        this.isSuccess=isSuccess;
      //  this.message=message;
        this.status=status;
        this.e=e;
        this.classname=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;

    }*/
    public NotifyEvent(Boolean isSuccess,Exception e){

        this.isSuccess=isSuccess;
        this.e=e;
        this.status=status;
        this.classname=null;
        this.priority=null;
       // this.message=null;
        this.height=-100;
        this.width=-100;
    }
public NotifyEvent(Boolean isSuccess,AjaxStatus status){

        this.isSuccess=isSuccess;
        this.status=status;
        this.e=e;
        this.classname=null;
        this.priority=null;
       // this.message=null;
        this.height=-100;
        this.width=-100;
    }
    public NotifyEvent(Boolean isSuccess,Boolean isPaged){

        this.isSuccess=isSuccess;
        this.isPaged=isPaged;
        this.e=e;
        this.classname=null;
        this.priority=null;
      //  this.message=null;
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
   public NotifyEvent(Boolean isSuccess){

       this.isSuccess=isSuccess;
       this.status=null;
       this.e=null;
       this.classname=null;
       this.priority=null;
      // this.message=null;
       this.height=-100;
       this.width=-100;
   }
    public NotifyEvent(){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
       // this.message=null;
        this.priority=null;
        this.height=-100;
        this.width=-100;
    }

    public NotifyEvent(int height, int width){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
      //  this.message=null;
        this.priority=null;
        this.height=height;
        this.width=width;
    }

    public NotifyEvent(int photolistsize){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
       // this.message=null;
        this.priority=null;
        this.photolistsize=photolistsize;

    }


    public NotifyEvent(String pririty){
        this.isSuccess=true;
        this.e=null;
        this.status=null;
      //  this.message=null;
        this.priority=pririty;
        this.height=height;
        this.width=width;
    }
}