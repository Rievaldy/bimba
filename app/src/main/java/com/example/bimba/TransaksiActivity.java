package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.ListHistoryPembayaranAdapter;
import com.example.bimba.Adapter.ListTagihanAdapter;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.HistoryPembayaran.ApiInterfaceHistoryPembayaran;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.HistoryPembayaran.ResponseHistoryPembayaran;
import com.example.bimba.RESTAPI.Tunggakan.ApiInterfaceTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.ResponseTunggakan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiActivity extends AppCompatActivity {

    private RecyclerView rvListTransaksi;
    private LinearLayoutManager mManager;
    private HistoryPembayaranListener listener;
    private ApiInterfaceHistoryPembayaran apiInterfaceHistoryPembayaran;
    private SessionManagement sessionManagement;
    private ArrayList<CompleteHistoryPembayaran> completeHistoryPembayaranArrayList;
    private FloatingActionButton btnDownloadExcel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        rvListTransaksi = findViewById(R.id.list_data_transaksi);
        apiInterfaceHistoryPembayaran = ApiClient.getClient().create(ApiInterfaceHistoryPembayaran.class);
        sessionManagement = new SessionManagement(getApplicationContext());
        btnDownloadExcel = findViewById(R.id.btn_downloadExcel);

        if(sessionManagement.getUserAccessSession() != 2){
            btnDownloadExcel.setVisibility(View.GONE);
        }


        listener = new HistoryPembayaranListener() {
            @Override
            public void onClickListener(CompleteHistoryPembayaran completeHistoryPembayaran) {
                goToDetailHistory(completeHistoryPembayaran);
            }
        };
        loadDataHistory();
        btnDownloadExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransaksiActivity.this, SettingDownloadExcel.class));
            }
        });
    }

    private void loadDataHistory(){
        Call<ResponseHistoryPembayaran> call;
        if(sessionManagement.getUserAccessSession() == 3){//user
            call = apiInterfaceHistoryPembayaran.readHistoryPembayaran("0",0,-1,sessionManagement.getUserIdSession(), null,null);
        }else if(sessionManagement.getUserAccessSession() == 2){//owner
            call = apiInterfaceHistoryPembayaran.readHistoryPembayaran("0",0,1,0, null, null);
        }else{ //admin
            call = apiInterfaceHistoryPembayaran.readHistoryPembayaran("0",0,-1,0, null, null);
        }
        call.enqueue(new Callback<ResponseHistoryPembayaran>() {
            @Override
            public void onResponse(Call<ResponseHistoryPembayaran> call, Response<ResponseHistoryPembayaran> response) {
                if(response.isSuccessful()){
                    completeHistoryPembayaranArrayList = response.body().getData();
                    prepareRecycleView(completeHistoryPembayaranArrayList);
                    Log.d("tes", "onResponse: success");
                }else{
                    Log.d("tes", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryPembayaran> call, Throwable t) {
                Log.d("tes", "onFailure: " + t.getMessage());
            }
        });
    }

    private void prepareRecycleView(ArrayList<CompleteHistoryPembayaran> completeHistoryPembayaranArrayList){
        ListHistoryPembayaranAdapter listHistoryPembayaranAdapter = new ListHistoryPembayaranAdapter(TransaksiActivity.this, completeHistoryPembayaranArrayList, listener);
        mManager = new LinearLayoutManager(TransaksiActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvListTransaksi.setLayoutManager(mManager);
        rvListTransaksi.setAdapter(listHistoryPembayaranAdapter);
    }

    private void goToDetailHistory(CompleteHistoryPembayaran completeHistoryPembayaran){
        Intent intent = new Intent(TransaksiActivity.this, DetailHistoryPembayaranActivity.class);
        intent.putExtra("EXTRA_COMPLETE_HISTORY_PEMBAYARAN", completeHistoryPembayaran);
        startActivity(intent);
        finish();
    }

    public interface HistoryPembayaranListener{
        void onClickListener(CompleteHistoryPembayaran completeHistoryPembayaran);
    }
}
