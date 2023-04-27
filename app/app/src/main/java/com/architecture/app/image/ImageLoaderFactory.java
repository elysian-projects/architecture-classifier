package com.architecture.app.image;

import android.content.Context;

import androidx.activity.result.ActivityResultRegistry;

public class ImageLoaderFactory {
    public AbstractImageLoader create(int requestCode, ActivityResultRegistry activityResultRegistry, Context context) {
        switch(requestCode) {
            case RequestCodes.CAMERA: return new CameraImageLoader(activityResultRegistry, context);
            case RequestCodes.GALLERY:
            default: return new GalleryImageLoader(activityResultRegistry, context);
        }
    }
}
