package com.example.bimba.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserLevel implements Parcelable {
    @SerializedName("id_level")
    int idLevel;

    @SerializedName("desc_level")
    String descLevel;

    protected UserLevel(Parcel in){
        this.idLevel = in.readInt();
        this.descLevel = in.readString();
    }

    public UserLevel(int idLevel, String descLevel) {
        this.idLevel = idLevel;
        this.descLevel = descLevel;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }

    public String getDescLevel() {
        return descLevel;
    }

    public void setDescLevel(String descLevel) {
        this.descLevel = descLevel;
    }

    public static final Creator<UserLevel> CREATOR = new Creator<UserLevel>() {
        @Override
        public UserLevel createFromParcel(Parcel in) {
            return new UserLevel(in);
        }

        @Override
        public UserLevel[] newArray(int size) {
            return new UserLevel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idLevel);
        parcel.writeString(descLevel);
    }
}
