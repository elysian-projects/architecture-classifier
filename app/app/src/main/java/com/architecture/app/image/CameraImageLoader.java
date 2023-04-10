package com.architecture.app.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class CameraImageLoader extends AbstractImageLoader {
    public CameraImageLoader(Context context) {
        super(context);
    }

    @Override
    public Bitmap load(Intent data) {
        return (Bitmap) data.getExtras().get("data");
    }
}
