package com.example.bimba.RESTAPI.Paket;

import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceJenisPaket {
    @GET("JenisPaket/read_jenisPaket")
    Call<ResponsePaket> getJenisPaket(
            @Query("id_paket") int idPaket,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @FormUrlEncoded
    @POST("JenisPaket/create_jenisPaket")
    Call<Response> createPaket(
            @Field("desc_paket") String descPaket,
            @Field("masa_pembelajaran") int masaPembelajaran
    );

    @FormUrlEncoded
    @POST("JenisPaket/update_jenisPaket")
    Call<Response> updatePaket(
            @Field("id_paket") int idPaket,
            @Field("desc_paket") String descPaket,
            @Field("masa_pembelajaran") int masaPembelajaran
    );
}
