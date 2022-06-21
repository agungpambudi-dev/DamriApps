package com.pamsdev.damriapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.pamsdev.damriapps.api.ApiConfig;
import com.pamsdev.damriapps.databinding.ActivityLoginBinding;
import com.pamsdev.damriapps.model.User;
import com.pamsdev.damriapps.response.ResponseLogin;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.btLogin.setOnClickListener(view -> {
            username = Objects.requireNonNull(binding.etUsername.getText()).toString();
            password = Objects.requireNonNull(binding.etPassword.getText()).toString();
            validateInput();
            closeKeyboard();
        });

        binding.tvForgetPasssword.setOnClickListener(view -> {
            Snackbar.make(view, "Fitur Lupa Password Belum Dikembangkan", Snackbar.LENGTH_SHORT).show();
        });

        binding.tvRegister.setOnClickListener(view -> {
            Snackbar.make(view, "Fitur Register Belum Dikembangkan", Snackbar.LENGTH_SHORT).show();
        });

    }

    private void validateInput(){
        if (username.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Username dan Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty() && !password.isEmpty()){
            Toast.makeText(this, "Silahkan Isi Username Anda", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty() && !username.isEmpty()){
            Toast.makeText(this, "Silahkan Isi Password Anda", Toast.LENGTH_SHORT).show();
        } else {
            getUser();
        }
    }

    private void getUser() {
        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseLogin> call = ApiConfig.getApiService().login(BuildConfig.TOKEN, username, password);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(@NotNull Call<ResponseLogin> call, @NotNull Response<ResponseLogin> response) {
                if (response.isSuccessful()){
                    binding.progressCircular.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getStatus().equals("success")){
                        List<String> CookieList = response.headers().values("Set-Cookie");
                        String session_id = (CookieList.get(0).split(";"))[0];
                        SharedPrefManager.getInstance(LoginActivity.this).setCookie(session_id);
                        User user = new User(
                                BuildConfig.TOKEN,
                                username,
                                password
                        );
                        SharedPrefManager.getInstance(LoginActivity.this).setUser(user);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.title_login_fail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, R.string.title_login_fail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseLogin> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}