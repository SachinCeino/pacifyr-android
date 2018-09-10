package com.ceino.utilitylibrary.model.model.pacifyr;

/**
 * Created by ceino on 25/1/18.
 */

public class MRatingStat
{
    private String averageRating;

    private String numberOfRatings;

    public String getAverageRating ()
    {
        return averageRating;
    }

    public void setAverageRating (String averageRating)
    {
        this.averageRating = averageRating;
    }

    public String getNumberOfRatings ()
    {
        return numberOfRatings;
    }

    public void setNumberOfRatings (String numberOfRatings)
    {
        this.numberOfRatings = numberOfRatings;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [averageRating = "+averageRating+", numberOfRatings = "+numberOfRatings+"]";
    }
}
