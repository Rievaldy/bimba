package com.example.bimba.RESTAPI.Siswa;

import com.example.bimba.Model.Siswa;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseSiswa {

    @SerializedName("message")
    String message;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<Siswa> data;

    public ResponseSiswa(String message, int status, ArrayList<Siswa> data) {
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

    public ArrayList<Siswa> getData() {
        return data;
    }

    public void setData(ArrayList<Siswa> data) {
        this.data = data;
    }
}
