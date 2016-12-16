package com.brainmatics.bpjs.bpjskesehatan.service;

import com.brainmatics.bpjs.bpjskesehatan.dto.FcmToken;
import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;

import java.util.Map;

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
import retrofit2.http.Path;

public interface BpjsBackend {

    @GET("/api/tagihan/{peserta}/")
    public Call<Page<Tagihan>> ambilTagihan(@Path("peserta") String idPeserta);

    @POST("/api/token/{peserta}/")
    public Call<Void> registrasiToken(@Path("peserta") String idPeserta, @Body FcmToken token);

    @POST("/oauth/token")
    @FormUrlEncoded
    public Call<Map<String, String>> login(
            @Field("username") String username,
            @Field("password") String password);

    @POST("/api/peserta/")
    @Multipart
    public Call<Void> simpanPeserta(@Part("peserta")RequestBody peserta,
                                    @Part MultipartBody.Part foto);
}
