package com.architecture.app.utils;

import android.content.Context;

import com.architecture.app.constants.Assets;
import com.architecture.app.viewModels.ArchitectureTypeFoundCountNode;
import com.architecture.app.viewModels.ArchitectureTypeNode;

import java.io.IOException;

public class AssetsParser {
    private static ArchitectureTypeNode[] _cachedArchitectureTypes;

    public static ArchitectureTypeNode[] parseArchitectureTypes(Context context) throws IOException {
        ArchitectureTypeNode[] nodes = new JSONFileParser().parse(
            context.getAssets().open(Assets.ARCHITECTURE_TYPES),
            ArchitectureTypeNode[].class
        );

        _cachedArchitectureTypes = nodes;

        return nodes;
    }

    public static ArchitectureTypeFoundCountNode[] parseTypesFoundData(Context context) throws IOException {
        // We don't need to cache this data, because it might quickly change multiple times
        return new JSONFileParser().parse(
            context.getAssets().open(Assets.TYPES_FOUND_DATA),
            ArchitectureTypeFoundCountNode[].class
        );
    }
}
