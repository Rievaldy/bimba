package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.ModuleAdapter;
import com.example.bimba.Model.Module;
import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;
import com.example.bimba.RESTAPI.User.ResponseUser;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.findDifference;
import static com.example.bimba.Util.loadImage;
import static com.example.bimba.Util.showMessage;

public class HomeActivity extends AppCompatActivity {

    private SessionManagement sessionManagement;
    private ArrayList<Module> modules;
    private Button btnLogout;
    private ModuleAdapter moduleAdapter;
    private int level;
    private String email;
    private ApiInterfaceUser apiInterfaceUser;
    private ImageView ivProfile;
    private TextView tvNama;
    private TextView tvNoRegis;
    private User user;
    private RecyclerView recyclerViewTemplateModule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManagement = new SessionManagement(getApplicationContext());
        prepareActivity();
    }

    private void prepareActivity(){
        level = sessionManagement.getUserAccessSession();
        email = sessionManagement.getUserEmailSession();
        ivProfile = findViewById(R.id.iv_profil);
        tvNama = findViewById(R.id.tv_nama);
        tvNoRegis = findViewById(R.id.tv_no_regis);
        btnLogout = findViewById(R.id.btnlogout);
        modules = new ArrayList<>();

        recyclerViewTemplateModule = findViewById(R.id.template_module);
        apiInterfaceUser = ApiClient.getClient().create(ApiInterfaceUser.class);
        loadDataUser();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement.removeUserAccessSession();
                sessionManagement.removeUserEmailSession();
                sessionManagement.removeUserIdSession();
                goBackToLogin();
            }
        });
        switch (level){
            case 1 :
                settingsForModeAdmin();break;
            case 2 :
                settingsForModeOwner();break;
            case 3 :
                settingsForModeUser();break;
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileUserActivity.class);
                intent.putExtra("USER_EXTRA", user);
                startActivity(intent);
            }
        });
    }

    private void settingsForModeAdmin(){
        modules.add(new Module(1,"Daftar Paket", R.drawable.paket, "ADMIN", PaketActivity.class));
        modules.add(new Module(2,"Daftar Murid", R.drawable.siswa, "ADMIN", SiswaActivity.class));
        modules.add(new Module(3,"Transaksi", R.drawable.transaksi, "ADMIN", TransaksiActivity.class));
        modules.add(new Module(4,"Tagihan", R.drawable.budget, "ADMIN", TagihanActivity.class));
        modules.add(new Module(5,"Biaya", R.drawable.biaya, "ADMIN", BiayaActivity.class));
    }

    private void settingsForModeOwner(){
        //modules.add(new Module(1,"Daftar Paket", R.drawable.paket, "USER", PaketActivity.class));
        //modules.add(new Module(2,"Daftar Murid", R.drawable.siswa, "USER", SiswaActivity.class));
        modules.add(new Module(3,"Transaksi", R.drawable.transaksi, "OWNER", TransaksiActivity.class));
        modules.add(new Module(4,"Tagihan", R.drawable.budget, "OWNER", TagihanActivity.class));
    }

    private void settingsForModeUser(){
        modules.add(new Module(1,"Daftar Paket", R.drawable.paket, "USER", PaketActivity.class));
        modules.add(new Module(2,"Daftar Murid", R.drawable.siswa, "USER", SiswaActivity.class));
        modules.add(new Module(3,"Transaksi", R.drawable.transaksi, "USER", TransaksiActivity.class));
        modules.add(new Module(4,"Tagihan", R.drawable.budget, "USER", TagihanActivity.class));
    }

    private void loadDataUser(){
        Call<ResponseUser> call = apiInterfaceUser.getUser(0,email, null,null, null);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if(response.isSuccessful()){
                    if(response.body().getData().size() > 0){
                        user = response.body().getData().get(0);
                        sessionManagement.saveUserIdSession(user.getIdUser());
                        loadImage(user.getFotoProfile(), ivProfile, HomeActivity.this);
                        tvNama.setText(user.getFirstName()+ " "+ user.getLastName());
                        tvNoRegis.setText(Integer.toString(user.getIdUser()));
                        setAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                showMessage(HomeActivity.this, "Something Wrong when Getting User Data");
            }
        });
    }

    private void setAdapter(){
        moduleAdapter = new ModuleAdapter(HomeActivity.this, modules,user);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerViewTemplateModule.setHasFixedSize(true);
        recyclerViewTemplateModule.setLayoutManager(layoutManager);
        recyclerViewTemplateModule.setAdapter(moduleAdapter);
        recyclerViewTemplateModule.setBackground(null);
    }

    private void goBackToLogin(){
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
