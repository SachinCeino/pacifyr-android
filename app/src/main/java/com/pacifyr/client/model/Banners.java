package com.pacifyr.client.model;

import java.util.List;

public class Banners {
    private String bannerUrl;
    private String buttonText;
    private String webinarUrl;
    private String text;
    private List<String> countrys;
    private String type;

    private String userId;

    private int order = Integer.MAX_VALUE;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getWebinarUrl() {
        return webinarUrl;
    }

    public void setWebinarUrl(String webinarUrl) {
        this.webinarUrl = webinarUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCountrys() {
        return countrys;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCountrys(List<String> countrys) {
        this.countrys = countrys;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}