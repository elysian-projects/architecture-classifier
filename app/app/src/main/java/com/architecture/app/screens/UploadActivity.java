package com.architecture.app.screens;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.image.RequestCodes;
import com.architecture.app.R;
import com.architecture.app.components.DialogWindow;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;
import com.architecture.app.permission.PermissionNotGrantedException;

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

        _cameraButton.setOnClickListener(createOnClickListener(RequestCodes.CAMERA));
        _galleryButton.setOnClickListener(createOnClickListener(RequestCodes.GALLERY));
    }

    // TODO: не переносить
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private View.OnClickListener createOnClickListener(int requestCode) {
        return (view) -> {
            try {
                new ImageLoaderFactory().create(requestCode, getActivityResultRegistry(), getApplicationContext()).runLoader(image -> {
                    if(image != null) {
                        runClassification(image);
                    }
                });
            } catch(PermissionNotGrantedException exception) {
                _dialog.setFailedState();
                _dialog.show("Ошибка", "Не удалось получить доступ к источнику изображений!");
            }
        };
    }

    private void runClassification(Bitmap image) {
        try {
            setImage(image);
            classifyImage(image);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
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
