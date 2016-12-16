package com.brainmatics.training.bpjs.android.controller;

import com.brainmatics.training.bpjs.android.dao.FcmTokenDao;
import com.brainmatics.training.bpjs.android.dao.PesertaDao;
import com.brainmatics.training.bpjs.android.dao.TagihanDao;
import com.brainmatics.training.bpjs.android.entity.FcmToken;
import com.brainmatics.training.bpjs.android.entity.Peserta;
import com.brainmatics.training.bpjs.android.entity.Tagihan;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BpjsBackendRestController {
    
    @Autowired private TagihanDao tagihanDao;
    @Autowired private FcmTokenDao fcmTokenDao;
    
    
    @RequestMapping(method = RequestMethod.GET, value = "/tagihan/{peserta}/")
    public Page<Tagihan> cariTagihan(@PathVariable Peserta peserta, Pageable page){
        return tagihanDao.findByPeserta(peserta, page);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/peserta/{id}/")
    public Peserta cariPesertaById(@PathVariable(name = "id") Peserta peserta){
        return peserta;
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/tagihan/{peserta}/")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertTagihan(@RequestBody @Valid Tagihan t){
        tagihanDao.save(t);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/token/{peserta}/")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrasiToken(@RequestBody @Valid FcmToken t){
        FcmToken tokenDb = fcmTokenDao.findByPeserta(t.getPeserta());
        if(tokenDb == null){
            tokenDb = new FcmToken();
            tokenDb.setPeserta(t.getPeserta());
        }
        tokenDb.setFcmToken(t.getFcmToken());
        fcmTokenDao.save(tokenDb);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/token/")
    public Iterable<FcmToken> semuaToken(){
        return fcmTokenDao.findAll();
    }
}
