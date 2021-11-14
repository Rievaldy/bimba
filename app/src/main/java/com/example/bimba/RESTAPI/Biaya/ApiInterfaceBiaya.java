package com.example.bimba.RESTAPI.Biaya;

import com.example.bimba.RESTAPI.Response;
import com.example.bimba.RESTAPI.UserAccess.ResponseUserAccess;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceBiaya {

    @GET("ListBiaya/read_listBiaya")
    Call<ResponseBiaya> getListBiaya(
            @Query("id_biaya") int idBiaya,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @FormUrlEncoded
    @POST("ListBiaya/create_listBiaya")
    Call<Response> createBiaya(
            @Field("desc_biaya") String descBiaya,
            @Field("harga") int harga,
            @Field("pembayaran_berkala") int pembayaranBerkala
    );

    @FormUrlEncoded
    @POST("ListBiaya/update_listBiaya")
    Call<Response> updateBiaya(
            @Field("id_biaya") int idBiaya,
            @Field("desc_biaya") String descBiaya,
            @Field("harga") int harga,
            @Field("pembayaran_berkala") int pembayaranBerkala
    );

}
