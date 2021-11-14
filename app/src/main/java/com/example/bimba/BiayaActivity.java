package com.example.bimba;

import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.DataSiswaAdapter;
import com.example.bimba.Adapter.ListBiayaAdapter;
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Biaya.ApiInterfaceBiaya;
import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.Siswa.ApiInterfaceSiswa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.showDialogEditText;
import static com.example.bimba.Util.showMessage;

public class BiayaActivity extends AppCompatActivity {
    EditListener editListener;
    RecyclerView rvDataBiaya;
    FloatingActionButton btnAddBiaya;
    LinearLayoutManager mManager;
    ArrayList<ListBiaya> listDataBiaya;
    ApiInterfaceBiaya apiInterfaceBiaya;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya);

        editListener = listBiaya -> showDialogTambahData(listBiaya);
        rvDataBiaya = findViewById(R.id.rv_data_biaya);
        btnAddBiaya = findViewById(R.id.btn_addBiaya);
        apiInterfaceBiaya = ApiClient.getClient().create(ApiInterfaceBiaya.class);
        loadDataBiaya();

        btnAddBiaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTambahData(null);
            }
        });

    }

    private void showDialogTambahData(ListBiaya listBiaya) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_biaya);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        TextView tvTitle = dialog.findViewById(R.id.title_dialog_biaya);
        EditText etDescBiaya = dialog.findViewById(R.id.et_desc_biaya);
        EditText etHargaBiaya = dialog.findViewById(R.id.et_harga_biaya);
        Spinner jenisPembayaran = dialog.findViewById(R.id.spinner_dialog_jenis_pembayaran);
        ListBiaya data = new ListBiaya();
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Pilih Jenis Pembayaran");
        stringArrayList.add("Sekali");
        stringArrayList.add("Perbulan");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BiayaActivity.this, R.layout.style_spinner, stringArrayList);
        jenisPembayaran.setAdapter(arrayAdapter);

        jenisPembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data.setPembayaranBerkala(i-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button tblTambah = dialog.findViewById(R.id.btn_add_dialog_biaya);

        if(listBiaya != null){
            tvTitle.setText("Edit Data Biaya");
            etDescBiaya.setText(listBiaya.getDescBiaya());
            etHargaBiaya.setText(String.valueOf(listBiaya.getHargaBiaya()));
            jenisPembayaran.setSelection(listBiaya.getPembayaranBerkala() + 1);
            tblTambah.setText("Ubah");
        }


        tblTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etDescBiaya.getText()) || TextUtils.isEmpty(etHargaBiaya.getText()) || data.getPembayaranBerkala() == -1)
                {
                    tblTambah.setError("Data Tidak Boleh Kosong");
                }
                else {
                    data.setDescBiaya(etDescBiaya.getText().toString());
                    data.setHargaBiaya(Integer.valueOf(etHargaBiaya.getText().toString()));
                    if(listBiaya != null){
                        data.setIdBiaya(listBiaya.getIdBiaya());
                        editDataBiaya(data);
                    }else{
                        addDataBiaya(data);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void loadDataBiaya(){
        Call<ResponseBiaya> call = apiInterfaceBiaya.getListBiaya( 0, "desc_biaya desc", null, null);
        call.enqueue(new Callback<ResponseBiaya>() {
            @Override
            public void onResponse(Call<ResponseBiaya> call, retrofit2.Response<ResponseBiaya> response) {
                if(response.isSuccessful()){
                    listDataBiaya = (ArrayList<ListBiaya>) response.body().getData();
                    prepareRecycleView();
                }else{
                    Log.d("tes", "onResponse: failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBiaya> call, Throwable t) {
                Log.d("tes", "onResponse: " + t.getMessage());
            }
        });
    }

    private void prepareRecycleView(){
        ListBiayaAdapter listBiayaAdapter = new ListBiayaAdapter(BiayaActivity.this, listDataBiaya, editListener);
        mManager = new LinearLayoutManager(BiayaActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvDataBiaya.setLayoutManager(mManager);
        rvDataBiaya.setAdapter(listBiayaAdapter);
    }


    private void editDataBiaya(ListBiaya data) {
        Call<Response> call = apiInterfaceBiaya.updateBiaya(data.getIdBiaya(), data.getDescBiaya(), data.getHargaBiaya(),data.getPembayaranBerkala());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    loadDataBiaya();
                    showMessage(BiayaActivity.this, "Berhasil Merubah Biaya");
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


    private void addDataBiaya(ListBiaya listBiaya){
        Call<Response> call = apiInterfaceBiaya.createBiaya(listBiaya.getDescBiaya(), listBiaya.getHargaBiaya(), listBiaya.getPembayaranBerkala());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    loadDataBiaya();
                    showMessage(BiayaActivity.this, "Berhasil Menambahkan Biaya");
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

    public interface EditListener{
        void onEditClicker(ListBiaya listBiaya);
    }
}
