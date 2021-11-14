package com.example.bimba.RESTAPI.Midtrans;

import com.google.gson.annotations.SerializedName;

public class ResponseMidtrans {

    @SerializedName("order_id")
    String orderId;

    @SerializedName("status_code")
    int statusCode;

    public ResponseMidtrans() {
    }

    public ResponseMidtrans(String orderId, int statusCode) {
        this.orderId = orderId;
        this.statusCode = statusCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
