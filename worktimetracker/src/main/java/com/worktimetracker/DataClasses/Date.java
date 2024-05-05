package com.worktimetracker.DataClasses;

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;
    public int year(){return year;}
    public int month(){return month;}
    public int day(){return day;}

    public Date(int year, int month, int day){this.year = year; this.month=month; this.day=day;}

    /**
     * DD for day, MM for month, YYYY for year
     * @param format
     * @return date object
     */
    public static Date fromString(String date, String format){
        String day = date.substring(format.indexOf("DD"), format.indexOf("DD")+2);
        String month = date.substring(format.indexOf("MM"), format.indexOf("MM")+2);
        String year = date.substring(format.indexOf("YYYY"), format.indexOf("YYYY")+4);

        return new Date(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
    }

    /*
     * Is in format "YYYY-MM"
     */    
    public static Date fromYearMonthString(String date){
        String month = date.substring(5, 7);
        String year = date.substring(0, 4);
        return new Date(Integer.valueOf(year), Integer.valueOf(month), 0);
    }

    /**
     * DD for day, MM for month, YYYY for year
     * @param format
     * @return
     */
    public String toString(String format){
        format = format.replace("DD", String.valueOf(day).length() == 1 ? "0"+String.valueOf(day) : String.valueOf(day));
        format = format.replace("MM", String.valueOf(month).length() == 1 ? "0"+String.valueOf(month) : String.valueOf(month));
        return format.replace("YYYY", String.valueOf(year).length() == 3 ? "0"+String.valueOf(year) : String.valueOf(year));
    }
    @Override
    public int compareTo(Date o) {
        if(this.year > o.year){
            return 1;
        }else if(this.year < o.year){
            return -1;
        }else{
            if(this.month > o.month){
                return 1;
            }else if(this.month < o.month){
                return -1;
            }else{
                if(this.day > o.day){
                    return 1;
                }else if(this.day < o.day){
                    return -1;
                }else{
                    return 0;
                }
            }
        }
    }
    public String getMonthAsString(){
        String s = "";
        switch (month) {
            case 1 -> {s="Jan";}
            case 2 -> {s="Feb";}
            case 3 -> {s="Mar";}
            case 4 -> {s="Apr";}
            case 5 -> {s="Mai";}
            case 6 -> {s="Jun";}
            case 7 -> {s="Jul";}
            case 8 -> {s="Aug";}
            case 9 -> {s="Sep";}
            case 10 -> {s="Oct";}
            case 11 -> {s="Nov";}
            case 12 -> {s="Dec";}
        }
        return s;
    }

    /**
     * Use Zellers' method to calculate the day of the week
     * @return 1 -> Monday, 2 -> Tuesday, ...
     */
    public int getDayOfWeek(){
        int day = this.day;
        int month = this.month;
        int year = this.year;


        if (month == 1)
        {
            month = 13;
            year--;
        }
        if (month == 2)
        {
            month = 14;
            year--;
        }
        int q = day;
        int m = month;
        int k = year % 100;
        int j = year / 100;
        int h = q + 13*(m + 1) / 5 + k + k / 4 + j / 4 + 5 * j;
        h = h % 7;
        

        switch (h) {
            case 0: return 6;
            case 1: return 7;
            case 2: return 1;
            case 3: return 2;
            case 4: return 3;
            case 5: return 4;
            case 6: return 5;
        }
        return -1;
    }
    
    public int getTotalDaysOfMonth(){
        switch(this.month){
            case 1, 3, 5, 7, 8, 10, 12 : return 31;
            case 4, 6, 9, 11 : return 30;
            case 2 : {
                if(isLeapYear()){
                    return 28;
                }else{
                    return 27;
                }
            }
        }
        return -1;
    }

    public boolean isLeapYear(){
        boolean isLeapYear = false;

        if (year % 4 == 0){
            isLeapYear = true;

            if (year % 100 == 0){
                if ( year % 400 == 0){
                    isLeapYear = true;
                }else{
                    isLeapYear = false;
                }
            }
        }
        return isLeapYear;
    }
}
