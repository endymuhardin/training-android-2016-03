package com.brainmatics.training.bpjs.android.dao;

import com.brainmatics.training.bpjs.android.entity.Peserta;
import com.brainmatics.training.bpjs.android.entity.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagihanDao extends PagingAndSortingRepository<Tagihan, String>{

    public Page<Tagihan> findByPeserta(Peserta peserta, Pageable page);
    
}
