package com.worktimetracker.DataClasses;

import java.util.Calendar;

public class DateTime implements Comparable<DateTime>{
    private Date date;
    private Time time;
    public Date date(){return date;}
    public Time time(){return time;}
    public DateTime(Date date, Time time){this.date = date; this.time = time;}
    public DateTime(int year, int month, int day, int hours, int minutes, int seconds){
        this(new Date(year, month, day), new Time(hours, minutes, seconds));
    }
    
    /**
     * hh for hours, mm for minutes, ss for seconds
     * DD for day, MM for month, YYYY for year
     * @param format
     * @return DateTime object
     */
    public static DateTime fromString(String datetime, String format){
        String hours = datetime.substring(format.indexOf("hh"), format.indexOf("hh")+2);
        String minutes = datetime.substring(format.indexOf("mm"), format.indexOf("mm")+2);
        String seconds = datetime.substring(format.indexOf("ss"), format.indexOf("ss")+2);
        String day = datetime.substring(format.indexOf("DD"), format.indexOf("DD")+2);
        String month = datetime.substring(format.indexOf("MM"), format.indexOf("MM")+2);
        String year = datetime.substring(format.indexOf("YYYY"), format.indexOf("YYYY")+4);

        return new DateTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds));
    }

    public String toString(String format){
        return time.toString(date.toString(format));
    }

    public String toString(){
        return toString("DD.MM.YYYY hh:mm:ss");
    }

    public static DateTime now(){
        Calendar now = Calendar.getInstance();
        Date date = new Date(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1, now.get(Calendar.DAY_OF_MONTH));
        Time time = new Time(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));

        return new DateTime(date, time);
    }

    public String toNumber(){
        Calendar c = Calendar.getInstance();
        c.set(date.year(), date.month()-1, date.day(), time.hours(),time.minutes(), time.seconds());
        return String.valueOf(c.getTimeInMillis());
    }

    @Override
    public int compareTo(DateTime o) {
        if(this.date.compareTo(o.date) == 0){
            return this.time.compareTo(o.time);
        }else{
            return this.date.compareTo(o.date);
        }
    }

    
} 
