package com.pamsdev.damriapps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    private String id;
    private String nama;
    private String nomor;

    public Contact(String id, String nama, String nomor) {
        this.id = id;
        this.nama = nama;
        this.nomor = nomor;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNomor() {
        return nomor;
    }

    public static Creator<Contact> getCREATOR() {
        return CREATOR;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        nama = in.readString();
        nomor = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nama);
        parcel.writeString(nomor);
    }
}
