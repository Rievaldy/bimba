package com.example.bimba.RESTAPI.User;

import com.example.bimba.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUser {

    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    List<User> data;

    public ResponseUser() {
    }

    public ResponseUser(String message, int status, List<User> data) {
        this.message = message;
        this.status = status;
        this.data = data;
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
