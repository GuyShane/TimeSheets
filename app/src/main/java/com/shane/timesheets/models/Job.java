package com.shane.timesheets.models;

import com.shane.timesheets.DateFormatter;

import java.util.Date;

public class Job {
    private int id;
    private String title;
    private String address;
    private Date startDate;
    private Date endDate;
    private double cost;
    private DateFormatter df;

    public Job(String title, String address, String startDate, String endDate, double cost) {
        this.df = new DateFormatter();
        this.title = title;
        this.address = address;
        if (startDate != null) {
            this.startDate = df.getDate(startDate);
        } else {
            this.startDate = null;
        }
        if (endDate != null) {
            this.endDate = df.getDate(endDate);
        } else {
            this.endDate = null;
        }
        this.cost = cost;
    }

    public Job(int id, String title, String address, String startDate, String endDate, double cost) {
        this.df = new DateFormatter();
        this.id = id;
        this.title = title;
        this.address = address;
        if (startDate != null) {
            this.startDate = df.getDate(startDate);
        } else {
            this.startDate = null;
        }
        if (endDate != null) {
            this.endDate = df.getDate(endDate);
        } else {
            this.endDate = null;
        }
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        if (startDate != null) {
            return df.getYMDString(startDate);
        } else {
            return null;
        }
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEndDateString() {
        if (endDate != null) {
            return df.getYMDString(endDate);
        } else {
            return null;
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
