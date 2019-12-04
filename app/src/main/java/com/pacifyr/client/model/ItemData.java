package com.pacifyr.client.model;

/**
 * Created by ceino on 11/1/18.
 */

public class ItemData {

    private String country;
    private String code;
    private Integer imageId;
    public ItemData(String text, Integer imageId){
        this.country=text;
        this.imageId=imageId;
    }

    public ItemData(String text,String country, Integer imageId){
        this.country=country;
        this.code=text;
        this.imageId=imageId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
