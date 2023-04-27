package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.Nullable;

public class Utils {
    @Nullable
    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch(Exception exception) {
            return null;
        }
    }
}
