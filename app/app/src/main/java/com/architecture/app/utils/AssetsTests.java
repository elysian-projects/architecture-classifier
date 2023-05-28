package com.architecture.app.utils;

import android.content.Context;

import com.architecture.app.constants.Assets;
import com.architecture.app.viewModels.TestNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetsTests {
    private static final List<TestNode> _cachedTestNodes = new ArrayList<>();

    public static TestNode[] parseArchitectureTypes(Context context) throws IOException {
        if(_cachedTestNodes.size() != 0) {
            return _cachedTestNodes.toArray(new TestNode[] {});
        }

        TestNode[] nodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.TESTS),
            TestNode[].class
        );

        _cachedTestNodes.addAll(Arrays.asList(nodes));

        return nodes;
    }
}
