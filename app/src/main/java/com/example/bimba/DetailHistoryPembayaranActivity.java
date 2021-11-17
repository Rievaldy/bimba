package com.example.bimba;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;


import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.HistoryPembayaran.ApiInterfaceHistoryPembayaran;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailHistoryPembayaranActivity extends AppCompatActivity {
    private TextView idHistoryPembayaran, idTagihan, namaPaket, namaSiswa,namaUser, jumlahPembayaran, tanggalTransaksi;
    private FloatingActionButton btnDownloadPdf;
    private CompleteHistoryPembayaran completeHistoryPembayaran;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private LinearLayout layout;
    private Button btnApproval;
    private SessionManagement sessionManagement;
    private ApiInterfaceHistoryPembayaran apiInterfaceHistoryPembayaran;
    private String dirpath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_pembayaran);
        idHistoryPembayaran = findViewById(R.id.et_id_history_pembayaran_dtr);
        idTagihan = findViewById(R.id.et_id_tagihan_pembayaran_dtr);
        namaPaket = findViewById(R.id.et_nama_paket_dtr);
        namaSiswa = findViewById(R.id.et_nama_siswa_dtr);
        namaUser = findViewById(R.id.et_nama_user_dtr);
        jumlahPembayaran = findViewById(R.id.et_jumlah_pembayaran_dtr);
        tanggalTransaksi = findViewById(R.id.et_tanggal_pembayaran_dtr);
        layout = findViewById(R.id.layout_detail_history_pembayaran);
        btnApproval = findViewById(R.id.btnApprovalPembayaran);
        sessionManagement = new SessionManagement(getApplicationContext());
        completeHistoryPembayaran = getIntent().getParcelableExtra("EXTRA_COMPLETE_HISTORY_PEMBAYARAN");
        apiInterfaceHistoryPembayaran = ApiClient.getClient().create(ApiInterfaceHistoryPembayaran.class);

        idHistoryPembayaran.setText(completeHistoryPembayaran.getHistoryPembayaran().getIdHistoryPembayaran());
        idTagihan.setText(String.valueOf(completeHistoryPembayaran.getTunggakan().getIdTunggakan()));
        namaPaket.setText(completeHistoryPembayaran.getJenisPaket().getDescPaket());
        namaSiswa.setText(completeHistoryPembayaran.getSiswa().getFirstName() + " "+ completeHistoryPembayaran.getSiswa().getLastName());
        namaUser.setText(completeHistoryPembayaran.getUser().getFirstName() + " "+ completeHistoryPembayaran.getUser().getLastName()    );
        jumlahPembayaran.setText("Rp. " + completeHistoryPembayaran.getHistoryPembayaran().getJumlahDisetorkan());
        tanggalTransaksi.setText(completeHistoryPembayaran.getHistoryPembayaran().getTanggalTransaksi());
        btnDownloadPdf = findViewById(R.id.btn_downloadPdf);


        if(sessionManagement.getUserAccessSession() == 3){
            btnApproval.setVisibility(View.GONE);
            btnDownloadPdf.setVisibility(View.VISIBLE);

        }else if(sessionManagement.getUserAccessSession() == 1){
            btnApproval.setVisibility(View.VISIBLE);
            btnDownloadPdf.setVisibility(View.GONE);

            if(completeHistoryPembayaran.getHistoryPembayaran().getApproved() == 1){
                btnApproval.setText("Batalkan Persetujuan");
            }
        }else{
            btnApproval.setVisibility(View.GONE);
            btnDownloadPdf.setVisibility(View.GONE);
        }
        btnDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    layoutToImage(layout);
                    try {
                        imageToPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    requestStoragePermission();
                }
            }
        });

        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateApprovalStatus();
            }
        });
    }

    public void layoutToImage(View view) {
        // convert view group to bitmap
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bm = layout.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/" +completeHistoryPembayaran.getHistoryPembayaran().getIdHistoryPembayaran()+".pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateApprovalStatus(){
        int approval = completeHistoryPembayaran.getHistoryPembayaran().getApproved() == 0 ? 1 : 0;
        Call<Response> call = apiInterfaceHistoryPembayaran
                .updateHistoryPembayaran(
                        completeHistoryPembayaran.getHistoryPembayaran().getIdHistoryPembayaran(),
                        completeHistoryPembayaran.getHistoryPembayaran().getIdTunggakan(),
                        completeHistoryPembayaran.getHistoryPembayaran().getJumlahDisetorkan(),
                        approval);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    Util.showMessage(DetailHistoryPembayaranActivity.this, "Berhasil Merubah Approval");
                    goBackToListTransaksi();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
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
                            ActivityCompat.requestPermissions(DetailHistoryPembayaranActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
                layoutToImage(layout);
                try {
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Tidak Mendapatkan Hak Akses Melihat Gambar",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goBackToListTransaksi(){
        startActivity(new Intent(DetailHistoryPembayaranActivity.this, TransaksiActivity.class));
        finish();
    }
}

