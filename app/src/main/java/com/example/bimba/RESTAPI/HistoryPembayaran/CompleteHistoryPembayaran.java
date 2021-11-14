package com.example.bimba.RESTAPI.HistoryPembayaran;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bimba.Model.HistoryPembayaran;
import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.Tunggakan;
import com.example.bimba.Model.User;
import com.google.gson.annotations.SerializedName;

public class CompleteHistoryPembayaran implements Parcelable {

    @SerializedName("history_pembayaran")
    HistoryPembayaran historyPembayaran;

    @SerializedName("tunggakan_siswa")
    Tunggakan tunggakan;

    @SerializedName("siswa")
    Siswa siswa;

    @SerializedName("paket")
    JenisPaket jenisPaket;

    @SerializedName("user")
    User user;

    public CompleteHistoryPembayaran() {
    }

    public CompleteHistoryPembayaran(HistoryPembayaran historyPembayaran, Tunggakan tunggakan, Siswa siswa, JenisPaket jenisPaket, User user) {
        this.historyPembayaran = historyPembayaran;
        this.tunggakan = tunggakan;
        this.siswa = siswa;
        this.jenisPaket = jenisPaket;
        this.user = user;
    }

    protected CompleteHistoryPembayaran(Parcel in) {
        historyPembayaran = in.readParcelable(HistoryPembayaran.class.getClassLoader());
        tunggakan = in.readParcelable(Tunggakan.class.getClassLoader());
        siswa = in.readParcelable(Siswa.class.getClassLoader());
        jenisPaket = in.readParcelable(JenisPaket.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<CompleteHistoryPembayaran> CREATOR = new Creator<CompleteHistoryPembayaran>() {
        @Override
        public CompleteHistoryPembayaran createFromParcel(Parcel in) {
            return new CompleteHistoryPembayaran(in);
        }

        @Override
        public CompleteHistoryPembayaran[] newArray(int size) {
            return new CompleteHistoryPembayaran[size];
        }
    };

    public HistoryPembayaran getHistoryPembayaran() {
        return historyPembayaran;
    }

    public void setHistoryPembayaran(HistoryPembayaran historyPembayaran) {
        this.historyPembayaran = historyPembayaran;
    }

    public Tunggakan getTunggakan() {
        return tunggakan;
    }

    public void setTunggakan(Tunggakan tunggakan) {
        this.tunggakan = tunggakan;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    public JenisPaket getJenisPaket() {
        return jenisPaket;
    }

    public void setJenisPaket(JenisPaket jenisPaket) {
        this.jenisPaket = jenisPaket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(historyPembayaran, i);
        parcel.writeParcelable(tunggakan, i);
        parcel.writeParcelable(siswa, i);
        parcel.writeParcelable(jenisPaket, i);
        parcel.writeParcelable(user, i);
    }
}
