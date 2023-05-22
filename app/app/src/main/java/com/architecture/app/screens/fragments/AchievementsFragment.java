package com.architecture.app.screens.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.app.R;
import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.databinding.FragmentAchievementsBinding;
import com.architecture.app.utils.AchievementsGridAdapter;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.Achievement;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AchievementsFragment extends Fragment {
    private FragmentAchievementsBinding _binding;
    private DialogWindow _dialog;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false);
        _dialog = new DialogWindow(requireContext());

        try {
            Achievement[] achievements = AssetsParser.parseAchievements(requireContext());
            AchievementsGridAdapter achievementsGridAdapter = new AchievementsGridAdapter(achievements, requireContext());

            _binding.achievementsGridLayout.setAdapter(achievementsGridAdapter);
            _binding.achievementsGridLayout.setOnItemClickListener((adapterView, view, a, b) -> {
                Achievement currentAchievement = (Achievement)adapterView.getSelectedItem();

                System.out.println(view.findViewById(R.id.achievement_label));

                try {
                    _dialog.setVariant(DialogVariant.INFO, requireContext())
                            .setTitle(currentAchievement.label)
                            .setMessage(currentAchievement.description)
                            .setIcon(AssetsParser.readPreviewImage(requireContext(), currentAchievement.preview))
                           .show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch(IOException exception) {
            _dialog.setVariant(DialogVariant.DANGER, requireContext())
                    .setTitle("Ошибка")
                    .setMessage("Не удалось загрузить данные достижений!")
                    .show();
        }

        return _binding.getRoot();
    }
}