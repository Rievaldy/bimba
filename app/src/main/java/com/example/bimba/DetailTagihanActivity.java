package com.example.bimba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bimba.Model.HistoryPembayaran;
import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.ApiClient;
import com.example.bimba.RESTAPI.ApiMidtrans;
import com.example.bimba.RESTAPI.HistoryPembayaran.ApiInterfaceHistoryPembayaran;
import com.example.bimba.RESTAPI.Midtrans.ApiInterfaceMidtrans;
import com.example.bimba.RESTAPI.Midtrans.ResponseMidtrans;
import com.example.bimba.RESTAPI.Tunggakan.ApiInterfaceTunggakan;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bimba.Util.showMessage;

public class DetailTagihanActivity extends AppCompatActivity {

    private final String BASE_URL_MERCHANT = "https://bimbaindonesia.000webhostapp.com/rest_bimba/webhook/webhook_midtrans.php/";
    private TextView idTagihan, namaSiswa, namaUser, namaPaket, statusPembayaran, totalHarusBayar, sudahDibayarkan, sisaPembayaran;
    private TextInputEditText jumlahPembayaran;
    private Button btnBayar;
    private LinearLayout layoutBayar;
    private CompleteTunggakan completeTunggakan;
    private HistoryPembayaran historyPembayaran;
    private ApiInterfaceMidtrans apiInterfaceMidtrans;
    private ApiInterfaceHistoryPembayaran apiInterfaceHistoryPembayaran;
    private ApiInterfaceTunggakan apiInterfaceTunggakan;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan);
        completeTunggakan = getIntent().getParcelableExtra("EXTRA_COMPLETE_TUNGGAKAN");
        idTagihan = findViewById(R.id.et_id_tagihan_dt);
        namaSiswa = findViewById(R.id.et_nama_siswa_dt);
        namaUser = findViewById(R.id.et_nama_user_dt);
        namaPaket = findViewById(R.id.et_nama_paket_dt);
        statusPembayaran = findViewById(R.id.et_status_pembayaran_dt);
        totalHarusBayar = findViewById(R.id.et_total_harus_bayar_dt);
        sudahDibayarkan = findViewById(R.id.et_baru_dibayarkan_dt);
        sisaPembayaran = findViewById(R.id.et_sisa_pembayaran_dt);
        btnBayar = findViewById(R.id.btn_bayar_tagihan);
        jumlahPembayaran = findViewById(R.id.et_jumlah_pembayaran);
        layoutBayar = findViewById(R.id.layout_bayar_detail_tagihan);
        sessionManagement = new SessionManagement(getApplicationContext());
        historyPembayaran = new HistoryPembayaran();



        apiInterfaceMidtrans = ApiMidtrans.createService(ApiInterfaceMidtrans.class, "SB-Mid-server-22EyrGFZV-X4qWbLHKRTtfND", null);
        apiInterfaceHistoryPembayaran = ApiClient.getClient().create(ApiInterfaceHistoryPembayaran.class);
        apiInterfaceTunggakan = ApiClient.getClient().create(ApiInterfaceTunggakan.class);

        initializeData();
    }

    private void initializeData(){
        String status = (completeTunggakan.getTunggakan().getTotalHarusBayar() - completeTunggakan.getTunggakan().getBaruDibayarkan()) == 0 ? "Lunas" : "Belum Lunas";
        String total = "Rp. " + completeTunggakan.getTunggakan().getTotalHarusBayar();
        String baruBayar = "Rp. " + completeTunggakan.getTunggakan().getBaruDibayarkan();
        String sisa = "Rp. " + (completeTunggakan.getTunggakan().getTotalHarusBayar()-completeTunggakan.getTunggakan().getBaruDibayarkan());

        if(sessionManagement.getUserAccessSession() == 1 || completeTunggakan.getTunggakan().getTotalHarusBayar()-completeTunggakan.getTunggakan().getBaruDibayarkan() == 0){
            layoutBayar.setVisibility(View.GONE);
        }

        idTagihan.setText(String.valueOf(completeTunggakan.getTunggakan().getIdTunggakan()));
        namaSiswa.setText(completeTunggakan.getSiswa().getFirstName()+" "+completeTunggakan.getSiswa().getLastName());
        namaPaket.setText(completeTunggakan.getJenisPaket().getDescPaket());
        namaUser.setText(completeTunggakan.getUser().getFirstName()+" "+completeTunggakan.getUser().getLastName());
        statusPembayaran.setText(status);
        totalHarusBayar.setText(total);
        sudahDibayarkan.setText(baruBayar);
        sisaPembayaran.setText(sisa);
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxPembayaran = (completeTunggakan.getTunggakan().getTotalHarusBayar()-completeTunggakan.getTunggakan().getBaruDibayarkan());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String currentTime = sdf.format(Calendar.getInstance().getTime());
                long diffTahunMasukCurrentTime = Util.findDifference(completeTunggakan.getTunggakan().getTahunMasuk(), currentTime);
                int bayarPerBulan = (completeTunggakan.getTunggakan().getTotalHarusBayar() / completeTunggakan.getJenisPaket().getMasaPembelajaran());
                int jumlahBulanBayar = completeTunggakan.getTunggakan().getBaruDibayarkan() /bayarPerBulan;
                int selisihHariBayarDanLunas = (completeTunggakan.getJenisPaket().getMasaPembelajaran() * 30) - (jumlahBulanBayar * 30);
                int selisihHari = (int)diffTahunMasukCurrentTime - selisihHariBayarDanLunas;
                int minimumPembayaran = 0;
                if(selisihHari >= 30){
                    minimumPembayaran = bayarPerBulan * (selisihHari % 30);
                }else{
                    minimumPembayaran = bayarPerBulan;
                }
                if( jumlahPembayaran.getText().toString().isEmpty()){
                    jumlahPembayaran.setError("Harap Isi Jumlah Pembayaran");
                    jumlahPembayaran.requestFocus();
                }else if(Integer.valueOf(jumlahPembayaran.getText().toString()) < minimumPembayaran){
                    jumlahPembayaran.setError("Jumlah Minimum Pembayaran Rp. "+ minimumPembayaran);
                    jumlahPembayaran.requestFocus();
                }else{
                    if(Integer.valueOf(jumlahPembayaran.getText().toString()) > maxPembayaran){
                        jumlahPembayaran.setText(String.valueOf(maxPembayaran));

                    }
                    historyPembayaran.setJumlahDisetorkan(Integer.valueOf(jumlahPembayaran.getText().toString()));
                    openMidtransPayment();
                }
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
                            Toast.makeText(DetailTagihanActivity.this, "Transaction Canceled", Toast.LENGTH_LONG).show();
                        } else {
                            if (transactionResult.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                                Toast.makeText(DetailTagihanActivity.this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DetailTagihanActivity.this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
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
        TransactionRequest transactionRequest = new TransactionRequest("bimba-" + System.currentTimeMillis(), Integer.valueOf(jumlahPembayaran.getText().toString()));
        fillCustomerDetail(transactionRequest, completeTunggakan);
    }

    private void fillCustomerDetail(TransactionRequest transactionRequest, CompleteTunggakan completeTunggakan){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerIdentifier(String.valueOf(completeTunggakan.getUser().getIdUser()));
        customerDetails.setPhone(completeTunggakan.getUser().getNoHp());
        customerDetails.setFirstName(completeTunggakan.getUser().getFirstName());
        customerDetails.setLastName(completeTunggakan.getUser().getLastName());
        customerDetails.setEmail(completeTunggakan.getUser().getEmailUser());

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress(completeTunggakan.getUser().getAlamat() + "RT. " + completeTunggakan.getUser().getRt()+" /RW. "+ completeTunggakan.getUser().getRw());
        shippingAddress.setCity("Tangerang");
        shippingAddress.setPostalCode("15144");

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress(completeTunggakan.getUser().getAlamat() + "RT. " + completeTunggakan.getUser().getRt()+" /RW. "+ completeTunggakan.getUser().getRw());
        billingAddress.setCity("Tangerang");
        billingAddress.setPostalCode("15144");

        customerDetails.setShippingAddress(shippingAddress);
        customerDetails.setBillingAddress(billingAddress);

        transactionRequest.setCustomerDetails(customerDetails);

        ItemDetails itemDetails = new ItemDetails(String.valueOf(completeTunggakan.getJenisPaket().getIdPaket()), completeTunggakan.getTunggakan().getTotalHarusBayar(), 1 , completeTunggakan.getJenisPaket().getDescPaket());

        int kekurangan = -(completeTunggakan.getTunggakan().getTotalHarusBayar() - (Integer.valueOf(jumlahPembayaran.getText().toString()) + completeTunggakan.getTunggakan().getBaruDibayarkan()));
        Log.d("tes", "fillCustomerDetail: " + kekurangan);
        ItemDetails kekuranganBiaya = new ItemDetails("0", kekurangan, 1 , "Kekurangan Biaya");
        ItemDetails sudahBayar = new ItemDetails("0", -completeTunggakan.getTunggakan().getBaruDibayarkan(), 1 , "Sudah Dibayarkan");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(kekuranganBiaya);
        itemDetailsArrayList.add(sudahBayar);

        transactionRequest.setItemDetails(itemDetailsArrayList);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
        MidtransSDK.getInstance().startPaymentUiFlow(DetailTagihanActivity.this);

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
                        showMessage(DetailTagihanActivity.this, "Berhasil Membayar Tagihan");
                        createHistoryPembayaran();
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

    private void updateTagihan(){
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceTunggakan.updateTunggakan(completeTunggakan.getTunggakan().getIdTunggakan(), completeTunggakan.getTunggakan().getNis(), completeTunggakan.getTunggakan().getIdPaket(), completeTunggakan.getTunggakan().getTahunMasuk(), completeTunggakan.getTunggakan().getTotalHarusBayar(), completeTunggakan.getTunggakan().getBaruDibayarkan() + historyPembayaran.getJumlahDisetorkan());
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
        historyPembayaran.setApproved(0);
        Call<com.example.bimba.RESTAPI.Response> call = apiInterfaceHistoryPembayaran.createHistoryPembayaran(historyPembayaran.getIdHistoryPembayaran(), completeTunggakan.getTunggakan().getIdTunggakan(), historyPembayaran.getJumlahDisetorkan(), historyPembayaran.getApproved());
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

    private void goToHome(){
        Intent intent = new Intent(DetailTagihanActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
