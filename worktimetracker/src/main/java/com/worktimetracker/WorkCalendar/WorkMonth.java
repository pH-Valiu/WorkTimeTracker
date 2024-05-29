package com.worktimetracker.WorkCalendar;

import java.util.List;

import com.worktimetracker.ANSI_COLORS;
import com.worktimetracker.Chart.MonthChart;
import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.Period;
import com.worktimetracker.DataClasses.WorkSession;

public class WorkMonth extends AbstractWorkCalendarFrame{

    public WorkMonth(List<WorkSession> sessions, Date date){
        super(sessions, date);
    }
    


    /**
     * Calculate the total period already worked off
     * @return
     */
    @Override
    public Period getWorkedOffTotal(){
        return sessions.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
    }

    /**
     * Calculate the distribution for already worked off labour in each week
     * @return
     */
    @Override
    public Period[] getDistributionForWorkedOff(){
        //pre-create the array
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0)
        };

        // Fill the array
        for (int i = 0; i < workLoadPerWeek.length; i++) {
            workLoadPerWeek[i] = getWorkedOffByWeek(i+1);
        }

        //return array
        return workLoadPerWeek;
    }

    /**
     * Calculate and create string depiciting the distribution for already worked off labour each week.
     * And how it correlates to the optimal distribution of the total work load
     * @param totalWorkLoad
     * @return
     */
    @Override
    public String getDistributionForWorkedOffOfOptimalAsString(Period totalWorkLoad){
        Period[] periodsPerWeek = getDistributionForWorkedOff();
        Period[] optWorkPerWeek = getOptimalDistributionForWorkLoad(totalWorkLoad);
        String s = "\t1.\t|\t2.\t|\t3.\t|\t4.\t|\t5\t|\t6\t|\n";

        //create string with green: accomplished, red: missed
        for (int i = 0; i < periodsPerWeek.length; i++) {
            if(periodsPerWeek[i].compareTo(optWorkPerWeek[i]) >= 0){
                s += "    " + periodsPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_GREEN) + "\t|";
            }else{
                s += "    " + periodsPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_RED)   + "\t|";
            }
        }
        s += " <- "+ANSI_COLORS.ANSI_GREEN+"# Accomplished"+ANSI_COLORS.ANSI_RESET+", "+ANSI_COLORS.ANSI_RED+"# Missed"+ANSI_COLORS.ANSI_RESET;
        s += "\n";
        
        //add the optimal distribution
        s += getOptimalDistributionForWorkLoadAddonAsString(totalWorkLoad);
        return s;
    }

    /**
     * Calculates the distribution of the given workload on the remaining weeks, based on the number of days each of those weeks have
     * @param workLoad
     * @return array <weekNr(-1) -> workload>
     */
    @Override
    public Period[] getDistributionForRemainingWorkLoad(Period workLoad){
        
        //calculate the remaining days (and current day (+1))
        int remainingDays = this.date.getTotalDaysOfMonth() - this.date.day() + 1;

        //calculate work load per day by dividing the total work load with the #days left
        Period workLoadPerDay = workLoad.divideBy(remainingDays);
        
        //array to store the periods 
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0)
        };

        //loop for each reamining day where i is the day
        for (int i = this.date.day(); i < (this.date.day() + remainingDays); i++) {
            Date dateI = new Date(this.date.year(), this.date.month(), i);

            //get the week number based on the days
            int weekNumber = calculateWeekNrBasedOnWeekAndMonth(dateI.getDayOfWeek(), dateI.day());
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[(weekNumber - 1)] = workLoadPerWeek[(weekNumber - 1)].add(workLoadPerDay);
        }

        //return array
        return workLoadPerWeek;
    }

    @Override
    public String getDistributionForRemainingWorkLoadAsString(Period remainingWorkLoad){
        Period[] periodsPerWeek = getDistributionForRemainingWorkLoad(remainingWorkLoad);
        String s = "\t1.\t|\t2.\t|\t3.\t|\t4.\t|\t5\t|\t6\t|\n";
        for (int i = 0; i < periodsPerWeek.length; i++) {
            s += "    "+periodsPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_RED)+"\t|";
        }
        return s;
    }

    @Override
    public Period[] getOptimalDistributionForWorkLoad(Period totalWorkLoad){

        //calculate the total days 
        int totalDays = this.date.getTotalDaysOfMonth();

        //calculate work load per day by dividing the total work load with the #days left
        Period workLoadPerDay = totalWorkLoad.divideBy(totalDays);
        
        //array to store the periods momentarily
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0)
        };

        //loop for each reamining day where i is the day
        for (int i = 1; i <= totalDays; i++) {
            Date dateI = new Date(this.date.year(), this.date.month(), i);

            //get the week number based on the days
            int weekNumber = calculateWeekNrBasedOnWeekAndMonth(dateI.getDayOfWeek(), dateI.day());
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[(weekNumber - 1)] = workLoadPerWeek[(weekNumber - 1)].add(workLoadPerDay);
        }


        //return array
        return workLoadPerWeek;
    }

    @Override
    public String getOptimalDistributionForWorkLoadAddonAsString(Period totalWorkLoad){
        Period[] optimalWorkLoadPerWeek = getOptimalDistributionForWorkLoad(totalWorkLoad);
        String s = "";
        for (int i = 0; i < optimalWorkLoadPerWeek.length; i++) {
            if (optimalWorkLoadPerWeek[i].isZero()){
                s += "    ";
            }else{
                s += " of ";
            }
            s += optimalWorkLoadPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_CYAN)+"\t|";
        }
        s += ANSI_COLORS.ANSI_CYAN+" <- Optimal Distribution"+ANSI_COLORS.ANSI_RESET;
        return s;
    }


    public MonthChart getChart(){
        MonthChart chart = new MonthChart(date);

        for (int i = 0; i < sessions.size(); i++) {
            chart.addPeriod(sessions.get(i).start(), Period.getPeriod(sessions.get(i).start().time(), sessions.get(i).end()));
        }

        return chart;
    }
}
