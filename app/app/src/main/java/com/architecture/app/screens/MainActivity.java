package com.architecture.app.screens;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.R;
import com.architecture.app.components.Navbar;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Navbar().attachToLayout(this);
    }
 }