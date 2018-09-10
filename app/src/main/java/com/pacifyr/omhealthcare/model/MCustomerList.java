package com.pacifyr.omhealthcare.model;

import com.ceino.utilitylibrary.model.pacifyr.MUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MCustomerList
{
    private Results[] results;

    public Results[] getResults ()
    {
        return results;
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

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }

    public class Results
    {
        private String id;

        private String averageRating;

        private MUser user;

        private String totalSpending;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAverageRating ()
        {
            return averageRating;
        }

        public void setAverageRating (String averageRating)
        {
            this.averageRating = averageRating;
        }

        public MUser getUser ()
        {
            return user;
        }

        public void setUser (MUser user)
        {
            this.user = user;
        }

        public String getTotalSpending ()
        {
            return totalSpending;
        }

        public String getPrecisedTotalSpending ()
        {
            DecimalFormat format = new DecimalFormat("00.00");
            return format.format(totalSpending);
        }

        public void setTotalSpending (String totalSpending)
        {
            this.totalSpending = totalSpending;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", averageRating = "+averageRating+", user = "+user+", totalSpending = "+totalSpending+"]";
        }
    }
}