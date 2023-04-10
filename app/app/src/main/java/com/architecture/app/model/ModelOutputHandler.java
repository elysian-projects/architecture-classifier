package com.architecture.app.model;

import android.content.Context;

import java.io.IOException;
import java.util.List;

public class ModelOutputHandler {
    private List<String> _classNames;

    public ModelOutputHandler(Context context) throws IOException {
        ModelFileReader fileReader = new ModelFileReader(context);
        _classNames = fileReader.readClassNamesList();
    }

    public String computeModelClassificationResult(float[] output) throws InvalidModelResultException {
        float maxValue = Float.MIN_VALUE;
        int maxIndex = 0;
        for(int i = 0; i < output.length; i++) {
            System.out.println(output[i]);

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
