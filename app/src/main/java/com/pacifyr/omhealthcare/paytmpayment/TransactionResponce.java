package com.pacifyr.omhealthcare.paytmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Belal on 1/10/2018.
 */

public class TransactionResponce {

    @SerializedName("isChecksumValid")
    @Expose
    private Boolean isChecksumValid;
    @SerializedName("transactionStatus")
    @Expose
    private String transactionStatus;
    @SerializedName("transactionResponse")
    @Expose
    private String transactionResponse;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;

    public Boolean getIsChecksumValid() {
        return isChecksumValid;
    }

    public void setIsChecksumValid(Boolean isChecksumValid) {
        this.isChecksumValid = isChecksumValid;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionResponse() {
        return transactionResponse;
    }

    public void setTransactionResponse(String transactionResponse) {
        this.transactionResponse = transactionResponse;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
