package com.brainmatics.bpjs.bpjskesehatan.service;


import com.brainmatics.bpjs.bpjskesehatan.dto.FcmToken;
import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Peserta;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendService {

    private static final String URL_SERVER = "https://endybpjsbackend.herokuapp.com";
    private static final String URL_DAFTAR_TAGIHAN = "/api/tagihan/{peserta}/";

    private BpjsBackend backend;

    public BackendService(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        backend = builder.client(httpClient.build()).build().create(BpjsBackend.class);
    }

    public Page<Tagihan> semuaTagihan(String peserta) throws IOException {
        return backend.ambilTagihan(peserta).execute().body();
    }

    public void registrasiToken(String token) throws IOException {
        // sementara id peserta dihardcode dulu
        String idPeserta = "p001";

        // request object
        Peserta p = new Peserta();
        p.setId(idPeserta);
        FcmToken fcmToken = new FcmToken();
        fcmToken.setPeserta(p);
        fcmToken.setFcmToken(token);

        backend.registrasiToken(idPeserta, fcmToken).execute();
    }
}
