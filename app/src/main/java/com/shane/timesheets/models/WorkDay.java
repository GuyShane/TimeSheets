package com.shane.timesheets.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkDay {
    private List<Painter> painters;
    private Date date;

    public WorkDay() {
        painters=new ArrayList<>();
        date=new Date();
    }

    public WorkDay(List<Painter> painters) {
        this.painters=painters;
        date=new Date();
    }

    public List<Painter> getPainters() {
        return painters;
    }

    public void setPainters(List<Painter> painters) {
        this.painters = painters;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
