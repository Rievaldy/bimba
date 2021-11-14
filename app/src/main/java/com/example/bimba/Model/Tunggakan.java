package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tunggakan implements Parcelable {

    @SerializedName("id_tunggakan")
    int idTunggakan;

    @SerializedName("nis")
    int nis;

    @SerializedName("id_paket")
    int idPaket;

    @SerializedName("total_harus_bayar")
    int totalHarusBayar;

    @SerializedName("baru_dibayarkan")
    int baruDibayarkan;

    @SerializedName("tahun_masuk")
    String tahunMasuk;

    public Tunggakan() {
    }

    public Tunggakan(int idTunggakan, int nis, int idPaket, int totalHarusBayar, int baruDibayarkan, String tahunMasuk) {
        this.idTunggakan = idTunggakan;
        this.nis = nis;
        this.idPaket = idPaket;
        this.totalHarusBayar = totalHarusBayar;
        this.baruDibayarkan = baruDibayarkan;
        this.tahunMasuk = tahunMasuk;
    }

    protected Tunggakan(Parcel in) {
        idTunggakan = in.readInt();
        nis = in.readInt();
        idPaket = in.readInt();
        totalHarusBayar = in.readInt();
        baruDibayarkan = in.readInt();
        tahunMasuk = in.readString();
    }

    public static final Creator<Tunggakan> CREATOR = new Creator<Tunggakan>() {
        @Override
        public Tunggakan createFromParcel(Parcel in) {
            return new Tunggakan(in);
        }

        @Override
        public Tunggakan[] newArray(int size) {
            return new Tunggakan[size];
        }
    };

    public int getIdTunggakan() {
        return idTunggakan;
    }

    public void setIdTunggakan(int idTunggakan) {
        this.idTunggakan = idTunggakan;
    }

    public int getNis() {
        return nis;
    }

    public void setNis(int nis) {
        this.nis = nis;
    }

    public int getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(int idPaket) {
        this.idPaket = idPaket;
    }

    public int getTotalHarusBayar() {
        return totalHarusBayar;
    }

    public void setTotalHarusBayar(int totalHarusBayar) {
        this.totalHarusBayar = totalHarusBayar;
    }

    public int getBaruDibayarkan() {
        return baruDibayarkan;
    }

    public void setBaruDibayarkan(int baruDibayarkan) {
        this.baruDibayarkan = baruDibayarkan;
    }

    public String getTahunMasuk() {
        return tahunMasuk;
    }

    public void setTahunMasuk(String tahunMasuk) {
        this.tahunMasuk = tahunMasuk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idTunggakan);
        parcel.writeInt(nis);
        parcel.writeInt(idPaket);
        parcel.writeInt(totalHarusBayar);
        parcel.writeInt(baruDibayarkan);
        parcel.writeString(tahunMasuk);
    }
}
