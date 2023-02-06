package com.pacifyrapp.utilitylibrary.model.pacifyr;

/**
 * Created by developer on 5/1/18.
 */

public class ModelStatistics
{
    private String earnings;

    private String totalSessions;

    private String duration;

    private String numberOfDeclinedCalls;

    private String spendings;

    private String numberOfMissedCalls;

    private String availableMins;

    public String getEarnings ()
    {
        return earnings;
    }

    public void setEarnings (String earnings)
    {
        this.earnings = earnings;
    }

    public String getTotalSessions ()
    {
        return totalSessions;
    }

    public void setTotalSessions (String totalSessions)
    {
        this.totalSessions = totalSessions;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getNumberOfDeclinedCalls ()
    {
        return numberOfDeclinedCalls;
    }

    public void setNumberOfDeclinedCalls (String numberOfDeclinedCalls)
    {
        this.numberOfDeclinedCalls = numberOfDeclinedCalls;
    }

    public String getSpendings ()
    {
        Double f = Double.parseDouble(spendings);
        return String.format("%.2f", f);
    }

    public void setSpendings (String spendings)
    {
        this.spendings = spendings;
    }

    public String getNumberOfMissedCalls ()
    {
        return numberOfMissedCalls;
    }

    public void setNumberOfMissedCalls (String numberOfMissedCalls)
    {
        this.numberOfMissedCalls = numberOfMissedCalls;
    }

    public String getAvailableMins ()
    {
        return availableMins;
    }

    public void setAvailableMins (String availableMins)
    {
        this.availableMins = availableMins;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [earnings = "+earnings+", totalSessions = "+totalSessions+", duration = "+duration+", numberOfDeclinedCalls = "+numberOfDeclinedCalls+", spendings = "+spendings+", numberOfMissedCalls = "+numberOfMissedCalls+", availableMins = "+availableMins+"]";
    }
}