package com.pab.mooneyq.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.pab.mooneyq.R;
import com.pab.mooneyq.adapters.CategoryAdapter;
import com.pab.mooneyq.databinding.ActivityInsertBinding;
import com.pab.mooneyq.models.CategoryResponse;
import com.pab.mooneyq.models.SubmitResponse;
import com.pab.mooneyq.retrofit.ApiEndpoint;
import com.pab.mooneyq.retrofit.ApiService;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertActivity extends BaseActivity {

    private final String TAG = InsertActivity.class.getSimpleName();

    private ActivityInsertBinding binding;
    private final ApiEndpoint api = ApiService.endpoint();

    private CategoryAdapter categoryAdapter;
    private List<CategoryResponse.Data> categories = new ArrayList<>();

    private Integer userId;
    private String categoryId = "", type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("user_id", 0);

        setupView();
        setupRecyclerView();
        setupListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getListCategory();
    }

    private void setupView() {
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(InsertActivity.this, categories, new CategoryAdapter.AdapterListener() {
            @Override
            public void onClick(CategoryResponse.Data result) {
                categoryId = result.getId();
            }
        });

        binding.listCategory.setAdapter(categoryAdapter);
    }

    private void setupListener() {
        binding.btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnIn.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        binding.btnIn, ColorStateList.valueOf(getResources().getColor(R.color.teal_700))
                );

                binding.btnOut.setTextColor(getResources().getColor(R.color.teal_200));
                ViewCompat.setBackgroundTintList(
                        binding.btnOut, ColorStateList.valueOf(getResources().getColor(R.color.white))
                );

                type = "IN";
            }
        });

        binding.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnOut.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        binding.btnOut, ColorStateList.valueOf(getResources().getColor(R.color.teal_700))
                );

                binding.btnIn.setTextColor(getResources().getColor(R.color.teal_200));
                ViewCompat.setBackgroundTintList(
                        binding.btnIn, ColorStateList.valueOf(getResources().getColor(R.color.white))
                );

                type = "OUT";
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRequired()) {
                    binding.buttonSave.setEnabled( false );
                    api.transaction(
                            userId,
                            Integer.parseInt( categoryId ),
                            binding.etKeterangan.getText().toString(),
                            Long.parseLong( binding.etJumlah.getText().toString() ),
                            type
                    ).enqueue(new Callback<SubmitResponse>() {
                        @Override
                        public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                            binding.buttonSave.setEnabled( true );
                            if (response.isSuccessful()) {
                                FancyToast.makeText(getApplicationContext(), "Transaksi berhasil disimpan!"
                                        , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                onBackPressed();
                            }
                        }
                        @Override
                        public void onFailure(Call<SubmitResponse> call, Throwable t) {
                            binding.buttonSave.setEnabled( true );
                        }
                    });
                }
            }
        });
    }

    private void getListCategory() {
        api.listCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    categories = response.body().getData();
                    categoryAdapter.setData(categories);
                }
            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    private Boolean isRequired() {
        if (type.isEmpty()) {
            FancyToast.makeText(getApplicationContext(), "Tentukan tipe transaksi masuk atau keluar"
                    , FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
            return false;
        } else if (categoryId.isEmpty()) {
            FancyToast.makeText(getApplicationContext(), "Kategori transaksi tidak boleh kosong"
                    , FancyToast.LENGTH_SHORT, FancyToast.WARNING, true).show();
            return false;
        } else if (binding.etJumlah.getText().toString().isEmpty()) {
            binding.etJumlah.setError("Masukkan jumlah transaksi");
            return false;
        } else if (binding.etKeterangan.getText().toString().isEmpty()) {
            binding.etKeterangan.setError("Masukkan keterangan transaksi");
            return false;
        }
        return true;
    }

}