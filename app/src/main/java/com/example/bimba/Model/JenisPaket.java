package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class JenisPaket implements Parcelable {

    @SerializedName("id_paket")
    int idPaket;

    @SerializedName("desc_paket")
    String descPaket;

    @SerializedName("masa_pembelajaran")
    int masaPembelajaran;


    public JenisPaket() {
    }

    public JenisPaket(int idPaket, String descPaket, int masaPembelajaran) {
        this.idPaket = idPaket;
        this.descPaket = descPaket;
        this.masaPembelajaran = masaPembelajaran;
    }

    protected JenisPaket(Parcel in) {
        idPaket = in.readInt();
        descPaket = in.readString();
        masaPembelajaran = in.readInt();
    }

    public static final Creator<JenisPaket> CREATOR = new Creator<JenisPaket>() {
        @Override
        public JenisPaket createFromParcel(Parcel in) {
            return new JenisPaket(in);
        }

        @Override
        public JenisPaket[] newArray(int size) {
            return new JenisPaket[size];
        }
    };

    public int getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(int idPaket) {
        this.idPaket = idPaket;
    }

    public String getDescPaket() {
        return descPaket;
    }

    public void setDescPaket(String descPaket) {
        this.descPaket = descPaket;
    }

    public int getMasaPembelajaran() {
        return masaPembelajaran;
    }

    public void setMasaPembelajaran(int masaPembelajaran) {
        this.masaPembelajaran = masaPembelajaran;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idPaket);
        parcel.writeString(descPaket);
        parcel.writeInt(masaPembelajaran);
    }

    @Override
    public String toString() {
        return descPaket;
    }
}
