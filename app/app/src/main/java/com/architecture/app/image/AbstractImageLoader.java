package com.architecture.app.image;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.Nullable;

import java.io.IOException;

public abstract class AbstractImageLoader {
    private final ActivityResultRegistry _activityResultRegistry;
    private final Context _context;

    public AbstractImageLoader(ActivityResultRegistry activityResultRegistry, Context context) {
        _activityResultRegistry = activityResultRegistry;
        _context = context;
    }

    protected Context getContext() {
        return _context;
    }

    protected ActivityResultRegistry getRegistry() {
        return _activityResultRegistry;
    }

    @Nullable
    public abstract Bitmap runLoader();
}
