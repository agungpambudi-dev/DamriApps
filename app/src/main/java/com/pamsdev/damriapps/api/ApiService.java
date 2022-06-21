package com.pamsdev.damriapps.api;

import com.pamsdev.damriapps.response.ResponseDelete;
import com.pamsdev.damriapps.response.ResponseGetContact;
import com.pamsdev.damriapps.response.ResponseLogin;
import com.pamsdev.damriapps.response.ResponseUpdateContact;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    @FormUrlEncoded
    @POST(EndPoint.Login)
    Call<ResponseLogin> login(
            @Field("token") String token,
            @Field("username") String username,
            @Field("password") String password
    );

    @POST(EndPoint.Logout)
    Call<ResponseLogin> logout(
            @Header("Cookie") String sessionId
    );

    @GET(EndPoint.DataContact)
    Call<ResponseGetContact> getContact(@Header("Cookie") String sessionId);

    @FormUrlEncoded
    @PUT(EndPoint.DataContact)
    Call<ResponseUpdateContact> updateContact(
            @Header("Cookie") String sessionId,
            @Field("id") String id,
            @Field("nama") String nama,
            @Field("nomor") String nomor
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = EndPoint.DataContact, hasBody = true)
    Call<ResponseDelete> deleteContact(
            @Header("Cookie") String sessionId,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(EndPoint.DataContact)
    Call<ResponseUpdateContact> addContact(
            @Header("Cookie") String sessionId,
            @Field("nama") String nama,
            @Field("nomor") String nomor
    );

}
