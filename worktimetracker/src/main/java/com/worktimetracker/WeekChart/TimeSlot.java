package com.worktimetracker.WeekChart;

import com.worktimetracker.ANSI_COLORS;

public class TimeSlot {
    public TimeSlot(int day, int dayOfWeek, int startHour) {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = startHour + 1;
        this.usage = Usage.FREE;
    }
    enum Usage{
        FREE, USED, HALF
    }
    private final int day;
    private final int dayOfWeek;
    private final int startHour;
    private final int endHour;
    private Usage usage;

    public Usage getUsage() {
        return usage;
    }
    public int getDay() {
        return day;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public int getStartHour() {
        return startHour;
    }
    public int getEndHour() {
        return endHour;
    }

    public void increaseUsage(){
        switch (usage) {
            case FREE -> {usage = Usage.HALF;}
            case HALF -> {usage = Usage.USED;}
            case USED -> {usage = Usage.USED;}
        }
    }

    @Override
    public String toString(){
        switch (usage) {
            case FREE -> {return ANSI_COLORS.ANSI_WHITE+"     "+ANSI_COLORS.ANSI_RESET;}
            case HALF -> {return ANSI_COLORS.ANSI_GREEN+"░░░░░"+ANSI_COLORS.ANSI_RESET;}
            case USED -> {return ANSI_COLORS.ANSI_GREEN+"▒▒▒▒▒"+ANSI_COLORS.ANSI_RESET;}
        }
        return null;
    }
}
