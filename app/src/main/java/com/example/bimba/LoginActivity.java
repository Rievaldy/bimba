package com.example.bimba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bimba.Model.User;
import com.example.bimba.Model.UserAccess;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;
import com.example.bimba.RESTAPI.UserAccess.ApiInterfaceUserAccess;
import com.example.bimba.RESTAPI.UserAccess.ResponseUserAccess;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.showMessage;

public class LoginActivity extends AppCompatActivity {

    TextView tvDaftar, lupaPass;
    EditText etEmailUser;
    EditText etPassword;
    Button login;

    private ApiInterfaceUserAccess apiInterfaceUserAccess;
    private UserAccess userAccess;
    private SessionManagement sessionManagement ;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvDaftar = findViewById(R.id.daftar);
        login = findViewById(R.id.loginBtn);
        etEmailUser = findViewById(R.id.loginEmail);
        etPassword = findViewById(R.id.loginPassword);
        apiInterfaceUserAccess = ApiClient.getClient().create(ApiInterfaceUserAccess.class);
        lupaPass = findViewById(R.id.lupapass);
        sessionManagement = new SessionManagement(getApplicationContext());

        lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterUser.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<ResponseUserAccess> call = apiInterfaceUserAccess.getUserAccess(0, etEmailUser.getText().toString(), etPassword.getText().toString(),null,null,null);
                call.enqueue(new Callback<ResponseUserAccess>() {
                    @Override
                    public void onResponse(Call<ResponseUserAccess> call, Response<ResponseUserAccess> response) {
                        if(response.isSuccessful()){
                            if (response.body().getData() != null){
                                userAccess = response.body().getData().get(0);
                                sessionManagement.saveUserAccessSession(userAccess.getIdLevel());
                                sessionManagement.saveUserEmailSession(userAccess.getEmailUser());
                                showMessage(LoginActivity.this, userAccess.getEmailUser() + userAccess.getIdLevel());
                                switch (userAccess.getIdLevel()){
                                    case 1 :
                                        modeAdmin();
                                        break;
                                    case 2 :
                                        modeOwner();
                                        break;
                                    case 3 :
                                        modeUser();
                                        break;
                                    default:
                                        showMessage(LoginActivity.this, "something Wrong Happend");
                                        break;
                                }
                            }else{
                                showMessage(LoginActivity.this, "Maaf Email dan Password Tidak dikenali");
                            }
                        }else{
                            try {
                                Log.d("error", "onResponse: " + response.errorBody().string());
                            } catch (Exception e) {
                                Log.d("tes", "onResponse: " + e.getMessage());
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUserAccess> call, Throwable t) {
                        Log.d("tes", "onFailure: " + t.getMessage());
                        showMessage(LoginActivity.this, "Failed To getting Data User Access");
                    }
                });

            }
        });
    }

    public void modeUser(){
        if(!requestStoragePermission()){
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS_STORAGE, STORAGE_PERMISSION_CODE);
        }else{
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void modeAdmin(){
        if(!requestStoragePermission()){
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS_STORAGE, STORAGE_PERMISSION_CODE);
        }else{
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void modeOwner(){
        if(!requestStoragePermission()){
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS_STORAGE, STORAGE_PERMISSION_CODE);
        }else{
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean requestStoragePermission() {
        for(String permission : PERMISSIONS_STORAGE){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Tidak Mendapatkan Hak Akses",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Butuh Hak Akses Membaca File",Toast.LENGTH_SHORT).show();
            } else if (grantResults[1] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Butuh Hak Akses Membuat File",Toast.LENGTH_SHORT).show();
            }else if(grantResults[2] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Butuh Hak Akses Untuk Midtrans",Toast.LENGTH_SHORT).show();
            }else{
                switch (userAccess.getIdLevel()){
                    case 1 :
                        modeAdmin();
                        break;
                    case 2 :
                        modeOwner();
                        break;
                    case 3 :
                        modeUser();
                        break;
                    default:
                        showMessage(LoginActivity.this, "something Wrong Happend");
                        break;
                }
            }
        }
    }

}