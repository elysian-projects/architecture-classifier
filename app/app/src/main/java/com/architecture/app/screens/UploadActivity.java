package com.architecture.app.screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.architecture.app.R;
import com.architecture.app.components.DialogWindow;
import com.architecture.app.image.AbstractImageLoader;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;
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
            grantPermission(MediaStore.ACTION_IMAGE_CAPTURE, RequestCodes.CAMERA);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RequestCodes.CAMERA);

        });
        _galleryButton.setOnClickListener(view -> {
            grantPermission(Intent.ACTION_GET_CONTENT, RequestCodes.GALLERY);

            Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select picture"), RequestCodes.GALLERY);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void grantPermission(String permissionName, int requestCode) {
        if(checkSelfPermission(permissionName) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                new String[] {permissionName},
                requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int grantResult : grantResults) {
            if(grantResult != PackageManager.PERMISSION_GRANTED) {
                grantPermission(permissions[0], requestCode);
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
