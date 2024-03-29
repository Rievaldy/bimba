package com.example.bimba.RESTAPI.UserAccess;

import com.example.bimba.Model.UserAccess;
import com.example.bimba.RESTAPI.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaceUserAccess {
    @GET("UserAccess/read_userAccess")
    Call<ResponseUserAccess> getUserAccess(
            @Query("id_level") int idLevel,
            @Query("email_user") String emailUser,
            @Query("password") String password,
            @Query("order_by") String orderBy,
            @Query("limit") String limit,
            @Query("offset") String offset
    );

    @FormUrlEncoded
    @POST("UserAccess/create_userAccess")
    Call<Response> createUserAccess(
            @Field("email_user") String emailUser,
            @Field("password") String password,
            @Field("id_level") int idLevel
    );

    @FormUrlEncoded
    @POST("UserAccess/update_userAccess")
    Call<Response> updateUserAccess(
            @Field("email_user") String emailUser,
            @Field("password") String password,
            @Field("id_level") int idLevel
    );
}
