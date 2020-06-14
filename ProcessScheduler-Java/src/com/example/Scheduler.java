package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Scheduler {
    private boolean randomized;
    private LinkedList<SchedulerProcess> sleeipngQueue;
    private LinkedList<SchedulerProcess> runningQueue;
    private LinkedList<SchedulerProcess> waitingQueue;
    private LinkedList<SchedulerProcess> completedList;
    private Map<String, Boolean> resourceAvailable;

    private int cyclesToRun;

    public Scheduler(String inputString) {
        String[] processes;
        processes = inputString.split(";");

        sleeipngQueue = new LinkedList<SchedulerProcess>();
        for(String p : processes){
            this.sleeipngQueue.addLast(new SchedulerProcess(p));
        }

        randomized = false;

        resourceAvailable = new HashMap<String, Boolean>();
        for(SchedulerProcess p : sleeipngQueue){
            for(String r : p.getResources()){
                resourceAvailable.putIfAbsent(r, true);
            }
        }

        cyclesToRun = 0;

        runningQueue = new LinkedList<SchedulerProcess>();
        waitingQueue = new LinkedList<SchedulerProcess>();
        completedList = new LinkedList<SchedulerProcess>();
    }

    public void execute() {
        while(!sleeipngQueue.isEmpty()){
            // will be used in Part B
            moveProcessesFromWaitingToSleeping();

            // general scheduling
            moveProcessesFromRunningToCompleted();
            moveProcessesFromSleepingToRunning();

            // will be used in Part B
            addNewProcessesToWaiting();

            cyclesToRun++;
        }

        System.out.println("Cycles to complete : " + cyclesToRun);
    }

    private void moveProcessesFromRunningToCompleted() {
        while(!runningQueue.isEmpty()){
            for(String r : runningQueue.peekFirst().getResources()){
                resourceAvailable.put(r, true);
            }
            completedList.addLast(runningQueue.removeFirst());
        }
    }

    private void addNewProcessesToWaiting() {

    }

    private void moveProcessesFromSleepingToRunning() {
        while(canMoveToRunning(sleeipngQueue.peekFirst())){
            for(String r : sleeipngQueue.peekFirst().getResources()){
                resourceAvailable.put(r, false);
            }
            runningQueue.addLast(sleeipngQueue.removeFirst());
        }
    }

    private boolean canMoveToRunning(SchedulerProcess p) {
        if(p == null)
            return false;

        for(String r : p.getResources()){
            if(!resourceAvailable.get(r)){
                return false;
            }
        }
        return true;
    }

    private void moveProcessesFromWaitingToSleeping() {

    }
}
