package com.pacifyr.omhealthcare.event;

/**
 * Created by ceino on 30/1/18.
 */

public class WeekSetEvent {

    private String url;

    private String statUrl;

    private String weekType;

    public WeekSetEvent(String url) {
        setUrl(url);
    }

    public WeekSetEvent(String url, String statUrl, String type) {
        setUrl(url);
        setStatUrl(statUrl);
        setWeekType(type);
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
}
