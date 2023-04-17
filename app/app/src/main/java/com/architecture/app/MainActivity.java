package com.architecture.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.components.Navbar;
import com.architecture.app.screens.UploadActivity;

public class MainActivity extends AppCompatActivity {
    private Button _uploadButton;
    private Navbar _navbar;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();

        _uploadButton.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(this, UploadActivity.class);
            startActivity(switchActivityIntent);
        });
    }

    private void initializeUI() {
        _uploadButton = findViewById(R.id.uploadButton);
        _navbar = new Navbar(this);
    }
}