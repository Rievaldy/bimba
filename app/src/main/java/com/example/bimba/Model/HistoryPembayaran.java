package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HistoryPembayaran implements Parcelable {

    @SerializedName("id_history_pembayaran")
    String idHistoryPembayaran;

    @SerializedName("id_tunggakan")
    int idTunggakan;

    @SerializedName("jumlah_disetorkan")
    int jumlahDisetorkan;

    @SerializedName("approved")
    int approved;

    @SerializedName("tanggal_transaksi")
    String tanggalTransaksi;

    public HistoryPembayaran() {
    }

    public HistoryPembayaran(String idHistoryPembayaran, int idTunggakan, int jumlahDisetorkan, int approved, String tanggalTransaksi) {
        this.idHistoryPembayaran = idHistoryPembayaran;
        this.idTunggakan = idTunggakan;
        this.jumlahDisetorkan = jumlahDisetorkan;
        this.approved = approved;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    protected HistoryPembayaran(Parcel in) {
        idHistoryPembayaran = in.readString();
        idTunggakan = in.readInt();
        jumlahDisetorkan = in.readInt();
        approved = in.readInt();
        tanggalTransaksi = in.readString();
    }

    public static final Creator<HistoryPembayaran> CREATOR = new Creator<HistoryPembayaran>() {
        @Override
        public HistoryPembayaran createFromParcel(Parcel in) {
            return new HistoryPembayaran(in);
        }

        @Override
        public HistoryPembayaran[] newArray(int size) {
            return new HistoryPembayaran[size];
        }
    };

    public String getIdHistoryPembayaran() {
        return idHistoryPembayaran;
    }

    public void setIdHistoryPembayaran(String idHistoryPembayaran) {
        this.idHistoryPembayaran = idHistoryPembayaran;
    }

    public int getIdTunggakan() {
        return idTunggakan;
    }

    public void setIdTunggakan(int idTunggakan) {
        this.idTunggakan = idTunggakan;
    }

    public int getJumlahDisetorkan() {
        return jumlahDisetorkan;
    }

    public void setJumlahDisetorkan(int jumlahDisetorkan) {
        this.jumlahDisetorkan = jumlahDisetorkan;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idHistoryPembayaran);
        parcel.writeInt(idTunggakan);
        parcel.writeInt(jumlahDisetorkan);
        parcel.writeInt(approved);
        parcel.writeString(tanggalTransaksi);
    }
}
