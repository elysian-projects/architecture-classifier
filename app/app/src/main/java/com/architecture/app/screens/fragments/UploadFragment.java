package com.architecture.app.screens.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.architecture.app.R;
import com.architecture.app.components.DialogWindow;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;
import com.architecture.app.permission.PermissionNotGrantedException;
import com.architecture.app.screens.UploadActivity;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class UploadFragment extends Fragment {
    private Button _cameraButton;
    private Button _galleryButton;
    private ImageView _image;
    private DialogWindow _dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        initializeUIComponents(rootView);

        _cameraButton.setOnClickListener(createOnClickListener(RequestCodes.CAMERA));
        _galleryButton.setOnClickListener(createOnClickListener(RequestCodes.GALLERY));

        return rootView;
    }

    private View.OnClickListener createOnClickListener(int requestCode) {
        return (view) -> {
            try {
                new ImageLoaderFactory().create(requestCode, getActivity().getActivityResultRegistry(), getContext()).runLoader(image -> {
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
        ModelLoader modelLoader = new ModelLoader(getContext());
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

    private void initializeUIComponents(View view) {
        _cameraButton = view.findViewById(R.id.openCameraButton);
        _galleryButton = view.findViewById(R.id.openGalleryButton);
        _image = view.findViewById(R.id.imagePreview);
        _dialog = new DialogWindow(getContext());
    }
}