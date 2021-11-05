package com.example.bimba.RESTAPI.Siswa;

import com.example.bimba.RESTAPI.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceSiswa {

    @GET("siswa/read_siswa")
    Call<ResponseSiswa> getSiswa(
            @Query("nis") int nis,
            @Query("id_user") String id_user,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @FormUrlEncoded
    @POST("siswa/create_siswa")
    Call<Response> createSiswa(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("jenis_kelamin") String jenisKelamin,
            @Field("tanggal_lahir") String tanggalLahir,
            @Field("tanggal_masuk") String tanggalMasuk,
            @Field("foto_siswa") String fotoSiswa,
            @Field("id_user") String id_user
    );
}
