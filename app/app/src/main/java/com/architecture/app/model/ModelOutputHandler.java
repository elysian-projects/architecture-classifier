package com.architecture.app.model;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class ModelOutputHandler {
    private final List<String> _classNames;

    public ModelOutputHandler(Context context) throws IOException {
        ModelFileReader fileReader = new ModelFileReader(context);
        _classNames = fileReader.readClassNamesList();
    }

    public String computeModelClassificationResult(float[] output) throws InvalidModelResultException {
        float maxValue = Float.MIN_VALUE;
        int maxIndex = 0;

        for(int i = 0; i < output.length; i++) {
            Log.i("ModelOutputHandler", String.valueOf(output[i]));

            if(output[i] > maxValue) {
                maxValue = output[i];
                maxIndex = i;
            }
        }

        if(_classNames.size() - 1 < maxIndex) {
            throw new InvalidModelResultException();
        }

        return _classNames.get(maxIndex);
    }
}
