package com.example;

public class SchedulerProcess {
    private String[] resources;
    private String name;

    public SchedulerProcess(String p) {
        String[] elements = p.split("[\\(\\)]");
        name = elements[0];
        resources = elements[1].split(",");
    }

    public String[] getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }
}
