package com.architecture.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.image.AbstractImageLoader;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;

public class MainActivity extends AppCompatActivity {
    private Button _cameraButton;
    private Button _galleryButton;
    private TextView _label;
    private ImageView _image;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeUI();

        _cameraButton.setOnClickListener(
            view -> setupActivity(MediaStore.ACTION_IMAGE_CAPTURE, RequestCodes.CAMERA)
        );
        _galleryButton.setOnClickListener(
            view -> setupActivity(MediaStore.ACTION_PICK_IMAGES, RequestCodes.GALLERY)
        );
    }

    private void setupActivity(String intentType, int requestCode) {
        Intent galleryIntent = new Intent(intentType);

        // FIXME: this method is deprecated, better find a modern approach
        startActivityForResult(galleryIntent, requestCode);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) {
            _label.setText("Error loading file!");
            return;
        }

        try {
            AbstractImageLoader imageLoader = new ImageLoaderFactory().create(requestCode, getApplicationContext());
            Bitmap photo = imageLoader.load(data);

            _image.setImageBitmap(photo);
        } catch(Exception exception) {
            _label.setText(exception.getMessage());
        }
    }

    private void initializeUI() {
        _cameraButton = findViewById(R.id.cameraButton);
        _galleryButton = findViewById(R.id.galleryButton);
        _image = findViewById(R.id.image);
        _label = findViewById(R.id.textView);
    }
}