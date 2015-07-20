package com.shane.timesheets.models;

import java.util.Date;

public class PainterDay {
    private Painter painter;
    private double hours;
    private Date date;
    private int id;

    public PainterDay(Painter painter, double hours, Date date) {
        this.painter = painter;
        this.hours = hours;
        this.date=date;
    }

    public PainterDay(Painter painter, double hours, Date date, int id) {
        this.painter = painter;
        this.hours = hours;
        this.date=date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
