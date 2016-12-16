package com.brainmatics.training.bpjs.android.controller;

import com.brainmatics.training.bpjs.android.dao.FcmTokenDao;
import com.brainmatics.training.bpjs.android.dao.TagihanDao;
import com.brainmatics.training.bpjs.android.entity.FcmToken;
import com.brainmatics.training.bpjs.android.entity.Peserta;
import com.brainmatics.training.bpjs.android.entity.Tagihan;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class BpjsBackendRestController {
    
    private static final String FCM_SERVER = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_KEY = "AAAArBbTg8k:APA91bGqebA1TUTSHf5oJsWTMfWz3CuroVLDTvdjWOOeQc8lBd2zCxuTJbWhhv4MSoKEl3zTOxFwBJkiOEHmQFtq0aM6Vpxr7SU6Nv-5BywPPQlmennTzE0a8IvzMOLeDQWK9y3H36xIAVTOYYTiedVdpDjvqDyXOw";
    
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
        
        // kirim FCM ke device untuk notifikasi tagihan
        FcmToken token = fcmTokenDao.findByPeserta(t.getPeserta());
        if(token == null) {
            System.out.println("Token tidak ditemukan untuk peserta "+t.getPeserta().getId());
            return;
        }
        
        // Data yang mau dikirim
        Map<String, Object> fcmMsg = new HashMap<>();
        Map<String, String> fcmData = new HashMap<>();
        fcmData.put("id", t.getPeserta().getId());
        fcmData.put("nomor", t.getPeserta().getNomor());        
        fcmData.put("nama", t.getPeserta().getNama());

        fcmMsg.put("to", token.getFcmToken());
        fcmMsg.put("data", fcmData);
        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "key=" + FCM_KEY);
        headers.add("Content-Type", "application/json");
        
        HttpEntity<Map<String, Object>> httpRequestBody = new HttpEntity<>(fcmMsg, headers);
        System.out.println("HTTP Request : "+httpRequestBody);
        RestTemplate httpClient = new RestTemplate();
        Map<String, Object> response = httpClient.postForObject(FCM_SERVER, httpRequestBody, Map.class);
        System.out.println("Response : "+response);
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
    
    @RequestMapping(method = RequestMethod.POST, value = "/peserta/")
    public void simpanPeserta(@RequestBody Peserta p, MultipartFile foto){
        System.out.println("Nomor : "+p.getNomor());
        System.out.println("Nama : "+p.getNama());
        
        System.out.println("Content type : "+foto.getContentType());
        System.out.println("Ukuran : "+foto.getSize());
    }
}
