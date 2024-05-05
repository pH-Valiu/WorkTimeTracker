package com.worktimetracker.DataClasses;

public record WorkSession(DateTime start, Time end) {
    public Period getWorkTime(){
        return Period.getPeriod(start.time(), end);
    }
    @Override
    public String toString(){
        return start.toString() + "  "+ end.toString();
    }
}
