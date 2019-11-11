package com.pacifyr.omhealthcare.event;

/**
 * Created by ceino on 30/1/18.
 */

public class WeekSetSessionEvent {

    private String url;

    private String statUrl;

    private String weekType;

    private String fromDWeek;

    private String toDWeek;
    private boolean editable;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public WeekSetSessionEvent(String url) {
        setUrl(url);
    }

    public WeekSetSessionEvent(String url, String statUrl, String type,String fromDWeek,String toDWeek) {
        setUrl(url);
        setStatUrl(statUrl);
        setWeekType(type);
        setFromDWeek(fromDWeek);
        setToDWeek(toDWeek);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatUrl() {
        return statUrl;
    }

    public void setStatUrl(String statUrl) {
        this.statUrl = statUrl;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public String getFromDWeek() {
        return fromDWeek;
    }

    public void setFromDWeek(String fromDWeek) {
        this.fromDWeek = fromDWeek;
    }

    public String getToDWeek() {
        return toDWeek;
    }

    public void setToDWeek(String toDWeek) {
        this.toDWeek = toDWeek;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
