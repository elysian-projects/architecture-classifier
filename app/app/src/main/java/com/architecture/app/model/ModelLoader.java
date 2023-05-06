package com.architecture.app.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import com.architecture.app.ml.Model4;

public class ModelLoader {
    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 180;

    private final Context _context;

    public ModelLoader(@NonNull Context context) {
        _context = context;
    }

    public ModelResponse classifyImage(Bitmap image) {
        try {
            if(image == null) {
                return new ModelResponse("Ошибка!", false);
            }

            Model4 model = Model4.newInstance(_context);
            Bitmap rescaledImage = Bitmap.createScaledBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, true);

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(rescaledImage);

            float[] output = model
                    .process(tensorImage.getTensorBuffer())
                    .getOutputFeature0AsTensorBuffer()
                    .getFloatArray();

            model.close();

            String definedClass = new ModelOutputHandler(_context).computeModelClassificationResult(output);
            return new ModelResponse(definedClass, true);

        } catch (Exception exception) {
            exception.printStackTrace();
            return new ModelResponse("Ошибка!", false);
        }
    }
}

