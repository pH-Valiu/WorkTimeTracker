package com.worktimetracker.DataClasses;

public record Period(int hours, int minutes, int seconds) {
    public static Period getPeriod(Time t1, Time t2){
        int hours = t2.hours() - t1.hours();
        int minutes = t2.minutes() - t1.minutes();
        int seconds = t2.seconds() - t1.seconds();

        if(seconds < 0){
            seconds = 60 + seconds;
            minutes--;
        }
        if(minutes < 0){
            minutes = 60 + minutes;
            hours --;
        }
        if(hours < 0){
            hours = 24 + hours;
        }
        return new Period(hours, minutes, seconds);
    }

    public Period add(Period other){
        int hours = this.hours;
        int minutes = this.minutes;
        int seconds = this.seconds;

        seconds += other.seconds;
        if(seconds >= 60){
            seconds = 0;
            minutes++;
        }

        minutes += other.minutes;
        if(minutes >= 60){
            minutes = 0;
            hours++;
        }

        hours += other.hours;

        return new Period(hours, minutes, seconds);
    }

    @Override
    public String toString(){
        return hours+"h "+minutes+"min "+seconds+"sec";
    }
    
}
