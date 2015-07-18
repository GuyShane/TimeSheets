package com.shane.timesheets.models;

public class Painter {
    int id;
    private String name;
    private double wage;

    public Painter(int id, String name, double wage) {
        this.id = id;
        this.name = name;
        this.wage = wage;
    }

    public Painter(String name, double wage) {
        this.name = name;
        this.wage = wage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
