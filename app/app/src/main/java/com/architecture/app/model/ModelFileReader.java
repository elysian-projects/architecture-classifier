package com.architecture.app.model;

import android.content.Context;

import com.architecture.app.utils.AssetsParser;
import com.architecture.app.viewModels.ArchitectureNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelFileReader {
    private static List<String> _classNames;

    private final Context _context;

    public ModelFileReader(Context context) {
        _context = context;
    }

    public List<String> readClassNamesList() throws IOException {
        if(_classNames != null && _classNames.size() != 0) {
            return _classNames;
        }

        ArchitectureNode[] nodes = AssetsParser.parseArchitectureTypes(_context);
        List<String> classList = new ArrayList<>();

        Arrays.stream(nodes).forEach(node -> classList.add(node.label));

        // Caching class names to avoid reading every time it is required
        _classNames = classList;

        return classList;
    }
}
