package com.example.bimba.RESTAPI.Siswa;

import com.example.bimba.RESTAPI.Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterfaceSiswa {

    @GET("Siswa/read_siswa")
    Call<ResponseSiswa> getSiswa(
            @Query("nis") int nis,
            @Query("id_user") int id_user,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @Multipart
    @POST("Siswa/create_siswa")
    Call<Response> createSiswa(
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("jenis_kelamin") RequestBody jenisKelamin,
            @Part("tanggal_lahir") RequestBody tanggalLahir,
            @Part("tahun_masuk") RequestBody tanggalMasuk,
            @Part MultipartBody.Part fotoSiswa,
            @Part("id_user") RequestBody id_user
    );

    @Multipart
    @POST("Siswa/update_siswa")
    Call<Response> updateSiswa(
            @Part("nis") RequestBody nis,
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("jenis_kelamin") RequestBody jenisKelamin,
            @Part("tanggal_lahir") RequestBody tanggalLahir,
            @Part("tahun_masuk") RequestBody tanggalMasuk,
            @Part MultipartBody.Part fotoSiswa
    );
}
