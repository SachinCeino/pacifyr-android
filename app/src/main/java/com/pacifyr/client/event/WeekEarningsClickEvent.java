package com.pacifyr.client.event;

/**

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
