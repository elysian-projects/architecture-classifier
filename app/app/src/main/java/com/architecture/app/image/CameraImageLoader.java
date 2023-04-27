package com.architecture.app.image;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;

import com.architecture.app.permission.Permissions;

public class CameraImageLoader extends AbstractImageLoader {
    private static final String REGISTRY_KEY = "CameraImageLoader";

    private LoaderCallback _callback;

    private final ActivityResultLauncher<Intent> _takePhotoLauncher;

    public CameraImageLoader(ActivityResultRegistry activityResultRegistry, Context context) {
        super(activityResultRegistry, context);

        _takePhotoLauncher = getRegistry().register(REGISTRY_KEY, new ActivityResultContracts.StartActivityForResult(), success -> {
            try {
                Bitmap image = Utils.getBitmapFromUri(success.getData().getData(), getContext());
                _callback.run(image);
            } catch(Exception exception) {
                _callback.run(null);
            }
        });
    }

    @Override
    public void runLoader(LoaderCallback callback) {
        _callback = callback;
        _takePhotoLauncher.launch(getCameraLoadIntent());
    }

    private Intent getCameraLoadIntent() {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, "New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Load From Camera");

        Uri image = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image);

        return cameraIntent;
    }
}
