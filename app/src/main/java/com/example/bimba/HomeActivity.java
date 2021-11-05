package com.example.bimba;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;
import com.example.bimba.RESTAPI.User.ResponseUser;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.loadImage;
import static com.example.bimba.Util.showMessage;

public class HomeActivity extends AppCompatActivity {

    private String level;
    private String email;
    private ApiInterfaceUser apiInterfaceUser;
    private ImageView ivProfile;
    private TextView tvNama;
    private TextView tvNoRegis;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prepareActivity();
    }

    private void prepareActivity(){
        level = getIntent().getStringExtra(LoginActivity.MODE_USER);
        email = getIntent().getStringExtra(LoginActivity.USER_EMAIL);
        ivProfile = findViewById(R.id.iv_profil);
        tvNama = findViewById(R.id.tv_nama);
        tvNoRegis = findViewById(R.id.tv_noregis);
        apiInterfaceUser = ApiClient.getClient().create(ApiInterfaceUser.class);
        switch (level){
            case "ADMIN" :
                settingsForModeAdmin();break;
            case "OWNER" :
                settingsForModeOwner();break;
            case "USER" :
                settingsForModeUser();break;
        }

        loadDataUser();

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void settingsForModeAdmin(){

    }

    private void settingsForModeOwner(){

    }

    private void settingsForModeUser(){

    }

    private void loadDataUser(){
        Call<ResponseUser> call = apiInterfaceUser.getUser(0,email, null,null, null);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if(response.isSuccessful()){
                    if(response.body().getData().size() > 0){
                        user = response.body().getData().get(0);
                        loadImage(new File(user.getFotoProfile()), ivProfile, HomeActivity.this);
                        tvNama.setText(user.getFirstName()+ " "+ user.getLastName());
                        tvNoRegis.setText(Integer.toString(user.getIdUser()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                showMessage(HomeActivity.this, "Something Wrong when Getting User Data");
            }
        });
    }
}
