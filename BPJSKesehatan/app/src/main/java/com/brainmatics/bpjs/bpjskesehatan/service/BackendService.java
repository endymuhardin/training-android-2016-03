package com.brainmatics.bpjs.bpjskesehatan.service;


import android.content.Context;
import android.util.Base64;

import com.brainmatics.bpjs.bpjskesehatan.db.BpjsDbHelper;
import com.brainmatics.bpjs.bpjskesehatan.dto.FcmToken;
import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Peserta;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendService {

    private static final String URL_SERVER = "https://endybpjsbackend.herokuapp.com";
    private static final String URL_DAFTAR_TAGIHAN = "/api/tagihan/{peserta}/";
    private static final String CLIENT_ID = "clientapp";
    private static final String CLIENT_SECRET = "123456";

    private BpjsBackend backend;

    public BackendService(Context ctx) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // kalau ada oauth token, gunakan dalam request
        final String oauthToken = new BpjsDbHelper(ctx).getOauthToken();

        if(oauthToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + oauthToken)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

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

    public String login(String username, String password) {
        String credential = CLIENT_ID + ":" + CLIENT_SECRET;
        final String basicAuthHeader =
                "Basic " + Base64.encodeToString(credential.getBytes(), Base64.NO_WRAP);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                HttpUrl urlOriginal = original.url();
                HttpUrl urlBaru = urlOriginal.newBuilder()
                        .addQueryParameter("client_id", "clientapp")
                        .addQueryParameter("grant_type", "password")
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basicAuthHeader)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .url(urlBaru);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();

        BpjsBackend loginBackend = retrofit.create(BpjsBackend.class);

        try {
            Map<String, String> loginResult = loginBackend.login(username, password).execute().body();
            return loginResult.get("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void simpanPeserta(Peserta peserta, File foto) throws IOException {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), foto);

        MultipartBody.Part fotoPart =
                MultipartBody.Part.createFormData("foto", foto.getName(), requestFile);

        String pesertaJson = new Gson().toJson(peserta);
        RequestBody requestPeserta =
                RequestBody.create(MediaType.parse("application/json"), pesertaJson);
        backend.simpanPeserta(requestPeserta, fotoPart).execute();
    }
}
