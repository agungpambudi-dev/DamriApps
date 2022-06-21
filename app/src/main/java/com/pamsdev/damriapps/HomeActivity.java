package com.pamsdev.damriapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pamsdev.damriapps.adapter.AdapterContact;
import com.pamsdev.damriapps.api.ApiConfig;
import com.pamsdev.damriapps.databinding.ActivityHomeBinding;
import com.pamsdev.damriapps.model.Contact;
import com.pamsdev.damriapps.model.User;
import com.pamsdev.damriapps.response.ResponseGetContact;
import com.pamsdev.damriapps.response.ResponseLogin;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private final List<Contact> contactList = new ArrayList<>();
    private Contact contact;
    private AdapterContact adapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.ivLogout.setOnClickListener(view -> confirmLogout());
        binding.tvUsername.setText(SharedPrefManager.getInstance(this).getUser().getUsername());
        binding.ivAbout.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
        binding.fabAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        });
        if (contactList==null){
            binding.svContact.setFocusable(false);
            binding.svContact.setClickable(false);
        }
        binding.svContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.svContact.clearFocus();
                adapterContact.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterContact.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void getListContact() {
        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseGetContact> call = ApiConfig.getApiService().getContact(SharedPrefManager.getInstance(this).getCookie());
        call.enqueue(new Callback<ResponseGetContact>() {
            @Override
            public void onResponse(@NotNull Call<ResponseGetContact> call, @NotNull Response<ResponseGetContact> response) {
                if (response.isSuccessful()) {
                    binding.progressCircular.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getStatus().equals("success")) {
                        for (int i = 0; i < response.body().getDataContact().size(); i++) {
                            contact = new Contact(
                                    response.body().getDataContact().get(i).getId(),
                                    response.body().getDataContact().get(i).getNama(),
                                    response.body().getDataContact().get(i).getNomor()
                            );
                            contactList.add(contact);
                        }
                        binding.rvDataContact.setVisibility(View.VISIBLE);
                        adapterContact = new AdapterContact(HomeActivity.this,contactList);
                        binding.rvDataContact.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                        binding.rvDataContact.setAdapter(adapterContact);
                        adapterContact.notifyDataSetChanged();
                        adapterContact.setOnItemClickCallback(data -> {
                            Intent intent = new Intent(HomeActivity.this, DetailsContactActivity.class);
                            intent.putExtra(DetailsContactActivity.data_contact, data);
                            startActivity(intent);
                        });
                    } else {
                        Toast.makeText(HomeActivity.this, "Session Anda Habis, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseGetContact> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void confirmLogout() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Apakah anda ingin keluar dari Aplikasi ?");

        alertDialog.setPositiveButton("Ya", (dialog, which) -> {
            logout();
        });

        alertDialog.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());

        AlertDialog ad = alertDialog.create();
        ad.show();

    }

    private void logout(){
        binding.progressCircular.setVisibility(View.VISIBLE);

        Call<ResponseLogin> call = ApiConfig.getApiService().logout(BuildConfig.TOKEN);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(@NotNull Call<ResponseLogin> call, @NotNull Response<ResponseLogin> response) {
                if (response.isSuccessful()){
                    binding.progressCircular.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getKode().equals("200")){
                        Toast.makeText(HomeActivity.this, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(HomeActivity.this).logout();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(HomeActivity.this, R.string.title_login_fail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressCircular.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseLogin> call, @NotNull Throwable t) {
                binding.progressCircular.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, R.string.title_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.svContact.setIconified(true);
        binding.svContact.clearFocus();
        contactList.clear();
        binding.rvDataContact.setVisibility(View.GONE);
        getListContact();
    }
}