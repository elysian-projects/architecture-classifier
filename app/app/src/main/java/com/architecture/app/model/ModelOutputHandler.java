package com.architecture.app.model;

import android.content.Context;
import android.util.Log;

import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.ArchitectureNode;

import java.io.IOException;

public class ModelOutputHandler {
    private final ArchitectureNode[] _nodes;

    public ModelOutputHandler(Context context) throws IOException {
        _nodes = AssetsParser.parseArchitectureTypes(context);
    }

    public ArchitectureNode computeModelClassificationResult(float[] output) throws InvalidModelResultException {
        float maxValue = Float.MIN_VALUE;
        int maxIndex = 0;

        for(int i = 0; i < output.length; i++) {
            Log.i("ModelOutputHandler", String.valueOf(output[i]));

            if(output[i] > maxValue) {
                maxValue = output[i];
                maxIndex = i;
            }
        }

        if(_nodes.length - 1 < maxIndex) {
            throw new InvalidModelResultException();
        }

        return _nodes[maxIndex];
    }
}
