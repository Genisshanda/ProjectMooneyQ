package com.pab.mooneyq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pab.mooneyq.databinding.ActivityRegisterBinding;
import com.pab.mooneyq.models.SubmitResponse;
import com.pab.mooneyq.retrofit.ApiEndpoint;
import com.pab.mooneyq.retrofit.ApiService;
import com.pab.mooneyq.retrofit.ErrorUtil;
import com.shashank.sony.fancytoastlib.FancyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private final ApiEndpoint api = ApiService.endpoint();

    private String nama, email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();

        mAuth = FirebaseAuth.getInstance();
    }

    private void createUser() {
        nama = binding.etNama.getText().toString();
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();

        if (TextUtils.isEmpty(nama)) {
            binding.etNama.setError("Nama tidak boleh kosong!");
            binding.etNama.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email tidak boleh kosong!");
            binding.etEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Kata sandi tidak boleh kosong!");
            binding.etPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FancyToast.makeText(RegisterActivity.this, "Daftar akun telah berhasil!"
                                , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        FancyToast.makeText(RegisterActivity.this, "Daftar akun gagal: " + task.getException().getMessage()
                                , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress(false);
    }

    private void setupListener() {
        binding.btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                if (isRequired()) {
                    showProgress(true);
                    api.register(
                            binding.etNama.getText().toString(),
                            binding.etEmail.getText().toString(),
                            binding.etPassword.getText().toString()
                    ).enqueue(new Callback<SubmitResponse>() {
                        @Override
                        public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                            showProgress(false);
                            if (response.isSuccessful()) {
                                showMessage("Register Successfully!");
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                showMessage(ErrorUtil.getMessage(response));
                            }
                        }
                        @Override
                        public void onFailure(Call<SubmitResponse> call, Throwable t) {
                            showProgress(false);
                        }
                    });
                } else {
                    showMessage("Isi data dengan benar");
                }
            }
        });

        binding.tvMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private Boolean isRequired() {
        return (
                binding.etNama.getText() != null && binding.etEmail.getText() != null &&
                        binding.etPassword.getText() != null
        );
    }

    private void showMessage(String message) {
        FancyToast.makeText(RegisterActivity.this, message
                , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
    }

    private void showProgress(Boolean progress) {
        if (progress) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.btnDaftar.setEnabled(false);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.btnDaftar.setEnabled(true);
        }
    }

}