package com.pacifyr.client.event;

/**
 * Created by ceino on 30/1/18.
 */

public class MonthRatingSetEvent {

    private String url;

    private String statUrl;

    public MonthRatingSetEvent(String url) {
        setUrl(url);
    }

    public MonthRatingSetEvent(String url, String statUrl) {
        setUrl(url);
        setStatUrl(statUrl);
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
}
