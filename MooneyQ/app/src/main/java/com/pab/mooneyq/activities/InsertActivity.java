package com.pab.mooneyq.activities;

import android.os.Bundle;

import com.pab.mooneyq.databinding.ActivityInsertBinding;

public class InsertActivity extends BaseActivity {

    private ActivityInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}