package com.example.notification.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.notification.R;
import com.example.notification.databinding.ActivityMainBinding;
import com.example.notification.databinding.ActivityToBinding;

public class ToActivity extends AppCompatActivity {

    private ActivityToBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}