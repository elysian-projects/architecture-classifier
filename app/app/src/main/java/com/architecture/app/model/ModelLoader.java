package com.architecture.app.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.architecture.app.ml.Model1;

import java.util.List;

public class ModelLoader {
    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 180;

    private final Context _context;

    public ModelLoader(@NonNull Context context) {
        _context = context;
    }

    public String classifyImage(Bitmap image) {
        try {
            if(image == null) {
                throw new IOException("Could not load image!");
            }

            Model1 model = Model1.newInstance(_context);
            Bitmap rescaledImage = Bitmap.createScaledBitmap(image, IMAGE_WIDTH, IMAGE_HEIGHT, true);

            TensorBuffer inputFeature = TensorBuffer.createFixedSize(
                new int[] {1, IMAGE_WIDTH, IMAGE_HEIGHT, 3},
                DataType.FLOAT32
            );

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(rescaledImage);

            ByteBuffer byteBuffer = bitmapToByteBuffer(rescaledImage, IMAGE_WIDTH, IMAGE_HEIGHT);
            inputFeature.loadBuffer(byteBuffer);

            TensorBuffer outputFeature = model.process(inputFeature).getOutputFeature0AsTensorBuffer();

            float[] results = outputFeature.getFloatArray();

            int maxIndex = 0;
            float maxValue = Float.MIN_VALUE;

            for(int i = 0; i < results.length; i++) {
                System.out.println(results[i]);

                if(results[i] > maxValue) {
                    maxIndex = i;
                    maxValue = results[i];
                }
            }

            model.close();

            List<String> labelsList = new ArrayList<>();

            labelsList.add("Ancient Egyptian architecture");
            labelsList.add("Deconstructivism");
            labelsList.add("Greek Revival architecture");

            return labelsList.get(maxIndex);
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    public ByteBuffer bitmapToByteBuffer(Bitmap image, int width, int height) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * width * height * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[width * height];
        image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        int pixel = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
            }
        }
        return byteBuffer;
    }
}

