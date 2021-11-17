package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.DataSiswaAdapter;
import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Siswa.ApiInterfaceSiswa;
import com.example.bimba.RESTAPI.Siswa.ResponseSiswa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiswaActivity extends AppCompatActivity {

    private RecyclerView rvDataSiswa;
    private SessionManagement sessionManagement;
    private LinearLayoutManager mManager;
    private FloatingActionButton btnAddSiswa;
    private int mode;
    private int idUser;
    private InterfaceInitializeData interfaceInitializeData;
    private ApiInterfaceSiswa apiInterfaceSiswa;
    private ArrayList<Siswa> listSiswa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa);

        rvDataSiswa = findViewById(R.id.list_data_siswa);
        btnAddSiswa = findViewById(R.id.btn_addsiswa);
        apiInterfaceSiswa = ApiClient.getClient().create(ApiInterfaceSiswa.class);
        sessionManagement = new SessionManagement(getApplicationContext());
        interfaceInitializeData = new InterfaceInitializeData() {
            @Override
            public void onSuccessRetriveListSiswa(ArrayList<Siswa> data) {
                listSiswa = data;
                prepareRecycleView();
            }
        };
        idUser = sessionManagement.getUserIdSession();
        mode = sessionManagement.getUserAccessSession();
        initializeUI();
        if( mode == 3){
            btnAddSiswa.setVisibility(View.VISIBLE);
        }else{
            btnAddSiswa.setVisibility(View.GONE);
        }

        btnAddSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SiswaActivity.this, TambahSiswaActivity.class);
                intent.putExtra("USER_ID",idUser);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initializeUI(){
        loadDataSiswa(idUser);
    }

    private void loadDataSiswa(int idUser){
        if(idUser != 0){
            Call<ResponseSiswa> call;
            if(mode == 3){
                call = apiInterfaceSiswa.getSiswa(0, idUser, null, null, null);
            }else{
                call = apiInterfaceSiswa.getSiswa(0, 0, null, null, null);
            }
            call.enqueue(new Callback<ResponseSiswa>() {
                @Override
                public void onResponse(Call<ResponseSiswa> call, Response<ResponseSiswa> response) {
                    if(response.isSuccessful()){
                        if(response.body().getData() !=  null){
                            ArrayList<Siswa> listSiswa = response.body().getData();
                            interfaceInitializeData.onSuccessRetriveListSiswa(listSiswa);
                        }else{
                            Log.d("status", "onResponse: is null" );
                        }
                    }else{
                        Log.d("status", "onResponse: is success" );
                    }
                }

                @Override
                public void onFailure(Call<ResponseSiswa> call, Throwable t) {
                    Log.d("error", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void prepareRecycleView(){
        DataSiswaAdapter dataSiswaAdapter = new DataSiswaAdapter(SiswaActivity.this, listSiswa);
        mManager = new LinearLayoutManager(SiswaActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvDataSiswa.setLayoutManager(mManager);
        rvDataSiswa.setAdapter(dataSiswaAdapter);
    }


    private interface InterfaceInitializeData{
        void onSuccessRetriveListSiswa(ArrayList<Siswa> data);
    }

}
