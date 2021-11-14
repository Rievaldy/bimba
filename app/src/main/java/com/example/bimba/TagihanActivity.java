package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.DataSiswaAdapter;
import com.example.bimba.Adapter.ListTagihanAdapter;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Tunggakan.ApiInterfaceTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.ResponseTunggakan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagihanActivity extends AppCompatActivity {
    private RecyclerView rvListTagihan;
    private LinearLayoutManager mManager;
    private TagihanListener listener;
    private ApiInterfaceTunggakan apiInterfaceTunggakan;
    private SessionManagement sessionManagement;
    private ArrayList<CompleteTunggakan> completeTunggakanArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);

        rvListTagihan = findViewById(R.id.list_data_tagihan);
        apiInterfaceTunggakan = ApiClient.getClient().create(ApiInterfaceTunggakan.class);
        sessionManagement = new SessionManagement(getApplicationContext());
        listener = new TagihanListener() {
            @Override
            public void onClickListener(CompleteTunggakan completeTunggakan) {
                goToDetailTagihan(completeTunggakan);
            }
        };
        loadDataTagihan();

    }

    private void loadDataTagihan(){
        Call<ResponseTunggakan> call = apiInterfaceTunggakan.readTunggakan(0,0,0,null,sessionManagement.getUserIdSession());
        call.enqueue(new Callback<ResponseTunggakan>() {
            @Override
            public void onResponse(Call<ResponseTunggakan> call, Response<ResponseTunggakan> response) {
                if(response.isSuccessful()){
                    completeTunggakanArrayList = response.body().getData();
                    prepareRecycleView(completeTunggakanArrayList);
                    Log.d("tes", "onResponse: success");
                }else{
                    Log.d("tes", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseTunggakan> call, Throwable t) {
                Log.d("tes", "onFailure: " + t.getMessage());
            }
        });
    }

    private void prepareRecycleView(ArrayList<CompleteTunggakan> completeTunggakanArrayList){
        ListTagihanAdapter dataTagihanAdapter = new ListTagihanAdapter(TagihanActivity.this, completeTunggakanArrayList, listener);
        mManager = new LinearLayoutManager(TagihanActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvListTagihan.setLayoutManager(mManager);
        rvListTagihan.setAdapter(dataTagihanAdapter);
    }

    private void goToDetailTagihan(CompleteTunggakan completeTunggakan){
        Intent intent = new Intent(TagihanActivity.this, DetailTagihanActivity.class);
        intent.putExtra("EXTRA_COMPLETE_TUNGGAKAN", completeTunggakan);
        startActivity(intent);
    }

    public interface TagihanListener{
        void onClickListener(CompleteTunggakan completeTunggakan);
    }
}
