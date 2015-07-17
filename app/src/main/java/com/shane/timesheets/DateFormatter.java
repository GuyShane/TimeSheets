package com.shane.timesheets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private SimpleDateFormat formatter;

    public DateFormatter() {
        formatter=new SimpleDateFormat("", Locale.getDefault());
    }

    public String getShortDateString() {
        formatter.applyLocalizedPattern("E, MMM dd");
        return formatter.format(new Date());
    }

    public String getShortDateString(int year, int month, int day) {
        formatter.applyLocalizedPattern("E, MMM dd");
        Calendar c= Calendar.getInstance();
        c.set(year, month, day);
        return formatter.format(c.getTime());
    }

    public String getShortDateString(Date date) {
        formatter.applyLocalizedPattern("E, MMM dd");
        return formatter.format(date);
    }

    public String getLongDateString() {
        formatter.applyLocalizedPattern("EEEE, MMM dd");
        return formatter.format(new Date());
    }

    public String getLongDateString(int year, int month, int day) {
        formatter.applyLocalizedPattern("EEEE, MMM dd");
        Calendar c= Calendar.getInstance();
        c.set(year, month, day);
        return formatter.format(c.getTime());
    }

    public String getLongDateString(Date date) {
        formatter.applyLocalizedPattern("EEEE, MMM dd");
        return formatter.format(date);
    }


    public String getDMYString() {
        formatter.applyLocalizedPattern("dd-MM-yyyy");
        return formatter.format(new Date());
    }

    public String getDMYString(int year, int month, int day) {
        formatter.applyLocalizedPattern("dd-MM-yyyy");
        Calendar c= Calendar.getInstance();
        c.set(year, month, day);
        return formatter.format(c.getTime());
    }

    public String getDMYString(Date date) {
        formatter.applyLocalizedPattern("dd-MM-yyyy");
        return formatter.format(date);
    }

    public Date getDate(String dateString) {
        formatter.applyLocalizedPattern("dd-MM-yyyy");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
