package com.architecture.app.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class GalleryImageLoader extends AbstractImageLoader {
    public GalleryImageLoader(Context context) {
        super(context);
    }

    @Override
    public Bitmap load(Intent data) throws FileNotFoundException {
        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
        return BitmapFactory.decodeStream(inputStream);
    }
}
