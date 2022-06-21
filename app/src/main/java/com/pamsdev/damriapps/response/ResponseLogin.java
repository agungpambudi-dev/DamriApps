package com.pamsdev.damriapps.response;

public class ResponseLogin {

    private final String status;
    private final String kode;

    public ResponseLogin(String status, String kode) {
        this.status = status;
        this.kode = kode;
    }

    public String getStatus() {
        return status;
    }

    public String getKode() {
        return kode;
    }
}
