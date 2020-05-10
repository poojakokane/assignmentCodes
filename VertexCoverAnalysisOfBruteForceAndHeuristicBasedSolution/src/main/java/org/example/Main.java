package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws FileNotFoundException {
        GraphVertexSolver solution;

        System.out.println("**************************");
        System.out.println("USING BRUTE-FORCE APPROACH");
        System.out.println("**************************");
        solution = new BruteForceVertexCoverSolver();
        Scanner s = new Scanner(new FileReader(args[0]));
        while(s.hasNext()){
            String inputString = s.nextLine();

            //Skip comment lines from beginning of the input file
            if(inputString.startsWith("//")){
                continue;
            }

            solution.readInGraphFromString(inputString);
            System.out.println(solution.showCurrentGraph());
            for(int i=1; i<=solution.getNumberOfVerticesInGraph(); i++) {
                ArrayList<Integer> result = solution.solveAndStoreResultStats(i);
                System.out.println("For the above graph, there " + (result!=null?"IS":"ISN'T") +
                        " a vertex cover of size " + i +
                        (result!=null? ", VertexCover = " + result.toString() : ""));
            }
        }
        System.out.println(solution.showStats());

        System.out.println("\n\n");
        System.out.println("******************************");
        System.out.println("USING HEURISTIC-BASED APPROACH");
        System.out.println("******************************");
        solution = new HeuristicBasedGraphVertexCoverSolver();
        s = new Scanner(new FileReader(args[0]));
        while(s.hasNext()){
            String inputString = s.nextLine();

            //Skip comment lines from beginning of the input file
            if(inputString.startsWith("//")){
                continue;
            }

            solution.readInGraphFromString(inputString);
            System.out.println(solution.showCurrentGraph());
            for(int i=1; i<=solution.getNumberOfVerticesInGraph(); i++) {
                ArrayList<Integer> result = solution.solveAndStoreResultStats(i);
                System.out.println("For the above graph, there " + (result!=null?"IS":"ISN'T") + " a vertex cover of size " + i);
            }
        }
        System.out.println(solution.showStats());

    }

}
