package com.architecture.app.viewModels;

import android.content.Context;
import android.util.Log;

import com.architecture.app.utils.AssetsParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

public class HomeScreenViewModel {
    private static ArchitectureNode[] _nodes;

    public HashMap<ArchitectureNode, Integer> getLayoutData(Context context) throws IOException {
        ArchitectureNode[] nodes = readNodesFromStorage(context);
        TypeFoundNode[] nodesFoundData = readNodesFoundData(context);

        Log.i("HomeScreenViewModel", new Gson().toJson(nodesFoundData));

        HashMap<ArchitectureNode, Integer> foundListMatch = new HashMap<>();

        for(ArchitectureNode node : nodes) {
            for(int i = 0; i < nodesFoundData.length; i++) {
                TypeFoundNode foundTypeNode = nodesFoundData[i];

                Log.i("HomeScreenViewModel", node.value + " " + foundTypeNode.value + " " + node.value.equalsIgnoreCase(foundTypeNode.value));

                if(node.value.equalsIgnoreCase(foundTypeNode.value)) {
                    foundListMatch.put(node, foundTypeNode.foundTimes);
                    break;
                }

                // In case if the default data in file has a mistake, we need to make sure the found list has the same
                // length as the nodes list by adding zero to the index with
                if(i == nodesFoundData.length - 1) {
                    Log.i("HomeScreenViewMode",  "Not found for: " + node.value);
                    foundListMatch.put(node, 0);
                }
            }
        }

        return foundListMatch;
    }

    private ArchitectureNode[] readNodesFromStorage(Context context) throws IOException {
        if(_nodes == null) {
            _nodes = AssetsParser.parseArchitectureTypes(context);
            Log.i("HomeFragment", "Successfully read nodes from storage!");
        }

        return _nodes;
    }

    private TypeFoundNode[] readNodesFoundData(Context context) throws IOException {
        return AssetsParser.parseTypesFoundData(context);
    }
}
