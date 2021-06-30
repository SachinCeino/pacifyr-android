package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by developer on 11/August/2017.

 */

public class MListNegativeEmotions {

    private com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[] results;

    public com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[] getResults ()
    {
        return results;
    }

    public void setResults (com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }

    public void setResults (ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion> list)
    {
        if(list!=null)
            this.results=list.toArray( (new com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[list.size()]));
    }

    public ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion> getResultsList() {
        ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion>(Arrays.asList(results));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[] results)
    {
        ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion> arrayList = new ArrayList<com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion[arrayList.size()]));


    }


 /*   public class Results
    {
        private String updatedAt;

        private String id;

        private String title;

        private String createdAt;

        private String image;

        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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
    }*/
}
