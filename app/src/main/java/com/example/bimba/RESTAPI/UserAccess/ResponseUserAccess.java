package com.example.bimba.RESTAPI.UserAccess;

import com.example.bimba.Model.UserAccess;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUserAccess {
    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    List<UserAccess> data;

    public ResponseUserAccess() {
    }

    public ResponseUserAccess(String message, int status, List<UserAccess> data) {
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

    public List<UserAccess> getData() {
        return data;
    }

    public void setData(List<UserAccess> data) {
        this.data = data;
    }
}
