package com.architecture.app.image;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public interface LoaderCallback {
    void run(@NonNull Bitmap image);
}
