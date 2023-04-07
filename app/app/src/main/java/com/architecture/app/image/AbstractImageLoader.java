package com.architecture.app.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;

public abstract class AbstractImageLoader {
    private final Context _context;

    public AbstractImageLoader(Context context) {
        _context = context;
    }

    public Context getContext() {
        return _context;
    }

    @Nullable
    public abstract Bitmap load(Intent intent) throws FileNotFoundException;
}
