package com.pab.mooneyq.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.pab.mooneyq.R;
import com.pab.mooneyq.databinding.ActivityLoginBinding;
import com.pab.mooneyq.models.Login;
import com.pab.mooneyq.retrofit.ApiEndpoint;
import com.pab.mooneyq.retrofit.ApiService;
import com.pab.mooneyq.retrofit.ErrorUtil;
import com.pab.mooneyq.sharedpreferences.PreferencesManager;
import com.shashank.sony.fancytoastlib.FancyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private PreferencesManager pref;
    private final ApiEndpoint api = ApiService.endpoint();
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        pref = new PreferencesManager(this);

        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress(false);
    }

    private void setupListener(){
        binding.btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRequired()) {
                    showProgress(true);
                    api.login(
                            binding.etEmail.getText().toString(),
                            binding.etPassword.getText().toString()
                    ).enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            showProgress(false);
                            if (response.isSuccessful()) {
                                saveLogin( response.body().getData() );
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                showMessage(ErrorUtil.getMessage(response));
                            }
                        }
                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            showProgress(false);
                        }
                    });
                } else {
                    showMessage("Isi data dengan benar");
                }
            }
        });

        binding.tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private Boolean isRequired() {
        return (binding.etEmail.getText() != null && binding.etPassword.getText() != null);
    }

    private void showMessage(String message) {
        FancyToast.makeText(LoginActivity.this, message
                , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
    }

    private void showProgress(Boolean progress) {
        if (progress) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.btnMasuk.setEnabled(false);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.btnMasuk.setEnabled(true);
        }
    }

    private void saveLogin(Login.Data data){
        pref.put("pref_is_login", true);
        pref.put("pref_user_id", data.getId());
        pref.put("pref_user_name", data.getName());
        pref.put("pref_user_email", data.getEmail());
        pref.put("pref_user_date", data.getDate());
        if (pref.getInt("pref_user_avatar") == 0 ) pref.put("pref_user_avatar", R.drawable.avatar2);
    }
}