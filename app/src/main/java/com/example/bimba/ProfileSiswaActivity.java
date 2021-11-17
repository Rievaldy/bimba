package com.example.bimba;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.DataSiswaAdapter;
import com.example.bimba.Adapter.ListPaketSiswaAdapter;
import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.Siswa.ApiInterfaceSiswa;
import com.example.bimba.RESTAPI.Tunggakan.ApiInterfaceTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.ResponseTunggakan;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.getPathFromUri;
import static com.example.bimba.Util.loadImage;
import static com.example.bimba.Util.showDialogEditText;
import static com.example.bimba.Util.showMessage;

public class ProfileSiswaActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private String path;
    private Siswa siswa;
    private ImageView ivFotoSiswa;
    private ImageView ivUploadSiswa;
    private TextView tvNisSiswa;
    private TextView tvNamaSiswa;
    private TextView tvJenisKelaminSiswa;
    private TextView tvTanggalLahir;
    private TextView tvTahunMasuk;
    private LinearLayout layoutNisSiswa;
    private LinearLayout layoutNamaSiswa;
    private LinearLayout layoutJenisKelaminSiswa;
    private LinearLayout layoutTanggalLahir;
    private LinearLayout layoutTahunMasuk;
    private ApiInterfaceSiswa apiInterfaceSiswa;
    private ApiInterfaceTunggakan apiInterfaceTunggakan;
    private RecyclerView rvPaketSiswa;
    private ProfileSiswaListener listener;
    private ArrayList<CompleteTunggakan> completeTunggakanArrayList;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_siswa);

        siswa = getIntent().getParcelableExtra("SISWA_EXTRA");
        ivFotoSiswa = findViewById(R.id.ivDetailSiswaImage);
        ivUploadSiswa = findViewById(R.id.ivDetailUploadSiswaImage);
        tvNisSiswa = findViewById(R.id.tvNisSiswa);
        tvNamaSiswa = findViewById(R.id.tvDetailNamaSiswa);
        tvJenisKelaminSiswa = findViewById(R.id.tvDetailJKSiswa);
        tvTanggalLahir = findViewById(R.id.tvDetailTanggalLahir);
        tvTahunMasuk = findViewById(R.id.tvDetailTahunMasuk);
        layoutNisSiswa = findViewById(R.id.layoutDetailNisSiswa);
        layoutNamaSiswa = findViewById(R.id.layoutDetailNamaSiswa);
        layoutJenisKelaminSiswa = findViewById(R.id.layoutDetailJKSiswa);
        layoutTanggalLahir = findViewById(R.id.layoutDetailTanggalLahirSiswa);
        layoutTahunMasuk = findViewById(R.id.layoutDetailTahunMasukSiswa);
        rvPaketSiswa = findViewById(R.id.rvPaketSiswa);
        listener = new ProfileSiswaListener() {
            @Override
            public void onClickPaket(CompleteTunggakan completeTunggakan) {

            }
        };


        apiInterfaceSiswa = ApiClient.getClient().create(ApiInterfaceSiswa.class);
        apiInterfaceTunggakan = ApiClient.getClient().create(ApiInterfaceTunggakan.class);
        loadUser();
        loadTagihanBySiswa();

        ivUploadSiswa.setOnClickListener(view -> SelectImage());
        layoutNamaSiswa.setOnClickListener(view -> siswa = (Siswa)showDialogEditText(ProfileSiswaActivity.this, "Ubah Nama Siswa", "nama", tvNamaSiswa, siswa));
        layoutJenisKelaminSiswa.setOnClickListener(view -> siswa = (Siswa)showDialogEditText(ProfileSiswaActivity.this, "Ubah Jenis Kelamin Siswa", "jeniskelamin", tvJenisKelaminSiswa, siswa));
        layoutTanggalLahir.setOnClickListener(view -> siswa = (Siswa)showDialogEditText(ProfileSiswaActivity.this, "Ubah Tanggal Lahir", "calendar", tvTanggalLahir, siswa));

    }

    private void loadUser(){
        loadImage(siswa.getFotoSiswa(), ivFotoSiswa, ProfileSiswaActivity.this);
        tvNisSiswa.setText(String.valueOf(siswa.getNis()));
        tvNamaSiswa.setText(siswa.getFirstName() + " " +siswa.getLastName());
        tvJenisKelaminSiswa.setText(siswa.getJenisKelamin());
        tvTanggalLahir.setText(siswa.getTanggalLahir());
        tvTahunMasuk.setText(siswa.getTahunMasuk());
    }

    private void loadTagihanBySiswa(){
        Call<ResponseTunggakan> call = apiInterfaceTunggakan.readTunggakan(0,siswa.getNis(),0, null, 0);
        call.enqueue(new Callback<ResponseTunggakan>() {
            @Override
            public void onResponse(Call<ResponseTunggakan> call, retrofit2.Response<ResponseTunggakan> response) {
                if(response.isSuccessful()){
                    completeTunggakanArrayList = response.body().getData();
                    if(response.body().getData() != null){
                        prepareRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTunggakan> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void updateUser(){
        Log.d("tes", "updateUser: Running" );
        RequestBody requestFile = null;
        String filename = "none";
        String name = siswa.getFirstName()+"-"+siswa.getLastName();
        if(path != null){
            Log.d("update", "updateUser: " + siswa.getFotoSiswa());
            File file = new File(siswa.getFotoSiswa());
            filename = file.getName().contains(".jpeg") ? name + ".jpeg" : name + ".jpg";
            requestFile = RequestBody.create(MediaType.parse("multipart/form-file"), file);
        }else{
            requestFile = RequestBody.create(MultipartBody.FORM,"");
        }
        Call<Response> call = apiInterfaceSiswa.updateSiswa(
                MultipartBody.create(MediaType.parse("multipart/form-data"), String.valueOf(siswa.getNis())),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getFirstName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getLastName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getJenisKelamin()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getTanggalLahir()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getTahunMasuk()),
                MultipartBody.Part.createFormData("foto_siswa", filename,requestFile)
        );
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("TAG", "onResponse: Running" );
                if(response.isSuccessful()){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMessage(ProfileSiswaActivity.this, "Berhasil Merubah Data");
                            backToHome();
                        }
                    }, 1000L);

                }else{
                    Log.d("tes", "onResponse: update Failed");
                    Log.d("tes", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("tes", "onFailure: " + t.getMessage());
            }
        });
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void prepareRecycleView(){
        ListPaketSiswaAdapter dataSiswaAdapter = new ListPaketSiswaAdapter(ProfileSiswaActivity.this, completeTunggakanArrayList, listener);
        mManager = new LinearLayoutManager(ProfileSiswaActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvPaketSiswa.setLayoutManager(mManager);
        rvPaketSiswa.setAdapter(dataSiswaAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage();
            } else {
                Toast.makeText(this, "Tidak Mendapatkan Hak Akses Melihat Gambar",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void backToHome(){
        startActivity(new Intent(ProfileSiswaActivity.this, SiswaActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ){
            Uri uri = data.getData();
            path = getPathFromUri(this, uri);
            loadImage(uri.toString(), ivFotoSiswa, ProfileSiswaActivity.this);
            siswa.setFotoSiswa(path);
        }
    }


    @Override
    public void onBackPressed() {
        updateUser();
    }

    public interface ProfileSiswaListener{
        void onClickPaket(CompleteTunggakan completeTunggakan);
    }
}
