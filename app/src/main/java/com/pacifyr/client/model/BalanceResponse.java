package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResponse {

    public static transient String TYPE_WALLET = "walletBalance";
    public static transient String TYPE_SESSION_PURCHASED = "sessionPackPurchased";

    @SerializedName("numberOfFreeSessions")
    @Expose
    private Integer numberOfFreeSessions;
    @SerializedName("numberOfRemainingBundleSessions")
    @Expose
    private Integer numberOfRemainingBundleSessions;
    @SerializedName("type")
    @Expose
    private String type; //sessionPackPurchased, walletBalance
    @SerializedName("remainingWalletBalances")
    @Expose
    private Double remainingWalletBalances;
    @SerializedName("availableFreeMinutes")
    @Expose
    private Integer availableFreeMinutes;

    public Integer getNumberOfFreeSessions() {
        return numberOfFreeSessions;
    }

    public void setNumberOfFreeSessions(Integer numberOfFreeSessions) {
        this.numberOfFreeSessions = numberOfFreeSessions;
    }

    public Integer getNumberOfRemainingBundleSessions() {
        return numberOfRemainingBundleSessions;
    }

    public void setNumberOfRemainingBundleSessions(Integer numberOfRemainingBundleSessions) {
        this.numberOfRemainingBundleSessions = numberOfRemainingBundleSessions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRemainingWalletBalances() {
        return remainingWalletBalances;
    }

    public void setRemainingWalletBalances(Double remainingWalletBalances) {
        this.remainingWalletBalances = remainingWalletBalances;
    }

    public Integer getAvailableFreeMinutes() {
        return availableFreeMinutes;
    }

    public void setAvailableFreeMinutes(Integer availableFreeMinutes) {
        this.availableFreeMinutes = availableFreeMinutes;
    }

}