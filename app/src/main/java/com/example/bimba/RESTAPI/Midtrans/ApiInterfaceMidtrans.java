package com.example.bimba.RESTAPI.Midtrans;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceMidtrans {

    @GET("{id_order}/status")
    Call<ResponseMidtrans> readStatus(@Path("id_order") String idOrder);
}
