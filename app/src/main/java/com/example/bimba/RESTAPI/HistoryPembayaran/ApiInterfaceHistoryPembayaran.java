package com.example.bimba.RESTAPI.HistoryPembayaran;

import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.Tunggakan.ResponseTunggakan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceHistoryPembayaran {

    @GET("HistoryPembayaran/read_historyPembayaran")
    Call<ResponseHistoryPembayaran> readHistoryPembayaran(
            @Query("id_history_pembayaran") String idHistoryPembayaran,
            @Query("id_tunggakan") int idTunggakan,
            @Query("approved") int approved,
            @Query("id_user") int idUser,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    @FormUrlEncoded
    @POST("HistoryPembayaran/create_historyPembayaran")
    Call<Response> createHistoryPembayaran(
            @Field("id_history_pembayaran") String idHistoryPembayaran,
            @Field("id_tunggakan") int idTunggakan,
            @Field("jumlah_disetorkan") int jumlahDisetorkan,
            @Field("approved") int approved
    );

    @FormUrlEncoded
    @POST("HistoryPembayaran/update_historyPembayaran")
    Call<Response> updateHistoryPembayaran(
            @Field("id_history_pembayaran") String idHistoryPembayaran,
            @Field("id_tunggakan") int idTunggakan,
            @Field("jumlah_disetorkan") int jumlahDisetorkan,
            @Field("approved") int approved
    );
}
