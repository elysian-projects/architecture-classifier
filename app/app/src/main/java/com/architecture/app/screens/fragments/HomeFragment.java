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
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.ArchitectureTypeNode;

import java.util.Arrays;

public class HomeFragment extends Fragment {
    private LinearLayout _linearLayout;
    private DialogWindow _dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Arrays.asList(AssetsParser.parseArchitectureTypes(requireContext())).forEach(this::addNewRow);
        } catch(Exception exception) {
            Log.i("HomeFragment", "Error reading data from json file!", exception);
        }
    }

    private void initializeUI(View view) {
        _linearLayout = view.findViewById(R.id.linear_layout);
        _dialog = new DialogWindow(getContext());
    }

    private void addNewRow(ArchitectureTypeNode architectureTypeNode) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_architecture_type, null);

        TextView labelTextView = view.findViewById(R.id.architecture_type_title);
        TextView foundCountTextView = view.findViewById(R.id.architecture_type_found_count);
        ImageView previewImageView = view.findViewById(R.id.architecture_type_image_preview);

        labelTextView.setText(architectureTypeNode.label);

        // TODO: get this data from another file
        foundCountTextView.setText(String.valueOf(4));

        // FIXME: update this function when `DialogWindow` is updated
        View.OnClickListener listener = (onClickView) -> {
            _dialog.setSuccessfulState();
            _dialog.show(architectureTypeNode.label, architectureTypeNode.description);
        };

        labelTextView.setOnClickListener(listener);
        previewImageView.setOnClickListener(listener);

        foundCountTextView.setOnClickListener(onClickView -> {
            showToastWithFoundAmount(architectureTypeNode.label, 4);
        });

        loadImageToRow(previewImageView, architectureTypeNode.preview);

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