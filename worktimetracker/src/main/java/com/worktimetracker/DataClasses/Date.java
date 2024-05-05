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
    
}
