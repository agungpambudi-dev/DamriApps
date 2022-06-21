package com.pamsdev.damriapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pamsdev.damriapps.api.ApiConfig;
import com.pamsdev.damriapps.databinding.ActivityDetailsContactBinding;
import com.pamsdev.damriapps.model.Contact;
import com.pamsdev.damriapps.response.ResponseDelete;
import com.pamsdev.damriapps.response.ResponseUpdateContact;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsContactActivity extends AppCompatActivity {

    private ActivityDetailsContactBinding binding;
    public static final String data_contact = "data";
    private Contact contact;
    private String name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Detail Kontak");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        contact = getIntent().getParcelableExtra(data_contact);

        if (contact != null) {
            binding.etName.setText(contact.getNama());
            binding.etPhone.setText(contact.getNomor());
        }

        binding.ivEdit.setOnClickListener(view -> viewEdit());
        binding.btCancel.setOnClickListener(view -> viewNoEdit());
        binding.btSave.setOnClickListener(view -> {
            name = binding.etName.getText().toString();
            phone = binding.etPhone.getText().toString();
            checkInput();
            closeKeyboard();
        });
        binding.ivDelete.setOnClickListener(view -> confirmDelete());
        binding.ivPhone.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + binding.etPhone.getText().toString()));
            startActivity(intent);
        });


    }

    private void viewNoEdit(){
        binding.btSave.setVisibility(View.GONE);
        binding.btCancel.setVisibility(View.GONE);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.AndroidGray));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.AndroidGray));
        binding.etName.setFocusableInTouchMode(false);
        binding.etName.setFocusable(false);
        binding.etName.setInputType(InputType.TYPE_NULL);
        binding.etPhone.setFocusable(false);
        binding.etPhone.setFocusableInTouchMode(false);
        binding.etPhone.setInputType(InputType.TYPE_NULL);
    }

    private void viewEdit(){
        binding.view1.setBackgroundColor(getResources().getColor(R.color.SelectiveYellow));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.SelectiveYellow));
        binding.btSave.setVisibility(View.VISIBLE);
        binding.btCancel.setVisibility(View.VISIBLE);
        binding.etName.setFocusableInTouchMode(true);
        binding.etName.setFocusable(true);
        binding.etName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        binding.etPhone.setEnabled(true);
        binding.etPhone.setFocusableInTouchMode(true);
        binding.etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    private void checkInput() {
        if (name.isEmpty() && phone.isEmpty()) {
            Toast.makeText(this, "Nama dan Telepon Harus di Isi", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty() && !phone.isEmpty()) {
            Toast.makeText(this, "Kolom Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty() && !name.isEmpty()) {
            Toast.makeText(this, "Kolom Telepon Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            updateData();
        }
    }

    private void confirmDelete(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Apakah anda yakin ingin menghapus data "+contact.getNama()+" ?");

        alertDialog.setPositiveButton("Ya", (dialog, which) -> deleteData());

        alertDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());

        AlertDialog ad = alertDialog.create();
        ad.show();
    }

    private void updateData() {

        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseUpdateContact> call = ApiConfig.getApiService().updateContact(SharedPrefManager.getInstance(this).getCookie(), contact.getId(), name, phone);
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
                        Toast.makeText(DetailsContactActivity.this, "Berhasil Memperbarui Kontak", Toast.LENGTH_SHORT).show();
                        binding.etName.setText(name);
                        binding.etPhone.setText(phone);
                        viewNoEdit();

                    } else {
                        Toast.makeText(DetailsContactActivity.this, "Gagal Memperbarui Kontak", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(DetailsContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseUpdateContact> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(DetailsContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteData(){
        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseDelete> call = ApiConfig.getApiService().deleteContact(SharedPrefManager.getInstance(this).getCookie(), contact.getId());
        call.enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDelete> call, @NotNull Response<ResponseDelete> response) {
                if (response.isSuccessful()) {
                    binding.progressCircular.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getStatus().equals("success")) {
                        Toast.makeText(DetailsContactActivity.this, "Berhasil Menghapus Kontak", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailsContactActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DetailsContactActivity.this, "Gagal Menghapus Kontak", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(DetailsContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDelete> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(DetailsContactActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
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