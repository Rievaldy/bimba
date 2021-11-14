package com.example.bimba.RESTAPI.Tunggakan;

import com.example.bimba.Model.Tunggakan;
import com.example.bimba.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseTunggakan {

    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<CompleteTunggakan> data;

    public ResponseTunggakan() {
    }

    public ResponseTunggakan(String message, int status, ArrayList<CompleteTunggakan> data) {
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

    public ArrayList<CompleteTunggakan> getData() {
        return data;
    }

    public void setData(ArrayList<CompleteTunggakan> data) {
        this.data = data;
    }
}
