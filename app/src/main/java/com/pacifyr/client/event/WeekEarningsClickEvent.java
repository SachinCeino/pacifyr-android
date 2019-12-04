package com.pacifyr.client.event;

/**
 * Created by ceino on 30/1/18.
 */

public class WeekEarningsClickEvent {

    private String url;

    public WeekEarningsClickEvent(String url) {
        setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
