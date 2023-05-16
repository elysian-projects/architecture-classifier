package com.architecture.app.image;

import android.content.Context;

import androidx.activity.result.ActivityResultRegistry;

import com.architecture.app.permission.PermissionNotGrantedException;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractImageLoader {
    private final ActivityResultRegistry _activityResultRegistry;

    public AbstractImageLoader(ActivityResultRegistry activityResultRegistry) {
        _activityResultRegistry = activityResultRegistry;
    }

    protected ActivityResultRegistry getRegistry() {
        return _activityResultRegistry;
    }

    public abstract void runLoader(LoaderCallback callback, @NotNull Context context) throws PermissionNotGrantedException;
}
