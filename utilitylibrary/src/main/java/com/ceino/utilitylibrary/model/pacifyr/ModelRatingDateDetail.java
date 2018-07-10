package com.ceino.utilitylibrary.model.pacifyr;

/**
 * Created by ceino on 25/1/18.
 */

public class ModelRatingDateDetail {

    private String week;
    private String type;
    private String ratingUrl;
    private String toDate;
    private String fromDate;
    private boolean isSelect = false;
    private String fromDateDisplay;
    private String toDateDisplay;
    private String statUrl;

    public ModelRatingDateDetail() {
    }

    public ModelRatingDateDetail(String month, String startDate, String endDate, String type, String data) {
        this.week = month;
        this.type = type;
        this.ratingUrl = data;
    }

    public String getMonth() {
        return week;
    }

    public void setMonth(String month) {
        this.week = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRatingUrl() {
        return ratingUrl;
    }

    public void setRatingUrl(String data) {
        this.ratingUrl = data;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDateDisplay() {
        return fromDateDisplay;
    }

    public void setFromDateDisplay(String fromDateDisplay) {
        this.fromDateDisplay = fromDateDisplay;
    }

    public String getToDateDisplay() {
        return toDateDisplay;
    }

    public void setToDateDisplay(String toDateDisplay) {
        this.toDateDisplay = toDateDisplay;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getStatUrl() {
        return statUrl;
    }

    public void setStatUrl(String statUrl) {
        this.statUrl = statUrl;
    }
}
