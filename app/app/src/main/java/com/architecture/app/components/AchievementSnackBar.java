package com.architecture.app.components;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.architecture.app.databinding.AchievementNotificationBinding;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.Achievement;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class AchievementSnackBar {
    public static void openSnackBar(Achievement achievement, Context context, View rootView) {
        Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
        AchievementNotificationBinding snackBinding = AchievementNotificationBinding.inflate(LayoutInflater.from(context), null, false);

        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;

        view.setLayoutParams(params);
        view.setBackgroundColor(Color.TRANSPARENT);

        snackBinding.achievementNotificationLabel.setText(achievement.label);
        snackBinding.achievementNotificationDescription.setText(achievement.description);

        snackbar.setBackgroundTint(0XFFFFFF);
        snackbar.setDuration(5000);

        try {
            snackBinding.achievementNotificationImagePreview.setImageBitmap(AssetsParser.readPreviewImage(context, achievement.preview));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ((Snackbar.SnackbarLayout) snackbar.getView()).addView(snackBinding.getRoot(), 0);

        snackbar.show();
    }
}
