package com.architecture.app.model;

import android.content.Context;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ModelOutputHandler {
    private static final float MIN_DEFINITION_DIFFERENCE = 3f;

    private final List<String> _classNames;

    public ModelOutputHandler(Context context) throws IOException {
        ModelFileReader fileReader = new ModelFileReader(context);
        _classNames = fileReader.readClassNamesList();
    }

    public String computeModelClassificationResult(float[] output) throws InvalidModelResultException {
        // We can't define the correct type when two outputs are very close
        if(!canBeDefined(output)) {
            return "Other";
        }

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

    public boolean canBeDefined(float[] output) {
        Arrays.sort(output);
        int length = output.length;
        return (output[length - 1] - output[length - 2]) > MIN_DEFINITION_DIFFERENCE;
    }
}
