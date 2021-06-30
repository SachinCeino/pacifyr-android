package com.pacifyrapp.utilitylibrary.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by developer on 4/1/18.
 */

public class ModelListEmotion {

    private Results[] results;

    public Results[] getResults ()
    {
        return results;
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    public void setResults (ArrayList<Results> list)
    {
        if(list!=null)
            this.results=list.toArray( (new Results[list.size()]));
    }

    public ArrayList<Results> getResultsList() {
        ArrayList<Results> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<Results>(Arrays.asList(results));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (Results[] results)
    {
        ArrayList<Results> arrayList = new ArrayList<Results>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new Results[arrayList.size()]));


    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }

    public class Results
    {
        private String updatedAt;

        private String id;

        private String title;

        private String createdAt;

        private String image;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

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

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", title = "+title+", createdAt = "+createdAt+", image = "+image+"]";
        }
    }

}
