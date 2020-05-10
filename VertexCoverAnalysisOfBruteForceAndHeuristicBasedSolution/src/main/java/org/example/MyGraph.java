package org.example;

import javafx.util.Pair;

import java.util.*;

public class MyGraph {
    ArrayList<Pair<Integer, Integer>> edges;
    Set<Integer> vertices;

    public void addNewEdge(int node1, int node2) {
        if(edges == null){
            edges = new ArrayList<Pair<Integer, Integer>>();
            vertices = new HashSet<Integer>();
        }
        edges.add(new Pair<Integer, Integer>(node1, node2));
        vertices.add(node1);
        vertices.add(node2);
    }

    public void initializeNewGraph() {
        edges = null;
        vertices = null;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Pair<Integer, Integer> edge : edges){
            sb.append("\n").append(edge.getKey()).append(" - ").append(edge.getValue());
        }
        return sb.toString();
    }

    public int numberOfVertices() {
        return vertices.size();
    }

    public ArrayList<Pair<Integer, Integer>> getCopyOfAllEdges() {
        return new ArrayList<Pair<Integer, Integer>>(edges);
    }
}
