package com.pab.mooneyq.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

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

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.tvLupaSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.etEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Email harus diisi!");
                } else {
                    progressDialog.setTitle("Sending Email");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    FancyToast.makeText(LoginActivity.this, "Reset kata sandi sudah dikirimkan ke email Anda!"
                                            , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    FancyToast.makeText(LoginActivity.this, e.getMessage()
                                            , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                            });
                }
            }
        });
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
                loginUser();
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

    private void loginUser() {
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email tidak boleh kosong!");
            binding.etEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Kata sandi tidak boleh kosong!");
            binding.etPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FancyToast.makeText(LoginActivity.this, "Login telah berhasil!"
                                , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        FancyToast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage()
                                , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                }
            });
        }
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