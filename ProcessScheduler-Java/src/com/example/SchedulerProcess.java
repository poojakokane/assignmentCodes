package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SchedulerProcess {
    private String[] resources;
    private String name;

    public SchedulerProcess(String p) {
        String[] elements = p.split("[\\(\\)]");
        name = elements[0];
        resources = elements[1].split(",");
    }

    public SchedulerProcess(boolean random, int lastProcId) {
        name = "P" + Integer.toString(lastProcId);
        ArrayList<String> resources = new ArrayList<>();
        for(String r : Arrays.asList("A", "B", "C")){
            int prob = new Random().nextInt(100);
            if(prob > 50){
                resources.add(r);
            }
        }

        this.resources = new String[resources.size()];
        int i=0;
        for(String r : resources){
            this.resources[i] = r;
            i++;
        }
    }

    public String[] getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }
}
