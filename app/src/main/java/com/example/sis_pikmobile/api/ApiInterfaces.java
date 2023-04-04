package com.example.sis_pikmobile.api;
import com.example.sis_pikmobile.model.ResponseData;
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

    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseUser> dataRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmatio
    );

    @FormUrlEncoded
    @POST("api/permohonan-get")
    Call<ResponseData> dataPermohonan(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/permohonan-get-petugas")
    Call<ResponseData> dataPermohonanPetugas(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("api/post-permohonan")
    Call<ResponseData> postDataPermohonan(
            @Field("penanggung_jawab") String penanggung_jawab,
            @Field("kegiatan") String kegiatan,
            @Field("lokasi") String lokasi,
            @Field("tgl_mulai") String tgl_mulai,
            @Field("tgl_selesai") String tgl_selesai,
            @Field("waktu") String waktu,
            @Field("pemohon_id") String pemohon_id
    );

    @FormUrlEncoded
    @POST("api/post-permohonan-ktp")
    Call<ResponseData> postDataKtp(
            @Field("id") String id,
            @Field("ktp") String ktp
    );

    @FormUrlEncoded
    @POST("api/post-permohonan-kk")
    Call<ResponseData> postDataKk(
            @Field("id") String id,
            @Field("kk") String kk
    );

    @FormUrlEncoded
    @POST("api/post-permohonan-sp")
    Call<ResponseData> postDataSp(
            @Field("id") String id,
            @Field("sp") String sp
    );

    @POST("api/konfirmasi")
    Call<ResponseData> konfirmasiTempat(
        @Field("id") String id
    );

    @POST("api/tolak")
    Call<ResponseData> tolakKonfirmasi(
            @Field("id") String id
    );

    @POST("api/logout")
    Call<ResponseUser> logout(
            @Header("Authorization") String token
    );


}
