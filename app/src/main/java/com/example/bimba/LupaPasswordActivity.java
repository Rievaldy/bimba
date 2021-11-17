package com.example.bimba;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bimba.Model.UserAccess;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.UserAccess.ApiInterfaceUserAccess;
import com.example.bimba.RESTAPI.UserAccess.ResponseUserAccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.showMessage;

public class LupaPasswordActivity extends AppCompatActivity {

    EditText email, password, rePassword;
    Button btnSubmit;
    ApiInterfaceUserAccess apiInterfaceUserAccess;
    UserAccess userAccess;
    int state;//0 for confirmation email, 1 for input new password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        email = findViewById(R.id.lupaEmail);
        password = findViewById(R.id.lupaPass);
        rePassword = findViewById(R.id.lupaRePass);
        btnSubmit = findViewById(R.id.submitLupa);
        state = 0;
        apiInterfaceUserAccess = ApiClient.getClient().create(ApiInterfaceUserAccess.class);
        password.setVisibility(View.GONE);
        rePassword.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == 0){
                    if(email.getText().toString().isEmpty()){
                        email.setError("Tolong Isi Email Anda");
                    }else{
                        checkEmail();
                    }
                }else{

                    if(password.getText().toString().isEmpty()){
                        password.setError("Wajib diisi");
                    }else if(rePassword.getText().toString().isEmpty()){
                        rePassword.setError("Wajib diisi");
                    }else if(!rePassword.getText().toString().equals(password.getText().toString())){
                        rePassword.setError("Password Tidak Sama");
                    }else{
                        updatePassword();
                    }
                }
            }
        });

    }

    private void updatePassword() {
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceUserAccess.updateUserAccess(email.getText().toString(), password.getText().toString(), userAccess.getIdLevel());
        call.enqueue(new Callback<com.example.bimba.RESTAPI.Response>() {
            @Override
            public void onResponse(Call<com.example.bimba.RESTAPI.Response> call, Response<com.example.bimba.RESTAPI.Response> response) {
                if(response.isSuccessful()){
                    showMessage(LupaPasswordActivity.this, "Berhasil Merubah Password");

                    finish();
                }
            }

            @Override
            public void onFailure(Call<com.example.bimba.RESTAPI.Response> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    private void checkEmail(){
        Call<ResponseUserAccess> call = apiInterfaceUserAccess.getUserAccess(0,email.getText().toString(), null, null, null,null);
        call.enqueue(new Callback<ResponseUserAccess>() {
            @Override
            public void onResponse(Call<ResponseUserAccess> call, Response<ResponseUserAccess> response) {
                if(response.isSuccessful()){
                    if(response.body().getData()!= null){
                        email.setVisibility(View.GONE);
                        password.setVisibility(View.VISIBLE);
                        rePassword.setVisibility(View.VISIBLE);
                        userAccess = response.body().getData().get(0);
                        state = 1;
                    }else{
                        showMessage(LupaPasswordActivity.this, "Maaf Kami Tidak Dapat Menemukan Email yang anda maksud");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAccess> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}
