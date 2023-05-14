package com.architecture.app.screens.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;
import com.architecture.app.components.dialog.ButtonClickHandler;
import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.components.dialog.DialogWindowSingleButtonsLayout;
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
    private ImageView _image;
    private TextView _resultTitle;
    private TextView _resultDescription;
    private Button _uploadImageButton;
    private DialogWindow _dialog;
    private DialogWindowSingleButtonsLayout _imageUploadDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        initializeUIComponents(rootView);
        setEventListeners();
        openUploadDialog();

        return rootView;
    }

    private void initializeUIComponents(View view) {
        _image = view.findViewById(R.id.imagePreview);
        _resultTitle = view.findViewById(R.id.upload_result_title);
        _resultDescription = view.findViewById(R.id.upload_result_description);
        _uploadImageButton = view.findViewById(R.id.upload_image_button);
        _dialog = new DialogWindow(getContext());
        _imageUploadDialog = new DialogWindowSingleButtonsLayout(getContext());
    }

    private void setEventListeners() {
        _uploadImageButton.setOnClickListener(view -> openUploadDialog());
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

            new ImageLoaderFactory().create(requestCode, getActivity().getActivityResultRegistry(), getContext()).runLoader(image -> {
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
            exception.printStackTrace();
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
        _image.setImageBitmap(image);
    }

    private void setTextInfo(ModelResponse response) {
        _resultTitle.setText(response.node().label);
        _resultDescription.setText(response.node().description);
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