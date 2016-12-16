package com.brainmatics.bpjs.bpjskesehatan.dto;

public class FcmToken {
    private Peserta peserta;
    private String fcmToken;

    public Peserta getPeserta() {
        return peserta;
    }

    public void setPeserta(Peserta peserta) {
        this.peserta = peserta;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
