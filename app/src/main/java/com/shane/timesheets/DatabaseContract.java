package com.shane.timesheets;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String COUNT="count";

    public DatabaseContract() {
    }

    public static abstract class Painters implements BaseColumns {
        public static final String TABLE_NAME = "painters";
        public static final String COLUMN_NAME = "painter_name";
        public static final String COLUMN_WAGE = "wage";
        public static final String COLUMN_DELETED = "deleted";
        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text unique, " +
                COLUMN_WAGE + " real, " +
                COLUMN_DELETED + " integer check(" + COLUMN_DELETED +
                " in (0,1)) default 0);";
    }

    public static abstract class WorkDays implements BaseColumns {
        public static final String TABLE_NAME = "work_days";
        public static final String COLUMN_DATE = "work_date";
        public static final String COLUMN_JOB = "job";
        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_DATE + " text not null, " +
                COLUMN_JOB + " integer, " +
                "foreign key(" + COLUMN_JOB + ") references " + Jobs.TABLE_NAME +
                "(" + Jobs._ID + "));";
    }

    public static abstract class Jobs implements BaseColumns {
        public static final String TABLE_NAME = "jobs";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_TITLE + " text not null, " +
                COLUMN_START_DATE + " text not null, " +
                COLUMN_COMPLETED + " integer not null check(" + COLUMN_COMPLETED +
                " in (0,1)) default 0, " +
                COLUMN_ADDRESS + " text, " +
                COLUMN_END_DATE + " text, " +
                COLUMN_COST + " real);";
    }

    public static abstract class PainterDays implements BaseColumns {
        public static final String TABLE_NAME = "painter_days";
        public static final String COLUMN_PAINTER = "painter";
        public static final String COLUMN_DATE = "work_day";
        public static final String COLUMN_HOURS = "hours";
        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_PAINTER + " integer, " +
                COLUMN_DATE + " integer, " +
                COLUMN_HOURS + " real, " +
                "foreign key(" + COLUMN_PAINTER + ") references " + Painters.TABLE_NAME +
                "(" + Painters._ID + "), " +
                "foreign key(" + COLUMN_DATE + ") references " + WorkDays.TABLE_NAME +
                "(" + WorkDays._ID + "));";
    }

    public static abstract class JobPainters implements BaseColumns {
        public static final String TABLE_NAME = "job_painters";
        public static final String COLUMN_PAINTER = "painter";
        public static final String COLUMN_JOB = "job";
        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_PAINTER + " integer, " +
                COLUMN_JOB + " integer, " +
                "foreign key(" + COLUMN_PAINTER + ") references " + Painters.TABLE_NAME +
                "(" + Painters._ID + "), " +
                "foreign key(" + COLUMN_JOB + ") references " + Jobs.TABLE_NAME +
                "(" + Jobs._ID + "));";
    }
}
