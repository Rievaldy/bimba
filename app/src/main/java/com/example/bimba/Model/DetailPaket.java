package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DetailPaket implements Parcelable {

    @SerializedName("id_paket")
    private int idPaket;

    @SerializedName("id_biaya")
    private int idBiaya;

    public DetailPaket() {
    }

    public DetailPaket(int idPaket, int idBiaya) {
        this.idPaket = idPaket;
        this.idBiaya = idBiaya;
    }

    protected DetailPaket(Parcel in) {
        idPaket = in.readInt();
        idBiaya = in.readInt();
    }

    public static final Creator<DetailPaket> CREATOR = new Creator<DetailPaket>() {
        @Override
        public DetailPaket createFromParcel(Parcel in) {
            return new DetailPaket(in);
        }

        @Override
        public DetailPaket[] newArray(int size) {
            return new DetailPaket[size];
        }
    };

    public int getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(int idPaket) {
        this.idPaket = idPaket;
    }

    public int getIdBiaya() {
        return idBiaya;
    }

    public void setIdBiaya(int idBiaya) {
        this.idBiaya = idBiaya;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idPaket);
        parcel.writeInt(idBiaya);
    }
}
