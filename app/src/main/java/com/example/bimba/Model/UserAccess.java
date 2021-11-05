package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserAccess implements Parcelable {
    @SerializedName("email_user")
    String emailUser;

    @SerializedName("password_user")
    String passwordUser;

    @SerializedName("id_level")
    int idLevel;


    public UserAccess(Parcel parcel) {
    }

    public UserAccess(String emailUser, String passwordUser, int idLevel, int idUser) {
        this.emailUser = emailUser;
        this.passwordUser = passwordUser;
        this.idLevel = idLevel;
    }

    public UserAccess(String emailUser, String passwordUser, int idLevel) {
        this.emailUser = emailUser;
        this.passwordUser = passwordUser;
        this.idLevel = idLevel;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(emailUser);
        parcel.writeString(passwordUser);
        parcel.writeInt(idLevel);
    }
    public static final Parcelable.Creator<UserAccess> CREATOR = new Parcelable.Creator<UserAccess>() {

        @Override
        public UserAccess createFromParcel(Parcel parcel) {
            return new UserAccess(parcel);
        }

        @Override
        public UserAccess[] newArray(int size) {
            return new UserAccess[0];
        }
    };

}
