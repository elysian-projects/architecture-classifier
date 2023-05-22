package com.architecture.app.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import com.architecture.app.ml.Model4;
import com.architecture.app.viewModels.ArchitectureNode;

public class ModelLoader {
    private static final ArchitectureNode DEFAULT_ERROR_NODE = new ArchitectureNode("Ошибка!", "error", "", "", "");

    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 180;

    public ModelResponse classifyImage(@NonNull Bitmap image, @NonNull Context context) {
        try {
            Model4 model = Model4.newInstance(context);
            Bitmap rescaledImage = Bitmap.createScaledBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, true);

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(rescaledImage);

            float[] output = model
                    .process(tensorImage.getTensorBuffer())
                    .getOutputFeature0AsTensorBuffer()
                    .getFloatArray();

            model.close();

            ArchitectureNode definedClass = new ModelOutputHandler(context).computeModelClassificationResult(output);
            return new ModelResponse(definedClass, true);

        } catch (Exception exception) {
            exception.printStackTrace();
            return new ModelResponse(DEFAULT_ERROR_NODE, false);
        }
    }
}

