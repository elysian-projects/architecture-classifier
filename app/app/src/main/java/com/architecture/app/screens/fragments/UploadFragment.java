package com.architecture.app.screens.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.architecture.app.R;
import com.architecture.app.components.dialog.ButtonClickHandler;
import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.components.dialog.DialogWindowSingleButtonsLayout;
import com.architecture.app.databinding.FragmentUploadBinding;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;
import com.architecture.app.permission.PermissionNotGrantedException;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.ArchitectureNode;
import com.architecture.app.viewModels.TypeFoundNode;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class UploadFragment extends Fragment {
    private FragmentUploadBinding _binding;

    private DialogWindow _dialog;
    private DialogWindowSingleButtonsLayout _imageUploadDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        _binding = FragmentUploadBinding.inflate(inflater, container, false);

        initializeUIComponents();
        setEventListeners();
        openUploadDialog();

        return _binding.getRoot();
    }

    private void initializeUIComponents() {
        _dialog = new DialogWindow(requireContext());
        _imageUploadDialog = new DialogWindowSingleButtonsLayout(requireContext());
    }

    private void setEventListeners() {
        _binding.uploadImageButton.setOnClickListener(view -> openUploadDialog());
    }

    private void openUploadDialog() {
        _imageUploadDialog
            .setButtonsLayout(R.layout.dialod_upload_fragment)
            .setClickHandler(createClickHandler)
            .show();
    }

    private final ButtonClickHandler createClickHandler = view -> {
        try {
            int requestCode = ((Button) view).getText() == getString(R.string.camera_upload_button_value)
                ? RequestCodes.CAMERA
                : RequestCodes.GALLERY;

            new ImageLoaderFactory().create(requestCode, requireActivity().getActivityResultRegistry(), requireContext()).runLoader(image -> {
                if(image != null) {
                    runClassification(image);
                }
            });
        } catch(PermissionNotGrantedException exception) {
            _dialog.setVariant(DialogVariant.DANGER).setTitle("Ошибка!").setMessage("Не удалось получить доступ к источнику изображений!").show();
            Log.i("UploadFragment", "Access was not acquired!", exception);
        } catch(Exception exception) {
            _dialog.setVariant(DialogVariant.DANGER).setTitle("Ошибка!").setMessage("Неизвестная ошибка!").show();
            Log.i("UploadFragment", "Unhandled error!", exception);
        }
    };

    private void runClassification(Bitmap image) {
        try {
            setImage(image);
            classifyImage(image);
        } catch(Exception exception) {
            Log.i("UploadFragment", "Unhandled error!", exception);

            _dialog.setVariant(DialogVariant.DANGER)
                    .setTitle("Ошибка!")
                    .setMessage("Произошла неизвестная ошибка!")
                    .show();
        }
    }

    private void classifyImage(Bitmap image) {
        ModelLoader modelLoader = new ModelLoader(requireContext());
        ModelResponse response = modelLoader.classifyImage(image);

        if(response.ok()) {
            increaseFoundNodeCounter(response.node());
        }

        setTextInfo(response);
        openResultDialog(response);
    }

    private TypeFoundNode[] getFoundNodes() throws IOException {
        return AssetsParser.parseTypesFoundData(requireContext());
    }

    private void increaseFoundNodeCounter(ArchitectureNode node) {
        try {
            TypeFoundNode[] foundNodes = getFoundNodes();

            for(TypeFoundNode foundNode : foundNodes) {
                if(foundNode.value.equalsIgnoreCase(node.value)) {
                    foundNode.increase();
                    break;
                }
            }

            AssetsParser.writeFoundNodes(requireContext(), foundNodes);
        } catch(IOException exception) {
            Log.i("UploadActivity", "Increasing counter failed", exception);
        }
    }

    private void setImage(Bitmap image) {
        _binding.imagePreview.setImageBitmap(image);
    }

    private void setTextInfo(ModelResponse response) {
        _binding.uploadResultTitle.setText(response.node().label);
        _binding.uploadResultDescription.setText(response.node().description);
    }

    private void openResultDialog(ModelResponse response) {
        DialogVariant variant = response.ok()
            ? DialogVariant.SUCCESS
            : DialogVariant.WARNING;

        String message = response.ok()
            ? ModelResponse.SUCCESSFUL_RESPONSE_SHORT
            : ModelResponse.FAILED_RESPONSE_SHORT;

        _dialog.setVariant(variant)
                .setTitle(response.node().label)
                .setMessage(message)
                .show();
    }
}