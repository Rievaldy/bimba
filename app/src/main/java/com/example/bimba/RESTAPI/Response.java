package com.example.bimba.RESTAPI;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    public Response() {
    }

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
