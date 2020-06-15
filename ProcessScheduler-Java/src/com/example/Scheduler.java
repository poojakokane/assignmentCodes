package com.example;

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

    private int lastProcId;

    public Scheduler(String inputString) {
        lastProcId = 0;

        String[] processes;
        processes = inputString.split(";");

        sleeipngQueue = new LinkedList<SchedulerProcess>();
        for(String p : processes){
            this.sleeipngQueue.addLast(new SchedulerProcess(p));
            lastProcId++;
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

    public Scheduler(boolean random) {
        randomized = random;
        sleeipngQueue = new LinkedList<SchedulerProcess>();
        runningQueue = new LinkedList<SchedulerProcess>();
        waitingQueue = new LinkedList<SchedulerProcess>();
        completedList = new LinkedList<SchedulerProcess>();

        resourceAvailable = new HashMap<String, Boolean>();
        resourceAvailable.putIfAbsent("A", true);
        resourceAvailable.putIfAbsent("B", true);
        resourceAvailable.putIfAbsent("C", true);

        cyclesToRun = 0;
    }

    public void execute() {
        while(!sleeipngQueue.isEmpty()){
            // will be used in Part B
            moveProcessesFromWaitingToSleeping();

            // general scheduling
            moveProcessesFromRunningToCompleted();
            moveProcessesFromSleepingToRunning();

            // will be used in Part B
            if(randomized) {
                addNewProcessesToWaiting(2);
            }

            cyclesToRun++;

            if(cyclesToRun % 100 == 0){
                System.out.println("Length of processes at cycle " + cyclesToRun + ": "
                + sleeipngQueue.size());
            }

            if(cyclesToRun >= 1000){
                System.out.println("Not completed after 1000 cycles, breaking!");
                return;
            }
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

    public void addNewProcessesToWaiting(int numProcessesToAdd) {
        while(numProcessesToAdd > 0){
            waitingQueue.addLast(new SchedulerProcess(true, lastProcId));
            numProcessesToAdd--;
            lastProcId++;
        }
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

    public void moveProcessesFromWaitingToSleeping() {
        while(!waitingQueue.isEmpty()){
            sleeipngQueue.addLast(waitingQueue.removeFirst());
        }
    }
}
