package com.architecture.app.screens.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.architecture.app.R;
import com.architecture.app.components.DialogWindow;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initializeUI(rootView);
        rendersTypesRows();

        return rootView;
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
            Log.i("HomaFragment", "Unknown error", exception);
        }
    }

    private void initializeUI(View view) {
        _linearLayout = view.findViewById(R.id.linear_layout);
        _dialog = new DialogWindow(getContext());
    }

    private void addNewRow(ArchitectureNode architectureNode, int foundTimes) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_architecture_type, null);

        TextView labelTextView = view.findViewById(R.id.architecture_type_title);
        TextView foundCountTextView = view.findViewById(R.id.architecture_type_found_count);
        ImageView previewImageView = view.findViewById(R.id.architecture_type_image_preview);

        labelTextView.setText(architectureNode.label);
        foundCountTextView.setText(String.valueOf(foundTimes));

        // FIXME: update this function when `DialogWindow` is updated
        View.OnClickListener listener = (onClickView) -> {
            _dialog.setSuccessfulState();
            _dialog.show(architectureNode.label, architectureNode.description);
        };

        labelTextView.setOnClickListener(listener);
        previewImageView.setOnClickListener(listener);

        foundCountTextView.setOnClickListener(onClickView -> {
            showToastWithFoundAmount(architectureNode.label, foundTimes);
        });

        loadImageToRow(previewImageView, architectureNode.preview);

        _linearLayout.addView(view);
    }

    private void showToastWithFoundAmount(String label, int foundCount) {
        Toast.makeText(getContext(), String.format("Вы обнаружили %s %d раз", label, foundCount), Toast.LENGTH_LONG).show();
    }

    private void loadImageToRow(ImageView imageView, String preview) {
        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream(getContext().getAssets().open("previews/" + preview)));
        } catch(Exception ignored) {}
    }
}