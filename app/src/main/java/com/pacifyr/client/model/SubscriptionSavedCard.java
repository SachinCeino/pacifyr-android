package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionSavedCard implements Serializable
{

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("cardName")
    @Expose
    private Object cardName;
    @SerializedName("cardExpiryYear")
    @Expose
    private Integer cardExpiryYear;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("stripeCustomerId")
    @Expose
    private String stripeCustomerId;
    @SerializedName("cardBrand")
    @Expose
    private String cardBrand;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("cardExpiryMonth")
    @Expose
    private Integer cardExpiryMonth;
    @SerializedName("cardLast4")
    @Expose
    private Integer cardLast4;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getCardName() {
        return cardName;
    }

    public void setCardName(Object cardName) {
        this.cardName = cardName;
    }

    public Integer getCardExpiryYear() {
        return cardExpiryYear;
    }

    public void setCardExpiryYear(Integer cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public void setCardExpiryMonth(Integer cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    public Integer getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(Integer cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
