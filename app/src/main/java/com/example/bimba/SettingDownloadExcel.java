package com.example.bimba;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bimba.Model.Siswa;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.HistoryPembayaran.ApiInterfaceHistoryPembayaran;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.HistoryPembayaran.ResponseHistoryPembayaran;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingDownloadExcel extends AppCompatActivity {
    private LinearLayout layoutStart, layoutEnd;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private EditText etStart, etEnd;
    private Button download;
    private ApiInterfaceHistoryPembayaran apiInterfaceHistoryPembayaran;
    private ArrayList<CompleteHistoryPembayaran> completeHistoryPembayaranArrayList;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_excel);
        layoutStart = findViewById(R.id.layout_tanggal_start);
        layoutEnd = findViewById(R.id.layout_tanggal_end);
        etStart = findViewById(R.id.et_tanggalStart);
        etEnd = findViewById(R.id.et_tanggalEnd);
        download = findViewById(R.id.downloadNow);

        layoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showDialogEditText(SettingDownloadExcel.this, "Tanggal Awal", "calendar", etStart,new Siswa());
            }
        });

        layoutEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.showDialogEditText(SettingDownloadExcel.this, "Tanggal Akhir", "calendar", etEnd,new Siswa());
            }
        });
        apiInterfaceHistoryPembayaran = ApiClient.getClient().create(ApiInterfaceHistoryPembayaran.class);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etStart.getText().toString().isEmpty()){
                    etStart.setError("Tidak Boleh Kosong");
                }else if(etEnd.getText().toString().isEmpty()){
                    etEnd.setError("Tidak Boleh Kosong");
                }else{
                    String start = changeFormat(etStart.getText().toString());
                    String end = changeFormat(etEnd.getText().toString());
                    if(ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        loadDataHistory(start, end);
                    }else{
                        requestStoragePermission();
                    }
                }
            }
        });
    }

    private void loadDataHistory(String start, String end){
        Call<ResponseHistoryPembayaran> call = apiInterfaceHistoryPembayaran.readHistoryPembayaran("0",0,1, 0, start, end);
        call.enqueue(new Callback<ResponseHistoryPembayaran>() {
            @Override
            public void onResponse(Call<ResponseHistoryPembayaran> call, Response<ResponseHistoryPembayaran> response) {
                if(response.isSuccessful()){
                    if(response.body().getData() != null){
                        completeHistoryPembayaranArrayList = response.body().getData();
                        exportToExcel();
                    }else{
                        Util.showMessage(SettingDownloadExcel.this, "Maaf Tidak terdapat data pada tanggal" + etStart.getText().toString() +" - "+etEnd.getText().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryPembayaran> call, Throwable t) {

            }
        });
    }

    private String changeFormat(String oldDateString){
        final String OLD_FORMAT = "dd-MM-yyyy";
        final String NEW_FORMAT = "yyyy-MM-dd";

        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(oldDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d) ;

        return newDateString;
    }

    private void exportToExcel(){
        File filePath = new File(Environment.getDownloadCacheDirectory() + File.separator + "Laporan.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Laporan Pemasukan");
        HSSFRow hssfRow = hssfSheet.createRow(0);

        HSSFCell CellNo = hssfRow.createCell(0);
        CellNo.setCellValue("No.");

        HSSFCell CellIdPembayaran = hssfRow.createCell(1);
        CellIdPembayaran.setCellValue("Id History Pembayaran.");

        HSSFCell CellIdTagihan = hssfRow.createCell(2);
        CellIdTagihan.setCellValue("Id Tagihan.");

        HSSFCell jumlahSetoran = hssfRow.createCell(3);
        jumlahSetoran.setCellValue("Jumlah Setoran");

        addRowExcel(hssfSheet, completeHistoryPembayaranArrayList);

        try{
            if(!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }catch (Exception e){
            Log.d("TAG", "exportToExcel: " + e.getMessage());
        }
    }

    private void addRowExcel(HSSFSheet hssfSheet, ArrayList<CompleteHistoryPembayaran> completeHistoryPembayaran){
        int total = 0;
        for (int i = 1; i <= completeHistoryPembayaran.size() ; i++){
            HSSFRow hssfRow = hssfSheet.createRow(i);

            HSSFCell CellNo = hssfRow.createCell(0);
            CellNo.setCellValue(i);

            HSSFCell CellIdPembayaran = hssfRow.createCell(1);
            CellIdPembayaran.setCellValue(completeHistoryPembayaranArrayList.get(i-1).getHistoryPembayaran().getIdHistoryPembayaran());

            HSSFCell CellIdTagihan = hssfRow.createCell(2);
            CellIdTagihan.setCellValue(completeHistoryPembayaranArrayList.get(i-1).getHistoryPembayaran().getIdTunggakan());

            HSSFCell jumlahSetoran = hssfRow.createCell(3   );
            jumlahSetoran.setCellValue(completeHistoryPembayaranArrayList.get(i-1).getHistoryPembayaran().getJumlahDisetorkan());
            total+= completeHistoryPembayaranArrayList.get(i-1).getHistoryPembayaran().getJumlahDisetorkan();
        }

        HSSFRow totalRow = hssfSheet.createRow(completeHistoryPembayaran.size()+1);

        HSSFCell CellTitle = totalRow.createCell(0);
        CellTitle.setCellValue("TOTAL");

        HSSFCell cellTotal = totalRow.createCell(3);
        cellTotal.setCellValue(total);

    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("We Need Permission to Access Your Galery to upload your File into Our Database")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(SettingDownloadExcel.this, PERMISSIONS_STORAGE,STORAGE_PERMISSION_CODE);
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
                String start = changeFormat(etStart.getText().toString());
                String end = changeFormat(etEnd.getText().toString());
                loadDataHistory(start, end);
            } else {
                Toast.makeText(this, "Tidak Mendapatkan Hak Akses Storage",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
