package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;

import androidx.annotation.Nullable;

public class Utils {
    @Nullable
    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), uri));
        } catch(Exception exception) {
            return null;
        }
    }
}
