package com.example.bimba.RESTAPI.DetailPaket;

import com.example.bimba.RESTAPI.Biaya.ResponseBiaya;
import com.example.bimba.RESTAPI.Paket.ResponsePaket;
import com.example.bimba.RESTAPI.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceDetailPaket {
    @GET("DetailPaket/read_detailPaket")
    Call<ResponseBiaya> getDetailPaket(
            @Query("id_paket") int idPaket
    );

    @FormUrlEncoded
    @POST("DetailPaket/create_detailPaket")
    Call<Response> createDetailPaket(
            @Field("id_paket") int idPaket,
            @Field("id_biaya") int idBiaya
    );

    @FormUrlEncoded
    @POST("DetailPaket/delete_detailPaket")
    Call<Response> deleteDetailPaket(
            @Field("id_paket") int idPaket,
            @Field("id_biaya") int idBiaya
    );
}
