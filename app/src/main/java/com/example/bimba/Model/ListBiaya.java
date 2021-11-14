package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ListBiaya implements Parcelable {

    @SerializedName("id_biaya")
    int idBiaya;

    @SerializedName("desc_biaya")
    String descBiaya;

    @SerializedName("harga")
    int hargaBiaya;

    @SerializedName("pembayaran_berkala")
    int pembayaranBerkala;

    public ListBiaya() {
    }

    public ListBiaya(int idBiaya, String descBiaya, int hargaBiaya, int pembayaranBerkala) {
        this.idBiaya = idBiaya;
        this.descBiaya = descBiaya;
        this.hargaBiaya = hargaBiaya;
        this.pembayaranBerkala = pembayaranBerkala;
    }

    protected ListBiaya(Parcel in) {
        idBiaya = in.readInt();
        descBiaya = in.readString();
        hargaBiaya = in.readInt();
        pembayaranBerkala = in.readInt();
    }

    public static final Creator<ListBiaya> CREATOR = new Creator<ListBiaya>() {
        @Override
        public ListBiaya createFromParcel(Parcel in) {
            return new ListBiaya(in);
        }

        @Override
        public ListBiaya[] newArray(int size) {
            return new ListBiaya[size];
        }
    };

    public int getIdBiaya() {
        return idBiaya;
    }

    public void setIdBiaya(int idBiaya) {
        this.idBiaya = idBiaya;
    }

    public String getDescBiaya() {
        return descBiaya;
    }

    public void setDescBiaya(String descBiaya) {
        this.descBiaya = descBiaya;
    }

    public int getHargaBiaya() {
        return hargaBiaya;
    }

    public void setHargaBiaya(int hargaBiaya) {
        this.hargaBiaya = hargaBiaya;
    }

    public int getPembayaranBerkala() {
        return pembayaranBerkala;
    }

    public void setPembayaranBerkala(int pembayaranBerkala) {
        this.pembayaranBerkala = pembayaranBerkala;
    }

    @Override
    public String toString() {
        return descBiaya;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idBiaya);
        parcel.writeString(descBiaya);
        parcel.writeInt(hargaBiaya);
        parcel.writeInt(pembayaranBerkala);
    }
}
