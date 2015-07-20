package com.shane.timesheets.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkDay {
    private List<PainterDay> painterDays;
    private Date date;
    private int id;

    public WorkDay() {
        painterDays = new ArrayList<>();
        date = new Date();
    }

    public WorkDay(List<PainterDay> painterDays) {
        this.painterDays = painterDays;
        date = new Date();
    }

    public WorkDay(int id, List<PainterDay> painterDays) {
        this.id = id;
        this.painterDays = painterDays;
        date = new Date();
    }

    public List<PainterDay> getPainterDays() {
        return painterDays;
    }

    public void setPainterDays(List<PainterDay> painterDays) {
        this.painterDays = painterDays;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
