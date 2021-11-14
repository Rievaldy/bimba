package com.example.bimba.RESTAPI.Biaya;

import com.example.bimba.Model.ListBiaya;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseBiaya {

    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<ListBiaya> data;

    public ResponseBiaya() {
    }

    public ResponseBiaya(String message, int status, ArrayList<ListBiaya> data) {
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

    public ArrayList<ListBiaya> getData() {
        return data;
    }

    public void setData(ArrayList<ListBiaya> data) {
        this.data = data;
    }
}
