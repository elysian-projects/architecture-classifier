package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import java.io.IOException;

public class GalleryImageLoader extends AbstractImageLoader {
    private static final String REGISTRY_KEY = "GalleryImageLoader";

    private ActivityResultLauncher<String> _selectPhotoLauncher;
    private Uri _tempImageUri;

    public GalleryImageLoader(ActivityResultRegistry activityResultRegistry, Context context) {
        super(activityResultRegistry, context);

        registerAction();
    }

    @Override
    @Nullable
    public Bitmap runLoader() {
        _selectPhotoLauncher.launch("image/*");

        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), _tempImageUri));
        } catch(IOException exception) {
            return null;
        }
    }

    private void registerAction() {
        _selectPhotoLauncher = getRegistry().register(REGISTRY_KEY, new ActivityResultContracts.GetContent(), result -> {
            if(result != null) {
                _tempImageUri = result;
            }
        });
    }
}
