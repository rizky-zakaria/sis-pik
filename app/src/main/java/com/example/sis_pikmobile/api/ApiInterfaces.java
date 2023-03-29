package com.example.sis_pikmobile.api;
import com.example.sis_pikmobile.model.ResponseUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaces {

    @FormUrlEncoded
    @POST("api/login")
    Call<ResponseUser> dataLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("api/logout")
    Call<ResponseUser> logout(
            @Header("Authorization") String token
    );

}
