package com.shane.timesheets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shane.timesheets.models.Expense;
import com.shane.timesheets.models.Job;
import com.shane.timesheets.models.Painter;
import com.shane.timesheets.models.PainterDay;
import com.shane.timesheets.models.WorkDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db";
    private static final int version=2;

    private DateFormatter df;

    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, version);
        df = new DateFormatter();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Painters.CREATE);
        db.execSQL(DatabaseContract.Jobs.CREATE);
        db.execSQL(DatabaseContract.WorkDays.CREATE);
        db.execSQL(DatabaseContract.JobPainters.CREATE);
        db.execSQL(DatabaseContract.PainterDays.CREATE);
        db.execSQL(DatabaseContract.Expenses.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion==1 && newVersion==2) {
            db.execSQL(DatabaseContract.Expenses.CREATE);
        }
    }

    public boolean insertJob(Job job) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.Jobs.COLUMN_TITLE, job.getTitle());
        c.put(DatabaseContract.Jobs.COLUMN_ADDRESS, job.getAddress());
        c.put(DatabaseContract.Jobs.COLUMN_START_DATE, job.getStartDateString());
        c.put(DatabaseContract.Jobs.COLUMN_END_DATE, job.getEndDateString());
        c.put(DatabaseContract.Jobs.COLUMN_COST, job.getCost());
        try {
            db.insertOrThrow(DatabaseContract.Jobs.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public List<Job> getAllJobs(int completed) {
        List<Job> jobs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Jobs.TABLE_NAME +
                " where " + DatabaseContract.Jobs.COLUMN_COMPLETED +
                "=" + completed, null);
        r.moveToFirst();
        while (!r.isAfterLast()) {
            int id = r.getInt(r.getColumnIndex(DatabaseContract.Jobs._ID));
            String title = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_TITLE));
            String address = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_ADDRESS));
            String start = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_START_DATE));
            String end = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_END_DATE));
            double cost = r.getDouble(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_COST));
            jobs.add(new Job(id, title, address, start, end, cost));
            r.moveToNext();
        }
        r.close();
        return jobs;
    }

    public Job getJobById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Jobs.TABLE_NAME +
                " where " + DatabaseContract.Jobs._ID +
                "=" + id, null);
        r.moveToFirst();
        String title = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_TITLE));
        String address = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_ADDRESS));
        String start = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_START_DATE));
        String end = r.getString(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_END_DATE));
        double cost = r.getDouble(r.getColumnIndex(DatabaseContract.Jobs.COLUMN_COST));
        r.close();
        return new Job(title, address, start, end, cost);
    }

    private double getPainterCost(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select " + DatabaseContract.PainterDays.COLUMN_PAINTER +
                "," + DatabaseContract.PainterDays.COLUMN_HOURS + " from " +
                DatabaseContract.PainterDays.TABLE_NAME + " where " +
                DatabaseContract.PainterDays.COLUMN_DATE + " in (select " +
                DatabaseContract.WorkDays._ID + " from " + DatabaseContract.WorkDays.TABLE_NAME +
                " where " + DatabaseContract.WorkDays.COLUMN_JOB + "=" + job + ");", null);
        r.moveToFirst();
        double total = 0;
        while (!r.isAfterLast()) {
            int painter = r.getInt(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_PAINTER));
            double hours = r.getDouble(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_HOURS));
            total += hours * getPainterWage(painter);
            r.moveToNext();
        }
        r.close();
        return total;
    }

    private double getExpensesCost(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r=db.rawQuery("select sum("+ DatabaseContract.Expenses.COLUMN_COST+ ") " +
                "as "+ DatabaseContract.Expenses.COLUMN_COST+
                " from "+ DatabaseContract.Expenses.TABLE_NAME+
                " where "+ DatabaseContract.Expenses.COLUMN_JOB+"="+job+";",null);
        r.moveToFirst();
        double total=r.getDouble(r.getColumnIndex(DatabaseContract.Expenses.COLUMN_COST));
        r.close();
        return total;
    }

    public double getJobTotalCost(int job) {
        return getPainterCost(job)+getExpensesCost(job);
    }

    public Painter getPainterById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Painters.TABLE_NAME +
                " where " + DatabaseContract.Painters._ID +
                "=" + id, null);
        r.moveToFirst();
        int dbId=r.getInt(r.getColumnIndex(DatabaseContract.Painters._ID));
        String name = r.getString(r.getColumnIndex(DatabaseContract.Painters.COLUMN_NAME));
        double wage = r.getDouble(r.getColumnIndex(DatabaseContract.Painters.COLUMN_WAGE));
        r.close();
        return new Painter(dbId,name,wage);
    }

    public double getPainterWage(int painter) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select " + DatabaseContract.Painters.COLUMN_WAGE +
                " from " + DatabaseContract.Painters.TABLE_NAME +
                " where " + DatabaseContract.Painters._ID + "=" + painter + ";", null);
        r.moveToFirst();
        double wage = r.getDouble(r.getColumnIndex(DatabaseContract.Painters.COLUMN_WAGE));
        r.close();
        return wage;
    }

    public int getDaysWorked(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select count(*) from " + DatabaseContract.WorkDays.TABLE_NAME +
                " where " + DatabaseContract.WorkDays.COLUMN_JOB + "=" + job, null);
        r.moveToFirst();
        int days;
        if (r.getCount() > 0 && r.getColumnCount() > 0) {
            days = r.getInt(0);
        } else {
            days = 0;
        }
        r.close();
        return days;
    }

    public boolean insertPainter(Painter painter) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.Painters.COLUMN_NAME, painter.getName());
        c.put(DatabaseContract.Painters.COLUMN_WAGE, painter.getWage());
        try {
            db.insertOrThrow(DatabaseContract.Painters.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public void updatePainter(int id,String name,double wage) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+ DatabaseContract.Painters.TABLE_NAME+
                " set "+ DatabaseContract.Painters.COLUMN_NAME+
                "=\""+name+
                "\","+ DatabaseContract.Painters.COLUMN_WAGE+
                "="+wage+
                " where "+ DatabaseContract.Painters._ID+
                "="+id+";");
    }

    public List<Painter> getAllPainters() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Painters.TABLE_NAME + ";", null);
        r.moveToFirst();
        List<Painter> painters = new ArrayList<>();
        while (!r.isAfterLast()) {
            int id = r.getInt(r.getColumnIndex(DatabaseContract.Painters._ID));
            String name = r.getString(r.getColumnIndex(DatabaseContract.Painters.COLUMN_NAME));
            double wage = r.getDouble(r.getColumnIndex(DatabaseContract.Painters.COLUMN_WAGE));
            painters.add(new Painter(id, name, wage));
            r.moveToNext();
        }
        r.close();
        return painters;
    }

    public List<Painter> getPaintersOnJob(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Painters.TABLE_NAME +
                " where " + DatabaseContract.Painters._ID + " in (select " +
                DatabaseContract.JobPainters.COLUMN_PAINTER + " from " +
                DatabaseContract.JobPainters.TABLE_NAME + " where " +
                DatabaseContract.JobPainters.COLUMN_JOB + "=" + job + ");", null);
        r.moveToFirst();
        List<Painter> painters = new ArrayList<>();
        while (!r.isAfterLast()) {
            int id = r.getInt(r.getColumnIndex(DatabaseContract.Painters._ID));
            String name = r.getString(r.getColumnIndex(DatabaseContract.Painters.COLUMN_NAME));
            double wage = r.getDouble(r.getColumnIndex(DatabaseContract.Painters.COLUMN_WAGE));
            painters.add(new Painter(id, name, wage));
            r.moveToNext();
        }
        r.close();
        return painters;
    }

    public List<Painter> getPaintersNotOnJob(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.Painters.TABLE_NAME +
                " where " + DatabaseContract.Painters._ID + " not in (select " +
                DatabaseContract.JobPainters.COLUMN_PAINTER + " from " +
                DatabaseContract.JobPainters.TABLE_NAME + " where " +
                DatabaseContract.JobPainters.COLUMN_JOB + "=" + job + ");", null);
        r.moveToFirst();
        List<Painter> painters = new ArrayList<>();
        while (!r.isAfterLast()) {
            int id = r.getInt(r.getColumnIndex(DatabaseContract.Painters._ID));
            String name = r.getString(r.getColumnIndex(DatabaseContract.Painters.COLUMN_NAME));
            double wage = r.getDouble(r.getColumnIndex(DatabaseContract.Painters.COLUMN_WAGE));
            painters.add(new Painter(id, name, wage));
            r.moveToNext();
        }
        r.close();
        return painters;
    }

    public boolean insertJobPainter(int job, int painter) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.JobPainters.COLUMN_JOB, job);
        c.put(DatabaseContract.JobPainters.COLUMN_PAINTER, painter);
        try {
            db.insertOrThrow(DatabaseContract.JobPainters.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public boolean insertJobPainter(int job, Painter painter) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.JobPainters.COLUMN_JOB, job);
        c.put(DatabaseContract.JobPainters.COLUMN_PAINTER, painter.getId());
        try {
            db.insertOrThrow(DatabaseContract.JobPainters.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public boolean insertJobPainters(int job, List<Painter> painters) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        for (Painter painter : painters) {
            ContentValues c = new ContentValues();
            c.put(DatabaseContract.JobPainters.COLUMN_JOB, job);
            c.put(DatabaseContract.JobPainters.COLUMN_PAINTER, painter.getId());
            try {
                db.insertOrThrow(DatabaseContract.JobPainters.TABLE_NAME, null, c);
            } catch (SQLException e) {
                e.printStackTrace();
                inserted = false;
            }
        }
        return inserted;
    }

    public boolean isWorkDayToday(int job) {
        boolean exists = false;
        String dateString = df.getDMYString();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select count(*) from " + DatabaseContract.WorkDays.TABLE_NAME +
                " where " + DatabaseContract.WorkDays.COLUMN_JOB +
                "=" + job + " and " + DatabaseContract.WorkDays.COLUMN_DATE +
                "=\"" + dateString + "\";", null);
        r.moveToFirst();
        if (r.getCount() > 0 && r.getColumnCount() > 0) {
            exists = r.getInt(0) > 0;
        }
        r.close();
        return exists;
    }

    public boolean insertNewWorkDay(int job) {
        boolean inserted = true;
        String dateString = df.getDMYString();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.WorkDays.COLUMN_JOB, job);
        c.put(DatabaseContract.WorkDays.COLUMN_DATE, dateString);
        try {
            db.insertOrThrow(DatabaseContract.WorkDays.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public int getWorkDayId(int job) {
        String dateString = df.getDMYString();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from " + DatabaseContract.WorkDays.TABLE_NAME +
                " where " + DatabaseContract.WorkDays.COLUMN_JOB +
                "=" + job + " and " + DatabaseContract.WorkDays.COLUMN_DATE +
                "=\"" + dateString + "\";", null);
        r.moveToFirst();
        int id=r.getInt(r.getColumnIndex(DatabaseContract.WorkDays._ID));
        r.close();
        return id;
    }

    public boolean insertPainterDay(Painter painter, WorkDay workDay, double hours) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.PainterDays.COLUMN_PAINTER, painter.getId());
        c.put(DatabaseContract.PainterDays.COLUMN_DATE, workDay.getId());
        c.put(DatabaseContract.PainterDays.COLUMN_HOURS, hours);
        try {
            db.insertOrThrow(DatabaseContract.PainterDays.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public boolean insertPainterDay(Painter painter, int workDay, double hours) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.PainterDays.COLUMN_PAINTER, painter.getId());
        c.put(DatabaseContract.PainterDays.COLUMN_DATE, workDay);
        c.put(DatabaseContract.PainterDays.COLUMN_HOURS, hours);
        try {
            db.insertOrThrow(DatabaseContract.PainterDays.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public boolean insertPainterDay(int painter, int workDay, double hours) {
        boolean inserted = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.PainterDays.COLUMN_PAINTER, painter);
        c.put(DatabaseContract.PainterDays.COLUMN_DATE, workDay);
        c.put(DatabaseContract.PainterDays.COLUMN_HOURS, hours);
        try {
            db.insertOrThrow(DatabaseContract.PainterDays.TABLE_NAME, null, c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted = false;
        }
        return inserted;
    }

    public List<PainterDay> getPainterDays(int job, int painter) {
        List<PainterDay> days=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r=db.rawQuery("select "+ DatabaseContract.PainterDays.COLUMN_DATE+
                ", sum("+ DatabaseContract.PainterDays.COLUMN_HOURS+
                ") as "+ DatabaseContract.PainterDays.COLUMN_HOURS+
                " from "+ DatabaseContract.PainterDays.TABLE_NAME+" " +
                "where "+ DatabaseContract.PainterDays.COLUMN_PAINTER+"="+painter+
                " and "+ DatabaseContract.PainterDays.COLUMN_DATE+" in " +
                "(select "+ DatabaseContract.WorkDays._ID+
                " from "+ DatabaseContract.WorkDays.TABLE_NAME+
                " where "+ DatabaseContract.WorkDays.COLUMN_JOB+"="+job+") " +
                "group by "+ DatabaseContract.PainterDays.COLUMN_DATE+";",null);
        r.moveToFirst();
        while (!r.isAfterLast()) {
            int workDay=r.getInt(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_DATE));
            double hours=r.getDouble(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_HOURS));
            Painter p=getPainterById(painter);
            Date date=getWorkDate(workDay);
            days.add(new PainterDay(p,hours,date));
            r.moveToNext();
        }
        r.close();
        return days;
    }

    public Date getWorkDate(int workDay) {
        DateFormatter df=new DateFormatter();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r=db.rawQuery("select "+ DatabaseContract.WorkDays.COLUMN_DATE+
                " from "+ DatabaseContract.WorkDays.TABLE_NAME+
                " where "+ DatabaseContract.WorkDays._ID+
                "="+workDay+";",null);
        r.moveToFirst();
        String dateString=r.getString(r.getColumnIndex(DatabaseContract.WorkDays.COLUMN_DATE));
        r.close();
        return df.getDate(dateString);
    }

    public void markJobCompleted(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update " + DatabaseContract.Jobs.TABLE_NAME +
                " set " + DatabaseContract.Jobs.COLUMN_COMPLETED +
                "=1 where " + DatabaseContract.Jobs._ID + "=" + job + ";");
        updateEndDate(job);
    }

    public void updateEndDate(int job) {
        SQLiteDatabase db = this.getReadableDatabase();
        String dateString='"'+df.getDMYString()+'"';
        db.execSQL("update " + DatabaseContract.Jobs.TABLE_NAME +
                " set " + DatabaseContract.Jobs.COLUMN_END_DATE +
                "=" + dateString +
                " where " + DatabaseContract.Jobs._ID + "=" + job + ";");
    }

    public List<WorkDay> getWorkDays(int job) {
        List<WorkDay> days=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor r=db.rawQuery("select "+ DatabaseContract.WorkDays._ID+", "+
                DatabaseContract.WorkDays.COLUMN_DATE+
                " from "+ DatabaseContract.WorkDays.TABLE_NAME+
                " where "+ DatabaseContract.WorkDays.COLUMN_JOB+"="+job+";",null);
        r.moveToFirst();
        while(!r.isAfterLast()) {
            int id=r.getInt(r.getColumnIndex(DatabaseContract.WorkDays._ID));
            days.add(new WorkDay(getPainterDays(id),df.getDate(
                    r.getString(r.getColumnIndex(DatabaseContract.WorkDays.COLUMN_DATE)))));
            r.moveToNext();
        }
        r.close();
        return days;
    }

    public List<PainterDay> getPainterDays(int workDay) {
        List<PainterDay> days=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor r=db.rawQuery("select "+ DatabaseContract.PainterDays.COLUMN_PAINTER+
                ",sum("+ DatabaseContract.PainterDays.COLUMN_HOURS+
                ") as "+ DatabaseContract.PainterDays.COLUMN_HOURS+
                " from "+ DatabaseContract.PainterDays.TABLE_NAME+
                " where "+ DatabaseContract.PainterDays.COLUMN_DATE+
                "="+workDay+" group by "+ DatabaseContract.PainterDays.COLUMN_DATE+
                ","+ DatabaseContract.PainterDays.COLUMN_PAINTER+";",null);
        r.moveToFirst();
        while (!r.isAfterLast()) {
            int painterId=r.getInt(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_PAINTER));
            double hours=r.getDouble(r.getColumnIndex(DatabaseContract.PainterDays.COLUMN_HOURS));

            Painter p=getPainterById(painterId);
            Date date=getWorkDate(workDay);

            days.add(new PainterDay(p,hours,date));
            r.moveToNext();
        }
        r.close();
        return days;
    }

    public List<Expense> getExpenses(int job) {
        List<Expense> expenses=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor r=db.rawQuery("select * from "+ DatabaseContract.Expenses.TABLE_NAME+
                " where "+ DatabaseContract.Expenses.COLUMN_JOB+
                "="+job+";",null);
        r.moveToFirst();
        while(!r.isAfterLast()) {
            int id=r.getInt(r.getColumnIndex(DatabaseContract.Expenses._ID));
            String name=r.getString(r.getColumnIndex(DatabaseContract.Expenses.COLUMN_NAME));
            double cost=r.getDouble(r.getColumnIndex(DatabaseContract.Expenses.COLUMN_COST));

            expenses.add(new Expense(id,name,cost));

            r.moveToNext();
        }
        r.close();
        return expenses;
    }

    public boolean insertExpense(Expense expense, int job) {
        boolean inserted=true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DatabaseContract.Expenses.COLUMN_NAME,expense.getName());
        c.put(DatabaseContract.Expenses.COLUMN_COST,expense.getCost());
        c.put(DatabaseContract.Expenses.COLUMN_JOB,job);
        try {
            db.insertOrThrow(DatabaseContract.Expenses.TABLE_NAME,null,c);
        } catch (SQLException e) {
            e.printStackTrace();
            inserted=false;
        }
        return inserted;
    }

}
