package com.example.bimba;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private  final String SHARED_PREF_NAME = "sharedpref";
    private final String SHARED_PREF_USER_ID = "useridsharedpref";
    private final String SHARED_PREF_USER_ACCESS = "useraccesssharedpref";
    private final String SHARED_PREF_USER_EMAIL = "useremailsharedpref";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserIdSession(int noRegis){
        editor.putInt(SHARED_PREF_USER_ID, noRegis).commit();
    }

    public void removeUserIdSession(){
        editor.remove(SHARED_PREF_USER_ID).commit();
    }

    public int getUserIdSession(){
        return sharedPreferences.getInt(SHARED_PREF_USER_ID, 0);
    }

    public void saveUserAccessSession(int idAccess){
        editor.putInt(SHARED_PREF_USER_ACCESS, idAccess).commit();
    }

    public void removeUserAccessSession(){
        editor.remove(SHARED_PREF_USER_ACCESS).commit();
    }

    public int getUserAccessSession(){
        return sharedPreferences.getInt(SHARED_PREF_USER_ACCESS,0);
    }


    public void saveUserEmailSession(String userEmail){
        editor.putString(SHARED_PREF_USER_EMAIL, userEmail).commit();
    }

    public void removeUserEmailSession(){
        editor.remove(SHARED_PREF_USER_EMAIL).commit();
    }

    public String getUserEmailSession(){
        return sharedPreferences.getString(SHARED_PREF_USER_EMAIL,"none");
    }


}
