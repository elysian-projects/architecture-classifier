package com.architecture.app.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.gms.vision.detector.Detection;
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector;
import org.tensorflow.lite.task.gms.vision.detector.ObjectDetector.ObjectDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ModelLoader {
    private final Context _context;
    private final String _model;

    public ModelLoader(@NonNull Context context, @NonNull String model) {
        _context = context;
        _model = model;
    }

    public void detect(String path) {
        TensorImage image = getImage(path);

        BaseOptions baseOptions = BaseOptions.builder().useGpu().build();
        ObjectDetectorOptions options = ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(1)
            .build();

        try {
            ObjectDetector objectDetector = ObjectDetector.createFromFileAndOptions(_context, _model, options);
            List<Detection> results = objectDetector.detect(image);

            objectDetector.close();

            for(Detection result : results) {
                System.out.println(result);
            }
        } catch(IOException exception) {
            System.out.println("Could not make detection!");
            System.out.println(exception.getMessage());
        }
    }

    @Nullable
    public TensorImage getImage(String path) {
        File imageFile = new File(path);

        if(imageFile.exists()) {
            TensorImage image = new TensorImage(DataType.STRING);
            image.load(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));

            return image;
        }

        return null;
    }
}

