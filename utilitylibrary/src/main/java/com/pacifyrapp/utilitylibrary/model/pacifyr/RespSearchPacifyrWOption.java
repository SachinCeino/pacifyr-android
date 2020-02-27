package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class RespSearchPacifyrWOption
{
    private MUser[] results;

    public MUser[] getResults ()
    {
        return results;
    }

    public void setResults (MUser[] results)
    {
        this.results = results;
    }


    public void setResults (ArrayList<MUser> list)
    {

        if(list!=null)
            this.results=list.toArray( (new MUser[list.size()]));
    }

    public ArrayList<MUser> getResultsList() {
        ArrayList<MUser> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<MUser>(Arrays.asList(results));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (MUser[] results)
    {
        ArrayList<MUser> arrayList = new ArrayList<MUser>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new MUser[arrayList.size()]));


    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }




}