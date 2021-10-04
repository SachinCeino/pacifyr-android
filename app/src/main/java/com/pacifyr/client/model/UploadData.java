package com.pacifyr.client.model;

import android.net.Uri;

public class UploadData {

    public UploadData(Uri uri, String type){
        this.uri = uri;
        this.type = type;
    }

    public Uri uri;
    public String type;
}
