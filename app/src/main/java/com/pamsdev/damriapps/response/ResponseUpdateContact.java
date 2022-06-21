package com.pamsdev.damriapps.response;

import com.google.gson.annotations.SerializedName;
import com.pamsdev.damriapps.model.Contact;

public class ResponseUpdateContact {

    @SerializedName("data")
    private final Contact contact;
    private final String status;
    private final String kode;

    public ResponseUpdateContact(Contact contact, String status, String kode) {
        this.contact = contact;
        this.status = status;
        this.kode = kode;
    }

    public Contact getContact() {
        return contact;
    }

    public String getStatus() {
        return status;
    }

    public String getKode() {
        return kode;
    }
}
