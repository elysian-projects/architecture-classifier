package com.architecture.app.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

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
                _callback.run((Bitmap)success.getData().getExtras().get("data"));
            } catch(Exception exception) {
                Log.i("CameraImageLoader", "Could not load image!", exception);
            }
        });
    }

    @Override
    public void runLoader(LoaderCallback callback) {
        _callback = callback;

        new Permissions().grantPermission(Permissions.CAMERA, getContext(), getRegistry(), isGranted -> {
            if(isGranted) {
                _takePhotoLauncher.launch(getCameraLoadIntent());
            }
        });
    }

    private Intent getCameraLoadIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
