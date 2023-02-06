package com.pacifyr.client.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.pacifyr.client.R;
import com.pacifyr.client.model.Country;

import java.util.ArrayList;

/**

 */

public class CountrySpinnerAdapter extends ArrayAdapter<Country> {

    public

    int groupid;
    Activity context;
    ArrayList<Country> list;
    LayoutInflater inflater;
    public CountrySpinnerAdapter(Activity context, int groupid, int id, ArrayList<Country>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(final int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        final ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        //TextView textView=(TextView)itemView.findViewById(R.id.txt);
        // textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }
}
