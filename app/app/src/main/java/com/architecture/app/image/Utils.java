package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public class Utils {
    @NotNull
    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws FileNotFoundException {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch(FileNotFoundException exception) {
            Log.i("Utils", "File was not found!", exception);
            throw exception;
        }
    }
}
