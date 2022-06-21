package com.pamsdev.damriapps.response;

public class ResponseDelete {

    private String data;
    private String status;
    private String kode;

    public ResponseDelete(String data, String status, String kode) {
        this.data = data;
        this.status = status;
        this.kode = kode;
    }

    public String getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getKode() {
        return kode;
    }
}
