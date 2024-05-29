package com.worktimetracker.Chart;


import com.worktimetracker.DataClasses.*;

public class MonthChart {
    private TimeSlot[][] table;
    public TimeSlot[][] getTable() {
        return table;
    }


    private final Date date;

    public MonthChart(Date date){
        this.date = date;

        //Create and fill the table based on number of days needed
        table = new TimeSlot[date.getTotalDaysOfMonth()][24];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = new TimeSlot();
            }
        }
    }

    /**
     * Extremly complex method to put period into the chart and update timeslots accordingly
     * @param start
     * @param period
     */
    public void addPeriod(DateTime start, Period period){
        DateTime dateVar = new DateTime(start);
        Period remainingPeriod = period;
        Period halfAnHourPeriod = new Period(0, 30, 0);

        while( dateVar.date().month() == this.date.month()
                && (remainingPeriod.hours() != 0 || remainingPeriod.minutes() != 0 ) )
        {
            //update table TimeSlot object by increasing its usage
            //using hours directly as index is correct!
            table[dateVar.date().day()-1][dateVar.time().hours()].increaseUsage();

            //increase dateVar by 30min
            //lower remaining period to go through by 30min
            dateVar = dateVar.addPeriod(halfAnHourPeriod);
            remainingPeriod = remainingPeriod.minusWithLowerEnd(halfAnHourPeriod);
        }
        
    }


    @Override
    public String toString(){
        String s = "";
        //"inverse" for-for loop
        for (int j = 0; j < 24; j++) {
            for (int i = 0; i < table.length; i++) {
                if((j-2) % 3 == 0 || j == 0){
                    s += (j < 10 ? "0"+j : j) + ":00 |";
                }else{
                    s += "      |";
                }
                s += " "+table[i][j].toString()+" |     ";
            }
            s += "\n";
        }
        return s;
    }
}
