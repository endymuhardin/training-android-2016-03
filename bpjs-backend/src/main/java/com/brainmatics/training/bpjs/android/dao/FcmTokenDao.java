package com.brainmatics.training.bpjs.android.dao;

import com.brainmatics.training.bpjs.android.entity.FcmToken;
import org.springframework.data.repository.CrudRepository;

public interface FcmTokenDao extends CrudRepository<FcmToken, String>{
    
}
