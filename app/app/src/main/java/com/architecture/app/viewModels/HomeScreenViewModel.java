package com.architecture.app.viewModels;

import android.content.Context;
import android.util.Log;

import com.architecture.app.utils.AssetsParser;

import java.io.IOException;
import java.util.HashMap;

public class HomeScreenViewModel {
    private static ArchitectureNode[] _nodes;

    private final Context _context;

    public HomeScreenViewModel(Context context) {
        _context = context;
    }

    public HashMap<ArchitectureNode, Integer> getLayoutData() throws IOException {
        ArchitectureNode[] nodes = readNodesFromStorage();
        TypeFoundNode[] nodesFoundData = readNodesFoundData();

        HashMap<ArchitectureNode, Integer> foundListMatch = new HashMap<>();

        for(ArchitectureNode node : nodes) {
            for(int i = 0; i < nodesFoundData.length; i++) {
                TypeFoundNode foundTypeNode = nodesFoundData[i];

                boolean found = node.value.equalsIgnoreCase(foundTypeNode.value);

                if(found) {
                    foundListMatch.put(node, foundTypeNode.foundTimes);
                }

                // In case if the default data in file has a mistake, we need to make sure the found list has the same
                // length as the nodes list by adding zero to the index with
                boolean lastStep = i == nodesFoundData.length - 1;

                if(lastStep && !found) {
                    foundListMatch.put(node, 0);
                }
            }
        }

        return foundListMatch;
    }

    private ArchitectureNode[] readNodesFromStorage() throws IOException {
        if(_nodes == null) {
            _nodes = AssetsParser.parseArchitectureTypes(_context);
            Log.i("HomeFragment", "Successfully read nodes from storage!");
        }

        return _nodes;
    }

    private TypeFoundNode[] readNodesFoundData() throws IOException {
        return AssetsParser.parseTypesFoundData(_context);
    }
}
