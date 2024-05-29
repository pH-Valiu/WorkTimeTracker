package com.worktimetracker.WorkCalendar;

import com.worktimetracker.ANSI_COLORS;
import com.worktimetracker.Chart.WeekChart;
import com.worktimetracker.DataClasses.*;
import com.worktimetracker.DataClasses.Date;

import java.util.*;

public class WorkWeek extends AbstractWorkCalendarFrame {
    
    private final int weekNr;
    //mapping between dayOfMonth --> dayOfWeek (e.g. 23. <-> 2.    meaning 23rd is a tuesday)
    private final List<Pair<Integer, Integer>> daysOfWeekList;

    public WorkWeek(List<WorkSession> sessions, int weekNr, Date date){
        super(sessions, date);
        this.weekNr = weekNr;
        
        daysOfWeekList = new ArrayList<>();
        boolean weekFound = false;
        for (int i = 1; i <= date.getTotalDaysOfMonth(); i++) {
            Date day = new Date(date.year(), date.month(), i);
            if (calculateWeekNrBasedOnWeekAndMonth(day.getDayOfWeek(), day.day()) == weekNr){
                daysOfWeekList.add(new Pair<>(i, day.getDayOfWeek()));
                weekFound = true;
            }else if(weekFound){
                break;
            }
        }
    }

    @Override
    public Period getWorkedOffTotal() {
        return getWorkedOffByWeek(weekNr);
    }
    
    @Override
    protected Period[] getDistributionForWorkedOff() {
        Period[] workLoadPerDay = new Period[]{
            new Period(0,0,0), new Period(0,0,0), new Period(0,0,0),
            new Period(0,0,0), new Period(0,0,0), new Period(0,0,0),
            new Period(0,0,0)
        };

        for (int i = 0; i < daysOfWeekList.size(); i++) {
            workLoadPerDay[daysOfWeekList.get(i).second()-1] = getWorkedOffByDay(daysOfWeekList.get(i).first());
        }

        return workLoadPerDay;
    }

    @Override
    public String getDistributionForWorkedOffOfOptimalAsString(Period totalWorkLoad) {
        Period[] workedOffPerDay = getDistributionForWorkedOff();
        Period[] optWorkPerDay = getOptimalDistributionForWorkLoad(totalWorkLoad);
        String s = "         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |\n";

        //create string with green: accomplished, red: missed
        s+= " ";
        for (int i = 0; i < workedOffPerDay.length; i++) {
            if(workedOffPerDay[i].compareTo(optWorkPerDay[i]) >= 0){
                s += "     " + workedOffPerDay[i].toStringOrDash(ANSI_COLORS.ANSI_GREEN) + "     |";
            }else{
                s += "     " + workedOffPerDay[i].toStringOrDash(ANSI_COLORS.ANSI_RED)   + "     |";
            }
        }
        s += "\n";
        
        //add the optimal distribution
        s += getOptimalDistributionForWorkLoadAddonAsString(totalWorkLoad);
        return s;
    }

    public String getDistributionChart(){
        WeekChart chart = new WeekChart(daysOfWeekList);
        String s = "";
        s += "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n";
        for (WorkSession workSession : mapSessionToWeek.keySet()) {
            chart.addPeriod(workSession.start(), Period.getPeriod(workSession.start().time(), workSession.end()));
        }

        s += chart.toString();
        s += "  ";
        for (int i = 0; i < daysOfWeekList.size(); i++) {
            Date dateI = new Date(date.year(), date.month(), daysOfWeekList.get(i).first());
            s += "       "+dateI.getDayOfWeekAsString()+".         ";
        }
        s += "\n";
        s += "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n";
        return s;
    }


    @Override
    public String getOptimalDistributionForWorkLoadAddonAsString(Period totalWorkLoad) {
        Period[] optimalWorkLoadPerDay = getOptimalDistributionForWorkLoad(totalWorkLoad);
        String s = " ";
        for (int i = 0; i < optimalWorkLoadPerDay.length; i++) {
            if (optimalWorkLoadPerDay[i].isZero()){
                s += "     ";
            }else{
                s += "  of ";
            }
            s += optimalWorkLoadPerDay[i].toStringOrDash(ANSI_COLORS.ANSI_CYAN)+"     |";
        }
        return s;    
    }

    @Override
    protected Period[] getOptimalDistributionForWorkLoad(Period totalWorkLoad) {
        if(daysOfWeekList.isEmpty()) throw new IllegalArgumentException("Week contains no days");
        Period perDayWorkLoad = totalWorkLoad.divideBy(daysOfWeekList.size());
        Period[] workLoadPerDay = new Period[]{
            new Period(0,0,0), new Period(0,0,0), new Period(0,0,0),
            new Period(0,0,0), new Period(0,0,0), new Period(0,0,0),
            new Period(0,0,0)
        };

        for (int i = 0; i < daysOfWeekList.size(); i++) {
            workLoadPerDay[daysOfWeekList.get(i).second()-1] = perDayWorkLoad;
        }

        return workLoadPerDay;

    }

    @Override
    public String getDistributionForRemainingWorkLoadAsString(Period remainingWorkLoad) {
        Period[] periodsPerWeek = getDistributionForRemainingWorkLoad(remainingWorkLoad);
        String s = "         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |\n";
        s += " ";
        for (int i = 0; i < periodsPerWeek.length; i++) {
            s += "     "+periodsPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_RED)+"     |";
        }
        return s;
    }

    @Override
    protected Period[] getDistributionForRemainingWorkLoad(Period remainingWorkLoad) {
        int dayOfMonth = date.day();
        int dayOfWeek = date.getDayOfWeek();
        
        //calculate the remaining days (and current day (+1) (intrinsic due to index))
        int remainingDays = daysOfWeekList.size() - daysOfWeekList.indexOf(new Pair<>(dayOfMonth, dayOfWeek));
        System.out.println("remaining Days: "+ remainingDays);

        //calculate work load per day by dividing the total work load with the #days left
        Period workLoadPerDay = remainingWorkLoad.divideBy(remainingDays);
        
        //array to store the periods 
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0, 0, 0)
        };

        //loop for each reamining day where i is the day
        for (int i = date.day(); i < (date.day() + remainingDays); i++) {
            Date dateI = new Date(date.year(), date.month(), i);

            //get the week number based on the days
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[dateI.getDayOfWeek()-1] = workLoadPerWeek[dateI.getDayOfWeek()-1].add(workLoadPerDay);
        }

        //return array
        return workLoadPerWeek;
    }
}
