package com.example.bimba;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.ListPaketAdapter;
import com.example.bimba.Model.JenisPaket;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Paket.ApiInterfaceJenisPaket;
import com.example.bimba.RESTAPI.Paket.ResponsePaket;
import com.example.bimba.RESTAPI.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.showMessage;

public class PaketActivity extends AppCompatActivity {

    RecyclerView rvListPaket;
    FloatingActionButton btnAddPaket;
    ApiInterfaceJenisPaket apiInterfaceJenisPaket;
    ArrayList<JenisPaket> listJenisPaket;
    LinearLayoutManager mManager;
    private AdapterClickedListener adapterClickedListener;

    SessionManagement sessionManagement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket);

        rvListPaket = findViewById(R.id.list_data_paket);
        btnAddPaket = findViewById(R.id.btn_add_paket);
        apiInterfaceJenisPaket = ApiClient.getClient().create(ApiInterfaceJenisPaket.class);
        sessionManagement = new SessionManagement(getApplicationContext());
        loadDataJenisPaket();

        if(sessionManagement.getUserAccessSession() == 3){
            btnAddPaket.setVisibility(View.GONE);
        }

        adapterClickedListener = new AdapterClickedListener() {
            @Override
            public void onClickListener(JenisPaket jenisPaket) {
                goToDetailPaket(jenisPaket);
            }
        };

        btnAddPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTambahData();
            }
        });

    }

    private void showDialogTambahData() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_paket);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        TextView tvTitle = dialog.findViewById(R.id.title_dialog_paket);
        EditText etDescPaket = dialog.findViewById(R.id.et_desc_paket);
        EditText etMasaBelajarPaket = dialog.findViewById(R.id.et_masa_belajar_paket);
        Button tblTambah = dialog.findViewById(R.id.btn_add_dialog_paket);


        tblTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etDescPaket.getText()) || TextUtils.isEmpty(etMasaBelajarPaket.getText()))
                {
                    tblTambah.setError("Data Tidak Boleh Kosong");
                }
                else {
                    JenisPaket data = new JenisPaket();
                    data.setDescPaket(etDescPaket.getText().toString());
                    data.setMasaPembelajaran(Integer.valueOf(etMasaBelajarPaket.getText().toString()));
                    addDataBiaya(data);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void loadDataJenisPaket(){
        Call<ResponsePaket> call = apiInterfaceJenisPaket.getJenisPaket( 0, null, null, null);
        call.enqueue(new Callback<ResponsePaket>() {
            @Override
            public void onResponse(Call<ResponsePaket> call, retrofit2.Response<ResponsePaket> response) {
                if(response.isSuccessful()){
                    listJenisPaket = response.body().getData();
                    prepareRecycleView();
                }else{
                    Log.d("tes", "onResponse: failed");
                }
            }

            @Override
            public void onFailure(Call<ResponsePaket> call, Throwable t) {
                Log.d("tes", "onResponse: " + t.getMessage());
            }
        });
    }

    private void prepareRecycleView(){
        ListPaketAdapter listPaketAdapter = new ListPaketAdapter(PaketActivity.this,listJenisPaket , adapterClickedListener);
        mManager = new LinearLayoutManager(PaketActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvListPaket.setLayoutManager(mManager);
        rvListPaket.setAdapter(listPaketAdapter);
    }


    private void addDataBiaya(JenisPaket jenisPaket){
        Call<Response> call = apiInterfaceJenisPaket.createPaket(jenisPaket.getDescPaket(), jenisPaket.getMasaPembelajaran());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    loadDataJenisPaket();
                    showMessage(PaketActivity.this, "Berhasil Menambahkan Paket");
                }else{
                    Log.d("tes", "onResponse: Failed" );
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("tes", "onFailure: " + t.getMessage());
            }
        });
    }

    private void goToDetailPaket(JenisPaket jenisPaket){
        Intent intent = new Intent(PaketActivity.this, DetailPaketActivity.class);
        intent.putExtra("EXTRA_PAKET", jenisPaket);
        startActivity(intent);
    }

    public interface AdapterClickedListener{
        void onClickListener(JenisPaket jenisPaket);
    }
}
