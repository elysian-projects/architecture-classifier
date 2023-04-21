package com.architecture.app.screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.R;
import com.architecture.app.components.Navbar;

public class MainActivity extends AppCompatActivity {
    private Button _uploadButton;

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

        // Navbar logic is hidden inside of the constructor,
        // so there is not need to have any public methods
        new Navbar(this);
    }
}