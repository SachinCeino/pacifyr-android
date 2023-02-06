package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

/**
 * Created by JAIGISH on 16-02-2018.
 */

public class Mbonus {

    public  Integer toBonus;
    public String message;
    public Integer hoursThisWeek;

    public Mbonus() {
    }


    public Mbonus(Integer toBonus, String message, Integer hoursThisWeek) {
        this.toBonus = toBonus;
        this.message = message;
        this.hoursThisWeek = hoursThisWeek;
    }

    public Integer getToBonus() {
        return toBonus;
    }

    public String getMessage() {
        return message;
    }

    public Integer getHoursThisWeek() {
        return hoursThisWeek;
    }

    public void setToBonus(Integer toBonus) {
        this.toBonus = toBonus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHoursThisWeek(Integer hoursThisWeek) {
        this.hoursThisWeek = hoursThisWeek;
    }
}
