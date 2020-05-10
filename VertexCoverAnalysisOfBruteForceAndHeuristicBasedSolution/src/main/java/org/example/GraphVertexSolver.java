package org.example;

import java.util.ArrayList;

public interface GraphVertexSolver {

    void readInGraphFromString(String inputString);

    ArrayList<Integer> solveAndStoreResultStats(int vertexCoverSize);

    String showStats();

    String showCurrentGraph();

    int getNumberOfVerticesInGraph();
}
