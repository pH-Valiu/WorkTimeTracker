package com.worktimetracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Period;
import com.worktimetracker.DataClasses.WorkSession;

public class WorkMonth {

    public WorkMonth(List<WorkSession> sessions){
        this.sessions = sessions;
        this.mapSessionToWeek = new HashMap<>();
        //fill map <session - weekNr>
        for (WorkSession workSession : sessions) {
            mapSessionToWeek.put(workSession, calculateWeekNrBasedOnWeekAndMonth(
                    workSession.start().date().getDayOfWeek(), 
                    workSession.start().date().day()));
        }
         

    }
    private List<WorkSession> sessions;
    public List<WorkSession> getSessions(){return sessions;}
    private Map<WorkSession, Integer> mapSessionToWeek;


    /**
     * Calculate the total period already worked off
     * @return
     */
    public Period getWorkedOffTotal(){
        return sessions.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
    }

    /**
     * Calculate the total period already worked off based on the week
     * @param weekNr
     * @return
     */
    public Period getWorkedOffByWeek(int weekNr){
        List<WorkSession> sessionsOfWeek = mapSessionToWeek.entrySet().stream()
                .filter((entry) -> entry.getValue().equals(weekNr))
                .map((entry) -> entry.getKey())
                .toList();
        return sessionsOfWeek.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
    }

    /**
     * Calculate the distribution for already worked off labour in each week
     * @return
     */
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
    public String getDistributionForWorkedOffOfOptimalAsString(Period totalWorkLoad){
        Period[] periodsPerWeek = getDistributionForWorkedOff();
        Period[] optWorkPerWeek = getOptimalDistributionForMonth(totalWorkLoad);
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
        s += getOptimalDistributionAddonForMonthAsString(totalWorkLoad);
        return s;
    }

    /**
     * Calculates the distribution of the given workload on the remaining weeks, based on the number of days each of those weeks have
     * @param workLoad
     * @return array <weekNr(-1) -> workload>
     */
    public Period[] getDistributionForRemainingDays(Period workLoad){
        Date now = DateTime.now().date();
        
        //calculate the remaining days (and current day (+1))
        int remainingDays = now.getTotalDaysOfMonth() - now.day() + 1;

        //calculate work load per day by dividing the total work load with the #days left
        Period workLoadPerDay = workLoad.divideBy(remainingDays);
        
        //array to store the periods 
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0)
        };

        //loop for each reamining day where i is the day
        for (int i = now.day(); i < (now.day() + remainingDays); i++) {
            Date date = new Date(now.year(), now.month(), i);

            //get the week number based on the days
            int weekNumber = calculateWeekNrBasedOnWeekAndMonth(date.getDayOfWeek(), date.day());
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[(weekNumber - 1)] = workLoadPerWeek[(weekNumber - 1)].add(workLoadPerDay);
        }

        //return array
        return workLoadPerWeek;
    }

    public String getDistributionForRemainingDaysAsString(Period workLoad){
        Period[] periodsPerWeek = getDistributionForRemainingDays(workLoad);
        String s = "\t1.\t|\t2.\t|\t3.\t|\t4.\t|\t5\t|\t6\t|\n";
        for (int i = 0; i < periodsPerWeek.length; i++) {
            s += "    "+periodsPerWeek[i].toStringOrDash(ANSI_COLORS.ANSI_RED)+"\t|";
        }
        return s;
    }

    private int calculateWeekNrBasedOnWeekAndMonth(int weekDay, int monthDay){
        return (int) Math.ceil((monthDay - weekDay) / 7.0) + 1;
    }

    public Period[] getOptimalDistributionForMonth(Period workLoad){
        Date now = DateTime.now().date();

        //calculate the total days 
        int totalDays = now.getTotalDaysOfMonth();

        //calculate work load per day by dividing the total work load with the #days left
        Period workLoadPerDay = workLoad.divideBy(totalDays);
        
        //array to store the periods momentarily
        Period[] workLoadPerWeek = new Period[]{
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0),
            new Period(0,0,0),new Period(0,0,0)
        };

        //loop for each reamining day where i is the day
        for (int i = 1; i <= totalDays; i++) {
            Date date = new Date(now.year(), now.month(), i);

            //get the week number based on the days
            int weekNumber = calculateWeekNrBasedOnWeekAndMonth(date.getDayOfWeek(), date.day());
            
            //add the per day workload to the corresponding week
            workLoadPerWeek[(weekNumber - 1)] = workLoadPerWeek[(weekNumber - 1)].add(workLoadPerDay);
        }


        //return array
        return workLoadPerWeek;
    }

    public String getOptimalDistributionAddonForMonthAsString(Period totalWorkLoad){
        Period[] optimalWorkLoadPerWeek = getOptimalDistributionForMonth(totalWorkLoad);
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


}
