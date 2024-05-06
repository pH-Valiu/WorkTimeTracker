package com.worktimetracker.WeekChart;

import com.worktimetracker.ANSI_COLORS;

public class TimeSlot {
    /**
     * Creates a new TimeSlot and sets its usage status to Usage.FREE
     */
    public TimeSlot() {
        this.usage = Usage.FREE;
    }
    enum Usage{
        FREE, FULL, HALF
    }
    private Usage usage;

    public void increaseUsage(){
        switch (usage) {
            case FREE -> {usage = Usage.HALF;}
            case HALF -> {usage = Usage.FULL;}
            case FULL -> {usage = Usage.FULL;}
        }
    }

    @Override
    public String toString(){
        switch (usage) {
            case FREE -> {return ANSI_COLORS.ANSI_WHITE+"     "+ANSI_COLORS.ANSI_RESET;}
            case HALF -> {return ANSI_COLORS.ANSI_GREEN+"░░░░░"+ANSI_COLORS.ANSI_RESET;}
            case FULL -> {return ANSI_COLORS.ANSI_GREEN+"▒▒▒▒▒"+ANSI_COLORS.ANSI_RESET;}
        }
        return null;
    }
}
