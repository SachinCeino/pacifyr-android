package com.ceino.utilitylibrary.model.pacifyr;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class MAdminDashboardMonth
{
    private String numberOfCalls;

    private String totalDuration;

    private NumberOfSessionsAnalytics[] numberOfSessionsAnalytics;

    private NumberOfMinutesAnalytics[] numberOfMinutesAnalytics;

    private ActiveEmployeesAnalytics[] activeEmployeesAnalytics;

    private String numberOfEmployees;

    public String getNumberOfCalls ()
    {
        return numberOfCalls;
    }

    public void setNumberOfCalls (String numberOfCalls)
    {
        this.numberOfCalls = numberOfCalls;
    }

    public String getTotalDuration ()
    {
        return totalDuration;
    }

    public void setTotalDuration (String totalDuration)
    {
        this.totalDuration = totalDuration;
    }

    public NumberOfSessionsAnalytics[] getNumberOfSessionsAnalytics ()
    {
        return numberOfSessionsAnalytics;
    }

    public void setNumberOfSessionsAnalytics (NumberOfSessionsAnalytics[] numberOfSessionsAnalytics)
    {
        this.numberOfSessionsAnalytics = numberOfSessionsAnalytics;
    }

    public NumberOfMinutesAnalytics[] getNumberOfMinutesAnalytics ()
    {
        return numberOfMinutesAnalytics;
    }

    public void setNumberOfMinutesAnalytics (NumberOfMinutesAnalytics[] numberOfMinutesAnalytics)
    {
        this.numberOfMinutesAnalytics = numberOfMinutesAnalytics;
    }

    public ActiveEmployeesAnalytics[] getActiveEmployeesAnalytics ()
    {
        return activeEmployeesAnalytics;
    }

    public void setActiveEmployeesAnalytics (ActiveEmployeesAnalytics[] activeEmployeesAnalytics)
    {
        this.activeEmployeesAnalytics = activeEmployeesAnalytics;
    }

    public String getNumberOfEmployees ()
    {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees (String numberOfEmployees)
    {
        this.numberOfEmployees = numberOfEmployees;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [numberOfCalls = "+numberOfCalls+", totalDuration = "+totalDuration+", numberOfSessionsAnalytics = "+numberOfSessionsAnalytics+", numberOfMinutesAnalytics = "+numberOfMinutesAnalytics+", activeEmployeesAnalytics = "+activeEmployeesAnalytics+", numberOfEmployees = "+numberOfEmployees+"]";
    }


    public class NumberOfSessionsAnalytics
    {
        private String count;

        private String fromDate;

        private String endDate;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public String getFromDate ()
        {
            return fromDate;
        }

        public void setFromDate (String fromDate)
        {
            this.fromDate = fromDate;
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
            return "ClassPojo [count = "+count+", fromDate = "+fromDate+", endDate = "+endDate+"]";
        }
    }


    public class NumberOfMinutesAnalytics
    {
        private String count;

        private String fromDate;

        private String endDate;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public String getFromDate ()
        {
            return fromDate;
        }

        public void setFromDate (String fromDate)
        {
            this.fromDate = fromDate;
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
            return "ClassPojo [count = "+count+", fromDate = "+fromDate+", endDate = "+endDate+"]";
        }
    }



    public class ActiveEmployeesAnalytics
    {
        private String count;

        private String fromDate;

        private String endDate;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public String getFromDate ()
        {
            return fromDate;
        }

        public void setFromDate (String fromDate)
        {
            this.fromDate = fromDate;
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
            return "ClassPojo [count = "+count+", fromDate = "+fromDate+", endDate = "+endDate+"]";
        }
    }
}