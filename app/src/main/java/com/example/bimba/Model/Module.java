package com.example.bimba.Model;

import android.app.Activity;

public class Module {

    private int idModule;
    private String namaModule;
    private int drawableName;
    private String mode;
    private Class activity;

    public Module() {
    }

    public Module(int idModule, String namaModule, int drawableName, String mode, Class activity) {
        this.idModule = idModule;
        this.namaModule = namaModule;
        this.drawableName = drawableName;
        this.mode = mode;
        this.activity = activity;
    }

    public int getIdModule() {
        return idModule;
    }

    public void setIdModule(int idModule) {
        this.idModule = idModule;
    }

    public String getNamaModule() {
        return namaModule;
    }

    public void setNamaModule(String namaModule) {
        this.namaModule = namaModule;
    }

    public int getDrawableName() {
        return drawableName;
    }

    public void setDrawableName(int drawableName) {
        this.drawableName = drawableName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }
}
