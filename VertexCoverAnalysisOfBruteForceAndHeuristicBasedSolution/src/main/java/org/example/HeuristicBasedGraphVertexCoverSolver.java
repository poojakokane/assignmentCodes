package org.example;

import javafx.util.Pair;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class HeuristicBasedGraphVertexCoverSolver extends GraphUtils implements GraphVertexSolver {
    Map<Integer, ArrayList<Integer>> memoized;

    public ArrayList<Integer> solveAndStoreResultStats(int vertexCoverSize) {
        startTimer();

        if(memoized == null){
            memoized = new HashMap<>();
            ArrayList<Integer> minVertexCover = getApproxMinimumVertexCover();
            for(int i=1; i<=g.vertices.size(); i++){
                if(i >= minVertexCover.size()) {
                    memoized.put(i, minVertexCover);
                }
                else {
                    memoized.put(i, null);
                }
            }

            System.out.println("Heuristically found min vertex cover = " + minVertexCover);
        }

        endTimerAndSaveStats();
        return memoized.get(vertexCoverSize);
    }

    private ArrayList<Integer> getApproxMinimumVertexCover() {
        ArrayList<Pair<Integer, Integer>> allEdges = g.getCopyOfAllEdges();

        ArrayList<Integer> result = new ArrayList<Integer>();
        while(!allEdges.isEmpty()) {
            int v = findVertexWithMaxNUmberOfEdges(allEdges);
            result.add(v);
            removeAllEdgesThatContainsThisVertex(allEdges, v);
        }

        return result;
    }

    private int findVertexWithMaxNUmberOfEdges(ArrayList<Pair<Integer, Integer>> allEdges) {
        Map<Integer, Integer> vertexToNumberOfEdgesHavingThatVertex = new HashMap<Integer, Integer>();
        for(Pair<Integer, Integer> x : allEdges){
            int v1 = x.getKey();
            int v2 = x.getValue();
            if(vertexToNumberOfEdgesHavingThatVertex.containsKey(v1)){
                vertexToNumberOfEdgesHavingThatVertex.put(v1, vertexToNumberOfEdgesHavingThatVertex.get(v1)+1);
            }
            else{
                vertexToNumberOfEdgesHavingThatVertex.put(v1, 1);
            }

            if(vertexToNumberOfEdgesHavingThatVertex.containsKey(v2)){
                vertexToNumberOfEdgesHavingThatVertex.put(v2, vertexToNumberOfEdgesHavingThatVertex.get(v2)+1);
            }
            else{
                vertexToNumberOfEdgesHavingThatVertex.put(v2, 1);
            }
        }

        int maxEdges = -1;
        int vertexHavingMaxEdges = -1;
        for(Map.Entry<Integer, Integer> entry : vertexToNumberOfEdgesHavingThatVertex.entrySet()){
            if(entry.getValue() > maxEdges){
                maxEdges = entry.getValue();
                vertexHavingMaxEdges = entry.getKey();
            }
        }

        return vertexHavingMaxEdges;
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

    public void readInGraphFromString(String inputString){
        memoized = null;
        super.readInGraphFromString(inputString);
    }
}
