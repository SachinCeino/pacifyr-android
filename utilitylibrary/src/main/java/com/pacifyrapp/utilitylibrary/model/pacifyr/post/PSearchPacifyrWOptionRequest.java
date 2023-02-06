package com.pacifyrapp.utilitylibrary.model.pacifyr.post;

/**
 * Created by developer on 11/August/2017.
 */

public class PSearchPacifyrWOptionRequest
{
    private String[] specialities;

    private String price_end;

    private String price_start;

    private String gender;

    private String initiatorId;

    public String[] getSpecialities ()
    {
        return specialities;
    }

    public void setSpecialities (String[] specialities)
    {
        this.specialities = specialities;
    }

    public String getPrice_end ()
    {
        return price_end;
    }

    public void setPrice_end (String price_end)
    {
        this.price_end = price_end;
    }

    public String getPrice_start ()
    {
        return price_start;
    }

    public void setPrice_start (String price_start)
    {
        this.price_start = price_start;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getInitiatorId ()
    {
        return initiatorId;
    }

    public void setInitiatorId (String initiatorId)
    {
        this.initiatorId = initiatorId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [specialities = "+specialities+", price_end = "+price_end+", price_start = "+price_start+", gender = "+gender+", initiatorId = "+initiatorId+"]";
    }
}