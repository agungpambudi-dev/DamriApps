package com.pamsdev.damriapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.pamsdev.damriapps.model.User;

public class SharedPrefManager {

    public static final String shared_pref = "shared_pref";
    public static final String token = "token";
    public static final String username = "username";
    public static final String password = "password";

    public static final String cookie = "cookie"
;
    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void logout() {
        mCtx.getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().clear().apply();
    }

    /*public void clearNewMember() {
        mCtx.getSharedPreferences(new_anggota, Context.MODE_PRIVATE).edit().clear().apply();
    }*/

    public void setUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(token, user.getToken());
        editor.putString(username, user.getUsername());
        editor.putString(password, user.getPassword());
        editor.apply();
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(token, null),
                sharedPreferences.getString(username, null),
                sharedPreferences.getString(password, null)
        );
    }

    public void setCookie(String cookies) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cookie, cookies);
        editor.apply();
    }

    public String getCookie() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(cookie, null);
    }

}
