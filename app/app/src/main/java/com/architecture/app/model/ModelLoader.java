package com.architecture.app.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import java.io.IOException;

import com.architecture.app.ml.Model;

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
                throw new IOException("Could not load image!");
            }

            Model model = Model.newInstance(_context);
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

        } catch (IOException exception) {
            return new ModelResponse(exception.getMessage(), false);
        } catch (InvalidModelResultException exception) {
            return new ModelResponse(String.format("Classification error: %s", exception.getMessage()), false);
        }
    }
}

