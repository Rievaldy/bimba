package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable{

    @SerializedName("id_user")
    int idUser;

    @SerializedName("email_user")
    String emailUser;


    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("jenis_kelamin")
    String jenisKelamin;

    @SerializedName("no_hp")
    String noHp;

    @SerializedName("alamat")
    String alamat;

    @SerializedName("rt")
    String rt;

    @SerializedName("rw")
    String rw;

    @SerializedName("foto_profile")
    String fotoProfile;

    public User() {
    }

    public User(String emailUser, String firstName, String lastName, String jenisKelamin, String noHp, String alamat, String rt, String rw) {
        this.emailUser = emailUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jenisKelamin = jenisKelamin;
        this.noHp = noHp;
        this.alamat = alamat;
        this.rt = rt;
        this.rw = rw;
    }

    public User(String emailUser, String firstName, String lastName, String jenisKelamin, String noHp, String alamat, String rt, String rw, String fotoProfile) {
        this.emailUser = emailUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jenisKelamin = jenisKelamin;
        this.noHp = noHp;
        this.alamat = alamat;
        this.rt = rt;
        this.rw = rw;
        this.fotoProfile = fotoProfile;
    }

    public User(Parcel parcel) {
        this.idUser = parcel.readInt();
        this.emailUser = parcel.readString();
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.jenisKelamin = parcel.readString();
        this.noHp = parcel.readString();
        this.alamat = parcel.readString();
        this.rt = parcel.readString();
        this.rw = parcel.readString();
        this.fotoProfile = parcel.readString();
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
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

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getFotoProfile() {
        return fotoProfile;
    }

    public void setFotoProfile(String fotoProfile) {
        this.fotoProfile = fotoProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idUser);
        parcel.writeString(emailUser);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(jenisKelamin);
        parcel.writeString(noHp);
        parcel.writeString(alamat);
        parcel.writeString(rt);
        parcel.writeString(rw);
        parcel.writeString(fotoProfile);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[0];
        }
    };
}
