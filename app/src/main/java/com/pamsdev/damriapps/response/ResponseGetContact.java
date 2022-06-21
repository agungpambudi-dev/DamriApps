package com.pamsdev.damriapps.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.pamsdev.damriapps.model.Contact;

import java.util.List;

public class ResponseGetContact implements Parcelable {

    @SerializedName("data")
    private final List<Contact> dataContact;
    private final String status;
    private final String kode;

    public ResponseGetContact(List<Contact> dataContact, String status, String kode) {
        this.dataContact = dataContact;
        this.status = status;
        this.kode = kode;
    }

    public List<Contact> getDataContact() {
        return dataContact;
    }

    public String getStatus() {
        return status;
    }

    public String getKode() {
        return kode;
    }

    public static Creator<ResponseGetContact> getCREATOR() {
        return CREATOR;
    }

    protected ResponseGetContact(Parcel in) {
        dataContact = in.createTypedArrayList(Contact.CREATOR);
        status = in.readString();
        kode = in.readString();
    }

    public static final Creator<ResponseGetContact> CREATOR = new Creator<ResponseGetContact>() {
        @Override
        public ResponseGetContact createFromParcel(Parcel in) {
            return new ResponseGetContact(in);
        }

        @Override
        public ResponseGetContact[] newArray(int size) {
            return new ResponseGetContact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(dataContact);
        parcel.writeString(status);
        parcel.writeString(kode);
    }
}
