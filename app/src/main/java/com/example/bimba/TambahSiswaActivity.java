package com.example.bimba;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bimba.Model.Siswa;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.Siswa.ApiInterfaceSiswa;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.getPathFromUri;
import static com.example.bimba.Util.loadImage;

public class TambahSiswaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private String path;
    private ApiInterfaceSiswa apiInterfaceSiswa;
    private ImageView fotoSiswa;
    private ImageView uploadFotoSiswa;
    private RadioGroup rgKelaminSiswa;
    private EditText etFirstNameSiswa;
    private EditText etLastNameSiswa;
    private Button submitSiswa;
    private Siswa siswa;
    private EditText etTanggalLahir;
    private LinearLayout layoutCalendar;
    private SessionManagement sessionManagement;
    private  int idUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_siswa);

        sessionManagement = new SessionManagement(getApplicationContext());
        idUser = sessionManagement.getUserIdSession();

        apiInterfaceSiswa = ApiClient.getClient().create(ApiInterfaceSiswa.class);
        layoutCalendar = findViewById(R.id.layout_tanggal_lahir);
        etTanggalLahir= findViewById(R.id.et_tangalLahir);
        etFirstNameSiswa = findViewById(R.id.et_first_name_siswa);
        etLastNameSiswa = findViewById(R.id.et_last_name_siswa);
        rgKelaminSiswa = findViewById(R.id.rgKelaminSiswa);
        fotoSiswa = findViewById(R.id.ivDetailSiswaImage);
        uploadFotoSiswa = findViewById(R.id.ivDetailUploadSiswaImage);
        siswa = new Siswa();
        submitSiswa = findViewById(R.id.btn_simpan_siswa);



        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showDialogEditText(TambahSiswaActivity.this, "Tanggal Lahir", "calendar", etTanggalLahir, siswa);
            }
        });

        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showDialogEditText(TambahSiswaActivity.this, "Tanggal Lahir", "calendar", etTanggalLahir,siswa);
            }
        });

        uploadFotoSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(TambahSiswaActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SelectImage();
                }else{
                    requestStoragePermission();
                }
            }
        });

        submitSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siswa = new Siswa();
                siswa.setFotoSiswa(path);
                siswa.setFirstName(etFirstNameSiswa.getText().toString());
                siswa.setLastName(etLastNameSiswa.getText().toString());
                RadioButton rb = findViewById(rgKelaminSiswa.getCheckedRadioButtonId());
                siswa.setJenisKelamin(rb.getText().toString());
                siswa.setTanggalLahir(etTanggalLahir.getText().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = sdf.format(Calendar.getInstance().getTime());
                siswa.setTahunMasuk(currentDate);
                submit(siswa);
            }
        });
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("We Need Permission to Access Your Galery to upload your File into Our Database")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(TambahSiswaActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ){
            Uri uri = data.getData();
            path = getPathFromUri(this, uri);
            loadImage(uri.toString(), fotoSiswa, TambahSiswaActivity.this);
        }
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

    private void submit(Siswa siswa){
        RequestBody requestFile = null;
        String filename = "none";
        String name = siswa.getFirstName()+"-"+siswa.getLastName();
        if(path != null){
            File file = new File(path);
            filename = file.getName().contains(".jpeg") ? name + ".jpeg" : name + ".jpg";
            requestFile = RequestBody.create(MediaType.parse("multipart/form-file"), file);
            Log.d("tes", "submit: " + filename);
        }else{
            requestFile = RequestBody.create(MultipartBody.FORM,"");
        }
        Call<Response> call = apiInterfaceSiswa.createSiswa(
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getFirstName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getLastName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getJenisKelamin()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getTanggalLahir()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), siswa.getTahunMasuk()),
                MultipartBody.Part.createFormData("foto_siswa", filename,requestFile),
                MultipartBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idUser))
        );

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    Log.d("tes", "onResponse: isSuccess " + response.body().getMessage());
                    backToMainActivity();
                }else{
                    Log.d("tes", "onResponse: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("tes", "onFailure: " + t.getMessage());
            }
        });
    }

    public void backToMainActivity(){
        Intent intent = new Intent(TambahSiswaActivity.this, SiswaActivity.class);
        intent.putExtra("USER_ID",idUser);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
