package com.example.bimba;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bimba.Model.User;
import com.example.bimba.Model.UserAccess;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;
import com.example.bimba.RESTAPI.UserAccess.ApiInterfaceUserAccess;
import com.example.bimba.RESTAPI.UserAccess.ResponseUserAccess;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.getPathFromUri;
import static com.example.bimba.Util.loadImage;
import static com.example.bimba.Util.showMessage;

public class RegisterUser extends AppCompatActivity{

    private static final String TAG = "registrasi";
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int STORAGE_PERMISSION_CODE = 1;

    private String path;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etNotelp;
    private EditText etAlamat;
    private EditText etRT;
    private EditText etRW;
    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup rgKelamin;
    private ProgressDialog loading;
    private ImageView fotoProfile;

    private ApiInterfaceUserAccess apiInterfaceUserAccess;
    private ApiInterfaceUser apiInterfaceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        rgKelamin = findViewById(R.id.rgKelamin);
        etNotelp = findViewById(R.id.et_notelp);
        etAlamat = findViewById(R.id.et_alamat);
        etRT = findViewById(R.id.et_rt);
        etRW = findViewById(R.id.et_rw);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        fotoProfile = findViewById(R.id.ivDetailUserImage);
        fotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SelectImage();
                }else{
                    requestStoragePermission();
                }

            }
        });

        apiInterfaceUserAccess = ApiClient.getClient().create(ApiInterfaceUserAccess.class);
        apiInterfaceUser = ApiClient.getClient().create(ApiInterfaceUser.class);

        //Fungsi Button
        findViewById(R.id.btn_simpan).setOnClickListener(v -> {

            Integer buttonId = rgKelamin.getCheckedRadioButtonId();
            RadioButton rbkelamin = findViewById(buttonId);
            Log.d("Kelamin", rbkelamin.getText().toString());

            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String jenisKelamin = rbkelamin.getText().toString().toLowerCase();
            String noTelp = etNotelp.getText().toString().toLowerCase();
            String alamat = etAlamat.getText().toString().toLowerCase();
            String rt = etRT.getText().toString().toLowerCase();
            String rw = etRW.getText().toString().toLowerCase();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            //validasi saat input
            if (firstName.equals("")) {
                etFirstName.setError("Silahkan Masukan Nama Awal");
                etFirstName.requestFocus();
            } else if (noTelp.equals("")) {
                etNotelp.setError("Silahkan Masukan No. Telepon");
                etNotelp.requestFocus();
            } else if (alamat.equals("")) {
                etAlamat.setError("Silahkan Masukan Alamat");
                etAlamat.requestFocus();
            } else if (rt.equals("")) {
                etRT.setError("Silahkan Masukan RT");
                etRT.requestFocus();
            } else if (rw.equals("")) {
                etRW.setError("Silahkan Masukan RW");
                etRW.requestFocus();
            } else if (email.equals("")) {
                etEmail.setError("Silahkan Masukan Email Anda");
                etEmail.requestFocus();
            } else if (password.equals("")) {
                etPassword.setError("Silahkan Masukan Password");
                etPassword.requestFocus();
            } else {
                loading = ProgressDialog.show(RegisterUser.this,
                        null,
                        "please wait...",
                        true,
                        false);

                //fungsi menambahkan data ke firebase dengan memanfaatkan class request.java
                submitUser(
                        new UserAccess(
                                email,
                                password,
                                3
                        ),
                        new User(
                                email,
                                firstName,
                                lastName,
                                jenisKelamin,
                                noTelp,
                                alamat,
                                rt,
                                rw,
                                ""
                        )
                );
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
                            ActivityCompat.requestPermissions(RegisterUser.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
            Log.d(TAG, "onActivityResult: "+ path);
            loadImage(uri.toString(), fotoProfile, RegisterUser.this);
        }
    }

    private void submitUser(UserAccess userAccess, User user) {
        Call<ResponseUserAccess> call = apiInterfaceUserAccess.getUserAccess(0, userAccess.getEmailUser(), null, null, null, null);
        call.enqueue(new Callback<ResponseUserAccess>() {

            @Override
            public void onResponse(Call<ResponseUserAccess> call, retrofit2.Response<ResponseUserAccess> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        showMessage(RegisterUser.this, "Email sudah tersedia, mohon gunakan email lainnya");
                        loading.dismiss();
                    }else{
                        createUserAccess(user, userAccess);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAccess> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void createUser( User user){
        RequestBody requestFile = null;
        String filename = "none";
        String name = user.getFirstName()+"-"+user.getLastName();
        if(path != null){
            File file = new File(path);
            filename = file.getName().contains(".jpeg") ? name + ".jpeg" : name + ".jpg";
            requestFile = RequestBody.create(MediaType.parse("multipart/form-file"), file);
        }else{
            requestFile = RequestBody.create(MultipartBody.FORM,"");
        }
        Call<Response> call = apiInterfaceUser.createUser(
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getEmailUser()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getFirstName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getLastName()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getJenisKelamin()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getNoHp()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getAlamat()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getRt()),
                MultipartBody.create(MediaType.parse("multipart/form-data"), user.getRw()),
                MultipartBody.Part.createFormData("foto_profile", filename,requestFile)
        );
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    loading.dismiss();
                    showMessage(RegisterUser.this, "Berhasil Membuat Akun");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    },1000);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void createUserAccess(User user, UserAccess userAccess){

        Call<Response> call = apiInterfaceUserAccess.createUserAccess(
                userAccess.getEmailUser(), userAccess.getPasswordUser(), userAccess.getIdLevel()
        );
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    createUser(user);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
