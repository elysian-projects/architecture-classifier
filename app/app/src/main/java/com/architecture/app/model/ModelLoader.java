package com.architecture.app.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.architecture.app.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ModelLoader {
    private final Context _context;

    public ModelLoader(@NonNull Context context) {
        _context = context;
    }

    public void detect(String path) {
//        try {
//            InterpreterFactoryApi interpreter = new InterpreterFactoryApi(loadModelFile(_context));
//            System.out.println(interpreter.toString());
//        } catch(IOException exception) {
//            System.out.println("Error!");
//            exception.printStackTrace();
//        }

//        TensorImage image = getImage(path);
//
//        BaseOptions baseOptions = BaseOptions.builder().useGpu().build();
//        ObjectDetectorOptions options = ObjectDetectorOptions.builder()
//            .setBaseOptions(baseOptions)
//            .setMaxResults(1)
//            .build();

//        try {
//            ObjectDetector objectDetector = ObjectDetector.createFromFileAndOptions(_context, _model, options);
//            List<Detection> results = objectDetector.detect(image);
//
//            objectDetector.close();
//
//            for(Detection result : results) {
//                System.out.println(result);
//            }
//        } catch(IOException exception) {
//            System.out.println("Could not make detection!");
//            System.out.println(exception.getMessage());
//        }
    }

    @Nullable
    public Bitmap getImage(String path) {
        File imageFile = new File(path);

        try {
            if(imageFile.exists()) {
                Bitmap bitmapImage = null;



                TensorImage image = new TensorImage(DataType.STRING);

                image.load(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));

                return image.getBitmap();
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    public void classifyImage(Bitmap image) {
        try {
            if(image == null) {
                throw new IOException("Could not load image!");
            }

            Model model = Model.newInstance(_context);

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[] {1, 180, 180, 3}, DataType.FLOAT32);

            image = Bitmap.createScaledBitmap(image, 180, 180, true);

            inputFeature0.loadBuffer(TensorImage.fromBitmap(image).getBuffer());

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] results = outputFeature0.getFloatArray();

            for(float result : results) {
                System.out.println(result);
            }

            model.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

