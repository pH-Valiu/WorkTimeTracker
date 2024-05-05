package com.worktimetracker.DataClasses;

public record WorkSession(DateTime start, Time end) {
    /**
     * Calculates the time @see {@link Period} between the start and end time points.
     * @return
     */
    public Period getWorkTime(){
        return Period.getPeriod(start.time(), end);
    }

    /**
     * Returns "start.toString() end.toString()"
     */
    @Override
    public String toString(){
        return start.toString() + "  "+ end.toString();
    }
    
}
