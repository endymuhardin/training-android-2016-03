package com.brainmatics.bpjs.bpjskesehatan.service;

import com.brainmatics.bpjs.bpjskesehatan.dto.Page;
import com.brainmatics.bpjs.bpjskesehatan.dto.Tagihan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BpjsBackend {

    @GET("/api/tagihan/{peserta}/")
    public Call<Page<Tagihan>> ambilTagihan(@Path("peserta") String idPeserta);
}
