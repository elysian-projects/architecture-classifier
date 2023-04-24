package com.architecture.app.screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.permission.Permissions;
import com.architecture.app.R;
import com.architecture.app.components.DialogWindow;
import com.architecture.app.image.AbstractImageLoader;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;

import java.util.Objects;

public class UploadActivity extends AppCompatActivity {
    private Button _cameraButton;
    private Button _galleryButton;
    private ImageView _image;
    private DialogWindow _dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupActionBarTitle();
        initializeUIComponents();

        _cameraButton.setOnClickListener(view -> {
            if(grantPermission(Permissions.CAMERA, Permissions.CAMERA_REQUEST_CODE)) {
                Intent intent = new Intent().setAction(Permissions.CAMERA);
                startActivityForResult(intent, Permissions.CAMERA_REQUEST_CODE);
            }
        });
        _galleryButton.setOnClickListener(view -> {
            if(grantPermission(Permissions.READ_STORAGE, Permissions.STORAGE_REQUEST_CODE)) {
                Intent intent = new Intent().setType("image/*").setAction(Permissions.READ_STORAGE);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), Permissions.STORAGE_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        try {
            AbstractImageLoader imageLoader = new ImageLoaderFactory().create(requestCode, getApplicationContext());
            Bitmap bitmapImage = imageLoader.load(data);

            setImage(bitmapImage);
            classifyImage(bitmapImage);
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap image) {
        ModelLoader modelLoader = new ModelLoader(getApplicationContext());
        openResultDialog(modelLoader.classifyImage(image));
    }

    private void setImage(Bitmap image) {
        _image.setImageBitmap(image);
    }

    private void openResultDialog(ModelResponse response) {
        if(response.found()) {
            _dialog.setSuccessfulState();
        } else {
            _dialog.setFailedState();
        }

        _dialog.show(
            response.message(),
            response.found()
                ? ModelResponse.SUCCESSFUL_RESPONSE_SHORT
                : ModelResponse.FAILED_RESPONSE_SHORT
        );
    }

    private boolean grantPermission(String permissionName, int requestCode) {
        if(!Permissions.permissionGranted(this, permissionName)) {
            requestPermissions(
                new String[] {permissionName},
                requestCode
            );
       }

        return Permissions.permissionGranted(this, permissionName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int grantResult : grantResults) {
            if(grantResult == PackageManager.PERMISSION_DENIED) {
                _dialog.setFailedState();
                _dialog.show("Ошибка", "Не удалось получить доступ к источнику изображения!");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupActionBarTitle() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Добавить изображение");
        }
    }

    private void initializeUIComponents() {
        _cameraButton = findViewById(R.id.openCameraButton);
        _galleryButton = findViewById(R.id.openGalleryButton);
        _image = findViewById(R.id.imagePreview);
        _dialog = new DialogWindow(UploadActivity.this);
    }
}
