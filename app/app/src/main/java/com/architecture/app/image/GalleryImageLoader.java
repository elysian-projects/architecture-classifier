package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;

public class GalleryImageLoader extends AbstractImageLoader {
    private static final String REGISTRY_KEY = "GalleryImageLoader";

    private LoaderCallback _callback;

    private final ActivityResultLauncher<String> _selectPhotoLauncher;
    private Uri _imageUri;

    public GalleryImageLoader(ActivityResultRegistry activityResultRegistry, Context context) {
        super(activityResultRegistry, context);

        _selectPhotoLauncher = getRegistry().register(REGISTRY_KEY, new ActivityResultContracts.GetContent(), result -> {
            _imageUri = result;

            try {
                Bitmap image = Utils.getBitmapFromUri(_imageUri, getContext());
                _callback.run(image);
            } catch(Exception exception) {
                _callback.run(null);
            }
        });
    }

    @Override
    public void runLoader(LoaderCallback callback) {
        _callback = callback;
        _selectPhotoLauncher.launch("image/*");
    }
}
