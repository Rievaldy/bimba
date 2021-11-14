package com.example.bimba.RESTAPI.Tunggakan;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.Tunggakan;
import com.example.bimba.Model.User;
import com.google.gson.annotations.SerializedName;

public class CompleteTunggakan implements Parcelable {

    @SerializedName("tunggakan_siswa")
    Tunggakan tunggakan;

    @SerializedName("siswa")
    Siswa siswa;

    @SerializedName("paket")
    JenisPaket jenisPaket;

    @SerializedName("user")
    User user;

    public CompleteTunggakan() {
    }

    public CompleteTunggakan(Tunggakan tunggakan, Siswa siswa, JenisPaket jenisPaket, User user) {
        this.tunggakan = tunggakan;
        this.siswa = siswa;
        this.jenisPaket = jenisPaket;
        this.user = user;
    }

    protected CompleteTunggakan(Parcel in) {
        tunggakan = in.readParcelable(Tunggakan.class.getClassLoader());
        siswa = in.readParcelable(Siswa.class.getClassLoader());
        jenisPaket = in.readParcelable(JenisPaket.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<CompleteTunggakan> CREATOR = new Creator<CompleteTunggakan>() {
        @Override
        public CompleteTunggakan createFromParcel(Parcel in) {
            return new CompleteTunggakan(in);
        }

        @Override
        public CompleteTunggakan[] newArray(int size) {
            return new CompleteTunggakan[size];
        }
    };

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
        parcel.writeParcelable(tunggakan, i);
        parcel.writeParcelable(siswa, i);
        parcel.writeParcelable(jenisPaket, i);
        parcel.writeParcelable(user, i);
    }
}
