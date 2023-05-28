package com.architecture.app.screens.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;
import com.architecture.app.databinding.FragmentQuestionBinding;
import com.architecture.app.databinding.RowTestBinding;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.utils.AssetsTests;
import com.architecture.app.viewModels.TestNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuestionFragment extends Fragment {
    private static final Map<String, Integer> DIFFICULTY_COLORS = new HashMap<>();

    private FragmentQuestionBinding _binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false);

        renderTests();

        return _binding.getRoot();
    }

    private void renderTests() {
        try {
            TestNode[] nodes = AssetsTests.parseArchitectureTypes(requireContext());

            for(TestNode node : nodes) {
                renderRow(node);
            }
        } catch(IOException exception) {
            Log.i("QuestionFragment", "Could not load a preview image", exception);
        }
    }

    private void renderRow(TestNode node) {
        RowTestBinding rowTestBinding = RowTestBinding.inflate(LayoutInflater.from(requireContext()), _binding.getRoot(), false);

        rowTestBinding.testLabel.setText(node.label);
        rowTestBinding.testDescription.setText(node.description);
        rowTestBinding.testDifficulty.setText(node.difficulty);

        setDifficultyColor(rowTestBinding.testDifficulty, node.difficulty);
        loadImageToRow(rowTestBinding.testImagePreview, node.preview);

        _binding.linearLayout.addView(rowTestBinding.getRoot());
    }

    private void loadImageToRow(ImageView imageView, String preview) {
        try {
            imageView.setImageBitmap(AssetsParser.readPreviewImage(requireContext(), preview));
        } catch(Exception exception) {
            Log.i("QuestionFragment", "Could not load a preview image", exception);
        }
    }

    private void setDifficultyColor(TextView textView, String difficulty) {
        fillColors();

        try {
            GradientDrawable difficultyBackground = (GradientDrawable) textView.getBackground();
            difficultyBackground.setColor(ContextCompat.getColor(requireContext(), DIFFICULTY_COLORS.getOrDefault(difficulty, R.color.blue)));

            textView.setBackground(difficultyBackground);
        } catch(NullPointerException exception) {
            Log.e("QuestionFragment", "Could not set difficulty color", exception);
        }
    }

    private void fillColors() {
        if(DIFFICULTY_COLORS.isEmpty()) {
            DIFFICULTY_COLORS.put("Easy", R.color.green);
            DIFFICULTY_COLORS.put("Medium", R.color.yellow);
            DIFFICULTY_COLORS.put("Hard", R.color.red);
            DIFFICULTY_COLORS.put("Legend", R.color.purple_dark);
            DIFFICULTY_COLORS.put("Random", R.color.purple_light);
        }
    }
}