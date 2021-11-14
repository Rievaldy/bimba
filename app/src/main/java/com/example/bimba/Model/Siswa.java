package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Siswa implements Parcelable {

    @SerializedName("nis")
    int nis;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("jenis_kelamin")
    String jenisKelamin;

    @SerializedName("tanggal_lahir")
    String tanggalLahir;

    @SerializedName("tahun_masuk")
    String tahunMasuk;

    @SerializedName("foto_siswa")
    String fotoSiswa;

    @SerializedName("id_user")
    int idUser;

    public Siswa() {
    }

    public Siswa(int nis, String firstName, String lastName, String jenisKelamin, String tanggalLahir, String tahunMasuk, String fotoSiswa, int idUser) {
        this.nis = nis;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.tahunMasuk = tahunMasuk;
        this.fotoSiswa = fotoSiswa;
        this.idUser = idUser;
    }

    protected Siswa(Parcel in) {
        nis = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        jenisKelamin = in.readString();
        tanggalLahir = in.readString();
        tahunMasuk = in.readString();
        fotoSiswa = in.readString();
        idUser = in.readInt();
    }

    public int getNis() {
        return nis;
    }

    public void setNis(int nis) {
        this.nis = nis;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getTahunMasuk() {
        return tahunMasuk;
    }

    public void setTahunMasuk(String tahunMasuk) {
        this.tahunMasuk = tahunMasuk;
    }

    public String getFotoSiswa() {
        return fotoSiswa;
    }

    public void setFotoSiswa(String fotoSiswa) {
        this.fotoSiswa = fotoSiswa;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public static final Creator<Siswa> CREATOR = new Creator<Siswa>() {
        @Override
        public Siswa createFromParcel(Parcel in) {
            return new Siswa(in);
        }

        @Override
        public Siswa[] newArray(int size) {
            return new Siswa[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(nis);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(jenisKelamin);
        parcel.writeString(tanggalLahir);
        parcel.writeString(tahunMasuk);
        parcel.writeString(fotoSiswa);
        parcel.writeInt(idUser);
    }

    @Override
    public String toString() {
        return firstName +" "+lastName;
    }
}
