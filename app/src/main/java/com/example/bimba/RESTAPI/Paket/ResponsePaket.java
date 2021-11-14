package com.example.bimba.RESTAPI.Paket;

import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.ListBiaya;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePaket {
    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<JenisPaket> data;

    public ResponsePaket() {
    }

    public ResponsePaket(String message, int status, ArrayList<JenisPaket> data) {
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

    public ArrayList<JenisPaket> getData() {
        return data;
    }

    public void setData(ArrayList<JenisPaket> data) {
        this.data = data;
    }
}
