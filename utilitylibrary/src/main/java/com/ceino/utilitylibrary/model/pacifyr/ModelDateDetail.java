package com.ceino.utilitylibrary.model.pacifyr;

/**
 * Created by ceino on 25/1/18.
 */

public class ModelDateDetail {

    private String month;
    private String startDate;
    private String endDate;
    private String type;
    private String data;

    public ModelDateDetail() {
    }

    public ModelDateDetail(String month, String startDate, String endDate, String type, String data) {
        this.month = month;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.data = data;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
