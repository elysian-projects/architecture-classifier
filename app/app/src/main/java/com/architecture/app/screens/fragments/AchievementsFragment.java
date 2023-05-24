package com.architecture.app.screens.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.databinding.AchievementNotificationBinding;
import com.architecture.app.databinding.FragmentAchievementsBinding;
import com.architecture.app.utils.AchievementsGridAdapter;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.Achievement;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AchievementsFragment extends Fragment {
    private FragmentAchievementsBinding _binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false);

        try {
            Achievement[] achievements = AssetsParser.parseAchievements(requireContext());
            AchievementsGridAdapter achievementsGridAdapter = new AchievementsGridAdapter(achievements);
            GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);

            _binding.achievementsGridLayout.setLayoutManager(manager);
            _binding.achievementsGridLayout.setAdapter(achievementsGridAdapter);
        } catch(IOException exception) {
            new DialogWindow(requireContext()).setVariant(DialogVariant.DANGER, requireContext())
                .setTitle("Ошибка")
                .setMessage("Не удалось загрузить данные достижений!")
                .show();
        }

        return _binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        openSnackBar(new Achievement("Label", "Desc", "achievements/art.jpg"), requireContext());
    }

    private void openSnackBar(Achievement achievement, Context context) {
        Snackbar snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG);
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