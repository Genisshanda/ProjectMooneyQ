package com.pab.mooneyq.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;
import com.pab.mooneyq.R;
import com.pab.mooneyq.adapters.CategoryAdapter;
import com.pab.mooneyq.databinding.ActivityInsertBinding;
import com.pab.mooneyq.models.CategoryResponse;
import com.pab.mooneyq.models.SubmitResponse;
import com.pab.mooneyq.models.TransactionRequest;
import com.pab.mooneyq.models.TransactionResponse;
import com.pab.mooneyq.retrofit.ApiEndpoint;
import com.pab.mooneyq.retrofit.ApiService;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends BaseActivity {

    private ActivityInsertBinding binding;
    TransactionResponse.Data transaction;
    private final ApiEndpoint api = ApiService.endpoint();
    private CategoryAdapter categoryAdapter;
    private List<CategoryResponse.Data> categories = new ArrayList<>();
    private Integer categoryId = 0;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transaction = (TransactionResponse.Data) getIntent().getSerializableExtra("transaction");
        Log.e("intentTransaction", transaction.toString());

        setupView();
        setupRecyclerView();
        setupListener();
        getListCategory();
    }

    private void setupView(){
        switch (transaction.getType()) {
            case "IN": type = "IN"; buttonType(binding.btnIn);
                break;
            case "OUT": type = "OUT"; buttonType(binding.btnOut);
                break;
        }
        binding.etKeterangan.setText(transaction.getDescription());
        binding.etJumlah.setText(transaction.getAmount().toString());
        binding.buttonSave.setText("Simpan perubahan");
    }

    private void setupRecyclerView(){
        categoryAdapter = new CategoryAdapter(UpdateActivity.this, categories, new CategoryAdapter.AdapterListener() {
            @Override
            public void onClick(CategoryResponse.Data result) {
                categoryId = Integer.parseInt(result.getId());
            }
        });
        binding.listCategory.setAdapter( categoryAdapter );
    }

    private void buttonType(MaterialButton buttonSelected){
        List<MaterialButton> buttonList = new ArrayList<>();
        buttonList.add( binding.btnIn );
        buttonList.add( binding.btnOut );
        for (MaterialButton button : buttonList) {
            button.setTextColor(getResources().getColor(R.color.teal_200));
            ViewCompat.setBackgroundTintList( button, ColorStateList.valueOf(getResources().getColor(R.color.white)) );
        }
        buttonSelected.setTextColor(getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(
                buttonSelected, ColorStateList.valueOf(getResources().getColor(R.color.teal_700))
        );
    }

    private void setupListener(){
        binding.btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonType( (MaterialButton) view );
                type = "IN";
            }
        });
        binding.btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonType( (MaterialButton) view );
                type = "OUT";
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            @Override
            public void onClick(View view) {
                updateTransaction(
                        new TransactionRequest(
                                transaction.getId().toString(),
                                getIntent().getIntExtra("user_id", 0),
                                categoryId,
                                type,
                                Integer.parseInt(binding.etJumlah.getText().toString()),
                                binding.etKeterangan.getText().toString()
                        )
                );
                FancyToast.makeText(getApplicationContext(), "Transaksi berhasil diupdate!"
                        , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                onBackPressed();
            }
        });
    }

    private void getListCategory(){
        api.listCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    categories = response.body().getData();
                    categoryAdapter.setData( categories );
                    for (CategoryResponse.Data category: categories) {
                        if (category.getName().contains( transaction.getCategory() )) {
                            categoryAdapter.setButtonList(category);
                            categoryId = Integer.parseInt(category.getId());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    private void updateTransaction(TransactionRequest transactionRequest){
        api.transaction(transactionRequest).enqueue(new Callback<SubmitResponse>() {
            @Override
            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                if (response.isSuccessful()) {
                    SubmitResponse submitResponse = response.body();
                    FancyToast.makeText(UpdateActivity.this, submitResponse.getMessage()
                            , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<SubmitResponse> call, Throwable t) {

            }
        });
    }
}