package com.architecture.app.screens.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.databinding.RowArchitectureTypeBinding;
import com.architecture.app.databinding.FragmentHomeBinding;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.utils.Localization;
import com.architecture.app.viewModels.ArchitectureNode;
import com.architecture.app.viewModels.HomeScreenViewModel;

import java.io.IOException;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private HomeScreenViewModel _homeScreenModel;

    private LinearLayout _linearLayout;
    private DialogWindow _dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _homeScreenModel = new HomeScreenViewModel(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater);

        _linearLayout = binding.linearLayout;
        _dialog = new DialogWindow(getContext());

        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if(event == Lifecycle.Event.ON_RESUME) {
                clearLayout();
                rendersTypesRows();
            }
        });

        return binding.getRoot();
    }

    private void rendersTypesRows() {
        try {
            HashMap<ArchitectureNode, Integer> layoutData = _homeScreenModel.getLayoutData();

            for(ArchitectureNode node : layoutData.keySet()) {
                addNewRow(node, layoutData.get(node));
            }
        } catch(NullPointerException exception) {
            Log.i("HomeFragment", "Error getting the found value for a node!", exception);
        } catch(IOException exception) {
            Log.i("HomeFragment", "Error reading data from json file!", exception);
        } catch(Exception exception) {
            Log.i("HomeFragment", "Unknown error", exception);
        }
    }

    private void clearLayout() {
        _linearLayout.removeAllViews();
    }

    private void addNewRow(ArchitectureNode architectureNode, int foundTimes) {
        RowArchitectureTypeBinding rowArchitectureTypeBinding = RowArchitectureTypeBinding.inflate(getLayoutInflater());

        rowArchitectureTypeBinding.architectureTypeTitle.setText(architectureNode.label);
        rowArchitectureTypeBinding.architectureTypeFoundCount.setText(String.valueOf(foundTimes));

        View.OnClickListener listener = (onClickView) -> _dialog.setVariant(DialogVariant.INFO)
                                                                .setTitle(architectureNode.label)
                                                                .setMessage(architectureNode.description)
                                                                .show();

        rowArchitectureTypeBinding.architectureTypeTitle.setOnClickListener(listener);
        rowArchitectureTypeBinding.architectureTypeImagePreview.setOnClickListener(listener);
        rowArchitectureTypeBinding.architectureTypeFoundCount.setOnClickListener(onClickView ->
            showToastWithFoundAmount(architectureNode.label, foundTimes)
        );

        loadImageToRow(rowArchitectureTypeBinding.architectureTypeImagePreview, architectureNode.preview);
        _linearLayout.addView(rowArchitectureTypeBinding.getRoot());

        Log.i("HomeFragment", "Added node: " + architectureNode.value);
    }

    @SuppressLint("DefaultLocale")
    private void showToastWithFoundAmount(String label, int foundCount) {
        Toast.makeText(getContext(),
            String.format(
                "Вы обнаружили %s %d %s",
                label, foundCount, Localization.chooseProperEnding(foundCount, "раз", "раза", "раз")
            ), Toast.LENGTH_LONG
        ).show();
    }

    private void loadImageToRow(ImageView imageView, String preview) {
        try {
            imageView.setImageBitmap(AssetsParser.readPreviewImage(requireContext(), preview));
        } catch(Exception exception) {
            Log.i("HomeFragment", "Could not load a preview image", exception);
        }
    }
}