package com.example.bimba.RESTAPI.User;

import com.example.bimba.Model.User;
import com.example.bimba.RESTAPI.Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterfaceUser {
    @GET("User/read_user")
    Call<ResponseUser> getUser(
            @Query("id_user") int idUser,
            @Query("email_user") String emailUser,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @Multipart
    @POST("User/create_user")
    Call<Response> createUser(
            @Part("email_user") RequestBody emailUser,
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("jenis_kelamin") RequestBody jenisKelamin,
            @Part("no_hp") RequestBody noHp,
            @Part("alamat") RequestBody alamat,
            @Part("rt") RequestBody rt,
            @Part("rw") RequestBody rw,
            @Part MultipartBody.Part foto_profile
    );

    @Multipart
    @POST("User/update_user")
    Call<Response> updateUser(
            @Part("id_user") RequestBody id_user,
            @Part("email_user") RequestBody emailUser,
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("jenis_kelamin") RequestBody jenisKelamin,
            @Part("no_hp") RequestBody noHp,
            @Part("alamat") RequestBody alamat,
            @Part("rt") RequestBody rt,
            @Part("rw") RequestBody rw,
            @Part MultipartBody.Part foto_profile
    );

}
