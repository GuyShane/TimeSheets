package com.shane.timesheets.models;

public class PainterDay {
    private Painter painter;
    private double hours;
    private int id;

    public PainterDay(Painter painter, double hours) {
        this.painter = painter;
        this.hours = hours;
    }

    public PainterDay(Painter painter, double hours, int id) {
        this.painter = painter;
        this.hours = hours;
        this.id = id;
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
