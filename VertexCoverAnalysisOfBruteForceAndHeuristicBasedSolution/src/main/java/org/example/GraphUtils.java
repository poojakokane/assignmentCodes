package org.example;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;

public abstract class GraphUtils implements GraphVertexSolver{
    MyGraph g;
    Statistics s;
    String inputString;
    private long currentRunStartTime;

    public GraphUtils(){
        g = new MyGraph();
        s = new Statistics();
    }

    public void readInGraphFromString(String inputString) {
        this.inputString = inputString;
        Scanner s = new Scanner(new BufferedReader(new StringReader(inputString)));
        g.initializeNewGraph();
        while(s.hasNext()){
            g.addNewEdge(s.nextInt(), s.nextInt());
        }
    }

    public final String showStats() {
        return s.toString();
    }

    public final String showCurrentGraph(){
        return g.toString();
    }

    public final int getNumberOfVerticesInGraph(){
        return g.numberOfVertices();
    }

    public ArrayList<ArrayList<Integer>> getAllSetOfKVertices(int k){
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        List<Integer> listOfNodes = new ArrayList<Integer>(g.vertices);
        Collections.sort(listOfNodes);

        Stack<Integer> currentBuffer = new Stack<Integer>();
        for(int i=0; i<k; i++){
            currentBuffer.add(i);
        }

        result.add(new ArrayList<Integer>(convertToNodeNumbers(listOfNodes, currentBuffer)));

        // Insert back the same number of nodes that were popped above
        while(hasNextSelectionOfNodes(currentBuffer, listOfNodes.size()-1)){
            result.add(new ArrayList<Integer>(convertToNodeNumbers(listOfNodes,
                    setAndReturnNextCombinationOfNodes(currentBuffer, listOfNodes.size()))));
        }

        return result;
    }

    private Stack<Integer> setAndReturnNextCombinationOfNodes(Stack<Integer> currentBuffer, int sizeOfList) {
        int currentSize = sizeOfList-1;
        while(currentBuffer.peek() == currentSize){
            currentBuffer.pop();
            currentSize--;
        }

        currentBuffer.push(currentBuffer.pop()+1);
        while(currentSize < sizeOfList-1){
            currentBuffer.push(currentBuffer.peek()+1);
            currentSize++;
        }

        return currentBuffer;
    }

    private boolean hasNextSelectionOfNodes(Stack<Integer> currentBuffer, int maxElement) {
        for(int i=currentBuffer.size()-1; i>=0; i--){
            if(currentBuffer.get(i) != maxElement)
                return true;
            maxElement--;
        }

        return false;
    }

    private Stack<Integer> convertToNodeNumbers(List<Integer> listOfNodes, Stack<Integer> currentBuffer) {
        Stack<Integer> result = new Stack<Integer>();
        for(int i : currentBuffer) {
            result.push(listOfNodes.get(i));
        }
        return result;
    }

    protected void endTimerAndSaveStats() {
        long endTime = System.nanoTime();
        long timeTakenForCurrentRun = endTime - this.currentRunStartTime;
        System.out.println("Time taken = " + timeTakenForCurrentRun + " ns");

        s.insertNewStatDatPoint(g.vertices.size(), g.edges.size(), timeTakenForCurrentRun);
    }

    protected void startTimer() {
        this.currentRunStartTime = System.nanoTime();
    }
}
