package com.architecture.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.architecture.app.components.Navbar;
import com.architecture.app.image.AbstractImageLoader;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.permission.Permissions;

public class MainActivity extends AppCompatActivity {
    private Button _cameraButton;
    private Button _galleryButton;
    private TextView _label;
    private ImageView _image;
    private Navbar _navbar;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeUI();
        grantPermissions();

        _cameraButton.setOnClickListener(
            view -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RequestCodes.CAMERA);
            }
        );
        _galleryButton.setOnClickListener(
            view -> {
                Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), RequestCodes.GALLERY);
            }
        );
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        try {
            AbstractImageLoader imageLoader = new ImageLoaderFactory().create(requestCode, getApplicationContext());
            setImage(imageLoader.load(data));
        } catch(Exception exception) {
            _label.setText(exception.getMessage());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImage(Bitmap image) {
        _image.setImageBitmap(image);

        ModelLoader modelLoader = new ModelLoader(getApplicationContext());
        _label.setText(modelLoader.classifyImage(image));
    }

    private void grantPermissions() {
        boolean hasCameraAccess = checkSelfPermission(Permissions.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasStorageAccess = checkSelfPermission(Permissions.READ_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!hasCameraAccess) {
            ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Permissions.CAMERA},
                Permissions.CAMERA_REQUEST_CODE
            );
        }

        if(!hasStorageAccess) {
            ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Permissions.READ_STORAGE},
                Permissions.STORAGE_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                grantPermissions();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initializeUI() {
        _cameraButton = findViewById(R.id.cameraButton);
        _galleryButton = findViewById(R.id.galleryButton);
        _image = findViewById(R.id.image);
        _label = findViewById(R.id.textView);
        _navbar = new Navbar(this);
    }
}