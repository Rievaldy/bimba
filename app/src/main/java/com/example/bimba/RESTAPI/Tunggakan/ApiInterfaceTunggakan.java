package com.example.bimba.RESTAPI.Tunggakan;

import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceTunggakan {

    @GET("TunggakanSiswa/read_tunggakanSiswa")
    Call<ResponseTunggakan> readTunggakan(
            @Query("id_tunggakan") int idTunggakan,
            @Query("nis") int nis,
            @Query("id_paket") int idPaket,
            @Query("tahun_masuk") String tahunMasuk,
            @Query("id_user") int idUser
    );

    @FormUrlEncoded
    @POST("TunggakanSiswa/create_tunggakanSiswa")
    Call<Response> createTunggakan(
            @Field("nis") int nis,
            @Field("id_paket") int idPaket,
            @Field("tahun_masuk") String tahunMasuk,
            @Field("total_harus_bayar") int totalHarusBayar,
            @Field("baru_dibayarkan") int baruDibayarkan
    );

    @FormUrlEncoded
    @POST("TunggakanSiswa/update_tunggakanSiswa")
    Call<Response> updateTunggakan(
            @Field("id_tunggakan") int idTunggakan,
            @Field("nis") int nis,
            @Field("id_paket") int idPaket,
            @Field("tahun_masuk") String tahunMasuk,
            @Field("total_harus_bayar") int totalHarusBayar,
            @Field("baru_dibayarkan") int baruDibayarkan
    );
}
