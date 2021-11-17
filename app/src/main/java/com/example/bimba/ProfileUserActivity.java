package com.example.bimba;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.bimba.Util.getPathFromUri;
import static com.example.bimba.Util.loadImage;
import static com.example.bimba.Util.showDialogEditText;
import static com.example.bimba.Util.showMessage;

public class ProfileUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private String path;
    private User user;
    private ImageView ivFotoUser;
    private ImageView ivUploadUser;
    private TextView tvIdUser;
    private TextView tvEmailUser;
    private TextView tvNamaUser;
    private TextView tvJenisKelamin;
    private TextView tvNoHpUser;
    private TextView tvAlamat;
    private LinearLayout layoutIdUser;
    private LinearLayout layoutEmailUser;
    private LinearLayout layoutNamaUser;
    private LinearLayout layoutJenisKelamin;
    private LinearLayout layoutNoHpUser;
    private LinearLayout layoutAlamat;
    private ApiInterfaceUser apiInterfaceUser;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra("USER_EXTRA");
        Log.d("TAG", "onCreate:  " + user.getIdUser());
        ivFotoUser = findViewById(R.id.ivDetailUserImage);
        ivUploadUser = findViewById(R.id.ivDetailUploadUserImage);
        tvIdUser = findViewById(R.id.tvIdUser);
        tvEmailUser = findViewById(R.id.tvEmailUser);
        tvNamaUser = findViewById(R.id.tvNamaUser);
        tvJenisKelamin = findViewById(R.id.tvDetailJKUser);
        tvNoHpUser = findViewById(R.id.tvDetailTeleponUser);
        tvAlamat = findViewById(R.id.tvDetailAlamatUser);
        layoutIdUser = findViewById(R.id.layoutDetailIdUser);
        layoutEmailUser = findViewById(R.id.layoutDetailEmailUser);
        layoutNamaUser = findViewById(R.id.layoutDetailNamaUser);
        layoutJenisKelamin = findViewById(R.id.layoutDetailJKUser);
        layoutNoHpUser = findViewById(R.id.layoutDetailTeleponUser);
        layoutAlamat = findViewById(R.id.layoutDetailAlamatUser);
        apiInterfaceUser = ApiClient.getClient().create(ApiInterfaceUser.class);

        loadUser();

        ivUploadUser.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                SelectImage();
            }else{
                requestStoragePermission();
            }

        });

        layoutNamaUser.setOnClickListener(view -> {
            user = (User) showDialogEditText(ProfileUserActivity.this, "Ubah Nama", "nama", tvNamaUser, user);
        });
        layoutJenisKelamin.setOnClickListener(view -> user = (User)showDialogEditText(ProfileUserActivity.this, "Ubah Jenis Kelamin", "jeniskelamin", tvJenisKelamin, user));
        layoutNoHpUser.setOnClickListener(view -> user = (User)showDialogEditText(ProfileUserActivity.this, "Ubah No HP", "nohp", tvNoHpUser, user));
        layoutAlamat.setOnClickListener(view ->user = (User) showDialogEditText(ProfileUserActivity.this, "Ubah Alamat", "alamat", tvAlamat, user));
    }


    private void loadUser(){
        loadImage(user.getFotoProfile(), ivFotoUser, ProfileUserActivity.this);
        tvIdUser.setText(String.valueOf(user.getIdUser()));
        tvEmailUser.setText(user.getEmailUser());
        tvNamaUser.setText(user.getFirstName() + " " +user.getLastName());
        tvJenisKelamin.setText(user.getJenisKelamin());
        tvNoHpUser.setText(user.getNoHp());
        tvAlamat.setText(user.getAlamat()+", RT."+user.getRt()+"/RW."+user.getRw());
    }

    private void updateUser(){
        Log.d("tes", "updateUser: Running" );
        RequestBody requestFile = null;
        String filename = "none";
        String name = user.getFirstName()+"-"+user.getLastName();
        if(path != null){
            Log.d("update", "updateUser: " + user.getFotoProfile());
            File file = new File(user.getFotoProfile());
            filename = file.getName().contains(".jpeg") ? name + ".jpeg" : name + ".jpg";
            requestFile = RequestBody.create(MediaType.parse("multipart/form-file"), file);
        }else{
            requestFile = RequestBody.create(MultipartBody.FORM,"");
        }
        Call<Response> call = apiInterfaceUser.updateUser(
                MultipartBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user.getIdUser())),
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
                Log.d("TAG", "onResponse: Running" );
                if(response.isSuccessful()){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMessage(ProfileUserActivity.this, "Berhasil Merubah Data");
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

    private void backToHome(){
        startActivity(new Intent(ProfileUserActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ){
            Uri uri = data.getData();
            path = getPathFromUri(this, uri);
            loadImage(uri.toString(), ivFotoUser, ProfileUserActivity.this);
            user.setFotoProfile(path);
        }
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("We Need Permission to Access Your Galery to upload your File into Our Database")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(ProfileUserActivity.this, PERMISSIONS_STORAGE,STORAGE_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage();
            } else {
                Toast.makeText(this, "Tidak Mendapatkan Hak Akses Storage",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        updateUser();
    }
}
