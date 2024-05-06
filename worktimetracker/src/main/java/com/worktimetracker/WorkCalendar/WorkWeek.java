package com.worktimetracker.WorkCalendar;

import com.worktimetracker.ANSI_COLORS;
import com.worktimetracker.DataClasses.*;
import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.WeekChart.Chart;

import java.util.*;

public class WorkWeek extends AbstractWorkCalendarFrame {
    
    private final int weekNr;
    //mapping between dayOfMonth --> dayOfWeek (e.g. 23. <-> 2.    meaning 23rd is a tuesday)
    private final List<Pair<Integer, Integer>> daysOfWeekList;

    public WorkWeek(List<WorkSession> sessions, int weekNr){
        super(sessions);
        this.weekNr = weekNr;
        
        DateTime now = DateTime.now();
        daysOfWeekList = new ArrayList<>();
        boolean weekFound = false;
        for (int i = 1; i <= now.date().getTotalDaysOfMonth(); i++) {
            Date day = new Date(now.date().year(), now.date().month(), i);
            int dayOfWeek = day.getDayOfWeek();
            if (calculateWeekNrBasedOnWeekAndMonth(dayOfWeek, day.day()) == weekNr){
                daysOfWeekList.add(new Pair<>(i, dayOfWeek));
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
        s += " <- "+ANSI_COLORS.ANSI_GREEN+"# Accomplished"+ANSI_COLORS.ANSI_RESET+", "+ANSI_COLORS.ANSI_RED+"# Missed"+ANSI_COLORS.ANSI_RESET;
        s += "\n";
        
        //add the optimal distribution
        s += getOptimalDistributionForWorkLoadAddonAsString(totalWorkLoad);
        return s;
    }

    public String getDistributionChart(){
        Chart chart = new Chart(daysOfWeekList);
        String s = "";
        s += "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n";
        for (WorkSession workSession : mapSessionToWeek.keySet()) {
            chart.addPeriod(workSession.start(), Period.getPeriod(workSession.start().time(), workSession.end()));
        }

        s += chart.toString();
        s += "  ";
        for (int i = 0; i < daysOfWeekList.size(); i++) {
            DateTime now = DateTime.now();
            Date date = new Date(now.date().year(), now.date().month(), daysOfWeekList.get(i).first());
            s += "       "+date.getDayOfWeekAsString()+".         ";
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
        s += ANSI_COLORS.ANSI_CYAN+" <- Optimal Distribution"+ANSI_COLORS.ANSI_RESET;
        return s;    
    }

    @Override
    protected Period[] getOptimalDistributionForWorkLoad(Period totalWorkLoad) {
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
        Date now = DateTime.now().date();
        int dayOfMonth = now.day();
        int dayOfWeek = now.getDayOfWeek();
        
        //calculate the remaining days (and current day (+1))
        int remainingDays = daysOfWeekList.size() - daysOfWeekList.indexOf(new Pair<>(dayOfMonth, dayOfWeek)) - 1;

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
        for (int i = now.day(); i < (now.day() + remainingDays); i++) {
            Date date = new Date(now.year(), now.month(), i);

            //get the week number based on the days
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[date.getDayOfWeek()-1] = workLoadPerWeek[date.getDayOfWeek()-1].add(workLoadPerDay);
        }

        //return array
        return workLoadPerWeek;
    }
}
