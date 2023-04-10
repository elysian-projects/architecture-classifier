package com.architecture.app.image;

import android.content.Context;

public class ImageLoaderFactory {
    public AbstractImageLoader create(int requestCode, Context context) {
        switch(requestCode) {
            case RequestCodes.CAMERA: return new CameraImageLoader(context);
            case RequestCodes.GALLERY:
            default: return new GalleryImageLoader(context);
        }
    }
}
