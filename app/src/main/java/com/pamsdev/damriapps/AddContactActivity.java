package com.pamsdev.damriapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pamsdev.damriapps.api.ApiConfig;
import com.pamsdev.damriapps.databinding.ActivityAddContactBinding;
import com.pamsdev.damriapps.model.Contact;
import com.pamsdev.damriapps.response.ResponseDelete;
import com.pamsdev.damriapps.response.ResponseUpdateContact;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    ActivityAddContactBinding binding;
    private String name, phone;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Kontak");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        binding.btSave.setOnClickListener(view -> {
            name = binding.etName.getText().toString();
            phone = binding.etPhone.getText().toString();
            closeKeyboard();
            checkInput();
        });

    }

    private void checkInput(){
        if (name.isEmpty() && !phone.isEmpty()){
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (!name.isEmpty() && phone.isEmpty()){
            Toast.makeText(this, "Nomor telepon tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty() && phone.isEmpty()){
            Toast.makeText(this, "Semua input harus di isi", Toast.LENGTH_SHORT).show();
        } else {
            addContact();
        }
    }

    private void addContact(){

        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseUpdateContact> call = ApiConfig.getApiService().addContact(SharedPrefManager.getInstance(this).getCookie(), name, phone);
        call.enqueue(new Callback<ResponseUpdateContact>() {
            @Override
            public void onResponse(@NotNull Call<ResponseUpdateContact> call, @NotNull Response<ResponseUpdateContact> response) {
                if (response.isSuccessful()) {
                    binding.progressCircular.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getStatus().equals("success")) {
                        contact = new Contact(
                                response.body().getContact().getId(),
                                response.body().getContact().getNama(),
                                response.body().getContact().getNomor()
                        );
                        Toast.makeText(AddContactActivity.this, "Berhasil Menambahkan Kontak", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddContactActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AddContactActivity.this, "Gagal Menambahkan Kontak", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(AddContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseUpdateContact> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(AddContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}