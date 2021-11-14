package com.example.bimba.RESTAPI.HistoryPembayaran;

import com.example.bimba.Model.HistoryPembayaran;
import com.example.bimba.Model.Tunggakan;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseHistoryPembayaran {

    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<CompleteHistoryPembayaran> data;

    public ResponseHistoryPembayaran() {
    }

    public ResponseHistoryPembayaran(String message, int status, ArrayList<CompleteHistoryPembayaran> data) {
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

    public ArrayList<CompleteHistoryPembayaran> getData() {
        return data;
    }

    public void setData(ArrayList<CompleteHistoryPembayaran> data) {
        this.data = data;
    }
}
