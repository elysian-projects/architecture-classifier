package com.architecture.app.screens.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
}