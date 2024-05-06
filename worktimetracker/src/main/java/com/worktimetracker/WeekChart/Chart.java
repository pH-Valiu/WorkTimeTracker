package com.worktimetracker.WeekChart;

import java.util.List;

import com.worktimetracker.DataClasses.*;

public class Chart {
    private TimeSlot[][] table;
    private final List<Pair<Integer, Integer>> daysOfSelectedWeek;

    public Chart(List<Pair<Integer, Integer>> daysOfSelectedWeek){
        this.daysOfSelectedWeek = daysOfSelectedWeek;

        //Create and fill the table based on number of days needed
        table = new TimeSlot[daysOfSelectedWeek.size()][24];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = new TimeSlot(
                    daysOfSelectedWeek.get(i).first(), 
                    daysOfSelectedWeek.get(i).second(), 
                    j
                );
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

        while( daysOfSelectedWeek.contains(new Pair<>(dateVar.date().day(), dateVar.date().getDayOfWeek()))
                && (remainingPeriod.hours() != 0 || remainingPeriod.minutes() != 0 ) )
        {
            //update table TimeSlot object by increasing its usage
            //using hours directly as index is correct!
            table[dateVar.date().getDayOfWeek()-1][dateVar.time().hours()].increaseUsage();

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
