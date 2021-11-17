package com.example.bimba;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Adapter.ListBiayaDetailPaketAdapter;
import com.example.bimba.Model.HistoryPembayaran;
import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.Model.Siswa;
import com.example.bimba.Model.Tunggakan;
import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.ApiMidtrans;
import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.DetailPaket.ApiInterfaceDetailPaket;
import com.example.bimba.RESTAPI.HistoryPembayaran.ApiInterfaceHistoryPembayaran;
import com.example.bimba.RESTAPI.Midtrans.ApiInterfaceMidtrans;
import com.example.bimba.RESTAPI.Midtrans.ResponseMidtrans;
import com.example.bimba.RESTAPI.Paket.ApiInterfaceJenisPaket;
import com.example.bimba.RESTAPI.Paket.ResponsePaket;
import com.example.bimba.RESTAPI.Siswa.ApiInterfaceSiswa;
import com.example.bimba.RESTAPI.Siswa.ResponseSiswa;
import com.example.bimba.RESTAPI.Tunggakan.ApiInterfaceTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.ResponseTunggakan;
import com.example.bimba.RESTAPI.User.ApiInterfaceUser;
import com.example.bimba.RESTAPI.User.ResponseUser;
import com.google.android.material.textfield.TextInputEditText;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.findDifference;
import static com.example.bimba.Util.showMessage;

public class DaftarPaketActivity extends AppCompatActivity {

    private final String BASE_URL_MERCHANT = "https://bimbaindonesia.000webhostapp.com/rest_bimba/webhook/webhook_midtrans.php/";
    private JenisPaket jenisPaket;
    private User user;
    private Tunggakan tunggakan;
    private Spinner spSiswa, spPaket;
    private RecyclerView rvListBiaya;
    private EditText etTotalBiaya;
    private TextInputEditText etSetoran;
    private TextView lamaPembelajaran;
    private Button daftar;
    private SessionManagement sessionManagement;
    private LinearLayoutManager mManager;

    private ApiInterfaceDetailPaket apiInterfaceDetailPaket;
    private ApiInterfaceJenisPaket apiInterfaceJenisPaket;
    private ApiInterfaceSiswa apiInterfaceSiswa;
    private ApiInterfaceHistoryPembayaran apiInterfaceHistoryPembayaran;
    private ApiInterfaceTunggakan apiInterfaceTunggakan;
    private ApiInterfaceUser apiInterfaceUser;
    private ApiInterfaceMidtrans apiInterfaceMidtrans;

    private ArrayList<Siswa> siswaArrayList;
    private ArrayList<JenisPaket> jenisPaketArrayList;
    private ArrayList<ListBiaya> listBiayaArrayList;
    private HistoryPembayaran historyPembayaran;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_paket);
        jenisPaket = getIntent().getParcelableExtra("EXTRA_PAKET");
        tunggakan = new Tunggakan();
        tunggakan.setIdPaket(jenisPaket.getIdPaket());
        spSiswa = findViewById(R.id.spin_siswa);
        spPaket = findViewById(R.id.spin_paket);
        rvListBiaya = findViewById(R.id.rvListBiaya);
        etTotalBiaya = findViewById(R.id.et_total_biaya);
        etSetoran = findViewById(R.id.etPembayaranPertama);
        lamaPembelajaran = findViewById(R.id.lamaPembelajaran);

        daftar = findViewById(R.id.btnDaftar);
        apiInterfaceDetailPaket = ApiClient.getClient().create(ApiInterfaceDetailPaket.class);
        apiInterfaceJenisPaket = ApiClient.getClient().create(ApiInterfaceJenisPaket.class);
        apiInterfaceSiswa = ApiClient.getClient().create(ApiInterfaceSiswa.class);
        apiInterfaceHistoryPembayaran = ApiClient.getClient().create(ApiInterfaceHistoryPembayaran.class);
        apiInterfaceTunggakan = ApiClient.getClient().create(ApiInterfaceTunggakan.class);
        apiInterfaceUser = ApiClient.getClient().create(ApiInterfaceUser.class);
        apiInterfaceMidtrans = ApiMidtrans.createService(ApiInterfaceMidtrans.class, "SB-Mid-server-22EyrGFZV-X4qWbLHKRTtfND", null);
        sessionManagement = new SessionManagement(getApplicationContext());
        historyPembayaran = new HistoryPembayaran();
        historyPembayaran.setApproved(0);

        loadDataSiswa();
        loadDataPaket();
        loadDataRincianBiaya(jenisPaket);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDaftar(tunggakan);
            }
        });

    }

    private void loadDataSiswa(){
        Call<ResponseSiswa> call = apiInterfaceSiswa.getSiswa(0, sessionManagement.getUserIdSession(), null, null, null);
        call.enqueue(new Callback<ResponseSiswa>() {
            @Override
            public void onResponse(Call<ResponseSiswa> call, Response<ResponseSiswa> response) {
                if(response.isSuccessful()){
                     siswaArrayList = response.body().getData();
                     settingSpinnerSiswa(siswaArrayList);
                }else{
                    Log.d("TAG", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseSiswa> call, Throwable t) {
                Log.d("TAG", "onResponse: Failed" + t.getMessage());
            }
        });
    }

    private void loadDataPaket(){
        Call<ResponsePaket> call = apiInterfaceJenisPaket.getJenisPaket(0, null, null, null);
        call.enqueue(new Callback<ResponsePaket>() {
            @Override
            public void onResponse(Call<ResponsePaket> call, Response<ResponsePaket> response) {
                if(response.isSuccessful()){
                    jenisPaketArrayList = response.body().getData();
                    settingPaket(jenisPaketArrayList);
                }
            }

            @Override
            public void onFailure(Call<ResponsePaket> call, Throwable t) {

            }
        });
    }

    private void loadDataRincianBiaya(JenisPaket jenisPaket){
        Call<ResponseBiaya> call = apiInterfaceDetailPaket.getDetailPaket(jenisPaket.getIdPaket());
        call.enqueue(new Callback<ResponseBiaya>() {
            @Override
            public void onResponse(Call<ResponseBiaya> call, Response<ResponseBiaya> response) {
                if(response.isSuccessful()){
                    listBiayaArrayList = response.body().getData();
                     sumTotalBiaya(listBiayaArrayList);
                     prepareRvRincianBiaya(listBiayaArrayList);
                }
            }

            @Override
            public void onFailure(Call<ResponseBiaya> call, Throwable t) {

            }
        });

    }

    private void loadDataUser(TransactionRequest transactionRequest){
        Call<ResponseUser> call = apiInterfaceUser.getUser(sessionManagement.getUserIdSession(), null,null, null, null);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if(response.isSuccessful()){
                    user = response.body().getData().get(0);
                    fillCustomerDetail(transactionRequest,user);
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
    }


    private void settingSpinnerSiswa(ArrayList<Siswa> siswaArrayList){
        Siswa dumySiswa = new Siswa();
        dumySiswa.setNis(0);
        dumySiswa.setFirstName("Pilih");
        dumySiswa.setLastName("Siswa");
        siswaArrayList.add(0, dumySiswa);
        ArrayAdapter<Siswa> adapterSiswa = new ArrayAdapter<Siswa>(DaftarPaketActivity.this, R.layout.style_spinner, siswaArrayList);
        spSiswa.setAdapter(adapterSiswa);
        spSiswa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tunggakan.setNis(siswaArrayList.get(i).getNis());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void settingPaket(ArrayList<JenisPaket> paketArrayList){
        JenisPaket dumyPaket = new JenisPaket();
        dumyPaket.setIdPaket(0);
        dumyPaket.setDescPaket("Pilih Paket");
        dumyPaket.setMasaPembelajaran(0);
        paketArrayList.add(0, dumyPaket);

        lamaPembelajaran.setText(jenisPaket.getMasaPembelajaran() + " Bulan");
        ArrayAdapter<JenisPaket> arrayAdapter = new ArrayAdapter<>(DaftarPaketActivity.this, R.layout.style_spinner, paketArrayList);
        spPaket.setAdapter(arrayAdapter);
        for(int i = 1; i < paketArrayList.size(); i++){
            if(paketArrayList.get(i).getIdPaket() == jenisPaket.getIdPaket()){
                spPaket.setSelection(i);
            }
        }
        spPaket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tunggakan.setIdPaket(paketArrayList.get(i).getIdPaket());
                lamaPembelajaran.setText(paketArrayList.get(i).getMasaPembelajaran() + " Bulan");
                jenisPaket = paketArrayList.get(i);
                loadDataRincianBiaya(paketArrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepareRvRincianBiaya(ArrayList<ListBiaya> listBiayaArrayList){
        ListBiayaDetailPaketAdapter listBiayaDetailPaketAdapter = new ListBiayaDetailPaketAdapter(DaftarPaketActivity.this,listBiayaArrayList , null);
        mManager = new LinearLayoutManager(DaftarPaketActivity.this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        rvListBiaya.setLayoutManager(mManager);
        rvListBiaya.setAdapter(listBiayaDetailPaketAdapter);
    }

    private void sumTotalBiaya(ArrayList<ListBiaya> listBiayaArrayList){
        if(listBiayaArrayList != null){
            int totalBiaya = 0;
            for(ListBiaya listBiaya : listBiayaArrayList){
                if(listBiaya.getPembayaranBerkala() == 0){
                    totalBiaya+=listBiaya.getHargaBiaya();
                }else{
                    totalBiaya+= (jenisPaket.getMasaPembelajaran() * listBiaya.getHargaBiaya());
                }

            }
            String total = "Rp. "+ totalBiaya;
            etTotalBiaya.setText(total);
            tunggakan.setTotalHarusBayar(totalBiaya);
        }
    }

    private void submitDaftar(Tunggakan tunggakan){
        if(tunggakan.getNis() == 0 ){
            showMessage(DaftarPaketActivity.this, "Harap Pilih Siswa");
        }else if(tunggakan.getIdPaket() == 0){
            showMessage(DaftarPaketActivity.this, "Harap Pilih Paket");
        }else if( etSetoran.getText().toString().isEmpty()){
            etSetoran.setError("Harap Isi Jumlah Setoran");
            etSetoran.requestFocus();
        }else if(Integer.valueOf(etSetoran.getText().toString()) < (tunggakan.getTotalHarusBayar() / jenisPaket.getMasaPembelajaran())){
            etSetoran.setError("Jumlah Minimum Pembayaran Rp. "+ (tunggakan.getTotalHarusBayar() / jenisPaket.getMasaPembelajaran()));
            etSetoran.requestFocus();
        }else{
            if(Integer.valueOf(etSetoran.getText().toString()) > tunggakan.getTotalHarusBayar()){
                etSetoran.setText(String.valueOf(tunggakan.getTotalHarusBayar()));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy-hh-mm-ss", Locale.getDefault());
            String currentDate = sdf.format(Calendar.getInstance().getTime());
            tunggakan.setTahunMasuk(currentDate);
            tunggakan.setBaruDibayarkan(0);
            openMidtransPayment();
        }
    }

    private void createTunggakan(){
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceTunggakan.createTunggakan(tunggakan.getNis(), tunggakan.getIdPaket(), tunggakan.getTahunMasuk(), tunggakan.getTotalHarusBayar(), tunggakan.getBaruDibayarkan());
        call.enqueue(new Callback<com.example.bimba.RESTAPI.Response>() {
            @Override
            public void onResponse(Call<com.example.bimba.RESTAPI.Response> call, Response<com.example.bimba.RESTAPI.Response> response) {
                if(response.isSuccessful()){
                    readTunggakan();
                    Log.d("TAG", "onResponse: Succcess");
                }
            }

            @Override
            public void onFailure(Call<com.example.bimba.RESTAPI.Response> call, Throwable t) {
                Log.d("TAG", "onResponse: Failed");
            }
        });
    }

    private void readTunggakan(){
        Call<ResponseTunggakan> call = apiInterfaceTunggakan.readTunggakan(0, tunggakan.getNis(), tunggakan.getIdPaket(), tunggakan.getTahunMasuk(),0);
        call.enqueue(new Callback<ResponseTunggakan>() {
            @Override
            public void onResponse(Call<ResponseTunggakan> call, Response<ResponseTunggakan> response) {
                if(response.isSuccessful()){
                    Log.d("tes", "onResponse: " + response.body().getData().toString());
                    tunggakan = response.body().getData().get(0).getTunggakan();
                    historyPembayaran.setIdTunggakan(tunggakan.getIdTunggakan());
                    historyPembayaran.setJumlahDisetorkan(Integer.valueOf(etSetoran.getText().toString()));
                    createHistoryPembayaran();
                }
            }

            @Override
            public void onFailure(Call<ResponseTunggakan> call, Throwable t) {

            }
        });
    }

    private void updateTagihan(){
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceTunggakan.updateTunggakan(tunggakan.getIdTunggakan(), tunggakan.getNis(), tunggakan.getIdPaket(), tunggakan.getTahunMasuk(), tunggakan.getTotalHarusBayar(), historyPembayaran.getJumlahDisetorkan());
        call.enqueue(new Callback<com.example.bimba.RESTAPI.Response>() {
            @Override
            public void onResponse(Call<com.example.bimba.RESTAPI.Response> call, Response<com.example.bimba.RESTAPI.Response> response) {
                if(response.isSuccessful()){
                    goToHome();
                }
            }

            @Override
            public void onFailure(Call<com.example.bimba.RESTAPI.Response> call, Throwable t) {

            }
        });
    }

    private void createHistoryPembayaran(){
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceHistoryPembayaran.createHistoryPembayaran(historyPembayaran.getIdHistoryPembayaran(), historyPembayaran.getIdTunggakan(), historyPembayaran.getJumlahDisetorkan(), historyPembayaran.getApproved());
        call.enqueue(new Callback<com.example.bimba.RESTAPI.Response>() {
            @Override
            public void onResponse(Call<com.example.bimba.RESTAPI.Response> call, Response<com.example.bimba.RESTAPI.Response> response) {
                if(response.isSuccessful()){
                    updateTagihan();
                }
            }

            @Override
            public void onFailure(Call<com.example.bimba.RESTAPI.Response> call, Throwable t) {

            }
        });
    }

    private void openMidtransPayment(){
        SdkUIFlowBuilder.init()
                .setClientKey("SB-Mid-client-0qN6Kp3Hg0L6z8My")
                .setContext(getApplicationContext())
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult transactionResult) {
                        if (transactionResult.getResponse() != null) {
                            Log.d("tes", "onTransactionFinished: running" );
                            historyPembayaran.setIdHistoryPembayaran(transactionResult.getResponse().getOrderId());
                            checkStatusOnMidtrans(historyPembayaran.getIdHistoryPembayaran());
                        } else if (transactionResult.isTransactionCanceled()) {
                            Toast.makeText(DaftarPaketActivity.this, "Transaction Canceled", Toast.LENGTH_LONG).show();
                        } else {
                            if (transactionResult.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                                Toast.makeText(DaftarPaketActivity.this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DaftarPaketActivity.this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).setMerchantBaseUrl(BASE_URL_MERCHANT)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .setLanguage("id")
                .buildSDK();

        prepareDataForTransaction();
    }

    private void prepareDataForTransaction(){
        TransactionRequest transactionRequest = new TransactionRequest("bimba-" + System.currentTimeMillis(), Integer.valueOf(etSetoran.getText().toString()));
        loadDataUser(transactionRequest);
    }

    private void fillCustomerDetail(TransactionRequest transactionRequest, User user){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerIdentifier(String.valueOf(user.getIdUser()));
        customerDetails.setPhone(user.getNoHp());
        customerDetails.setFirstName(user.getFirstName());
        customerDetails.setLastName(user.getLastName());
        customerDetails.setEmail(user.getEmailUser());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress(user.getAlamat() + "RT. " + user.getRt()+" /RW. "+ user.getRw());
        shippingAddress.setCity("Tangerang");
        shippingAddress.setPostalCode("15144");

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress(user.getAlamat() + "RT. " + user.getRt()+" /RW. "+ user.getRw());
        billingAddress.setCity("Tangerang");
        billingAddress.setPostalCode("15144");

        customerDetails.setShippingAddress(shippingAddress);
        customerDetails.setBillingAddress(billingAddress);

        transactionRequest.setCustomerDetails(customerDetails);

        ItemDetails itemDetails = new ItemDetails(String.valueOf(jenisPaket.getIdPaket()), tunggakan.getTotalHarusBayar(), 1 , jenisPaket.getDescPaket());

        int kekurangan = -(tunggakan.getTotalHarusBayar() - Integer.valueOf(etSetoran.getText().toString()));
        ItemDetails kekuranganBiaya = new ItemDetails("0", kekurangan, 1 , "Kekurangan Biaya");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(kekuranganBiaya);

        transactionRequest.setItemDetails(itemDetailsArrayList);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        MidtransSDK.getInstance().startPaymentUiFlow(DaftarPaketActivity.this);

    }

    private void checkStatusOnMidtrans(String idOrder){
        Log.d("tes", "checkStatusMidtrans: running" );
        Call<ResponseMidtrans> call = apiInterfaceMidtrans.readStatus(idOrder);
        call.enqueue(new Callback<ResponseMidtrans>() {
            @Override
            public void onResponse(Call<ResponseMidtrans> call, Response<ResponseMidtrans> response) {
                if(response.isSuccessful()){
                    Log.d("TAG", "onResponse: " + response.body().getStatusCode());
                    if(response.body().getStatusCode() == 200){
                        showMessage(DaftarPaketActivity.this, "Berhasil Membayar Tagihan");
                        createTunggakan();
                    }
                }else{
                    Log.d("TAG", "onResponse: Failed"  + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseMidtrans> call, Throwable t) {
                Log.d("TAG", "onResponse: Failed" +t.getMessage());
            }
        });
    }

    private void goToHome(){
        Intent intent = new Intent(DaftarPaketActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

