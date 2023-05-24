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
import com.architecture.app.constants.Assets;
import com.architecture.app.databinding.FragmentUploadBinding;
import com.architecture.app.image.ImageLoaderFactory;
import com.architecture.app.image.RequestCodes;
import com.architecture.app.model.ModelLoader;
import com.architecture.app.model.ModelResponse;
import com.architecture.app.permission.PermissionNotGrantedException;
import com.architecture.app.utils.AssetsAchievements;
import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.AcquiredAchievementNode;
import com.architecture.app.viewModels.ArchitectureNode;
import com.architecture.app.viewModels.TypeFoundNode;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Locale;

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
            .setButtonsLayout(R.layout.dialod_upload_fragment, requireContext())
            .setClickHandler(createClickHandler)
            .show();
    }

    private final ButtonClickHandler createClickHandler = view -> {
        try {
            int requestCode = ((Button) view).getId() == R.id.camera_button
                ? RequestCodes.CAMERA
                : RequestCodes.GALLERY;

            new ImageLoaderFactory().create(
                requestCode,
                requireActivity().getActivityResultRegistry(),
                requireContext()
            ).runLoader(this::runClassification, requireContext());

        } catch(PermissionNotGrantedException exception) {
            _dialog.setVariant(DialogVariant.DANGER, requireContext()).setTitle("Ошибка!").setMessage("Не удалось получить доступ к источнику изображений!").show();
            Log.i("UploadFragment", "Access was not acquired!", exception);
        } catch(Exception exception) {
            _dialog.setVariant(DialogVariant.DANGER, requireContext()).setTitle("Ошибка!").setMessage("Неизвестная ошибка!").show();
            Log.i("UploadFragment", "Unhandled error!", exception);
        }
    };

    private void runClassification(@NonNull Bitmap image) {
        try {
            setImage(image);
            classifyImage(image);
        } catch(Exception exception) {
            Log.i("UploadFragment", "Unhandled error!", exception);

            _dialog.setVariant(DialogVariant.DANGER, requireContext())
                    .setTitle("Ошибка!")
                    .setMessage("Произошла неизвестная ошибка!")
                    .show();
        }
    }

    private void classifyImage(Bitmap image) {
        ModelLoader modelLoader = new ModelLoader();
        ModelResponse response = modelLoader.classifyImage(image, requireContext());

        if(response.ok()) {
            int foundTimes = increaseFoundNodeCounter(response.node());
            checkAchievement(response.node(), foundTimes);
        }

        setTextInfo(response);
        openResultDialog(response);
    }

    private TypeFoundNode[] getFoundNodes() throws IOException {
        return AssetsParser.parseTypesFoundData(requireContext());
    }

    private int increaseFoundNodeCounter(ArchitectureNode node) {
        try {
            TypeFoundNode[] foundNodes = getFoundNodes();
            int foundTimes = 0;

            for(TypeFoundNode foundNode : foundNodes) {
                if(foundNode.value.equalsIgnoreCase(node.value)) {
                    foundNode.increase();
                    foundTimes = foundNode.foundTimes;
                    break;
                }
            }

            AssetsParser.writeNodes(requireContext(), foundNodes, Assets.TYPES_FOUND_DATA);
            return foundTimes;
        } catch(IOException exception) {
            Log.i("UploadActivity", "Increasing counter failed", exception);
            return 0;
        }
    }

    private void checkAchievement(ArchitectureNode node, int foundTimes) {
        try {
            String candidateCondition = String.format(Locale.getDefault(), "%s.%d", node.value, foundTimes);
            AcquiredAchievementNode[] achievementNodes = AssetsAchievements.parseAcquiredAchievementsData(requireContext());

            for(AcquiredAchievementNode achievementNode : achievementNodes) {
                if(candidateCondition.equalsIgnoreCase(achievementNode.condition) && !achievementNode.acquired) {
                    AcquiredAchievementNode.triggerNewAchievement(requireContext(), requireView(), achievementNodes, candidateCondition);
                    return;
                }
            }
        } catch(IOException exception) {
            Log.w("UploadFragment", "Checking achievement failed", exception);
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

        _dialog.setVariant(variant, requireContext())
                .setTitle(response.node().label)
                .setMessage(message)
                .show();
    }
}