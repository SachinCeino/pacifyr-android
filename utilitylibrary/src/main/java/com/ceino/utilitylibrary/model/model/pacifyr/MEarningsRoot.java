package com.ceino.utilitylibrary.model.model.pacifyr;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class MEarningsRoot
{
    private Earnings[] earnings;

    public Earnings[] getEarnings ()
    {
        return earnings;
    }

    public void setEarnings (Earnings[] earnings)
    {
        this.earnings = earnings;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [earnings = "+earnings+"]";
    }



    public class Earnings
    {
        private float earnings;

        private String startDate;

        private String duration;

        private String userId;

        private String endDate;

        public float getEarnings ()
        {
            return earnings;
        }

        public void setEarnings (float earnings)
        {
            this.earnings = earnings;
        }

        public String getStartDate ()
        {
            return startDate;
        }

        public void setStartDate (String startDate)
        {
            this.startDate = startDate;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        public String getEndDate ()
        {
            return endDate;
        }

        public void setEndDate (String endDate)
        {
            this.endDate = endDate;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [earnings = "+earnings+", startDate = "+startDate+", duration = "+duration+", userId = "+userId+", endDate = "+endDate+"]";
        }
    }

}