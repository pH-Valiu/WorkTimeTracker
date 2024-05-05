package com.worktimetracker.DataClasses;

public class WorkSession {
    private Date date;
    private Time startTime;
    private Time stopTime;

    public WorkSession(Date date, Time startTime) {
        this.date = date;
        this.startTime = startTime;
        this.stopTime = null;
    }
    public WorkSession(Date date, Time startTime, Time stopTime) {
        this.date = date;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }
    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}
    public Time getStartTime() {return startTime;}
    public void setStartTime(Time startTime) {this.startTime = startTime;}
    public Time getStopTime() {return stopTime;}
    public void setStopTime(Time stopTime) {this.stopTime = stopTime;}
}
