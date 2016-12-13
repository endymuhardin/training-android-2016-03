package com.brainmatics.training.bpjs.android.controller;

import com.brainmatics.training.bpjs.android.dao.PesertaDao;
import com.brainmatics.training.bpjs.android.dao.TagihanDao;
import com.brainmatics.training.bpjs.android.entity.Peserta;
import com.brainmatics.training.bpjs.android.entity.Tagihan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BpjsBackendRestController {
    
    @Autowired private TagihanDao tagihanDao;
    @Autowired private PesertaDao pesertaDao;
    
    @RequestMapping(method = RequestMethod.GET, value = "/tagihan/{peserta}/")
    public Page<Tagihan> cariTagihan(@PathVariable Peserta peserta, Pageable page){
        return tagihanDao.findByPeserta(peserta, page);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/peserta/{id}/")
    public Peserta cariPesertaById(@PathVariable(name = "id") Peserta peserta){
        return peserta;
    }
}
