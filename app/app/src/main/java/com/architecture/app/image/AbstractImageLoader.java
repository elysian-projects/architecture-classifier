package com.architecture.app.image;

import android.content.Context;

import androidx.activity.result.ActivityResultRegistry;

import com.architecture.app.permission.PermissionNotGrantedException;

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

    public abstract void runLoader(LoaderCallback callback) throws PermissionNotGrantedException;
}
