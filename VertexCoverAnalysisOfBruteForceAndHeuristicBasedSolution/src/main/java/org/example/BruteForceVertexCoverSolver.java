package org.example;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BruteForceVertexCoverSolver extends GraphUtils implements GraphVertexSolver {
    public ArrayList<Integer> solveAndStoreResultStats(int vertexCoverSize) {
        startTimer();

        ArrayList<ArrayList<Integer>> allSetOf_k_Vertices = getAllSetOfKVertices(vertexCoverSize);

        for(ArrayList<Integer> vc : allSetOf_k_Vertices){
            if(isVertexCover(g.getCopyOfAllEdges(), vc)){
                endTimerAndSaveStats();
                return vc;
            }
        }

        endTimerAndSaveStats();
        return null;
    }



    private boolean isVertexCover(ArrayList<Pair<Integer, Integer>> copyOfAllEdges, ArrayList<Integer> verticesInVertexCover) {
        for(int vertex : verticesInVertexCover){
            removeAllEdgesThatContainsThisVertex(copyOfAllEdges, vertex);
        }

        if(copyOfAllEdges.isEmpty())
            return true;

        return false;
    }

    private void removeAllEdgesThatContainsThisVertex(ArrayList<Pair<Integer, Integer>> copyOfAllEdges,
                                                      int targetVertex) {
        List<Pair<Integer, Integer>> elementsToRemove = new ArrayList<Pair<Integer, Integer>>();
        for(int i=0; i<copyOfAllEdges.size(); i++){
            if(copyOfAllEdges.get(i).getKey() == targetVertex || copyOfAllEdges.get(i).getValue() == targetVertex)
                elementsToRemove.add(copyOfAllEdges.get(i));
        }

        for(Pair<Integer, Integer> element : elementsToRemove){
            copyOfAllEdges.remove(element);
        }
    }
}
