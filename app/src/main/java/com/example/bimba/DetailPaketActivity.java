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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.ListBiayaDetailPaketAdapter;
import com.example.bimba.Adapter.ListPaketAdapter;
import com.example.bimba.Model.DetailPaket;
import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Biaya.ApiInterfaceBiaya;
import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.DetailPaket.ApiInterfaceDetailPaket;
import com.example.bimba.RESTAPI.Response;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailPaketActivity extends AppCompatActivity {
    private SessionManagement sessionManagement;
    private DetailPaketListener detailPaketListener;
    private JenisPaket jenisPaket;
    private DetailPaket detailPaket;
    private ArrayList<ListBiaya> listBiayaArrayList;
    private ArrayList<ListBiaya> rincianBiayaArrayList;
    private LinearLayoutManager mManager;
    private TextView descPaket, masaPembelajaran;
    private RecyclerView rvListBiaya;
    private Button daftarPaket;
    private ImageButton addBiaya;
    private ApiInterfaceDetailPaket apiInterfaceDetailPaket;
    private ApiInterfaceBiaya apiInterfaceBiaya;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket);

        jenisPaket = getIntent().getParcelableExtra("EXTRA_PAKET");
        descPaket = findViewById(R.id.tvDescPaket);
        masaPembelajaran = findViewById(R.id.tvMasaPembelajaran);
        rvListBiaya = findViewById(R.id.rv_list_biaya);
        addBiaya = findViewById(R.id.btnTambahBiaya);
        daftarPaket = findViewById(R.id.btn_daftar_paket);
        detailPaket = new DetailPaket();
        detailPaket.setIdPaket( (jenisPaket == null) ? 0 : jenisPaket.getIdPaket());

        apiInterfaceDetailPaket = ApiClient.getClient().create(ApiInterfaceDetailPaket.class);
        apiInterfaceBiaya = ApiClient.getClient().create(ApiInterfaceBiaya.class);
        loadData(jenisPaket);
        sessionManagement = new SessionManagement(getApplicationContext());

        if(sessionManagement.getUserAccessSession() == 3){
            addBiaya.setVisibility(View.GONE);
            daftarPaket.setVisibility(View.VISIBLE);
        }else{
            addBiaya.setVisibility(View.VISIBLE);
            daftarPaket.setVisibility(View.GONE);
        }
        detailPaketListener = new DetailPaketListener() {
            @Override
            public void onDeleteListBiaya(ListBiaya listBiaya) {
                onDeleteBiaya(listBiaya);
            }
        };

        addBiaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataBiaya();
            }
        });

        daftarPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivityDaftarPaket();
            }
        });

    }

    private void loadData(JenisPaket jenisPaket){
        descPaket.setText(jenisPaket.getDescPaket());
        masaPembelajaran.setText(String.valueOf(jenisPaket.getMasaPembelajaran()) + " Bulan");

        Call<ResponseBiaya> call = apiInterfaceDetailPaket.getDetailPaket(jenisPaket.getIdPaket());
        call.enqueue(new Callback<ResponseBiaya>() {
            @Override
            public void onResponse(Call<ResponseBiaya> call, retrofit2.Response<ResponseBiaya> response) {
                if(response.isSuccessful()){
                    rincianBiayaArrayList = response.body().getData();
                    prepareRecycleView();
                }
            }

            @Override
            public void onFailure(Call<ResponseBiaya> call, Throwable t) {

            }
        });
    }

    private void loadDataBiaya(){
        Call<ResponseBiaya> call = apiInterfaceBiaya.getListBiaya(0,"id_biaya desc", null, null);
        call.enqueue(new Callback<ResponseBiaya>() {
            @Override
            public void onResponse(Call<ResponseBiaya> call, retrofit2.Response<ResponseBiaya> response) {
                if(response.isSuccessful()){
                    listBiayaArrayList = response.body().getData();
                    listBiayaArrayList = sortIfAlreadyAdded(rincianBiayaArrayList, listBiayaArrayList);
                    showDialogTambahData(listBiayaArrayList);
                }else{
                    Log.d("tes", "onSuccess: Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBiaya> call, Throwable t) {
                Log.d("tes", "onFailure: ");
            }
        });
    }

    private void prepareRecycleView(){
        ListBiayaDetailPaketAdapter listBiayaDetailPaketAdapter = new ListBiayaDetailPaketAdapter(DetailPaketActivity.this,rincianBiayaArrayList , detailPaketListener);
        mManager = new LinearLayoutManager(DetailPaketActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvListBiaya.setLayoutManager(mManager);
        rvListBiaya.setAdapter(listBiayaDetailPaketAdapter);
    }

    private void onDeleteBiaya(ListBiaya listBiaya){
        Call<Response> call = apiInterfaceDetailPaket.deleteDetailPaket(jenisPaket.getIdPaket(), listBiaya.getIdBiaya());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    loadData(jenisPaket);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void addBiaya(DetailPaket detailPaket){
        Call<Response> call = apiInterfaceDetailPaket.createDetailPaket(detailPaket.getIdPaket(), detailPaket.getIdBiaya());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    Log.d("tes", "onSuccess: Success");
                    loadData(jenisPaket);
                }else{
                    Log.d("tes", "onSuccess: Failed");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("tes", "onFailure: ");
            }
        });
    }

    private void showDialogTambahData(ArrayList<ListBiaya> data) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_rincian_biaya);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        Spinner spBiaya = dialog.findViewById(R.id.spinner_dialog);
        Button tblTambah = dialog.findViewById(R.id.btn_add_dialog_rincian_biaya);
        ListBiaya listBiaya = new ListBiaya();
        listBiaya.setIdBiaya(0);
        listBiaya.setDescBiaya("Pilih Rincian Biaya");
        data.add(0, listBiaya);
        ArrayAdapter<ListBiaya> arrayAdapter = new ArrayAdapter<>(DetailPaketActivity.this, R.layout.style_spinner, data);
        spBiaya.setAdapter(arrayAdapter);

        spBiaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                detailPaket.setIdBiaya(data.get(i).getIdBiaya());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tblTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailPaket.getIdBiaya() < 1) {
                    tblTambah.setError("Data Tidak Boleh Kosong");
                }
                else {
                    addBiaya(detailPaket);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private ArrayList<ListBiaya> sortIfAlreadyAdded(ArrayList<ListBiaya> rincian, ArrayList<ListBiaya> all){
        if(rincian != null && all != null){
            int j = rincian.size() - 1;
            int size = all.size() - 1;
            Log.d("tes", "sortIfAlreadyAdded: " + j);
            Log.d("tes", "sortIfAlreadyAdded: " + size);
            for(int i = size; i >= 0; i--){
                if(all.get(i).getIdBiaya() == rincian.get(j).getIdBiaya()){
                    all.remove(i);
                    if(j > 0){
                        j--;
                    }
                }
            }
        }

        return all;
    }

    private void goToActivityDaftarPaket(){
        Intent intent = new Intent(DetailPaketActivity.this, DaftarPaketActivity.class);
        intent.putExtra("EXTRA_PAKET", jenisPaket);
        startActivity(intent);
    }

    public interface DetailPaketListener{
        void onDeleteListBiaya(ListBiaya listBiaya);
    }
}
