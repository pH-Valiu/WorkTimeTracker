package com.worktimetracker.DataClasses;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Time extends Number implements Comparable<Time>{
    private final int hours;
    private final int minutes;
    private final int seconds;
    public int hours(){return hours;}
    public int minutes(){return minutes;}
    public int seconds(){return seconds;}
    public Time(int hours, int minutes, int seconds){this.hours=hours; this.minutes=minutes; this.seconds=seconds;}
    public static Time now(){
        ZonedDateTime z = ZonedDateTime.now(ZoneId.systemDefault());
        return new Time(z.getHour(), z.getMinute(), z.getSecond());
    }

    /**
     * hh for hours, mm for minutes, ss for seconds
     * @param format
     * @return date object
     */
    public static Time fromString(String date, String format){
        String hours = date.substring(format.indexOf("hh"), format.indexOf("hh")+2);
        String minutes = date.substring(format.indexOf("mm"), format.indexOf("mm")+2);
        String seconds = date.substring(format.indexOf("ss"), format.indexOf("ss")+2);

        return new Time(Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds));
    }

    /**
     * hh for hours, mm for minutes, ss for seconds
     * @param format
     * @return
     */
    public String toString(String format){
        format = format.replace("hh", String.valueOf(hours).length() == 1 ? "0"+String.valueOf(hours) : String.valueOf(hours));
        format = format.replace("mm", String.valueOf(minutes).length() == 1 ? "0"+String.valueOf(minutes) : String.valueOf(minutes));
        return format.replace("ss", String.valueOf(seconds).length() == 1 ? "0"+String.valueOf(seconds) : String.valueOf(seconds));
    }

    /**
     * default to string method using format 'hh:mm:ss'
     */
    @Override
    public String toString(){
        return toString("hh:mm:ss");
    }

    @Override
    public int compareTo(Time o) {
        if(this.hours > o.hours){
            return 1;
        }else if(this.hours < o.hours){
            return -1;
        }else{
            if(this.minutes > o.minutes){
                return 1;
            }else if(this.minutes < o.minutes){
                return -1;
            }else{
                if(this.seconds > o.seconds){
                    return 1;
                }else if(this.seconds < o.seconds){
                    return -1;
                }else{
                    return 0;
                }
            }
        }
    }

    /**
     * Return time as total seconds count
     * @return
     */
    public int toNumber(){
        return ( seconds + (minutes*60) + (hours*60*60) );
    }

    @Override
    public int intValue() {
        return toNumber();
    }
    @Override
    public long longValue() {
        return (long) toNumber();
    }
    @Override
    public float floatValue() {
        return (float) toNumber();
    }
    @Override
    public double doubleValue() {
        return (double) toNumber();
    }

    public static StringConverter<Number> getStringConverter(){
        return new StringConverter<Number>() {

            @Override
            public String toString(Number object) {
                int totalSeconds = object.intValue();
                int h = ((totalSeconds / 60) / 60);
                int m = ((totalSeconds - (h*60*60)) / 60); 
                int s = (totalSeconds - (h*60*60) - (m*60));

                return new Time(h, m, s).toString("hh:mm:ss");
            }

            @Override
            public Number fromString(String string) {
                return Time.fromString(string, "hh:mm:ss").toNumber();
            }
        };
    }

    public Time plusTime(int hours, int minutes, int seconds){
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        while(minutes >= 60){
            minutes -= 60;
            hours++;
        }
        while(hours >= 24){
            hours -= 24;
        }

        int newSeconds = this.seconds;
        int newMinutes = this.minutes;
        int newHours = this.hours;

        if((newSeconds + seconds) >= 60){
            newSeconds = (newSeconds + seconds) % 60;
            newMinutes++;
        }else{
            newSeconds += seconds;
        }

        if((newMinutes + minutes) >= 60){
            newMinutes = (newMinutes + minutes) % 60;
            newHours++;
        }else{
            newMinutes += minutes;
        }

        if((newHours + hours) >= 24){
            newHours = (newHours + hours) % 24;
        }else{
            newHours += hours;
        }

        return new Time(newHours, newMinutes, newSeconds);
    }

    public Time plusTime(Time other){
        return this.plusTime(other.hours, other.minutes, other.seconds);
    }

    public Time minusTime(int hours, int minutes, int seconds){
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        while(minutes >= 60){
            minutes -= 60;
            hours++;
        }
        while(hours >= 24){
            hours -= 24;
        }

        int newSeconds = this.seconds;
        int newMinutes = this.minutes;
        int newHours = this.hours;

        if((newSeconds - seconds) < 0){
            newSeconds = 60 - (seconds - newSeconds);
            newMinutes--;
        }else{
            newSeconds -= seconds;
        }

        if((newMinutes - minutes) < 0){
            newMinutes = 60 - (minutes - newMinutes);
            newHours--;
        }else{
            newMinutes -= minutes;
        }

        if((newHours - hours) < 0){
            newHours = 24 - (hours - newHours);
        }else{
            newHours -= hours;
        }

        return new Time(newHours, newMinutes, newSeconds);
    }

    public Time minusTime(Time other){
        return minusTime(other.hours, other.minutes, other.seconds);
    }
}
