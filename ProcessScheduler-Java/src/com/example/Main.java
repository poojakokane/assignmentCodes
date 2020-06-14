package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]){
        ArrayList<String> testcases = getTestcasesFromFile("input.txt");
        for(String t : testcases) {
            Scheduler s = new Scheduler(t);
            s.execute();
        }
    }

    private static ArrayList<String> getTestcasesFromFile(String inputFile) {
        ArrayList<String> testcases = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String s;
            while((s = br.readLine()) != null && s.length() != 0){
                testcases.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testcases;
    }
}
