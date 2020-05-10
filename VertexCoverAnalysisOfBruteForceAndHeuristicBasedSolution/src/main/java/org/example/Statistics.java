package org.example;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    Map<Pair<Integer, Integer>, Pair<Integer, Long>> dataEntries;

    public Statistics() {
        dataEntries = new HashMap<>();
    }

    public void insertNewStatDatPoint(int numberOfVertices, int numberOfEdges, long timeTakenForCurrentRun) {
        Pair<Integer, Integer> dataPointKey = new Pair<Integer, Integer>(numberOfVertices, numberOfEdges);
        if(dataEntries.containsKey(dataPointKey)){
            Pair<Integer, Long> dataPoint = dataEntries.get(dataPointKey);
            Pair<Integer, Long> modifiedDataPoint = new Pair<Integer, Long>(dataPoint.getKey() + 1,
                    dataPoint.getValue() + timeTakenForCurrentRun);
            dataEntries.put(dataPointKey, modifiedDataPoint);
        }
        else{
            Pair<Integer, Long> newDataPoint = new Pair<Integer, Long>(1, timeTakenForCurrentRun);
            dataEntries.put(dataPointKey, newDataPoint);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n \nAggregated statistics");
        sb.append("\n=======================");
        sb.append("\nNumber of Vertices\t|").append("Number Of Edges\t|").append("Average time taken in NanoSeconds\n");
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        for(Pair<Integer, Integer> graphDimensions : dataEntries.keySet()){
            Pair<Integer, Long> dataPoint = dataEntries.get(graphDimensions);
            sb.append(graphDimensions.getKey()).append("\t\t\t\t\t|").append(graphDimensions.getValue()).append("\t\t\t\t\t|")
                    .append(dataPoint.getValue() / dataPoint.getKey()).append("\n");
        }
        return sb.toString();
    }
}
