package com.architecture.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.architecture.app.model.ModelLoader;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ModelLoader loader = new ModelLoader(getApplicationContext(), "model.tflite");
        loader.detect("image.jpg");
    }
}