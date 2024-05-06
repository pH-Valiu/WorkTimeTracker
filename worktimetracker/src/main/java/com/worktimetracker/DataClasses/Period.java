package com.worktimetracker.DataClasses;

import com.worktimetracker.ANSI_COLORS;

public record Period(int hours, int minutes, int seconds) implements Comparable<Period>{
    /**
     * Returns the time period between two {@link Time} points
     * @param t1
     * @param t2
     * @return Period
     */
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


    /**
     * Static constructor to get Period object from total seconds count
     * @param totalSeconds
     * @return
     */
    public static Period fromSeconds(long totalSeconds){
        int hoursFraction = (int) ( totalSeconds / 3600 );
        int minutesFraction = (int) ( totalSeconds - ( hoursFraction * 3600 ) ) / 60;
        int secondsFraction = (int) ( totalSeconds - ( hoursFraction * 3600 ) - ( minutesFraction * 60) );
        return new Period(hoursFraction, minutesFraction, secondsFraction);
    }
    /**
     * Adds another period object to this one and returns the addition of both as a new period object
     * @param other
     * @return the addition object
     */
    public Period add(Period other){
        int seconds = this.seconds + other.seconds;
        int minutes = this.minutes + other.minutes;
        int hours = this.hours + other.hours;

        if(seconds >= 60){
            seconds = seconds - 60;
            minutes++;
        }

        if(minutes >= 60){
            minutes = minutes - 60;
            hours++;
        }

        return new Period(hours, minutes, seconds);
    }

    /**
     * Subtracts another period object to this one and returns the subtraction of both as a new period object.
     * It always returns sth > 0
     * @param other
     * @return the subtraction object
     */
    public Period minus(Period other){
        int h1 = this.hashCode();
        int h2 = other.hashCode();
        int res;
        if (h1 > h2){
            res = h1 - h2;
        }else{
            res = h2 - h1;
        }

        return Period.fromSeconds(res);
    }

    /**
     * Subtract from another but it can't go smaller than zero
     * @param other
     * @return
     */
    public Period minusWithLowerEnd(Period other){
        long res = this.hashCode() - other.hashCode();
        if (res < 0){
            res = 0;
        }

        return Period.fromSeconds(res);
    }

    public Period divideBy(int divisor){
        long totalSecondsFraction = this.hashCode() / divisor;

        return Period.fromSeconds(totalSecondsFraction);
    }

    public boolean isZero(){
        return hours == 0 && minutes == 0 && seconds == 0;
    }

    /**
     * Returns "{@link Period#hours}h {@link Period#minutes}min {@link Period#seconds}sec".
     * Example: "2h 30min 12sec"
     */
    @Override
    public String toString(){
        return toString("hhh mmmin sssec", ANSI_COLORS.ANSI_WHITE);
    }

    /**
     * Format based on: hh, mm, ss
     * @param format
     * @return
     */
    public String toString(String format, String color){
        format =  format.replace("ss", String.valueOf(seconds).length() == 1 ? color + "0"+ ANSI_COLORS.ANSI_RESET+colorNumber(seconds, color) : colorNumber(seconds, color));
        format = format.replace("mm", String.valueOf(minutes).length() == 1 ? color + "0"+ ANSI_COLORS.ANSI_RESET+colorNumber(minutes, color) : colorNumber(minutes, color));
        return format.replace("hh", String.valueOf(hours).length() == 1 ? color + "0"+ ANSI_COLORS.ANSI_RESET+colorNumber(hours, color) : colorNumber(hours, color));
    }

    public String toStringOrDash(String color){
        if (isZero()){
            return "---------";
        }else{
            return toString("hhh mmmin", color);
        }
    }

    private String colorNumber(int number, String color){
        return color + String.valueOf(number) + ANSI_COLORS.ANSI_RESET;
    }

    @Override
    public int compareTo(Period other) {
        if (this.hours > other.hours){
            return 1;
        }else if (this.hours < other.hours){
            return -1;
        }else{
            if (this.minutes > other.minutes){
                return 1;
            }else if (this.minutes < other.minutes){
                return -1;
            }else{
                if (this.seconds > other.seconds){
                    return 1;
                }else if (this.seconds < other.seconds){
                    return -1;
                }else{
                    return 0;
                }
            }
        }
    }
  
    @Override
    public int hashCode(){
        return (hours * 3600) + (minutes * 60) + (seconds);
    }
}
