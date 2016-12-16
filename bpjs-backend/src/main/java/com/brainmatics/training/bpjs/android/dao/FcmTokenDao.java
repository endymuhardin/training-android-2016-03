package com.brainmatics.training.bpjs.android.dao;

import com.brainmatics.training.bpjs.android.entity.FcmToken;
import com.brainmatics.training.bpjs.android.entity.Peserta;
import org.springframework.data.repository.CrudRepository;

public interface FcmTokenDao extends CrudRepository<FcmToken, String>{
    public FcmToken findByPeserta(Peserta peserta);
}
