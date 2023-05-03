package com.architecture.app.screens;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.image.RequestCodes;
import com.architecture.app.R;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;
import com.architecture.app.permission.PermissionNotGrantedException;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.ArchitectureNode;
import com.architecture.app.viewModels.TypeFoundNode;

import java.io.IOException;
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
                _dialog.setVariant(DialogVariant.DANGER).setTitle("Ошибка!").setMessage("Не удалось получить доступ к источнику изображений!").show();
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
        ModelResponse response = new ModelLoader(getApplicationContext()).classifyImage(image);

        try {
            if(response.found()) {
                increaseFoundNodeCounter(response.message());
            }
        } catch(Exception exception) {
            Log.i("UploadActivity", "Increasing counter failed", exception);
        }

        openResultDialog(response);
    }

    private TypeFoundNode[] getFoundNodes() throws IOException {
        return AssetsParser.parseTypesFoundData(getApplicationContext());
    }

    private void increaseFoundNodeCounter(String label) throws IOException {
        ArchitectureNode[] nodes = AssetsParser.parseArchitectureTypes(getApplicationContext());
        TypeFoundNode[] foundNodes = getFoundNodes();

        String value = "";

        for(ArchitectureNode node : nodes) {
            if(node.label.equalsIgnoreCase(label)) {
                value = node.label;
                break;
            }
        }

        for(TypeFoundNode foundNode : foundNodes) {
            if(foundNode.value.equalsIgnoreCase(value)) {
                foundNode.increase();
                return;
            }
        }

        AssetsParser.writeFoundNodes(getApplicationContext(), foundNodes);
    }

    private void setImage(Bitmap image) {
        _image.setImageBitmap(image);
    }

    private void openResultDialog(ModelResponse response) {
        DialogVariant variant = response.found()
            ? DialogVariant.SUCCESS
            : DialogVariant.WARNING;

        String message = response.found()
            ? ModelResponse.SUCCESSFUL_RESPONSE_SHORT
            : ModelResponse.FAILED_RESPONSE_SHORT;

        _dialog.setVariant(variant)
                .setTitle(response.message())
                .setMessage(message)
                .show();
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
