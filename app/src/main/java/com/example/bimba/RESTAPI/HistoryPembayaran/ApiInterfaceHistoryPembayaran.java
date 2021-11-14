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
            @Query("id_user") int idUser
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
    @POST("HistoryPembayaran/delete_historyPembayaran")
    Call<Response> updateHistoryPembayaran(
            @Field("id_history_pembayaran") String idHistoryPembayaran,
            @Field("id_tunggakan") int idTunggakan,
            @Field("jumlah_disetorkan") int jumlahDisetorkan,
            @Field("approved") int approved
    );
}
