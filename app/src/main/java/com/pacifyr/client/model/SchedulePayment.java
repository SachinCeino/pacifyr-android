package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sachin Thampan on 28/10/19.
 */
public class SchedulePayment {
    @SerializedName("bookingRequestPayment")
    @Expose
    private BookingRequestPayment bookingRequestPayment;
    @SerializedName("status")
    @Expose
    private String status;

    public BookingRequestPayment getBookingRequestPayment() {
        return bookingRequestPayment;
    }

    public void setBookingRequestPayment(BookingRequestPayment bookingRequestPayment) {
        this.bookingRequestPayment = bookingRequestPayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
